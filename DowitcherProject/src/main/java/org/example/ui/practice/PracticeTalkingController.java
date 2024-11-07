package org.example.ui.practice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.App;
//import org.example.data.VertexWebScrapper;
import org.example.utility.Sound;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class PracticeTalkingController {

    // Data
    @FXML private Button practiceMenuButton;
    @FXML private TextArea chatLogTextArea;
    @FXML private VBox chatLogVBox;
    @FXML private Button dahButton;
    @FXML private Button ditButton;
    @FXML private Slider filterSlider;
    @FXML private Slider frequencySlider;
    @FXML private Button mainMenuButton;
    @FXML private Label roomTitleLabel;
    @FXML private Button straightKeyButton;
    @FXML private TextField typingTextField;
    @FXML private Button sendButton;  // Add a Send button for sending typed messages

    private boolean isPlaying = false;

    // Chatbot instance
//    private VertexWebScrapper chatBot;

    // Initialize the controller
    @FXML
    public void initialize() {
        // Instantiate the GeminiWebScraper for chatbot communication
//        chatBot = new VertexWebScrapper();
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
        String userMessage = typingTextField.getText().trim();
        if (!userMessage.isEmpty()) {
            // Display user's message in the chat log
            chatLogTextArea.appendText("You: " + userMessage + "\n");

            // Get AI response via GeminiWebScraper
            new Thread(() -> {
                //String botResponse = chatBot.getChatBotResponse(userMessage);

                // Update chat log with bot's response (on JavaFX Application Thread)
                javafx.application.Platform.runLater(() -> {
                    //chatLogTextArea.appendText("Bot: " + botResponse + "\n");
                    typingTextField.clear();
                });
            }).start();
        }
    }

    // Morse code handling methods
    @FXML
    void handleDahButton() {
        dahButton.setOnMousePressed(event -> {
            new Thread(() -> {
                isPlaying = true;
                try {
                    playDahHold();
                } catch (LineUnavailableException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });

        dahButton.setOnMouseReleased(event -> isPlaying = false);
    }

    @FXML
    void handleDitButton() {
        ditButton.setOnMousePressed(event -> {
            isPlaying = true;
            new Thread(() -> {
                try {
                    playDitHold();
                } catch (LineUnavailableException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });

        ditButton.setOnMouseReleased(event -> isPlaying = false);
    }

    // Straight key button handler
    @FXML
    void handleStraightKeyButton(ActionEvent event) {}

    // Morse code playback methods
    private void playDitHold() throws LineUnavailableException, InterruptedException {
        while (isPlaying) {
            Sound.playDit();
            Thread.sleep(50);
            typingTextField.setText(typingTextField.getText() + ".");
        }
    }

    private void playDahHold() throws LineUnavailableException, InterruptedException {
        while (isPlaying) {
            Sound.playDah();
            Thread.sleep(50);
            typingTextField.setText(typingTextField.getText() + "-");
        }
    }
}
