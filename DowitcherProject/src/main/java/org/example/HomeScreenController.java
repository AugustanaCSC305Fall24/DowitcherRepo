package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.IOException;

public class HomeScreenController {
    //Buttons
    @FXML private Button toLiveChatViewButton;
    @FXML private Button toPracticeMenuButton;
    @FXML private Button toTutorialViewButton;
    @FXML private Button toSettingsButton;
    @FXML private Button exitProgramButton;

    //Handlers
    @FXML private void handleToLiveChatButton(ActionEvent event) {handleUnbuiltButton(toLiveChatViewButton);}
    @FXML private void handleToPracticeMenuButton(ActionEvent actionEvent) throws IOException {App.setRoot("PracticeMenuView");}
    @FXML private void handleToTutorialButton(ActionEvent event) {handleUnbuiltButton(toTutorialViewButton);}
    @FXML private void handleToSettingsButton(ActionEvent actionEvent) throws IOException {App.setRoot("SettingsView");}
    @FXML private void handleExitProgramButton() {Stage stage = (Stage) exitProgramButton.getScene().getWindow();stage.close();}


    private void handleUnbuiltButton(Button object){object.setText("Not For This Sprint!");}

}
