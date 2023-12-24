/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
 * @author ntu-user
 */
public class FileManagment {
    private String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
    private DB db=new DB();
    private MainController mc=new MainController();
    
    private String generateRandomString(int n){
        StringBuilder sb = new StringBuilder(n); 
        for (int i = 0; i < n; i++) { 
 
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index   = (int)(this.AlphaNumericString.length() * Math.random()); 
 
            // add Character one by one in end of sb 
            sb.append(this.AlphaNumericString.charAt(index)); 
        } 
 
        return sb.toString(); 
    } 
    
    public boolean createNewFile(String user,String filenName, String content){
        Boolean flag =false;
        try {
            String actualFileName=this.generateRandomString(10);
            String filePath="Folder"+File.separator+actualFileName+".txt";
            
            File fl=new File(filePath);
            fl.getParentFile().mkdirs();
            
            if(fl.createNewFile()){
                FileWriter fw;
                fw = new FileWriter(filePath);
                fw.write(content);
                fw.close();
            }
            
            String userId=db.getUserId(user);
            
            db.addFileDataToDB(userId, filenName, actualFileName+".txt", fl.length()+" bytes");
            
            if(this.mc.dialogue("File created successfully", "Successful!",Alert.AlertType.INFORMATION).equals("OK")){
                flag=true;
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
}
