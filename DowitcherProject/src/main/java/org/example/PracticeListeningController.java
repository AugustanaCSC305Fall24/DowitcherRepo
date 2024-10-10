package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class PracticeListeningController{


    // Buttons on screen
    @FXML private Button backButton;
    @FXML private Button goToSettingsButton;
    @FXML private Button replayAudioButton;
    @FXML private Button checkTranslationButton;
    @FXML private TextArea userInputTextArea;
    @FXML private TextArea correctAnswerTextArea;

    @FXML private void switchToHomeScreenView() throws IOException{App.setRoot("HomeScreenView");}

}
