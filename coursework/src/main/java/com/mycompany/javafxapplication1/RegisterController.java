/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class RegisterController {

    private MainController mc = new MainController();
    private DB db = new DB();

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button registerBtn;

    @FXML
    private Button backLoginBtn;

    @FXML
    private PasswordField passPasswordField;

    @FXML
    private PasswordField rePassPasswordField;

    @FXML
    private TextField userTextField;

    @FXML
    private Text fileText;

    @FXML
    private void registerBtnHandler(ActionEvent event) {
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            //check there is empty input field
            if (userTextField.getText().equals("") || passPasswordField.getText().equals("")) {
                this.mc.dialogue("Error", "Username or password cannot be empty!", Alert.AlertType.ERROR);
            } else {
                //check pass and confirm pass match
                if (passPasswordField.getText().equals(rePassPasswordField.getText())) {
                    //check if password length is greater than or equal to 8
                    if (passPasswordField.getText().length() >= 8) {
                        //validate if user already exist in database
                        if (!this.db.validateUser(userTextField.getText())) {
                            //add data to database
                            this.db.addUserDataToDB(userTextField.getText(), passPasswordField.getText());
                            this.mc.dialogue("Adding information to the database", "Successful!", Alert.AlertType.INFORMATION);
                            String[] credentials = {userTextField.getText()};
                            this.mc.redirectUser(credentials);

                        } else {
                            this.mc.dialogue("Error", "User already exist.", Alert.AlertType.ERROR);
                            return;
                        }
                    }
                    else{
                        this.mc.dialogue("Error", "Password must contain atleast 8 characters. You entered "+ passPasswordField.getText().length() +" characters.", Alert.AlertType.ERROR);
                        return;
                    }

                } else {
                    this.mc.dialogue("Error", "password and confirm password did not match.", Alert.AlertType.ERROR);
                    return;
                }
                primaryStage.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backLoginBtnHandler(ActionEvent event) {
        Stage primaryStage = (Stage) backLoginBtn.getScene().getWindow();
        try {
            this.mc.redirectLogin();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
