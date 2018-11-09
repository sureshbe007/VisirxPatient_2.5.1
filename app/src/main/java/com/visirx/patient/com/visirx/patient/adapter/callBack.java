package com.visirx.patient.com.visirx.patient.adapter;

import com.visirx.patient.model.AvailableTimeslotsModel;

import java.util.List;

/**
 * Created by Suresh on 29-02-2016.
 */
public interface callBack {
    void dataLoad(List<AvailableTimeslotsModel> list, String reservationMode, int responsecode, boolean status,String MainOffer,String SubOffer,boolean isNoFee);
}
