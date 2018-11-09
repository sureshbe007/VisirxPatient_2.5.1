package com.visirx.patient.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.BookappointmentProvider;
import com.visirx.patient.common.provider.ConfirmAppointmentProvider;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.AppointmentModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.ConfirmAppointmentModel;
import com.visirx.patient.model.CustomerProfileModel;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class ConformAppointmentActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button BookNow;

    NormalFont doctorName, appointmentMode,
            speciality, appoint_dateTime,
            address, Symptomsone, Symptomstwo,
            txtTime;
    MediumFont txtTimeheard, Attachfile;
    FindDoctorModel findDoctorModel;
    SharedPreferences sharedPreferences;
    String DoctorID, Date, Symptmosone1, Symptmosoneone2, AppointmeMode, Time1, DoctorFees;
    CustomerProfileModel customerProfileModel;
    public boolean isnoFee;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userId;
    String imageThumb;
    byte[] ThumUserImage;
    String symptomstest;
    String PatientOffer;
    static final String TAG = ConformAppointmentActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conformappoint);
        toolbar = (Toolbar) findViewById(R.id.summary_appointtoolbar);
        setSupportActionBar(toolbar);
        if (getIntent() != null) {
            try {
                DoctorID = (getIntent().getStringExtra(VTConstants.DOCTORID));
                DoctorFees = (getIntent().getStringExtra(VTConstants.DOCTOR_FEES));
                Date = (getIntent().getStringExtra(VTConstants.APPOINT_DATE));
                Time1 = (getIntent().getStringExtra(VTConstants.APPOINTMENT_TIME));
                Symptmosone1 = (getIntent().getStringExtra(VTConstants.SYMPTOMS1));
                Symptmosoneone2 = (getIntent().getStringExtra(VTConstants.SYMPTOMS2));
                PatientOffer = (getIntent().getStringExtra(VTConstants.OFFER_APPOINTMNET));

                isnoFee = (getIntent().getExtras().getBoolean(VTConstants.ISNOFEE));
                Log.d("Conform", "isnoFee getBoolean :  " + isnoFee);


                AppointmeMode = (getIntent().getStringExtra(VTConstants.APPOINTMENT_TYPE));
                sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                // User ID
                userId = sharedPreferences.getString(VTConstants.USER_ID, null);
                findDoctorModel = VisirxApplication.customerDAO.GetCustomerDetailsForID(DoctorID);
                customerProfileModel = VisirxApplication.userRegisterDAO.getUserDetails(userId);
                doctorName = (NormalFont) findViewById(R.id.summary_docName);

                appointmentMode = (NormalFont) findViewById(R.id.etxtaptsummary_Mode);
                speciality = (NormalFont) findViewById(R.id.txtsummary_speciality2);
                appoint_dateTime = (NormalFont) findViewById(R.id.txtsummary_date2);
                address = (NormalFont) findViewById(R.id.txtsummary_address2);
                Symptomsone = (NormalFont) findViewById(R.id.txtsummary_symptoms2);
                Symptomstwo = (NormalFont) findViewById(R.id.txtsummary_symptoms3);
                txtTime = (NormalFont) findViewById(R.id.txtsummary_time1);
                txtTimeheard = (MediumFont) findViewById(R.id.txtsummary_time);
                if (Time1.contains("T")) {
                    txtTimeheard.setText("Token");
                } else {
                    txtTimeheard.setText("Time");
                }
                doctorName.setText(findDoctorModel.getDoctorFirstName() + " " + findDoctorModel.getDoctorLastName());
                speciality.setText(findDoctorModel.getDoctorSpecialization());
                address.setText(customerProfileModel.getCustomerAddress());

                String[] parts = customerProfileModel.getCustomerAddress().toString().trim().split("~");
                address.setText("");
                if (parts.length > 0) {
                    address.setText(parts[0] + "\n" + customerProfileModel.getCustomerZipcode());
                }
                if (parts.length > 1) {
                    address.setText(parts[0] + "\n" + parts[1] + "\n" + customerProfileModel.getCustomerZipcode());
                }
                if (parts.length > 2) {

                    if (parts[1].length() == 0) {

                        address.setText(parts[0] + "\n" + parts[2] + "\n" + customerProfileModel.getCustomerZipcode());
                    } else {


                        address.setText(parts[0] + "\n" + parts[1].trim() + "\n" + parts[2] + "\n" + customerProfileModel.getCustomerZipcode());
                    }
                }

                appointmentMode.setText(AppointmeMode);
                appoint_dateTime.setText(Date);
                txtTime.setText(Time1);
                Symptomsone.setText(Symptmosone1);

                if (!Symptmosoneone2.trim().toString().isEmpty()) {
                    Symptomstwo.setText(Symptmosoneone2);
                    symptomstest = Symptmosone1 + ", " + Symptmosoneone2;
                } else {

                    Symptomstwo.setText("");
                    symptomstest = Symptmosone1;
                }


                ActionBar actionBar = getSupportActionBar();
                getSupportActionBar().setTitle(R.string.appoint_summary);
            } catch (Exception e) {
                e.printStackTrace();
            }

            BookNow = (Button) findViewById(R.id.summary_bookappoint);
            BookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (VTConstants.checkAvailability(ConformAppointmentActivity.this)) {
                        try {
                            SnackBarhide();
                            // Fabric for Current Appointments
                            String DoctorName = VisirxApplication.customerDAO.getName(DoctorID);

                            EventChecking.currentAppointments(DoctorID, DoctorName, Date, Time1, ConformAppointmentActivity.this);
                            BookappointmentProvider bookappointmentProvider = new BookappointmentProvider(ConformAppointmentActivity.this);
                            bookappointmentProvider.crteateAppointmet(symptomstest, Time1, Date, customerProfileModel.getCustomerAddress(), customerProfileModel.getCustomerZipcode(), DoctorID, DoctorFees, AppointmeMode, PatientOffer, isnoFee);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    } else {
                        SnackBar();
                    }

                }
            });
