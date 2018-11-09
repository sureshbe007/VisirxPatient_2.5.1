package com.visirx.patient.api;

/**
 * Created by aa on 20.1.16.
 */
public class AppointmentNotesRes {
    private ResponseHeader responseHeader;
    private String createdAtServer;


    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getCreatedAtServer() {
        return createdAtServer;
    }

    public void setCreatedAtServer(String createdAtServer) {
        this.createdAtServer = createdAtServer;
    }
}
