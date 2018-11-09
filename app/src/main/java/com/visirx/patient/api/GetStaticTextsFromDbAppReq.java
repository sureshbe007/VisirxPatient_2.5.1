package com.visirx.patient.api;

/**
 * Created by Suresh on 28-03-2016.
 */
public class GetStaticTextsFromDbAppReq {
    private RequestHeader requestHeader;   //set the logged in user id
    private String action;  // action name to retrieve static data.Names to be used are listed below.
    private String lastUpdatedFromClient;  // last updated timestamp  for the action name.pass null if its for the first time.

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLastUpdatedFromClient() {
        return lastUpdatedFromClient;
    }

    public void setLastUpdatedFromClient(String lastUpdatedFromClient) {
        this.lastUpdatedFromClient = lastUpdatedFromClient;
    }
}
