package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.data.AccessTokenFetcher;
import org.example.data.GoogleApiRequest;
import java.io.IOException;

public class AiTestController {

    @FXML private TextField UserTextField;
    @FXML private TextArea conversationTextArea;
    @FXML private Button sendButton;

    private GoogleApiRequest googleApiRequest;

    private static final String SERVICE_ACCOUNT_KEY_PATH = "C:\\SoftwareDev\\dowitchercwbot-5d84a82c5f0c.json";

    @FXML
    public void initialize() {
        googleApiRequest = new GoogleApiRequest();
    }

    @FXML
    public void handleSendButton(ActionEvent event) {
        String inputText = UserTextField.getText().trim();

        if (!inputText.isEmpty()) {
            try {
                // Construct the request body
                String requestBody = "{ \"instances\": [{\"input\": \"" + inputText + "\"}] }";

                // Fetch the access token once and pass it
                String accessToken = AccessTokenFetcher.getAccessToken(SERVICE_ACCOUNT_KEY_PATH);

                // Send the API request using the fetched access token
                String response = googleApiRequest.sendApiRequest(requestBody, accessToken);

                // Display the bot's response in the output area
                conversationTextArea.setText(response);
            } catch (Exception e) {
                e.printStackTrace();
                conversationTextArea.setText("Error: Could not send request.");
            }
        } else {
            conversationTextArea.setText("Error: Input is empty.");
        }

        UserTextField.clear();
    }


    public void handleExitButton(ActionEvent actionEvent) {
        App.exitProgram();
    }

    public void handleHomeButton(ActionEvent actionEvent) throws IOException {
        App.homeScreenView();
    }
}
