/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author ntu-user
 */
public class FileChunk {

    private static byte PART_SIZE = 4;
    private CommonClass commonClass = new CommonClass();
    private String fileId;
    private DB db = new DB();
    private Boolean isCreate;

    private void split(File file, String name, byte part_size) {

        String encryptionKey = isCreate ? commonClass.generateRandomString(10) : this.getDecryptionKey();

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setCompressionLevel(CompressionLevel.MAXIMUM);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);

        File inputFile = file;
        FileInputStream inputStream;
        String newFileName;
        FileOutputStream filePart;
        int fileSize = (int) inputFile.length();
        int nChunks = 0, read = 0, readLength = part_size;
        byte[] byteChunkPart;
        LinkedList<String> fileArray = new LinkedList();
        try {
            inputStream = new FileInputStream(inputFile);
            while (fileSize > 0) {
                if (fileSize <= part_size) {
                    readLength = fileSize;
                }
                byteChunkPart = new byte[readLength];
                read = inputStream.read(byteChunkPart, 0, readLength);
                fileSize -= read;
                assert (read == byteChunkPart.length);
                nChunks++;

                newFileName = file + ".part" + Integer.toString(nChunks - 1);
                filePart = new FileOutputStream(new File(newFileName));
                filePart.write(byteChunkPart);
                filePart.flush();
                filePart.close();
                byteChunkPart = null;
                filePart = null;

                String zipFileName = commonClass.localDirectory + name + "part" + Integer.toString(nChunks - 1) + ".zip";
                ZipFile zipfile = new ZipFile(zipFileName);
//                ZipFile zipfile = new ZipFile(zipFileName, encryptionKey.toCharArray());

                File partFile = new File(newFileName);
                zipfile.addFile(partFile);
//                zipfile.addFile(partFile, zipParameters);

                fileArray.add(zipFileName);
                partFile.delete();
            }
            inputStream.close();
            file.delete();
            FileServers fc = new FileServers();
            fc.fileUpload(fileArray, name);

            //adding encryption key to database
            if (isCreate) {
                db.addKeysToDb(encryptionKey, this.fileId);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(FileChunk.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileChunk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getSizeInBytes(long largefileSizeInBytes, int numberOfFilesforSplit) {
        if (largefileSizeInBytes % numberOfFilesforSplit != 0) {
            largefileSizeInBytes = ((largefileSizeInBytes / numberOfFilesforSplit) + 1) * numberOfFilesforSplit;
        }
        long x = largefileSizeInBytes / numberOfFilesforSplit;
        if (x > Integer.MAX_VALUE) {
            throw new NumberFormatException("size too large");
        }
        return (int) x;
    }

    public void fileSplit(File fl, String name, String newFileId, Boolean isCreate) throws IOException {
        this.fileId = newFileId;
        this.isCreate = isCreate;
        this.split(fl, name, (byte) this.getSizeInBytes(fl.length(), this.PART_SIZE));
    }

    private String getDecryptionKey() {
        return db.getKeyFromDb(this.fileId);
    }

    private Boolean decryptFile(LinkedList<String> fileArray) {
        String key = this.getDecryptionKey();
        for (int i = 0; i < fileArray.size(); i++) {
            String fileName = commonClass.localDirectory + fileArray.get(i);

            try {
                ZipFile zipFile = new ZipFile(fileName);
//                ZipFile zipFile = new ZipFile(fileName, key.toCharArray());

                zipFile.extractAll(commonClass.localDirectory);
                System.out.println("unzipping");

            } catch (ZipException ex) {
                Logger.getLogger(FileChunk.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                File fl = new File(fileName);
                fl.delete();
            }
        }
        return true;
    }

    public Boolean merge(String outputFile, LinkedList<String> fileArray, String fileId) {
        this.fileId = fileId;
        System.out.println("merge calling");
        Boolean flag = false;
        LinkedList<String> fileList = new LinkedList();

        if (this.decryptFile(fileArray)) {
            File ofile = new File(commonClass.localDirectory + outputFile + ".txt");
            FileOutputStream fos;
            FileInputStream fis;
            byte[] fileBytes;
            int bytesRead = 0;

            for (int i = 0; i < fileArray.size(); i++) {
                fileList.add(outputFile + ".txt.part" + i);
            }

            try {
                fos = new FileOutputStream(ofile, true);
                for (String files : fileList) {
                    File file = new File(commonClass.localDirectory + files);

                    fis = new FileInputStream(file);
                    fileBytes = new byte[(int) file.length()];
                    bytesRead = fis.read(fileBytes, 0, (int) file.length());
                    assert (bytesRead == fileBytes.length);
                    assert (bytesRead == (int) file.length());
                    fos.write(fileBytes);
                    fos.flush();
                    fileBytes = null;
                    fis.close();
                    fis = null;
                }
                fos.close();
                fos = null;
                flag = true;

                //write code to delete zip file and empty list
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                //delete chunked file after merge in temp folder
                for (int j = 0; j < fileList.size(); j++) {
                    File fl = new File(commonClass.localDirectory + fileList.get(j));
                    fl.delete();
                }
            }
        }

        return flag;
    }

}
