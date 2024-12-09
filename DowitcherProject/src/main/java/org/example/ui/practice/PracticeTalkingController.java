package org.example.ui.practice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.data.ChatBot;
import org.example.data.User;
import org.example.ui.generalizedHamRadioController;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.RadioFunctions;
import org.example.data.RadioApiRequestHandler;
import org.example.utility.Sound;

import java.io.IOException;

public class PracticeTalkingController extends generalizedHamRadioController implements MorseCodeOutput {

    // FXML injected elements
    @FXML private VBox rightVBox;
    @FXML private HBox bottomHBox;
    @FXML private HBox topHBox;
    @FXML private TextArea mainTextArea;

    @FXML private Button paddleModeButton;
    @FXML private Button straightKeyModeButton;

    @FXML private Slider filterSlider;
    @FXML private Slider frequencySlider;

    @FXML private Button mainMenuButton;
    @FXML private TextField cwInputTextField;
    @FXML private Button sendButton;
    @FXML private Button staticButton;

    // Internal data
    private Label roomTitleLabel;
    private TextArea chatLogTextArea; // Initialize this in the controller but do not add to the layout
    private RadioFunctions radioFunctions;
    private RadioApiRequestHandler radioApiRequestHandler;
    private MorseCodeTranslator morseCodeTranslator;
    private Sound sound;

    private ChatBot lastPlayedBot = null;
    private boolean isPlaybackActive = false;
    private double lastLoggedThreshold = -1;
    private boolean isWithinRange = false;
    private boolean isPlaying = true;
    private boolean isStaticPlaying = false;
    private Thread staticThread;

    // Room Name
    private final String ROOM_NAME = "AI Chat";

