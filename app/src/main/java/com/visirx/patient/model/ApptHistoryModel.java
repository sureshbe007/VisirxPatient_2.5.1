package com.visirx.patient.model;

/**
 * Created by aa on 20.1.16.
 */
public class ApptHistoryModel {

    public ApptHistoryModel(String date,
                            String status, String symptoms) {
        super();
        this.date = date;
        this.status = status;
        this.symptoms = symptoms;
    }

    private String patientId;
    private int appointmentId;
    private String date;
    private String status;
    private String symptoms;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    private String createdBy;
}
