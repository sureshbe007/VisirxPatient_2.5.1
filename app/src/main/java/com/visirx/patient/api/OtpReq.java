package com.visirx.patient.api;

/**
 * Created by Suresh on 04-02-2016.
 */
public class OtpReq {

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    private String userName;
    private int otp;


}
