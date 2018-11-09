package com.visirx.patient.com.visirx.patient.adapter;

import com.visirx.patient.model.PerformerWorkingTimeModel;

import java.util.List;

/**
 * Created by Suresh on 19-03-2016.
 */
public interface DoctorProfileCallBack {

    void DoctorProfile(String id,String address,String zipcode ,List<PerformerWorkingTimeModel> performerWorkingTimeModels);
}
