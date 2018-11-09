package com.visirx.patient.api;

import com.visirx.patient.model.AddEmrFileModel;

import java.util.List;

/**
 * Created by Suresh on 23-02-2016.
 */
public class AddEmrFileReq {

    private RequestHeader requestHeader;
    private List<AddEmrFileModel> addEmrFileModel;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public List<AddEmrFileModel> getAddEmrFileModel() {
        return addEmrFileModel;
    }

    public void setAddEmrFileModel(List<AddEmrFileModel> addEmrFileModel) {
        this.addEmrFileModel = addEmrFileModel;
    }
}
