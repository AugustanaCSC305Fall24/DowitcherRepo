package org.example;

import javafx.fxml.FXML;

import java.io.IOException;

public abstract class SwitchTo {

    @FXML
    private void switchToControlMenuView() throws IOException {
        App.setRoot("ControlMenuView");
    }

    @FXML
    private void switchToGameSettingMenuView() throws IOException {
        App.setRoot("GameSettingMenuView");
    }

    @FXML
    private void switchToMainMenuView() throws IOException {
        App.setRoot("MainMenuView");
    }

    @FXML
    private void switchToPracticeListeningView() throws IOException {
        App.setRoot("PracticeListeningView");
    }

    @FXML
    private void switchToPracticeMenuView() throws IOException {
        App.setRoot("PracticeMenuView");
    }

    @FXML
    private void switchToPracticeTypingView() throws IOException {
        App.setRoot("PracticeTypingView");
    }

    @FXML
    private void switchToSettingMenuView() throws IOException {
        App.setRoot("SettingMenuView");
    }

//    @FXML
//    private void switchTo() throws IOException {
//        App.setRoot("");
//    }


}
