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
    
    public void addACL(String userId, String fileId, Boolean write){
        try {
            this.db.addACLData(userId, fileId, write);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateACL(String userId, String fileId, Boolean write){
        try {
            this.db.updateACLData(fileId, userId, write);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteACL(String fileId){
        try {
            this.db.deleteACL(fileId);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ACLManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
