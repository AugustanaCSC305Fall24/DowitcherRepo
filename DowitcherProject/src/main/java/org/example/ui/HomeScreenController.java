package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import org.example.App;

import java.io.IOException;

public class HomeScreenController {

    @FXML
    private StackPane stackPane; // Bind to the StackPane in FXML
    @FXML
    private ImageView backgroundImageView; // Bind this to the ImageView in the FXML
    @FXML
    private Button learnCWButton; // Button to load the CW learning popup
    @FXML
    private Button settingsButton; // Button to open settings popup
    @FXML
    private Button toLiveChatViewButton; // Button to navigate to Live Chat
    @FXML
    private Button aiChatButton; // Button to navigate to AI Chat

    private Popup settingsPopup; // Popup for settings
    private Popup practiceModesPopup; // Popup for practice modes

    @FXML
    private HBox bottomHBox;

    /**
     * Handle Learn CW button click: Toggles the practice modes popup.
     */
    @FXML
    void handleLearnCWButton(ActionEvent event) {
        togglePopup(
                practiceModesPopup,
                "PracticeModesPopup.fxml",
                learnCWButton
        );
    }

    /**
     * Handle Settings button click: Toggles the settings popup.
     */
    @FXML
    void handleSettingsButton(ActionEvent event) {
        togglePopup(
                settingsPopup,
                "SettingsPopup.fxml",
                settingsButton
        );
    }

    /**
     * Handle Live Chat button click: Navigate to the Live Chat view.
     */
    @FXML
    void handleToLiveChatButton(ActionEvent event) throws IOException {
        App.liveChatView1();
    }

    /**
     * Handle AI Chat button click: Navigate to the AI Chat view.
     */
    @FXML
    void handleToPracticeTalkingButton(ActionEvent event) throws IOException {
        App.botAddEditRemoveView();
    }

    /**
     * Initialize the Home Screen.
     */
    @FXML
    private void initialize() {
        App.currentUser.addView("HomeScreenView");

        // Load and set the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/HomeScreenBackground.png"));
        backgroundImageView.setImage(backgroundImage);

        // Bind the ImageView's dimensions to the StackPane's dimensions
        backgroundImageView.fitHeightProperty().bind(stackPane.heightProperty());
        backgroundImageView.fitWidthProperty().bind(stackPane.widthProperty());

        HBox.setHgrow(bottomHBox, Priority.ALWAYS);

        // Enable horizontal resizing of buttons
        HBox.setHgrow(toLiveChatViewButton, Priority.ALWAYS);
        HBox.setHgrow(aiChatButton, Priority.ALWAYS);
        HBox.setHgrow(learnCWButton, Priority.ALWAYS);
        HBox.setHgrow(settingsButton, Priority.ALWAYS);

        VBox.setVgrow(toLiveChatViewButton, Priority.ALWAYS);
        VBox.setVgrow(aiChatButton, Priority.ALWAYS);
        VBox.setVgrow(learnCWButton, Priority.ALWAYS);
        VBox.setVgrow(settingsButton, Priority.ALWAYS);
    }

    /**
     * Toggle visibility of a popup. If not created, it initializes the popup using the given FXML.
     *
     * @param popup       The Popup instance to toggle.
     * @param fxmlFile    The FXML file to load for the popup content.
     * @param anchorButton The button to position the popup relative to.
     */
    private void togglePopup(Popup popup, String fxmlFile, Button anchorButton) {
        if (popup == null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlFile));
                Parent content = loader.load();
                popup = new Popup();
                popup.getContent().add(content);
                popup.setAutoHide(true);
                popup.setHideOnEscape(true);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        if (popup.isShowing()) {
            popup.hide();
        } else {
            double xPos = anchorButton.localToScene(anchorButton.getBoundsInLocal()).getMinX()
                    + anchorButton.getScene().getWindow().getX();
            // Position below the button (positive Y offset for below)
            double yPos = anchorButton.localToScene(anchorButton.getBoundsInLocal()).getMaxY()
                    + anchorButton.getScene().getWindow().getY() + 10;

            popup.show(anchorButton.getScene().getWindow(), xPos, yPos);
        }
    }
}
