package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

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

    private String sameTextColor = "-fx-background-color: red;";
    private String defaultColor = "";

    //Field handlers
    @FXML private void handleCreateUsernameTextfield(){handleTextFieldSameCheck(createUsernameTextfield, createUsernameCheckTextfield);}
    @FXML private void handleCreateUsernameCheckTextfield(){handleTextFieldSameCheck(createUsernameTextfield, createUsernameCheckTextfield);}
    @FXML private void handleCreatePasswordTextfield(){handleTextFieldSameCheck(createPasswordTextfield, createPasswordCheckTextfield);}
    @FXML private void handleCreatePasswordCheckTextfield(){handleTextFieldSameCheck(createPasswordTextfield, createPasswordCheckTextfield);}
    @FXML private void handleEmailTextfield(){handleTextFieldSameCheck(emailTextfield, emailCheckTextfield);}
    @FXML private void handleEmailCheckTextfield(){handleTextFieldSameCheck(emailTextfield, emailCheckTextfield);}


    //Bottom Buttons
    @FXML private void handleBackButton() throws IOException {App.setRoot(App.currentUser.popLastView());}
    @FXML private void handleCreateAccountButton(){
        if(!sameTextField()){
            App.currentUser = new User(createUsernameTextfield.getText(), createPasswordTextfield.getText(), emailTextfield.getText());
        }
        else{
            sameTextField();
        }
    }


    // Helper Methods for TextField Handlers
    private void handleTextFieldSameCheck(TextField f1, TextField f2){if(f1.equals(f2)){setTextFieldRed(f1, f2); createAccountButton.setDisable(true);} else{setTextFieldNormal(f1, f2); createAccountButton.setDisable(false);}}
    private void setTextFieldNormal(TextField f1, TextField f2){f1.setStyle(defaultColor); f2.setStyle(defaultColor);}
    private void setTextFieldRed(TextField f1, TextField f2){f1.setStyle(sameTextColor); f2.setStyle(sameTextColor);}
    private boolean sameTextField() {return(sameUsername() || samePassword() || sameEmail());}
    private boolean sameUsername(){return(createUsernameTextfield.getText().equals(createUsernameCheckTextfield.getText()));}
    private boolean samePassword(){return(createPasswordTextfield.getText().equals(createPasswordCheckTextfield.getText()));}
    private boolean sameEmail(){return(emailTextfield.getText().equals(emailCheckTextfield.getText()));}

}
