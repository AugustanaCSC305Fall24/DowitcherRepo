package org.example.ui.mainviews;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import org.example.App;
import org.example.data.ChatMessage;
import org.example.data.User;
import org.example.ui.MorseCodeOutput;
import org.example.utility.MorseCodeTranslator;
import org.example.utility.RadioFunctions;
import java.io.*;
import java.net.URI;
import java.util.Random;
import jakarta.websocket.*;
import org.example.utility.Sound;

import javax.sound.sampled.LineUnavailableException;

@ClientEndpoint
public class LiveChatController implements MorseCodeOutput {
    //Top Hbox
    @FXML private HBox topHBox;
    @FXML private Button settingsButton;
    @FXML private Button menuButton;
    @FXML private Label screenName;
    private final String ROOM_NAME = "Live Chat";

    //Right Vbox
    @FXML private VBox rightVBox;
    @FXML private Slider filterSlider;
    @FXML private Slider frequencySlider;
    @FXML private Slider volumeSlider;
    @FXML private TextArea chatLogTextArea;
    @FXML private Button sendButton; // Send button for sending messages
    @FXML private TextField cwInputTextField;


    //Bottom Hbox
    @FXML private HBox bottomHBox;
    @FXML private Button paddleModeButton;
    @FXML private Button straightKeyModeButton;
    @FXML private Button connectButton;
    @FXML private Button staticButton;

    //Class Objects
    private RadioFunctions radioFunctions;
    private MorseCodeTranslator morseCodeTranslator;
    private Sound sound;
    private int sendFrequency;
    private static final double MIN_FREQUENCY = 7.000; // MHz
    private static final double MAX_FREQUENCY = 7.067; // MHz
    private int targetFrequency;
    private boolean isMatched = false;

    //Data
    @FXML private TextArea mainTextArea;
    private Session session;
    final String USER_NAME = "user" + new Random().nextInt(1000);
    private boolean isPlaying = true;
    private boolean isStaticPlaying = false;
    private Thread staticThread;

    @FXML
    public void initialize() {
        morseCodeTranslator = new MorseCodeTranslator();
        radioFunctions = new RadioFunctions(this);
        sound = new Sound();
        //App.currentUser.addView("LiveChat");
        initializeUIElements();
        getSendMessageFrequency();
    }

    private void initializeUIElements() {
        topHboxInitialized();
        sideVboxInitialized();
        botHboxInitialized();

        mainTextArea.setEditable(false);
        mainTextArea.setFocusTraversable(false); // Makes it undetectable
        mainTextArea.setStyle("-fx-font-size: 30px;"); // Larger text size

    }

