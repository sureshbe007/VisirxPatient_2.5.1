package com.visirx.patient.api;

/**
 * Created by Suresh on 27-02-2016.
 */
public class GetTimeslotsForPerformerReq {

    private RequestHeader requestHeader;
    private String performerId;
    private String appointmentDate;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getPerformerId() {
        return performerId;
    }

    public void setPerformerId(String performerId) {
        this.performerId = performerId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