//            Attachfile.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AttachFile();
//                }
//            });
        }

    }

    private void AttachFile() {
        LayoutInflater li = LayoutInflater.from(ConformAppointmentActivity.this);
        View promptsView = li.inflate(R.layout.imagechooser, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ConformAppointmentActivity.this);
        NormalFont Takephoto = (NormalFont) promptsView.findViewById(R.id.appointmentID);
        NormalFont FromLibrary = (NormalFont) promptsView.findViewById(R.id.doctorfees);
        NormalFont Cancel = (NormalFont) promptsView.findViewById(R.id.Platformfees);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        Takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
                alertDialog.hide();
            }
        });
        FromLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                alertDialog.hide();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    //     Profile Image Set  and send to Server
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
////            if (requestCode == SELECT_FILE)
////                onSelectFromGalleryResult(data);
//            if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
//        }
//    }

//    private void onCaptureImageResult(Intent data) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
//        String profileThumbImage = VTConstants.createDirectoryProfileImageThumnail();
//        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
//        imageThumb = profileThumbImage + File.separator + VTConstants.SAVED_IMAGE_PREFIX + userId + timeStamp + ".visirx";
//        try {
//            File f = null;
//            f = new File(imageThumb);
//            if (f.exists()) {
//                f.delete();
//            }
//        } catch (Exception e) {
//            LogTrace.w(TAG, "Error:::Delete existing profile - " + e.getMessage());
//        }
//        File destination = new File(imageThumb);
//        FileOutputStream fo;
//        try {
//            ThumUserImage = bytes.toByteArray();
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


    public void SnackBar() {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text("No InterNet Connection...") // text to display
                        .actionLabel("Close")
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)//
                        .color(Color.BLACK) // change the background color// action button label
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                Log.d(TAG, "Undoing something");

                            }
                        }) // action button's ActionClickListener
                , ConformAppointmentActivity.this);
    }

    public void SnackBarhide() {
        SnackbarManager.dismiss();
    }

    @Override
    public void onBackPressed() {


        Intent dashboardActivity = new Intent(ConformAppointmentActivity.this, DashBoardActivity.class);
        startActivity(dashboardActivity);
        dashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        super.onBackPressed();

    }
}
