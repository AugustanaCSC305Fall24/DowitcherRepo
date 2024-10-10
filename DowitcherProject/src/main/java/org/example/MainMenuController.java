package org.example;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController extends SwitchTo{
    @FXML
    private Button switchToLiveChatButton;

    @FXML
    private Button switchToPracticeMenuButton;

    @FXML
    private Button switchToSettingsButton;

    @FXML
    private Button exitProgramButton;

    @FXML
    private void handleLiveChatButton(){
        SwitchTo.handleUnbuiltButton(switchToLiveChatButton);
    }

    @FXML
    private void handleExitProgramButton() {
        Stage stage = (Stage) exitProgramButton.getScene().getWindow();
        stage.close();
    }

}
