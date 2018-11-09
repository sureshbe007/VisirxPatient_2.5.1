package com.visirx.patient.com.visirx.patient.adapter;

import com.visirx.patient.model.CustomerProfileModel;
import com.visirx.patient.model.FindDoctorModel;

import java.util.List;

/**
 * Created by Suresh on 05-03-2016.
 */
public interface FindDoctorCallback {

    void doctorlist(List<FindDoctorModel> findDoctorModels,int responseCode);
}
