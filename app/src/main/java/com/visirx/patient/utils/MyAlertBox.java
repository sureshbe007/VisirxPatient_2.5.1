package com.visirx.patient.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.visirx.patient.R;
import com.visirx.patient.customview.MediumFont;

/**
 * Created by Suresh on 30-05-2016.
 */
public class MyAlertBox {


    public Context context;
    public  MyAlertBox(Context context) {
        this.context = context;
    }

    public void InfoAlerBox(String alertHeading, String alertMessage,String buttonName) {


        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.fileattach_alertbox, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        Button positive = (Button) promptsView.findViewById(R.id.info_positive);
        positive.setText(buttonName);


        MediumFont heading = (MediumFont)promptsView.findViewById(R.id.info_heading);
        heading.setText(alertHeading);

        TextView message = (TextView)promptsView.findViewById(R.id.intfo_message);
        message.setText(alertMessage);

        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }

        });

    }
}
