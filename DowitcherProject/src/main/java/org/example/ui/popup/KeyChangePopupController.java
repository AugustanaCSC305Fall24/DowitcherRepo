package org.example.ui.popup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class KeyChangePopupController {

    @FXML
    private Label keyDisplayLabel;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private TextField targetTextField;
    private String lastKeyPressed;
    private TextField[] allTextFields;

    // Method to set the TextField in the main controller that should be updated
    public void setTargetTextField(TextField targetTextField) {
        this.targetTextField = targetTextField;
    }

    // Called after FXML loading, sets up key event listening and focus
    @FXML
    private void initialize() {
        confirmButton.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        newWindow.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
                    }
                });
            }
        });
    }

    // Method to handle key press events and update the display label
    private void handleKeyPress(KeyEvent event) {
        lastKeyPressed = event.getCode().toString();
        keyDisplayLabel.setText("Selected Key: " + lastKeyPressed);

        checkForDuplicates(lastKeyPressed);

        event.consume();
    }

    private void checkForDuplicates(String selectedKey) {
        boolean hasDuplicate = false;

        // Reset all fields to default style
        for (TextField field : allTextFields) {
            field.setStyle("");
        }

        // Check for duplicates and mark conflicting fields
        for (TextField field : allTextFields) {
            if (field != targetTextField && field.getText().equals(selectedKey)) {
                field.setStyle("-fx-background-color: red;");
                targetTextField.setStyle("-fx-background-color: red;");
                hasDuplicate = true;
            }
        }

        confirmButton.setDisable(hasDuplicate); // Disable confirm if duplicate exists
    }


    // Method to confirm and set the selected key in the target TextField
    @FXML
    private void handleConfirmButton() {
        if (lastKeyPressed != null && targetTextField != null) {
            targetTextField.setText(lastKeyPressed);
        }
        closePopup();
    }

    // Method to cancel the popup without making changes
    @FXML
    private void handleCancelButton() {
        closePopup();
    }

    // Method to close the popup window
    private void closePopup() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    public void setAllTextFields(TextField... fields) {
        this.allTextFields = fields;
    }
}
