package org.example.ui.practice;

import javafx.scene.layout.AnchorPane;
import org.example.App;
import org.example.utility.RadioFunctions;
import org.example.utility.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.*;

public class ListeningGameController {

    // All FXML elements on screen that are interacted with
    @FXML private Button mainMenuButton;
    @FXML private Button goToSettingsButton;
    @FXML private Button playPauseAudioButton;
    @FXML private Button restartAudioButton;
    @FXML private Button checkTranslationButton;
    @FXML private Button newAudioButton;
    @FXML private TextField userInputTextField;
    @FXML private ScrollPane userInputScrollPane;
    @FXML private AnchorPane userInputAnchorPane;

    private Stack<String> cwMessageStack = new Stack<>();
    private Stack<String> cwAudioStack = new Stack<>();
    private String cwMessage;
    private String cwAudio;

    Random random = new Random();

    private Boolean isPaused = true;
    private Sound sound = new Sound();

    private final List<TextFlow> userInputsList = new ArrayList<>();
    private final String textSize = "16";

    //All view switching button presses
    @FXML private void switchToSettingsView() throws IOException, InterruptedException {sound.stopAudioPlayback();
        App.settingsPopupView();}
    @FXML private void switchToHomeScreenView() throws IOException, InterruptedException {sound.stopAudioPlayback();App.homeScreenView();}

    @FXML
    // Initializes HashMap and fills it with all practice messages
    // Then generates a starting message
    private void initialize() {
        generateRandomCwMessageOrder();
        newAudio();
        App.currentUser.addView("PracticeListeningView");
        userInputAnchorPane.setDisable(true);
        userInputAnchorPane.setStyle("-fx-opacity: 1.0;");
    }

    @FXML
    // If audio is paused, plays audio and changes button to say pause
    // If audio is playing, pauses audio and changes button to say play
    private void playPauseAudio() throws InterruptedException {
        sound.playPauseAudio(cwAudio);
        if (isPaused){
            isPaused = false;
            playPauseAudioButton.setText("Pause");
        } else {
            isPaused = true;
            playPauseAudioButton.setText("Play");
        }
    }

    @FXML
    // Restarts the audio that is playing
    private void restartAudio() throws InterruptedException {
        sound.restartAudio();
        playPauseAudioButton.setText("Play");
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
        playPauseAudioButton.setText("Play");

        if(cwMessageStack.isEmpty() || cwAudioStack.isEmpty()) {
            generateRandomCwMessageOrder();
        }
        cwMessage = cwMessageStack.pop();
        cwAudio = cwAudioStack.pop();
        userInputTextField.clear();

        // For testing
        System.out.println(cwMessage + " " + cwAudio);
    }

    private void generateRandomCwMessageOrder () {
        List<Map.Entry<String, String>> cwMessages = new ArrayList<>(MorseCodeTranslator.getCwMessagesMap().entrySet());

        while (!cwMessages.isEmpty()) {
            int randIndex = random.nextInt(cwMessages.size());
            Map.Entry<String, String> entry = cwMessages.get(randIndex);

            String cwMessage = entry.getKey();
            String audio = entry.getValue();

            cwMessageStack.push(cwMessage);
            cwAudioStack.push(audio);

            cwMessages.remove(randIndex);
        }

    }

    @FXML
    // Generates a new line break for the ScrollPanes
    private TextFlow generateLineBreak() {
        TextFlow lineBreak = new TextFlow();
        int lineBreakLength = 72;
        for (int i = 0; i < lineBreakLength; i++) {
            Text dash = new Text("-");
            dash.setStyle("-fx-fill: black;");
            lineBreak.getChildren().add(dash);
        }
        return lineBreak;
    }

}
