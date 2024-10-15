package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class PracticeTypingController {

        @FXML
        private TextArea morseCodeInput;

        @FXML
        private Button translateButton;

        @FXML
        private Button backButton;

        @FXML
        private TextArea englishOutput;

        private MorseCodeTranslator morseCodeTranslator;

    // Initialize the controller and translator
        @FXML
        public void initialize() {
            morseCodeTranslator = new MorseCodeTranslator();
            App.currentUser.addView("PracticeTypingView");
            // Set the action for the translate button
            translateButton.setOnAction(event -> translateMorseCode());
            App.getScene().setOnKeyPressed(event -> {
                try {
                    handleKeyPress(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // Method to handle the translation
        private void translateMorseCode() {
            String morseCode = morseCodeInput.getText();
            String translatedText = MorseCodeTranslator.translateMorseCode(morseCode);
            englishOutput.setText(translatedText);
        }

    @FXML private void switchToHomeScreenView() throws IOException {App.setRoot("HomeScreenView");}
    @FXML private void switchToSettingsView() throws IOException{App.setRoot("ControlMenuView");}

    private void handleKeyPress(KeyEvent event) throws IOException {
        String pressedKey = event.getCode().toString(); // Get the pressed key as a string

        // Check if the pressed key has a corresponding action in the map
        String action = App.currentUser.getKeyFirstActionMap().get(pressedKey);
        if (action != null) {
            switch (action) {
                case "translate":
                    translateMorseCode();
                    System.out.println("Translating...");
                    break;
                case "settings":
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

