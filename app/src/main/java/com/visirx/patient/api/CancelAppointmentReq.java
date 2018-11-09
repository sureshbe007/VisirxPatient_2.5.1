package com.visirx.patient.api;

import com.visirx.patient.model.CancelAppointmentModel;

import java.util.List;

/**
 * Created by Suresh on 07-03-2016.
 */
public class CancelAppointmentReq {

    private RequestHeader requestHeader;
    private List<CancelAppointmentModel> caModelList;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public List<CancelAppointmentModel> getCaModelList() {
        return caModelList;
    }

    public void setCaModelList(List<CancelAppointmentModel> caModelList) {
        this.caModelList = caModelList;
    }
}
