/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class EditProfileController {
    private String username;
    private String userId;
    private MainController mc=new MainController();
    private DB db=new DB();
    
    @FXML
    private TextField usernameText;
    
    @FXML
    private Button cancelBtn;
    
    @FXML
    private Button saveBtn;
    
    private  void setUserPage(Stage stage){
        Stage primaryStage = (Stage) stage;
        mc.redirectUser(this.username);
        primaryStage.close();   
    }

    @FXML
    private void onChangePass(){}
    
    @FXML
    private void onCancel(){
        setUserPage((Stage) cancelBtn.getScene().getWindow());
    }
    
    @FXML
    private void onSave(){
        try {
            if(this.db.updateUsername(this.userId, usernameText.getText())){
                setUserPage((Stage) saveBtn.getScene().getWindow());
            }
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void initialise(String credentials) throws InvalidKeySpecException, ClassNotFoundException {
        this.username=credentials;
        usernameText.setText(this.username);
        
        this.userId=this.db.getUserId(this.username);

    }
    
}
