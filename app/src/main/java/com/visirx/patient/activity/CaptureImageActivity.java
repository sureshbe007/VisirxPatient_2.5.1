package com.visirx.patient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.LogTrace;

import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.model.AppointmentModel;
import com.visirx.patient.model.AppointmentParamedicModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class CaptureImageActivity extends AppCompatActivity {

    String TAG = CaptureImageActivity.class.getName();
    private static final int CAMERA_REQUEST = 1888;
    TextView txtHeading;
    private ImageView imageView;
    // hide by suresh for temp check

    //    AppointmentParamedicModel appointmentModel = null;
    AppointmentParamedicModel appointmentModel = null;
    byte[] byteArray = null;
    byte[] byteArrayThumbnail = null;
    final int THUMBNAIL_SIZE = 64;
    int reservationNumber = -1;
    private EditText textFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);
        if (getIntent() != null) {
            reservationNumber = getIntent().getIntExtra(VTConstants.APPTMODEL_KEY, -1);
        }
//        appointmentModel = VisirxApplication.aptDAO.GetAppointmentsByID(reservationNumber);
        this.imageView = (ImageView) this.findViewById(R.id.imageEMR);
        textFileName = (EditText) findViewById(R.id.textFileName);
        textFileName.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.prescription_view);
        txtHeading = (TextView) findViewById(R.id.title_bar);
        txtHeading.setText("Capture EMR Image");
        ImageView imagebackView = (ImageView) findViewById(R.id.backView);
        imagebackView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView cameraImageView = (ImageView) findViewById(R.id.camera_image);
        cameraImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } catch (Exception e) {
                    LogTrace.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        if (appointmentModel != null) {
            TextView viewApptID = (TextView) findViewById(R.id.textApptID);
            viewApptID.setText(Integer.toString(appointmentModel.getReservationNumber()));
            TextView viewVisirxID = (TextView) findViewById(R.id.textVisirxID);
            viewVisirxID.setText(appointmentModel.getCustomerId());
        }


    }


    public void SaveImage(View view) { //TODO
        try {
            String fileName = textFileName.getText().toString();
            if (fileName == null || fileName.length() <= 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Enter the file name");
                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }
            if (byteArray == null || byteArrayThumbnail == null) {
                Popup.ShowErrorMessageString(getBaseContext(), "No image found to save", Toast.LENGTH_SHORT);
                return;
            }
            SharedPreferences loggedPreferance = getSharedPreferences(
                    VTConstants.PREFS_LOGGEDUSERID, 0);
            AddEmrFileModel model = new AddEmrFileModel();
            model.setAppointmentId(appointmentModel.getReservationNumber());
            model.setCreatedAt(DateFormat.getDateStr(new Date()));
            model.setCreatedById(loggedPreferance.getString(VTConstants.LOGGED_USERID, "Not set"));
//			model.setCreatedById(VisirxApplication.appContext.getLoggedUser().getUserId());
            model.setPatientId(appointmentModel.getCustomerId());
            model.setFileType("Image");
//            model.setImageThumbnail(byteArrayThumbnail);
            model.setEmrFile(byteArray);
            model.setFileMimeType("png");
            model.setFileGroup("Image");
            model.setFileLabel(fileName);
//            long flag = VisirxApplication.aptEmrDAO.insertAppointmentEMR(model,VTConstants.PROCESSED_FLAG_NOT_SENT);
//            if(flag > 0){
//                Popup.ShowSuccessMessage(this, R.string.emr_saved_sucessfully, Toast.LENGTH_SHORT);
//                finish();
//
//                AddEMRFileProvider provider = new AddEMRFileProvider(getBaseContext());
//                provider.SendApptReq();
//
//                Intent intent = new Intent(VTConstants.NOTIFICATION_EMRFILE);
//                sendBroadcast(intent);
//            }
        } catch (Exception e) {
            LogTrace.e(TAG, e.getMessage());
            e.printStackTrace();
        }
      /*  if(VTConstants.checkAvailability(CaptureImageActivity.this)){snackBarHide();}else{snackBarShow();}*/
    }

    public void CancelImage(View view) {
        try {
            finish();
        } catch (Exception e) {
            LogTrace.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bmp);
            byteArray = getByteArrayFromBitmap(bmp);
            Bitmap imageBitmap = Bitmap.createScaledBitmap(bmp, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
            byteArrayThumbnail = getByteArrayFromBitmap(imageBitmap);
        }
    }

    private byte[] getByteArrayFromBitmap(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
