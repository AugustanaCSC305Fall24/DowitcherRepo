package org.example.ui.practice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.utility.RadioFunctions;
//import org.example.data.VertexWebScrapper;

import java.io.IOException;

public class PracticeTalkingController implements MorseCodeOutput {

    // Data
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
    @FXML private Button sendButton;  // Add a Send button for sending typed messages
    @FXML private RadioFunctions radioFunctions;

    // Chatbot instance
//    private VertexWebScrapper chatBot;

    // Initialize the controller
    @FXML
    public void initialize() {
        // Instantiate the GeminiWebScraper for chatbot communication
//        chatBot = new VertexWebScrapper();
        radioFunctions = new RadioFunctions(this);
    }

    // All view-switching button presses
    @FXML
    void handlePracticeMenuButton(ActionEvent event) throws IOException {
        App.practiceMenuView();
    }

    @FXML
    void handleMainMenuButton(ActionEvent event) throws IOException {
        App.homeScreenView();
    }

    // Handle the send button click to communicate with the AI
    @FXML
    void handleSendButton(ActionEvent event) {
        String userMessage = cwInputTextField.getText().trim();
        if (!userMessage.isEmpty()) {
            // Display user's message in the chat log
            chatLogTextArea.appendText("You: " + userMessage + "\n");

            // Get AI response via GeminiWebScraper
            new Thread(() -> {
                //String botResponse = chatBot.getChatBotResponse(userMessage);

                // Update chat log with bot's response (on JavaFX Application Thread)
                javafx.application.Platform.runLater(() -> {
                    //chatLogTextArea.appendText("Bot: " + botResponse + "\n");
                    cwInputTextField.clear();
                });
            }).start();
        }
    }

    @FXML
    private void handlePaddleMode() {
        paddleModeButton.setDisable(true);
        straightKeyModeButton.setDisable(false);
        //currentModeLabel.setText("Current Mode - Paddle");

        radioFunctions.handleTyping("Paddle", "PracticeTalking");
    }

    @FXML
    private void handleStraightKeyMode() {
        paddleModeButton.setDisable(false);
        straightKeyModeButton.setDisable(true);
        //currentModeLabel.setText("Current Mode - Straight Key");

        radioFunctions.handleTyping("Straight", "PracticeTalking");
    }

    public void addCwToInput(String cwChar) {
        if (cwChar.equals("/")) {
            if(cwInputTextField.getText().charAt(cwInputTextField.getText().length() - 1) != '/') {
                cwInputTextField.setText(cwInputTextField.getText() + "/");
            }
        } else if (cwChar.equals(" ")) {
            if(cwInputTextField.getText().charAt(cwInputTextField.getText().length() - 1) != ' ' && cwInputTextField.getText().charAt(cwInputTextField.getText().length() - 1) != '/') {
                cwInputTextField.setText(cwInputTextField.getText() + " ");
            }
        } else {
            if (cwChar.equals(".")) {
                cwInputTextField.setText(cwInputTextField.getText() + ".");
            } else {
                cwInputTextField.setText(cwInputTextField.getText() + "-");
            }
        }
    }
}
