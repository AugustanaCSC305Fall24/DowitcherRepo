package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class PracticeMenuController {
    //Buttons
    @FXML private Button toListenViewButton;
    @FXML private Button toTypeViewButton;
    @FXML private Button toTalkViewButton;
    @FXML private Button toTuningButton;
    @FXML private Button toAlphebetButton;
    @FXML private Button toMainMenuButton;

    //Handlers
    @FXML private void handleToListeningButton() throws IOException {App.setRoot("PracticeListeningView");}
    @FXML private void handleToTypingButton() throws IOException {App.setRoot("PracticeTypingView");}
    @FXML private void handleToTalkingButton() throws IOException {App.setRoot("PracticeTalkingView");}
    @FXML private void handleToTuningButton() throws IOException {handleUnbuiltButton(toTuningButton);}                   // No view App.setRoot("PracticeTuningView")
    @FXML private void handleToAlphebetButton() throws IOException {App.setRoot("CwAlphabetView");}               // No view App.setRoot("PracticeAlphebetView")
    @FXML private void handleToMainMenuButton() throws IOException {App.setRoot("HomeScreenView");}

    private void handleUnbuiltButton(Button object){object.setText("Not built yet!");}

}