    // Initialize the controller
    @FXML
    public void initialize() {
        if (rightVBox == null) {
            rightVBox = new VBox();
        }
        if (bottomHBox == null) {
            bottomHBox = new HBox();
        }

        // Initialize the handlers and translators
        radioApiRequestHandler = new RadioApiRequestHandler();
        morseCodeTranslator = new MorseCodeTranslator();
        radioFunctions = new RadioFunctions(this);
        sound = new Sound();

        // Initialize chatLogTextArea but don't add it to any layout
        chatLogTextArea = new TextArea();
        chatLogTextArea.setEditable(false); // Ensure it's non-editable
        chatLogTextArea.setFocusTraversable(false); // Makes it undetectable

        // Initialize the UI elements
        initializeUIElements();

        // Print all bot call signs and frequencies
        System.out.println("List of Registered Bots:");
        for (ChatBot bot : User.chatBotRegistry) {
            System.out.println("Bot Call Sign: " + bot.getCallSign() + ", Frequency: " + bot.getFrequency());
        }

        // Add a listener to the frequencySlider
        frequencySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                handleNearbyBotDetection(newValue.doubleValue());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Frequency Slider Value Changed: " + newValue.doubleValue() + " kHz");
        });
    }

    private void initializeUIElements() {
        // Initialize the topHBox with buttons and "AI Chat" label
        topHboxInitialized();

        // Initialize sliders and add them to the rightVBox
        if (frequencySlider == null) {
            frequencySlider = new Slider(0, 100, 50);
            frequencySlider.setBlockIncrement(1);
            frequencySlider.setShowTickLabels(true);
            frequencySlider.setShowTickMarks(true);
            frequencySlider.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
            frequencySlider.valueProperty().addListener((observable, oldValue, newValue) -> updateMainTextArea());
            frequencySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    handleNearbyBotDetection(newValue.doubleValue());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            frequencySlider.getStyleClass().add("slider"); // Apply slider style from the CSS
        }

        Slider volumeSlider = new Slider(0, 100, 50); // Create new volume slider
        volumeSlider.setBlockIncrement(1);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> updateMainTextArea());
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> User.setVolume(volumeSlider.getValue()));
        volumeSlider.getStyleClass().add("slider"); // Apply slider style from the CSS

        if (filterSlider == null) {
            filterSlider = new Slider(0, 1, 0.5);
            filterSlider.setShowTickLabels(true);
            filterSlider.setShowTickMarks(true);
            filterSlider.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
            filterSlider.valueProperty().addListener((observable, oldValue, newValue) -> updateMainTextArea());
            filterSlider.valueProperty().addListener((observable, oldValue, newValue) -> adjustStaticVolume());
            filterSlider.getStyleClass().add("slider"); // Apply slider style from the CSS
        }

        // Create and add "Your input" label and text field
        Label yourInputLabel = new Label("Your input:");
        yourInputLabel.getStyleClass().add("label"); // Apply label style from the CSS

        if (cwInputTextField == null) {
            cwInputTextField = new TextField();
            cwInputTextField.getStyleClass().add("text-field"); // Apply text field style from the CSS
        }
        cwInputTextField.setFocusTraversable(false);

        if (sendButton == null) {
            sendButton = new Button("Send");
            sendButton.setOnAction(event -> handleSendButton(event));
            sendButton.getStyleClass().add("custom-button"); // Apply button style from the CSS
        }

        if(staticButton == null) {
            staticButton = new Button("Play Static");
            staticButton.setOnAction(e -> handleStatic());
            staticButton.getStyleClass().add("custom-button"); // Apply button style from the CSS
        }

        // Initialize and configure the chatLogTextArea
        chatLogTextArea = new TextArea();
        chatLogTextArea.setEditable(false); // Prevent editing
        chatLogTextArea.setWrapText(true); // Optional: Wrap text for better formatting
        chatLogTextArea.getStyleClass().add("text-area"); // Apply text area style from the CSS

        // Clear and add elements to rightVBox
        rightVBox.getChildren().clear();
        rightVBox.getChildren().addAll(
                frequencySlider,
                volumeSlider,
                filterSlider,
                yourInputLabel,
                cwInputTextField,
                sendButton,
                chatLogTextArea // Add the chatLogTextArea to the VBox
        );

        // Initialize buttons and text field (unchanged)
        if (paddleModeButton == null) {
            paddleModeButton = new Button("Paddle Mode");
            paddleModeButton.setOnAction(event -> handlePaddleMode());
            paddleModeButton.getStyleClass().add("custom-button"); // Apply button style from the CSS
        }

        if (straightKeyModeButton == null) {
            straightKeyModeButton = new Button("Straight Key Mode");
            straightKeyModeButton.setOnAction(event -> handleStraightKeyMode());
            straightKeyModeButton.getStyleClass().add("custom-button"); // Apply button style from the CSS
        }

        Button botManagementButton = new Button("Go to Bot Management");
        botManagementButton.setOnAction(event -> {
            try {
                stopStatic();
                App.botView();
            } catch (IOException e) {
                throw new RuntimeException(e);
            };
        });
        botManagementButton.getStyleClass().add("custom-button"); // Apply button style from the CSS

        // Add elements to bottomHBox (unchanged)
        bottomHBox.getChildren().clear();
        bottomHBox.getChildren().addAll(paddleModeButton, straightKeyModeButton, botManagementButton, staticButton);

        // Configure mainTextArea to display slider values, be non-editable, and have larger text
        mainTextArea.setEditable(false);
        mainTextArea.setFocusTraversable(false); // Makes it undetectable
        mainTextArea.setStyle("-fx-font-size: 30px;"); // Larger text size
        updateMainTextArea();
    }

    private void topHboxInitialized() {
        if (topHBox == null) {
            topHBox = new HBox();
        }

        // Create the "Settings" button
        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(event -> App.togglePopup("SettingsPopup.fxml", settingsButton));
        settingsButton.getStyleClass().add("custom-button"); // Apply button style from the CSS

        // Create the "Menu" button
        Button menuButton = new Button("Menu");
        menuButton.setOnAction(event -> {
            try {
                stopStatic();
                App.homeScreenView();
            } catch (IOException e) {
                throw new RuntimeException("Failed to return to home screen", e);
            }
        });
        menuButton.getStyleClass().add("custom-button"); // Apply button style from the CSS

        // Add the buttons to the topHBox
        topHBox.getChildren().clear();
        topHBox.getChildren().addAll(menuButton, settingsButton);

        // Ensure topHBox has the expected components before adding "AI Chat" label
        Label screenName = new Label(ROOM_NAME);
        screenName.getStyleClass().add("label"); // Apply label style from the CSS
        topHBox.getChildren().add(1, screenName); // Insert label between menu and settings buttons
    }


    // Handle sending a message
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
                    for (ChatBot bot : User.chatBotRegistry) {
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

                        chatLogTextArea.appendText("Bot (Morse): " + botMorseResponse + "\n");
                        sound.playAudio(0,botMorseResponse);
                        // Optionally display English response as well for debugging
                        //chatLogTextArea.appendText("Bot (English): " + botResponse + "\n");
                        System.out.println("Bot (English): " + botResponse + "\n");

                        cwInputTextField.clear();
                    } else {
                        // Print to terminal if no nearby bot is found
                        System.out.println("No bot nearby.");
                        javafx.application.Platform.runLater(() -> {
                            chatLogTextArea.appendText("No bot nearby.\n");
                            cwInputTextField.clear();
                        });
                    }
                } catch (IOException | InterruptedException e) {
                    javafx.application.Platform.runLater(() -> {
                        chatLogTextArea.appendText("Bot: Error communicating with bot.\n");
                        cwInputTextField.clear();
                    });
                }
            }).start();
        }
    }


    private void updateMainTextArea() {
        double frequencyValue = frequencySlider.getValue();
        double volumeValue = User.getVolume();
        double filterValue = filterSlider.getValue();

        mainTextArea.setText(String.format(
                "Frequency: %.2f\nVolume: %.2f\nFilter: %.2f",
                frequencyValue,
                volumeValue,
                filterValue
        ));
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

    private ChatBot getNearbyBot(Double targetFrequency) {
        // Map the slider value (0 to 1) to a frequency threshold range between 1 kHz and 5 kHz
        double filterValue = filterSlider.getValue();
        double frequencyThreshold = 1 + 4 * filterValue;  // This will give a threshold between 1 kHz and 5 kHz

        // Log the threshold only if it changes
        if (frequencyThreshold != lastLoggedThreshold) {
            System.out.println("Frequency Threshold: " + frequencyThreshold + " kHz");
            lastLoggedThreshold = frequencyThreshold; // Update the last logged threshold
        }

        // Iterate over all registered bots and check if they are within the frequency threshold
        for (ChatBot bot : User.chatBotRegistry) {
            if (Math.abs(bot.getFrequency() - targetFrequency) <= frequencyThreshold) {
                return bot;  // Return the nearby bot
            }
        }
        return null;  // No bot found nearby
    }

    private void handleNearbyBotDetection(Double targetFrequency) throws InterruptedException {
        ChatBot nearbyBot = getNearbyBot(targetFrequency);

        if (nearbyBot != null && nearbyBot != lastPlayedBot) {
            lastPlayedBot = nearbyBot;  // Update the last played bot
            isWithinRange = true;      // Allow playback
            sound.playAudio(0, nearbyBot.getDefaultMessageInCW());
        } else if (nearbyBot == null || nearbyBot != lastPlayedBot) {
            isWithinRange = false;     // Stop playback immediately
            lastPlayedBot = null;      // Reset the last played bot
            if (sound.isAudioPlaying()) {
                sound.stopAudioPlayback();   // Stop audio by playing an empty message
            }
        }
    }

    private void playDefaultMessageIfNeeded(double targetFrequency) throws InterruptedException {
        // Check if the user is within range of a bot and get the nearby bot
        ChatBot nearbyBot = getNearbyBot(targetFrequency);

        if (nearbyBot != null) {
            String defaultMessage = nearbyBot.getDefaultMessageInCW();

            // Display the default message in the chat log
            chatLogTextArea.appendText("Bot (Intro): " + defaultMessage + "\n");

            // Convert the default message to Morse code and play it
            String botMorseMessage = morseCodeTranslator.translateToMorseCode(defaultMessage);
            sound.playAudio(0, botMorseMessage);
        }
    }



    @Override
    public void addCwToInput(String cwChar) {
        // Initialize chatLogTextArea if it's not already initialized
        if (chatLogTextArea == null) {
            chatLogTextArea = new TextArea();
            chatLogTextArea.setEditable(false);
        }

        // Get the current text from the input field
        String currentText = cwInputTextField.getText();

        // Process the incoming Morse code character
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

    private void handleStatic() {
        if (isStaticPlaying) {
            staticButton.setText("Play Static");
            stopStatic();
        } else {
            staticButton.setText("Pause Static");
            playStatic(getStaticVolume());
            isStaticPlaying = true;
        }
    }

    private void playStatic(double volume) {
        staticThread = new Thread(() -> {
            try {
                sound.staticSound(volume, true);
                while (isPlaying) Thread.sleep(100);
                sound.staticSound(volume, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        staticThread.setDaemon(true);
        staticThread.start();
    }

    private void adjustStaticVolume() {
        // Adjust the volume directly if the static sound is playing
        if (isStaticPlaying && sound != null) {
            sound.adjustVolume(getStaticVolume());
        }
    }

    private double getStaticVolume() {
        return (filterSlider.getValue());
    }

    private void stopStatic() {
        staticThread.interrupt();
        sound.setIsStaticPlaying(false);
        isStaticPlaying = false;
    }

}
