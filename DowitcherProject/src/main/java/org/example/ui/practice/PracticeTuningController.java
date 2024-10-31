package org.example.ui.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
//import javafx.scene.media.AudioClip;

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

    private static final double MIN_FREQUENCY = 7.000; // MHz
    private static final double MAX_FREQUENCY = 7.067; // MHz
    //private static final String SOUND_FILE = "path/to/sound/file.wav"; // Path to your sound file

    private double targetFrequency; // Randomly set each reset
    // private AudioClip audioClip;
    private final Random random = new Random();


    @FXML
    public void initialize() {
        // Load the audio file
        //URL resource = getClass().getResource(SOUND_FILE);
        //if (resource != null) {
           // audioClip = new AudioClip(resource.toString());
       // }


        // Bind sliders to update labels in real-time
        frequencySlider.valueProperty().addListener((obs, oldVal, newVal) ->
                frequencyLabel.setText(String.format("%.3f MHz", newVal.doubleValue())));

        filterWidthSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                filterLabel.setText(String.format("Filter Width: %.1f KHz", newVal.doubleValue())));

        // Set initial random frequency
        setRandomTargetFrequency();
    }

    @FXML
    private void onTransmit() {
        double tunedFrequency = frequencySlider.getValue();
        double filterWidth = filterWidthSlider.getValue();

        if (isWithinFilterWidth(tunedFrequency, targetFrequency, filterWidth)) {
            feedbackLabel.setText("Good job!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            //playSound(1.0); // Normal pitch if within filter range
        } else {
            feedbackLabel.setText("Try again");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            //double pitch = calculatePitch(tunedFrequency, targetFrequency);
            //playSound(pitch); // Adjust pitch based on frequency difference
        }
    }

    @FXML
    private void onReset() {
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

    private double calculatePitch(double tunedFrequency, double targetFrequency) {
        double frequencyDifference = Math.abs(tunedFrequency - targetFrequency);
        return 1.0 + (frequencyDifference * 15); // Increase pitch more significantly with distance
    }

    //private void playSound(double pitch) {
        //if (audioClip != null) {
            //audioClip.setRate(pitch); // Adjust pitch by modifying playback rate
            //audioClip.play();}}


}

