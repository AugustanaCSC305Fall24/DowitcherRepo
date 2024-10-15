package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.example.Sound.playDitOrDah;


public class PracticeListeningController {

    // All FXML elements on screen that are interacted with
    @FXML private Button mainMenuButton;
    @FXML private Button goToSettingsButton;
    @FXML private Button playAudioButton;
    @FXML private Button checkTranslationButton;
    @FXML private Button newAudioButton;
    @FXML private TextArea userInputTextArea;
    @FXML private ScrollPane correctAnswerScrollPane;


    private String cwMessage;
    private String cwAudio;
    HashMap<String, String> cwMessagesList;
    Random random = new Random();

    @FXML
    // Initializes HashMap and fills it with all practice messages
    // Then generates a starting message
    private void initialize() {
        cwMessagesList = new HashMap<>();
        cwMessagesList.put("SOS", "... --- ...");
        cwMessagesList.put("ALPHA", ".- .-.. .--. .... .-");
        cwMessagesList.put("BRAVO", "-... .-. .- ...- ---");
        cwMessagesList.put("PAPA", ".--. .- .--. .-.. ---");
        cwMessagesList.put("HI", ".... ..");
        cwMessagesList.put("OK", "--- -.-");
        cwMessagesList.put("YES", "-.-- . ...");
        cwMessagesList.put("NO", "-. ---");
        newAudio();
        App.currentUser.addView("PracticeListeningView");
        App.getScene().setOnKeyPressed(event -> {
            try {
                handleKeyPress(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    // Plays audio for cwMessage to be translated
    private void playAudio() {
        for (int i = 0; i < cwAudio.length(); i++) {
            if (cwAudio.charAt(i) == '.') {
                playDitOrDah("/dit.wav");
            }
            if (cwAudio.charAt(i) == '-') {
                playDitOrDah("/dah.wav");
            }
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
        List<String> allCWMessages = new ArrayList<>(cwMessagesList.keySet());
        int randomMessageNum = random.nextInt(allCWMessages.size());

        // Makes it so you can not get the same message twice in a row
        while (allCWMessages.get(randomMessageNum).equals(cwMessage)) {
            randomMessageNum = random.nextInt(allCWMessages.size());
        }

        cwMessage = allCWMessages.get(randomMessageNum);
        cwAudio = cwMessagesList.get(cwMessage);

        // For testing
        System.out.println(cwMessage + " " + cwAudio);
    }

    // Switches screen to controls screen
    @FXML private void switchToSettingsView() throws IOException{App.setRoot("ControlMenuView");}

    // Switches view to main menu
    @FXML private void switchToHomeScreenView() throws IOException{App.setRoot("HomeScreenView");}

    private void handleKeyPress(KeyEvent event) throws IOException {
        String pressedKey = event.getCode().toString(); // Get the pressed key as a string

        // Check if the pressed key has a corresponding action in the map
        String action = App.currentUser.getKeyFirstActionMap().get(pressedKey);
        if (action != null) {
            switch (action) {
                case "playAudio":
                    playAudio();
                    System.out.println("Playing audio...");
                    break;
                case "checkTranslation":
                    checkTranslation();
                    System.out.println("Checking translating...");
                    break;
                case "newAudio":
                    newAudio();
                    System.out.println("New audio...");
                    break;
                case "settings":
                    switchToSettingsView();
                    System.out.println("Switching to controls view.");
                    break;
                case "mainMenu":
                    switchToHomeScreenView();
                    System.out.println("Switching to main menu.");
                    break;
                default:
                    System.out.println("No action assigned for this key.");
                    break;
            }
        }
    }

}
