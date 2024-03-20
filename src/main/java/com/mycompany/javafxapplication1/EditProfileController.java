/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
    private MainController mc = new MainController();
    private DB db = new DB();
    private CommonClass commonClass = new CommonClass();
    private FileServers fileServers = new FileServers();

    @FXML
    private TextField usernameText;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private void onChangePass() {
        Stage primaryStage = (Stage) cancelBtn.getScene().getWindow();
        String[] data = {this.username};
        mc.redirectChangePass(data);
        primaryStage.close();
    }

    @FXML
    private void onCancel() {
        Stage primaryStage = (Stage) cancelBtn.getScene().getWindow();
        String[] data = {this.username};
        mc.redirectUser(data);
        primaryStage.close();
    }

    @FXML
    private void onSave() {
        try {
            if (!this.db.validateUser(usernameText.getText())) {
                if (this.mc.dialogue("Confirm", "Are you sure you want to update your username?", Alert.AlertType.CONFIRMATION).equals("OK")) {
                    if (this.db.updateUsername(this.userId, usernameText.getText())) {
                        this.username = usernameText.getText();
                        this.mc.dialogue("Success!", "Username updated successfully!", Alert.AlertType.INFORMATION);
                    }
                }
            } else {
                this.mc.dialogue("Error", "User already exist.", Alert.AlertType.ERROR);
            }
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Boolean fileDelete() {
        Boolean flag = false;
        try {
            ObservableList<FileData> data;
            data = db.getAllFilesFromTable(this.userId, "userId");

            if (!data.isEmpty()) {

                for (int i = 0; i < data.size(); i++) {

                    String fileId = data.get(i).getFileId();
                    String filePath = data.get(i).getPath();

                    //Check no. of file versions
                    ObservableList<FileVersions> versionList = this.db.getFileVersionList(fileId);
                    int no_of_versions = versionList.size();

                    //delete all versions files from server 
                    for (int j = 0; j < no_of_versions; j++) {
                        this.fileServers.deleteFiles(filePath + (j + 1));
                    }

                    //delete all file versions from database
                    db.deleteFileVersions(fileId);

                    //delete user ACL with file id on database
                    db.deleteACL("fileId", fileId);

                    db.deleteACL("userId", this.userId);

                    //delete encryption key for that file from database
                    db.deleteKey(fileId);

                    //delete file logs from database
                    db.deleteFileLogs(fileId);

                    //delete file metadata from database
                    db.deleteFileMetadata(fileId);

                }
            }
            flag = true;
        } catch (ClassNotFoundException ex) {
            flag = false;
            Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            flag = false;
            Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return flag;
    }

    @FXML
    private void onAccountDelete() {
        Stage primaryStage = (Stage) deleteBtn.getScene().getWindow();

        if (this.mc.dialogue("Confirm", "Are you sure you want to delete your Account?", Alert.AlertType.CONFIRMATION).equals("OK")) {
            try {
                //delete user file  and all data from server and database which are connected to current user
                if (fileDelete()) {
                    //delete user account
                    if (this.db.deleteUser(this.userId)) {
                        if (this.mc.dialogue("Success!", "Account deleated successfully!", Alert.AlertType.INFORMATION).equals("OK")) {
                            this.mc.redirectLogin();
                            primaryStage.close();
                        }
                    }
                } else {
                    this.mc.dialogue("Error!", "Something went wrong. Please try again.", Alert.AlertType.ERROR);
                }
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void initialise(String[] credentials) throws InvalidKeySpecException, ClassNotFoundException {
        this.username = credentials[0];
        usernameText.setText(this.username);
        this.userId = this.db.getUser(this.username, "name", "id");
    }
}
