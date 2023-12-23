/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author ntu-user
 */
public class MainController {
    
    private void openView(int width, int height, String fileName, String title,String credentials){
        Stage secondaryStage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fileName));
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);
            secondaryStage.setScene(scene);
            if(!credentials.equals("")){
                if(fileName=="admin.fxml"){
                    AdminController controller = loader.getController();
                    controller.initialise(credentials);
                }
                else if(fileName=="user.fxml"){
                    UserController controller = loader.getController();
                    controller.initialise(credentials);
                }
                else if(fileName=="editProfile.fxml"){
                    EditProfileController controller = loader.getController();
                    controller.initialise(credentials);
                }
                
            }
            secondaryStage.setTitle(title);
            secondaryStage.show();
            
//          String msg="some data sent from Primary Controller";
//          secondaryStage.setUserData(msg);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void redirectLogin(){
        this.openView(640,480,"primary.fxml","Login","");
    }
    
    public void redirectRegister(){
        this.openView(640,480,"register.fxml","Register a new User","");
    }
    
    public void redirectAdmin(String credentials){
       this.openView(640,480,"admin.fxml","Admin", credentials);
    }
    
    public void redirectUser(String credentials){
        this.openView(640,480,"user.fxml","User",credentials);
    }
    
    public void redirectEditProfile(String credentials){
        this.openView(640,480,"editProfile.fxml","Edit Profile",credentials);
    }
}
