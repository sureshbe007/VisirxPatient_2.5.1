package com.visirx.patient.model;

/**
 * Created by aa on 20.1.16.
 */
public class EMRModel {
    public EMRModel(String header, String result, String date, boolean syched) {
        super();
        this.header = header;
        this.result = result;
        this.date = date;
        this.syched = syched;
    }

    public EMRModel() {
        // TODO Auto-generated constructor stub
    }

    private String header;
    private String result;
    private String date;
    private boolean syched;
    private String emrDate;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSyched() {
        return syched;
    }

    public void setSyched(boolean syched) {
        this.syched = syched;
    }

    public String getEmrDate() {
        return emrDate;
    }

    public void setEmrDate(String emrDate) {
        this.emrDate = emrDate;
    }
}