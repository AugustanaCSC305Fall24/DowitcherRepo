package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import org.example.App;

import javax.swing.*;
import java.io.IOException;
public class HomeScreenController {

    @FXML public HBox bottomHBox;
    @FXML public Button aiChatButton;
    @FXML private Button learnCWButton;
    @FXML private Button settingsButton;
    @FXML private Button toLiveChatViewButton;
    @FXML private ImageView backgroundImageView; // Bind this to the ImageView in the FXML

    private Popup settingsPopup; // Popup for settings
    private Popup practiceModesPopup; // Popup for practice modes

    @FXML private Scene scene; // Reference to the current scene

    // Learn CW Button Handler
    @FXML
    void handleLearnCWButton(ActionEvent event) {
        if (practiceModesPopup == null) {
            try {
                // Load PracticeModesPopup FXML and controller
                FXMLLoader loader = new FXMLLoader(App.class.getResource("PracticeModesPopup.fxml"));
                Parent popupContent = loader.load();

                // Create the popup and add content
                practiceModesPopup = new Popup();
                practiceModesPopup.getContent().add(popupContent);

                // Set popup properties
                practiceModesPopup.setAutoHide(true); // Hide when clicking outside
                practiceModesPopup.setHideOnEscape(true);

                // Show the popup near the Learn CW button
                practiceModesPopup.show(
                        learnCWButton.getScene().getWindow(),
                        learnCWButton.localToScene(learnCWButton.getBoundsInLocal()).getMaxX() + learnCWButton.getScene().getWindow().getX(),
                        learnCWButton.localToScene(learnCWButton.getBoundsInLocal()).getMinY() + learnCWButton.getScene().getWindow().getY()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Toggle visibility of the popup
            if (practiceModesPopup.isShowing()) {
                practiceModesPopup.hide();
            } else {
                practiceModesPopup.show(
                        learnCWButton.getScene().getWindow(),
                        learnCWButton.localToScene(learnCWButton.getBoundsInLocal()).getMaxX() + learnCWButton.getScene().getWindow().getX(),
                        learnCWButton.localToScene(learnCWButton.getBoundsInLocal()).getMinY() + learnCWButton.getScene().getWindow().getY()
                );
            }
        }
    }

    // Settings Button Handler
    @FXML
    void handleSettingsButton(ActionEvent event) {
        if (settingsPopup == null) {
            try {
                // Load SettingsView FXML
                FXMLLoader loader = new FXMLLoader(App.class.getResource("SettingsView.fxml"));
                Parent settingsContent = loader.load();

                // Create a popup and add content
                settingsPopup = new Popup();
                settingsPopup.getContent().add(settingsContent);

                // Set popup properties
                settingsPopup.setAutoHide(true); // Hide when clicking outside
                settingsPopup.setHideOnEscape(true);

                // Show the popup near the settings button
                settingsPopup.show(
                        settingsButton.getScene().getWindow(),
                        settingsButton.localToScene(settingsButton.getBoundsInLocal()).getMaxX() + settingsButton.getScene().getWindow().getX(),
                        settingsButton.localToScene(settingsButton.getBoundsInLocal()).getMinY() + settingsButton.getScene().getWindow().getY()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Toggle visibility of the popup
            if (settingsPopup.isShowing()) {
                settingsPopup.hide();
            } else {
                settingsPopup.show(
                        settingsButton.getScene().getWindow(),
                        settingsButton.localToScene(settingsButton.getBoundsInLocal()).getMaxX() + settingsButton.getScene().getWindow().getX(),
                        settingsButton.localToScene(settingsButton.getBoundsInLocal()).getMinY() + settingsButton.getScene().getWindow().getY()
                );
            }
        }
    }

    // Live Chat Button Handler
    @FXML void handleToLiveChatButton(ActionEvent event) throws IOException {App.liveChatView1();}
    @FXML void handleToPracticeTalkingButton(ActionEvent event) throws IOException {App.botAddEditRemoveView();}

   // Initialize View
    @FXML
    private void initialize() {
        App.currentUser.addView("HomeScreenView");

        Image backgroundImage = new Image(getClass().getResourceAsStream("/HomeScreenBackground.png"));
        backgroundImageView.setImage(backgroundImage);

        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, null, null);
        Background background = new Background(backgroundFill);
        bottomHBox.setBackground(background);
    }
}
