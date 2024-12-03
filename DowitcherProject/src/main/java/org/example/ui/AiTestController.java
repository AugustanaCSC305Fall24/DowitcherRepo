package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.data.RadioApiRequestHandler;

import java.io.IOException;

public class AiTestController {

    @FXML private TextField UserTextField;
    @FXML private TextArea conversationTextArea;
    @FXML private Button sendButton;

    private RadioApiRequestHandler radioApiRequestHandler;
    private StringBuilder conversationHistory;

    @FXML
    public void initialize() {
        // Initialize the APIRequestExample class and conversation history
        radioApiRequestHandler = new RadioApiRequestHandler();
        conversationHistory = new StringBuilder();
    }

    @FXML
    public void handleSendButton(ActionEvent event) {
        String inputText = UserTextField.getText().trim();

        if (!inputText.isEmpty()) {
            try {
                // Append the user's input to the conversation history
                conversationHistory.append("You: ").append(inputText).append("\n");

                // Send the user input to the API and get the response
                String response = radioApiRequestHandler.sendRequest(inputText);

                // Append the bot's response to the conversation history
                if (response.contains("Error")) {
                    conversationHistory.append("Bot: Error - ").append(response).append("\n");
                } else {
                    conversationHistory.append("Bot: ").append(response).append("\n");
                }

                // Update the conversationTextArea with the complete conversation history
                conversationTextArea.setText(conversationHistory.toString());

            } catch (Exception e) {
                e.printStackTrace();
                conversationHistory.append("Error: Could not send request.\n");
                conversationTextArea.setText(conversationHistory.toString());
            }
        } else {
            conversationHistory.append("Error: Input is empty.\n");
            conversationTextArea.setText(conversationHistory.toString());
        }

        // Clear the user text field after sending the message
        UserTextField.clear();
    }

    public void handleExitButton(ActionEvent actionEvent) {
        App.exitProgram();
    }

    public void handleHomeButton(ActionEvent actionEvent) throws IOException {
        App.homeScreenView();
    }
}
