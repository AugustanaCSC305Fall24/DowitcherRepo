package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.App;
import org.example.utility.MorseCodeTranslator;

import java.io.IOException;
import java.util.HashMap;

public class PracticeMenuController {
    //Buttons
    @FXML private Button toListenViewButton;
    @FXML private Button toTypeViewButton;
    @FXML private Button toTalkViewButton;
    @FXML private Button toTuningButton;
    @FXML private Button toAlphebetButton;
    @FXML private Button toMainMenuButton;
    @FXML private Button AITestButton;

    private void handleUnbuiltButton(Button object){object.setText("Not built yet!");}

    @FXML
    private void initialize() {
        App.currentUser.addView("PracticeMenuView");
    }

}
