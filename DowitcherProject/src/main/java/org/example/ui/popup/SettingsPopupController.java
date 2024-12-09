package org.example.ui.popup;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import org.example.App;

import java.io.IOException;

public class SettingsPopupController {
    // UI Elements
    @FXML private Slider cwSpeedSlider;
    @FXML private Button showCWLettersButton;
    @FXML private Button showCWAcronymsButton;
    @FXML private Button saveButton;
    @FXML private Button editControlsButton;

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

    @FXML public void handleShowCWAcroymnsButton(ActionEvent actionEvent) {
        App.togglePopupWithScroll("AcronymsPopup.fxml", showCWAcronymsButton, 200, 400);
    }

    @FXML public void handleShowCWLettersButton(ActionEvent actionEvent) {
        App.togglePopupWithScroll("MorseTranslationsPopup.fxml", showCWLettersButton, 200, 400);
    }

    @FXML
    private void editControlsButton(ActionEvent event) {
        App.togglePopup("ControlsPopup.fxml", editControlsButton);
    }
}
