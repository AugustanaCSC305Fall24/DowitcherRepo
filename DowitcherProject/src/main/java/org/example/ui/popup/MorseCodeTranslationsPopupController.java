package org.example.ui.popup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.utility.MorseCodeTranslator;

import java.util.Map;

public class MorseCodeTranslationsPopupController {

    @FXML private VBox translationsContainer;
    @FXML private Button closeButton;

    @FXML public void initialize() {
        closeButton.setOnAction(e -> closePopup());
    }

    @FXML public void closePopup() {
        closeButton.getScene().getWindow().hide();
    }
}
