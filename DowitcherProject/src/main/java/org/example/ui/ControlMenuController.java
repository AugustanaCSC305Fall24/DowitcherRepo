package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.example.App;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControlMenuController {

    //Buttons
    @FXML private Button saveButton;
    @FXML private Button switchToHomeScreenButton;
    @FXML private Button toSettingsButton;
    @FXML private Button backButton;

    //All view switching button presses
    @FXML private void handleToSettingsButton() throws IOException {
        App.settingsView();}
    @FXML private void switchToHomeScreenView() throws IOException {App.homeScreenView();}
    @FXML private void handleBackButton() throws IOException {App.back();}

    //Keys
    @FXML private TextField exitKeyField;
    @FXML private TextField settingsKeyField;
    @FXML private TextField dahKeyField;
    @FXML private TextField ditKeyField;
    @FXML private TextField frequencyUpTextField;
    @FXML private TextField frequencyDownTextField;
    @FXML private TextField filterUpTextField;
    @FXML private TextField filterDownTextField;

    private final Map<String, TextField> keyBindings = new HashMap<>(); //Chat GPT Generated
    private TextField activeTextField = null; //Chat gpt made

    @FXML public void initialize() {
        addFocusListeners();
        checkForDuplicateKeys();
        setActionTextField();
    }

    private void setActionTextField(){
        Map<String, String> actionMap = App.currentUser.getActionFirstActionMap();
        for (Map.Entry<String, String> entry : actionMap.entrySet()) {
            String action = entry.getKey();
            String key = entry.getValue();
            switch (action) {
                case "exitProgram":
                    exitKeyField.setText(key);
                    System.out.println(key);
                    break;
                case "settingsKey":
                    settingsKeyField.setText(key);
                    System.out.println(key);
                    break;
                case "dahKey":
                    dahKeyField.setText(key);
                    System.out.println(key);
                    break;
                case "ditKey":
                    ditKeyField.setText(key);
                    System.out.println(key);
                    break;
                case "frequencyUpKey":
                    frequencyUpTextField.setText(key);
                    System.out.println(key);
                    break;
                case "frequencyDownKey":
                    frequencyDownTextField.setText(key);
                    System.out.println(key);
                    break;
                case "filterUpKey":
                    filterUpTextField.setText(key);
                    System.out.println(key);
                    break;
                case "filterDownKey":
                    filterDownTextField.setText(key);
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

    private void addFocusListeners() { //Char gpt generated
        for (TextField field : keyBindings.values()) {
            field.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    activeTextField = field;
                } else if (activeTextField == field) {
                    activeTextField = null;
                }
            });
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent event) { //Chat gpt geneterated
        if (activeTextField != null) {
            event.consume(); // Prevents the default key input behavior
            String newKey = event.getCode().toString().toUpperCase();
            activeTextField.setText(newKey); // Set the TextField to the new key
        }
        checkForDuplicateKeys();
    }

    @FXML
    private void handleSaveButton() {
        checkForDuplicateKeys(); // Check for duplicate keys before proceeding with saving
        if (!checkForDuplicateKeys()) {
            // Retrieve the key bindings from each text field
            String exitProgram = processedKeyInput(exitKeyField.getText());
            String settingsKey = processedKeyInput(settingsKeyField.getText());
            String dahKey = processedKeyInput(dahKeyField.getText());
            String ditKey = processedKeyInput(ditKeyField.getText());
            String frequencyUpKey = processedKeyInput(frequencyUpTextField.getText());
            String frequencyDownKey = processedKeyInput(frequencyDownTextField.getText());
            String filterUpKey = processedKeyInput(filterUpTextField.getText());
            String filterDownKey = processedKeyInput(filterDownTextField.getText());
            //exitProgram settingsKey dahKey ditKey frequencyUpKey frequencyDownKey filterUpKey filterDownKey
            // Update the user's action map with the new bindings

            App.currentUser.setActionMap(exitProgram, settingsKey, dahKey, ditKey, frequencyUpKey, frequencyDownKey, filterUpKey, filterDownKey);

            // Optionally, provide feedback to the user that the save was successful
            System.out.println("Key bindings saved successfully.");
            initialize();

        } else {
            // Handle the scenario where there are duplicate keys (e.g., show an error message)
            System.out.println("Duplicate keys detected. Please resolve before saving.");
        }
    }

    private String processedKeyInput(String input){
        return String.valueOf(input.toUpperCase().charAt(0));
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
}
