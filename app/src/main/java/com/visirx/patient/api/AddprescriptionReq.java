package com.visirx.patient.api;

import com.visirx.patient.model.ApptPrescriptionModel;

import java.util.List;

/**
 * Created by aa on 20.1.16.
 */
public class AddprescriptionReq {


    private RequestHeader requestHeader;
    private List<ApptPrescriptionModel> prescriptionModelList;

    public int getPrescriptionCount() {
        return prescriptionCount;
    }

    public void setPrescriptionCount(int prescriptionCount) {
        this.prescriptionCount = prescriptionCount;
    }

    private int prescriptionCount;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public List<ApptPrescriptionModel> getPrescriptionModelList() {
        return prescriptionModelList;
    }

    public void setPrescriptionModelList(
            List<ApptPrescriptionModel> prescriptionModelList) {
        this.prescriptionModelList = prescriptionModelList;
    }


}
