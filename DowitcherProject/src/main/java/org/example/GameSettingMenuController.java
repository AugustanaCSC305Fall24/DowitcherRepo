package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.event.ActionEvent;

public class GameSettingMenuController extends SwitchTo{

    @FXML private Button switchToMainMenuButton;
    @FXML private Button switchToSettingMenuViewButton;
    @FXML private Slider cwSpeedSlider;
    @FXML private Slider volumeSlider;
    @FXML private Slider staticSlider;
    @FXML public CheckBox showCWLettersCheckBox;
    @FXML public CheckBox showCWAcronymsCheckBox;
    //made checkboxes public so you can use the .isSelected() method to change how the program is initialized for the practice and live chat, could also have the handle methods change some data stored somewhere for this.


    @FXML void handleCWAcronymsCheckBox(ActionEvent event) {

    }

    @FXML void handleCWLettersCheckBox(ActionEvent event) {

    }
}
