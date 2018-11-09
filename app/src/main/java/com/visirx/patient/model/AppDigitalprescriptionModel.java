package com.visirx.patient.model;

import java.util.List;

/**
 * Created by Lenovo on 6/29/2016.
 */
public class AppDigitalprescriptionModel {


    private String patientId;
    private int appointmentId;
    private String createdAtclient;
    private String createdAtServer;
    private int prescriptionId;
    private String isDeleted;
    private int processFlag;

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    private List<DigitalprescriptionDataModel> dpDataModel;

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

    public String getCreatedAtclient() {
        return createdAtclient;
    }

    public void setCreatedAtclient(String createdAtclient) {
        this.createdAtclient = createdAtclient;
    }

    public String getCreatedAtServer() {
        return createdAtServer;
    }

    public void setCreatedAtServer(String createdAtServer) {
        this.createdAtServer = createdAtServer;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(int processFlag) {
        this.processFlag = processFlag;
    }

    public List<DigitalprescriptionDataModel> getDpDataModel() {
        return dpDataModel;
    }

    public void setDpDataModel(List<DigitalprescriptionDataModel> dpDataModel) {
        this.dpDataModel = dpDataModel;
    }
}
