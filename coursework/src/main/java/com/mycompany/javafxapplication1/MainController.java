/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author ntu-user
 */
public class MainController {

    private String[] emptyArray = {};

    private void openView(int width, int height, String fileName, String title, String[] data) {
        Stage secondaryStage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fileName));
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);
            secondaryStage.setScene(scene);
//            if(!data.equals("")){
            if (fileName == "primary.fxml") {
                PrimaryController controller = loader.getController();
                controller.initialise();
            }
            if (fileName == "admin.fxml") {
                AdminController controller = loader.getController();
                controller.initialise(data);
            } else if (fileName == "user.fxml") {
                UserController controller = loader.getController();
                controller.initialise(data);
            } else if (fileName == "editProfile.fxml") {
                EditProfileController controller = loader.getController();
                controller.initialise(data);
            } else if (fileName == "file.fxml") {
                FileController controller = loader.getController();
                controller.initialise(data);
            } else if (fileName == "fileRestore.fxml") {
                FileRestoreController controller = loader.getController();
                controller.initialise(data);
            } else if (fileName == "logs.fxml") {
                LogsController controller = loader.getController();
                controller.initialise(data);
            }

//            }
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

    public String dialogue(String headerMsg, String contentMsg, Alert.AlertType alertType) {
        Stage secondaryStage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);
        Alert alert = new Alert(alertType);
//        alert.setTitle(alertType == Alert.AlertType.CONFIRMATION? "Confirmation Dialog":"Error Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get().getText();
    }

    public void redirectLogin() {
        this.openView(555, 177, "primary.fxml", "Login", this.emptyArray);
    }

    public void redirectRegister() {
        this.openView(600, 300, "register.fxml", "Register a new User", this.emptyArray);
    }

    public void redirectAdmin(String[] credentials) {
        this.openView(640, 480, "admin.fxml", "Admin", credentials);
    }

    public void redirectUser(String[] credentials) {
        this.openView(640, 780, "user.fxml", "User", credentials);
    }

    public void redirectEditProfile(String[] credentials) {
        this.openView(640, 480, "editProfile.fxml", "Edit Profile", credentials);
    }

    public void redirectFile(String[] data, String title) {
        this.openView(640, 680, "file.fxml", title, data);
    }

    public void redirectFileRestore(String[] data) {
        this.openView(954, 653, "fileRestore.fxml", "Restore file", data);
    }

    public void redirectFileLog(String[] data) {
        this.openView(1102, 400, "logs.fxml", "File log", data);
    }
}
