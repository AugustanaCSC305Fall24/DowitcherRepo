package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.App;
import org.example.ui.MorseCodeOutput;
import org.example.utility.RadioFunctions;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.Sound;

import java.io.IOException;
import java.util.*;

public class AlphabetGameController implements MorseCodeOutput {

    // All FXML elements on screen that are interacted with
    @FXML private VBox rightVBox;
    @FXML private HBox topHBox;
    @FXML private ScrollPane previousTranslationsScrollPane;
    @FXML private AnchorPane previousTranslationsAnchorPane;
    @FXML private TextFlow currentLetterTextFlow;
    @FXML private TextField cwInputTextField;
    @FXML private BorderPane mainBorderPane;

    private Button checkAnswerButton;
    private Button skipNextButton;
    private Button restartButton;
    private Button settingsButton;
    private Button practiceMenuButton;
    private Button mainMenuButton;
    private Button showLetterButton;
    private Button paddleModeButton;
    private Button straightKeyModeButton;

    private Random random = new Random();
    private Stack<String> cwStack = new Stack<>();
    private Stack<String> letterStack = new Stack<>();
    private String currentCW;
    private String currentLetter;
    private Text currentLetterText = new Text();
    private boolean correctAnswer = false;
    private int numCorrect;
    private VBox translationsContainer = new VBox();
    private final String textSize = "42";
    private boolean showLetter = true;
    @FXML private RadioFunctions radioFunctions;
    private Sound sound;
    private List<TextFlow> checkedInputList = new ArrayList<>();
    private List<Text> checkedLetterList = new ArrayList<>();

    // Room Name
    private final String ROOM_NAME = "Alphabet Game";

    @FXML
    private void handleSettingsButton() throws IOException {
        radioFunctions.stopTypingMode();
        App.settingsPopupView();
    }

    @FXML
    private void handlePracticeMenuButton() throws IOException {
        radioFunctions.stopTypingMode();
        App.practiceModesPopupView();
    }

    @FXML
    private void handleMainMenuButton() throws IOException {
        radioFunctions.stopTypingMode();
        App.homeScreenView();
    }

    @FXML
    private void initialize() {
        if (rightVBox == null) {
            rightVBox = new VBox(); // Initialize VBox if not provided
        }
        if (cwInputTextField == null) {
            cwInputTextField = new TextField(); // Initialize the TextField manually
        }
        cwInputTextField.setEditable(true);
        cwInputTextField.setPromptText("Enter Morse Code");

        // Ensure previousTranslationsScrollPane is initialized if it's null
        if (previousTranslationsScrollPane == null) {
            previousTranslationsScrollPane = new ScrollPane(); // Initialize ScrollPane
        }

        // Ensure previousTranslationsAnchorPane is initialized if it's null
        if (previousTranslationsAnchorPane == null) {
            previousTranslationsAnchorPane = new AnchorPane(); // Initialize AnchorPane
        }

        App.currentUser.addView("CwAlphabetView");

        // Initialize the currentLetterTextFlow
        currentLetterTextFlow = new TextFlow();
        currentLetterTextFlow.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        currentLetterText.setFont(new Font(48));
        currentLetterText.setStyle("-fx-fill: white;");

        // Initialize previousTranslationsScrollPane content
        previousTranslationsScrollPane.setContent(translationsContainer);
        mainBorderPane.setCenter(previousTranslationsScrollPane);

        // Initialize RadioFunctions and Sound
        radioFunctions = new RadioFunctions(this);
        sound = new Sound();

        // Call restart method
        restartAlphabet();

        // Disable previous translations container and input
        previousTranslationsAnchorPane.setDisable(true);
        previousTranslationsAnchorPane.setStyle("-fx-opacity: 1.0;");
        cwInputTextField.setDisable(true);
        cwInputTextField.setStyle("-fx-opacity: 1.0;");

        // Initialize all UI elements dynamically
        initializeUIElements();
    }

