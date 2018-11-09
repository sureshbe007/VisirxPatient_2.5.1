package com.visirx.patient.com.visirx.patient.adapter;

import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.FindDoctorModel;

import java.util.List;

/**
 * Created by Suresh on 07-03-2016.
 */
public interface AllappointmentListCallback {


    void AllappointmentLoad(List<FindDoctorModel> findDoctorModels, List<AppointmentPatientModel> appointmentPatientModels);
}
