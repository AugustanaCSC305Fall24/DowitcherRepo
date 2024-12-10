package org.example.ui.popup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.App;
import org.example.utility.MorseCodeTranslator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControlsHotkeysPopupController {

    @FXML private VBox hotkeysContainer;

    // Bottom Row Buttons
    @FXML private Button saveButton;
    @FXML private Button closeButton;
    @FXML private Button toSettingsButton;
    @FXML private Button backButton;

    // Hotkey TextFields (All are uneditable by the user from clicking on it)
    @FXML private TextField settingsKeyField;
    @FXML private TextField dahKeyField;
    @FXML private TextField ditKeyField;
    @FXML private TextField straightKeyField;
    @FXML private TextField frequencyUpTextField;
    @FXML private TextField frequencyDownTextField;
    @FXML private TextField filterUpTextField;
    @FXML private TextField filterDownTextField;

    // Buttons to change what a hotkey is
    @FXML private Button changeDahKeyButton;
    @FXML private Button changeDitKeyButton;
    @FXML private Button changeStraightKeyButton;
    @FXML private Button changeFilterDownKeyChangeButton;
    @FXML private Button changeFilterUpKeyChangeButton;
    @FXML private Button changeFrequencyDownKeyButton;
    @FXML private Button changeFrequencyUpKeyButton;
    @FXML private Button changeSettingsKeyButton;
    
    private final Map<String, TextField> keyBindings = new HashMap<>();
    public static String selectedAction;
    public static TextField targetTextField;

    @FXML public void initialize() {updateActionTextField();}
    
    public void updateActionTextField(){
        Map<String, KeyCode> actionMap = App.currentUser.getActionFirstActionMap();
        for (Map.Entry<String, KeyCode> entry : actionMap.entrySet()) {
            String action = entry.getKey();
            KeyCode key = entry.getValue();
            String keyString = key.getName();
            if (keyString.equalsIgnoreCase("Esc")) {
                keyString = "Escape";
            }
            switch (action) {
                case "settingsKey":
                    settingsKeyField.setText(keyString);
                    System.out.println(key);
                    break;
                case "dahKey":
                    dahKeyField.setText(keyString);
                    System.out.println(key);
                    break;
                case "ditKey":
                    ditKeyField.setText(keyString);
                    System.out.println(key);
                    break;
                case "straightKey":
                    straightKeyField.setText(keyString);
                    System.out.println(key);
                    break;
                case "frequencyUpKey":
                    frequencyUpTextField.setText(keyString);
                    System.out.println(key);
                    break;
                case "frequencyDownKey":
                    frequencyDownTextField.setText(keyString);
                    System.out.println(key);
                    break;
                case "filterUpKey":
                    filterUpTextField.setText(keyString);
                    System.out.println(key);
                    break;
                case "filterDownKey":
                    filterDownTextField.setText(keyString);
                    System.out.println(key);
                    break;
                default:
                    // Handle any unexpected actions if needed
                    break;
            }
        }
        System.out.println("setActionTextField Method Ended.");
        System.out.println("actionMap Replica: " + actionMap.toString());
        System.out.println("actionMap App Class: " + App.currentUser.getActionFirstActionMap().toString());
    }

    // Handler methods for key changes
    @FXML void handleChangeDahKeyButton(ActionEvent event) { handleChangeKeyButton(dahKeyField, "dahKey"); }
    @FXML void handleChangeDitKeyButton(ActionEvent event) { handleChangeKeyButton(ditKeyField, "ditKey"); }
    @FXML void handleChangeStraightKeyButton(ActionEvent event) { handleChangeKeyButton(straightKeyField, "straightKey"); }
    @FXML void handleChangeFilterDownKeyChangeButton(ActionEvent event) { handleChangeKeyButton(filterDownTextField, "filterDownKey"); }
    @FXML void handleChangeFilterUpKeyChangeButton(ActionEvent event) { handleChangeKeyButton(filterUpTextField, "filterUpKet"); }
    @FXML void handleChangeFrequencyDownKeyButton(ActionEvent event) { handleChangeKeyButton(frequencyDownTextField, "frequencyDownKey"); }
    @FXML void handleChangeFrequencyUpKeyButton(ActionEvent event) { handleChangeKeyButton(frequencyUpTextField, "frequencyUpKey"); }
    @FXML void handleChangeSettingsKeyButton(ActionEvent event) { handleChangeKeyButton(settingsKeyField, "settingsKey"); }

    private void handleChangeKeyButton(TextField target, String action) {
        selectedAction = action;
        targetTextField = target;
        KeyChangePopupController.controller = (this);
        App.togglePopup("KeyChangePopup.fxml", changeFrequencyDownKeyButton, 400, 400);
    }

    @FXML public void handleCloseButton() {
        Stage stage = (Stage) frequencyDownTextField.getScene().getWindow();
        stage.close();
    }

}
