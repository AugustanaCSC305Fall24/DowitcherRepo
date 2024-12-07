package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.example.App;
import org.example.data.User;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.RadioFunctions;

import java.io.IOException;


public class TypingGameController implements MorseCodeOutput {

   // Data
   @FXML private TextArea cwInputTextArea;
   @FXML private Button translateButton;
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
    private void switchToHomeScreenView() throws IOException {
        radioFunctions.stopTypingMode();
        App.homeScreenView();
    }
    @FXML
    private void switchToSettingsView() throws IOException{
        radioFunctions.stopTypingMode();
        App.settingsPopupView();
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

        cwInputTextArea.setDisable(true);
        cwInputTextArea.setStyle("-fx-opacity: 1.0;");
        englishOutput.setDisable(true);
        englishOutput.setStyle("-fx-opacity: 1.0;");

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
        System.out.println("adding " + cwChar + " to text area" + cwInputTextArea.hashCode());
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

}

