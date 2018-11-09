package com.visirx.patient.api;

/**
 * Created by Suresh on 15-02-2016.
 */
public class AppointmentParamedicReq {

    private String patientId;
    private RequestHeader requestHeader;
    private String appointmentid;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getAppointmentid() {
        return appointmentid;
    }

    public void setAppointmentid(String appointmentid) {
        this.appointmentid = appointmentid;
    }
}
