package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;

import org.example.data.User;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static User currentUser;
    private static String accessToken;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("LoginView"), 640, 480);
        stage.setScene(scene);
        stage.setFullScreen(true);                          // Make the stage full screen
        stage.setFullScreenExitHint("");                    // Hides the exit hint text
        stage.setFullScreenExitKeyCombination(null);        // Disables the default escape key to exit full screen
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Scene getScene() {
        return scene;
    }

    // All switch view methods
    public static void back() throws IOException {
        setRoot(currentUser.popLastView());
    }

    public static void controlMenuView() throws IOException {
        App.setRoot("ControlMenuView");
    }

    public static void cwAlphabetView() throws IOException {
        App.setRoot("CwAlphabetView");
    }

    public static void homeScreenView() throws IOException {
        App.setRoot("HomeScreenView");
    }

    public static void loginView() throws IOException {
        App.setRoot("LoginView");
    }

    public static void practiceListeningView() throws IOException {
        App.setRoot("PracticeListeningView");
    }

    public static void practiceMenuView() throws IOException {
        App.setRoot("PracticeMenuView");
    }

    public static void practiceTalkingView() throws IOException {
        App.setRoot("PracticeTalkingView");
    }

    public static void practiceTuningView() throws IOException {
        App.setRoot("PracticeTuningView");
    }

    public static void practiceTypingView() throws IOException {
        App.setRoot("PracticeTypingView");
    }

    public static void settingsView() throws IOException {
        App.setRoot("SettingsView");
    }

    public static void signupView() throws IOException {
        App.setRoot("SignupView");
    }

    public static void exitProgram() {
        Platform.exit();
    }

    public static void testAiView() throws IOException {
        App.setRoot("AiTestView");
    }

    public static void liveChatView1() throws IOException {
        App.setRoot("LiveChatChatRoom");
    }

    public static void botAddEditRemoveView() throws IOException {
        App.setRoot("BotAddEditRemoveView");
    }

    // Method to set the background image for the entire scene
    public static void setBackgroundImage(String imagePath) {
        // Get the current scene from the App class
        Scene currentScene = getScene();

        // Load the image from the resources folder
        InputStream imageStream = App.class.getResourceAsStream("/" + imagePath);

        // If the image stream is null, handle the error
        if (imageStream == null) {
            System.out.println("Error: Image not found at path " + imagePath);
            return;
        }

        // Load the image using the InputStream
        Image backgroundImage = new Image(imageStream);

        // Create a BackgroundImage object with necessary properties
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );

        // Get the root of the scene and check its type
        Node root = currentScene.getRoot();
        if (root instanceof VBox) {
            // If the root is a VBox, set the background
            VBox vBoxRoot = (VBox) root;
            vBoxRoot.setBackground(new Background(background));
        } else {
            // Handle other layout types if needed (e.g., HBox, StackPane)
            System.out.println("Unsupported layout type for background: " + root.getClass().getSimpleName());
        }
    }

}


