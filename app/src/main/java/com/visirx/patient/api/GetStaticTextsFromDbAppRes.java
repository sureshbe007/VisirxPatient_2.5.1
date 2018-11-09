package com.visirx.patient.api;

/**
 * Created by Suresh on 28-03-2016.
 */
public class GetStaticTextsFromDbAppRes {
    private ResponseHeader responseHeader;  //  responsecode and response message from server
    private String responseString;   // result for the requested action.
    private String responseStringLastUpdated; // last updated timestamp of the  result for the requested action.

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public String getResponseStringLastUpdated() {
        return responseStringLastUpdated;
    }

    public void setResponseStringLastUpdated(String responseStringLastUpdated) {
        this.responseStringLastUpdated = responseStringLastUpdated;
    }
}
