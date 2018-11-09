package com.visirx.patient.api;

/**
 * Created by Suresh on 20-03-2016.
 */
public class ForgotPasswordOtpRes {
    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private ResponseHeader  responseHeader;
    private String userId;

}
