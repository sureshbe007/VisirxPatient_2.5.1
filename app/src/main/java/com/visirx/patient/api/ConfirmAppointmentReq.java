package com.visirx.patient.api;

import com.visirx.patient.model.ConfirmAppointmentModel;

/**
 * Created by reveilleadmin on 3/12/2016.
 */
public class ConfirmAppointmentReq {
    private RequestHeader requestHeader;
    private ConfirmAppointmentModel confirmAppointmentModel;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }


    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public ConfirmAppointmentModel getConfirmAppointmentModel() {
        return confirmAppointmentModel;
    }

    public void setConfirmAppointmentModel(ConfirmAppointmentModel confirmAppointmentModel) {
        this.confirmAppointmentModel = confirmAppointmentModel;
    }
}
