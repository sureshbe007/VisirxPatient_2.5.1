package com.visirx.patient.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.visirx.patient.R;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;

/**
 * Created by Suresh on 15-03-2016.
 */
public class RecyclerViewHolder_Cancelappointment extends RecyclerView.ViewHolder {

    public MediumFont name,Appoint_type;
    public NormalFont status, speciallist, DoctorID,appointmentid,Date, time, Appointstatus,AppointHead;
    public ImageView imageView;

    public RecyclerViewHolder_Cancelappointment(View itemView) {
        super(itemView);
        name = (MediumFont) itemView.findViewById(R.id.Docotor_name);
        status = (NormalFont) itemView.findViewById(R.id.Dsecription);
        speciallist = (NormalFont) itemView.findViewById(R.id.Specialist);
        DoctorID = (NormalFont) itemView.findViewById(R.id.DocotrID);
        appointmentid = (NormalFont) itemView.findViewById(R.id.cancel_appointID);
        Date = (NormalFont) itemView.findViewById(R.id.AppointDate);
        time = (NormalFont) itemView.findViewById(R.id.AppointTime);
        AppointHead = (NormalFont) itemView.findViewById(R.id.AppointTime1);
        Appoint_type = (MediumFont) itemView.findViewById(R.id.Appointtype);
        Appointstatus = (NormalFont) itemView.findViewById(R.id.Appointstatus);
        imageView = (ImageView) itemView.findViewById(R.id.all_can_list_avatar1);
    }


}
