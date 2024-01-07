/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class ChangepassController {

    private String username;
    MainController mc = new MainController();
    DB db = new DB();

    @FXML
    private PasswordField currentPass;
    @FXML
    private PasswordField newPass;
    @FXML
    private PasswordField confirmNewPass;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelBtn;

    @FXML
    private void onSave() {
        Stage primaryStage = (Stage) saveButton.getScene().getWindow();
        String newPassword = newPass.getText();
        try {
            if (!newPassword.equals("") && !confirmNewPass.getText().equals("") && !currentPass.getText().equals("")) {

                if (db.validateUser(this.username, currentPass.getText())) {
                    if (newPassword.equals(confirmNewPass.getText())) {
                        if (newPassword.length() >= 8) {
                            if (!newPassword.equals(currentPass.getText())) {

                                db.updatePassword(newPassword, this.username);
                                if (mc.dialogue("Success", "Password updated successfully", Alert.AlertType.INFORMATION).equals("OK")) {
                                    String[] data = {this.username};
                                    mc.redirectEditProfile(data);
                                    primaryStage.close();
                                }
                            } else {
                                mc.dialogue("Error", "New Password should not match the old password", Alert.AlertType.ERROR);

                            }

                        } else {
                            mc.dialogue("Error", "Password much contain atlest 8 characters", Alert.AlertType.ERROR);

                        }

                    } else {
                        mc.dialogue("Error", "New Password and Confirm Password did not match", Alert.AlertType.ERROR);

                    }
                } else {
                    mc.dialogue("Error", "Invalid Current Password", Alert.AlertType.ERROR);
                }
            } else {
                mc.dialogue("Error", "Enter all the required fields", Alert.AlertType.ERROR);

            }
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ChangepassController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChangepassController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void onCancel() {
        Stage primaryStage = (Stage) cancelBtn.getScene().getWindow();
        String[] data = {this.username};
        mc.redirectEditProfile(data);
        primaryStage.close();

    }

    public void initialise(String[] data) {
        this.username = data[0];
        System.out.println(this.username);
    }

}    

