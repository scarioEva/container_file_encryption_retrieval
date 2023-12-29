/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class FileController {

    private String username;
    private MainController mc = new MainController();
    private FileManagment fileManage = new FileManagment();
    private Boolean createMode = true;
    private Boolean userFile = true;
    private DB db = new DB();
    private String fileId;
    private String filePath;
    private ACLManagment acl = new ACLManagment();
    private FileServers fileservers = new FileServers();
    private FileChunk fileChunk = new FileChunk();
    private CommonClass commonClass = new CommonClass();

    @FXML
    private TextArea fileTextArea;

    @FXML
    private TextField fileNameId;

    @FXML
    private Button fileSaveId;

    @FXML
    private Button fileCancelId;

    @FXML
    private Button fileDelId;

    @FXML
    private Label ownerNameId;

    //testing
    @FXML
    private GridPane test;

    @FXML
    private GridPane shareContainer;

    @FXML
    private ChoiceBox userSelect;

    @FXML
    private CheckBox writeCheckbox;

    @FXML
    private CheckBox shareFileCheckBox;

    @FXML
    private void onShareFIleChecked() {
        if (shareFileCheckBox.isSelected()) {
            shareContainer.setVisible(true);
        } else {
            shareContainer.setVisible(false);
            userSelect.setValue(null);
        }
    }

    public void setUsersPermissions(String currentFileId) {
        if (userFile) {

            try {
                String userId = this.db.getUser(userSelect.getValue().toString(), "name", "id");
                acl.addACL(userId, currentFileId, writeCheckbox.isSelected());

            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void onCreateFile(String fileName, String fileContent) throws InvalidKeySpecException, ClassNotFoundException {
        Stage primaryStage = (Stage) fileSaveId.getScene().getWindow();
        if (fileManage.createNewFile(this.username, fileName, fileContent)) {

            // get filed id of the newly created file and psss to setUserPermission() to add ACL data
            if (shareFileCheckBox.isSelected()) {
                String userId = this.db.getUser(this.username, "name", "id");
                ObservableList<FileData> fileData;
                fileData = this.db.getFileFromTable(userId, "userId");
                this.setUsersPermissions(fileData.get(0).getFileId());
            }

            String[] data = {this.username};
            mc.redirectUser(data);
            primaryStage.close();
        }
    }

    private Boolean checkUserAclExists() throws ClassNotFoundException, InvalidKeySpecException {
        Boolean flag = false;
        ObservableList<ACL> data;
        data = this.db.getUserAcl(this.fileId, "fileId");

        if (!data.isEmpty()) {
            flag = true;
        }
        return flag;
    }

    @FXML
    private void onSave() throws InvalidKeySpecException, ClassNotFoundException {
//        this.setUsersPermissions();

        String fileName = fileNameId.getText();
        String fileContent = fileTextArea.getText();
        if (createMode) {
            if (!fileName.equals("")) {
                if (shareFileCheckBox.isSelected()) {
                    if (userSelect.getValue() != null) {
                        this.onCreateFile(fileName, fileContent);
                    } else {
                        this.mc.dialogue("Error", "Please select user to share the file", Alert.AlertType.ERROR);
                    }
                } else {
                    this.onCreateFile(fileName, fileContent);
                }

            } else {
                this.mc.dialogue("Error", "Please enter name of the file", Alert.AlertType.ERROR);
            }
        } else {

            if (this.userFile) {
                if (!shareFileCheckBox.isSelected()) {
                    this.acl.deleteACL(this.fileId);
                    this.fileManage.updatFile(this.fileId, fileName, this.filePath, fileContent);
                } else {

                    if (userSelect.getValue() != null) {
                        if (checkUserAclExists()) {
                            String new_userId = this.db.getUser(userSelect.getValue().toString(), "name", "id");
                            this.acl.updateACL(new_userId, this.fileId, writeCheckbox.isSelected());
                        } else {
                            this.setUsersPermissions(this.fileId);
                        }

                        this.fileManage.updatFile(this.fileId, fileName, this.filePath, fileContent);
                    } else {
                        this.mc.dialogue("Error", "Please select user to share file", Alert.AlertType.ERROR);
                    }
                }
            } else {
                this.fileManage.updatFile(this.fileId, fileName, this.filePath, fileContent);
            }

        }

    }

    @FXML
    private void onCancel() {
        Stage primaryStage = (Stage) fileCancelId.getScene().getWindow();
        String[] data = {this.username};
        this.mc.redirectUser(data);
        primaryStage.close();
    }

    @FXML
    private void onDel() {

    }

    private void getFileContent(String fileName) {
        String remoteFile;
            if (fileChunk.merge(fileName, fileservers.downloadEncryptedFile(fileName), this.fileId)) {
                remoteFile = commonClass.localDirectory + fileName+".txt";

                try {
                    //update to read conainer

                    FileReader fileReader = new FileReader(remoteFile);
                    var bufferReader = new BufferedReader(fileReader);

                    String fileData;

                    while ((fileData = bufferReader.readLine()) != null) {
                        fileTextArea.setText(fileData);
                    }
                    fileReader.close();
                    
                    //delete tem file after sent to TextField
                    File fl=new File(remoteFile);
                    fl.delete();

                } catch (FileNotFoundException fe) {

                } catch (IOException ie) {

                }
        }

    }

    private void getFileData(String[] fileData) {
        this.fileId = fileData[1];
        this.createMode = !fileData[2].equals("false");
        this.userFile = fileData[3].equals("yes");

        ObservableList<FileData> data;
        try {
            if (!this.createMode) {
                data = this.db.getFileFromTable(this.fileId, "fileId");
                if (!data.isEmpty()) {
                    fileNameId.setText(data.get(0).getFilaName());
                    this.filePath = data.get(0).getPath();

                    this.getFileContent(this.filePath);

                    String ownerName = this.db.getUser(data.get(0).getUserId(), "id", "name");

                    ownerNameId.setText(ownerName);
                }

            } else {
                ownerNameId.setText(this.username);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void getUserData() {
        try {
            ObservableList<User> data;
            data = this.db.getUserList();
            if (!data.isEmpty()) {

                LinkedList<String> userData = new LinkedList();

                for (int i = 0; i < data.size(); i++) {
                    String user = data.get(i).getUser();
                    if (!user.equals(this.username) && !user.equals("admin")) {
                        userData.add(user);
                    }
                }

//                ((ChoiceBox)userSelect).setItems(userData);
                userSelect.getItems().addAll(userData);

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getACLdata() {
        try {
            ObservableList<ACL> data;

            data = this.db.getUserAcl(this.fileId, "fileId");
            if (!data.isEmpty()) {
                if (userFile) {
                    String shared_username = this.db.getUser(data.get(0).getUserId(), "id", "name");
                    if (!shared_username.equals("")) {
                        shareContainer.setVisible(true);
                        shareFileCheckBox.setSelected(true);
                        writeCheckbox.setSelected(data.get(0).getWrite().equals("true"));
                        userSelect.setValue(shared_username);
                    } else {
                        shareContainer.setVisible(false);
                        shareFileCheckBox.setSelected(false);
                        writeCheckbox.setSelected(false);
                        userSelect.setValue(null);
                        this.acl.deleteACL(this.fileId);

                    }
                } else {
                    if (data.get(0).getWrite().equals("false")) {
                        fileSaveId.setVisible(false);
                        fileDelId.setVisible(false);
                        fileTextArea.setDisable(true);
                        fileNameId.setDisable(true);
                    }
                }

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initialise(String[] data) {
        shareContainer.setVisible(false);
        this.username = data[0];
        this.createMode = data[2].equals("true");
        this.userFile = data[3].equals("yes");

        this.getFileData(data);
        this.getUserData();

        if (!createMode) {
            this.getACLdata();
        }

        if (!userFile) {
            shareContainer.setVisible(false);
            shareFileCheckBox.setVisible(false);
        }
    }

}
