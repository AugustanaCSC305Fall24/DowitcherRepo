package org.example.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.example.App;

import java.io.IOException;

public abstract class generalizedHamRadioController {
    protected Button settingsButton;
    protected Button menuButton;
    protected HBox topHBox;

    public generalizedHamRadioController() {
        settingsButton = new Button("Settings");
        menuButton = new Button("Menu");
        topHBox = new HBox();

        initializeMenuSettingsButtons();
    }

    protected void initializeMenuSettingsButtons() {
        // Initialize menu button
        menuButton.setOnAction(event -> goToMainMenu());

        // Initialize settings button
        settingsButton.setOnAction(event -> goToSettingsMenu());

        // Add buttons to topHBox
        topHBox.getChildren().clear();
        topHBox.getChildren().addAll(menuButton, settingsButton);
    }

    // Shared logic for navigation buttons
    protected void goToMainMenu() {
        try {
            App.homeScreenView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void goToSettingsMenu() {
        try {
            App.settingsPopupView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
