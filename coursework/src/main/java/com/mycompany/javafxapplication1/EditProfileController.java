/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.net.URL;
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
    
    @FXML
    private TextField usernameText;
    
    @FXML
    private Button cancelBtn;
    
    @FXML
    private Button saveBtn;
    
    private  void setUserPage(Stage stage){
        try {
            Stage editProfileStage = new Stage();
            
            Stage primaryStage = (Stage) stage;
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(getClass().getResource("user.fxml"));
            Parent root = loader.load();
            Scene scene =new Scene(root, 640,480);
            
            UserController controller = loader.getController();
            controller.initialise(this.username);
            
            editProfileStage.setScene(scene);
            editProfileStage.setTitle("User");
            editProfileStage.show();
            primaryStage.close();
            
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onChangePass(){}
    
    @FXML
    private void onCancel(){
        setUserPage((Stage) cancelBtn.getScene().getWindow());
    }
    
    @FXML
    private void onSave(){}

    public void initialise(String credentials) {
        this.username=credentials;
        usernameText.setText(this.username);

    }
    
}
