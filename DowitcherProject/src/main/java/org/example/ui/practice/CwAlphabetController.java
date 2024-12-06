package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.App;
import org.example.data.User;
import org.example.utility.RadioFunctions;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.Sound;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.*;

public class CwAlphabetController implements MorseCodeOutput{

    // All FXML elements on screen that are interacted with
    @FXML private Button checkAnswerButton;
    @FXML private Button skipNextButton;
    @FXML private Button restartButton;
    @FXML private Button settingsButton;
    @FXML private Button practiceMenuButton;
    @FXML private Button mainMenuButton;
    @FXML private Button showLetterButton;
    @FXML private Button paddleModeButton;
    @FXML private Button straightKeyModeButton;
    @FXML private ScrollPane previousTranslationsScrollPane;
    @FXML private AnchorPane previousTranslationsAnchorPane;
    @FXML public TextFlow currentLetterTextFlow;
    @FXML private TextField cwInputTextField;

    private Random random = new Random();
    public Stack<String> cwStack = new Stack<>();
    public Stack<String> letterStack = new Stack<>();
    public String currentCW;
    public String currentLetter;
    private Text currentLetterText = new Text();
    private boolean correctAnswer = false;
    private int numCorrect;
    private VBox translationsContainer = new VBox();
    private final String textSize = "20";
    private boolean showLetter = true;
    @FXML private RadioFunctions radioFunctions;
    private Sound sound;
    private List<TextFlow> checkedInputList = new ArrayList<>();
    private List<Text> checkedLetterList = new ArrayList<>();

    //All view switching button presses
    @FXML private void handleSettingsButton() throws IOException {
        radioFunctions.stopTypingMode();
        App.settingsView();
    }
    @FXML private void handlePracticeMenuButton() throws IOException {
        radioFunctions.stopTypingMode();
        App.practiceMenuView();
    }
    @FXML private void handleMainMenuButton() throws IOException {
        radioFunctions.stopTypingMode();
        App.homeScreenView();
    }

    @FXML
    private void initialize() {
        App.currentUser.addView("CwAlphabetView");
        currentLetterText.setFont(new Font(48));
        currentLetterTextFlow.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        previousTranslationsScrollPane.setContent(translationsContainer);
        radioFunctions = new RadioFunctions(this);
        sound = new Sound();
        restartAlphabet();

        previousTranslationsAnchorPane.setDisable(true);
        previousTranslationsAnchorPane.setStyle("-fx-opacity: 1.0;");
        cwInputTextField.setDisable(true);
        cwInputTextField.setStyle("-fx-opacity: 1.0;");
    }


    public void generateRandomOrder() {
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
        String userTranslation = cwInputTextField.getText();

        List<Object> checkedList = new ArrayList<>(RadioFunctions.checkTranslation(userTranslation, currentCW, textSize));
        TextFlow checkedUserInput = (TextFlow) checkedList.get(0);
        correctAnswer = (boolean) checkedList.get(2);

        if (correctAnswer) {
            skipNextButton.setText("Next");
        }

        Text letter = new Text(currentLetter);
        letter.setStyle("-fx-font-size: 25px;");
        TextFlow letterToDisplay = new TextFlow(letter);

        // Add the new TextFlows to the VBox container
        HBox translationPair = new HBox(10); // To display the two TextFlows side by side

        checkedInputList.add(checkedUserInput);
        checkedLetterList.add(letter);

        if (showLetter) {
            translationPair.getChildren().addAll(letterToDisplay, checkedUserInput);
        } else {
            translationPair.getChildren().addAll(checkedUserInput);
        }

        // Add spacing and style if needed
        translationPair.setSpacing(20); // Spacing between the user input and correct translation

        translationsContainer.getChildren().add(translationPair);

        // Ensure the scroll pane scrolls to the bottom after adding new content
        previousTranslationsScrollPane.layout();
        previousTranslationsScrollPane.setVvalue(1.0);

        cwInputTextField.clear();
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
    public void generateNewLetter() {
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

        if (!showLetter) {
            currentLetterTextFlow.getChildren().clear();
        }
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

    @FXML
    private void handleShowLetter() {
        if (showLetter) {
            showLetter = false;
            currentLetterTextFlow.getChildren().clear();

            translationsContainer.getChildren().clear();
            for( int i = 0; i < checkedInputList.size(); i++) {
                HBox translationPair = new HBox(10); // To display the two TextFlows side by side
                translationPair.getChildren().addAll(checkedInputList.get(i));
                translationPair.setSpacing(20); // Spacing between the user input and correct translation
                translationsContainer.getChildren().add(translationPair);
            }

            showLetterButton.setText("Show Character");
        } else {
            showLetter = true;
            currentLetterTextFlow.getChildren().clear();
            currentLetterTextFlow.getChildren().add(currentLetterText);

            translationsContainer.getChildren().clear();
            for( int i = 0; i < checkedInputList.size(); i++) {
                HBox translationPair = new HBox(10); // To display the two TextFlows side by side
                translationPair.getChildren().addAll(checkedLetterList.get(i), checkedInputList.get(i));
                translationPair.setSpacing(20); // Spacing between the user input and correct translation
                translationsContainer.getChildren().add(translationPair);
            }

            showLetterButton.setText("Hide Character");
        }

        // Ensure the scroll pane scrolls to the bottom after adding new content
        previousTranslationsScrollPane.layout();
        previousTranslationsScrollPane.setVvalue(1.0);
    }

    @FXML
    private void playAudio() throws InterruptedException {
        sound.playAudio(0, currentCW);
    }

    @FXML
    private void handlePaddleMode() {
        paddleModeButton.setDisable(true);
        straightKeyModeButton.setDisable(false);
        //currentModeLabel.setText("Current Mode - Paddle");

        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Paddle", this);
    }

    @FXML
    private void handleStraightKeyMode() {
        paddleModeButton.setDisable(false);
        straightKeyModeButton.setDisable(true);
        //currentModeLabel.setText("Current Mode - Straight Key");

        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Straight", this);
    }

    public void addCwToInput(String cwChar) {
        String currentText = cwInputTextField.getText();

        if (cwChar.equals("/")) {
            if (!currentText.isEmpty() && currentText.charAt(currentText.length() - 1) != '/') {
                cwInputTextField.setText(currentText + "/");
            }
        } else if (cwChar.equals(" ")) {
            if (!currentText.isEmpty() && currentText.charAt(currentText.length() - 1) != ' ' && currentText.charAt(currentText.length() - 1) != '/') {
                cwInputTextField.setText(currentText + " ");
            }
        } else {
            cwInputTextField.setText(currentText + cwChar);
        }
    }

    @FXML
    private void clearInput() {
        cwInputTextField.clear();
    }

}
