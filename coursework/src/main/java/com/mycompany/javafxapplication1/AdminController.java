package com.mycompany.javafxapplication1;

import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminController {

    private MainController mainController = new MainController();
    private DB db = new DB();

    @FXML
    private TextField userTextField;

    @FXML
    private TableView dataTableView;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private TextField customTextField;

    @FXML
    private void RefreshBtnHandler(ActionEvent event) {
        Stage primaryStage = (Stage) customTextField.getScene().getWindow();
        customTextField.setText((String) primaryStage.getUserData());
    }

    @FXML
    private void handleLogout() {
        Stage primaryStage = (Stage) logoutBtn.getScene().getWindow();
        ObservableList<User> data;
        try {

            this.mainController.redirectLogin();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialise(String[] credentials) {
        userTextField.setText(credentials[0]);
        ObservableList<User> data;
        try {
            data = this.db.getDataFromTable();

            TableColumn user = new TableColumn("User");
            user.setCellValueFactory(
                    new PropertyValueFactory<>("user"));

            TableColumn pass = new TableColumn("Pass");
            pass.setCellValueFactory(
                    new PropertyValueFactory<>("pass"));
            dataTableView.setItems(data);
            dataTableView.getColumns().addAll(user, pass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
