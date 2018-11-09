package com.visirx.patient.api;

/**
 * Created by Suresh on 20-03-2016.
 */
public class ForgotNewpasswordReq {

    private RequestHeader requestHeader;

    private String newPassword;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
