package org.example.ui.mainviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import org.example.App;

import java.io.IOException;

public class HomeScreenController {

    @FXML private StackPane stackPane; // Bind to the StackPane in FXML

    @FXML private ImageView backgroundImageView; // Bind this to the ImageView in the FXML
    @FXML private Button learnCWButton; // Button to load the CW learning popup
    @FXML private Button settingsButton; // Button to open settings popup
    @FXML private Button liveChatButton; // Button to navigate to Live Chat
    @FXML private Button aiChatButton; // Button to navigate to AI Chat

    private Popup settingsPopup; // Popup for settings
    private Popup practiceModesPopup; // Popup for practice modes

    @FXML private HBox bottomHBox;
    @FXML private HBox topHBox;

    /**
     * Handle Learn CW button click: Toggles the practice modes popup.
     */
    @FXML
    void handleLearnCW(ActionEvent event) {
        App.togglePopup("LearnModesPopup.fxml", learnCWButton, 400, 300);
    }

    /**
     * Handle Settings button click: Toggles the settings popup.
     */
    @FXML
    void handleSettings(ActionEvent event) {
        App.togglePopup("SettingsPopup.fxml", settingsButton, 500, 300);
    }

    /**
     * Handle Live Chat button click: Navigate to the Live Chat view.
     */
    @FXML
    void handleLiveChat(ActionEvent event) throws IOException {
        App.generalizedHamRadioView("LiveChatChatRoomController");
    }

    /**
     * Handle AI Chat button click: Navigate to the AI Chat view.
     */
    @FXML
    void handleAiChat(ActionEvent event) throws IOException {
        App.botView();
    }

    /**
     * Initialize the Home Screen.
     */
    @FXML
    private void initialize() {
        App.currentUser.addView("HomeScreenView");

        // Allow the buttons to expand horizontally in the bottom HBox
        HBox.setHgrow(bottomHBox, Priority.ALWAYS);

        // Enable horizontal resizing of buttons to fill available space
        HBox.setHgrow(liveChatButton, Priority.ALWAYS);
        HBox.setHgrow(aiChatButton, Priority.ALWAYS);
        HBox.setHgrow(learnCWButton, Priority.ALWAYS);
        HBox.setHgrow(settingsButton, Priority.ALWAYS);
    }

}
