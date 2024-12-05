package org.example.ui.practice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.data.ChatBot;
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
    @FXML void handlePracticeMenuButton(ActionEvent event) throws IOException { radioFunctions.stopTypingMode(); App.practiceMenuView(); }
    @FXML void handleMainMenuButton(ActionEvent event) throws IOException { radioFunctions.stopTypingMode(); App.homeScreenView(); }
    @FXML void handleAddEditRemoveBotScreenButton(ActionEvent event) throws IOException { radioFunctions.stopTypingMode(); App.botAddEditRemoveView(); }

    // Handle the send button click to communicate with the AI
    @FXML
    void handleSendButton(ActionEvent event) {
        String userMessage = cwInputTextField.getText().trim();
        if (!userMessage.isEmpty()) {
            // Translate Morse code to English before sending to the bot
            String englishMessage = morseCodeTranslator.translateMorseCode(userMessage);

            // Display user's message in the chat log (show Morse code to the user)
            chatLogTextArea.appendText("You (Morse): " + userMessage + "\n");
            //Comment below in when chat box goes away
//            System.out.println("You (Morse): " + userMessage + "\n");
            // Display English translation in terminal
            System.out.println("You (English): " + englishMessage + "\n");

            // Get AI response via RadioApiRequestHandler
            new Thread(() -> {
                try {
                    // Debugging: Print out the user's frequency and all bot frequencies
                    System.out.println("User Frequency: " + frequencySlider.getValue());
                    System.out.println("List of All Bot Frequencies:");
                    for (ChatBot bot : ChatBot.chatBotRegistry) {
                        System.out.println("Bot Call Sign: " + bot.getCallSign() + ", Frequency: " + bot.getFrequency());
                    }

                    // Check if there is a nearby bot within the frequency range
                    ChatBot nearbyBot = getNearbyBot(frequencySlider.getValue());
                    if (nearbyBot != null) {
                        nearbyBot.addNewMessage("User Said: " + englishMessage);
                        String prompt = nearbyBot.formatPrompt();
                        String botResponse = radioApiRequestHandler.sendRequest(prompt);
                        nearbyBot.addNewMessage("You Responded: " + botResponse);
                        String botMorseResponse = morseCodeTranslator.translateToMorseCode(botResponse);

                        // Update chat log with bot's response in Morse code
                        javafx.application.Platform.runLater(() -> {
                            chatLogTextArea.appendText("Bot (Morse): " + botMorseResponse + "\n");

                            // Optionally display English response as well for debugging
                            chatLogTextArea.appendText("Bot (English): " + botResponse + "\n");

                            cwInputTextField.clear();
                        });
                    } else {
                        // Print to terminal if no nearby bot is found
                        System.out.println("No bot nearby.");
                        javafx.application.Platform.runLater(() -> {
                            chatLogTextArea.appendText("No bot nearby.\n");
                            cwInputTextField.clear();
                        });
                    }
                } catch (IOException e) {
                    javafx.application.Platform.runLater(() -> {
                        chatLogTextArea.appendText("Bot: Error communicating with bot.\n");
                        cwInputTextField.clear();
                    });
                }
            }).start();
        }
    }

    // Method to find a nearby bot based on dynamic frequency threshold
    private ChatBot getNearbyBot(Double targetFrequency) {
        // Map the slider value (0 to 1) to a frequency threshold range between 1 kHz and 5 kHz
        double filterValue = filterSlider.getValue();
        double frequencyThreshold = 1 + 4 * filterValue;  // This will give a threshold between 1 kHz and 5 kHz

        // Debugging: Print the threshold for the current filter slider value
        System.out.println("Frequency Threshold: " + frequencyThreshold + " kHz");

        // Iterate over all registered bots and check if they are within the frequency threshold
        for (ChatBot bot : ChatBot.chatBotRegistry) {
            if (Math.abs(bot.getFrequency() - targetFrequency) <= frequencyThreshold) {
                return bot;  // Return the nearby bot
            }
        }
        return null;  // No bot found nearby
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
