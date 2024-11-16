package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import org.example.App;
import org.example.data.User;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.RadioFunctions;

import java.io.IOException;


public class PracticeTypingController implements MorseCodeOutput {

   // Data
   @FXML private TextArea cwInputTextArea;
   @FXML private Button translateButton;
   @FXML private Button practiceMenuButton;
   @FXML private TextArea englishOutput;
   @FXML private Button paddleModeButton;
   @FXML private Button straightKeyModeButton;
   @FXML private Label currentModeLabel;
    @FXML private Label ditKeyLabel;
    @FXML private Label dahKeyLabel;
    @FXML private Label straightKeyLabel;

   private MorseCodeTranslator morseCodeTranslator;
   private RadioFunctions radioFunctions;

   //All view switching button presses
   @FXML
   private void handlePracticeMenuButton() throws IOException {
        radioFunctions.stopTypingMode();
        App.practiceMenuView();
   }
    @FXML
    private void switchToHomeScreenView() throws IOException {
        radioFunctions.stopTypingMode();
        App.homeScreenView();
    }
    @FXML
    private void switchToSettingsView() throws IOException{
        radioFunctions.stopTypingMode();
        App.settingsView();
    }

    // Initialize the controller and translator
    @FXML
    public void initialize() {
        morseCodeTranslator = new MorseCodeTranslator();
        radioFunctions = new RadioFunctions(this);
        App.currentUser.addView("PracticeTypingView");

        String ditKeyCode = User.getKeyForAction("ditKey").getName();
        String dahKeyCode =User.getKeyForAction("dahKey").getName();
        String straightKeyCode = User.getKeyForAction("straightKey").getName();

        ditKeyLabel.setText("Dit  ->  " + ditKeyCode);
        dahKeyLabel.setText("Dah  ->  " + dahKeyCode);
        straightKeyLabel.setText("Straight Key  ->  " + straightKeyCode);

        App.getScene().setOnKeyPressed(event -> {
            try {
                handleKeyPress(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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

    // Method to handle the translation
    @FXML
    private void translateMorseCode() {
        String morseCode = cwInputTextArea.getText();
        String translatedText = MorseCodeTranslator.translateMorseCode(morseCode);
        englishOutput.setText(translatedText);
        cwInputTextArea.clear();
    }

    private void handleKeyPress(KeyEvent event) throws IOException {
        // Check if the pressed key has a corresponding action in the map
        String pressedKey = event.getCode().toString();
        String action = App.currentUser.getKeyFirstActionMap().get(pressedKey);
        if (action != null) {
            switch (action) {
                case "translate":
                    translateMorseCode();
                    System.out.println("Translating...");
                    break;
                case "settingsKey":
                    switchToSettingsView();
                    System.out.println("Switching to controls view.");
                    break;
                case "mainMenu":
                    switchToHomeScreenView();
                    System.out.println("Switching to main menu.");
                    break;
                default:
                    System.out.println("No action assigned for this key.");
                    break;
            }
        }
    }

}

