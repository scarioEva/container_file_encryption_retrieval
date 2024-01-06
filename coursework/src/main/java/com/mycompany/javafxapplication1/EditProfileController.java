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
import javafx.scene.control.Alert;
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
    
    @FXML
    private Button deleteBtn;
    
    private  void setUserPage(Stage stage){
        Stage primaryStage = (Stage) stage;
        String[] data= {this.username};
        mc.redirectUser(data);
        primaryStage.close();   
    }

    @FXML
    private void onChangePass(){
        Stage primaryStage = (Stage) cancelBtn.getScene().getWindow();
        String[] data= {this.username};
        mc.redirectChangePass(data);
        primaryStage.close();  
    }
    
    @FXML
    private void onCancel(){
        setUserPage((Stage) cancelBtn.getScene().getWindow());
    }
    
    @FXML
    private void onSave(){
        try {
            if(!this.db.validateUser(usernameText.getText())){
//                System.out.println(this.mc.dialogue("Confirm", "Are you sure you want to update username?", Alert.AlertType.CONFIRMATION)+"  confitm");
                if(this.mc.dialogue("Confirm", "Are you sure you want to update your username?", Alert.AlertType.CONFIRMATION).equals("OK")){
                    if(this.db.updateUsername(this.userId, usernameText.getText())){
                        this.username=usernameText.getText();
                        this.mc.dialogue("Success!", "Username updated successfully!", Alert.AlertType.INFORMATION);
                    }
                }
            }
            else{
                this.mc.dialogue("Error", "User already exist.",Alert.AlertType.ERROR);
            }
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    private void onAccountDelete(){
        Stage primaryStage = (Stage) deleteBtn.getScene().getWindow();
        
        if(this.mc.dialogue("Confirm", "Are you sure you want to delete your Account?", Alert.AlertType.CONFIRMATION).equals("OK")){
            try {
                if(this.db.deleteUser(this.userId)){
                    if(this.mc.dialogue("Success!", "Account deleated successfully!", Alert.AlertType.INFORMATION).equals("OK")){
                        this.mc.redirectLogin();
                        primaryStage.close();
                    }
                }
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void initialise(String[] credentials) throws InvalidKeySpecException, ClassNotFoundException {
        this.username=credentials[0];
        usernameText.setText(this.username);
        this.userId=this.db.getUser(this.username, "name","id");
    }
}
