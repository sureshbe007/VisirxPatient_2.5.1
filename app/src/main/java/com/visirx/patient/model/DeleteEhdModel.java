package com.visirx.patient.model;

/**
 * Created by Administrator on 25-03-2016.
 */
public class DeleteEhdModel {
    private String patientId;
    private int appointmentId;
    private String createdAt;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    private String mediaType;

    public String getPatientId() {
        return patientId;
    }
    public int getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
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
}
