/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class FileRestoreController {

    private DB db = new DB();
    private String username;
    private MainController mainController = new MainController();
    private String filePath;
    private String oldFileName;
    private String fileId;
    private FileManagment fileManage = new FileManagment();
    private int selectedVersion;
    private String selectedFileName;
    private String selectedDate;
    private CommonClass commonClass = new CommonClass();
    private long fileSize;
    private FileServers fileServer = new FileServers();
    private int no_of_versions;

    @FXML
    private TableView fileTable;

    @FXML
    private TableView versionTable;

    @FXML
    private Label fileNameText;

    @FXML
    private TextArea fileContent;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private void onCancel() {
        Stage primaryStage = (Stage) cancelBtn.getScene().getWindow();
        String[] data = {this.username};
        this.mainController.redirectUser(data);
        primaryStage.close();
    }

    private Boolean deleteRemoteFiles() {
        System.out.println(this.selectedVersion + ", " + this.no_of_versions);
        for (int i = this.selectedVersion; i < this.no_of_versions; i++) {
            System.out.println(i + 1);
            this.fileServer.deleteFiles(this.filePath + (i + 1));
        }

        return true;
    }

    @FXML
    private void onSave() {
        Stage primaryStage = (Stage) saveBtn.getScene().getWindow();
        try {
            if (this.deleteRemoteFiles()) {
                db.updateFileData(this.fileId, this.selectedFileName, this.fileSize + "bytes", this.selectedVersion, this.selectedDate, true);
                db.addLogToDb(this.fileId, "You restored or rolebacked to \""+this.selectedDate+"\" with the file name: "+this.selectedFileName);
                db.deleteFileVersions(this.fileId, this.selectedVersion);

                if (this.mainController.dialogue("Success!", "File Restored Successfully!", Alert.AlertType.CONFIRMATION).equals("OK")) {
                    String[] data = {this.username};
                    this.mainController.redirectUser(data);
                    primaryStage.close();
                }

            }
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(FileRestoreController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileRestoreController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void getFileList() {
        try {
            String userId = db.getUser(this.username, "name", "id");

            ObservableList<FileRestore> fileList = this.db.getUserFiles(userId);

            TableColumn fileName = new TableColumn("File Name");
            fileName.setCellValueFactory(
                    new PropertyValueFactory<>("fileName"));

            TableColumn createdAt = new TableColumn("Created at");
            createdAt.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

            TableColumn action = new TableColumn("Action");

            Callback<TableColumn<FileRestore, String>, TableCell<FileRestore, String>> cellFactory
                    = new Callback<TableColumn<FileRestore, String>, TableCell<FileRestore, String>>() {
                @Override
                public TableCell call(final TableColumn<FileRestore, String> param) {
                    final TableCell<FileRestore, String> cell = new TableCell<FileRestore, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                FileRestore fileData = getTableView().getItems().get(getIndex());
                                final Button btn = new Button("select");

                                btn.setOnAction(event -> {
                                    fileContent.setVisible(false);
                                    saveBtn.setVisible(false);

                                    fileId = fileData.getFileId();
                                    oldFileName = fileData.getFileName();
                                    versionTable.setVisible(true);
                                    filePath = fileData.getFilePath();
                                    getFileVersions(fileData.getFileId());
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
            fileTable.setItems(fileList);
            fileTable.getColumns().addAll(fileName, createdAt, action);

        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(FileRestoreController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileRestoreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getFileVersions(String fileId) {
        versionTable.getColumns().clear();
        try {
            ObservableList<FileVersions> versionList = this.db.getFileVersionList(fileId);

            this.no_of_versions = versionList.size();

            TableColumn date = new TableColumn("Updated date");
            date.setCellValueFactory(
                    new PropertyValueFactory<>("date"));

            TableColumn action = new TableColumn("Action");

            Callback<TableColumn<FileVersions, String>, TableCell<FileVersions, String>> cellFactory
                    = new Callback<TableColumn<FileVersions, String>, TableCell<FileVersions, String>>() {
                @Override
                public TableCell call(final TableColumn<FileVersions, String> param) {
                    final TableCell<FileVersions, String> cell = new TableCell<FileVersions, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                FileVersions fileData = getTableView().getItems().get(getIndex());
                                final Button btn = new Button("select");

                                btn.setOnAction(event -> {
                                    fileNameText.setText(fileData.getFilaName() + ".txt <--- " + oldFileName+".txt");
                                    showContent(fileData.getFilaVersion());
                                    selectedVersion = fileData.getFilaVersion();
                                    selectedFileName = fileData.getFilaName();
                                    selectedDate = fileData.getDate();
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
            versionTable.setItems(versionList);
            versionTable.getColumns().addAll(date, action);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileRestoreController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(FileRestoreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showContent(int version) {
        fileContent.setVisible(true);
        saveBtn.setVisible(true);

        this.fileSize = fileManage.getFileContent(this.filePath + version, this.fileId, fileContent);
    }

    public void initialise(String[] data) throws ClassNotFoundException, InvalidKeySpecException {

        saveBtn.setVisible(false);
        versionTable.setVisible(false);
        fileContent.setVisible(false);
        this.username = data[0];

        this.getFileList();

    }
}
