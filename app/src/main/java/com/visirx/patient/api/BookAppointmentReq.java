package com.visirx.patient.api;

import java.util.Date;

/**
 * Created by Suresh on 27-02-2016.
 */
public class BookAppointmentReq {

    private RequestHeader requestHeader;
    private String performerId;
    private String customerAddress;
    private String customerSymptoms; //optional field
    private String appointmentType; // virtual / walkin
    private String customerZipcode;
    private String reservationDate;
    private String reservedTimeslot;
    private int paidAmount;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReservedTimeslot() {
        return reservedTimeslot;
    }

    public void setReservedTimeslot(String reservedTimeslot) {
        this.reservedTimeslot = reservedTimeslot;
    }

    public String getPerformerId() {
        return performerId;
    }

    public void setPerformerId(String performerId) {
        this.performerId = performerId;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerSymptoms() {
        return customerSymptoms;
    }

    public void setCustomerSymptoms(String customerSymptoms) {
        this.customerSymptoms = customerSymptoms;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getCustomerZipcode() {
        return customerZipcode;
    }

    public void setCustomerZipcode(String customerZipcode) {
        this.customerZipcode = customerZipcode;
    }


    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }
}
