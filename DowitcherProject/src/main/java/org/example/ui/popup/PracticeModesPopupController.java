package org.example.ui.popup;

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
        App.generalizedHamRadioView("ListeningGameController");
    }

    @FXML
    private void handleTyping(ActionEvent event) throws IOException {
        App.generalizedHamRadioView("TypingGameController");
    }

    @FXML
    private void handleTuning(ActionEvent event) throws IOException {
        App.generalizedHamRadioView("TuningGameController");
    }

    @FXML
    private void handleCwAlphabet(ActionEvent event) throws IOException {
        App.generalizedHamRadioView("AlphabetGameController");
    }

}
