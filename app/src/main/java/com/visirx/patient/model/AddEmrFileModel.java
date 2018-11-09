package com.visirx.patient.model;

public class AddEmrFileModel {

    private String patientId;
    private int appointmentId;
    private String createdAt;
    private String createdById;
    private String fileType;
    private String fileMimeType;
    private String fileGroup;
    private String fileLabel;
    private byte[] emrFile;

    private String createdAtServer;

    public String getCreatedAtServer() {
        return createdAtServer;
    }

    public void setCreatedAtServer(String createdAtServer) {
        this.createdAtServer = createdAtServer;
    }
    // rony - EMRFRAGMENT GC  - Starts
    //private byte[] imageThumbnail;
    //private byte[] emrFile;

    public byte[] getEmrFile() {
        return emrFile;
    }

    public void setEmrFile(byte[] emrFile) {
        this.emrFile = emrFile;
    }

    private String emrImageThumbnailPath;


    private String emrImagePath;
    // rony - EMRFRAGMENT GC  - Starts
    private int processedFlag;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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
    /*public byte[] getImageThumbnail() {
		return imageThumbnail;
	}
	public void setImageThumbnail(byte[] imageThumbnail) {
		this.imageThumbnail = imageThumbnail;
	}
	public byte[] getEmrFile() {
		return emrFile;
	}
	public void setEmrFile(byte[] emrFile) {
		this.emrFile = emrFile;
	}*/

    public String getEmrImageThumbnailPath() {
        return emrImageThumbnailPath;
    }

    public void setEmrImageThumbnailPath(String emrImageThumbnailPath) {
        this.emrImageThumbnailPath = emrImageThumbnailPath;
    }

    public String getEmrImagePath() {
        return emrImagePath;
    }

    public void setEmrImagePath(String emrImagePath) {
        this.emrImagePath = emrImagePath;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getProcessedFlag() {
        return processedFlag;
    }

    public void setProcessedFlag(int processedFlag) {
        this.processedFlag = processedFlag;
    }


}