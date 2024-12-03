package org.example.ui;



import jakarta.websocket.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.data.User;
import org.example.ui.practice.MorseCodeOutput;
import org.example.utility.RadioFunctions;
import java.io.*;
import java.net.*;

@ClientEndpoint
public class LiveChatChatRoomController implements MorseCodeOutput {

    // UI elements
    @FXML private Button practiceMenuButton;
    @FXML private TextArea chatLogTextArea;
    @FXML private VBox chatLogVBox;
    @FXML private Button paddleModeButton;
    @FXML private Button straightKeyModeButton;
    @FXML private Slider filterSlider;
    @FXML private Slider frequencySlider;
    @FXML private Button mainMenuButton;
    @FXML private Label roomTitleLabel;
    @FXML private TextField cwInputTextField;
    @FXML private Button sendButton; // Send button for sending messages
    @FXML private RadioFunctions radioFunctions;

    // WebSocket session
    private Session session;

    @FXML
    public void initialize() {
        // Initialize RadioFunctions
        radioFunctions = new RadioFunctions(this);

        // Connect to WebSocket server
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = new URI("ws://localhost:8000/ws/"+ User.getUsername());
            container.connectToServer(this, uri);
        } catch (Exception e) {
            chatLogTextArea.appendText("Error connecting to chat server: " + e.getMessage() + "\n");
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        chatLogTextArea.appendText("Connected to chat server.\n");
    }

    @OnMessage
    public void onMessage(String message) {
        // Display the Morse code message received
        javafx.application.Platform.runLater(() -> {
            chatLogTextArea.appendText("Received (Morse): " + message + "\n");
        });
    }

    @OnClose
    public void onClose() {
        javafx.application.Platform.runLater(() -> {
            chatLogTextArea.appendText("Disconnected from chat server.\n");
        });
    }

    @FXML
    void handlePracticeMenuButton(ActionEvent event) throws IOException {
        radioFunctions.stopTypingMode();
        App.practiceMenuView();
    }

    @FXML
    void handleMainMenuButton(ActionEvent event) throws IOException {
        radioFunctions.stopTypingMode();
        App.homeScreenView();
    }

    @FXML
    void handleSendButton(ActionEvent event) {
        String userMessage = cwInputTextField.getText().trim();
        if (!userMessage.isEmpty()) {
            try {
                session.getBasicRemote().sendText(userMessage);
                chatLogTextArea.appendText("You (Morse): " + userMessage + "\n");
                cwInputTextField.clear();

            } catch (Exception e) {
                chatLogTextArea.appendText("Error sending message: " + e.getMessage() + "\n");
            }
        }
    }

    @FXML
    private void handlePaddleMode() {
        paddleModeButton.setDisable(true);
        straightKeyModeButton.setDisable(false);
        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Paddle", this);
    }

    @FXML
    private void handleStraightKeyMode() {
        paddleModeButton.setDisable(false);
        straightKeyModeButton.setDisable(true);
        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Straight", this);
    }

    public void addCwToInput(String cwChar) {
        String currentText = cwInputTextField.getText();

        if (cwChar.equals("/")) {
            if (!currentText.isEmpty() && currentText.charAt(currentText.length() - 1) != '/') {
                cwInputTextField.setText(currentText + "/");
            }
        } else if (cwChar.equals(" ")) {
            if (!currentText.isEmpty() && currentText.charAt(currentText.length() - 1) != ' ' && currentText.charAt(currentText.length() - 1) != '/') {
                cwInputTextField.setText(currentText + " ");
            }
        } else {
            cwInputTextField.setText(currentText + cwChar);
        }
    }
}
