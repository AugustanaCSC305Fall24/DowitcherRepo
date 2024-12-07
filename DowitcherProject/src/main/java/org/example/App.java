package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javafx.stage.Window;
import org.example.data.User;
import org.example.ui.generalizedHamRadioController;
import org.example.ui.practice.AlphabetGameController;
import org.example.ui.practice.ListeningGameController;
import org.example.ui.practice.TuningGameController;
import org.example.ui.practice.TypingGameController;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static User currentUser;
    private static final Map<String, Object> CONTROLLER_MAP = new HashMap<>();
    private static final Map<String, Popup> POPUP_MAP = new HashMap<>();

    // Paths for organized FXML files
    private static final String MAIN_VIEWS_PATH = "org/example/mainviews/";
    private static final String POPUP_VIEWS_PATH = "org/example/popup/";

    static {
        // Preload controllers for reuse
        CONTROLLER_MAP.put("AlphabetGameController", new AlphabetGameController());
        CONTROLLER_MAP.put("ListeningGameController", new ListeningGameController());
        CONTROLLER_MAP.put("TuningGameController", new TuningGameController());
        CONTROLLER_MAP.put("TypingGameController", new TypingGameController());
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML(MAIN_VIEWS_PATH + "LoginView.fxml"), 640, 480);
        stage.setScene(scene);
        stage.setFullScreen(true);
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
        setRoot(MAIN_VIEWS_PATH + "EditControlsView.fxml");
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
        setRoot(POPUP_VIEWS_PATH + "PracticeModesPopup.fxml");
    }

    public static void keyChangePopupView() throws IOException {
        setRoot(POPUP_VIEWS_PATH + "KeyChangePopup.fxml");
    }

    public static void exitProgram() {
        Platform.exit();
    }

    public static void setBackgroundImage(String imagePath) {
        InputStream imageStream = App.class.getResourceAsStream("/" + imagePath);
        if (imageStream == null) {
            System.err.println("Error: Image not found at path " + imagePath);
            return;
        }

        Image backgroundImage = new Image(imageStream);
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );

        Node root = getScene().getRoot();
        if (root instanceof VBox) {
            ((VBox) root).setBackground(new Background(background));
        } else {
            System.err.println("Unsupported layout type for background: " + root.getClass().getSimpleName());
        }
    }

    public static void togglePopup(String fxmlFile, Button anchorButton) {
        try {
            Popup popup = POPUP_MAP.get(fxmlFile);

            // If the popup isn't initialized, create it
            if (popup == null) {
                var resource = App.class.getResource("/" + POPUP_VIEWS_PATH + fxmlFile);
                if (resource == null) {
                    throw new IOException("FXML file not found for popup: " + fxmlFile);
                }
                FXMLLoader loader = new FXMLLoader(resource);
                Parent content = loader.load();
                popup = new Popup();
                popup.getContent().add(content);
                popup.setAutoHide(true);
                popup.setHideOnEscape(true);
                POPUP_MAP.put(fxmlFile, popup);
            }

            // Toggle popup visibility
            if (popup.isShowing()) {
                popup.hide();
            } else {
                // Get screen coordinates for popup placement
                Window window = anchorButton.getScene().getWindow();
                double xPos = anchorButton.localToScene(anchorButton.getBoundsInLocal()).getMinX()
                        + window.getX();
                double yPos = anchorButton.localToScene(anchorButton.getBoundsInLocal()).getMaxY()
                        + window.getY() + 10;

                popup.show(window, xPos, yPos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
