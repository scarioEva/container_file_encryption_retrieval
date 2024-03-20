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
public class FileLog {
    private SimpleStringProperty details;
    private SimpleStringProperty date;

    
    FileLog(String details, String date) {
        this.details = new SimpleStringProperty(details);  
        this.date = new SimpleStringProperty(date);

    }
    
    public String getDetails() {
        return details.get();
    }
    
    public String getDate() {
        return date.get();
    }
}
