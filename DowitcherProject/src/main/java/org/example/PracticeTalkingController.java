package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.HashMap;

public class PracticeTalkingController {

    //Data
    @FXML private Button practiceMenuButton;
    @FXML private TextArea chatLogTextArea;
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

    // Chatbot instance
    private GenerativeAIChat chatBot;

    //All view switching button presses
    @FXML void handlePracticeMenuButton(ActionEvent event) throws IOException {App.practiceMenuView();}
    @FXML void handleMainMenuButton(ActionEvent event) throws IOException {App.homeScreenView();}

    @FXML
    public void initialize() {
        // Initialize GenerativeAIChat with the text field and a placeholder TextArea or handler for output
        chatBot = new GenerativeAIChat(typingTextField, chatLogTextArea); // Adjust output component as necessary
        chatBot.startChatSession();
    }

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
            Thread.sleep(50);
        }
    }

    private void playDahHold() throws LineUnavailableException, InterruptedException {
        while (isPlaying){
            Sound.playDah();
            Thread.sleep(50);
        }
    }



}
