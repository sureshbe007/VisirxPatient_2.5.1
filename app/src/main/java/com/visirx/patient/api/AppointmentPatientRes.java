package com.visirx.patient.api;

import com.visirx.patient.model.AppointmentPatientModel;

import java.util.List;

/**
 * Created by Suresh on 03-03-2016.
 */
public class AppointmentPatientRes {

    private ResponseHeader responseHeader;
    private List<AppointmentPatientModel> apptModel;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<AppointmentPatientModel> getApptModel() {
        return apptModel;
    }

    public void setApptModel(List<AppointmentPatientModel> apptModel) {
        this.apptModel = apptModel;
    }
}
