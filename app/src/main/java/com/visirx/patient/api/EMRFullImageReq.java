package com.visirx.patient.api;

import java.util.Date;

/**
 * Created by aa on 20.1.16.
 */
public class EMRFullImageReq {

    private RequestHeader requestHeader;
    private Date createdAt;
    private String patientId;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
