package com.visirx.patient.model;

import android.graphics.Bitmap;

import com.visirx.patient.api.RequestHeader;

import java.util.Date;

/**
 * Created by Suresh on 26-02-2016.
 */
public class CustomerProfileModel {


    private RequestHeader requestHeader;
    private String customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String customerMobileNumber;
    private String customerAddress;
    private String customerZipcode;
    private String customerDateOfBirth;
    private String customerGender;
    private byte[] customerPhoto;
    private String customerHeight;
    private String customerWeight;
    private String lastCreated;
    private Bitmap bitMap;
    private String user_thumimage;

    public String getUser_thumimage() {
        return user_thumimage;
    }

    public void setUser_thumimage(String user_thumimage) {
        this.user_thumimage = user_thumimage;
    }

    public Bitmap getBitMap() {
        return bitMap;
    }

    public void setBitMap(Bitmap bitMap) {
        this.bitMap = bitMap;
    }

    public String getLastCreated() {
        return lastCreated;
    }

    public void setLastCreated(String lastCreated) {
        this.lastCreated = lastCreated;
    }

    public String getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(String customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
    }

    public String getCustomerWeight() {
        return customerWeight;
    }

    public void setCustomerWeight(String customerWeight) {
        this.customerWeight = customerWeight;
    }

    public String getCustomerHeight() {
        return customerHeight;
    }

    public void setCustomerHeight(String customerHeight) {
        this.customerHeight = customerHeight;
    }


    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerZipcode() {
        return customerZipcode;
    }

    public void setCustomerZipcode(String customerZipcode) {
        this.customerZipcode = customerZipcode;
    }


    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public byte[] getCustomerPhoto() {
        return customerPhoto;
    }

    public void setCustomerPhoto(byte[] customerPhoto) {
        this.customerPhoto = customerPhoto;
    }


}
