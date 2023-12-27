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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author ntu-user
 */
public class FileSplit {

    private static byte PART_SIZE = 4;


    private void split(File file, String name, byte part_size) {
        
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
        LinkedList<String> fileArray= new LinkedList();
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
                
                
                newFileName = "Folder/" + file + ".part"+ Integer.toString(nChunks - 1);
                filePart = new FileOutputStream(new File(newFileName));
                filePart.write(byteChunkPart);
                filePart.flush();
                filePart.close();
                byteChunkPart = null;
                filePart = null;
                
                String zipFileName="Folder/"+name+"part"+Integer.toString(nChunks - 1)+".zip";
                ZipFile zipfile=new ZipFile(zipFileName, "password".toCharArray());
                File partFile=new File(newFileName);
                zipfile.addFile(partFile,zipParameters);
                
                System.out.println("sip name:"+zipFileName);
                fileArray.add(zipFileName);
                partFile.delete();
            }
            inputStream.close();
            
            FileContainers fc=new FileContainers();
            fc.fileUpload(fileArray);
        } catch (IOException exception) {
            exception.printStackTrace();
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


    public void fileSplit(File fl, String name) throws IOException {

        this.split(fl, name, (byte) this.getSizeInBytes(fl.length(), this.PART_SIZE));

    }
}
