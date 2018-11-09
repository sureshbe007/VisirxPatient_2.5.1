package com.visirx.patient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.visirx.patient.R;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AppointmentParamedicModel;
import com.visirx.patient.utils.VTConstants;

public class PatientBaseActivity extends AppCompatActivity {

    String TAG = PatientBaseActivity.class.getName();

    protected TextView txtHeading;
    ImageView callImageView = null;
    ImageView addImageView = null;
    String mobileNumber = null;
    String header = "";
    int position = -1;
    AppointmentParamedicModel model;
    TextView txtheaderId = null;
    AlertDialog.Builder builder = null;
    CharSequence options[] = new CharSequence[]{"Image", "Auscultation"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_patient_base);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.activity_patient_base);

			/*TextView textView = (TextView)findViewById(R.id.title_bar);
            textView.setText("Add Notes");
			 */
            ImageView imageView = (ImageView) findViewById(R.id.backView);
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            callImageView = (ImageView) findViewById(R.id.call_image);
            callImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        if (mobileNumber != null) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + mobileNumber));
//                            startActivity(callIntent);
                        }
                    } catch (Exception e) {
                        LogTrace.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
            addImageView = (ImageView) findViewById(R.id.add_image);
            addImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (position == 1) { //Notes
                        builder = new AlertDialog.Builder(PatientBaseActivity.this);
                        builder.setTitle("Select file type");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on options[which]
                                String option = (String) options[which];
                                if (option.equalsIgnoreCase("Text")) {
                                    Intent intent = new Intent(getBaseContext(), VitalsActivity.class);
                                    intent.putExtra(VTConstants.APPTMODEL_KEY, model.getReservationNumber());
                                    startActivity(intent);
                                } else if (option.equalsIgnoreCase("Image")) {
                                    Intent intent = new Intent(getBaseContext(), CaptureImageActivity.class);
                                    intent.putExtra(VTConstants.APPTMODEL_KEY, model.getReservationNumber());
                                    startActivity(intent);

                                } else if (option.equalsIgnoreCase("Auscultation")) {
                                    Intent intent = new Intent(getBaseContext(), CaptureAudioActivity.class);
                                    intent.putExtra(VTConstants.APPTMODEL_KEY, model.getReservationNumber());
                                    startActivity(intent);
                                } else if (option.equalsIgnoreCase("Video")) {
                                }
                            }
                        });
                        builder.show();
                    } else if (position == 2) { //Notes
//                        Intent intent = new  Intent(getBaseContext(), AddNotesActivity.class);
//                        intent.putExtra(VTConstants.APPTMODEL_KEY, model.getReservationNumber());
//                        startActivity(intent);
                    } else { //prescription
                        Intent intent = new Intent(getBaseContext(), AddPrescriptionActivity.class);
                        intent.putExtra(VTConstants.APPTMODEL_KEY, model.getReservationNumber());
                        startActivity(intent);

                    }
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }


    }

    protected void setAppoitmentModel(AppointmentParamedicModel apptModel) {
        this.model = apptModel;
    }

    protected void setHeading(String heading) {
        header = heading;
        if (txtHeading == null)
            txtHeading = (TextView) findViewById(R.id.title_bar);
        if (txtHeading != null)
            txtHeading.setText(heading);
    }

    protected void showCallIcon(int visible) {
        /*	if(callImageView == null)
			callImageView = (ImageView) findViewById(R.id.call_image);*/
        //	showAddIcon(View.GONE);
        if (callImageView != null)
            callImageView.setVisibility(visible);
        if (addImageView != null)
            addImageView.setVisibility(View.GONE);
    }

    protected void showAddIcon(int visible) {
		/*if(addImageView == null)
			addImageView = (ImageView) findViewById(R.id.add_image);*/
        //showCallIcon(View.GONE);
        if (addImageView != null)
            addImageView.setVisibility(visible);
        if (callImageView != null)
            callImageView.setVisibility(View.GONE);
    }

    protected void showSubText(String text, String status) {
        if (status.equalsIgnoreCase("Scheduled") == false
                && status.equalsIgnoreCase("Ready") == false
                && status.equalsIgnoreCase("NoShow") == false) {
            this.addImageView.setVisibility(View.GONE);
        }
        if (txtheaderId == null) {
            txtheaderId = (TextView) findViewById(R.id.id_content);
        }
        txtheaderId.setText(text);
    }

    protected void setMobileNumber(String number) {
        mobileNumber = number;
    }

    protected void setPosition(int number) {
        position = number;
    }
}

