/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ntu-user
 */
public class ACL {
    private SimpleStringProperty userId;
    private SimpleStringProperty fileId;
    private SimpleStringProperty write;
    
    ACL(String userId, String fileId, String write) {
        this.userId = new SimpleStringProperty(userId);
        this.fileId = new SimpleStringProperty(fileId);
        this.write = new SimpleStringProperty(write);
    }
    
    public String getUserId() {
        return userId.get();
    }
    
    public String getFileId() {
        return fileId.get();
    }
    
    public String getWrite() {
        return write.get();
    }



}
