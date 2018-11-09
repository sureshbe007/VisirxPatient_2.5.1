package com.visirx.patient.api;

import com.visirx.patient.model.RegisterModel;

import java.util.List;

/**
 * Created by aa on 18.1.16.
 */
public class RegisterRes {

    private ResponseHeader responseHeader;
    private List<RegisterModel> registerModels;

    public List<RegisterModel> getRegisterModels() {
        return registerModels;
    }

    public void setRegisterModels(List<RegisterModel> registerModels) {
        this.registerModels = registerModels;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }


}
