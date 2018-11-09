package com.visirx.patient.api;

import com.visirx.patient.model.ParamedicDetailsModel;

/**
 * Created by Suresh on 22-02-2016.
 */
public class ParamedicDetailsRes {

    private ResponseHeader responseHeader;
    private ParamedicDetailsModel paramedicDetailsModel;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public ParamedicDetailsModel getParamedicDetailsModel() {
        return paramedicDetailsModel;
    }

    public void setParamedicDetailsModel(ParamedicDetailsModel paramedicDetailsModel) {
        this.paramedicDetailsModel = paramedicDetailsModel;
    }
}
