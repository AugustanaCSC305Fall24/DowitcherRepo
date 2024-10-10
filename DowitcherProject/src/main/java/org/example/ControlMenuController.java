package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControlMenuController {

    @FXML private TextField escKeyField;
    @FXML private TextField mainMenuKeyField;
    @FXML private TextField spaceBarKeyField;
    @FXML private TextField exBeepKeyField;
    @FXML private Button switchToHomeScreenButton;
    @FXML private Button saveButton;

    private final Map<String, TextField> keyBindings = new HashMap<>(); //Chat GPT Generated
    private TextField activeTextField = null; //Chat gpt made

    @FXML public void initialize() { //Chat GPT Generated
        keyBindings.put("Exit Program", escKeyField);
        keyBindings.put("Go To Main Menu", mainMenuKeyField);
        keyBindings.put("CW", spaceBarKeyField);
        keyBindings.put("Pause", exBeepKeyField);

        // Set default key bindings
        escKeyField.setText("ESCAPE");
        mainMenuKeyField.setText("M");
        spaceBarKeyField.setText("SPACE");
        exBeepKeyField.setText("TAB");

        addFocusListeners();
        checkForDuplicateKeys();
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

    @FXML private void handleSaveButton() {  //Chat GPT Generated
        // Save the key bindings
        checkForDuplicateKeys();
        if(!checkForDuplicateKeys()) {
            Map<String, String> savedBindings = new HashMap<>();
            for (Map.Entry<String, TextField> entry : keyBindings.entrySet()) {
                savedBindings.put(entry.getKey(), entry.getValue().getText());
            }

            // Here you can save the bindings to a file, database, or any other storage
            // For demonstration, we'll just print them to the console
            savedBindings.forEach((action, key) -> System.out.println(action + ": " + key));
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
            return hasDuplicates;
        }
}
