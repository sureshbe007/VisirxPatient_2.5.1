package com.visirx.patient.api;

/**
 * Created by aa on 18.1.16.
 */
public class RegisterReq {

    private String UserName;
    private String Email;
    private String Phone;
    private String Pass;
    private String ConPass;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getConPass() {
        return ConPass;
    }

    public void setConPass(String conPass) {
        ConPass = conPass;
    }
}
