package org.example;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import java.io.IOException;

public class SettingMenuController extends SwitchTo {
    @FXML
    private Button switchToControlMenuButton;

    @FXML
    private Button switchToGameSettingButton;

    @FXML
    private Button switchToMainMenuButton;

    @FXML
    private Button switchToTutorialViewButton;

    @FXML
    private void handleTutorialButton(){
        switchToTutorialViewButton.setText("No For This Sprint!");
    }
}
