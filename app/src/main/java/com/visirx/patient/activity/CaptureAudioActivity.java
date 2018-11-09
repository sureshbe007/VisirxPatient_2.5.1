package com.visirx.patient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.LogTrace;

import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.model.AppointmentModel;
import com.visirx.patient.model.AppointmentParamedicModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class CaptureAudioActivity extends BaseActivity {

    String TAG = CaptureImageActivity.class.getName();
    // hide by suresh for temp check
//    AppointmentParamedicModel appointmentModel = null;
    AppointmentPatientModel appointmentModel = null;
    byte[] byteArray = null;
    byte[] byteArrayThumbnail = null;
    final int THUMBNAIL_SIZE = 64;
    int reservationNumber = -1;
    private String outputFile = null;
    private MediaRecorder myRecorder;
    private EditText textFileName;

    Button startButton = null;
    Button stopButton = null;
    Button saveAudio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_audio);
        if(getIntent() != null){
            reservationNumber =   getIntent().getIntExtra(VTConstants.APPTMODEL_KEY, -1);

            appointmentModel = VisirxApplication.aptDAO.GetAppointmentsByID(reservationNumber);
            setHeading("Auscultation");

            textFileName = (EditText) findViewById(R.id.textFileName);

            outputFile = getCacheDir().getAbsolutePath() + "/AudioRecord.mp3";

            myRecorder = new MediaRecorder();
		/*myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);*/
            myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            myRecorder.setOutputFile(outputFile);

            startButton = (Button)findViewById(R.id.startButton);
            stopButton = (Button)findViewById(R.id.stopButton);
            stopButton.setEnabled(false);

            saveAudio = (Button) findViewById(R.id.saveAudio);
            saveAudio.setEnabled(false);
        }
    }

    public void startRecord(View view){
        try {
            myRecorder.prepare();
            myRecorder.start();

            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            saveAudio.setEnabled(false);

        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

    }

    public void stopRecord(View view)
    {
        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;

            startButton.setEnabled(true);
            stopButton.setEnabled(false);

            saveAudio.setEnabled(true);

        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            LogTrace.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public void SaveAudio(View view)
    { //TODO
        try {
            String fileName = textFileName.getText().toString();

            if(fileName == null || fileName.length() <= 0){
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

            FileInputStream fis = new FileInputStream(outputFile);
            //System.out.println(file.exists() + "!!");
            //InputStream in = resource.openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum); //no doubt here is 0
                }
            } catch (IOException e) {
                LogTrace.e(TAG, e.getMessage());
            }

            String thump = "";
            byteArray = bos.toByteArray();
            AddEmrFileModel model = new AddEmrFileModel();
            model.setAppointmentId(appointmentModel.getReservationNumber());
            model.setCreatedAt(DateFormat.getDateStr(new Date()));
            model.setCreatedById(VisirxApplication.appContext.getLoggedUser().getUserId());
            model.setPatientId(appointmentModel.getPerfomerid());
            model.setFileType("Audio");
//            model.setImageThumbnail(thump.getBytes());
            model.setEmrFile(byteArray);
            model.setFileMimeType("mp3");
            model.setFileGroup("Auscultation");
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
    }

    public void CancelAudio(View view){
        try {
            finish();
        } catch (Exception e) {
            LogTrace.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }
}
