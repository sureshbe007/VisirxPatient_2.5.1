package com.visirx.patient.api;

import com.visirx.patient.model.AppointmentModel;

import java.util.List;

/**
 * Created by aa on 20.1.16.
 */
public class AppointmentRes {
    private ResponseHeader responseHeader;
    private List<AppointmentModel> apptModel;


    public AppointmentRes() {
        // TODO Auto-generated constructor stub
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<AppointmentModel> getAppointmentModel() {
        return apptModel;
    }

    public void setAppointmentModel(List<AppointmentModel> apptModel) {
        this.apptModel = apptModel;
    }
}
