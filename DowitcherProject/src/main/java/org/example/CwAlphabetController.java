package org.example;

import javafx.fxml.FXML;
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
        String userTranslation = userInputTextField.getText();
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

            checkedUserInput.setStyle("-fx-font-size: 20px;");
            checkedCorrectLetter.setStyle("-fx-font-size: 20px;");

            checkedUserInput.getChildren().addAll(checkedUserLetter);
            correctTranslation.getChildren().addAll(checkedCorrectLetter);

        }

        // Displays the remaining cwMessage as incorrect if user's input
        // is shorter than the correct message.
        if (currentCW.length() > userTranslation.length()) {
            for (int i = userTranslation.length(); i < currentCW.length(); i++) {
                checkedCorrectLetter = new Text("_");
                checkedCorrectLetter.setStyle("-fx-font-size: 20px;");
                correctTranslation.getChildren().addAll(checkedCorrectLetter);
                numIncorrect++;
            }
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

        if (numIncorrect == 0) {
            skipNextButton.setText("Next");
            correctAnswer = true;
        }

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
