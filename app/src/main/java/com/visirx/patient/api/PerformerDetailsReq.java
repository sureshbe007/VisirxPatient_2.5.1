package com.visirx.patient.api;

/**
 * Created by Suresh on 19-03-2016.
 */
public class PerformerDetailsReq {

    private String performerId;
    private RequestHeader requestHeader;

    public String getPerformerId() {
        return performerId;
    }

    public void setPerformerId(String performerId) {
        this.performerId = performerId;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }
}
