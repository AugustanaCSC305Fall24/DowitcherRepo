package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AccountCreationController {


    //Login View
    @FXML private Button loginButton;
    @FXML private TextField passwordTextfield;
    @FXML private Button signUpButton;
    @FXML private TextField usernameTextfield;

    @FXML void handleLoginButton(ActionEvent event) {}
    @FXML void handlePasswordTextfield(ActionEvent event) {}
    @FXML void handleSignUpButton(ActionEvent event) {}
    @FXML void handleUsernameTextfield(ActionEvent event) {}



    //-----------------------------------------------------------------------------------------------------
    //Signup View
    @FXML private TextField createUsernameTextfield;
    @FXML private TextField createUsernameCheckTextfield;

    @FXML private TextField createPasswordTextfield;
    @FXML private TextField createPasswordCheckTextfield;

    @FXML private TextField emailTextfield;
    @FXML private TextField emailCheckTextfield;

    @FXML private Button backButton;
    @FXML private Button createAccountButton;


    //Field handlers
    @FXML private void handleCreateUsernameTextfield(){}
    @FXML private void handleCreateUsernameCheckTextfield(){}
    @FXML private void handleCreatePasswordTextfield(){}
    @FXML private void handleCreatePasswordCheckTextfield(){}
    @FXML private void handleEmailTextfield(){}
    @FXML private void handleEmailCheckTextfield(){}


    //Bottom Buttons
    @FXML private void handleBackButton(){}
    @FXML private void handleCreateAccountButton(){}

}
