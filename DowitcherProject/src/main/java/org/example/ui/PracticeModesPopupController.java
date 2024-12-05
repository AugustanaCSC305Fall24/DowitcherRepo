package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.App;

import java.io.IOException;

public class PracticeModesPopupController {

    @FXML private Button listeningButton;
    @FXML private Button typingButton;
    @FXML private Button tuningButton;
    @FXML private Button cwAlphabetButton;

    // Handle Listening Button
    @FXML
    private void handleListening(ActionEvent event) throws IOException {
        App.practiceListeningView();
    }

    // Handle Typing Button
    @FXML
    private void handleTyping(ActionEvent event) throws IOException {
        App.practiceTypingView();
    }

    // Handle Tuning Button
    @FXML
    private void handleTuning(ActionEvent event) throws IOException {
        App.practiceTuningView();
    }

    // Handle CW Alphabet Button
    @FXML
    private void handleCwAlphabet(ActionEvent event) throws IOException {
        App.cwAlphabetView();
    }
}
