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

    //All view switching button presses
    @FXML private void handleToListeningButton() throws IOException {App.practiceListeiningView();}
    @FXML private void handleToTypingButton() throws IOException {App.practiceTypingView();}
    @FXML private void handleToTalkingButton() throws IOException {App.practiceTalkingView();}
    @FXML private void handleToTuningButton() throws IOException {handleUnbuiltButton(toTuningButton);}                   // No view App.setRoot("PracticeTuningView")
    @FXML private void handleToAlphebetButton() throws IOException {App.cwAlphabetView();}
    @FXML private void handleToMainMenuButton() throws IOException {App.homeScreenView();}

    private void handleUnbuiltButton(Button object){object.setText("Not built yet!");}

}
