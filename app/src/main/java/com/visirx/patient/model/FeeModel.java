package com.visirx.patient.model;

/**
 * Created by aa on 20.1.16.
 */
public class FeeModel {

    private int Appointmentid;
    private String DoctorFee;
    private String lastUpdatedBy;
    private String lastUpdatedAt;

    public int getAppointmentid() {
        return Appointmentid;
    }

    public void setAppointmentid(int appointmentid) {
        Appointmentid = appointmentid;
    }

    public String getDoctorFee() {
        return DoctorFee;
    }

    public void setDoctorFee(String doctorFee) {
        DoctorFee = doctorFee;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
