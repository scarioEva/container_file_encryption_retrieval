package com.mycompany.javafxapplication1;

import java.security.spec.InvalidKeySpecException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PrimaryController {

    private MainController mainController = new MainController();
    private DB db = new DB();
    private CommonClass commonClass = new CommonClass();
    private FileServers fileServers = new FileServers();

    @FXML
    private Button registerBtn;

    @FXML
    private TextField userTextField;

    @FXML
    private PasswordField passPasswordField;

    @FXML
    private void registerBtnHandler() {
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            //redirect to register page
            this.mainController.redirectRegister();
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void userLogin(String[] credentials) {
        this.mainController.redirectUser(credentials);
    }

    private void adminLogin(String[] credentials) {
        this.mainController.redirectAdmin(credentials);
    }

    @FXML
    private void handleLogin() {

        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            DB myObj = new DB();
            String[] credentials = {userTextField.getText()};
            //validate username and passwrod from database
            if (myObj.validateUser(userTextField.getText(), passPasswordField.getText())) {

                if (!userTextField.getText().equals("admin")) {
                    userLogin(credentials);
                } else {
                    adminLogin(credentials);
                }

                primaryStage.close();
            } else {
                this.mainController.dialogue("Invalid User Name / Password", "Please try again!", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteInactiveFiles() {
        ObservableList<FileData> data;

        try {
            //get user deleted inactive filed from database
            data = db.getInactiveFile();

            if (!data.isEmpty()) {

                for (int i = 0; i < data.size(); i++) {

                    //check deleted filed date is expiry (ie. exceeds 31 days)
                    if (commonClass.checkFileExpiry(data.get(i).getDateDeleted())) {
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

                        //delete encryption key for that file from database
                        db.deleteKey(fileId);

                        //delete file logs from database
                        db.deleteFileLogs(fileId);

                        //delete file metadata from database
                        db.deleteFileMetadata(fileId);

                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void initialise() {
        this.deleteInactiveFiles();
    }
}
