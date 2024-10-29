package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.App;


import java.io.IOException;

public class HomeScreenController {
    //Buttons
    @FXML private Button toLiveChatViewButton;
    @FXML private Button toPracticeMenuButton;
    @FXML private Button toTutorialViewButton;
    @FXML private Button toSettingsButton;
    @FXML private Button exitProgramButton;

    //All view switching button presses
    @FXML private void handleToLiveChatButton(ActionEvent event) {handleUnbuiltButton(toLiveChatViewButton);}
    @FXML private void handleToPracticeMenuButton(ActionEvent actionEvent) throws IOException {
        App.practiceMenuView();}
    @FXML private void handleToTutorialButton(ActionEvent event) {handleUnbuiltButton(toTutorialViewButton);}
    @FXML private void handleToSettingsButton(ActionEvent actionEvent) throws IOException {App.settingsView();}
    @FXML private void handleExitProgramButton() {App.exitProgram();}

    private void handleUnbuiltButton(Button object){object.setText("Not For This Sprint!");}

    @FXML private void initialize() {App.currentUser.addView("HomeScreenView");}
}
