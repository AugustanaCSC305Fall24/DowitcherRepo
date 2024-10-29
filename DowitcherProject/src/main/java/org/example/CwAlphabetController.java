package org.example;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private VBox translationsContainer = new VBox();
    private final String textSize = "20";

    //All view switching button presses
    @FXML private void handleSettingsButton() throws IOException {App.settingsView();}
    @FXML private void handlePracticeMenuButton() throws IOException {App.practiceMenuView();}
    @FXML private void handleMainMenuButton() throws IOException {App.homeScreenView();}

    @FXML
    private void initialize() {
        App.currentUser.addView("CwAlphabetView");
        currentLetterText.setFont(new Font(48));
        currentLetterTextFlow.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        previousTranslationsScrollPane.setContent(translationsContainer);
        restartAlphabet();
    }


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
        String userTranslation = userInputTextField.getText();

        List<Object> checkedList = new ArrayList<>(RadioFunctions.checkTranslation(userTranslation, currentCW, textSize));
        TextFlow checkedUserInput = (TextFlow) checkedList.get(0);
        TextFlow correctTranslation = (TextFlow) checkedList.get(1);
        correctAnswer = (boolean) checkedList.get(2);

        if (correctAnswer) {
            skipNextButton.setText("Next");
        }

        Text letter = new Text(currentLetter);
        letter.setStyle("-fx-font-size: 25px;");
        TextFlow letterToDisplay = new TextFlow(letter);

        // Add the new TextFlows to the VBox container
        HBox translationPair = new HBox(10); // To display the two TextFlows side by side
        translationPair.getChildren().addAll(letterToDisplay, checkedUserInput, correctTranslation);

        // Add spacing and style if needed
        translationPair.setSpacing(20); // Spacing between the user input and correct translation

        translationsContainer.getChildren().add(translationPair);

        // Ensure the scroll pane scrolls to the bottom after adding new content
        previousTranslationsScrollPane.layout();
        previousTranslationsScrollPane.setVvalue(1.0);

        userInputTextField.clear();
    }

    @FXML
    private void handleSkipNextButton() {
        if (correctAnswer) {
            skipNextButton.setText("Skip");
            numCorrect++;
        }

        generateNewLetter();
        correctAnswer = false;

        // For Testing
        System.out.println("numCorrect - " + numCorrect);
    }

    @FXML
    private void generateNewLetter() {
        if (!cwStack.isEmpty() || !letterStack.isEmpty()) {
            currentCW = cwStack.pop();
            currentLetter = letterStack.pop();

            // For Testing
            System.out.println(currentCW);
            System.out.println(currentLetter);

        } else {
            currentCW = "";
            currentLetter = "Done";
        }

        currentLetterText.setText(currentLetter);
        currentLetterTextFlow.getChildren().clear();
        currentLetterTextFlow.getChildren().add(currentLetterText);
    }


    @FXML
    private void restartAlphabet() {
        cwStack.clear();
        letterStack.clear();
        translationsContainer.getChildren().clear();
        numCorrect = 0;

        generateRandomOrder();
        generateNewLetter();
    }
}
