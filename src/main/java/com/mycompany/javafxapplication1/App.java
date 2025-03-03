package com.mycompany.javafxapplication1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException, InvalidKeySpecException {
        DB myObj = new DB();
        myObj.log("-------- Simple Tutorial on how to make JDBC connection to SQLite DB ------------");
//        myObj.log("\n---------- Drop table ----------");
//        try {
//            myObj.delTable(myObj.getTableName());
//            myObj.delFileTable();
//            myObj.delFileVersionsTable();
//            myObj.delAclTable();
//
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//        }
        myObj.log("\n---------- Create table ----------");
        try {
            myObj.createTable(myObj.getTableName());
            myObj.createFilesTable();
            myObj.createACLsTable();
            myObj.createEncryptionTable();
            myObj.createFileVersionsTable();
            myObj.createFileLogTable();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            MainController mc = new MainController();
            mc.redirectLogin();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
