package com.visirx.patient.api;

/**
 * Created by aa on 18.1.16.
 */
public class LoginReq {


    private RequestHeader requestHeader;
    private String userName;
    private String password;
    private int statusFlag;
    private String gcmid;

    public String getGcmid() {
        return gcmid;
    }

    public void setGcmid(String gcmid) {
        this.gcmid = gcmid;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }
}
