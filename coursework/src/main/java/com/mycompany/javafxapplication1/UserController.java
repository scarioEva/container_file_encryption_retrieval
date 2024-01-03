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
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class UserController {

    private String fileName = "newfile.txt";
    private String username;
    private MainController mc = new MainController();
    private DB db = new DB();
    private String userFileId;
    private String sharedFileId;
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
    private void selectBtnHandler(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) selectBtn.getScene().getWindow();
        primaryStage.setTitle("Select a File");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
//            System.out.println();
            String selectedFileName = selectedFile.getName();

            FileReader fileReader = new FileReader(selectedFile);
            var bufferReader = new BufferedReader(fileReader);

            String content = "";

            String fileData;

            while ((fileData = bufferReader.readLine()) != null) {
                content = fileData;
            }
            fileReader.close();

            System.out.println("content:" + content);

            if (fileManage.createNewFile(this.username, selectedFileName.substring(0, selectedFileName.length() - 4), content)) {

                if (this.mc.dialogue("Success", "File uploaded successfully", Alert.AlertType.CONFIRMATION).equals("OK")) {
                    String[] data = {this.username};
                    mc.redirectUser(data);
                    primaryStage.close();
                }
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
        String[] data = {this.username, "", "true", "yes"};
        this.mc.redirectFile(data, "Create File");
        primaryStage.close();
    }

    @FXML
    private void onUserFlDel() {

    }

    private void onUserFileEdit(String fileId) {
        Stage primaryStage = (Stage) fileCreate.getScene().getWindow();
        String[] data = {this.username, fileId, "false", "yes"};
        this.mc.redirectFile(data, "Edit file");
        primaryStage.close();
    }

    private void onSharedFileEdit(String fileId) {
        Stage primaryStage = (Stage) fileCreate.getScene().getWindow();
        String[] data = {this.username, fileId, "false", "no"};
        this.mc.redirectFile(data, "Edit file");
        primaryStage.close();
    }


//    public void getSharedFileValue(String value) {
//        System.out.println(value);
//        try {
//
//            String fileName = value;
//
//            ObservableList<FileData> fileData;
//
//            fileData = db.getFileFromTable(fileName, "name");
//            sharedFileId = fileData.get(0).getFileId();
//
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvalidKeySpecException ex) {
//            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    private void getFileData() {
        ObservableList<FileData> data;

        try {
            String userId = db.getUser(this.username, "name", "id");
            data = this.db.getFileFromTable(userId, "userId");
//            aclData = this.db.getUserAcl(userId, "userId");

           TableColumn fileName = new TableColumn("File Name");
            fileName.setCellValueFactory(
                    new PropertyValueFactory<>("fileName"));
            
            TableColumn edit = new TableColumn("Action");
            
            Callback<TableColumn<FileData, String>, TableCell<FileData, String>> cellFactory
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


            edit.setCellFactory(cellFactory);
            userFileTable.setItems(data);
            userFileTable.getColumns().addAll(fileName, edit);
            
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
            aclList = this.db.getAclFileList(userId);
//            this.button=new Button[aclList.size];

            TableColumn fileName = new TableColumn("File Name");
            fileName.setCellValueFactory(
                    new PropertyValueFactory<>("fileName"));

            TableColumn write = new TableColumn("Write");
            write.setCellValueFactory(
                    new PropertyValueFactory<>("write"));


            TableColumn action = new TableColumn("Action");

            
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
                                final Button btn = new Button(aclData.getWrite().equals("true") ? "Edit" : "View");

                                btn.setOnAction(event -> {
                                    onSharedFileEdit(aclData.getFileId());
                                    System.out.println(aclData.getFileId());
                                });
                                setGraphic(btn);
                                setText(null);
                            }
                        }
                    };
                    return cell;
                }
            };

            action.setCellFactory(cellFactory);
            sharedFileTableView.setItems(aclList);
            sharedFileTableView.getColumns().addAll(fileName, write, action);


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void initialise(String credentials[]) {
        this.username = credentials[0];
        userLabel.setText(this.username);

        this.getFileData();
        this.getAclList();

    }

}
