/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author ntu-user
 */
public class DB {
    private String fileName = "jdbc:sqlite:comp20081.db";
    private int timeout = 30;
    private String dataBaseName = "COMP20081";
    private String dataBaseTableName = "Users";
    private String aclTableName="ACL";
    private String filesTableName="FileMetadata";
    private String encryptionTableName="Encryption";
    Connection connection = null;
    private Random random = new SecureRandom();
    private String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private int iterations = 10000;
    private int keylength = 256;
    private String saltValue;
    
    /**
     * @brief constructor - generates the salt if it doesn't exists or load it from the file .salt
     */
    DB() {
        try {
            File fp = new File(".salt");
            if (!fp.exists()) {
                saltValue = this.getSaltvalue(30);
                FileWriter myWriter = new FileWriter(fp);
                myWriter.write(saltValue);
                myWriter.close();
            } else {
                Scanner myReader = new Scanner(fp);
                while (myReader.hasNextLine()) {
                    saltValue = myReader.nextLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private ResultSet executeDb(String query, Boolean resultSet ) throws InvalidKeySpecException, ClassNotFoundException {
        ResultSet rs=null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            
            if(resultSet){
                rs=statement.executeQuery(query);
            }
            else
                statement.executeUpdate(query);
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return rs;
    }
    
    private void closeConnection(){
        try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
    }
        
    /**
     * @brief create a new table
     * @param tableName name of type String
     */
    public void createTable(String tableName) throws InvalidKeySpecException, ClassNotFoundException {
        this.executeDb("create table if not exists " + tableName + "(id integer primary key autoincrement, name string, password string, active boolean)", false);
        this.closeConnection();
    }
    
    public void createFilesTable() throws InvalidKeySpecException, ClassNotFoundException {
        this.executeDb("create table if not exists " + this.filesTableName + "(id integer primary key autoincrement, name string, userId String, size string, path string)", false);
        this.closeConnection();
    }

    public void createACLsTable() throws InvalidKeySpecException, ClassNotFoundException {
        this.executeDb("create table if not exists " + this.aclTableName + "(id integer primary key autoincrement, fileId string, userId String, write boolean)", false);
        this.closeConnection();
    }
    /**
     * @brief delete table
     * @param tableName of type String
     */
    public void delTable(String tableName) throws ClassNotFoundException, InvalidKeySpecException {
        this.executeDb("drop table if exists " + tableName, false);
        this.closeConnection();
    }

    /**
     * @brief add data to the database method
     * @param user name of type String
     * @param password of type String
     */
    public void addUserDataToDB(String user, String password) throws InvalidKeySpecException, ClassNotFoundException {
        this.executeDb("insert into " + dataBaseTableName + " (name, password, active) values('" + user + "','" + generateSecurePassword(password) + "', true)", false);
        this.closeConnection();
    }

    /**
     * @brief get data from the Database method
     * @retunr results as ResultSet
     */
    public ObservableList<User> getDataFromTable() throws ClassNotFoundException, InvalidKeySpecException {
        ObservableList<User> result = FXCollections.observableArrayList();
        try {
            ResultSet rs=this.executeDb("select * from " + this.dataBaseTableName, true);

            while (rs.next()) {
                // read the result set
                result.add(new User(rs.getString("name"),rs.getString("password")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            this.closeConnection();
        }
        return result;
    }
    
    public ObservableList<User> getUserList() throws ClassNotFoundException, InvalidKeySpecException {
        ObservableList<User> result = FXCollections.observableArrayList();
        try {
            ResultSet rs=this.executeDb("select name from " + this.dataBaseTableName, true);

            while (rs.next()) {
                // read the result set
                result.add(new User(rs.getString("name")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            this.closeConnection();
        }
        return result;
    }

    /**
     * @brief decode password method
     * @param user name as type String
     * @param pass plain password of type String
     * @return true if the credentials are valid, otherwise false
     */
    public boolean validateUser(String user, String pass) throws InvalidKeySpecException, ClassNotFoundException {
 
        Boolean flag = false;
        try {
            ResultSet rs=this.executeDb("select name, password from " + this.dataBaseTableName, true);

            String inPass = generateSecurePassword(pass);
            
            while (rs.next()) {
                if (user.equals(rs.getString("name")) && rs.getString("password").equals(inPass)) {
                    flag = true;
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally{
            this.closeConnection();
        }
        
        return flag;
    }
    
    public boolean validateUser(String user) throws InvalidKeySpecException, ClassNotFoundException {
        Boolean flag = false;
        try {
            ResultSet rs=this.executeDb("select name, password from " + this.dataBaseTableName, true);

            while (rs.next()) {
                if (user.toLowerCase().equals(rs.getString("name").toLowerCase())) {
                    flag = true;
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally{
            this.closeConnection();
        }
        
        return flag;
    }

    private String getSaltvalue(int length) {
        StringBuilder finalval = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            finalval.append(characters.charAt(random.nextInt(characters.length())));
        }

        return new String(finalval);
    }

    /* Method to generate the hash value */
    private byte[] hash(char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keylength);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public String generateSecurePassword(String password) throws InvalidKeySpecException {
        String finalval = null;

        byte[] securePassword = hash(password.toCharArray(), saltValue.getBytes());

        finalval = Base64.getEncoder().encodeToString(securePassword);

        return finalval;
    }

    /**
     * @brief get table name
     * @return table name as String
     */
    public String getTableName() {
        return this.dataBaseTableName;
    }

    /**
     * @brief print a message on screen method
     * @param message of type String
     */
    public void log(String message) {
        System.out.println(message);

    }
    
    public ObservableList<User> getActiveUser() throws ClassNotFoundException, InvalidKeySpecException {
        ObservableList<User> result = FXCollections.observableArrayList();
        try {
             ResultSet rs=this.executeDb("select * from " + this.dataBaseTableName+ " where active=true", true);
            while (rs.next()) {
                result.add(new User(rs.getString("name"),rs.getString("password")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            this.closeConnection();
        }
        
        return result;
    }
    
    public boolean setUserActive(String user, boolean active) throws InvalidKeySpecException, ClassNotFoundException {
        Boolean flag = false;
        try {
            this.executeDb("update " + this.dataBaseTableName + " set active="+active+" where name='"+user+"'", false);

        }finally {
            flag=true;
            this.closeConnection();
        }
        return flag;
    }
    
    public String getUser(String value, String key, String result) throws InvalidKeySpecException, ClassNotFoundException{
        String output="";
        try{
            ResultSet rs=this.executeDb("select "+result+" from "+this.dataBaseTableName+" where "+key+"='"+value+"'", true);
            while(rs.next()){
                output=rs.getString(result);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            this.closeConnection();
        }
        return output;
    }
    
    public boolean updateUsername(String id, String newUsername) throws InvalidKeySpecException, ClassNotFoundException {
        Boolean flag = false;
        try {
            this.executeDb("update " + this.dataBaseTableName + " set name='"+newUsername+"' where id='"+id+"'", false);
        }finally {
            flag=true;
            this.closeConnection();
        }
        return flag;
    }
    
    public boolean deleteUser(String id) throws InvalidKeySpecException, ClassNotFoundException {
        Boolean flag = false;
        try {
            this.executeDb("delete from " + this.dataBaseTableName + " where id='"+id+"'", false);
        }finally {
            flag=true;
            this.closeConnection();
        }
        return flag;
    }
    
    public void addFileDataToDB(String userId, String fileName, String path, String size) throws InvalidKeySpecException, ClassNotFoundException {
        this.executeDb("insert into " + this.filesTableName + " (name, userId, size, path) values('" + fileName + "','" + userId + "','"+size+"','"+path+"')", false);
        this.closeConnection();
    }
    
    public ObservableList<FileData> getFileFromTable(String value, String key) throws ClassNotFoundException, InvalidKeySpecException {
        ObservableList<FileData> result = FXCollections.observableArrayList();
        try {
            ResultSet rs=this.executeDb("select * from " + this.filesTableName+ " where "+key+"='"+value+"'", true);

            while (rs.next()) {
                // read the result set
                result.add(new FileData(rs.getString("name"),rs.getString("path"),rs.getString("id"), rs.getString("userId")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            this.closeConnection();
        }
        return result;
    }
    
    public void updateFileData(String fileId, String fileName, String size) throws InvalidKeySpecException, ClassNotFoundException {
        this.executeDb("update "+this.filesTableName+ " set name='"+fileName+"', size='"+size+"' where id='"+fileId+"'", false);
        this.closeConnection();
    }
    
    public void addACLData(String userId, String fileId, Boolean write) throws InvalidKeySpecException, ClassNotFoundException {
        this.executeDb("insert into " + this.aclTableName + " (fileId, userId, write) values('" + fileId + "','" + userId +"','"+write+"')", false);
        this.closeConnection();
    }
    
    public void updateACLData(String fileId, String userId, Boolean write) throws InvalidKeySpecException, ClassNotFoundException{
        this.executeDb("update "+this.aclTableName+" set userID='"+userId+"', write='"+write+"' where fileId='"+fileId+"'", false);
    }
    
    public boolean deleteACL(String fileId) throws InvalidKeySpecException, ClassNotFoundException {
        System.out.println("delete");
        Boolean flag = false;
        try {
            this.executeDb("delete from " + this.aclTableName + " where fileId='"+fileId+"'", false);
        }finally {
            flag=true;
            this.closeConnection();
        }
        return flag;
    }
    
    public ObservableList<ACL> getUserAcl(String value, String key) throws ClassNotFoundException, InvalidKeySpecException {
        ObservableList<ACL> result = FXCollections.observableArrayList();
        try {
            ResultSet rs=this.executeDb("select * from " + this.aclTableName+ " where "+key+"='"+value+"'", true);

            while (rs.next()) {
                System.out.println("data "+ rs.getString("write"));
                result.add(new ACL(rs.getString("userId"),rs.getString("fileId"), rs.getString("write")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            this.closeConnection();
        }
        return result;
    }
    
}
