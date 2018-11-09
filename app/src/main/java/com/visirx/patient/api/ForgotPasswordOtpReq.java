package com.visirx.patient.api;

/**
 * Created by Suresh on 20-03-2016.
 */
public class ForgotPasswordOtpReq {

    private String keyword;  // the same keyword which the user enters from the first screen.
    private int otp;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }
}
