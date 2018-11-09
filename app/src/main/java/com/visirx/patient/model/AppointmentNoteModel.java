package com.visirx.patient.model;

/**
 * Created by aa on 20.1.16.
 */
public class AppointmentNoteModel {

    private String patientId;
    private int appointmentId;
    private String createdAt;
    private String createdById;
    private String createdByName;
    private String notes;
    private String patientName;
    private int processFlag;
    private String createdAtServer;

    public String getCreatedAtServer() {
        return createdAtServer;
    }

    public void setCreatedAtServer(String createdAtServer) {
        this.createdAtServer = createdAtServer;
    }



    public int getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(int processFlag) {
        this.processFlag = processFlag;
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

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
