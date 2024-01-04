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
import javafx.stage.Stage;

/**
 *
 * @author ntu-user
 */
public class FileManagment {

    private DB db = new DB();
    private MainController mc = new MainController();
    FileServers fc = new FileServers();
    private CommonClass commonClass = new CommonClass();
    private String fileLength;
    private FileChunk fileChunk = new FileChunk();
    private FileServers fileServers = new FileServers();
//    public String fileDirectory="Folder"+File.separator;

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
        String generateFileId = commonClass.generateRandomString(10);

        Boolean flag = false;
        try {
            String actualFileName = commonClass.generateRandomString(10);

            String filePathId = actualFileName + 1;
            String filePath = commonClass.localDirectory + filePathId + ".txt";

            File fl = new File(filePath);
//            fl.getParentFile().mkdirs();

            if (fl.createNewFile()) {
                this.writeFile(filePath, content);
            }
            this.fileLength = fl.length() + "bytes";

            FileChunk fs = new FileChunk();

            fs.fileSplit(fl, filePathId, generateFileId, true);
//            fc.fileUpload(fl, fl, fl, fl);

            String userId = db.getUser(user, "name", "id");

            db.addFileDataToDB(generateFileId, userId, fileName, actualFileName, this.fileLength, 1, commonClass.currentDate);
            db.addFileVersionsToDB(generateFileId, 1, fileName, commonClass.currentDate);
            db.addLogToDb(generateFileId, "You created new file name: " + fileName);
            if (this.mc.dialogue("File created successfully", "Successful!", Alert.AlertType.INFORMATION).equals("OK")) {
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

    public void updatFile(String id, String name, String fileName, String content, int version, String modification_date, String currentUser, Stage stage) {

        Boolean flag = false;
        String newFileName = fileName + version;
        String filePath = commonClass.localDirectory + newFileName + ".txt";
        try {
            String currentFileUserId = db.getSingleFileData("fileId", id, "userId");
            String owner = db.getUser(currentFileUserId, "id", "name");

            File fl = new File(filePath);
//            fl.getParentFile().mkdirs();

            this.writeFile(filePath, content);

            db.updateFileData(id, name, fl.length() + "bytes", version, commonClass.displayDate(modification_date), true);
            db.addFileVersionsToDB(id, version, name, modification_date);
            db.addLogToDb(id, (currentUser.equals(owner) ? "You" : currentUser) + " updated the file with file name: \"" + name + "\" and content: \"" + content + "\".");

            FileChunk fs = new FileChunk();

            System.out.println("path:" + filePath + "\nfilename:" + newFileName);

            fs.fileSplit(fl, newFileName, id, false);

            if (this.mc.dialogue("File updated successfully", "Successful!", Alert.AlertType.INFORMATION).equals("OK")) {
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
            mc.redirectUser(data);
            stage.close();
        }

    }

    public long getFileContent(String fileName, String fileId, TextArea fileTextArea) {
        String remoteFile;
        long fileSize = 0;
        if (fileChunk.merge(fileName, fileServers.downloadEncryptedFile(fileName), fileId)) {
            remoteFile = commonClass.localDirectory + fileName + ".txt";

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
                File fl = new File(remoteFile);
                fileSize = fl.length();
                fl.delete();

            } catch (FileNotFoundException fe) {

            } catch (IOException ie) {

            }
        }
        return fileSize;

    }

    public void deleteFile(String fileId, String fileName, Stage stage, String username) {
        Stage primaryStage = (Stage) stage;
        if (this.mc.dialogue("Confirmation", "Are you sure you want to delete " + fileName + "?", Alert.AlertType.CONFIRMATION).equals("OK")) {
            try {
                db.deleteFile(fileId);
                db.addLogToDb(fileId, "You deleted the file: \"" + fileName + "\"");
                if (this.mc.dialogue("Succcess", "File deleted successfully!", Alert.AlertType.INFORMATION).equals("OK")) {

                    String[] data = {username};
                    this.mc.redirectUser(data);
                    primaryStage.close();
                }
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
