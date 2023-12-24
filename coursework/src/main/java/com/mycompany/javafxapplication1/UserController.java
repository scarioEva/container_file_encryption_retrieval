/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class UserController {
    private String fileName="newfile.txt";
    private String username;
    private MainController mc=new MainController();
    private DB db=new DB();
    

    @FXML
    private Label userLabel;
       
    @FXML
    private Button logoutBtn;
    
    @FXML
    private Button editProfileBtn;
    
    @FXML
    private Button fileCreate;
    
    @FXML
    private Button userEditFlBtn;
    
    @FXML
    private Button userDelFlBtn;
    
    @FXML
    private Button sharedFlEditBtn;
    
    @FXML
    private Label userFileName;
    
    @FXML
    private Label sharedFileName;
    
    @FXML
    private void handleLogout(){
        Stage primaryStage = (Stage) logoutBtn.getScene().getWindow();
        DB myObj = new DB();
        ObservableList<User> data;
        try {
            
            data=myObj.getActiveUser();
            
            if(myObj.setUserActive(data.get(0).getUser(), false)){
                this.mc.redirectLogin();
                primaryStage.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onEditProfile(){
        Stage primaryStage = (Stage) editProfileBtn.getScene().getWindow();
        String[] data={this.username};
        this.mc.redirectEditProfile(data);
        primaryStage.close();
    }
    
    @FXML
    private void onFileCreate(){
        Stage primaryStage = (Stage) fileCreate.getScene().getWindow();
        String[] data={this.username};
        this.mc.redirectFile(data, "Create File");
        primaryStage.close();
    }
    
    @FXML
    private void onUserFlDel(){
        
    }
    
    @FXML
    private void onUserFileEdit(){
        
    }
    
    @FXML
    private void onSharedFileEdit(){
        
    }
    
    private void getFileData(){
        ObservableList<FileData> data;
        
        try {
            String userId= db.getUserId(this.username);
            data = this.db.getFileFromTable(userId);
            System.out.println(data.size());
            
            if(!data.isEmpty()){
                userFileName.setText(data.get(0).getFilaName()+".txt");
                userDelFlBtn.setVisible(true);
                userEditFlBtn.setVisible(true);
                fileCreate.setVisible(false);
            }
            else{
                userFileName.setText("No file created");
                userDelFlBtn.setVisible(false);
                userEditFlBtn.setVisible(false);
                fileCreate.setVisible(true);
            }
            
                
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initialise(String credentials[]) {
        this.username=credentials[0];
        userLabel.setText(this.username);
        
        
//        userEditFlBtn.setVisible(false);
        getFileData();
    }
    
}
