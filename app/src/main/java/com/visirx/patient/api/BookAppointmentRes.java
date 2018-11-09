package com.visirx.patient.api;

/**
 * Created by Suresh on 27-02-2016.
 */
public class BookAppointmentRes {

    private ResponseHeader responseHeader;
    private String appointmentId;

    private String bookedSlot;
    private String requestType;
    private String MID;
    private String cusId;
    private String channelId;
    private String industryTypeId;
    private String website;
    private String checkSumHash;
    private String theme;
    private String performerFee;
    private String visiRxFee;
    private String totalFee;
    private String serviceTax;
    private String generateChecksumUrl;
    private String verifyChecksumUrl;

    public String getPerformerFee() {
        return performerFee;
    }

    public void setPerformerFee(String performerFee) {
        this.performerFee = performerFee;
    }

    public String getGenerateChecksumUrl() {
        return generateChecksumUrl;
    }

    public void setGenerateChecksumUrl(String generateChecksumUrl)
    {
        this.generateChecksumUrl = generateChecksumUrl;
    }

    public String getVerifyChecksumUrl() {
        return verifyChecksumUrl;
    }

    public void setVerifyChecksumUrl(String verifyChecksumUrl) {
        this.verifyChecksumUrl = verifyChecksumUrl;
    }

    public String getVisiRxFee() {
        return visiRxFee;
    }

    public void setVisiRxFee(String visiRxFee) {
        this.visiRxFee = visiRxFee;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(String serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getBookedSlot() {
        return bookedSlot;
    }

    public void setBookedSlot(String bookedSlot) {
        this.bookedSlot = bookedSlot;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getMID() {
        return MID;
    }

    public void setMID(String MID) {
        this.MID = MID;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getIndustryTypeId() {
        return industryTypeId;
    }

    public void setIndustryTypeId(String industryTypeId) {
        this.industryTypeId = industryTypeId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCheckSumHash() {
        return checkSumHash;
    }

    public void setCheckSumHash(String checkSumHash) {
        this.checkSumHash = checkSumHash;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }
}
