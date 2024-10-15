package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControlMenuController {

    //Buttons
    @FXML private Button switchToHomeScreenButton;
    @FXML private Button saveButton;
    @FXML private Button backButton;

    //Keys
    @FXML private TextField checkTranslationKeyField;
    @FXML private TextField datKeyField;
    @FXML private TextField ditKeyField;
    @FXML private TextField escKeyField;
    @FXML private TextField mainMenuKeyField;
    @FXML private TextField newAudioKeyField;
    @FXML private TextField pauseKeyField;
    @FXML private TextField restartAudioKeyField;
    @FXML private TextField settingsKeyField;
    @FXML private TextField translateKeyField;

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
                case "restartAudio":
                    restartAudioKeyField.setText(key);
                    break;
                case "checkTranslation":
                    checkTranslationKeyField.setText(key);
                    break;
                case "newAudio":
                    newAudioKeyField.setText(key);
                    break;
                case "settings":
                    settingsKeyField.setText(key);
                    break;
                case "mainMenu":
                    mainMenuKeyField.setText(key);
                    break;
                case "translate":
                    translateKeyField.setText(key);
                    break;
                case "datAction":
                    datKeyField.setText(key);
                    break;
                case "ditAction":
                    ditKeyField.setText(key);
                    break;
                case "escape":
                    escKeyField.setText(key);
                    break;
                case "pause":
                    pauseKeyField.setText(key);
                    break;
                default:
                    // Handle any unexpected actions if needed
                    break;
            }
        }
    }

    @FXML private void switchToHomeScreenView() throws IOException {App.setRoot("HomeScreenView");}

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
            String restartAudioKey = processedKeyInput(restartAudioKeyField.getText());
            String checkTranslationKey = processedKeyInput(checkTranslationKeyField.getText());
            String newAudioKey = processedKeyInput(newAudioKeyField.getText());
            String settingsKey = processedKeyInput(settingsKeyField.getText());
            String mainMenuKey = processedKeyInput(mainMenuKeyField.getText());
            String translateKey = processedKeyInput(translateKeyField.getText());
            String dahKey = processedKeyInput(datKeyField.getText());
            String ditKey = processedKeyInput(ditKeyField.getText());
            String escKey = processedKeyInput(escKeyField.getText());
            String pauseKey = processedKeyInput(pauseKeyField.getText());

            // Update the user's action map with the new bindings
            App.currentUser.setActionMap(restartAudioKey, checkTranslationKey, newAudioKey, settingsKey, mainMenuKey, translateKey, dahKey, ditKey, escKey, pauseKey);

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

    @FXML private void handleBackButton() throws IOException {
        String previousView = App.currentUser.popLastView();
        App.setRoot(previousView);
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
