package org.example.ui;


import com.google.api.client.json.Json;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.ui.practice.MorseCodeOutput;
import org.example.utility.RadioFunctions;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.http.WebSocket;


//ask about what server gets, am i writing to the server? does the server actually receive the message?

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

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean connected = false;

    @FXML
    public void initialize() {
        radioFunctions = new RadioFunctions(this);
    }


    @FXML
    private void connectToServer() throws Exception {
        URI uri = new URI("ws://localhost:8000/ws/user");

        String host = uri.getHost();
        int port = uri.getPort() == -1 ? 80 : uri.getPort();
        String path = uri.getPath();

        socket = new Socket(host,port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        connected = socket.isConnected();
        if(connected){
            appendToChatLog("Connected to server.");
        } else {
            appendToChatLog("Not connected to server.");
        }
        // Start a thread to listen for messages
        new Thread(this::listenForMessages).start();
    }

    private void listenForMessages() {
        try {
            while (connected) {
                String message = reader.readLine();
                JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
                String jsonMessage = jsonObject.get("message").toString();
                Platform.runLater(() -> appendToChatLog("Received: " + jsonMessage));
            }
        } catch (IOException e) {
            appendToChatLog("Error receiving message: " + e.getMessage());
        }
    }

    private void sendMessage(String message) throws IOException {
        JsonObject jsonObject = new JsonObject();//JsonParser.parseString(message).getAsJsonObject();
        jsonObject.addProperty("message",message);
        writer.write(jsonObject.toString());
        writer.newLine();
        writer.flush();

    }

    private void appendToChatLog(String message) {
        Platform.runLater(() -> chatLogTextArea.appendText(message + "\n"));
    }

    @FXML
    public void onClose() {
        try {
            connected = false;
            if (socket != null) socket.close();
        } catch (IOException e) {
            appendToChatLog("Error closing connection: " + e.getMessage());
        }
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
        if (!message.isEmpty() && connected) {
            try {
                sendMessage(message);
                appendToChatLog("You: " + message);
                cwInputTextField.clear();
            } catch (Exception e) {
                appendToChatLog("Error sending message: " + e.getMessage());
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
