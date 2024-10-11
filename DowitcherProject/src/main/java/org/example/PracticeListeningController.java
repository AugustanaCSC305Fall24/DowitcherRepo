package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.IOException;

public class PracticeListeningController{


    // Buttons on screen
    @FXML private Button backButton;
    @FXML private Button goToSettingsButton;
    @FXML private Button replayAudioButton;
    @FXML private Button checkTranslationButton;
    @FXML private TextArea userInputTextArea;
    @FXML private ScrollPane correctAnswerScrollPane;
    private String cwMessage = "Hello";

    @FXML private void switchToHomeScreenView() throws IOException{App.setRoot("HomeScreenView");}

    @FXML
    private void checkTranslation() {
        String userTranslation = userInputTextArea.getText();
        TextFlow correctedTranslation = new TextFlow();

        for (int i = 0; i < userTranslation.length(); i++) {
            Text checkedLetter;
            if (i >= cwMessage.length()) {
                checkedLetter = new Text(".");
                checkedLetter.setStyle("-fx-fill: red;");
            } else {
                checkedLetter = new Text(Character.toString(cwMessage.charAt(i)));
                if (userTranslation.charAt(i) == cwMessage.charAt(i)) {
                    checkedLetter.setStyle("-fx-fill: green;");
                } else {
                    checkedLetter.setStyle("-fx-fill: red;");
                }
            }
            correctedTranslation.getChildren().addAll(checkedLetter);
        }

        correctAnswerScrollPane.setContent(correctedTranslation);
    }

}
