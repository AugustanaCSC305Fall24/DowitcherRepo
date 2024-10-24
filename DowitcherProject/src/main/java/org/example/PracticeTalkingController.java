package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.HashMap;

public class PracticeTalkingController {

    //Data
    @FXML private Button practiceMenuButton;
    @FXML private ScrollPane chatLogScrollPane;
    @FXML private VBox chatLogVBox;
    @FXML private Button dahButton;
    @FXML private Button ditButton;
    @FXML private Slider filterSlider;
    @FXML private Slider frequencySlider;
    @FXML private Button mainMenuButton;
    @FXML private Label roomTitleLabel;
    @FXML private Button straightKeyButton;
    @FXML private TextField typingTextField;

    private boolean isPlaying = false;

    //All view switching button presses
    @FXML void handlePracticeMenuButton(ActionEvent event) throws IOException {App.practiceMenuView();}
    @FXML void handleMainMenuButton(ActionEvent event) throws IOException {App.homeScreenView();}


    //Handlers
    @FXML void handleDahButton() {
        dahButton.setOnMousePressed(event -> {
            new Thread( () -> {
                isPlaying = true;
                try {
                    playDahHold();
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });

        dahButton.setOnMouseReleased(event -> isPlaying = false);
    }
    @FXML void handleDitButton() {
        ditButton.setOnMousePressed(event -> {
            isPlaying = true;
            new Thread( () -> {
                try {
                    playDitHold();
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });

        ditButton.setOnMouseReleased(event -> isPlaying = false);
    }
    @FXML void handleStraightKeyButton(ActionEvent event) {}

    private void straightTone(){

    }

    private void playDitHold() throws LineUnavailableException, InterruptedException {
        while (isPlaying){
            Sound.playDit();
            Thread.sleep(120);
        }
    }

    private void playDahHold() throws LineUnavailableException, InterruptedException {
        while (isPlaying){
            Sound.playDah();
            Thread.sleep(120);
        }
    }



}
