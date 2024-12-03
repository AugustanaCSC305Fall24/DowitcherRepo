package org.example.ui;

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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

//@ClientEndpoint
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

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean connected = false;

    @FXML
    public void initialize() {
        try {
            connectToWebSocketServer("ws://localhost:8000/ws/user");
        } catch (Exception e) {
            appendToChatLog("Error connecting to server: " + e.getMessage());
        }
    }

    private void connectToWebSocketServer(String serverUri) throws Exception {
        URI uri = new URI(serverUri);
        String host = uri.getHost();
        int port = uri.getPort() == -1 ? 80 : uri.getPort();
        String path = uri.getPath();

        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Perform WebSocket handshake
        String secWebSocketKey = generateWebSocketKey();
        String handshakeRequest = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "Upgrade: websocket\r\n" +
                "Connection: Upgrade\r\n" +
                "Sec-WebSocket-Key: " + secWebSocketKey + "\r\n" +
                "Sec-WebSocket-Version: 13\r\n" +
                "\r\n";

        writer.write(handshakeRequest);
        writer.flush();

        // Read handshake response
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) break; // End of headers
        }

        connected = true;
        appendToChatLog("Connected to server.");

        // Start a thread to listen for messages
        new Thread(this::listenForMessages).start();
    }

    private String generateWebSocketKey() {
        byte[] keyBytes = new byte[16];
        new Random().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    private void listenForMessages() {
        try {
            while (connected) {
                // Read raw WebSocket frame
                int firstByte = reader.read();
                if (firstByte == -1) break; // End of stream

                int payloadLength = reader.read() & 0x7F;
                byte[] payload = new byte[payloadLength];
                reader.read();

                String message = new String(payload, StandardCharsets.UTF_8);
                Platform.runLater(() -> appendToChatLog("Received: " + message));
            }
        } catch (IOException e) {
            appendToChatLog("Error receiving message: " + e.getMessage());
        }
    }

    private void sendMessage(String message) throws IOException {
        byte[] payload = message.getBytes(StandardCharsets.UTF_8);
        int payloadLength = payload.length;

        writer.write(0x81); // Text frame with FIN bit set
        writer.write(payloadLength); // No masking, short payload
        writer.write(Arrays.toString(payload));
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
