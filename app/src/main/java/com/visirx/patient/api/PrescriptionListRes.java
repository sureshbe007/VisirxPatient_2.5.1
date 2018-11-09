package com.visirx.patient.api;

import com.visirx.patient.model.ApptPrescriptionModel;

import java.util.List;

/**
 * Created by aa on 20.1.16.
 */
public class PrescriptionListRes {


    private ResponseHeader responseHeader;
    private List<ApptPrescriptionModel> modelList;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<ApptPrescriptionModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<ApptPrescriptionModel> modelList) {
        this.modelList = modelList;
    }
}
