package org.example.ui.popup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;
import org.example.data.User;

import java.util.Map;

public class ControlsHotkeysPopupController {

    @FXML
    private VBox hotkeysContainer;

    private User user;

    public ControlsHotkeysPopupController(User user) {
        this.user = user; // Pass the user instance
    }

    @FXML
    public void initialize() {
        // Get the hotkeys map
        Map<KeyCode, String> keyFirstActionMap = User.getKeyFirstActionMap();

        // Dynamically populate the container with hotkeys and actions
        for (Map.Entry<KeyCode, String> entry : keyFirstActionMap.entrySet()) {
            Text hotkeyText = new Text(entry.getKey() + " - " + entry.getValue());
            hotkeyText.setStyle("-fx-fill: white; -fx-font-size: 14px;");
            hotkeysContainer.getChildren().add(hotkeyText);
        }
    }

    @FXML
    private void closePopup() {
        // Logic to close the popup
        hotkeysContainer.getScene().getWindow().hide();
    }
}
