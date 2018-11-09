package com.visirx.patient.api;

/**
 * Created by Suresh on 20-02-2016.
 */
public class FindDoctorReq {


    private String DoctorId;

    private RequestHeader requestHeader;

    public String getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }
}
