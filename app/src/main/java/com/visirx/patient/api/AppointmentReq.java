package com.visirx.patient.api;

/**
 * Created by aa on 20.1.16.
 */
public class AppointmentReq {

    private String patientId;
    private RequestHeader requestHeader;
    private String appointmentid;
    private String reservationnumber;

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

    public String getReservationnumber() {
        return reservationnumber;
    }

    public void setReservationnumber(String reservationnumber) {
        this.reservationnumber = reservationnumber;
    }
}
