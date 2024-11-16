package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.example.App;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControlMenuController {

    //Bottom Row Buttons
    @FXML private Button saveButton;
    @FXML private Button switchToHomeScreenButton;
    @FXML private Button toSettingsButton;
    @FXML private Button backButton;

    //All view switching button presses
    @FXML private void handleToSettingsButton() throws IOException {App.settingsView();}
    @FXML private void switchToHomeScreenView() throws IOException {App.homeScreenView();}
    @FXML private void handleBackButton() throws IOException {App.back();}

    //Hotkey TextFields (All are uneditable by the user from clicking on it)
    @FXML private TextField exitKeyField;
    @FXML private TextField settingsKeyField;
    @FXML private TextField dahKeyField;
    @FXML private TextField ditKeyField;
    @FXML private TextField straightKeyField;
    @FXML private TextField frequencyUpTextField;
    @FXML private TextField frequencyDownTextField;
    @FXML private TextField filterUpTextField;
    @FXML private TextField filterDownTextField;

    //Buttons to change what a hot key is
    @FXML private Button changeDahKeyButton;
    @FXML private Button changeDitKeyButton;
    @FXML private Button changeStraightKeyButton;
    @FXML private Button changeExitKeyButton;
    @FXML private Button changeFilterDownKeyChangeButton;
    @FXML private Button changeFilterUpKeyChangeButton;
    @FXML private Button changeFrequencyDownKeyButton;
    @FXML private Button changeFrequencyUpKeyButton;
    @FXML private Button changeSettingsKeyButton;

    private final Map<String, TextField> keyBindings = new HashMap<>(); //Chat GPT Generated

    @FXML public void initialize() {
        setActionTextField();
    }

    private void setActionTextField(){
        Map<String, KeyCode> actionMap = App.currentUser.getActionFirstActionMap();
        for (Map.Entry<String, KeyCode> entry : actionMap.entrySet()) {
            String action = entry.getKey();
            KeyCode key = entry.getValue();
            String keyString = key.getName();
            if (keyString.equalsIgnoreCase("Esc")) {
                keyString = "Escape";
            }
            switch (action) {
                case "exitProgram":
                    exitKeyField.setText(keyString);
                    System.out.println(key);
                    break;
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

    @FXML
    private void handleSaveButton() {
        checkForDuplicateKeys(); // Check for duplicate keys before proceeding with saving
        if (!checkForDuplicateKeys()) {
            // Retrieve the key bindings from each text field
            KeyCode exitProgramKeyCode = KeyCode.valueOf(exitKeyField.getText().toUpperCase());
            KeyCode settingsKeyCode = KeyCode.valueOf(settingsKeyField.getText().toUpperCase());
            KeyCode dahKeyCode = KeyCode.valueOf(dahKeyField.getText().toUpperCase());
            KeyCode ditKeyCode = KeyCode.valueOf(ditKeyField.getText().toUpperCase());
            KeyCode straightKeyCode = KeyCode.valueOf(straightKeyField.getText().toUpperCase());
            KeyCode frequencyUpKeyCode = KeyCode.valueOf(frequencyUpTextField.getText().toUpperCase());
            KeyCode frequencyDownKeyCode = KeyCode.valueOf(frequencyDownTextField.getText().toUpperCase());
            KeyCode filterUpKeyCode = KeyCode.valueOf(filterUpTextField.getText().toUpperCase());
            KeyCode filterDownKeyCode = KeyCode.valueOf(filterDownTextField.getText().toUpperCase());
            //exitProgram settingsKey dahKey ditKey frequencyUpKey frequencyDownKey filterUpKey filterDownKey
            // Update the user's action map with the new bindings

            App.currentUser.setActionMap(
                    exitProgramKeyCode,
                    settingsKeyCode,
                    dahKeyCode,
                    ditKeyCode,
                    straightKeyCode,
                    frequencyUpKeyCode,
                    frequencyDownKeyCode,
                    filterUpKeyCode,
                    filterDownKeyCode
            );

            // Optionally, provide feedback to the user that the save was successful
            System.out.println("Key bindings saved successfully.");
            setActionTextField();

        } else {
            // Handle the scenario where there are duplicate keys (e.g., show an error message)
            System.out.println("Duplicate keys detected. Please resolve before saving.");
        }
    }


    private boolean checkForDuplicateKeys() {
        Map<String, Integer> keyCount = new HashMap<>();
        boolean hasDuplicates = false;

        for (TextField field : keyBindings.values()) {
                String key = field.getText();
                keyCount.put(key, keyCount.getOrDefault(key, 0) + 1);
            }

        for (TextField field : keyBindings.values()) {
            if (keyCount.get(field.getText()) > 1) {
                field.setStyle("-fx-background-color: red;");
                hasDuplicates = true;
            } else {
                field.setStyle("");
                }
            }
        switchToHomeScreenButton.setDisable(hasDuplicates);
        saveButton.setDisable(hasDuplicates);
        backButton.setDisable(hasDuplicates);
        return hasDuplicates;
    }

    // All Handler Methods for the change key buttons
    @FXML void handleChangeDahKeyButton(ActionEvent event) {handleChangeKeyButton(dahKeyField);}
    @FXML void handleChangeDitKeyButton(ActionEvent event) {handleChangeKeyButton(ditKeyField);}
    @FXML void handleChangeStraightKeyButton(ActionEvent event) {handleChangeKeyButton(straightKeyField);}
    @FXML void handleChangeExitKeyButton(ActionEvent event) {handleChangeKeyButton(exitKeyField);}
    @FXML void handleChangeFilterDownKeyChangeButton(ActionEvent event) {handleChangeKeyButton(filterDownTextField);}
    @FXML void handleChangeFilterUpKeyChangeButton(ActionEvent event) {handleChangeKeyButton(filterUpTextField);}
    @FXML void handleChangeFrequencyDownKeyButton(ActionEvent event) {handleChangeKeyButton(frequencyDownTextField);}
    @FXML void handleChangeFrequencyUpKeyButton(ActionEvent event) {handleChangeKeyButton(frequencyUpTextField);}
    @FXML void handleChangeSettingsKeyButton(ActionEvent event) {handleChangeKeyButton(settingsKeyField);}

    private void handleChangeKeyButton(TextField targetTextField) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/KeyChangePopup.fxml"));
            Parent root = loader.load();

            // Initialize the new Stage (popup window)
            Stage popupStage = new Stage();
            popupStage.setTitle("Change Key Binding");
            popupStage.setScene(new Scene(root));
            popupStage.initOwner(App.getScene().getWindow()); // Set the parent window

            // Show the popup
            popupStage.show();

            // Pass the target TextField and all other TextFields to the popup controller
            KeyChangePopupController popupController = loader.getController();
            popupController.setTargetTextField(targetTextField);
            popupController.setAllTextFields(exitKeyField, settingsKeyField, dahKeyField, ditKeyField,
                    frequencyUpTextField, frequencyDownTextField,
                    filterUpTextField, filterDownTextField);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
