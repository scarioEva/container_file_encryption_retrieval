/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class UserController {

    private String username;
    private MainController mc = new MainController();
    private DB db = new DB();
    private FileManagment fileManage = new FileManagment();

    @FXML
    private Label userLabel;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button editProfileBtn;

    @FXML
    private Button fileCreate;

    @FXML
    private TableView sharedFileTableView;

    @FXML
    private Button selectBtn;

    @FXML
    private Button fileRestoreBtn;

    @FXML
    private TableView userFileTable;

    @FXML
    private void selectBtnHandler() throws IOException {
        Stage primaryStage = (Stage) selectBtn.getScene().getWindow();
        primaryStage.setTitle("Select a File");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            
            String selectedFileName = selectedFile.getName();
            
            //get file from selected path
            FileReader fileReader = new FileReader(selectedFile);
            var bufferReader = new BufferedReader(fileReader);

            String content = "";

            String fileData;
            //read the file
            while ((fileData = bufferReader.readLine()) != null) {
                 content += fileData + System.getProperty("line.separator");
                
            }
            fileReader.close();

            //create the new file with new filename and move the content to new file
            if (fileManage.createNewFile(this.username, selectedFileName.substring(0, selectedFileName.length() - 4), content)) {
                    String[] data = {this.username};
                    mc.redirectUser(data);
                    primaryStage.close();
            }
        }

    }

    @FXML
    private void onFileRestore() {
        Stage primaryStage = (Stage) fileRestoreBtn.getScene().getWindow();
        String[] data = {this.username};
        this.mc.redirectFileRestore(data);
        primaryStage.close();
    }

    @FXML
    private void handleLogout() {
        Stage primaryStage = (Stage) logoutBtn.getScene().getWindow();
        DB myObj = new DB();
        ObservableList<User> data;
        try {

            data = myObj.getActiveUser();
            //set current user inactive on user table
            if (myObj.setUserActive(data.get(0).getUser(), false)) {
                this.mc.redirectLogin();
                primaryStage.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onEditProfile() {
        Stage primaryStage = (Stage) editProfileBtn.getScene().getWindow();
        String[] data = {this.username};
        this.mc.redirectEditProfile(data);
        primaryStage.close();
    }

    @FXML
    private void onFileCreate() {
        Stage primaryStage = (Stage) fileCreate.getScene().getWindow();
        
        //pass data with {username, fileId, createMode, isUserFile}
        String[] data = {this.username, "", "true", "yes"};
        this.mc.redirectFile(data, "Create File");
        primaryStage.close();
    }


    private void onUserFileEdit(String fileId) {
        Stage primaryStage = (Stage) fileCreate.getScene().getWindow();
        
        //pass data with {username, fileId, createMode, isUserFile}
        String[] data = {this.username, fileId, "false", "yes"};
        this.mc.redirectFile(data, "Edit file");
        primaryStage.close();
    }

    private void onSharedFileEdit(String fileId) {
        Stage primaryStage = (Stage) fileCreate.getScene().getWindow();
        
        //pass data with {username, fileId, createMode, isUserFile}
        String[] data = {this.username, fileId, "false", "no"};
        this.mc.redirectFile(data, "Edit file");
        primaryStage.close();
    }

    private void getFileData() {
        ObservableList<FileData> data;

        try {
            String userId = db.getUser(this.username, "name", "id");
            
            //getting user active files from the file metadata table
            data = this.db.getFileFromTable(userId, "userId");

            TableColumn fileName = new TableColumn("File Name");
            fileName.setCellValueFactory(
                    new PropertyValueFactory<>("fileName"));
            
            TableColumn date_created = new TableColumn("Date created ");
            date_created.setCellValueFactory(
                    new PropertyValueFactory<>("createdAt"));
            
            TableColumn last_modified = new TableColumn("Last modified");
            last_modified.setCellValueFactory(
                    new PropertyValueFactory<>("lastModified"));

            TableColumn edit = new TableColumn("Action");

            //Creating edit button for edit action column
            Callback<TableColumn<FileData, String>, TableCell<FileData, String>> editCellFactory
                    = new Callback<TableColumn<FileData, String>, TableCell<FileData, String>>() {
                @Override
                public TableCell call(final TableColumn<FileData, String> param) {
                    final TableCell<FileData, String> cell = new TableCell<FileData, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                FileData fileData = getTableView().getItems().get(getIndex());
                                final Button btn = new Button("Edit");

                                btn.setOnAction(event -> {
                                    //call button fuction
                                    onUserFileEdit(fileData.getFileId());
                                });
                                setGraphic(btn);
                                setText(null);
                            }
                        }
                    };
                    return cell;
                }
            };

            //add edit button on column edit action
            edit.setCellFactory(editCellFactory);

            TableColumn delete = new TableColumn("Action");

            //Creating delete button for delete action column
            Callback<TableColumn<FileData, String>, TableCell<FileData, String>> delCellFactory
                    = new Callback<TableColumn<FileData, String>, TableCell<FileData, String>>() {
                @Override
                public TableCell call(final TableColumn<FileData, String> param) {
                    final TableCell<FileData, String> cell = new TableCell<FileData, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                FileData fileData = getTableView().getItems().get(getIndex());
                                final Button btn = new Button("Delete");

                                btn.setOnAction(event -> {
                                    //call delete file function from FileManagment.java
                                    fileManage.deleteFile(fileData.getFileId(), fileData.getFilaName(), (Stage) fileCreate.getScene().getWindow(), username);
                                });
                                setGraphic(btn);
                                setText(null);
                            }
                        }
                    };
                    return cell;
                }
            };
            
            //add delete button on column delete action
            delete.setCellFactory(delCellFactory);

            userFileTable.setItems(data);
            userFileTable.getColumns().addAll(fileName,date_created, last_modified, edit, delete);

        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getAclList() {
        try {
            ObservableList<AclList> aclList;

            String userId = this.db.getUser(this.username, "name", "id");
            //get ACL list with current user from ACL list table
            aclList = this.db.getAclFileList(userId);

            TableColumn fileName = new TableColumn("File Name");
            fileName.setCellValueFactory(
                    new PropertyValueFactory<>("fileName"));
            
             TableColumn owner = new TableColumn("Owner");
            owner.setCellValueFactory(
                    new PropertyValueFactory<>("owner"));

            TableColumn write = new TableColumn("Write");
            write.setCellValueFactory(
                    new PropertyValueFactory<>("write"));

            TableColumn action = new TableColumn("Action");

            //create button to add to column action
            Callback<TableColumn<AclList, String>, TableCell<AclList, String>> cellFactory
                    = new Callback<TableColumn<AclList, String>, TableCell<AclList, String>>() {
                @Override
                public TableCell call(final TableColumn<AclList, String> param) {
                    final TableCell<AclList, String> cell = new TableCell<AclList, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                AclList aclData = getTableView().getItems().get(getIndex());
                                //set the name of the button based on user permission (if write==true the "edit" else "View")
                                final Button btn = new Button(aclData.getWrite().equals("true") ? "Edit" : "View");

                                btn.setOnAction(event -> {
                                    //call button function
                                    onSharedFileEdit(aclData.getFileId());
                                });
                                setGraphic(btn);
                                setText(null);
                            }
                        }
                    };
                    return cell;
                }
            };
            
            //add button to action column
            action.setCellFactory(cellFactory);
            sharedFileTableView.setItems(aclList);
            sharedFileTableView.getColumns().addAll(fileName,owner, action);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initialise(String credentials[]) {
        this.username = credentials[0];
        userLabel.setText(this.username);
        
        //get user file list
        this.getFileData();
        
        //get files shared by others list
        this.getAclList();

    }

}
