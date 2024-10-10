package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class PracticeTypingController {

    @FXML private Button practiceTypingBackButton;

    @FXML private void switchToHomeScreenView() throws IOException {App.setRoot("HomeScreenView");}


}
