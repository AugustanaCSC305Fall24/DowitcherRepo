package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class PracticeTypingController {

    // Data
        @FXML private TextArea morseCodeInput;
        @FXML private Button translateButton;
        @FXML private Button practiceMenuButton;
        @FXML private TextArea englishOutput;
        @FXML private Button paddleModeButton;
        @FXML private Button straightKeyModeButton;

        private boolean isPaddleMode = true;
        private long keyPressStartTime = 0;
        private long lastKeyPressTime = 0;
        private StringBuilder currentMorseCode = new StringBuilder();
        private MorseCodeTranslator morseCodeTranslator;

        //All view switching button presses
        @FXML private void handlePracticeMenuButton() throws IOException {App.practiceMenuView();}
    @FXML private void switchToHomeScreenView() throws IOException {App.homeScreenView();}
    @FXML private void switchToSettingsView() throws IOException{App.settingsView();}

    // Initialize the controller and translator
        @FXML
        public void initialize() {
            morseCodeTranslator = new MorseCodeTranslator();
            App.currentUser.addView("PracticeTypingView");
            // Set the action for the translate button
            translateButton.setOnAction(event -> translateMorseCode());

            //set up mode buttons
            paddleModeButton.setOnAction(event -> setInputMode(true));   // Paddle mode
            straightKeyModeButton.setOnAction(event -> setInputMode(false)); // Straight key mode

            App.getScene().setOnKeyPressed(event -> {
                try {
                    handleKeyPress(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    //Method to switch between input modes
    private void setInputMode(boolean usePaddles) {
        isPaddleMode = usePaddles;
    }

    //Handle paddle key presses (dot and dash using different keys)
    private void handlePaddleKeyPress(KeyEvent event) {
        String keyPressed = event.getCode().toString();
        if (keyPressed.equals("a")) {
            currentMorseCode.append(".");  // Dot
        } else if (keyPressed.equals("d")) {
            currentMorseCode.append("-");  // Dash
        }
        morseCodeInput.setText(currentMorseCode.toString());
    }

    //Handle straight key presses (timing-based input)
    private void handleStraightKeyPress(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            keyPressStartTime = System.currentTimeMillis();  // Start timing
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            long pressDuration = System.currentTimeMillis() - keyPressStartTime;
            if (pressDuration < 200) {
                currentMorseCode.append(".");  // Short press = dot
            } else {
                currentMorseCode.append("-");  // Long press = dash
            }
            morseCodeInput.setText(currentMorseCode.toString());
        }
    }

        // Method to handle the translation
        private void translateMorseCode() {
            String morseCode = morseCodeInput.getText();
            String translatedText = MorseCodeTranslator.translateMorseCode(morseCode);
            englishOutput.setText(translatedText);
        }

    private void handleKeyPress(KeyEvent event) throws IOException {
        if (isPaddleMode) {
            handlePaddleKeyPress(event);
        } else {
            handleStraightKeyPress(event);
        }

        // Check if the pressed key has a corresponding action in the map
        String pressedKey = event.getCode().toString();
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

