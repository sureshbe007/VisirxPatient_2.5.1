package com.visirx.patient.api;

import com.visirx.patient.model.LoginModel;
import com.visirx.patient.model.RegisterModel;

import java.util.List;

/**
 * Created by aa on 18.1.16.
 */
public class LoginRes {

    private ResponseHeader responseHeader;
    private LoginModel loginModel;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }
}
