package org.example.ui.practice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.App;
import org.example.data.ChatBot;
import org.example.data.User;

import java.io.IOException;
import java.util.Random;

public class BotCreationController {

    // All UI Elements
    @FXML private Button addUpdateButton;
    @FXML private Button backButton;
    @FXML private ListView<ChatBot> botListView; // Changed to display ChatBot objects
    @FXML private TextField contextField;
    @FXML private Button goToChatButton;
    @FXML private Button mainMenuButton;
    @FXML private TextField nameField;
    @FXML private Button removeButton;

    //View Switching Methods
    @FXML void handleBackButton(ActionEvent event) throws IOException {App.back();}
    @FXML void handleGoToChatButton(ActionEvent event) throws IOException {App.generalizedHamRadioView("PracticeTalkingController");}
    @FXML void handleMainMenuButton(ActionEvent event) throws IOException {App.homeScreenView();}

    @FXML
    public void initialize() {
        // Populate the bot list from chatBotRegistry
        botListView.getItems().addAll(User.chatBotRegistry);

        // Set a custom cell factory for better display of ChatBot objects
        botListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(ChatBot bot, boolean empty) {
                super.updateItem(bot, empty);
                setText(empty || bot == null ? null : bot.getName() + " (" + bot.getCallSign() + ")");
            }
        });

        // Handle list item selection to populate form fields
        botListView.setOnMouseClicked(this::handleBotSelection);
    }

    @FXML
    void handleAddUpdateButton(ActionEvent event) {
        String name = nameField.getText();
        String context = contextField.getText();

        if (name.isEmpty() || context.isEmpty()) {
            showAlert("Validation Error", "All fields are required.", Alert.AlertType.ERROR);
            return;
        }

        ChatBot selectedBot = botListView.getSelectionModel().getSelectedItem();

        if (selectedBot == null) {
            // Create a new bot without call sign and frequency (they are auto-generated)
            ChatBot newBot = new ChatBot(name, context, "");
            botListView.getItems().add(newBot);
        } else {
            // Update existing bot (call sign and frequency won't change)
            selectedBot.setName(name);
            selectedBot.setContext(context);
            botListView.refresh();
        }

        //Reset for next bot to be added
        clearForm();
    }


    @FXML
    void handleRemoveButton(ActionEvent event) {
        ChatBot selectedBot = botListView.getSelectionModel().getSelectedItem();
        if (selectedBot == null) {
            showAlert("Selection Error", "Please select a bot to remove.", Alert.AlertType.ERROR);
            return;
        }

        User.chatBotRegistry.remove(selectedBot);
        botListView.getItems().remove(selectedBot);
        clearForm();
    }

    private void handleBotSelection(MouseEvent event) {
        ChatBot selectedBot = botListView.getSelectionModel().getSelectedItem();
        if (selectedBot != null) {
            nameField.setText(selectedBot.getName());
            contextField.setText(selectedBot.getContext());
        }
    }

    private void clearForm() {
        nameField.clear();
        contextField.clear();
        botListView.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
