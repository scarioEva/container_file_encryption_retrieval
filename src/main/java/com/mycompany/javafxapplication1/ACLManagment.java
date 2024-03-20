/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ntu-user
 */
public class ACLManagment {
    private DB db=new DB();
    
    public void addACL(String username, String fileId, Boolean write){
        
        System.out.println(username+ ", "+ fileId+", "+ write);
        try {
            String userId = this.db.getUser(username, "name", "id");
            this.db.addACLData(userId, fileId, write);
            
            this.db.addLogToDb(fileId, "You granted the access to "+ (write? "write":"read")+" the file to the following user: "+username);
            
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateACL(String username, String fileId, Boolean write){
        try {
            String userId = this.db.getUser(username, "name", "id");
            this.db.updateACLData(fileId, userId, write);
            this.db.addLogToDb(fileId, "You modified the permission to "+ (write? "write":"read")+" the file to the following user: "+username);
            
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteACL(String fileId){
        try {
            this.db.deleteACL("fileId",fileId);
            this.db.addLogToDb(fileId, "You had removed the acces to share your file to the other user.");
            
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
