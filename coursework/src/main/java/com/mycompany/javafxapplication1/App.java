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

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date d2 = sdf.parse("20-06-2020 06:30:50");
            Date d1 = sdf.parse("10-06-2020 01:10:20");

            System.out.println("current:" + sdf.format(new Date()));
            long difference_In_Time
                    = d2.getTime() - d1.getTime();

            long difference_In_Days
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24))
                    % 365;

            System.out.println("Days:" + difference_In_Days);

        } catch (ParseException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        Stage secondaryStage = new Stage();
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
