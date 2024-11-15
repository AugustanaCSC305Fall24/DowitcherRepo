package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import org.example.App;
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

        private MorseCodeTranslator morseCodeTranslator;
        private RadioFunctions radioFunctions;

        //All view switching button presses
        @FXML private void handlePracticeMenuButton() throws IOException {
            App.practiceMenuView();}
    @FXML private void switchToHomeScreenView() throws IOException {App.homeScreenView();}
    @FXML private void switchToSettingsView() throws IOException{App.settingsView();}

    // Initialize the controller and translator
    @FXML
    public void initialize() {
        morseCodeTranslator = new MorseCodeTranslator();
        radioFunctions = new RadioFunctions(this);
        App.currentUser.addView("PracticeTypingView");

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
        currentModeLabel.setText("Current Mode - Paddle");

        radioFunctions.handleTyping("Paddle", "PracticeTalking");
    }

    @FXML
    private void handleStraightKeyMode() {
        paddleModeButton.setDisable(false);
        straightKeyModeButton.setDisable(true);
        currentModeLabel.setText("Current Mode - Straight Key");

        radioFunctions.handleTyping("Straight", "PracticeTalking");
    }

    public void addCwToInput(String cwChar) {
        if (cwChar.equals("/")) {
            if(cwInputTextArea.getText().charAt(cwInputTextArea.getText().length() - 1) != '/') {
                cwInputTextArea.setText(cwInputTextArea.getText() + "/");
            }
        } else if (cwChar.equals(" ")) {
            if(cwInputTextArea.getText().charAt(cwInputTextArea.getText().length() - 1) != ' ' && cwInputTextArea.getText().charAt(cwInputTextArea.getText().length() - 1) != '/') {
                cwInputTextArea.setText(cwInputTextArea.getText() + " ");
            }
        } else {
            if (cwChar.equals(".")) {
                cwInputTextArea.setText(cwInputTextArea.getText() + ".");
            } else {
                cwInputTextArea.setText(cwInputTextArea.getText() + "-");
            }
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

