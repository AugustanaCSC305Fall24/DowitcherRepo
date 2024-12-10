package org.example.ui.popup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.App;

import java.io.IOException;

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

    public static ControlsHotkeysPopupController controller;

    // Method to set the TextField in the main controller that should be updated
    public void setTargetTextField(TextField targetTextField) {
        this.targetTextField = targetTextField;
    }

    // Called after FXML loading, sets up key event listening and focus
    @FXML
    private void initialize() {
        // Register the key press event listener for the scene
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

            // Set the text color to black for the last pressed key
            keyDisplayLabel.setStyle("-fx-text-fill: black;");

            checkForDuplicates(lastKeyPressed);

            event.consume();
    }

    private void checkForDuplicates(String selectedKey) {
        System.out.println("Check is duplicates method is called.");

        boolean hasDuplicate = false;

        // Convert the selectedKey (String) to KeyCode
        KeyCode selectedKeyCode = KeyCode.valueOf(selectedKey.toUpperCase());

        // Check for duplicates in the keyFirstActionMap (keyFirstActionMap maps KeyCode to action string)
        for (KeyCode mappedKey : App.currentUser.getKeyFirstActionMap().keySet()) {
            if (mappedKey == selectedKeyCode) {  // Compare KeyCode objects
                hasDuplicate = true;
                break;
            }
        }

        // Disable or enable the confirm button based on the duplicate check
        confirmButton.setDisable(hasDuplicate);
    }



    // Method to confirm and set the selected key in the target TextField
    @FXML
    private void handleConfirmButton() throws IOException {
        String actionToBeReplaced = ControlsHotkeysPopupController.selectedAction;

        // Retrieve the current key codes for each action
        KeyCode settingsKeyCode = App.currentUser.getKeyForAction("settingsKey");
        KeyCode dahKeyCode = App.currentUser.getKeyForAction("dahKey");
        KeyCode ditKeyCode = App.currentUser.getKeyForAction("ditKey");
        KeyCode straightKeyCode = App.currentUser.getKeyForAction("straightKey");
        KeyCode frequencyUpKeyCode = App.currentUser.getKeyForAction("frequencyUpKey");
        KeyCode frequencyDownKeyCode = App.currentUser.getKeyForAction("frequencyDownKey");
        KeyCode filterUpKeyCode = App.currentUser.getKeyForAction("filterUpKey");
        KeyCode filterDownKeyCode = App.currentUser.getKeyForAction("filterDownKey");

        KeyCode newKeyCode = KeyCode.valueOf(lastKeyPressed.toUpperCase());

        // Based on the selected action, set the corresponding key code
        switch (actionToBeReplaced) {
            case "settingsKey":
                App.currentUser.setActionMap(newKeyCode, dahKeyCode, ditKeyCode, straightKeyCode, frequencyUpKeyCode, frequencyDownKeyCode, filterUpKeyCode, filterDownKeyCode);
                break;
            case "dahKey":
                App.currentUser.setActionMap(settingsKeyCode, newKeyCode, ditKeyCode, straightKeyCode, frequencyUpKeyCode, frequencyDownKeyCode, filterUpKeyCode, filterDownKeyCode);
                break;
            case "ditKey":
                App.currentUser.setActionMap(settingsKeyCode, dahKeyCode, newKeyCode, straightKeyCode, frequencyUpKeyCode, frequencyDownKeyCode, filterUpKeyCode, filterDownKeyCode);
                break;
            case "straightKey":
                App.currentUser.setActionMap(settingsKeyCode, dahKeyCode, ditKeyCode, newKeyCode, frequencyUpKeyCode, frequencyDownKeyCode, filterUpKeyCode, filterDownKeyCode);
                break;
            case "frequencyUpKey":
                App.currentUser.setActionMap(settingsKeyCode, dahKeyCode, ditKeyCode, straightKeyCode, newKeyCode, frequencyDownKeyCode, filterUpKeyCode, filterDownKeyCode);
                break;
            case "frequencyDownKey":
                App.currentUser.setActionMap(settingsKeyCode, dahKeyCode, ditKeyCode, straightKeyCode, frequencyUpKeyCode, newKeyCode, filterUpKeyCode, filterDownKeyCode);
                break;
            case "filterUpKey":
                App.currentUser.setActionMap(settingsKeyCode, dahKeyCode, ditKeyCode, straightKeyCode, frequencyUpKeyCode, frequencyDownKeyCode, newKeyCode, filterDownKeyCode);
                break;
            case "filterDownKey":
                App.currentUser.setActionMap(settingsKeyCode, dahKeyCode, ditKeyCode, straightKeyCode, frequencyUpKeyCode, frequencyDownKeyCode, filterUpKeyCode, newKeyCode);
                break;
            default:
                // If the action is not recognized, you can add a fallback or error handling
                System.out.println("Unknown action: " + actionToBeReplaced);
                break;
        }

        System.out.println("Updated Action Map...");
        controller.updateActionTextField();

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
}
