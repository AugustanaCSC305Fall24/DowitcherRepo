package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import org.example.App;

public class SettingsController {
    // UI Elements
    @FXML private Slider cwSpeedSlider;
    @FXML private CheckBox staticCheckBox;
    @FXML private CheckBox showCWLettersCheckBox;
    @FXML private CheckBox showCWAcronymsCheckBox;
    @FXML private Button saveButton;
    @FXML private Button editControlsButton;

    // Initialize UI Elements with Current User Data
    @FXML
    public void initialize() {
        // Bind sliders and checkboxes to user settings
        cwSpeedSlider.setValue(App.currentUser.getCwSpeed() / 100.0); // Convert back to UI scale
        staticCheckBox.setSelected(App.currentUser.isStaticEnabled());
        showCWLettersCheckBox.setSelected(App.currentUser.getShowCWLetters());
        showCWAcronymsCheckBox.setSelected(App.currentUser.getShowCWAcronyms());
    }

    // Save Changes and Close Popup
    @FXML
    private void handleSaveButton(ActionEvent event) {
        // Save current settings to the user object
        App.currentUser.setCwSpeed((long) (cwSpeedSlider.getValue() * 100)); // Convert to storage scale
        App.currentUser.setStaticEnabled(staticCheckBox.isSelected());
        App.currentUser.setShowCWLetters(showCWLettersCheckBox.isSelected());
        App.currentUser.setShowCWAcronyms(showCWAcronymsCheckBox.isSelected());

        // Optional: Close the popup
        closePopup();
    }

    // Navigate to Edit Controls View
    @FXML
    private void handleEditControlsButton(ActionEvent event) {
        // Add logic to transition to the controls editing view
        try {
            App.controlMenuView(); // Assuming `App.controlMenuView` handles the transition
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility Method to Close the Popup
    private void closePopup() {
        // Locate the popup and hide it
        saveButton.getScene().getWindow().hide(); // This assumes the popup is the current window
    }
}
