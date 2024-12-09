package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.stage.Window;
import org.example.data.User;
import org.example.ui.mainviews.AiChatController;
import org.example.ui.mainviews.LiveChatController;
import org.example.ui.practice.*;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static User currentUser;
    private static final Map<String, Object> CONTROLLER_MAP = new HashMap<>();
    private static final Map<String, Object> POPUP_MAP = new HashMap<>();

    // Paths for organized FXML files
    private static final String MAIN_VIEWS_PATH = "org/example/mainviews/";
    private static final String POPUP_VIEWS_PATH = "org/example/popup/";

    static {
        // Preload controllers for reuse
        CONTROLLER_MAP.put("AlphabetGameController", new AlphabetGameController());
        CONTROLLER_MAP.put("ListeningGameController", new ListeningGameController());
        CONTROLLER_MAP.put("TuningGameController", new TuningGameController());
        CONTROLLER_MAP.put("TypingGameController", new TypingGameController());
        CONTROLLER_MAP.put("PracticeTalkingController", new AiChatController());
        CONTROLLER_MAP.put("LiveChatChatRoomController", new LiveChatController());
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML(MAIN_VIEWS_PATH + "LoginView.fxml"), 640, 480);
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.setMaximized(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(null);
        stage.show();
    }

    private static Parent loadFXML(String fxmlPath) throws IOException {
        System.out.println("Loading FXML from: " + fxmlPath);
        var resource = App.class.getResource("/" + fxmlPath);
        if (resource == null) {
            throw new IOException("FXML file not found: " + fxmlPath);
        }
        return new FXMLLoader(resource).load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Scene getScene() {
        return scene;
    }

    // View-switching methods
    public static void back() throws IOException {
        setRoot(currentUser.popLastView());
    }

    public static void controlMenuView() throws IOException {
        setRoot(MAIN_VIEWS_PATH + "ControlsPopup.fxml");
    }

    public static void homeScreenView() throws IOException {
        setRoot(MAIN_VIEWS_PATH + "HomeScreenView.fxml");
    }

    public static void loginView() throws IOException {
        setRoot(MAIN_VIEWS_PATH + "LoginView.fxml");
    }

    public static void botView() throws IOException {
        setRoot(MAIN_VIEWS_PATH + "BotAddEditRemoveView.fxml");
    }

    private static void setRoot(String fxmlPath) throws IOException {
        scene.setRoot(loadFXML(fxmlPath));
    }

    public static void generalizedHamRadioView(String controllerName) throws IOException {
        System.out.println("Attempting to load: " + "/org/example/mainviews/GeneralizedHamRadioView.fxml");

        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/mainviews/GeneralizedHamRadioView.fxml"));

        // Lazy load the controller if necessary
        Object controller = CONTROLLER_MAP.computeIfAbsent(controllerName, key -> {
            switch (key) {
                case "AlphabetGameController": return new AlphabetGameController();
                case "ListeningGameController": return new ListeningGameController();
                case "TuningGameController": return new TuningGameController();
                case "TypingGameController": return new TypingGameController();
                case "PracticeTalkingController": return new AiChatController();
                case "LiveChatChatRoomController": return new LiveChatController();
                default: throw new IllegalArgumentException("Unknown controller: " + controllerName);
            }
        });

        // Ensure the FXML file does not declare a controller in fx:controller attribute
        loader.setController(controller);

        // Load the FXML and set the root of the scene
        Parent root = loader.load();
        scene.setRoot(root);
    }



    public static void settingsPopupView() throws IOException {
        setRoot(POPUP_VIEWS_PATH + "SettingsPopup.fxml");
    }

    public static void practiceModesPopupView() throws IOException {
        setRoot(POPUP_VIEWS_PATH + "LearnModesPopup.fxml");
    }

    public static void keyChangePopupView() throws IOException {
        setRoot(POPUP_VIEWS_PATH + "KeyChangePopup.fxml");
    }

    public static void exitProgram() {
        Platform.exit();
    }

    public static void togglePopup(String fxmlFile, Node anchorNode) {
        try {
            Stage popupStage = (Stage) POPUP_MAP.get(fxmlFile);

            // If the popup isn't initialized, create it
            if (popupStage == null) {
                var resource = App.class.getResource("/" + POPUP_VIEWS_PATH + fxmlFile);
                if (resource == null) {
                    throw new IOException("FXML file not found for popup: " + fxmlFile);
                }
                FXMLLoader loader = new FXMLLoader(resource);
                Parent content = loader.load();
                popupStage = new Stage(StageStyle.DECORATED); // Adds decorations
                popupStage.setScene(new Scene(content));

                // Set the size of the popup
                popupStage.setWidth(400);  // Set the width to 300
                popupStage.setHeight(600); // Set the height to 600

                popupStage.initModality(Modality.NONE); // Allows interaction with other windows
                popupStage.setTitle("Popup - " + fxmlFile); // Optional title
                popupStage.setOnCloseRequest(e -> POPUP_MAP.remove(fxmlFile)); // Cleanup on close
                POPUP_MAP.put(fxmlFile, popupStage);
            }

            // Toggle popup visibility
            if (popupStage.isShowing()) {
                popupStage.hide();
            } else {
                // Get screen coordinates for popup placement
                Window window = anchorNode.getScene().getWindow();
                double xPos = anchorNode.localToScene(anchorNode.getBoundsInLocal()).getMinX() + window.getX();
                double yPos = anchorNode.localToScene(anchorNode.getBoundsInLocal()).getMaxY() + window.getY() + 10;

                popupStage.setX(xPos);
                popupStage.setY(yPos);
                popupStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void togglePopupWithScroll(String fxmlFile, Button triggerButton, double width, double height) {
        try {
            // Retrieve existing stage from the map
            Stage popupStage = (Stage) POPUP_MAP.get(fxmlFile);

            // If the popup isn't initialized, create it
            if (popupStage == null) {
                var resource = App.class.getResource("/" + POPUP_VIEWS_PATH + fxmlFile);
                if (resource == null) {
                    throw new IOException("FXML file not found for popup: " + fxmlFile);
                }

                FXMLLoader loader = new FXMLLoader(resource);
                Parent content = loader.load();

                // Wrap content in a ScrollPane
                ScrollPane scrollPane = new ScrollPane(content);
                scrollPane.setPrefSize(width, height);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                popupStage = new Stage(StageStyle.DECORATED); // Add window decorations (title bar, close button)
                popupStage.setScene(new Scene(scrollPane));

                // Set the size of the popup
                popupStage.setWidth(400);  // Set the width to 300
                popupStage.setHeight(700); // Set the height to 600

                popupStage.initModality(Modality.NONE); // Allows interaction with other windows
                popupStage.setTitle("Popup - " + fxmlFile); // Optional title
                popupStage.setOnCloseRequest(e -> POPUP_MAP.remove(fxmlFile)); // Cleanup on close
                POPUP_MAP.put(fxmlFile, popupStage);
            }

            // Toggle popup visibility
            if (popupStage.isShowing()) {
                popupStage.hide();
            } else {
                // Get screen coordinates for popup placement
                Window window = triggerButton.getScene().getWindow();
                double xPos = triggerButton.localToScene(triggerButton.getBoundsInLocal()).getMinX() + window.getX();
                double yPos = triggerButton.localToScene(triggerButton.getBoundsInLocal()).getMaxY() + window.getY() + 10;

                popupStage.setX(xPos);
                popupStage.setY(yPos);
                popupStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
