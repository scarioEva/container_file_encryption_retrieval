/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class FileController{
    private String username;
    private MainController mc=new MainController();
    private FileManagment fileManage=new FileManagment();
    private Boolean createMode=true;
    private Boolean userFile=true;
    private DB db=new DB();
    private String fileId;
    private String filePath;

    @FXML
    private TextArea fileTextArea;
    
    @FXML
    private TextField fileNameId;
    
    @FXML
    private Button fileSaveId;
    
    @FXML
    private Button fileCancelId;
    
    @FXML
    private Button fileDelId;

    @FXML
    private Label ownerNameId;
    
    //testing
    @FXML
    private GridPane test;
    
    @FXML
    private void onSave(){
        Stage primaryStage = (Stage) fileSaveId.getScene().getWindow();
        
        String fileName=fileNameId.getText();
        String fileContent=fileTextArea.getText();

        if(createMode){
            if(fileManage.createNewFile(this.username, fileName, fileContent)){
                String[] data ={this.username};
                mc.redirectUser(data);
                primaryStage.close();
            }
        }
        else{
            fileManage.updatFile(this.fileId,fileName, this.filePath, fileContent);
        }
    }
    
    @FXML
    private void onCancel(){
        Stage primaryStage = (Stage) fileCancelId.getScene().getWindow();
        String[] data ={this.username};
        this.mc.redirectUser(data);
        primaryStage.close();
    }
    
    @FXML
    private void onDel(){
        
    }
    
    private void getFileContent(String fileName){
        System.out.println(fileName);
        try{
            FileManagment fm = new FileManagment();
            FileReader fileReader =new FileReader(fm.fileDirectory+fileName);
            var bufferReader = new BufferedReader(fileReader);
            
            String fileData;
            
            while((fileData= bufferReader.readLine())!=null){
                fileTextArea.setText(fileData);
            }
            fileReader.close();
            
        }
        catch(FileNotFoundException fe){
           
        }
        catch(IOException ie){
            
        }
        
    }
    
    
    private void getFileData(String[] fileData){
            this.fileId=fileData[1];
            this.createMode=!fileData[2].equals("false");
            this.userFile=fileData[3].equals("yes");
            
            ObservableList<FileData> data;
            try {
                if(!this.createMode){
                    data = this.db.getFileFromTable(this.fileId,"id");
                        if(!data.isEmpty()){
                            fileNameId.setText(data.get(0).getFilaName());
                            this.filePath=data.get(0).getPath();
                            this.getFileContent(this.filePath);
                            
                            String ownerName=this.db.getUser(data.get(0).getUserId(),"id","name");
                            
                            ownerNameId.setText(ownerName);
                        }

                }
                else{
                    ownerNameId.setText(this.username);
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }

    public void initialise(String[] data) {
        this.username=data[0];
        this.getFileData(data);
    }
}
