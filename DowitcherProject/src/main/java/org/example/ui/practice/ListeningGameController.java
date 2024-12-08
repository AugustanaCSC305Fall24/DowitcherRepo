package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.App;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.RadioFunctions;
import org.example.utility.Sound;

import java.io.IOException;
import java.util.*;

public class ListeningGameController {

    @FXML private VBox rightVBox;

    private Button mainMenuButton;
    private Button playPauseAudioButton;
    private Button restartAudioButton;
    private Button checkTranslationButton;
    private Button newAudioButton;
    private TextField userInputTextField;
    private TextFlow userInputDisplay;
    private Label feedbackLabel;

    private Stack<String> cwMessageStack = new Stack<>();
    private Stack<String> cwAudioStack = new Stack<>();
    private String cwMessage;
    private String cwAudio;
    private boolean isPaused = true;

    private Sound sound = new Sound();
    private final List<TextFlow> userInputsList = new ArrayList<>();
    private final Random random = new Random();

    @FXML
    public void initialize() {
        if (rightVBox == null) {
            rightVBox = new VBox(); // Ensure rightVBox is initialized
        }

        initializeUIElements();
        generateRandomCwMessageOrder();
        newAudio();
    }

    private void initializeUIElements() {
        // Initialize buttons
        mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(e -> switchToMainMenu());

        playPauseAudioButton = new Button("Play");
        playPauseAudioButton.setOnAction(e -> {
            try {
                playPauseAudio();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        restartAudioButton = new Button("Restart");
        restartAudioButton.setOnAction(e -> {
            try {
                restartAudio();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        checkTranslationButton = new Button("Check");
        checkTranslationButton.setOnAction(e -> checkTranslation());

        newAudioButton = new Button("New Audio");
        newAudioButton.setOnAction(e -> newAudio());

        userInputTextField = new TextField();
        userInputDisplay = new TextFlow();
        feedbackLabel = new Label();

        // Add components to the right VBox
        rightVBox.getChildren().addAll(
                new Label("Practice Listening"),
                mainMenuButton, playPauseAudioButton, restartAudioButton,
                newAudioButton, checkTranslationButton,
                new Label("Your Input: "), userInputTextField,
                new Label("Feedback: "), feedbackLabel,
                new Label("Previous Inputs: "), userInputDisplay
        );
    }

    private void switchToMainMenu() {
        try {
            sound.stopAudioPlayback();
            App.homeScreenView();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void playPauseAudio() throws InterruptedException {
        sound.playPauseAudio(cwAudio);
        if (isPaused) {
            isPaused = false;
            playPauseAudioButton.setText("Pause");
        } else {
            isPaused = true;
            playPauseAudioButton.setText("Play");
        }
    }

    private void restartAudio() throws InterruptedException {
        sound.restartAudio();
        playPauseAudioButton.setText("Play");
    }

    private void checkTranslation() {
        String userTranslation = userInputTextField.getText();
        List<Object> checkedList = new ArrayList<>(RadioFunctions.checkTranslation(userTranslation, cwMessage, "16"));
        TextFlow checkedUserInput = (TextFlow) checkedList.get(0);

        // Update user inputs
        updateCheckedTranslations(checkedUserInput);
    }

    private void updateCheckedTranslations(TextFlow userInput) {
        userInputsList.add(userInput);

        // Clear previous content
        userInputDisplay.getChildren().clear();

        // Append all previous inputs
        for (TextFlow input : userInputsList) {
            userInputDisplay.getChildren().add(input);
            userInputDisplay.getChildren().add(new Text("\n"));
        }

        // Clear the input field
        userInputTextField.clear();
    }

    private void newAudio() {
        playPauseAudioButton.setText("Play");

        if (cwMessageStack.isEmpty() || cwAudioStack.isEmpty()) {
            generateRandomCwMessageOrder();
        }

        cwMessage = cwMessageStack.pop();
        cwAudio = cwAudioStack.pop();
        userInputTextField.clear();
        feedbackLabel.setText("");

        // For debugging
        System.out.println("New Message: " + cwMessage);
    }

    private void generateRandomCwMessageOrder() {
        List<Map.Entry<String, String>> cwMessages = new ArrayList<>(MorseCodeTranslator.getCwMessagesMap().entrySet());

        while (!cwMessages.isEmpty()) {
            int randIndex = random.nextInt(cwMessages.size());
            Map.Entry<String, String> entry = cwMessages.get(randIndex);

            cwMessageStack.push(entry.getKey());
            cwAudioStack.push(entry.getValue());

            cwMessages.remove(randIndex);
        }
    }
}
