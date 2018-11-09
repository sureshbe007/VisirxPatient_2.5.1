package com.visirx.patient.api;

/**
 * Created by aa on 20.1.16.
 */
public class AppointmentNotesListReq {

    private RequestHeader requestHeader;
    private int appointmentId;
    private String patientId;

    private String notesLastUpdated;


    public String getNotesLastUpdated() {
        return notesLastUpdated;
    }

    public void setNotesLastUpdated(String notesLastUpdated) {
        this.notesLastUpdated = notesLastUpdated;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
