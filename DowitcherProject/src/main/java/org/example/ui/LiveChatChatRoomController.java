package org.example.ui;


import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.data.ChatMessage;
import org.example.ui.practice.MorseCodeOutput;
import org.example.utility.RadioFunctions;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.http.WebSocket;
import java.util.Random;

import jakarta.websocket.*;


//ask about what server gets, am i writing to the server? does the server actually receive the message?
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
    public void onMessage(String jsonMessage) {
        System.out.println("Received WebSocket message: " + jsonMessage);

        ChatMessage chatMessage = new Gson().fromJson(jsonMessage, ChatMessage.class);
        chatLogTextArea.appendText(chatMessage.getSender() + " : " + chatMessage.getText()  + "\n");
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
        App.practiceMenuView();
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
                chatLogTextArea.appendText("You: " + message);
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
