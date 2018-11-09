package com.visirx.patient.api;

/**
 * Created by aa on 20.1.16.
 */
public class EMRFileReq {

    private RequestHeader requestHeader;
    private String appointmentId;
    private String patientId;

    private String EmrFilesLastUpdated;
    private String EmrVitalsLastUpdated;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getEmrFilesLastUpdated() {
        return EmrFilesLastUpdated;
    }

    public void setEmrFilesLastUpdated(String emrFilesLastUpdated) {
        EmrFilesLastUpdated = emrFilesLastUpdated;
    }

    public String getEmrVitalsLastUpdated() {
        return EmrVitalsLastUpdated;
    }

    public void setEmrVitalsLastUpdated(String emrVitalsLastUpdated) {
        EmrVitalsLastUpdated = emrVitalsLastUpdated;
    }
}
