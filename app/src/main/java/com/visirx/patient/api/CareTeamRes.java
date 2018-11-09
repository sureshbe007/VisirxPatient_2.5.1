package com.visirx.patient.api;

import com.visirx.patient.model.CareTeamModel;

import java.util.List;

/**
 * Created by Suresh on 09-03-2016.
 */
public class CareTeamRes {

    private ResponseHeader responseHeader;
    private List<CareTeamModel> apptModel;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<CareTeamModel> getApptModel()
    {
        return apptModel;
    }

    public void setApptModel(List<CareTeamModel> apptModel) {
        this.apptModel = apptModel;
    }
}
