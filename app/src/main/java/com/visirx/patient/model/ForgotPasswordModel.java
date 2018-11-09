package com.visirx.patient.model;

/**
 * Created by Suresh on 20-03-2016.
 */
public class ForgotPasswordModel
{

    private String keyword;
    private int otp;

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
