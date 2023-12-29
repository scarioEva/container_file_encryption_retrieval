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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    private String fileName = "newfile.txt";
    private String username;
    private MainController mc = new MainController();
    private DB db = new DB();
    private String userFileId;
    private String sharedFileId;

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

//    @FXML
//    private Label sharedFileName;
    @FXML
    private TableView sharedFileTableView;

    @FXML
    private ChoiceBox sharedFileSelect;

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

    @FXML
    private void onUserFileEdit() {
        Stage primaryStage = (Stage) userEditFlBtn.getScene().getWindow();
        String[] data = {this.username, this.userFileId, "false", "yes"};
        this.mc.redirectFile(data, "Edit file");
        primaryStage.close();
    }

    @FXML
    private void onSharedFileEdit() {
        Stage primaryStage = (Stage) userEditFlBtn.getScene().getWindow();
        String[] data = {this.username, this.sharedFileId, "false", "no"};
        this.mc.redirectFile(data, "Edit file");
        primaryStage.close();
    }

    @FXML
    private void onFileSelected() {
//        System.out.println("selected");
//        sharedFlEditBtn.setVisible(true);

    }

    public void getSharedFileValue(String value) {
        System.out.println(value);
        try {

            String fileName = value;

            ObservableList<FileData> fileData;

            fileData = db.getFileFromTable(fileName, "name");
            sharedFileId = fileData.get(0).getFileId();

            sharedFlEditBtn.setVisible(true);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getFileData() {
        ObservableList<FileData> data;
        ObservableList<ACL> aclData;
        ObservableList<FileData> sharedFileData;

        try {
            String userId = db.getUser(this.username, "name", "id");
            data = this.db.getFileFromTable(userId, "userId");
            aclData = this.db.getUserAcl(userId, "userId");

            if (!data.isEmpty()) {
                userFileName.setText(data.get(0).getFilaName() + ".txt");
                userDelFlBtn.setVisible(true);
                userEditFlBtn.setVisible(true);
                fileCreate.setVisible(false);
                this.userFileId = data.get(0).getFileId();
            } else {
                userFileName.setText("No file created");
                userDelFlBtn.setVisible(false);
                userEditFlBtn.setVisible(false);
                fileCreate.setVisible(true);
                this.userFileId = "";
            }

            if (!aclData.isEmpty()) {
                sharedFileData = this.db.getFileFromTable(aclData.get(0).getFileId(), "fileId");
//                sharedFlEditBtn.setVisible(true);
                this.sharedFileId = aclData.get(0).getFileId();
//                this.sharedFlEditBtn.setText(aclData.get(0).getWrite().equals("true") ? "Edit" : "View");

                if (!sharedFileData.isEmpty()) {
//                    sharedFileName.setText(sharedFileData.get(0).getFilaName()+".txt");
                }
            } else {
//                    sharedFileName.setText("No shared File");
//                sharedFlEditBtn.setVisible(false);
                this.sharedFileId = "";
            }

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

            TableColumn fileName = new TableColumn("File Name");
            fileName.setCellValueFactory(
                    new PropertyValueFactory<>("fileName"));

            TableColumn write = new TableColumn("Write");
            write.setCellValueFactory(
                    new PropertyValueFactory<>("write"));

            sharedFileTableView.setItems(aclList);
            sharedFileTableView.getColumns().addAll(fileName, write);

            if (!aclList.isEmpty()) {
                LinkedList<String> fileList = new LinkedList();

                for (int i = 0; i < aclList.size(); i++) {
                    fileList.add(aclList.get(i).getFileName());
                }

                sharedFileSelect.getItems().addAll(fileList);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void initialise(String credentials[]) {
        this.username = credentials[0];
        userLabel.setText(this.username);

        sharedFlEditBtn.setVisible(false);

        this.getFileData();
        this.getAclList();

        sharedFileSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                getSharedFileValue(sharedFileSelect.getItems().get((Integer) number2).toString());

//                System.out.println(sharedFileSelect.getItems().get((Integer) number2));
            }
        });
    }

}
