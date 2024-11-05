package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.App;

import java.io.IOException;

public class PracticeMenuController {
    //Buttons
    @FXML private Button toListenViewButton;
    @FXML private Button toTypeViewButton;
    @FXML private Button toTalkViewButton;
    @FXML private Button toTuningButton;
    @FXML private Button toAlphebetButton;
    @FXML private Button toMainMenuButton;
    @FXML private Button AITestButton;

    //All view switching button presses
    @FXML private void handleToListeningButton() throws IOException {App.practiceListeningView();}
    @FXML private void handleToTypingButton() throws IOException {App.practiceTypingView();}
    @FXML private void handleToTalkingButton() throws IOException {App.practiceTalkingView();}
    @FXML private void handleToTuningButton() throws IOException {App.practiceTuningView();}                   // No view App.setRoot("PracticeTuningView")
    @FXML private void handleToAlphebetButton() throws IOException {App.cwAlphabetView();}
    @FXML private void handleToMainMenuButton() throws IOException {App.homeScreenView();}
    @FXML private void handleAITestButton() throws IOException {App.testAiView();}

    private void handleUnbuiltButton(Button object){object.setText("Not built yet!");}

}
