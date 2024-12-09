package org.example.ui.popup;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import org.example.App;

public class SettingsPopupController {
    // UI Elements
    @FXML private Slider cwSpeedSlider;
    @FXML private Button showCWLettersButton;
    @FXML private Button showCWAcronymsButton;
    @FXML private Button saveButton;
    @FXML private Button editControlsButton;
    @FXML private Button showControlHotkeysButton;

    @FXML
    public void initialize() {
        cwSpeedSlider.setValue(App.currentUser.getCwSpeed() / 100.0); // Convert back to UI scale
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        App.currentUser.setCwSpeed((long) (cwSpeedSlider.getValue() * 100)); // Convert to storage scale\
        closePopup();
    }

    @FXML
    private void handleEditControlsButton(ActionEvent event) {
        try {
            App.controlMenuView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closePopup() {
        saveButton.getScene().getWindow().hide();
    }

    public void handleExitButton(ActionEvent actionEvent) {
        App.exitProgram();
    }

    public void handleShowCWAcroymnsButton(ActionEvent actionEvent) {
        App.togglePopupWithScroll("CWAcronymsPopup.fxml", showCWAcronymsButton, 200, 400);
    }

    public void handleShowCWLettersButton(ActionEvent actionEvent) {
        App.togglePopupWithScroll("MorseCodeTranslationsPopup.fxml", showCWLettersButton, 200, 400);
    }

    public void handleShowControlHotkeysButton(ActionEvent actionEvent) {
        App.togglePopupWithScroll("ControlsHotkeysPopup.fxml", showControlHotkeysButton, 200, 400);
    }
}
