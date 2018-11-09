package com.visirx.patient.api;

import com.visirx.patient.model.PerformerWorkingTimeModel;

import java.util.List;

/**
 * Created by Suresh on 19-03-2016.
 */
public class PerformerDetailsRes {


    private ResponseHeader responseHeader;
    private String performerId;
    private String clinicalAddress;
    private String clinicalZipcode;
    private List<PerformerWorkingTimeModel> performerWorkingTimeModel;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getPerformerId() {
        return performerId;
    }

    public void setPerformerId(String performerId) {
        this.performerId = performerId;
    }

    public String getClinicalAddress() {
        return clinicalAddress;
    }

    public void setClinicalAddress(String clinicalAddress) {
        this.clinicalAddress = clinicalAddress;
    }

    public String getClinicalZipcode() {
        return clinicalZipcode;
    }

    public void setClinicalZipcode(String clinicalZipcode) {
        this.clinicalZipcode = clinicalZipcode;
    }

    public List<PerformerWorkingTimeModel> getPerformerWorkingTimeModel() {
        return performerWorkingTimeModel;
    }

    public void setPerformerWorkingTimeModel(List<PerformerWorkingTimeModel> performerWorkingTimeModel) {
        this.performerWorkingTimeModel = performerWorkingTimeModel;
    }
}
