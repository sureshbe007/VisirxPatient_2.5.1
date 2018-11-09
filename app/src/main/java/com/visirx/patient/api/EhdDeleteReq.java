package com.visirx.patient.api;


import com.visirx.patient.model.DeleteEhdModel;

import java.util.List;

/**
 * Created by Administrator on 25-03-2016.
 */
public class EhdDeleteReq {

    private String patientId;
    private int appointmentId;
    private String createdAt;
    private RequestHeader requestHeader;
    private String mediType;

    public List<DeleteEhdModel> getDeleteMultimediaList() {
        return deleteMultimediaList;
    }

    public String getMediType() {
        return mediType;
    }

    public void setMediType(String mediType) {
        this.mediType = mediType;
    }

    public void setDeleteMultimediaList(List<DeleteEhdModel> deleteMultimediaList) {
        this.deleteMultimediaList = deleteMultimediaList;
    }

    private List<DeleteEhdModel> deleteMultimediaList;

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


    private List<DeleteEhdModel> deleteEhdModels;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public List<DeleteEhdModel> getDeleteEhdModels() {
        return deleteEhdModels;
    }

    public void setDeleteEhdModels(List<DeleteEhdModel> deleteEhdModels) {
        this.deleteEhdModels = deleteEhdModels;
    }
}
