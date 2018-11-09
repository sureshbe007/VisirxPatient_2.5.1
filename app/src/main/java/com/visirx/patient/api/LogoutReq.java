package com.visirx.patient.api;

/**
 * Created by Suresh on 30-05-2016.
 */
public class LogoutReq {

    private RequestHeader requestHeader;
    private String gcmId;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }
}
