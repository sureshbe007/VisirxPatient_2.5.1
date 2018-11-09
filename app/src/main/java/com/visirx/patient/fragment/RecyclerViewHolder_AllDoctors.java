package com.visirx.patient.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.visirx.patient.R;
import com.visirx.patient.activity.BookAppointActivity;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;

/**
 * Created by Suresh on 09-02-2016.
 */
public class RecyclerViewHolder_AllDoctors extends RecyclerView.ViewHolder {
    private Context context;


    public ImageView imageView;
    public Button consultbtn;
    public ImageView deletebtn;
    public MediumFont docor_name;
    public NormalFont doctor_status, doctor_speciallist, doctor_visiRxID, Doctor_fees;


    public RecyclerViewHolder_AllDoctors(View itemView) {
        super(itemView);
        docor_name = (MediumFont) itemView.findViewById(R.id.Doctorname);
        doctor_status = (NormalFont) itemView.findViewById(R.id.Dsecription);
        doctor_speciallist = (NormalFont) itemView.findViewById(R.id.Specialist);
        doctor_visiRxID = (NormalFont) itemView.findViewById(R.id.DocotrID);
        Doctor_fees = (NormalFont) itemView.findViewById(R.id.DoctorFees);
        consultbtn = (Button) itemView.findViewById(R.id.consult);
        deletebtn = (ImageView) itemView.findViewById(R.id.deleteImage);
        imageView = (ImageView) itemView.findViewById(R.id.all_doc_list_avatar1);

    }

}
