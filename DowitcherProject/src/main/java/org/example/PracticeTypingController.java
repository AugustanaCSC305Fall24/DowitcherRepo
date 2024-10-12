package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

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

            // Set the action for the translate button
            translateButton.setOnAction(event -> translateMorseCode());

        }

        // Method to handle the translation
        private void translateMorseCode() {
            String morseCode = morseCodeInput.getText();
            String translatedText = MorseCodeTranslator.translateMorseCode(morseCode);
            englishOutput.setText(translatedText);
        }

    @FXML
    private void switchToHomeScreenView() throws IOException {
        App.setRoot("HomeScreenView");
    }
}

