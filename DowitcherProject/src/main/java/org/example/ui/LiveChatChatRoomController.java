package org.example.ui;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
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

    // UI elements injected from FXML
    @FXML private Button practiceMenuButton;
    @FXML private TextArea mainTextArea; // Corresponds to center TextArea in FXML
    @FXML private VBox rightVBox; // Corresponds to the default right VBox in FXML
    @FXML private HBox bottomHBox; // Corresponds to the bottom HBox in FXML
    @FXML private VBox chatLogVBox;
    @FXML private Button paddleModeButton;
    @FXML private Button straightKeyModeButton;
    @FXML private Slider filterSlider; // Injected filterSlider
    @FXML private Slider frequencySlider; // Injected frequencySlider
    @FXML private Button mainMenuButton;
    @FXML private Label roomTitleLabel;
    @FXML private TextField cwInputTextField;
    @FXML private Button sendButton; // Send button for sending messages
    @FXML private RadioFunctions radioFunctions;
    @FXML private Button connectButton;
    @FXML private TextArea chatLogTextArea;

    private Session session;
    final String USER_NAME = "user" + new Random().nextInt(1000);

    @FXML
    public void initialize() {
        radioFunctions = new RadioFunctions(this);

        // Initialize all UI elements
        initializeUIElements();
    }

    private void initializeUIElements() {
        // Initialize labels
        if (roomTitleLabel == null) {
            roomTitleLabel = new Label("Live Chat Room");
        }

        // Initialize chat log TextArea
        if (chatLogTextArea == null) {
            chatLogTextArea = new TextArea();
            chatLogTextArea.setEditable(false);
            chatLogTextArea.setPrefHeight(300);
        }

        // Initialize frequency slider
        if (frequencySlider == null) {
            frequencySlider = new Slider(0, 100, 50);  // Set a default range and value
            frequencySlider.setBlockIncrement(1);
            frequencySlider.setShowTickLabels(true);
            frequencySlider.setShowTickMarks(true);
        }

        // Initialize filter slider
        if (filterSlider == null) {
            filterSlider = new Slider(0, 1, 0.5);
            filterSlider.setShowTickLabels(true);
            filterSlider.setShowTickMarks(true);
        }

        // Initialize buttons and actions
        if (paddleModeButton == null) {
            paddleModeButton = new Button("Paddle Mode");
            paddleModeButton.setOnAction(event -> handlePaddleMode());
        }

        if (straightKeyModeButton == null) {
            straightKeyModeButton = new Button("Straight Key Mode");
            straightKeyModeButton.setOnAction(event -> handleStraightKeyMode());
        }

        if (sendButton == null) {
            sendButton = new Button("Send");
            sendButton.setOnAction(event -> handleSendButton());
        }

        if (practiceMenuButton == null) {
            practiceMenuButton = new Button("Practice Menu");
            practiceMenuButton.setOnAction(event -> {
                try {
                    handlePracticeMenuButton(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if (mainMenuButton == null) {
            mainMenuButton = new Button("Main Menu");
            mainMenuButton.setOnAction(event -> {
                try {
                    handleMainMenuButton(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // Initialize chat input field
        if (cwInputTextField == null) {
            cwInputTextField = new TextField();
        }

        // Initialize the connect button
        if (connectButton == null) {
            connectButton = new Button("Connect");
            connectButton.setOnAction(event -> {
                try {
                    connectToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Add elements directly to rightVBox
        rightVBox.getChildren().clear();  // Clear any previous children
        rightVBox.getChildren().addAll(
                roomTitleLabel,
                chatLogTextArea,
                frequencySlider,
                filterSlider,
                sendButton,
                practiceMenuButton,
                mainMenuButton
        );

        // Add elements to the bottomHBox
        bottomHBox.getChildren().clear();
        bottomHBox.getChildren().addAll(
                paddleModeButton,
                straightKeyModeButton,
                connectButton // Add the connect button to the bottomHBox
        );
    }


    @FXML
    private void connectToServer() throws Exception {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, new URI("ws://34.172.226.133:8000/ws/" + USER_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String jsonMessage) throws InterruptedException {
        System.out.println("Received WebSocket message: " + jsonMessage);
        ChatMessage chatMessage = new Gson().fromJson(jsonMessage, ChatMessage.class);
        playMessage(chatMessage);
        mainTextArea.appendText(chatMessage.getSender() + " : " + chatMessage.getText() + "\n");
    }

    private void playMessage(ChatMessage chatMessage) throws InterruptedException {
        String msg = chatMessage.getText();
        char[] messageArray = msg.toCharArray();
        for (int i = 0; i < messageArray.length; i++) {
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
                Thread.sleep(2 * User.getCwSpeed());
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
        if (!message.isEmpty()) {
            try {
                sendMessage(message);
                mainTextArea.appendText("You: " + message + "\n");
                cwInputTextField.clear();
            } catch (Exception e) {
                mainTextArea.appendText("Error sending message: " + e.getMessage());
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
