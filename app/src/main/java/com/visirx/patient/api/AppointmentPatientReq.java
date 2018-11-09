package com.visirx.patient.api;

/**
 * Created by Suresh on 03-03-2016.
 */
public class AppointmentPatientReq {

    private String patientId;
    private String appointmentid;
    private RequestHeader requestHeader;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAppointmentid() {
        return appointmentid;
    }

    public void setAppointmentid(String appointmentid) {
        this.appointmentid = appointmentid;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }
}
