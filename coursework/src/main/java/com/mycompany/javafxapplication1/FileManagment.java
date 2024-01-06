/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author ntu-user
 */
public class FileManagment {

    private DB db = new DB();
    private MainController mainController = new MainController();
    private CommonClass commonClass = new CommonClass();
    private String fileLength;
    private FileChunk fileChunk = new FileChunk();
    private FileServers fileServers = new FileServers();

    private void writeFile(String filePath, String fileContent) {
        try {
            FileWriter fw;
            fw = new FileWriter(filePath);
            fw.write(fileContent);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean createNewFile(String user, String fileName, String content) {
        //generate new file id
        String generateFileId = commonClass.generateRandomString(10);

        Boolean flag = false;
        try {
            //generate new fileName to store to server 
            String actualFileName = commonClass.generateRandomString(10);

            //adding fileName with version 1(only creating)
            String filePathId = actualFileName + 1;
            String filePath = commonClass.localDirectory + filePathId + ".txt";

            File fl = new File(filePath);

            //creating new file to local temp folder
            if (fl.createNewFile()) {
                this.writeFile(filePath, content);
            }
            this.fileLength = fl.length() + "bytes";

            //calling function to split the file into chunks
            this.fileChunk.fileSplit(fl, filePathId, generateFileId, true);

            String userId = db.getUser(user, "name", "id");

            //add file metadata to db
            db.addFileDataToDB(generateFileId, userId, fileName, actualFileName, this.fileLength, 1, commonClass.currentDate);

            //add file version to file Version db
            db.addFileVersionsToDB(generateFileId, 1, fileName, commonClass.currentDate, userId);

            //add audit trail data to log db
            db.addLogToDb(generateFileId, "You created new file name: " + fileName);

            if (this.mainController.dialogue("File created successfully", "Successful!", Alert.AlertType.INFORMATION).equals("OK")) {
                flag = true;
            }

        } catch (IOException ex) {
            Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
        }

        return flag;

    }

    public Boolean updatFile(String id, String name, String fileName, String content, int version, String modification_date, String currentUser, Stage stage) {

        Boolean flag = false;
        String newFileName = fileName + version;
        String filePath = commonClass.localDirectory + newFileName + ".txt";
        try {
            String currentFileUserId = db.getSingleFileData("fileId", id, "userId");
            String owner = db.getUser(currentFileUserId, "id", "name");
            String current_user_id = db.getUser(currentUser, "name", "id");

            File fl = new File(filePath);

            this.writeFile(filePath, content);

            //update mile metadata to databse
            db.updateFileData(id, name, fl.length() + "bytes", version, commonClass.displayDate(modification_date), true, "");

            //update file versions to databse
            db.addFileVersionsToDB(id, version, name, modification_date, current_user_id);

            //add logs to db
            db.addLogToDb(id, (currentUser.equals(owner) ? "You" : currentUser) + " updated the file with file name: \"" + name + "\".");

            //split the file
            this.fileChunk.fileSplit(fl, newFileName, id, false);

            if (this.mainController.dialogue("File updated successfully", "Successful!", Alert.AlertType.INFORMATION).equals("OK")) {
                flag = true;
            }

        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            String[] data = {currentUser};
            mainController.redirectUser(data);
            stage.close();
        }

        return flag;

    }

    public long getFileContent(String fileName, String fileId, TextArea fileTextArea) {
        String remoteFile;
        long fileSize = 0;
        //download chunk file then decrypt and merge the file and store it onto temp folder
        if (fileChunk.merge(fileName, fileServers.downloadEncryptedFile(fileName), fileId)) {
            remoteFile = commonClass.localDirectory + fileName + ".txt";

            try {
                //read the downloaded file from the temp folder
                FileReader fileReader = new FileReader(remoteFile);
                var bufferReader = new BufferedReader(fileReader);

                String fileData = null;
                String output = "";
                while ((fileData = bufferReader.readLine()) != null) {
                    output += fileData + System.getProperty("line.separator");
                }
                fileTextArea.setText(output);
                fileReader.close();

                File fl = new File(remoteFile);
                fileSize = fl.length();

                //delete temp file after sent to TextArea
                fl.delete();

            } catch (FileNotFoundException fe) {

            } catch (IOException ie) {

            }
        }
        //used when restoring file and update the size of the previous file to the database
        return fileSize;

    }

    public void deleteFile(String fileId, String fileName, Stage stage, String username) {
        Stage primaryStage = (Stage) stage;
        if (this.mainController.dialogue("Confirmation", "Are you sure you want to delete " + fileName + "?", Alert.AlertType.CONFIRMATION).equals("OK")) {
            try {
                //set file to inactive on file metadata table
                db.deleteFile(fileId);

                //add logs
                db.addLogToDb(fileId, "You deleted the file: \"" + fileName + "\"");
                if (this.mainController.dialogue("Succcess", "File deleted successfully!", Alert.AlertType.INFORMATION).equals("OK")) {

                    String[] data = {username};
                    this.mainController.redirectUser(data);
                    primaryStage.close();
                }
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void downloadFile(Stage stage, String fileId, String filePath, int version, String fileName) {
        Boolean flag = false;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text files", "*.txt"));
        fileChooser.setInitialFileName(fileName + ".txt");

        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {

            //get destination path while saving via file chooser
            String destination_path = selectedFile.getAbsolutePath();

            //get exact filename with version from the file server
            String remoteFileName = filePath + version;

            //download chunk file then decrypt and merge the file and store it onto temp folder
            if (fileChunk.merge(remoteFileName, fileServers.downloadEncryptedFile(remoteFileName), fileId)) {
                FileReader fileReader = null;
                String remoteFile = commonClass.localDirectory + remoteFileName + ".txt";
                File rf = new File(remoteFile);

                try {

                    fileReader = new FileReader(remoteFile);
                    var bufferReader = new BufferedReader(fileReader);
                    String fileData;
                    String output = "";
                    while ((fileData = bufferReader.readLine()) != null) {
                        output += fileData + System.getProperty("line.separator");

                    }
                    //transfering file content from temp folder to the deftination file
                    File fl = new File(destination_path);

                    if (fl.createNewFile()) {
                        this.writeFile(destination_path, output);
                    }

                    flag = true;

                } catch (FileNotFoundException ex) {
                    flag = false;
                    Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    flag = false;
                    Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        //deleting file from temp folder
                        rf.delete();
                        fileReader.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            if (flag) {
                mainController.dialogue("Succes", "File downloaded successfully!", Alert.AlertType.INFORMATION);
            } else {
                mainController.dialogue("Error", "Something went wrong. Please try agaian", Alert.AlertType.ERROR);
            }

        }
    }
}
