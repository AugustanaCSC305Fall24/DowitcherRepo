package org.example.ui.practice;

import org.example.App;
import org.example.utility.RadioFunctions;
import org.example.utility.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.data.User;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PracticeListeningController {

    // All FXML elements on screen that are interacted with
    @FXML private Button mainMenuButton;
    @FXML private Button goToSettingsButton;
    @FXML private Button playPauseAudioButton;
    @FXML private Button restartAudioButton;
    @FXML private Button checkTranslationButton;
    @FXML private Button newAudioButton;
    @FXML private Button practiceMenuButton;
    @FXML private TextField userInputTextField;
    @FXML private ScrollPane userInputScrollPane;

    private String cwMessage;
    private String cwAudio;
    HashMap<String, String> cwMessagesList;
    Random random = new Random();


    private Boolean isPaused = true;
    private int pauseIndex;
    private Thread audioThread;
    private volatile boolean stopAudio = false;

    private final List<TextFlow> userInputsList = new ArrayList<>();
    private final List<TextFlow> correctTranslationsList = new ArrayList<>();
    private final String textSize = "16";

    //All view switching button presses
    @FXML private void switchToSettingsView() throws IOException, InterruptedException {stopAudioPlayback();
        App.settingsView();}
    @FXML private void switchToHomeScreenView() throws IOException, InterruptedException {stopAudioPlayback();App.homeScreenView();}
    @FXML private void handlePracticeMenuButton() throws IOException, InterruptedException {stopAudioPlayback();App.practiceMenuView();}

    @FXML
    // Initializes HashMap and fills it with all practice messages
    // Then generates a starting message
    private void initialize() {
        cwMessagesList = (HashMap<String, String>) MorseCodeTranslator.getCwMessagesMap();
        newAudio();
        App.currentUser.addView("PracticeListeningView");

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
                    try {
                        Sound.playDah();
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                } else if (messageArray[i] == '.') {
                    try {
                        Sound.playDit();
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                } else if (messageArray[i] == ' ') {
                    try {
                        Thread.sleep(User.getCwSpeed());
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

    // Stops the current audio that is playing
    private void stopAudioPlayback() throws InterruptedException {
        if (audioThread != null && audioThread.isAlive()) {
            stopAudio = true;
            audioThread.join();
            stopAudio = false;
        }
    }

    @FXML
    // Checks the translation put in by the user and text is displayed as green is character is correct
    // or is set as red if character is incorrect
    // Displays correct letters of the translation and _ for incorrect letter
    private void checkTranslation() {
        String userTranslation = userInputTextField.getText();

        List<Object> checkedList = new ArrayList<>(RadioFunctions.checkTranslation(userTranslation, cwMessage, textSize));
        TextFlow checkedUserInput = (TextFlow) checkedList.get(0);

        // Specific to PracticeListening
        updateCheckedTranslations(checkedUserInput);
    }

    @FXML
    // Adds lines to the userInputScrollPane and correctAnswerScrollPane while keeping previous lines
    private void updateCheckedTranslations(TextFlow userInput) {
        // Add new translations to the lists
        userInputsList.add(userInput);

        // Clear previous content from ScrollPanes
        userInputScrollPane.setContent(null);

        // Create a new TextFlow to hold all previous translations
        TextFlow allUserInputs = new TextFlow();

        // Append all previous user inputs with line breaks
        for (TextFlow input : userInputsList) {
            allUserInputs.getChildren().add(input);
            allUserInputs.getChildren().add(new Text("\n"));
        }

        // Append all previous correct translations with line breaks


        // Set the updated content in the ScrollPanes
        userInputScrollPane.setContent(allUserInputs);

        // Clear the input field
        userInputTextField.clear();
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

        TextFlow userInputLineBreak = generateLineBreak();
        updateCheckedTranslations(userInputLineBreak);

        cwMessage = allCWMessages.get(randomMessageNum);
        cwAudio = cwMessagesList.get(cwMessage);
        userInputTextField.clear();

        // For testing
        System.out.println(cwMessage + " " + cwAudio);
    }

    @FXML
    // Generates a new line break for the ScrollPanes
    private TextFlow generateLineBreak() {
        TextFlow lineBreak = new TextFlow();
        int lineBreakLength = 75;
        for (int i = 0; i < lineBreakLength; i++) {
            Text dash = new Text("-");
            dash.setStyle("-fx-fill: black;");
            lineBreak.getChildren().add(dash);
        }
        return lineBreak;
    }

}
