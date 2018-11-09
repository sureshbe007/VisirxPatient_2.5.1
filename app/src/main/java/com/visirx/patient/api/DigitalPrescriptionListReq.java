package com.visirx.patient.api;

/**
 * Created by Lenovo on 6/29/2016.
 */
public class DigitalPrescriptionListReq {


    private RequestHeader requestHeader;
    private String patientId;
    private int appointmentId;
    private String prescriptionLastUpdated;

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

    public String getPrescriptionLastUpdated() {
        return prescriptionLastUpdated;
    }

    public void setPrescriptionLastUpdated(String prescriptionLastUpdated) {
        this.prescriptionLastUpdated = prescriptionLastUpdated;
    }
}
