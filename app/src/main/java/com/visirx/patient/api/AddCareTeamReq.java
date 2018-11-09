package com.visirx.patient.api;

/**
 * Created by Suresh on 09-03-2016.
 */
public class AddCareTeamReq {
    private RequestHeader requestHeader;
    private String perfomerId;
    private String customerId;
    private String createdAt;
    private String createdById;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getPerfomerId() {
        return perfomerId;
    }

    public void setPerfomerId(String perfomerId) {
        this.perfomerId = perfomerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }
}
