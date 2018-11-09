package com.visirx.patient.api;

/**
 * Created by Suresh on 04-04-2016.
 */
public class DeleteDoctorReq {

    private String DoctorID;
    private RequestHeader requestHeader;

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }
}
