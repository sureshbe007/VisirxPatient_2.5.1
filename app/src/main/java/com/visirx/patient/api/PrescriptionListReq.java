package com.visirx.patient.api;

/**
 * Created by aa on 20.1.16.
 */
public class PrescriptionListReq {

    private RequestHeader requestHeader;
    private String patientId;
    private int appointmentId;


    private String prescriptionLastUpdated;

    public String getPrescriptionLastUpdated() {
        return prescriptionLastUpdated;
    }

    public void setPrescriptionLastUpdated(String prescriptionLastUpdated) {
        this.prescriptionLastUpdated = prescriptionLastUpdated;
    }


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
}
