package org.example.ui.practice;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import org.example.App;
import org.example.data.User;
import org.example.ui.generalizedHamRadioController;
import org.example.utility.Sound;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Random;

public class TuningGameController extends generalizedHamRadioController {

    @FXML
    private VBox rightVBox;

    @FXML
    private TextArea mainTextArea;

    @FXML
    private Button backButton;

    @FXML
    private Label modeTitleText;

    private Slider frequencySlider;
    private Slider filterWidthSlider;
    private Label frequencyLabel;
    private Label filterLabel;
    private Label targetFrequencyLabel;
    private Label feedbackLabel;
    private Button transmitButton;
    private Button resetButton;

    private static final double MIN_FREQUENCY = 7.000; // MHz
    private static final double MAX_FREQUENCY = 7.067; // MHz

    private double targetFrequency; // Randomly set each reset
    private final Random random = new Random();

    private String testSound = "... --- ...";
    private boolean isMatched;
    private boolean isPlaying = true;

    private Sound sound = new Sound();
    private Thread staticThread;
    private Thread messageThread;

    @FXML
    public void initialize() {
        // Initialize the right VBox before adding elements to it
        if (rightVBox == null) {
            rightVBox = new VBox(); // Ensure rightVBox is initialized
        }

        // Set the mode title text
        modeTitleText = new Label("Tuning Practice");
        modeTitleText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");

        // Add UI elements dynamically to the right VBox
        initializeUIElements();

        // Set up sliders and their listeners
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

        // Start sound and static threads
        playSound(testSound);
        double initialVolume = getStaticVolume();
        playStatic(initialVolume);
    }

    private void initializeUIElements() {
        // Create and configure UI components
        frequencySlider = new Slider(MIN_FREQUENCY, MAX_FREQUENCY, MIN_FREQUENCY);
        frequencyLabel = new Label(String.format("%.3f MHz", MIN_FREQUENCY));
        filterWidthSlider = new Slider(0.5, 5.0, 1.0);
        filterLabel = new Label("Filter Width: 0.5 KHz");
        targetFrequencyLabel = new Label(String.format("Target Frequency: %.3f MHz", MIN_FREQUENCY));
        feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-text-fill: green;");
        transmitButton = new Button("Transmit");
        resetButton = new Button("Reset");
        backButton = new Button("Back");

        // Set button actions
        transmitButton.setOnAction(e -> onTransmit());
        resetButton.setOnAction(e -> onReset());
        backButton.setOnAction(e -> {
            try {
                App.homeScreenView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Layout setup: Use HBox for the top section with the title
        HBox topBox = new HBox();
        topBox.getChildren().add(modeTitleText);
        topBox.setStyle("-fx-alignment: center; -fx-spacing: 20px; -fx-padding: 10px;");

        // Create the side button box (if needed)
        HBox sideButtonsBox = new HBox();
        sideButtonsBox.getChildren().addAll(backButton, transmitButton, resetButton);
        sideButtonsBox.setStyle("-fx-alignment: center; -fx-spacing: 10px; -fx-padding: 10px;");

        // Add components to the right VBox
        rightVBox.getChildren().addAll(topBox, sideButtonsBox,
                new Label("Tuning Frequency (MHz)"), frequencySlider, frequencyLabel,
                new Label("Filter Width (KHz)"), filterWidthSlider, filterLabel,
                targetFrequencyLabel, feedbackLabel);

    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        sound.setIsStaticPlaying(false);
        staticThread.interrupt();
        messageThread.interrupt();
        isPlaying = false;
        App.homeScreenView();
    }

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

    private double getStaticVolume() {
        double sliderValue = filterWidthSlider.getValue();
        return 1.0 - (sliderValue / 5.0);
    }

    private void playSound(String message) {
        messageThread = new Thread(() -> {
            while (isPlaying) {
                int frequency = getFrequencyForSound();
                for (char character : message.toCharArray()) {
                    try {
                        if (character == '-') Sound.playDah(frequency);
                        else if (character == '.') Sound.playDit(frequency);
                        else if (character == ' ') Thread.sleep(User.getCwSpeed());
                        Thread.sleep(200);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        messageThread.setDaemon(true);
        messageThread.start();
    }

    private void playStatic(double volume) {
        staticThread = new Thread(() -> {
            try {
                sound.staticSound(volume, true);
                while (isPlaying) Thread.sleep(100);
                sound.staticSound(volume, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        staticThread.setDaemon(true);
        staticThread.start();
    }

    private int getFrequencyForSound() {
        double sliderValue = frequencySlider.getValue();
        if (isMatched) return 600;

        double maxDeviation = Math.max(targetFrequency - MIN_FREQUENCY, MAX_FREQUENCY - targetFrequency);
        double deviation = (sliderValue - targetFrequency) / maxDeviation;
        return Math.max(200, Math.min(1200, (int) (600 + deviation * 600)));
    }

    private void checkFrequencyMatch() {
        double sliderValue = frequencySlider.getValue();
        isMatched = isWithinFilterWidth(sliderValue, targetFrequency, filterWidthSlider.getValue());
        feedbackLabel.setText(isMatched ? "Matched!" : "Not Matched");
        feedbackLabel.setStyle(isMatched ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}
