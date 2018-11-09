package com.visirx.patient.api;

import com.visirx.patient.model.AppDigitalprescriptionModel;

import java.util.ArrayList;

/**
 * Created by Lenovo on 6/29/2016.
 */
public class DigitalPrescriptionListRes {

    private ResponseHeader responseHeader;
    private ArrayList<AppDigitalprescriptionModel> dpGetResData;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public ArrayList<AppDigitalprescriptionModel> getDpGetResData() {
        return dpGetResData;
    }

    public void setDpGetResData(ArrayList<AppDigitalprescriptionModel> dpGetResData) {
        this.dpGetResData = dpGetResData;
    }
}
