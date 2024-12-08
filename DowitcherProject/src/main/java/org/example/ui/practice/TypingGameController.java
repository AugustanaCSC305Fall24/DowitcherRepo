package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.data.RadioApiRequestHandler;
import org.example.data.User;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.RadioFunctions;
import org.example.utility.Sound;

import java.io.IOException;

public class TypingGameController implements MorseCodeOutput {


    //Top Hbox Elements
    @FXML private HBox topHBox;
    @FXML private Button settingsButton;
    @FXML private Button menuButton;
    @FXML private Label screenName;
    private final String ROOM_NAME = "Typing Game";


    //Right Vbox Elements
    @FXML private VBox rightVBox;
    @FXML private TextField cwInputTextField;
    @FXML private Button sendButton;

    //Bottom Hbox Elements
    @FXML private HBox bottomHBox;
    @FXML private Button paddleModeButton;
    @FXML private Button straightKeyModeButton;

    //mainTextArea stuff
    @FXML private TextArea mainTextArea;
    private TextArea englishOutput;


    //Objects
    private RadioFunctions radioFunctions;
    private MorseCodeTranslator morseCodeTranslator;
    private Sound sound;


    @FXML
    public void initialize() {
        if (rightVBox == null) {
            rightVBox = new VBox(); // Ensure rightVBox is initialized
        }

        morseCodeTranslator = new MorseCodeTranslator();
        radioFunctions = new RadioFunctions(this);
        App.currentUser.addView("PracticeTypingView");

        initializeUIElements();
    }

    private void initializeUIElements() {
        // Create UI elements dynamically
        topHboxInitialized();
        sideVboxInitialized();
        botHboxInitialized();

        mainTextArea.setEditable(false);
        mainTextArea.setFocusTraversable(false); // Makes it undetectable
        mainTextArea.setStyle("-fx-font-size: 30px;"); // Larger text size
    }

    private void botHboxInitialized() {
        if (bottomHBox == null) {
            bottomHBox = new HBox();
        }

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

        bottomHBox.getChildren().clear();
        bottomHBox.getChildren().addAll(paddleModeButton, straightKeyModeButton);
    }

    private void sideVboxInitialized() {
        if (rightVBox == null) {
            rightVBox = new VBox();
        }

        Slider volumeSlider = new Slider(0, 100, 50); // Create new volume slider
        volumeSlider.setBlockIncrement(1);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> User.setVolume(volumeSlider.getValue()));
        volumeSlider.getStyleClass().add("slider"); // Apply slider style from the CSS

        Label yourInputLabel = new Label("Your input:");
        yourInputLabel.getStyleClass().add("label"); // Apply label style from the CSS

        if (cwInputTextField == null) {
            cwInputTextField = new TextField();
            cwInputTextField.getStyleClass().add("text-field"); // Apply text field style from the CSS
        }
        cwInputTextField.setFocusTraversable(false);

        if (sendButton == null) {
            sendButton = new Button("Send");
            sendButton.setOnAction(event -> handleSendButton());
            sendButton.getStyleClass().add("custom-button"); // Apply button style from the CSS
        }

        rightVBox.getChildren().clear();
        rightVBox.getChildren().addAll(
                volumeSlider,
                yourInputLabel,
                cwInputTextField,
                sendButton
        );
    }

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

    @FXML
    private void clearInput() {
        cwInputTextField.clear();
    }

    @FXML
    private void handleSendButton() {
        String morseCode = cwInputTextField.getText();
        String translatedText = MorseCodeTranslator.translateMorseCode(morseCode);
        mainTextArea.setText(translatedText);
        cwInputTextField.clear();
    }

    private void topHboxInitialized() {
        if (topHBox == null) {
            topHBox = new HBox();
        }

        // Create the "Settings" button
        settingsButton = new Button("Settings");
        settingsButton.setOnAction(event -> App.togglePopup("SettingsPopup.fxml", settingsButton));
        settingsButton.getStyleClass().add("custom-button"); // Apply button style from the CSS

        // Create the "Menu" button
        Button menuButton = new Button("Menu");
        menuButton.setOnAction(event -> {
            try {
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
}
