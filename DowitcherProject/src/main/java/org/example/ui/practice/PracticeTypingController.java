package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.example.App;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.Sound;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;


public class PracticeTypingController {

    // Data
        @FXML private TextArea morseCodeInput;
        @FXML private Button translateButton;
        @FXML private Button practiceMenuButton;
        @FXML private TextArea englishOutput;
        @FXML private Button paddleModeButton;
        @FXML private Button straightKeyModeButton;
        @FXML private Label currentModeLabel;

        private volatile boolean isPaddleMode = true;
        private long keyPressStartTime = 0;
        private long lastKeyPressTime = 0;
        private StringBuilder currentMorseCode = new StringBuilder();
        private MorseCodeTranslator morseCodeTranslator;

        private Thread typingModeThread;
        private KeyCode currentKey;
        public boolean isPlaying = true;
        private boolean isStraightKeyPressed = false;
        private long lastReleaseTime = -1;


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
        paddleModeButton.setDisable(true);
        straightKeyModeButton.setDisable(false);
        currentModeLabel.setText("Current Mode - Paddle");

        isPaddleMode = true;
        handleTyping("Paddle");
    }

    @FXML
    private void handleStraightKeyMode() {
        paddleModeButton.setDisable(false);
        straightKeyModeButton.setDisable(true);
        currentModeLabel.setText("Current Mode - Straight Key");

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

        lastReleaseTime = -1;
        typingModeThread.start();
    }

    private void runPaddleMode() {
        try {
            App.getScene().setOnKeyPressed(event -> handleKeyPressed(event.getCode()));
            App.getScene().setOnKeyReleased(event -> handleKeyReleased(event.getCode()));

            while (isPaddleMode) {
                if (isPlaying) {
                    long pressStartTime = System.nanoTime();

                    if (lastReleaseTime != -1) {
                        long timeBetweenPresses = (pressStartTime - lastReleaseTime) / 1_000_000;
                        System.out.println("Time between presses: " + timeBetweenPresses + " ms");
                        if (timeBetweenPresses >= 75 && timeBetweenPresses <= 225) {
                            if(morseCodeInput.getText().charAt(morseCodeInput.getText().length() - 1) != ' ' && morseCodeInput.getText().charAt(morseCodeInput.getText().length() - 1) != '/') {
                                morseCodeInput.setText(morseCodeInput.getText() + " ");
                            }
                        } else if (timeBetweenPresses > 225) {
                            if(morseCodeInput.getText().charAt(morseCodeInput.getText().length() - 1) != '/') {
                                morseCodeInput.setText(morseCodeInput.getText() + "/");
                            }
                        }
                    }

                    if (currentKey == KeyCode.D) {  // Assume D is the dit key
                        playDitHold();
                    } else if (currentKey == KeyCode.A) {  // Assume A is the dah key
                        playDahHold();
                    }
                }

                Thread.sleep(50); // Adjust delay as needed
            }
        } catch (InterruptedException e) {
//            System.out.println("Paddle mode interrupted.");
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private void playDitHold() throws LineUnavailableException, InterruptedException {
        while (isPlaying && currentKey == KeyCode.D){
            Sound.playDit();
            Thread.sleep(50);
            morseCodeInput.setText(morseCodeInput.getText() + ".");
        }
        lastReleaseTime = System.nanoTime();
    }

    private void playDahHold() throws LineUnavailableException, InterruptedException {
        while (isPlaying&& currentKey == KeyCode.A){
            Sound.playDah();
            Thread.sleep(50);
            morseCodeInput.setText(morseCodeInput.getText() + "-");
        }
        lastReleaseTime = System.nanoTime();
    }

    private void runStraightKeyMode() {
        while (!isPaddleMode) {// Only runs when isPaddleMode is false
            App.getScene().setOnKeyPressed(event -> handleKeyPressed(event.getCode()));
            App.getScene().setOnKeyReleased(event -> handleKeyReleased(event.getCode()));

            if (isStraightKeyPressed) { // Implement this method to detect space bar press
                long pressStartTime = System.nanoTime();

                if (lastReleaseTime != -1) {
                    long timeBetweenPresses = (pressStartTime - lastReleaseTime) / 1_000_000;
                    System.out.println("Time between presses: " + timeBetweenPresses + " ms");
                    if (timeBetweenPresses >= 75 && timeBetweenPresses <= 225) {
                        if(morseCodeInput.getText().charAt(morseCodeInput.getText().length() - 1) != ' ' && morseCodeInput.getText().charAt(morseCodeInput.getText().length() - 1) != '/') {
                            morseCodeInput.setText(morseCodeInput.getText() + " ");
                        }
                    } else if (timeBetweenPresses > 225) {
                        if(morseCodeInput.getText().charAt(morseCodeInput.getText().length() - 1) != '/') {
                            morseCodeInput.setText(morseCodeInput.getText() + "/");
                        }
                    }
                }

                // Wait until the space bar is released
                while (isStraightKeyPressed) {
                    // Optional: you could add a short sleep to avoid CPU overuse
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                long pressDuration = (System.nanoTime() - pressStartTime) / 1_000_000;

                //System.out.println("Straight key held for: " + pressDuration + " ms");
                if (pressDuration <= 150) {
                    morseCodeInput.setText(morseCodeInput.getText() + ".");
                } else {
                    morseCodeInput.setText(morseCodeInput.getText() + "-");
                }
            }
        }
    }

    private void handleKeyPressed(KeyCode code) {
        if (code == KeyCode.D || code == KeyCode.A) {
            isPlaying = true;
            currentKey = code;
        }

        if (code == KeyCode.L) {
            isStraightKeyPressed = true;
        }
    }

    private void handleKeyReleased(KeyCode code) {
        if (code == KeyCode.D || code == KeyCode.A) {
            isPlaying = false;
            currentKey = null;
        }

        if (code == KeyCode.L) {
            isStraightKeyPressed = false;
        }

        lastReleaseTime = System.nanoTime();
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
        morseCodeInput.clear();
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

