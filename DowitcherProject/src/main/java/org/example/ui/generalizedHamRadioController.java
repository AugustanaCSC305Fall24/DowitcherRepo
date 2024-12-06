package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.App;

import java.io.IOException;

public class generalizedHamRadioController {

    // Default Buttons
    @FXML private Button backButton;
    @FXML private ImageView backImage;
    @FXML private Button settingsButton;
    @FXML private ImageView settingsImage;

    @FXML private TextArea mainTextArea;

    @FXML private Text modeTitleText;

    private Popup settingsPopup; // Popup instance for settings

    @FXML
    void back(ActionEvent event) throws IOException {
        App.back();
    }

    @FXML
    void handleSettingsButton(ActionEvent event) {
        // Check if the popup already exists
        if (settingsPopup == null) {
            try {
                // Load the Settings FXML
                FXMLLoader loader = new FXMLLoader(App.class.getResource("SettingsPopup.fxml"));
                Parent settingsContent = loader.load();

                // Create the popup and set its content
                settingsPopup = new Popup();
                settingsPopup.getContent().add(settingsContent);

                // Set popup properties
                settingsPopup.setAutoHide(true); // Close when clicked outside
                settingsPopup.setHideOnEscape(true);

                // Show the popup near the button
                settingsPopup.show(settingsButton.getScene().getWindow(), settingsButton.getLayoutX() + settingsButton.getWidth(), settingsButton.getLayoutY() + settingsButton.getHeight());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Toggle the popup visibility
            if (settingsPopup.isShowing()) {
                settingsPopup.hide();
            } else {
                settingsPopup.show(settingsButton.getScene().getWindow(), settingsButton.getLayoutX() + settingsButton.getWidth(), settingsButton.getLayoutY() + settingsButton.getHeight());
            }
        }
    }
}
