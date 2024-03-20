/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ntu-user
 */
public class FileVersions {

    private SimpleStringProperty versionId;
    private SimpleStringProperty fileId;
    private SimpleIntegerProperty version;
    private SimpleStringProperty fileName;
    private SimpleStringProperty date;
    private SimpleStringProperty modifiedBy;

    FileVersions(String versionId, String fileId, String fileName, int version, String date, String modifiedBy) {
        this.fileName = new SimpleStringProperty(fileName);
        this.versionId = new SimpleStringProperty(versionId);
        this.version = new SimpleIntegerProperty(version);
        this.fileId = new SimpleStringProperty(fileId);
        this.date = new SimpleStringProperty(date);
        this.modifiedBy = new SimpleStringProperty(modifiedBy);
    }

    public StringProperty modifiedByProperty() {
        return modifiedBy;
    }

    public String getFilaName() {
        return fileName.get();
    }

    public String getFilaId() {
        return fileId.get();
    }

    public int getFilaVersion() {
        return version.get();
    }

    public String getVersionId() {
        return versionId.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getModifiedBY() {
        return modifiedBy.get();
    }

}
