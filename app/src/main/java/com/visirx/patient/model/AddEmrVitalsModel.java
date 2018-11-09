package com.visirx.patient.model;

/**
 * Created by aa on 20.1.16.
 */
public class AddEmrVitalsModel {

    private String patientId;
    private String createdAt;
    private String createdById;
    private int appointmentId;
    private String vitalGroup;
    private String vitalKey;
    private String vitalValue;
    private String vitalUnit;
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

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getVitalGroup() {
        return vitalGroup;
    }

    public void setVitalGroup(String vitalGroup) {
        this.vitalGroup = vitalGroup;
    }

    public String getVitalKey() {
        return vitalKey;
    }

    public void setVitalKey(String vitalKey) {
        this.vitalKey = vitalKey;
    }

    public String getVitalValue() {
        return vitalValue;
    }

    public void setVitalValue(String vitalValue) {
        this.vitalValue = vitalValue;
    }

    public String getVitalUnit() {
        return vitalUnit;
    }

    public void setVitalUnit(String vitalUnit) {
        this.vitalUnit = vitalUnit;
    }

    public int getProcessedFlag() {
        return processedFlag;
    }

    public void setProcessedFlag(int processedFlag) {
        this.processedFlag = processedFlag;
    }
}
