package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeScreenController {

    //MainMenuTab
    @FXML private Button toLiveChatViewButton;
    @FXML private Button toListenViewButton;
    @FXML private Button toTypeViewButton;
    @FXML private Button toTalkViewButton;
    @FXML private Button toTutorialViewButton;
    @FXML private Button exitProgramButton;

    @FXML void handleLiveChatButton(ActionEvent event) {handleUnbuiltButton(toLiveChatViewButton);}
    @FXML private void switchToPracticeListeningView() throws IOException {
        App.setRoot("PracticeListeningView");
    }
    @FXML private void switchToPracticeTypingView() throws IOException {
        App.setRoot("PracticeTypingView");
    }
    @FXML void handleToTalkButton(ActionEvent event) {handleUnbuiltButton(toTalkViewButton);}
    @FXML void handleTutorialButton(ActionEvent event) {handleUnbuiltButton(toTutorialViewButton);}
    @FXML private void handleExitProgramButton() {Stage stage = (Stage) exitProgramButton.getScene().getWindow();stage.close();}
    @FXML private void handleUnbuiltButton(Button object){
        object.setText("Not For This Sprint!");
    }


    //SettingsTab
    @FXML private Button switchToControlMenuButton;
    @FXML private Slider cwSpeedSlider;
    @FXML private Slider volumeSlider;
    @FXML private Slider staticSlider;
    @FXML private CheckBox showCWLettersCheckBox;
    @FXML private CheckBox showCWAcronymsCheckBox;
    @FXML private Button saveButton;

    @FXML public void initialize() {
        cwSpeedSlider.setValue(App.currentUser.getCwSpeed());
        volumeSlider.setValue(App.currentUser.getVolume());
        staticSlider.setValue(App.currentUser.getStaticAmount());
        showCWLettersCheckBox.setSelected(App.currentUser.isShowCWLetters());
        showCWAcronymsCheckBox.setSelected(App.currentUser.isShowCWAcronyms());
    }

    @FXML void switchToControlMenuView() throws IOException {
        App.setRoot("ControlMenuView");
        App.currentUser.addView("HomeScreenView");
    }

    @FXML void handleCWLettersCheckBox(ActionEvent event) {}
    @FXML void handleCWAcronymsCheckBox(ActionEvent event) {}

    @FXML private void handleSaveButton(){
        App.currentUser.setCwSpeed(cwSpeedSlider.getValue());
        App.currentUser.setVolume(volumeSlider.getValue());
        App.currentUser.setStaticAmount(staticSlider.getValue());
        App.currentUser.setShowCWLetters(showCWLettersCheckBox.isSelected());
        App.currentUser.setShowCWAcronyms(showCWAcronymsCheckBox.isSelected());
    }

}
