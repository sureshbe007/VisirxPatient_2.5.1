package com.visirx.patient.api;

import com.visirx.patient.model.FindDoctorModel;

import java.util.List;

/**
 * Created by Suresh on 20-02-2016.
 */
public class FindDoctorRes {

    private ResponseHeader responseHeader;
    private List<FindDoctorModel> findDoctorModel;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<FindDoctorModel> getFindDoctorModel() {
        return findDoctorModel;
    }

    public void setFindDoctorModel(List<FindDoctorModel> findDoctorModel) {
        this.findDoctorModel = findDoctorModel;
    }
}
