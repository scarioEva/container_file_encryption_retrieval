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
public class FileData {
    private SimpleStringProperty fileName;
    private SimpleStringProperty path;
    private SimpleStringProperty fileId;
    private SimpleStringProperty userId;

    
    
    FileData(String fileName, String path, String fileId, String userId) {
        this.fileName = new SimpleStringProperty(fileName);
        this.path = new SimpleStringProperty(path);
        this.userId = new SimpleStringProperty(userId);
        this.fileId=new SimpleStringProperty(fileId);
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
    
    
}
