package com.visirx.patient.api;

import com.visirx.patient.model.OtpModel;

/**
 * Created by Suresh on 04-02-2016.
 */
public class OtpRes {

    private ResponseHeader responseHeader;
    private String userId;

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
}
