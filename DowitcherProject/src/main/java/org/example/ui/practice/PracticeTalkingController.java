package org.example.ui.practice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.RadioFunctions;
import org.example.data.RadioApiRequestHandler;

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
    @FXML private Button sendButton;
    @FXML private RadioFunctions radioFunctions;

    // Instance of RadioApiRequestHandler for chatbot communication
    private RadioApiRequestHandler radioApiRequestHandler;

    // Instance of MorseCodeTranslator for translating messages
    private MorseCodeTranslator morseCodeTranslator;

    // Initialize the controller
    @FXML
    public void initialize() {
        // Instantiate the RadioApiRequestHandler for chatbot communication
        radioApiRequestHandler = new RadioApiRequestHandler();

        // Instantiate MorseCodeTranslator for translating messages
        morseCodeTranslator = new MorseCodeTranslator();

        radioFunctions = new RadioFunctions(this);
    }

    // All view-switching button presses
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

    // Handle the send button click to communicate with the AI
    @FXML
    void handleSendButton(ActionEvent event) {
        String userMessage = cwInputTextField.getText().trim();
        if (!userMessage.isEmpty()) {
            // Translate Morse code to English before sending to the bot
            String englishMessage = morseCodeTranslator.translateMorseCode(userMessage);

            // Display user's message in the chat log (show Morse code to the user)
            chatLogTextArea.appendText("You (Morse): " + userMessage + "\n");

            // Display English translation in chat log (optional, for debugging)
            chatLogTextArea.appendText("You (English): " + englishMessage + "\n");

            // Get AI response via RadioApiRequestHandler
            new Thread(() -> {
                try {
                    // Send the translated English message to the bot
                    String botResponse = radioApiRequestHandler.sendRequest(englishMessage);

                    // Translate the bot's English response to Morse code
                    String botMorseResponse = morseCodeTranslator.translateToMorseCode(botResponse);

                    // Update chat log with bot's response in Morse code
                    javafx.application.Platform.runLater(() -> {
                        chatLogTextArea.appendText("Bot (Morse): " + botMorseResponse + "\n");

                        // Optionally display English response as well for debugging
                        chatLogTextArea.appendText("Bot (English): " + botResponse + "\n");

                        cwInputTextField.clear();
                    });
                } catch (IOException e) {
                    javafx.application.Platform.runLater(() -> {
                        chatLogTextArea.appendText("Bot: Error communicating with bot.\n");
                        cwInputTextField.clear();
                    });
                }
            }).start();
        }
    }

    @FXML
    private void handlePaddleMode() {
        paddleModeButton.setDisable(true);
        straightKeyModeButton.setDisable(false);
        //currentModeLabel.setText("Current Mode - Paddle");

        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Paddle", this);
    }

    @FXML
    private void handleStraightKeyMode() {
        paddleModeButton.setDisable(false);
        straightKeyModeButton.setDisable(true);
        //currentModeLabel.setText("Current Mode - Straight Key");

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
