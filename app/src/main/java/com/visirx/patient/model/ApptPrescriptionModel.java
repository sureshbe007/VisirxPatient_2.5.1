package com.visirx.patient.model;

/**
 * Created by aa on 20.1.16.
 */
public class ApptPrescriptionModel {

    private String patientId;
    private int appointmentId;
    private String createdAt;
    private String createdById;
    private byte[] prescriptionImage;

    private String fileType;
    private String fileMimeType;
    private String fileGroup;
    private String fileLabel;
    //rony Prescription GC starts

    private String symptoms;
    private String createdByName;
    private String preimageName;
    private String preThumbImageName;
    private int processFlag;
    private String createdAtServer;

    public String getCreatedAtServer() {
        return createdAtServer;
    }

    public void setCreatedAtServer(String createdAtServer) {
        this.createdAtServer = createdAtServer;
    }




    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileMimeType() {
        return fileMimeType;
    }

    public void setFileMimeType(String fileMimeType) {
        this.fileMimeType = fileMimeType;
    }

    public String getFileGroup() {
        return fileGroup;
    }

    public void setFileGroup(String fileGroup) {
        this.fileGroup = fileGroup;
    }

    public String getFileLabel() {
        return fileLabel;
    }

    public void setFileLabel(String fileLabel) {
        this.fileLabel = fileLabel;
    }


    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public byte[] getPrescriptionImage() {
        return prescriptionImage;
    }

    public void setPrescriptionImage(byte[] prescriptionImage) {
        this.prescriptionImage = prescriptionImage;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getPreimageName() {
        return preimageName;
    }

    public void setPreimageName(String preimageName) {
        this.preimageName = preimageName;
    }

    public String getPreThumbImageName() {
        return preThumbImageName;
    }

    public void setPreThumbImageName(String preThumbImageName) {
        this.preThumbImageName = preThumbImageName;
    }

    public int getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(int processFlag) {
        this.processFlag = processFlag;
    }
}
