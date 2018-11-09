package com.visirx.patient.api;

/**
 * Created by aa on 20.1.16.
 */
public class EMRFullImageRes {

    private ResponseHeader responseHeader;
    private byte[] emrFullImageFile;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public byte[] getEmrFullImageFile() {
        return emrFullImageFile;
    }

    public void setEmrFullImageFile(byte[] emrFullImageFile) {
        this.emrFullImageFile = emrFullImageFile;
    }
}
