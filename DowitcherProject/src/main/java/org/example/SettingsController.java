package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

import java.io.IOException;

public class SettingsController {
    //UI Elements In Order
    @FXML private Button switchToControlMenuButton;
    @FXML private Slider cwSpeedSlider;
    @FXML private Slider volumeSlider;
    @FXML private Slider staticSlider;
    @FXML private CheckBox showCWLettersCheckBox;
    @FXML private CheckBox showCWAcronymsCheckBox;
    @FXML private Button saveButton;
    @FXML private Button toControlMenuButton;
    @FXML private Button toMainMenuButton;
    @FXML private Button toBackButton;


    //All view switching button presses
    @FXML void handleToControlMenuButton() throws IOException {App.currentUser.addView("SettingsView");App.controlMenuView();}
    @FXML private void handleToMainMenuButton() throws IOException {App.homeScreenView();}
    @FXML private void handleToBackButton() throws IOException {App.back();}

    //Handlers
    @FXML private void handleSaveButton(){
        App.currentUser.setCwSpeed((long) (cwSpeedSlider.getValue() * 100));
        App.currentUser.setVolume(volumeSlider.getValue());
        App.currentUser.setStaticAmount(staticSlider.getValue());
        App.currentUser.setShowCWLetters(showCWLettersCheckBox.isSelected());
        App.currentUser.setShowCWAcronyms(showCWAcronymsCheckBox.isSelected());
    }

    @FXML public void initialize() {
        cwSpeedSlider.setValue(App.currentUser.getCwSpeed());
        volumeSlider.setValue(App.currentUser.getVolume());
        staticSlider.setValue(App.currentUser.getStaticAmount());
        showCWLettersCheckBox.setSelected(App.currentUser.getShowCWLetters());
        showCWAcronymsCheckBox.setSelected(App.currentUser.getShowCWAcronyms());
    }
}