    private void initializeUIElements() {
        topHboxInitialized();
        // Create the necessary UI elements dynamically
        checkAnswerButton = new Button("Check Answer");
        checkAnswerButton.getStyleClass().add("custom-button");
        skipNextButton = new Button("Skip");
        skipNextButton.getStyleClass().add("custom-button");
        restartButton = new Button("Restart Alphabet");
        restartButton.getStyleClass().add("custom-button");
        settingsButton = new Button("Settings");
        restartButton.getStyleClass().add("custom-button");
        practiceMenuButton = new Button("Practice Menu");
        practiceMenuButton.getStyleClass().add("custom-button");
        mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().add("custom-button");
        showLetterButton = new Button("Hide Character");
        showLetterButton.getStyleClass().add("custom-button");
        paddleModeButton = new Button("Paddle Mode");
        paddleModeButton.getStyleClass().add("custom-button");
        straightKeyModeButton = new Button("Straight Key Mode");
        straightKeyModeButton.getStyleClass().add("custom-button");
        cwInputTextField = new TextField();
        cwInputTextField.setDisable(true);
        cwInputTextField.setOpacity(1);

        // Add handlers for buttons
        checkAnswerButton.setOnAction(e -> checkAnswer());
        skipNextButton.setOnAction(e -> handleSkipNextButton());
        restartButton.setOnAction(e -> restartAlphabet());
        settingsButton.setOnAction(e -> {
            try {
                handleSettingsButton();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        practiceMenuButton.setOnAction(e -> {
            try {
                handlePracticeMenuButton();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        mainMenuButton.setOnAction(e -> {
            try {
                handleMainMenuButton();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        showLetterButton.setOnAction(e -> handleShowLetter());
        paddleModeButton.setOnAction(e -> handlePaddleMode());
        straightKeyModeButton.setOnAction(e -> handleStraightKeyMode());

        // Add elements to rightVBox
        rightVBox.getChildren().addAll(
                checkAnswerButton,
                skipNextButton,
                restartButton,
                showLetterButton,
                currentLetterTextFlow,
                cwInputTextField,
                paddleModeButton,
                straightKeyModeButton
        );
    }

    private void topHboxInitialized() {
        if (topHBox == null) {
            topHBox = new HBox();
        }

        // Create the "Settings" button
        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(event -> App.togglePopup("SettingsPopup.fxml", settingsButton));
        settingsButton.getStyleClass().add("custom-button"); // Apply button style from the CSS

        // Create the "Menu" button
        Button menuButton = new Button("Menu");
        menuButton.setOnAction(event -> {
            try {
                App.homeScreenView();
            } catch (IOException e) {
                throw new RuntimeException("Failed to return to home screen", e);
            }
        });
        menuButton.getStyleClass().add("custom-button"); // Apply button style from the CSS

        // Add the buttons to the topHBox
        topHBox.getChildren().clear();
        topHBox.getChildren().addAll(menuButton, settingsButton);

        // Ensure topHBox has the expected components before adding "AI Chat" label
        Label screenName = new Label(ROOM_NAME);
        screenName.getStyleClass().add("label"); // Apply label style from the CSS
        topHBox.getChildren().add(1, screenName); // Insert label between menu and settings buttons
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
        letter.setStyle("-fx-font-size: 50px;");
        TextFlow letterToDisplay = new TextFlow(letter);

        HBox translationPair = new HBox(10); // To display the two TextFlows side by side

        checkedInputList.add(checkedUserInput);
        checkedLetterList.add(letter);

        if (showLetter) {
            translationPair.getChildren().addAll(letterToDisplay, checkedUserInput);
        } else {
            translationPair.getChildren().addAll(checkedUserInput);
        }

        translationPair.setSpacing(20); // Spacing between the user input and correct translation
        translationsContainer.getChildren().add(translationPair);
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
    }

    @FXML
    public void generateNewLetter() {
        if (!cwStack.isEmpty() || !letterStack.isEmpty()) {
            currentCW = cwStack.pop();
            currentLetter = letterStack.pop();
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
            for (int i = 0; i < checkedInputList.size(); i++) {
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
            for (int i = 0; i < checkedInputList.size(); i++) {
                HBox translationPair = new HBox(10); // To display the two TextFlows side by side
                translationPair.getChildren().addAll(checkedLetterList.get(i), checkedInputList.get(i));
                translationPair.setSpacing(20); // Spacing between the user input and correct translation
                translationsContainer.getChildren().add(translationPair);
            }

            showLetterButton.setText("Hide Character");
        }

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

        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Paddle", this);
    }

    @FXML
    private void handleStraightKeyMode() {
        paddleModeButton.setDisable(false);
        straightKeyModeButton.setDisable(true);

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
            if (!currentText.isEmpty() && currentText.charAt(currentText.length() - 1) != ' ') {
                cwInputTextField.setText(currentText + " ");
            }
        } else {
            cwInputTextField.setText(currentText + cwChar);
        }
    }
}
