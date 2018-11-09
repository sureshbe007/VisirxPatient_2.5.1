package com.visirx.patient.api;

import com.visirx.patient.model.CloseAppointmentModel;

import java.util.List;

/**
 * Created by aa on 20.1.16.
 */
public class CloseAppointmentReq {

    private RequestHeader requestHeader;

    private List<CloseAppointmentModel> coModelList;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public List<CloseAppointmentModel> getCoModelList() {
        return coModelList;
    }

    public void setCoModelList(List<CloseAppointmentModel> coModelList) {
        this.coModelList = coModelList;
    }
}
