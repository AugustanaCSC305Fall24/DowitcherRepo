package org.example.ui.practice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.example.App;
import org.example.data.User;
import org.example.utility.Sound;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Random;

public class PracticeTuningController {

    @FXML private Slider frequencySlider;
    @FXML private Slider filterWidthSlider;
    @FXML private Label frequencyLabel;
    @FXML private Label filterLabel;
    @FXML private Label targetFrequencyLabel;
    @FXML private Label feedbackLabel;
    @FXML private Button transmitButton;
    @FXML private Button resetButton;
    @FXML private Button PracticeMenuButton;
    @FXML private Button MainMenuButton;

    private static final double MIN_FREQUENCY = 7.000; // MHz
    private static final double MAX_FREQUENCY = 7.067; // MHz

    private double targetFrequency;
    private final Random random = new Random();
    private String testSound = "... --- ...";
    private boolean isMatched;
    private boolean isPlaying = true;

    // All view switching button presses
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

    private Thread staticSoundThread = null; // This will hold the reference to the static sound thread

    @FXML
    public void initialize() {
        setRandomTargetFrequency();
        frequencySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            frequencyLabel.setText(String.format("%.3f MHz", newVal.doubleValue()));
            checkFrequencyMatch();
        });

        // Single listener for the filterWidthSlider that updates both the label and volume
        filterWidthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            filterLabel.setText(String.format("Filter Width: %.1f KHz", newVal.doubleValue()));
            adjustVolumeAndPlaySound();
        });

        // Play the initial static sound when the view is initialized
        adjustVolumeAndPlaySound();
    }

    private void adjustVolumeAndPlaySound() {
        double volume = getStaticVolume();
        try {
            // Adjust the volume first
            Sound.adjustVolumeOfStatic(volume);
            // Play the static sound in a separate thread if it's not already playing
            if (staticSoundThread == null || !staticSoundThread.isAlive()) {
                staticSoundThread = new Thread(() -> {
                    try {
                        Sound.staticSound(volume, isPlaying);
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();  // Log the exception rather than throwing it
                    }
                });
                staticSoundThread.setDaemon(true);
                staticSoundThread.start();
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();  // Catch and log any errors from adjustVolumeAndPlaySound
        }
    }


    private void playStatic(double volume) {
        Thread audioThread = new Thread(() -> {
            try {
                Sound.staticSound(volume, isPlaying);
            } catch (LineUnavailableException e) {
                e.printStackTrace();  // Catch and log any errors from staticSound
            }
        });
        audioThread.setDaemon(true);
        audioThread.start();
    }

    private double getStaticVolume() {
        // Adjust the volume based on the filterWidthSlider value
        double sliderValue = filterWidthSlider.getValue();
        return 1.0 - (sliderValue / 5.0);  // This dynamically adjusts based on slider value
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
                            e.printStackTrace();
                        }
                    } else if (character == '.') {
                        try {
                            frequency = getFrequencyForSound();
                            Sound.playDit(frequency);
                        } catch (LineUnavailableException e) {
                            e.printStackTrace();
                        }
                    } else if (character == ' ') {
                        try {
                            Thread.sleep(User.getCwSpeed());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        audioThread.setDaemon(true);
        audioThread.start();
    }

    public int getFrequencyForSound() {
        double sliderValue = frequencySlider.getValue();

        if (isMatched) {
            return 600;
        }
        double maxDeviation = Math.max(targetFrequency - MIN_FREQUENCY, MAX_FREQUENCY - targetFrequency);
        double deviation = (sliderValue - targetFrequency) / maxDeviation;
        int frequency = (int) (600 + deviation * 600);

        return Math.max(200, Math.min(frequency, 1200));
    }
}
