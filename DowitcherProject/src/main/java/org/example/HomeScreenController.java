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
    @FXML private void switchToPracticeListeningView() throws IOException {App.setRoot("PracticeListeningView");}
    @FXML private void switchToPracticeTypingView() throws IOException {App.setRoot("PracticeTypingView");}
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
    private User currentUser;

    @FXML public void initialize() {
        this.currentUser = User.getInstance();
        cwSpeedSlider.setValue(currentUser.getCwSpeed());
        volumeSlider.setValue(currentUser.getVolume());
        staticSlider.setValue(currentUser.getStaticAmount());
        showCWLettersCheckBox.setSelected(currentUser.isShowCWLetters());
        showCWAcronymsCheckBox.setSelected(currentUser.isShowCWAcronyms());
    }

    @FXML void switchToControlMenuView() throws IOException {App.setRoot("ControlMenuView");}

    @FXML void handleCWLettersCheckBox(ActionEvent event) {}
    @FXML void handleCWAcronymsCheckBox(ActionEvent event) {}

    @FXML private void handleSaveButton(){
        currentUser.setCwSpeed(cwSpeedSlider.getValue());
        currentUser.setVolume(volumeSlider.getValue());
        currentUser.setStaticAmount(staticSlider.getValue());
        currentUser.setShowCWLetters(showCWLettersCheckBox.isSelected());
        currentUser.setShowCWAcronyms(showCWAcronymsCheckBox.isSelected());
    }

}
