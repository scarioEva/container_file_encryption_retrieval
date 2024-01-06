/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ntu-user
 */
public class FileServers {

    private static final String FILE_CONATINER1_REMOTE_HOST = "172.21.0.3";
    private static final String FILE_CONATINER2_REMOTE_HOST = "172.21.0.4";
    private static final String FILE_CONATINER3_REMOTE_HOST = "172.21.0.5";
    private static final String FILE_CONATINER4_REMOTE_HOST = "172.21.0.6";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "soft40051_pass";
    private static final int REMOTE_PORT = 22;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;
    private static final String REMOTE_DIRECTORY = "/home/ntu-user/APP/";
    private CommonClass commonClass = new CommonClass();

    Session jschSession = null;

    private ChannelSftp setupJsch(String remoteHost) throws JSchException {
        Session jschSession = null;
        JSch jsch = new JSch();
        jsch.setKnownHosts("/home/mkyong/.ssh/known_hosts");

        // Set the StrictHostKeyChecking option to "no" to automatically answer "yes" to the prompt
        jschSession = jsch.getSession(this.USERNAME, remoteHost, this.REMOTE_PORT);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        jschSession.setConfig(config);

        // authenticate using password
        jschSession.setPassword(this.PASSWORD);

        // 10 seconds session timeout
        jschSession.connect(this.SESSION_TIMEOUT);

        Channel sftp = jschSession.openChannel("sftp");

        // 5 seconds timeout
        sftp.connect(this.CHANNEL_TIMEOUT);
        return (ChannelSftp) sftp;
    }

    //creating array and adding file server ip address inside array
    private LinkedList fileContainers() {
        LinkedList<String> fileContainers = new LinkedList();
        fileContainers.add(this.FILE_CONATINER1_REMOTE_HOST);
        fileContainers.add(this.FILE_CONATINER2_REMOTE_HOST);
        fileContainers.add(this.FILE_CONATINER3_REMOTE_HOST);
        fileContainers.add(this.FILE_CONATINER4_REMOTE_HOST);

        return fileContainers;
    }

    private void deleteRemoteFile(String server, String fileName) {
        try {
            ChannelSftp channelSftp = setupJsch(server);
            
            //checking if file exist in remote server
            if (this.checkFileExists(server, fileName)) {
                //removing file from server
                channelSftp.rm(fileName);
            }
        } catch (JSchException ex) {
            Logger.getLogger(FileServers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SftpException ex) {
            Logger.getLogger(FileServers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fileUpload(LinkedList<String> fileArray, String fileName) {

        LinkedList<String> fileContainers = this.fileContainers();

        for (int i = 0; i < fileContainers.size(); i++) {
            
            if (i < fileArray.size()) {
                onFileUpload(fileContainers.get(i), fileArray.get(i));
            } else {
                this.deleteRemoteFile(fileContainers.get(i), this.REMOTE_DIRECTORY + fileName + "part" + i + ".zip");
            }
        }
    }

    public boolean deleteFiles(String fileName) {
        LinkedList<String> fileContainers = this.fileContainers();
        for (int i = 0; i < fileContainers.size(); i++) {
            this.deleteRemoteFile(fileContainers.get(i), this.REMOTE_DIRECTORY + fileName + "part" + i + ".zip");
        }
        return true;
    }

    private void onFileUpload(String remoteHost, String file) {
        File fl = new File(file);
        try {
            ChannelSftp channelSftp = setupJsch(remoteHost);
            channelSftp.put(file, this.REMOTE_DIRECTORY);
            channelSftp.exit();

        } catch (JSchException | SftpException e) {
            Logger.getLogger(FileServers.class.getName()).log(Level.SEVERE, null, e);
        } finally {

            //delete local encrypted chunk file in temp folder after delete
            fl.delete();

            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
    }

    private Boolean checkFileExists(String server, String file) throws JSchException {
        Vector res = null;
        ChannelSftp channelSftp = setupJsch(server);
        try {
            res = channelSftp.ls(file);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }
        }
        return res != null && !res.isEmpty();
    }

    private String getFileName(String fileName, int i) {
        return fileName + "part" + i + ".zip";
    }

    public LinkedList<String> downloadEncryptedFile(String fileName) {
        LinkedList<String> servers = this.fileContainers();
        LinkedList<String> filesList = new LinkedList();
        for (int i = 0; i < 4; i++) {
            try {
                String name = this.REMOTE_DIRECTORY + this.getFileName(fileName, i);
                System.out.println("server: " + servers.get(i) + ", file " + (i + 1) + "=" + this.checkFileExists(servers.get(i), name));

                Boolean fileExists = this.checkFileExists(servers.get(i), name);
                if (fileExists) {
                    ChannelSftp channelSftp = setupJsch(servers.get(i));
                    channelSftp.get(name, commonClass.localDirectory);

                    //adding downloaded file name to fileList array
                    filesList.add(this.getFileName(fileName, i));
                }
            } catch (JSchException ex) {
                Logger.getLogger(FileServers.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SftpException ex) {
                Logger.getLogger(FileServers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return filesList;
    }

}
