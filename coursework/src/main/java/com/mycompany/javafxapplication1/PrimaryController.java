package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class PrimaryController {
    private MainController mc=new MainController();
    private DB db=new DB();
    
    @FXML
    private Button registerBtn;

    @FXML
    private TextField userTextField;

    @FXML
    private PasswordField passPasswordField;

    @FXML
    private void registerBtnHandler(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            this.mc.redirectRegister();
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    
    
    private void userLogin(String credentials){
        this.mc.redirectUser(credentials);
    }
    
    private void adminLogin(String credentials){
        this.mc.redirectAdmin(credentials);
    }

    @FXML
    private void handleLogin() {
        
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            DB myObj = new DB();
//            String[] credentials = {userTextField.getText(), passPasswordField.getText()};
            String credentials=userTextField.getText();
            if(myObj.validateUser(userTextField.getText(), passPasswordField.getText())){
                
                myObj.setUserActive(userTextField.getText(), true);
                
                if(!userTextField.getText().equals("admin"))
                    userLogin(credentials);
                else
                    adminLogin(credentials);
               
                primaryStage.close();
            }
            else{
                this.mc.dialogue("Invalid User Name / Password","Please try again!",Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
