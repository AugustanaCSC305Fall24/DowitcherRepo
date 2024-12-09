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

public class TuningGameController  {

    @FXML
    private VBox rightVBox;

    @FXML private HBox topHBox;

    @FXML
    private TextArea mainTextArea;

    @FXML
    private Label modeTitleText;

    private Slider frequencySlider;
    private Slider filterWidthSlider;
    private Slider volumeSlider;
    private Label frequencyLabel;
    private Label filterLabel;
    private Label volumeLabel;
    private Label targetFrequencyLabel;
    private Label feedbackLabel;
    private Button transmitButton;
    private Button resetButton;
    private Button staticButton;

    private static final double MIN_FREQUENCY = 7.000; // MHz
    private static final double MAX_FREQUENCY = 7.067; // MHz

    private double targetFrequency; // Randomly set each reset
    private final Random random = new Random();

    private String testSound = "... --- ...";
    private boolean isMatched;
    private boolean isPlaying = true;
    private boolean isStaticPlaying = false;

    private Sound sound = new Sound();
    private Thread staticThread;
    private Thread messageThread;

    // Room Name
    private final String ROOM_NAME = "Practice Tuning";

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

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            volumeLabel.setText(String.format("Volume: %.1f", newVal.doubleValue()));
            updateMainTextArea();
        });

        frequencySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            frequencyLabel.setText(String.format("%.3f MHz", newVal.doubleValue()));
            checkFrequencyMatch();
            updateMainTextArea();
        });

        filterWidthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            filterLabel.setText(String.format("Filter Width: %.1f KHz", newVal.doubleValue()));
            updateMainTextArea();
            try {
                Sound.adjustVolumeOfStatic(getStaticVolume());
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });

        // Start sound and static threads
        isPlaying = true;
        playSound(testSound);
    }

    private void initializeUIElements() {

        topHboxInitialized();

        // Create and configure UI components
        frequencySlider = new Slider(MIN_FREQUENCY, MAX_FREQUENCY, MIN_FREQUENCY);
        frequencyLabel = new Label(String.format("%.3f MHz", MIN_FREQUENCY));
        filterWidthSlider = new Slider(0.5, 5.0, 1.0);
        filterWidthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            adjustStaticVolume(); // Adjust the static volume dynamically
        });
        filterLabel = new Label("Filter Width: 0.5 KHz");
        volumeSlider = new Slider(0.0, 100.0, 50.0);
        volumeLabel = new Label("Volume");
        targetFrequencyLabel = new Label(String.format("Target Frequency: %.3f MHz", MIN_FREQUENCY));
        feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-text-fill: green;");
        transmitButton = new Button("Transmit");
        staticButton = new Button("Play Static");
        resetButton = new Button("Reset");

        // Set button actions
        transmitButton.setOnAction(e -> onTransmit());
        staticButton.setOnAction(e -> handleStatic());
        resetButton.setOnAction(e -> onReset());

        // Layout setup: Use HBox for the top section with the title
        HBox topBox = new HBox();
        topBox.getChildren().add(modeTitleText);
        topBox.setStyle("-fx-alignment: center; -fx-spacing: 20px; -fx-padding: 10px;");

        // Create the side button box (if needed)
        HBox sideButtonsBox = new HBox();
        sideButtonsBox.getChildren().clear();

        sideButtonsBox.getChildren().addAll(transmitButton, staticButton, resetButton);

        sideButtonsBox.setStyle("-fx-alignment: center; -fx-spacing: 10px; -fx-padding: 10px;");

        // Add components to the right VBox
        rightVBox.getChildren().addAll(topBox, sideButtonsBox, volumeSlider, volumeLabel,
                new Label("Tuning Frequency (MHz)"), frequencySlider, frequencyLabel,
                new Label("Filter Width (KHz)"), filterWidthSlider, filterLabel,
                feedbackLabel);

        // Configure mainTextArea to display slider values, be non-editable, and have larger text
        mainTextArea.setEditable(false);
        mainTextArea.setFocusTraversable(false); // Makes it undetectable
        mainTextArea.setStyle("-fx-font-size: 30px;"); // Larger text size
        updateMainTextArea();

    }

    private void topHboxInitialized() {
        if (topHBox == null) {
            topHBox = new HBox();
        }

        // Create the "Settings" button
        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(event -> App.togglePopup("SettingsPopup.fxml", settingsButton));
        settingsButton.getStyleClass().add("custom-button"); // Apply button style from the CSS

        // Create the "Menu" button
        Button menuButton = new Button("Menu");
        menuButton.setOnAction(event -> {
            try {
                back();
            } catch (IOException e) {
                throw new RuntimeException("Failed to return to home screen", e);
            }
        });
        menuButton.getStyleClass().add("custom-button"); // Apply button style from the CSS

        // Add the buttons to the topHBox
        topHBox.getChildren().clear();
        topHBox.getChildren().addAll(menuButton, settingsButton);

        // Ensure topHBox has the expected components before adding "AI Chat" label
        Label screenName = new Label(ROOM_NAME);
        screenName.getStyleClass().add("label"); // Apply label style from the CSS
        topHBox.getChildren().add(1, screenName); // Insert label between menu and settings buttons
    }

    private void updateMainTextArea() {
        double frequencyValue = frequencySlider.getValue();
        double volumeValue = volumeSlider.getValue();
        double filterValue = filterWidthSlider.getValue();

        mainTextArea.setText(String.format(
                "Frequency: %.3f\nVolume: %.2f\nFilter: %.2f",
                frequencyValue,
                volumeValue,
                filterValue
        ));
    }

    private void handleStatic() {
        if (isStaticPlaying) {
            staticButton.setText("Play Static");
            staticThread.interrupt();
            sound.setIsStaticPlaying(false);
            isStaticPlaying = false;
        } else {
            staticButton.setText("Pause Static");
            playStatic(getStaticVolume());
            isStaticPlaying = true;
        }
    }

    @FXML
    private void back() throws IOException {
        sound.setIsStaticPlaying(false);
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
        return (sliderValue / 5.0);
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

    private void adjustStaticVolume() {
        // Adjust the volume directly if the static sound is playing
        if (isStaticPlaying && sound != null) {
            sound.adjustVolume(getStaticVolume());
        }
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
