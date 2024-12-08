package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.data.ChatBot;
import org.example.data.User;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.RadioFunctions;
import org.example.data.RadioApiRequestHandler;
import org.example.utility.Sound;

import java.io.IOException;

public class PracticeTalkingController implements MorseCodeOutput {

    // FXML injected elements
    @FXML private VBox rightVBox;
    @FXML private HBox bottomHBox;
    @FXML private TextArea mainTextArea;
    @FXML private Button practiceMenuButton;
    @FXML private Button paddleModeButton;
    @FXML private Button straightKeyModeButton;
    @FXML private Slider filterSlider;
    @FXML private Slider frequencySlider;
    @FXML private Button mainMenuButton;
    @FXML private TextField cwInputTextField;
    @FXML private Button sendButton;

    // Internal data
    private Label roomTitleLabel;
    private TextArea chatLogTextArea;
    private RadioFunctions radioFunctions;
    private RadioApiRequestHandler radioApiRequestHandler;
    private MorseCodeTranslator morseCodeTranslator;
    private Sound sound;
    private ChatBot lastPlayedBot = null;
    private boolean isPlaybackActive = false;
    private double lastLoggedThreshold = -1;
    private boolean isWithinRange = false;

    @FXML
    public void initialize() {
        if (rightVBox == null) {
            rightVBox = new VBox();
        }
        if (bottomHBox == null) {
            bottomHBox = new HBox();
        }

        radioApiRequestHandler = new RadioApiRequestHandler();
        morseCodeTranslator = new MorseCodeTranslator();
        radioFunctions = new RadioFunctions(this);
        sound = new Sound();

        initializeUIElements();
    }

    private void initializeUIElements() {
        // Initialize roomTitleLabel and chatLogTextArea
        roomTitleLabel = new Label("Practice Talking");
        chatLogTextArea = new TextArea();
        chatLogTextArea.setPrefHeight(300);
        chatLogTextArea.setEditable(false);
        chatLogTextArea.setWrapText(true);  // Enable text wrapping for better readability

        // Initialize sliders and make them horizontal
        if (frequencySlider == null) {
            frequencySlider = new Slider(0, 100, 50);
            frequencySlider.setBlockIncrement(1);
            frequencySlider.setShowTickLabels(true);
            frequencySlider.setShowTickMarks(true);
            frequencySlider.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
            frequencySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    handleNearbyBotDetection(newValue.doubleValue());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if (filterSlider == null) {
            filterSlider = new Slider(0, 1, 0.5);
            filterSlider.setShowTickLabels(true);
            filterSlider.setShowTickMarks(true);
            filterSlider.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        }

        // Initialize input text field and send button
        if (cwInputTextField == null) {
            cwInputTextField = new TextField();
        }

        if (sendButton == null) {
            sendButton = new Button("Send");
            sendButton.setOnAction(event -> handleSendButton());
        }

        // Initialize buttons
        if (paddleModeButton == null) {
            paddleModeButton = new Button("Paddle Mode");
            paddleModeButton.setOnAction(event -> handlePaddleMode());
        }

        if (straightKeyModeButton == null) {
            straightKeyModeButton = new Button("Straight Key Mode");
            straightKeyModeButton.setOnAction(event -> handleStraightKeyMode());
        }

        // Create "Go to Main Menu" button
        Button mainMenuButton = new Button("Go to Main Menu");
        mainMenuButton.setOnAction(event -> {
            try {
                App.homeScreenView();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Create "Go to Add/Edit/Remove Bot" button
        Button botManagementButton = new Button("Go to Bot Management");
        botManagementButton.setOnAction(event -> {
            try {
                App.botView();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Add elements to rightVBox
        rightVBox.getChildren().clear();
        rightVBox.getChildren().addAll(roomTitleLabel, chatLogTextArea, filterSlider, frequencySlider, cwInputTextField, sendButton);

        // Add elements to bottomHBox
        bottomHBox.getChildren().clear();
        bottomHBox.getChildren().addAll(paddleModeButton, straightKeyModeButton, mainMenuButton, botManagementButton);

        // Initialize mainTextArea (now chatLogTextArea) with welcome message
        mainTextArea.setText("Welcome to Practice Talking. Start by sending a message.\n");
    }

    // Handle sending a message
    private void handleSendButton() {
        String userMessage = cwInputTextField.getText().trim();
        if (!userMessage.isEmpty()) {
            String englishMessage = morseCodeTranslator.translateMorseCode(userMessage);
            chatLogTextArea.appendText("You (Morse): " + userMessage + "\n");

            new Thread(() -> {
                try {
                    ChatBot nearbyBot = getNearbyBot(frequencySlider.getValue());
                    if (nearbyBot != null) {
                        nearbyBot.addNewMessage("User Said: " + englishMessage);
                        String prompt = nearbyBot.formatPrompt();
                        String botResponse = radioApiRequestHandler.sendRequest(prompt);
                        nearbyBot.addNewMessage("You Responded: " + botResponse);
                        String botMorseResponse = morseCodeTranslator.translateToMorseCode(botResponse);

                        chatLogTextArea.appendText("Bot (Morse): " + botMorseResponse + "\n");
                        sound.playAudio(0, botMorseResponse);
                    } else {
                        chatLogTextArea.appendText("No bot nearby.\n");
                    }
                    cwInputTextField.clear();
                } catch (IOException | InterruptedException e) {
                    chatLogTextArea.appendText("Bot: Error communicating with bot.\n");
                    cwInputTextField.clear();
                }
            }).start();
        }
    }

    // Handle Paddle Mode
    @FXML
    private void handlePaddleMode() {
        paddleModeButton.setDisable(true);
        straightKeyModeButton.setDisable(false);
        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Paddle", this);
    }

    // Handle Straight Key Mode
    @FXML
    private void handleStraightKeyMode() {
        paddleModeButton.setDisable(false);
        straightKeyModeButton.setDisable(true);
        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Straight", this);
    }

    private void handleNearbyBotDetection(Double targetFrequency) throws InterruptedException {
        ChatBot nearbyBot = getNearbyBot(targetFrequency);
        if (nearbyBot != null && nearbyBot != lastPlayedBot) {
            lastPlayedBot = nearbyBot;
            isWithinRange = true;
            playBotMessageWithDelay(nearbyBot);
        } else {
            isWithinRange = false;
            lastPlayedBot = null;
            if (sound.isAudioPlaying()) {
                sound.stopAudioPlayback();
            }
        }
    }

    private ChatBot getNearbyBot(Double targetFrequency) {
        double filterValue = filterSlider.getValue();
        double frequencyThreshold = 1 + 4 * filterValue;

        for (ChatBot bot : User.chatBotRegistry) {
            if (Math.abs(bot.getFrequency() - targetFrequency) <= frequencyThreshold) {
                return bot;
            }
        }
        return null;
    }

    private void playBotMessageWithDelay(ChatBot bot) {
        new Thread(() -> {
            isWithinRange = true;
            while (isWithinRange && bot == lastPlayedBot) {
                try {
                    String defaultMessage = bot.getDefaultMessageInCW();
                    String defaultMessageMorse = morseCodeTranslator.translateToMorseCode(defaultMessage);

                    sound.playAudio(0, defaultMessageMorse);

                    javafx.application.Platform.runLater(() -> {
                        chatLogTextArea.appendText("Bot (Morse): " + defaultMessageMorse + "\n");
                    });

                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    @Override
    public void addCwToInput(String cwChar) {

    }
}
