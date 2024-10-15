package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;



public class PracticeListeningController{

    // All FXML elements on screen that are interacted with
    @FXML private Button mainMenuButton;
    @FXML private Button goToSettingsButton;
    @FXML private Button playPauseAudioButton;
    @FXML private Button restartAudioButton;
    @FXML private Button checkTranslationButton;
    @FXML private Button newAudioButton;
    @FXML private TextArea userInputTextArea;
    @FXML private ScrollPane correctAnswerScrollPane;


    private String cwMessage;
    private String cwAudio;
    HashMap<String, String> cwMessagesList;
    Random random = new Random();


    private Boolean isPaused = true;
    private int pauseIndex;
    private Thread audioThread;
    private volatile boolean stopAudio = false;

    @FXML
    // Initializes HashMap and fills it with all practice messages
    // Then generates a starting message
    private void initialize() {
        cwMessagesList = new HashMap<>();
        cwMessagesList.put("SOS", "... --- ...");
        cwMessagesList.put("ALPHA", ".- .-.. .--. .... .-");
        cwMessagesList.put("BRAVO", "-... .-. .- ...- ---");
        cwMessagesList.put("PAPA", ".--. .- .--. .-.. ---");
        newAudio();
    }

    @FXML
    // If audio is paused, plays audio and changes button to say pause
    // If audio is playing, pauses audio and changes button to say play
    private void playPauseAudio() throws InterruptedException {
        char[] messageArray = cwAudio.toCharArray();
        if (isPaused){
            isPaused = false;
            stopAudio = false;
            playAudio(pauseIndex,messageArray);
            playPauseAudioButton.setText("Pause");
        } else {
            isPaused = true;
            playPauseAudioButton.setText("Play");
        }
    }

    //play audio method for how the audio plays and saves the index where the message is paused so it picks up where it left off
    private void playAudio(int index, char[] messageArray) throws InterruptedException {
        stopAudioPlayback();

        audioThread = new Thread(() -> {
            for (int i = index; i < messageArray.length; i++) {
                if (isPaused || stopAudio) {
                    pauseIndex = i;
                    break;
                }
                if (messageArray[i] == '-') {
                    Sound.playDah();
                } else if (messageArray[i] == '.') {
                    Sound.playDit();
                } else if (messageArray[i] == ' ') {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        audioThread.start();
    }

    @FXML
    // Restarts the audio that is playing
    private void restartAudio() throws InterruptedException {
        stopAudioPlayback();

        char[] messageArray = cwAudio.toCharArray();
        pauseIndex = 0;
        isPaused = true;
        stopAudio = false;
        playPauseAudioButton.setText("Play");
    }

    @FXML
    // Stops the current audio that is playing
    private void stopAudioPlayback() throws InterruptedException {
        if (audioThread != null && audioThread.isAlive()) {
            stopAudio = true;
            audioThread.join();
            stopAudio = false;
        }
    }

    @FXML
    // Checks the translation put in by the user
    // Text is displayed as green is character is correct
    // or is set as red if character is incorrect
    private void checkTranslation() {
        String userTranslation = userInputTextArea.getText();
        TextFlow correctedTranslation = new TextFlow();
        Text checkedLetter;

        for (int i = 0; i < userTranslation.length(); i++) {

            // Prevents program from crashing if message put in by user
            // is longer than the correct message
            if (i >= cwMessage.length()) {
                checkedLetter = new Text(" ");
                checkedLetter.setStyle("-fx-fill: red;");
            } else {
                checkedLetter = new Text(Character.toString(cwMessage.charAt(i)));

                // Determines if the character is correct or incorrect
                // and sets it to the appropriate color
                if (userTranslation.charAt(i) == cwMessage.charAt(i)) {
                    checkedLetter.setStyle("-fx-fill: green;");
                } else {
                    checkedLetter.setStyle("-fx-fill: red;");
                }
            }

            correctedTranslation.getChildren().addAll(checkedLetter);
        }

        // Displays the remaining cwMessage as incorrect if user's input
        // is shorter than the correct message.
        if (cwMessage.length() > userTranslation.length()) {
            for (int i = userTranslation.length(); i < cwMessage.length(); i++) {
                checkedLetter = new Text(Character.toString(cwMessage.charAt(i)));
                checkedLetter.setStyle("-fx-fill: red;");
                correctedTranslation.getChildren().addAll(checkedLetter);
            }
        }

        correctAnswerScrollPane.setContent(correctedTranslation);
    }

    @FXML
    // Sets cwMessage and cwAudio to a random new message and audio from
    // the HashMap containing all the messages and audios
    private void newAudio() {
        pauseIndex = 0;
        isPaused = true;
        playPauseAudioButton.setText("Play");
        List<String> allCWMessages = new ArrayList<>(cwMessagesList.keySet());
        int randomMessageNum = random.nextInt(allCWMessages.size());

        // Makes it so you can not get the same message twice in a row
        while (allCWMessages.get(randomMessageNum).equals(cwMessage)) {
            randomMessageNum = random.nextInt(allCWMessages.size());
        }

        cwMessage = allCWMessages.get(randomMessageNum);
        cwAudio = cwMessagesList.get(cwMessage);
        userInputTextArea.clear();

        // For testing
        System.out.println(cwMessage + " " + cwAudio);
    }

    // Switches screen to controls screen
    @FXML private void switchToSettingsView() throws IOException, InterruptedException {
        stopAudioPlayback();
        App.setRoot("ControlMenuView");
    }

    // Switches view to main menu
    @FXML private void switchToHomeScreenView() throws IOException, InterruptedException {
        stopAudioPlayback();
        App.setRoot("HomeScreenView");
    }

}
