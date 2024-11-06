package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.data.VertexWebScrapper;

import java.io.IOException;

public class AiTestController {

    @FXML private TextField UserTextField;
    @FXML private TextArea conversationTextArea;
    @FXML private Button homeButton;
    @FXML private Button sendButton;
    @FXML private Button exitButton;

    @FXML public void initialize() {
        conversationTextArea.setText(" ");
        UserTextField.setText(" ");
    }

    @FXML void handleHomeButton(ActionEvent event) throws IOException {App.homeScreenView();}
    @FXML void handleExitButton(ActionEvent event) throws IOException {App.exitProgram();}



    @FXML
    void handleSendButton(ActionEvent event) {
        String msg = UserTextField.getText();
        String convo = conversationTextArea.getText();
        VertexWebScrapper newBot = new VertexWebScrapper();
        String botMsg = newBot.getChatBotResponse(msg);
        conversationTextArea.setText(conversationTextArea.getText() + "\n" + botMsg);
    }

}

