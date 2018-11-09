package com.visirx.patient.api;

import com.visirx.patient.model.AppointmentParamedicModel;

import java.util.List;

/**
 * Created by Suresh on 15-02-2016.
 */
public class AppointmentParamedicRes {

    private ResponseHeader responseHeader;
    private List<AppointmentParamedicModel> apptModel;

    public AppointmentParamedicRes() {
        // TODO Auto-generated constructor stub
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<AppointmentParamedicModel> getApptModel() {
        return apptModel;
    }

    public void setApptModel(List<AppointmentParamedicModel> apptModel) {
        this.apptModel = apptModel;
    }

    public List<AppointmentParamedicModel> getAppointmentModel() {
        return apptModel;
    }

    public void setAppointmentModel(List<AppointmentParamedicModel> apptModel) {
        this.apptModel = apptModel;
    }
}
