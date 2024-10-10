package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PracticeMenuController extends SwitchTo {

    // UI elements on screen
    @FXML private Label practiceMenuTitle;

    // Buttons on screen
    @FXML private Button practiceMenuToListenButton;
    @FXML private Button practiceMenuToTypeButton;
    @FXML private Button practiceMenuToTalkButton;
    @FXML private Button practiceMenuToMainMenuButton;

    @FXML
    private void handleToTalkButton(){
        SwitchTo.handleUnbuiltButton(practiceMenuToTalkButton);
    }

}
