package org.example.utility;

import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.App;
import org.example.ui.practice.MorseCodeOutput;
import org.example.ui.practice.PracticeTypingController;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.List;

public class RadioFunctions {

    private Thread typingModeThread;
    private KeyCode currentKey;
    public boolean isPlaying = true;
    private boolean isStraightKeyPressed = false;
    private long lastReleaseTime = -1;
    private volatile boolean isPaddleMode = true;
    private MorseCodeOutput typingOutputController;

    public RadioFunctions(MorseCodeOutput controller) {
        this.typingOutputController = controller;
    }

    public void setTypingOutputController(MorseCodeOutput newController) {
        this.typingOutputController = newController;
        System.out.println("TypingOutputController changed. Called with: " + newController.getClass().getName() + " Current controller: " + typingOutputController.getClass().getName());
    }

    public static List<Object> checkTranslation(String userTranslation, String currentCW, String textSize) {
        TextFlow checkedUserInput = new TextFlow();
        TextFlow correctTranslation = new TextFlow();
        Text checkedUserLetter;
        Text checkedCorrectLetter;
        int numIncorrect = 0;

        for (int i = 0; i < userTranslation.length(); i++) {

            // Prevents program from crashing if message put in by user
            // is longer than the correct message
            checkedUserLetter = new Text(Character.toString(userTranslation.charAt(i)).toUpperCase());
            if (i >= currentCW.length()) {
                checkedUserLetter.setStyle("-fx-fill: red;");
                checkedCorrectLetter = new Text("");
            } else {

                // Determines if the character is correct or incorrect
                // and sets it to the appropriate color
                if (Character.toUpperCase(userTranslation.charAt(i)) == Character.toUpperCase(currentCW.charAt(i))) {
                    checkedUserLetter.setStyle("-fx-fill: green;");
                    checkedCorrectLetter = new Text(Character.toString(currentCW.charAt(i)));
                } else {
                    checkedUserLetter.setStyle("-fx-fill: red;");
                    checkedCorrectLetter = new Text("_");
                    numIncorrect++;
                }
            }

            checkedUserInput.setStyle("-fx-font-size: " + textSize + "px;");
            checkedCorrectLetter.setStyle("-fx-font-size: " + textSize + "px;");

            checkedUserInput.getChildren().addAll(checkedUserLetter);
            correctTranslation.getChildren().addAll(checkedCorrectLetter);

        }

        // Displays the remaining cwMessage as incorrect if user's input
        // is shorter than the correct message.
        if (currentCW.length() > userTranslation.length()) {
            for (int i = userTranslation.length(); i < currentCW.length(); i++) {
                checkedCorrectLetter = new Text("_");
                checkedCorrectLetter.setStyle("-fx-font-size: " + textSize + "px;");
                correctTranslation.getChildren().addAll(checkedCorrectLetter);
                numIncorrect++;
            }
        }

        boolean isTrue = true;
        if (numIncorrect > 0) {
            isTrue = false;
        }

        List<Object> returnList = new ArrayList<>();
        returnList.add(checkedUserInput);
        returnList.add(correctTranslation);
        returnList.add(isTrue);

        return returnList;

    }

    public void handleTyping(String mode, MorseCodeOutput currentController) {
        System.out.println("handleTyping called with mode: " + mode );  // Debug print
        //System.out.println("Output Controller: " + currentController );// Debug print
        //currentTypingOutputController = currentController;

        stopTypingMode();
        setTypingOutputController(currentController);

        // Stop the existing thread if it's running
        if (typingModeThread != null && typingModeThread.isAlive()) {
            typingModeThread.interrupt();
        }

        // Create a new thread based on the mode
        if (mode.equals("Paddle")) {
            isPaddleMode = true;
            typingModeThread = new Thread(this::runPaddleMode);
        } else if (mode.equals("Straight")) {
            isPaddleMode = false;
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
                            addCw(" ");
                        } else if (timeBetweenPresses > 225) {
                            addCw("/");
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
            Thread.currentThread().interrupt();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private void playDitHold() throws LineUnavailableException, InterruptedException {
        while (isPlaying && currentKey == KeyCode.D){
            Sound.playDit();
            Thread.sleep(50);
            addCw(".");
        }
        lastReleaseTime = System.nanoTime();
    }

    private void playDahHold() throws LineUnavailableException, InterruptedException {
        while (isPlaying&& currentKey == KeyCode.A){
            Sound.playDah();
            Thread.sleep(50);
            addCw("-");
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
                        addCw(" ");
                    } else if (timeBetweenPresses > 225) {
                        addCw("/");
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
                    addCw(".");
                } else {
                    addCw("-");
                }
            }
        }
    }

    private void addCw(String cwChar) {
        typingOutputController.addCwToInput(cwChar);
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

    public void stopTypingMode() {
        if (typingModeThread != null && typingModeThread.isAlive()) {
            typingModeThread.interrupt();
        }
    }

}
