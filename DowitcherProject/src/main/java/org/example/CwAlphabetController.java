package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.*;

public class CwAlphabetController {

    // All FXML elements on screen that are interacted with
    @FXML private Button checkAnswerButton;
    @FXML private Button skipNextButton;
    @FXML private Button restartButton;
    @FXML private Button settingsButton;
    @FXML private Button practiceMenuButton;
    @FXML private Button mainMenuButton;
    @FXML private ScrollPane previousTranslationsScrollPane;
    @FXML private TextFlow currentLetterTextFlow;
    @FXML private TextField userInputTextField;

    private Random random = new Random();
    private Stack<String> cwStack = new Stack<>();
    private Stack<String> letterStack = new Stack<>();
    private String currentCW;
    private String currentLetter;
    private Text currentLetterText = new Text();
    private boolean correctAnswer = false;
    private int numCorrect;


    @FXML
    private void initialize() {
        App.currentUser.addView("CwAlphabetView");
        currentLetterText.setFont(new Font(48));
        currentLetterTextFlow.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        restartAlphabet();
    }

    @FXML
    private void generateRandomOrder() {
        List<Map.Entry<String, String>> cwAlphabetList = new ArrayList<>(MorseCodeTranslator.getCwAlphabet().entrySet());

        while (!cwAlphabetList.isEmpty()) {
            int randIndex = random.nextInt(cwAlphabetList.size());
            Map.Entry<String, String> entry = cwAlphabetList.get(randIndex);

            String cwMessage = entry.getKey();
            String letter = entry.getValue();

            cwStack.push(cwMessage);
            letterStack.push(letter);

            cwAlphabetList.remove(randIndex);
        }

    }

    @FXML
    private void checkAnswer() {

    }

    @FXML
    private void handleSkipNextButton() {
        if (correctAnswer) {
            skipNextButton.setText("Skip");
        }

        generateNewLetter();
        correctAnswer = false;
    }

    @FXML
    private void generateNewLetter() {
        currentCW = cwStack.pop();
        currentLetter = letterStack.pop();
        System.out.println(currentCW);
        System.out.println(currentLetter);

        currentLetterText.setText(currentLetter);
        currentLetterTextFlow.getChildren().clear();
        currentLetterTextFlow.getChildren().add(currentLetterText);
    }

    @FXML
    private void restartAlphabet() {
        cwStack.clear();
        letterStack.clear();

        generateRandomOrder();
        generateNewLetter();
    }

    @FXML
    private void handleSettingsButton() throws IOException {
        App.setRoot("SettingsView");
    }

    @FXML
    private void handlePracticeMenuButton() throws IOException {
        App.setRoot("PracticeMenuView");
    }

    @FXML
    private void handleMainMenuButton() throws IOException {
        App.setRoot("HomeScreenView");
    }

}
