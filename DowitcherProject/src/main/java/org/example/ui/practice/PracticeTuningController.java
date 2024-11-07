package org.example.ui.practice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.example.App;
import org.example.data.User;
import org.example.utility.Sound;
//import javafx.scene.media.AudioClip;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class PracticeTuningController {

    @FXML
    private Slider frequencySlider;
    @FXML
    private Slider filterWidthSlider;
    @FXML
    private Label frequencyLabel;
    @FXML
    private Label filterLabel;
    @FXML
    private Label targetFrequencyLabel;
    @FXML
    private Label feedbackLabel;
    @FXML
    private Button transmitButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button PracticeMenuButton;
    @FXML
    private Button MainMenuButton;


    private static final double MIN_FREQUENCY = 7.000; // MHz
    private static final double MAX_FREQUENCY = 7.067; // MHz

    private double targetFrequency; // Randomly set each reset
    private final Random random = new Random();

    private String testSound = "... --- ...";
    private boolean isMatched;
    private boolean isPlaying = true;


    //All view switching button presses
    @FXML
    void handlePracticeMenuButton(ActionEvent event) throws IOException {
        isPlaying = false;
        App.practiceMenuView();
    }

    @FXML
    void handleMainMenuButton(ActionEvent event) throws IOException {
        isPlaying = false;
        App.homeScreenView();
    }

    @FXML
    public void initialize() {
        setRandomTargetFrequency();
        frequencySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            frequencyLabel.setText(String.format("%.3f MHz", newVal.doubleValue()));
            checkFrequencyMatch();
        });
        filterWidthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            filterLabel.setText(String.format("Filter Width: %.1f KHz", newVal.doubleValue()));
            try {
                Sound.adjustVolumeOfStatic(getStaticVolume());
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
        playSound(testSound);
        double initialVolume = getStaticVolume();
        playStatic(initialVolume);
    }



    @FXML
    private void onTransmit() {
        double tunedFrequency = frequencySlider.getValue();
        double filterWidth = filterWidthSlider.getValue();

        if (isWithinFilterWidth(tunedFrequency, targetFrequency, filterWidth)) {
            feedbackLabel.setText("Good job!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {
            feedbackLabel.setText("Try again");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void onReset() {
        isMatched = false;
        setRandomTargetFrequency();
        feedbackLabel.setText("");
    }

    private void setRandomTargetFrequency() {
        targetFrequency = MIN_FREQUENCY + (MAX_FREQUENCY - MIN_FREQUENCY) * random.nextDouble();
        targetFrequencyLabel.setText(String.format("Target Frequency: %.3f MHz", targetFrequency));
    }

    private boolean isWithinFilterWidth(double tunedFrequency, double targetFrequency, double filterWidth) {
        double filterRange = filterWidth / 1000; // Convert KHz to MHz
        return Math.abs(tunedFrequency - targetFrequency) <= filterRange;
    }

    @FXML
    public int getFrequencyForSound() {
        double sliderValue = frequencySlider.getValue();

        if (isMatched){
            return 600;
        }
        double maxDeviation = Math.max(targetFrequency - MIN_FREQUENCY, MAX_FREQUENCY - targetFrequency);
        double deviation = (sliderValue - targetFrequency) / maxDeviation;
        int frequency = (int) (600 + deviation * 600);

        return Math.max(200,Math.min(frequency,1200));
    }

    private void checkFrequencyMatch() {
        double sliderValue = frequencySlider.getValue();
        isMatched = isWithinFilterWidth(sliderValue, targetFrequency, filterWidthSlider.getValue());

        if (isMatched) {
            feedbackLabel.setText("Matched!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {
            feedbackLabel.setText("Not Matched");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }


    private void playSound(String message) {
        Thread audioThread = new Thread(() -> {
            while (isPlaying) {
                int frequency = getFrequencyForSound();
                for (char character : message.toCharArray()) {
                    if (character == '-') {
                        try {
                            frequency = getFrequencyForSound();
                            Sound.playDah(frequency);
                        } catch (LineUnavailableException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (character == '.') {
                        try {
                            frequency = getFrequencyForSound();
                            Sound.playDit(frequency);
                        } catch (LineUnavailableException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (character == ' ') {
                        try {
                            Thread.sleep(User.getCwSpeed());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        audioThread.setDaemon(true);
        audioThread.start();
    }
    private void playStatic(double volume){
        Thread audioThread = new Thread(() -> {
            try {
                while (isPlaying) {
                    Sound.staticSound(volume, isPlaying);
                }
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
        audioThread.setDaemon(true);
        audioThread.start();
    }

    private double getStaticVolume() {
        double sliderValue =  filterWidthSlider.getValue();
        return 1.0 - (sliderValue /5.0);
    }

}
