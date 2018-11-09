package com.visirx.patient.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.visirx.patient.R;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;

/**
 * Created by Suresh on 09-02-2016.
 */
public class RecyclerViewHolder_Allappointment extends RecyclerView.ViewHolder {


    public ImageView imageView;
    public Button cancelappoint;
    public NormalFont Dsecription, Specialist, DocotrID, appointID, AppointDate, AppointTime, Appointstatus,AppointHead;
    public MediumFont appoint_name, Appoint_type;

    public RecyclerViewHolder_Allappointment(View itemView) {
        super(itemView);
        appoint_name = (MediumFont) itemView.findViewById(R.id.appoint_name);
        Dsecription = (NormalFont) itemView.findViewById(R.id.Dsecription);
        Specialist = (NormalFont) itemView.findViewById(R.id.Specialist);
        DocotrID = (NormalFont) itemView.findViewById(R.id.DocotrID);
        appointID = (NormalFont) itemView.findViewById(R.id.appointID);
        AppointDate = (NormalFont) itemView.findViewById(R.id.AppointDate);
        AppointTime = (NormalFont) itemView.findViewById(R.id.AppointTime);
        Appoint_type = (MediumFont) itemView.findViewById(R.id.Appointtype);
        Appointstatus = (NormalFont) itemView.findViewById(R.id.Appointstatus);
        AppointHead = (NormalFont) itemView.findViewById(R.id.AppointTime1);
        cancelappoint = (Button) itemView.findViewById(R.id.cancel);
        imageView = (ImageView) itemView.findViewById(R.id.list_avatar1);

    }
}
