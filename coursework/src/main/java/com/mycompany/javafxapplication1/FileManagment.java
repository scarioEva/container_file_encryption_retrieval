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
    
    private DB db=new DB();
    private MainController mc=new MainController();
    FileServers fc=new FileServers();
    private CommonClass commonClass=new CommonClass();
    private String fileLength;
//    public String fileDirectory="Folder"+File.separator;

    

    
    private void writeFile(String filePath, String fileContent){
        try {
            FileWriter fw;
            fw = new FileWriter(filePath);
            fw.write(fileContent);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  
    
    public boolean createNewFile(String user,String filenName, String content){
        String generateFileId=commonClass.generateRandomString(10);
        
        Boolean flag =false;
        try {
            String actualFileName=commonClass.generateRandomString(10);
            String filePath=commonClass.localDirectory+actualFileName+".txt";
            
            File fl=new File(filePath);
//            fl.getParentFile().mkdirs();
            
            if(fl.createNewFile()){
               this.writeFile(filePath, content);
            }
            this.fileLength=fl.length()+"bytes";
            
            FileChunk fs=new FileChunk();
            
            fs.fileSplit( fl, actualFileName,generateFileId, true);
//            fc.fileUpload(fl, fl, fl, fl);
            
            
            
            
            String userId=db.getUser(user,"name", "id");
            
            db.addFileDataToDB(generateFileId, userId, filenName, actualFileName, this.fileLength);
            
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
    
    public boolean updatFile(String id, String name, String fileName, String content){
        Boolean flag =false;
        String filePath=commonClass.localDirectory+ fileName+".txt";
        try {
            File fl=new File(filePath);
//            fl.getParentFile().mkdirs();
            
            this.writeFile(filePath, content);
            
            db.updateFileData(id, name, fl.length()+"bytes");
            
            FileChunk fs=new FileChunk();
            
            System.out.println("path:"+filePath+"\nfilename:"+ fileName);
            
            fs.fileSplit(fl, fileName, id, false);
            
            if(this.mc.dialogue("File updated successfully", "Successful!",Alert.AlertType.INFORMATION).equals("OK")){
                flag=true;
            }
            
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManagment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return flag;
        
    }
}
