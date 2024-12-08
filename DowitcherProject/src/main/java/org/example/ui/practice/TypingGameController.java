package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.data.User;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.RadioFunctions;

import java.io.IOException;

public class TypingGameController implements MorseCodeOutput {

    @FXML private VBox rightVBox;

    private TextArea cwInputTextArea;
    private Button translateButton;
    private TextArea englishOutput;
    private Button paddleModeButton;
    private Button straightKeyModeButton;
    private Label currentModeLabel;
    private Label ditKeyLabel;
    private Label dahKeyLabel;
    private Label straightKeyLabel;

    private MorseCodeTranslator morseCodeTranslator;
    private RadioFunctions radioFunctions;

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
        cwInputTextArea = new TextArea();
        cwInputTextArea.setDisable(true);
        cwInputTextArea.setStyle("-fx-opacity: 1.0;");

        englishOutput = new TextArea();
        englishOutput.setDisable(true);
        englishOutput.setStyle("-fx-opacity: 1.0;");

        translateButton = new Button("Translate");
        translateButton.setOnAction(e -> translateMorseCode());

        paddleModeButton = new Button("Paddle Mode");
        paddleModeButton.setOnAction(e -> handlePaddleMode());

        straightKeyModeButton = new Button("Straight Key Mode");
        straightKeyModeButton.setOnAction(e -> handleStraightKeyMode());

        currentModeLabel = new Label("Current Mode: None");
        ditKeyLabel = new Label();
        dahKeyLabel = new Label();
        straightKeyLabel = new Label();

        updateKeyLabels();

        // Add elements to the right VBox
        rightVBox.getChildren().addAll(
                new Label("Typing Practice"),
                cwInputTextArea,
                translateButton,
                englishOutput,
                paddleModeButton,
                straightKeyModeButton,
                currentModeLabel,
                ditKeyLabel,
                dahKeyLabel,
                straightKeyLabel
        );
    }

    private void updateKeyLabels() {
        String ditKeyCode = User.getKeyForAction("ditKey").getName();
        String dahKeyCode = User.getKeyForAction("dahKey").getName();
        String straightKeyCode = User.getKeyForAction("straightKey").getName();

        ditKeyLabel.setText("Dit  ->  " + ditKeyCode);
        dahKeyLabel.setText("Dah  ->  " + dahKeyCode);
        straightKeyLabel.setText("Straight Key  ->  " + straightKeyCode);
    }

    @FXML
    private void handlePaddleMode() {
        paddleModeButton.setDisable(true);
        straightKeyModeButton.setDisable(false);
        currentModeLabel.setText("Current Mode: Paddle");

        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Paddle", this);
    }

    @FXML
    private void handleStraightKeyMode() {
        paddleModeButton.setDisable(false);
        straightKeyModeButton.setDisable(true);
        currentModeLabel.setText("Current Mode: Straight Key");

        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Straight", this);
    }

    public void addCwToInput(String cwChar) {
        String currentText = cwInputTextArea.getText();
        if (cwChar.equals("/")) {
            if (!currentText.isEmpty() && currentText.charAt(currentText.length() - 1) != '/') {
                cwInputTextArea.setText(currentText + "/");
            }
        } else if (cwChar.equals(" ")) {
            if (!currentText.isEmpty() && currentText.charAt(currentText.length() - 1) != ' ' && currentText.charAt(currentText.length() - 1) != '/') {
                cwInputTextArea.setText(currentText + " ");
            }
        } else {
            cwInputTextArea.setText(currentText + cwChar);
        }
    }

    @FXML
    private void clearInput() {
        cwInputTextArea.clear();
    }

    @FXML
    private void translateMorseCode() {
        String morseCode = cwInputTextArea.getText();
        String translatedText = MorseCodeTranslator.translateMorseCode(morseCode);
        englishOutput.setText(translatedText);
        cwInputTextArea.clear();
    }

    @FXML
    private void switchToHomeScreenView() throws IOException {
        radioFunctions.stopTypingMode();
        App.homeScreenView();
    }

    @FXML
    private void switchToSettingsView() throws IOException {
        radioFunctions.stopTypingMode();
        App.settingsPopupView();
    }
}
