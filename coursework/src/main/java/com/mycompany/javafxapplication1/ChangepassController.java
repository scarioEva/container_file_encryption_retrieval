/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class ChangepassController  {
    
    @FXML
    private TextField currentPass;
    @FXML
    private TextField newPass;
    @FXML
    private TextField confirmNewPass;
    @FXML
    private Button saveButton;
    
    @FXML 
    private void onSave(){
    System.out.println(currentPass.getText()); 
            
            }
    public void initilise(){
        newPass.setText("zaki");
    }

    
    }    
//hello