    private void topHboxInitialized() {
        if (topHBox == null) {
            topHBox = new HBox();
        }

        // Create the "Settings" button
        settingsButton = new Button("Settings");
        settingsButton.setOnAction(event -> App.togglePopup("SettingsPopup.fxml", settingsButton, 500, 300));
        settingsButton.getStyleClass().add("custom-button"); // Apply button style from the CSS

        // Create the "Menu" button
        Button menuButton = new Button("Menu");
        menuButton.setOnAction(event -> {
            try {
                stopStatic();
                App.homeScreenView();
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

    private void sideVboxInitialized() {
        if (frequencySlider == null) {
            frequencySlider = new Slider(MIN_FREQUENCY, MAX_FREQUENCY, MIN_FREQUENCY);
            frequencySlider.setBlockIncrement(1);
            frequencySlider.setShowTickLabels(true);
            frequencySlider.setShowTickMarks(true);
            frequencySlider.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
            frequencySlider.valueProperty().addListener((observable, oldValue, newValue) -> updateMainTextArea());
            frequencySlider.getStyleClass().add("slider"); // Apply slider style from the CSS
        }

        Slider volumeSlider = new Slider(0, 100, 50); // Create new volume slider
        volumeSlider.setBlockIncrement(1);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> updateMainTextArea());
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> User.setVolume(volumeSlider.getValue()));
        volumeSlider.getStyleClass().add("slider"); // Apply slider style from the CSS

        if (filterSlider == null) {
            filterSlider = new Slider(0, 1, 0.5);
            filterSlider.setShowTickLabels(true);
            filterSlider.setShowTickMarks(true);
            filterSlider.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
            filterSlider.valueProperty().addListener((observable, oldValue, newValue) -> updateMainTextArea());
            filterSlider.valueProperty().addListener((observable, oldValue, newValue) -> adjustStaticVolume());
            filterSlider.getStyleClass().add("slider"); // Apply slider style from the CSS
        }

        // Create and add "Your input" label and text field
        Label yourInputLabel = new Label("Your input:");
        yourInputLabel.getStyleClass().add("label"); // Apply label style from the CSS

        if (cwInputTextField == null) {
            cwInputTextField = new TextField();
            cwInputTextField.getStyleClass().add("text-field"); // Apply text field style from the CSS
        }
        cwInputTextField.setFocusTraversable(false);

        if (sendButton == null) {
            sendButton = new Button("Send");
            sendButton.setOnAction(event -> handleSendButton());
            sendButton.getStyleClass().add("custom-button"); // Apply button style from the CSS
        }

        // Initialize and configure the chatLogTextArea
        chatLogTextArea = new TextArea();
        chatLogTextArea.setEditable(false); // Prevent editing
        chatLogTextArea.setWrapText(true); // Optional: Wrap text for better formatting
        chatLogTextArea.getStyleClass().add("text-area"); // Apply text area style from the CSS

        // Clear and add elements to rightVBox
        rightVBox.getChildren().clear();
        rightVBox.getChildren().addAll(
                frequencySlider,
                volumeSlider,
                filterSlider,
                yourInputLabel,
                cwInputTextField,
                sendButton,
                chatLogTextArea // Add the chatLogTextArea to the VBox
        );
    }

    private void updateMainTextArea() {
        double frequencyValue = frequencySlider.getValue();
        double volumeValue = User.getVolume();
        double filterValue = filterSlider.getValue();

        mainTextArea.setText(String.format(
                "Frequency: %.2f\nVolume: %.2f\nFilter: %.2f",
                frequencyValue,
                volumeValue,
                filterValue
        ));
    }

    private void botHboxInitialized() {
        if (bottomHBox == null) {
            bottomHBox = new HBox();
        }

        if (paddleModeButton == null) {
            paddleModeButton = new Button("Paddle Mode");
            paddleModeButton.setOnAction(event -> handlePaddleMode());
            paddleModeButton.getStyleClass().add("custom-button"); // Apply button style from the CSS
        }

        if (straightKeyModeButton == null) {
            straightKeyModeButton = new Button("Straight Key Mode");
            straightKeyModeButton.setOnAction(event -> handleStraightKeyMode());
            straightKeyModeButton.getStyleClass().add("custom-button"); // Apply button style from the CSS
        }

        if (connectButton == null){
            connectButton = new Button("Connect");
            connectButton.setOnAction(event -> {
                try {
                    connectToServer();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            connectButton.getStyleClass().add("custom-button"); // Apply button style from the CSS
        }

        if(staticButton == null) {
            staticButton = new Button("Play Static");
            staticButton.setOnAction(e -> handleStatic());
            staticButton.getStyleClass().add("custom-button"); // Apply button style from the CSS
        }

        bottomHBox.getChildren().clear();
        bottomHBox.getChildren().addAll(paddleModeButton, straightKeyModeButton, connectButton, staticButton);
    }


@FXML
    private void connectToServer() throws Exception {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, new URI("ws://34.172.226.133:8000/ws/" + USER_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String jsonMessage) throws InterruptedException {
        System.out.println("Received WebSocket message: " + jsonMessage);
        ChatMessage chatMessage = new Gson().fromJson(jsonMessage, ChatMessage.class);
        playMessage(chatMessage);
        targetFrequency = chatMessage.getFrequency();
        chatLogTextArea.appendText(chatMessage.getSender() + " : " + chatMessage.getText() + "\n");
    }

    private void checkFrequencySlider(){
        double tolerence = 0.1;
        if(Math.abs(frequencySlider.getValue() - targetFrequency) <= tolerence){
            isMatched = true;
        }
    }

    private int getFrequencyForSound() {
        double sliderValue = frequencySlider.getValue();
        checkFrequencySlider();
        if (isMatched) return 600;

        double maxDeviation = Math.max(targetFrequency - MIN_FREQUENCY, MAX_FREQUENCY - targetFrequency);
        double deviation = (sliderValue - targetFrequency) / maxDeviation;
        return Math.max(200, Math.min(1200, (int) (600 + deviation * 600)));
    }

    private void playMessage(ChatMessage chatMessage) throws InterruptedException {
        String msg = chatMessage.getText();
        char[] messageArray = msg.toCharArray();
        int frequency = getFrequencyForSound();
        for (int i = 0; i < messageArray.length; i++) {
            if (messageArray[i] == '-') {
                try {
                    Sound.playDah(frequency);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            } else if (messageArray[i] == '.') {
                try {
                    Sound.playDit(frequency);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            } else if (messageArray[i] == ' ') {
                try {
                    Thread.sleep(User.getCwSpeed());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (messageArray[i] == '/') {
                Thread.sleep(2 * User.getCwSpeed());
            }
        }
    }

    private void getSendMessageFrequency(){
        Random rand = new Random();
        sendFrequency = 400 + rand.nextInt(800);
    }


    private void sendMessage(String message) throws IOException {
        ChatMessage msg = new ChatMessage(message, USER_NAME, sendFrequency);
        Gson gson = new Gson();
        String jsonText = gson.toJson(msg);
        System.out.println("Sending WebSocket message: " + jsonText);
        session.getAsyncRemote().sendText(jsonText);
    }

    @FXML
    void handlePracticeMenuButton(ActionEvent event) throws IOException {
        radioFunctions.stopTypingMode();
        App.practiceModesPopupView();
    }

    @FXML
    void handleMainMenuButton(ActionEvent event) throws IOException {
        radioFunctions.stopTypingMode();
        App.homeScreenView();
    }

    @FXML
    private void handleSendButton() {
        String message = cwInputTextField.getText().trim();
        if (!message.isEmpty()) {
            try {
                sendMessage(message);
                chatLogTextArea.appendText("You: " + message + "\n");
                cwInputTextField.clear();
            } catch (Exception e) {
                chatLogTextArea.appendText("Error sending message: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handlePaddleMode() {
        paddleModeButton.setDisable(true);
        straightKeyModeButton.setDisable(false);
        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Paddle", this);
    }

    @FXML
    private void handleStraightKeyMode() {
        paddleModeButton.setDisable(false);
        straightKeyModeButton.setDisable(true);
        radioFunctions.stopTypingMode();
        radioFunctions.setTypingOutputController(this);
        radioFunctions.handleTyping("Straight", this);
    }

    public void addCwToInput(String cwChar) {
        String currentText = cwInputTextField.getText();

        if (cwChar.equals("/")) {
            if (!currentText.isEmpty() && currentText.charAt(currentText.length() - 1) != '/') {
                cwInputTextField.setText(currentText + "/");
            }
        } else if (cwChar.equals(" ")) {
            if (!currentText.isEmpty() && currentText.charAt(currentText.length() - 1) != ' ' && currentText.charAt(currentText.length() - 1) != '/') {
                cwInputTextField.setText(currentText + " ");
            }
        } else {
            cwInputTextField.setText(currentText + cwChar);
        }
    }

    private void handleStatic() {
        if (isStaticPlaying) {
            staticButton.setText("Play Static");
            stopStatic();
        } else {
            staticButton.setText("Pause Static");
            playStatic(getStaticVolume());
            isStaticPlaying = true;
        }
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

    private double getStaticVolume() {
        return (filterSlider.getValue());
    }

    private void stopStatic() {
        staticThread.interrupt();
        sound.setIsStaticPlaying(false);
        isStaticPlaying = false;
    }
}
