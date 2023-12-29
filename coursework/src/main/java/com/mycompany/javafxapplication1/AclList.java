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

    AclList(String fileName, String write) {
        this.fileName = new SimpleStringProperty(fileName);
        this.write = new SimpleStringProperty(write);

    }

    public String getFileName() {
        return fileName.get();
    }
    
    public String getWrite() {
        return write.get();
    }

}
