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
    private MainController mc=new MainController();
    private DB db=new DB();
    
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
    private Button selectBtn;
    
    @FXML
    private void selectBtnHandler(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) selectBtn.getScene().getWindow();
        primaryStage.setTitle("Select a File");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        
        if(selectedFile!=null){
//            fileText.setText((String)selectedFile.getCanonicalPath());
//            fileText.setText((String)selectedFile);

//            selectedFile.ge
        }
        
    }


    @FXML
    private void registerBtnHandler(ActionEvent event) {
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            if(userTextField.getText().equals("") || passPasswordField.getText().equals("")){
                this.mc.dialogue("Error", "Username or password cannot be empty!",Alert.AlertType.ERROR);
            }
            else{
                  if (passPasswordField.getText().equals(rePassPasswordField.getText())) {
                      if(!this.db.validateUser(userTextField.getText())){
                        this.db.addUserDataToDB(userTextField.getText(), passPasswordField.getText());
                        this.mc.dialogue("Adding information to the database", "Successful!",Alert.AlertType.INFORMATION);
//                        String[] credentials = {userTextField.getText(), passPasswordField.getText()};
                        String[] credentials={userTextField.getText()};
                        this.mc.redirectUser(credentials);

                      }
                      else{
                          this.mc.dialogue("Error", "User already exist.",Alert.AlertType.ERROR);
                          return;
                      }
                    
                } else {
                    this.mc.dialogue("Error", "password and confirm password did not match.",Alert.AlertType.ERROR);
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
