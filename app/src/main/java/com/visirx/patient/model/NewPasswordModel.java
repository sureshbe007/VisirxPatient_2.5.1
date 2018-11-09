package com.visirx.patient.model;

import com.visirx.patient.api.RequestHeader;

/**
 * Created by Suresh on 20-03-2016.
 */
public class NewPasswordModel {

    private RequestHeader requestHeader; // set the userId of the app user which you got from                                                                                                           server in servlet 2 response.
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
