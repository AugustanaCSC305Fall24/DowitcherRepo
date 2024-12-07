package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import org.example.App;

import java.io.IOException;

public abstract class generalizedHamRadioController {

    // Default Buttons
    @FXML private Button backButton;
    @FXML private ImageView backImage;
    @FXML private Button settingsButton;
    @FXML private ImageView settingsImage;

    // Dynamic UI elements
    @FXML private TextArea mainTextArea;
    @FXML private Text modeTitleText;

    private Popup settingsPopup; // Popup instance for settings

    /**
     * Initializes the view with the given mode title.
     *
     * @param modeTitle The title of the mode (e.g., "Alphabet Game").
     */
    public void initializeMode(String modeTitle) {
        // Update the mode title text
        modeTitleText.setText("Mode: " + modeTitle);

        // Add logic to update other UI elements based on the mode
        switch (modeTitle) {
            case "Alphabet Game":
                mainTextArea.setText("Welcome to the Alphabet Game! Practice your CW alphabet here.");
                break;
            case "Listening Game":
                mainTextArea.setText("Listening Game activated. Sharpen your CW decoding skills!");
                break;
            case "Tuning Game":
                mainTextArea.setText("Tune into the correct frequencies in the Tuning Game.");
                break;
            case "Typing Game":
                mainTextArea.setText("Enhance your CW typing accuracy with the Typing Game.");
                break;
            default:
                mainTextArea.setText("Welcome to the Generalized Ham Radio View!");
        }
    }

    // Navigation: Back button
    @FXML
    void back(ActionEvent event) throws IOException {
        App.back();
    }

    // Settings button popup handler
    @FXML
    void handleSettingsButton(ActionEvent event) {
        if (settingsPopup == null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("SettingsPopup.fxml"));
                Parent settingsContent = loader.load();

                settingsPopup = new Popup();
                settingsPopup.getContent().add(settingsContent);

                settingsPopup.setAutoHide(true);
                settingsPopup.setHideOnEscape(true);

                settingsPopup.show(settingsButton.getScene().getWindow(),
                        settingsButton.getLayoutX() + settingsButton.getWidth(),
                        settingsButton.getLayoutY() + settingsButton.getHeight());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (settingsPopup.isShowing()) {
                settingsPopup.hide();
            } else {
                settingsPopup.show(settingsButton.getScene().getWindow(),
                        settingsButton.getLayoutX() + settingsButton.getWidth(),
                        settingsButton.getLayoutY() + settingsButton.getHeight());
            }
        }
    }
}
