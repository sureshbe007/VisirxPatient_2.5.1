package com.visirx.patient.api;

import com.visirx.patient.model.AppEmrFilesModel;

/**
 * Created by aa on 20.1.16.
 */
public class EMRFileRes {


    private ResponseHeader responseHeader;
    private AppEmrFilesModel model;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public AppEmrFilesModel getModel() {
        return model;
    }

    public void setModel(AppEmrFilesModel model) {
        this.model = model;
    }
}
