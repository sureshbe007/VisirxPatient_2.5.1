package com.visirx.patient.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.visirx.patient.R;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;

/**
 * Created by Suresh on 17-02-2016.
 */
public class RecyclerViewHolder_AddDoctors extends RecyclerView.ViewHolder {
    private Context context;


    public NormalFont Dsecription, speciallist, visirxID, doctorFees;
    public MediumFont name;
    public ImageView imageView;
    public Button addbtn;

    public RecyclerViewHolder_AddDoctors(View itemView) {
        super(itemView);
        name = (MediumFont) itemView.findViewById(R.id.Doctorname);
        Dsecription = (NormalFont) itemView.findViewById(R.id.Dsecription);
        speciallist = (NormalFont) itemView.findViewById(R.id.Specialist);
        visirxID = (NormalFont) itemView.findViewById(R.id.DocotrID);
        doctorFees = (NormalFont) itemView.findViewById(R.id.DoctorFees);
        addbtn = (Button) itemView.findViewById(R.id.addbutton);

        imageView = (ImageView) itemView.findViewById(R.id.add_list_avatar1);
    }
}
