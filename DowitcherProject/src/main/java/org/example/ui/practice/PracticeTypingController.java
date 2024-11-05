package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import org.example.App;
import org.example.utility.MorseCodeTranslator;

import java.io.IOException;

public class PracticeTypingController {

    // Data
        @FXML private TextArea morseCodeInput;
        @FXML private Button translateButton;
        @FXML private Button practiceMenuButton;
        @FXML private TextArea englishOutput;
        @FXML private Button paddleModeButton;
        @FXML private Button straightKeyModeButton;

        private volatile boolean isPaddleMode = true;
        private long keyPressStartTime = 0;
        private long lastKeyPressTime = 0;
        private StringBuilder currentMorseCode = new StringBuilder();
        private MorseCodeTranslator morseCodeTranslator;

        private Thread typingModeThread;

        //All view switching button presses
        @FXML private void handlePracticeMenuButton() throws IOException {
            App.practiceMenuView();}
    @FXML private void switchToHomeScreenView() throws IOException {App.homeScreenView();}
    @FXML private void switchToSettingsView() throws IOException{App.settingsView();}

    // Initialize the controller and translator
    @FXML
    public void initialize() {
        morseCodeTranslator = new MorseCodeTranslator();
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

        isPaddleMode = true;
        handleTyping("Paddle");
    }

    @FXML
    private void handleStraightKeyMode() {

        isPaddleMode = false;
        handleTyping("Straight");
    }

    @FXML
    private void handleTyping(String mode) {
        System.out.println("handleTyping called with mode: " + mode);  // Debug print

        // Stop the existing thread if it's running
        if (typingModeThread != null && typingModeThread.isAlive()) {
            typingModeThread.interrupt();
        }

        // Create a new thread based on the mode
        if (mode.equals("Paddle")) {
            typingModeThread = new Thread(this::runPaddleMode);
        } else if (mode.equals("Straight")) {
            typingModeThread = new Thread(this::runStraightKeyMode);
        }

        typingModeThread.start();
    }

    private void runPaddleMode() {
        try {
            while (isPaddleMode) { // Only runs when isPaddleMode is true
                System.out.println("0");
                Thread.sleep(500); // Delay to prevent excessive CPU usage
            }
        } catch (InterruptedException e) {
            System.out.println("Paddle mode interrupted.");
        }
    }

    private void runStraightKeyMode() {
        try {
            while (!isPaddleMode) { // Only runs when isPaddleMode is false
                System.out.println("1");
                Thread.sleep(500); // Delay to prevent excessive CPU usage
            }
        } catch (InterruptedException e) {
            System.out.println("Straight key mode interrupted.");
        }
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
    @FXML
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

