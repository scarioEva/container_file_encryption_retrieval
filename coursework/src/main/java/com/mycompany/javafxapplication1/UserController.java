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
    private Label fileLabel;
    
    @FXML
    private Button logoutBtn;
    
    @FXML
    private TextArea fileTextArea;
    
    @FXML
    private Button saveFile;
    
    @FXML
    private Button editProfileBtn;
    
    private void dialogue(String headerMsg, String contentMsg,Alert.AlertType alertType) {
        Stage secondaryStage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == Alert.AlertType.CONFIRMATION? "Confirmation Dialog":"Error Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);
        Optional<ButtonType> result = alert.showAndWait();
    }
    
    @FXML
    private void fileOnSave(){
        try{
            String str = fileTextArea.getText();
            FileWriter writer = new FileWriter(fileName);
            writer.write(str);
            
            writer.close();
            
            dialogue("Success!", "File updated successfully!", Alert.AlertType.CONFIRMATION);
        }
        catch(IOException ie){
            
        }
    }
    
    private void getFileData(){
        try{
            File file=new File(fileName);
            fileLabel.setText(file.getName());

            FileReader fileReader =new FileReader(fileName);
            var bufferReader = new BufferedReader(fileReader);
            
            String fileData=null;
            
            while((fileData= bufferReader.readLine())!=null){
                fileTextArea.setText(fileData);
            }
            
        }
        catch(FileNotFoundException fe){
           
        }
        catch(IOException ie){
            
        }
        
    }
    
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
        this.mc.redirectEditProfile(this.username);
        primaryStage.close();
    }
    
    public void initialise(String credentials) {
        this.username=credentials;
        userLabel.setText(this.username);
        getFileData();
    }
    
}
