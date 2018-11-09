package com.visirx.patient.api;

/**
 * Created by Suresh on 04-04-2016.
 */
public class DeleteDoctorRes {

    private String DoctorID;
    private ResponseHeader responseHeader;

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }
}
