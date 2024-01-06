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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class LogsController {

    private String fileId;
    private DB db = new DB();

    @FXML
    private TableView logTable;

    @FXML
    private Button backBtn;

    @FXML
    private void onBack() {
        Stage primaryStage = (Stage) backBtn.getScene().getWindow();
        primaryStage.close();
    }

    private void getLogs() {
        ObservableList<FileLog> data;
        
        try {
            data = db.getFileLogs(this.fileId);

            TableColumn date = new TableColumn("Date");
            date.setCellValueFactory(
                    new PropertyValueFactory<>("date"));

            TableColumn log = new TableColumn("Log");
            log.setCellValueFactory(
                    new PropertyValueFactory<>("details"));

            logTable.setItems(data);
            logTable.getColumns().addAll(date, log);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LogsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(LogsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void initialise(String[] data) {
        this.fileId = data[1];

        this.getLogs();
    }

}
