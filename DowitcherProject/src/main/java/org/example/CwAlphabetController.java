package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;

import java.io.IOException;

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


    @FXML
    private void initialize() {
        App.currentUser.addView("CwAlphabetView");
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
