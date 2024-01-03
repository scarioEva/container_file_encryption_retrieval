/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ntu-user
 */
public class FileRestore {

    private SimpleStringProperty fileId;
    private SimpleStringProperty fileName;
    private SimpleStringProperty creationDate;
    private SimpleStringProperty filePath;

    FileRestore(String fileId, String fileName, String creationDate, String filePath) {
        this.fileId = new SimpleStringProperty(fileId);
        this.fileName = new SimpleStringProperty(fileName);
        this.creationDate = new SimpleStringProperty(creationDate);
        this.filePath = new SimpleStringProperty(filePath);

    }

    public String getFileId() {
        return fileId.get();
    }

    public String getFileName() {
        return fileName.get();
    }

    public String getCreationDate() {
        return creationDate.get();
    }

    public String getFilePath() {
        return filePath.get();
    }
}
