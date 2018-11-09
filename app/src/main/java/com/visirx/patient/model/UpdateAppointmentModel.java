package com.visirx.patient.model;

/**
 * Created by Suresh on 15-02-2016.
 */
public class UpdateAppointmentModel {

    private int appointmentId;
    private String patientId;
    private int fees;
    private String reason;

    private short visitStatus;
    private String lastUpdated;
    private String lastUpdatedBy;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public short getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(short visitStatus) {
        this.visitStatus = visitStatus;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
