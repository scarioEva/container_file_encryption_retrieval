/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ntu-user
 */
public class AclList {

    private SimpleStringProperty fileName;
    private SimpleStringProperty write;
    private SimpleStringProperty fileId;
    private SimpleStringProperty owner;
    private SimpleStringProperty created_at_date=new SimpleStringProperty();
    private SimpleStringProperty last_updated_date=new SimpleStringProperty();

    AclList(String fileName, String write, String fileId, String owner, String created_at, String last_updated) {
        this.fileName = new SimpleStringProperty(fileName);
        this.write = new SimpleStringProperty(write);
        this.fileId = new SimpleStringProperty(fileId);
        this.owner = new SimpleStringProperty(owner);
    }

    
    public String getFileName() {
        return fileName.get();
    }

    public String getWrite() {
        return write.get();
    }

    public String getFileId() {
        return fileId.get();
    }

    public String getOwner() {
        return owner.get();
    }


}
