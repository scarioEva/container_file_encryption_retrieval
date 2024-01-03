/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ntu-user
 */
public class FileData {

    private SimpleStringProperty fileName;
    private SimpleStringProperty path;
    private SimpleStringProperty fileId;
    private SimpleStringProperty userId;
    private SimpleIntegerProperty version;
    private SimpleStringProperty createdAt;
    private SimpleStringProperty lastModified;

    FileData(String fileName, String path, String fileId, String userId, int version, String created_at, String last_modified) {
        this.fileName = new SimpleStringProperty(fileName);
        this.path = new SimpleStringProperty(path);
        this.userId = new SimpleStringProperty(userId);
        this.fileId = new SimpleStringProperty(fileId);
        this.createdAt = new SimpleStringProperty(created_at);
        this.lastModified = new SimpleStringProperty(last_modified);
        this.version = new SimpleIntegerProperty(version);
    }
    
    public final StringProperty fileNameProperty() {
        return fileName;
    }

    public String getFilaName() {
        return fileName.get();
    }

    public String getPath() {
        return path.get();
    }

    public String getFileId() {
        return fileId.get();
    }

    public String getUserId() {
        return userId.get();
    }
    
    public int getVersion() {
        return version.get();
    }
    
    public String getCreatedAt() {
        return createdAt.get();
    }
    
    public String getLastModified() {
        return lastModified.get();
    }

}
