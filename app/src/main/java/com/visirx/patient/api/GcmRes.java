package com.visirx.patient.api;

import com.visirx.patient.model.GcmModel;

/**
 * Created by Suresh on 05-02-2016.
 */
public class GcmRes {
    private ResponseHeader responseHeader;
    private GcmModel gcmModel;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public GcmModel getGcmModel() {
        return gcmModel;
    }

    public void setGcmModel(GcmModel gcmModel) {
        this.gcmModel = gcmModel;
    }
}
