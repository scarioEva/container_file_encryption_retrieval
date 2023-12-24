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

        if(fileManage.createNewFile(this.username, fileName, fileContent)){
            String[] data ={this.username};
            mc.redirectUser(data);
            primaryStage.close();
        }
//        try{
//            String str = fileTextArea.getText();
//            FileWriter writer = new FileWriter(fileName);
//            writer.write(str);
//            
//            writer.close();
//            
//            this.mc.dialogue("Success!", "File updated successfully!", Alert.AlertType.INFORMATION);
//        }
//        catch(IOException ie){
//            
//        }
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
    
//    private void getFileData(){
//        try{
//            File file=new File(fileName);
//            fileLabel.setText(file.getName());
//
//            FileReader fileReader =new FileReader(fileName);
//            var bufferReader = new BufferedReader(fileReader);
//            
//            String fileData=null;
//            
//            while((fileData= bufferReader.readLine())!=null){
//                fileTextArea.setText(fileData);
//            }
//            
//        }
//        catch(FileNotFoundException fe){
//           
//        }
//        catch(IOException ie){
//            
//        }
//        
//    }

    public void initialise(String[] credentials) {
        this.username=credentials[0];
    }
}
