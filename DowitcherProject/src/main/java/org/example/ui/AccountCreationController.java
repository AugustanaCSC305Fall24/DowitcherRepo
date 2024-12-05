package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.data.User;
import org.example.data.UserSerialization;

import java.io.IOException;

public class AccountCreationController {

    @FXML private TextField callSignTextField;
    @FXML private Button loginButton;
    @FXML private Button signUpButton;
    @FXML private Button continueButton;
    @FXML private Button exitButton;
    @FXML private Label messageLabel;

    private static final String USER_DATA_FILE = "userdata.json";

    // Handle login button click
    @FXML
    private void handleLoginButton() {
        String callSign = callSignTextField.getText().trim();

        // Validate the call sign input
        if (callSign.isEmpty() || isValidCallSign(callSign)) {
            showError("Call sign is not valid.");
            return;
        }

        try {
            // Check if the call sign exists in the userdata file
            boolean userExists = UserSerialization.userExistsByCallSign(callSign);
            if (userExists) {
                // Load the user and set them as the current user
                User user = UserSerialization.loadUsers().stream()
                        .filter(u -> u.getCallSign().equals(callSign))
                        .findFirst()
                        .orElse(null);

                if (user != null) {
                    App.currentUser = user;
                    App.homeScreenView();
                }
            } else {
                showError("Call sign does not exist.");
            }
        } catch (IOException e) {
            showError("Error checking user data: " + e.getMessage());
        }
    }

    // Handle sign-up button click
    @FXML
    private void handleSignUpButton() throws IOException {
        String callSign = callSignTextField.getText().trim();

        // Validate the call sign input
        if (callSign.isEmpty() || isValidCallSign(callSign)) {
            showError("Call sign is not valid.");
            return;
        }

        try {
            // Check if the call sign already exists
            boolean userExists = UserSerialization.userExistsByCallSign(callSign);
            if (userExists) {
                showError("Call sign already exists. Please choose another one.");
                return;
            }

            // Create a new user with default settings
            User newUser = new User(callSign);  // Adjust fields as necessary
            UserSerialization.addUser(newUser);
            App.currentUser = newUser;
            App.homeScreenView();

        } catch (IOException e) {
            showError("Error saving user data: " + e.getMessage());
        }
    }

    @FXML
    private void handleContinueAsGuestButton() throws IOException {
        App.currentUser = new User("Guest");
        App.homeScreenView();  // Navigate to HomeScreenController
    }

    @FXML
    private void handleExitButton() {
        App.exitProgram();  // Exit the application
    }

    private boolean isValidCallSign(String callSign) {
        return !(callSign.length() >= 4 && callSign.length() <= 7 && callSign.matches(".*\\d.*"));
    }

    // Helper method to show error alerts
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
