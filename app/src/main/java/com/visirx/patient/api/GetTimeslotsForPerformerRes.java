package com.visirx.patient.api;

import com.visirx.patient.model.AvailableTimeslotsModel;

import java.util.List;

/**
 * Created by Suresh on 27-02-2016.
 */
public class GetTimeslotsForPerformerRes {

    private ResponseHeader responseHeader;
    private List<AvailableTimeslotsModel> availableTimeslotsModel;
    private String reservationMode;
    private boolean isOfferAvailable;
    private String offerMainText;
    private String offerSubText;

    public void setOfferAvailable(boolean offerAvailable) {
        isOfferAvailable = offerAvailable;
    }

    public boolean isNoFee() {
        return isNoFee;
    }

    public void setNoFee(boolean noFee) {
        isNoFee = noFee;
    }

    private boolean isNoFee;

    public String getOfferSubText() {
        return offerSubText;
    }

    public void setOfferSubText(String offerSubText) {
        this.offerSubText = offerSubText;
    }

    public String getOfferMainText() {
        return offerMainText;
    }

    public void setOfferMainText(String offerMainText) {
        this.offerMainText = offerMainText;
    }

    public boolean isOfferAvailable() {
        return isOfferAvailable;
    }

    public void setIsOfferAvailable(boolean isOfferAvailable) {
        this.isOfferAvailable = isOfferAvailable;
    }

    public String getReservationMode() {
        return reservationMode;
    }

    public void setReservationMode(String reservationMode) {
        this.reservationMode = reservationMode;
    }

    public List<AvailableTimeslotsModel> getAvailableTimeslotsModel() {
        return availableTimeslotsModel;
    }

    public void setAvailableTimeslotsModel(List<AvailableTimeslotsModel> availableTimeslotsModel) {
        this.availableTimeslotsModel = availableTimeslotsModel;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }
}

