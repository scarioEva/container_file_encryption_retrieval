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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ntu-user
 */
public class FileContainers {

    private static final String FILE_CONATINER1_REMOTE_HOST = "172.21.0.3";
    private static final String FILE_CONATINER2_REMOTE_HOST = "172.21.0.4";
    private static final String FILE_CONATINER3_REMOTE_HOST = "172.21.0.5";
    private static final String FILE_CONATINER4_REMOTE_HOST = "172.21.0.6";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "soft40051_pass";
    private static final int REMOTE_PORT = 22;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;
    private static final String REMOTE_FILE = "/home/ntu-user/APP/";

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

    public void fileUpload(LinkedList<String> array) {
        if (!array.get(0).equals("")) {
            onFileUpload(this.FILE_CONATINER1_REMOTE_HOST, array.get(0));
        }

        if (!array.get(1).equals("")) {
            onFileUpload(this.FILE_CONATINER2_REMOTE_HOST, array.get(0));
        }

        if (!array.get(2).equals("")) {
            onFileUpload(this.FILE_CONATINER3_REMOTE_HOST, array.get(0));
        }

        if (!array.get(3).equals("")) {
            onFileUpload(this.FILE_CONATINER4_REMOTE_HOST, array.get(0));
        }
    }

    public void onFileUpload(String remoteHost, String file) {
        File fl = new File(file);
        try {

            ChannelSftp channelSftp = setupJsch(remoteHost);
//            channelSftp.connect();

            channelSftp.put(fl.getName(), this.REMOTE_FILE);
            channelSftp.exit();

        } catch (JSchException | SftpException e) {
            Logger.getLogger(FileContainers.class.getName()).log(Level.SEVERE, null, e);
        } finally {

            if (jschSession != null) {
                jschSession.disconnect();
                fl.delete();
            }
        }
    }
}
