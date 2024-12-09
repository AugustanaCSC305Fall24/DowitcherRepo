package org.example.ui.popup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.utility.MorseCodeTranslator;

import java.util.Map;

public class CWAcronymsPopupController {

    @FXML private VBox acronymsContainer;

    @FXML private Button closeButton;

    @FXML
    public void initialize() {
        closeButton.setOnAction(event -> closePopup());
    }

    private void closePopup() {
        acronymsContainer.getScene().getWindow().hide();
    }
}
