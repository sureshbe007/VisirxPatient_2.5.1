package com.visirx.patient.model;

import java.util.Date;

/**
 * Created by Suresh on 27-02-2016.
 */
public class BookAppointmentModel {
    private String performerId;
    private String customerAddress;
    private String customerSymptoms; //optional field
    private String appointmentType; // virtual / walkin
    private String customerZipcode;
    private Date reservationDate;
    private Date reservedTimeslot;
    private int paidAmount;

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

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Date getReservedTimeslot() {
        return reservedTimeslot;
    }

    public void setReservedTimeslot(Date reservedTimeslot) {
        this.reservedTimeslot = reservedTimeslot;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }
}
