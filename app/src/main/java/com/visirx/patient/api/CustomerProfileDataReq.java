package com.visirx.patient.api;

import java.util.Date;

/**
 * Created by Suresh on 26-02-2016.
 */
public class CustomerProfileDataReq {

    private RequestHeader requestHeader;
    private String customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerAddress;
    private String customerZipcode;
    private String customerDateOfBirth;  // date format should be "2016-02-22"
    private String customerGender; // values should be "MALE" or "FEMALE"
    private byte[] customerPhoto;
    private String lastCreated;

    // Suresh
    private String customeremail;
    private String customer_mobilenumber;
    private String customer_height;
    private String customer_weight;


    public String getCustomeremail() {
        return customeremail;
    }

    public void setCustomeremail(String customeremail) {
        this.customeremail = customeremail;
    }

    public String getCustomer_mobilenumber() {
        return customer_mobilenumber;
    }

    public void setCustomer_mobilenumber(String customer_mobilenumber) {
        this.customer_mobilenumber = customer_mobilenumber;
    }

    public String getCustomer_height() {
        return customer_height;
    }

    public void setCustomer_height(String customer_height) {
        this.customer_height = customer_height;
    }

    public String getCustomer_weight() {
        return customer_weight;
    }

    public void setCustomer_weight(String customer_weight) {
        this.customer_weight = customer_weight;
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

    public String getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(String customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
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

    public String getLastCreated() {
        return lastCreated;
    }

    public void setLastCreated(String lastCreated) {
        this.lastCreated = lastCreated;
    }
}
