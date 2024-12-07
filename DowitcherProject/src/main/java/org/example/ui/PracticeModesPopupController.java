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

    @FXML
    private void handleListening(ActionEvent event) throws IOException {
        App.generalizedHamRadioView("Listening Game");
    }

    @FXML
    private void handleTyping(ActionEvent event) throws IOException {
        App.generalizedHamRadioView("Typing Game");
    }

    @FXML
    private void handleTuning(ActionEvent event) throws IOException {
        App.generalizedHamRadioView("Tuning Game");
    }

    @FXML
    private void handleCwAlphabet(ActionEvent event) throws IOException {
        App.generalizedHamRadioView("Alphabet Game");
    }

}
