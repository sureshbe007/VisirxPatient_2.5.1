package com.visirx.patient;

import com.visirx.patient.api.RequestHeader;

/**
 * Created by Suresh on 22-02-2016.
 */
public class ParamedicDetailsReq {

    private RequestHeader requestHeader;
    private String patientId;
    private int appointmentId;
    private String nurseLastUpdated;
    private int isNurseAssigned;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
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

    public String getNurseLastUpdated() {
        return nurseLastUpdated;
    }

    public void setNurseLastUpdated(String nurseLastUpdated) {
        this.nurseLastUpdated = nurseLastUpdated;
    }

    public int getIsNurseAssigned() {
        return isNurseAssigned;
    }

    public void setIsNurseAssigned(int isNurseAssigned) {
        this.isNurseAssigned = isNurseAssigned;
    }
}
