package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;

public class PracticeTalkingController {

    @FXML private Button backButton;
    @FXML private ScrollPane chatLogScrollPane;
    @FXML private VBox chatLogVBox;
    @FXML private Button dahButton;
    @FXML private Button ditButton;
    @FXML private Slider filterSlider;
    @FXML private Slider frequencySlider;
    @FXML private Button mainMenuButton;
    @FXML private Label roomTitleLabel;
    @FXML private Button straightKeyButton;
    @FXML private TextField typingTextField;

    private void initialize() {
        App.currentUser.addView("PracticeTalkingView");
    }

    @FXML void handleBackButton(ActionEvent event) throws IOException {App.setRoot(App.currentUser.popLastView());}

    @FXML void handleDahButton(ActionEvent event) {

    }

    @FXML void handleDitButton(ActionEvent event) {

    }

    @FXML void handleMainMenuButton(ActionEvent event) throws IOException {App.setRoot("HomeScreenView");}

    @FXML void handleStraightKeyButton(ActionEvent event) {

    }

}
