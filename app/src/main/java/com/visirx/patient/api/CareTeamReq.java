package com.visirx.patient.api;

/**
 * Created by Suresh on 09-03-2016.
 */
public class CareTeamReq {
    private String customerId;
    private RequestHeader requestHeader;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }
}
