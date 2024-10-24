package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;

public class AccountCreationController {

    // Paths to user data files
    private static final String USER_DATA_FILE = "userdata";

    // Login View
    //Buttons
    @FXML private Button loginButton;
    @FXML private Button signUpButton;
    @FXML private Button exitButton;
    @FXML private Button continueButton;

    //TextFields
    @FXML private TextField passwordTextfield;
    @FXML private TextField usernameTextfield;

    //Button Handlers
    @FXML private void handleSignUpButton() throws IOException {App.signupView();}
    @FXML private void handleExitButton() {App.exitProgram();}
    @FXML private void handleContinueAsGuestButton() throws IOException {App.homeScreenView();}

    @FXML private void handleLoginButton(ActionEvent event) {
        if (validLogin()) {
            try {
                User loginUser = UserSerialization.deserializeUser(USER_DATA_FILE); // Load user data from file
                App.currentUser = loginUser; // Set the current user in the app
                App.homeScreenView(); // Navigate to the home screen
            } catch (IOException | ClassNotFoundException e) {
                // Handle any errors in loading the user data
                e.printStackTrace();
            }
        } else {
            // Show an error message: Invalid username or password
            System.out.println("Invalid username or password.");
        }
    }

    // Helper Method for Login Button
    private boolean validLogin() {
        try {
            // Deserialize the user object from file to check credentials
            User savedUser = UserSerialization.deserializeUser(USER_DATA_FILE);

            // Check if the entered username and password match the saved user data
            if (savedUser.getUsername().equals(usernameTextfield.getText()) &&
                    savedUser.getPassword().equals(passwordTextfield.getText())) {
                return true;
            }

        } catch (IOException | ClassNotFoundException e) {
            // Handle any errors (e.g., file not found)
            e.printStackTrace();
        }

        return false; // Return false if no match is found
    }

    // -----------------------------------------------------------------------------------------------------
    // Signup View
    @FXML private TextField createUsernameTextfield;
    @FXML private TextField createUsernameCheckTextfield;
    @FXML private TextField createPasswordTextfield;
    @FXML private TextField createPasswordCheckTextfield;
    @FXML private TextField emailTextfield;
    @FXML private TextField emailCheckTextfield;
    @FXML private Button backButton;
    @FXML private Button createAccountButton;

    private String sameTextColor = "-fx-background-color: red;";
    private String defaultColor = "";

    // Field handlers
    @FXML private void handleCreateUsernameTextfield() { handleTextFieldSameCheck(createUsernameTextfield, createUsernameCheckTextfield); }
    @FXML private void handleCreateUsernameCheckTextfield() { handleTextFieldSameCheck(createUsernameTextfield, createUsernameCheckTextfield); }
    @FXML private void handleCreatePasswordTextfield() { handleTextFieldSameCheck(createPasswordTextfield, createPasswordCheckTextfield); }
    @FXML private void handleCreatePasswordCheckTextfield() { handleTextFieldSameCheck(createPasswordTextfield, createPasswordCheckTextfield); }
    @FXML private void handleEmailTextfield() { handleTextFieldSameCheck(emailTextfield, emailCheckTextfield); }
    @FXML private void handleEmailCheckTextfield() { handleTextFieldSameCheck(emailTextfield, emailCheckTextfield); }

    // Bottom Buttons
    @FXML private void handleBackButton() throws IOException { App.setRoot("LoginView"); }

    @FXML private void handleCreateAccountButton() {
        if (!sameTextField()) {
            // Create a new user object based on input fields
            User newUser = new User(createUsernameTextfield.getText(), createPasswordTextfield.getText(), emailTextfield.getText());

            // Serialize the new user object to the file
            try {
                UserSerialization.serializeUser(newUser, USER_DATA_FILE);
                System.out.println("Account created successfully!");
                App.currentUser = newUser; // Set the current user
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sameTextField();
        }
    }

    // Helper Methods for TextField Handlers
    private void handleTextFieldSameCheck(TextField f1, TextField f2) {
        if (!f1.getText().equals(f2.getText())) {
            setTextFieldRed(f1, f2);
            createAccountButton.setDisable(true);
        } else {
            setTextFieldNormal(f1, f2);
            createAccountButton.setDisable(false);
        }
    }

    private void setTextFieldNormal(TextField f1, TextField f2) {
        f1.setStyle(defaultColor);
        f2.setStyle(defaultColor);
    }

    private void setTextFieldRed(TextField f1, TextField f2) {
        f1.setStyle(sameTextColor);
        f2.setStyle(sameTextColor);
    }

    private boolean sameTextField() {
        return (!sameUsername() || !samePassword() || !sameEmail());
    }

    private boolean sameUsername() {
        return createUsernameTextfield.getText().equals(createUsernameCheckTextfield.getText());
    }

    private boolean samePassword() {
        return createPasswordTextfield.getText().equals(createPasswordCheckTextfield.getText());
    }

    private boolean sameEmail() {
        return emailTextfield.getText().equals(emailCheckTextfield.getText());
    }
}
