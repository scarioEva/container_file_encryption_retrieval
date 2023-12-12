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
        DB myObj = new DB();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Register a new User");
            secondaryStage.show();
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void dialogue(String headerMsg, String contentMsg) {
        Stage secondaryStage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);

        Optional<ButtonType> result = alert.showAndWait();
    }
    
    
    private void userLogin(String credentials){
        Stage secondaryStage = new Stage();
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("user.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            
            UserController controller = loader.getController();
            controller.initialise(credentials);
 
            secondaryStage.setTitle("User");
            String msg="some data sent from Primary Controller";
            secondaryStage.setUserData(msg);
            secondaryStage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private void adminLogin(String credentials){
        Stage secondaryStage = new Stage();
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("admin.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            
            AdminController controller = loader.getController();
            controller.initialise(credentials);
 
            secondaryStage.setTitle("Show Users");
            String msg="some data sent from Primary Controller";
            secondaryStage.setUserData(msg);
            secondaryStage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
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
                dialogue("Invalid User Name / Password","Please try again!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
