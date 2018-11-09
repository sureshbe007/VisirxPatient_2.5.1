package com.visirx.patient.api;

import com.visirx.patient.model.CustomerProfileModel;

import java.util.List;

/**
 * Created by Suresh on 26-02-2016.
 */
public class CustomerProfileDataRes {

    private ResponseHeader responseHeader;
    private List<CustomerProfileModel> customerProfileModel;

    public List<CustomerProfileModel> getCustomerProfileModel() {
        return customerProfileModel;
    }

    public void setCustomerProfileModel(List<CustomerProfileModel> customerProfileModel) {
        this.customerProfileModel = customerProfileModel;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }


}
