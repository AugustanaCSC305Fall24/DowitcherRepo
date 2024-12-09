package org.example.ui.popup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.utility.MorseCodeTranslator;

import java.util.Map;

public class CWAcronymsPopupController {

    @FXML
    private VBox acronymsContainer;

    @FXML
    private Button closeButton;

    @FXML
    public void initialize() {
        // Get the CW acronyms map
        Map<String, String> cwMessagesMap = MorseCodeTranslator.getCwMessagesMap();

        // Dynamically populate the container with acronyms and their meanings
        for (Map.Entry<String, String> entry : cwMessagesMap.entrySet()) {
            Text acronymText = new Text(entry.getKey() + " - " + entry.getValue());
            acronymText.setStyle("-fx-fill: white; -fx-font-size: 14px;");
            acronymsContainer.getChildren().add(acronymText);
        }

        // Set button styling and actions
        closeButton.setOnAction(event -> closePopup());
    }

    private void closePopup() {
        // Logic to close the popup
        acronymsContainer.getScene().getWindow().hide();
    }
}
