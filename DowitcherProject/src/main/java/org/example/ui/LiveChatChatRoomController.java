package org.example.ui;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.data.ChatMessage;
import org.example.data.User;
import org.example.ui.practice.MorseCodeOutput;
import org.example.utility.RadioFunctions;
import java.io.*;
import java.net.URI;
import java.util.Random;
import jakarta.websocket.*;
import org.example.utility.Sound;

import javax.sound.sampled.LineUnavailableException;

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
    @FXML private Button connectButton;

    private Session session;
    final String USER_NAME = "user" + new Random().nextInt(1000);

    @FXML
    public void initialize() {
        radioFunctions = new RadioFunctions(this);
    }

    @FXML
    private void connectToServer() throws Exception {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, new URI("ws://34.172.226.133:8000/ws/"+USER_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String jsonMessage) throws InterruptedException {
        System.out.println("Received WebSocket message: " + jsonMessage);
        ChatMessage chatMessage = new Gson().fromJson(jsonMessage, ChatMessage.class);
        playMessage(chatMessage);
        chatLogTextArea.appendText(chatMessage.getSender() + " : " + chatMessage.getText()  + "\n");
    }

    private void playMessage(ChatMessage chatMessage) throws InterruptedException {
        String msg = chatMessage.getText();
        char[] messageArray = msg.toCharArray();
        for (int i = 0; i < messageArray.length;i++ ){
            if (messageArray[i] == '-') {
                try {
                    Sound.playDah();
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            } else if (messageArray[i] == '.') {
                try {
                    Sound.playDit();
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            } else if (messageArray[i] == ' ') {
                try {
                    Thread.sleep(User.getCwSpeed());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (messageArray[i] == '/') {
                Thread.sleep(2*User.getCwSpeed());
            }
        }
    }


    private void sendMessage(String message) throws IOException {
        ChatMessage msg = new ChatMessage(message, USER_NAME, 7000);
        Gson gson = new Gson();
        String jsonText = gson.toJson(msg);
        System.out.println("Sending WebSocket message: " + jsonText);
        session.getAsyncRemote().sendText(jsonText);
    }

    @FXML
    void handlePracticeMenuButton(ActionEvent event) throws IOException {
        radioFunctions.stopTypingMode();
        App.practiceModesPopupView();
    }

    @FXML
    void handleMainMenuButton(ActionEvent event) throws IOException {
        radioFunctions.stopTypingMode();
        App.homeScreenView();
    }

    @FXML
    private void handleSendButton() {
        String message = cwInputTextField.getText().trim();
        if (!message.isEmpty() ) {
            try {
                sendMessage(message);
                chatLogTextArea.appendText("You: " + message+"\n");
                cwInputTextField.clear();
            } catch (Exception e) {
                chatLogTextArea.appendText("Error sending message: " + e.getMessage());
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
