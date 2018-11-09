package com.visirx.patient.common.provider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.AddEmrFileReq;
import com.visirx.patient.api.AddEmrFileRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 14-03-2016.
 */
public class AddEMRFileProvider {


    static final String TAG = AddEMRFileProvider.class.getName();
    Context context;
    SharedPreferences loggedPreferance;
    ArrayList<AddEmrFileModel> modelList;
    int lstcnt = 0;
    private String patientId = "0";
    private int appointmentId = 0;
    private String createdAt = "";
    private String createdById = "";
    private String FileName = "";
    RequestHeader requestMessageHeader;

    public AddEMRFileProvider(Context context) {
        super();
        this.context = context;
    }

    public void SendApptReq(int ReservationNumber) {

        try {
            loggedPreferance = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            requestMessageHeader = new RequestHeader();
            requestMessageHeader.setUserId(loggedPreferance.getString(VTConstants.USER_ID, null));
            modelList = VisirxApplication.aptEmrDAO.GetEMRFileNotSent(ReservationNumber);
            if (modelList.size() > 0) {
                ArrayList<AddEmrFileModel> modelListnew = new ArrayList<AddEmrFileModel>();
                AddEmrFileModel modelnew = modelList.get(lstcnt);
                Log.d("EMRFILEUPLOADCHECKING", "UPLOADED" + modelnew.getEmrImagePath());
                byte[] data = convertFileToByteArray(modelnew.getEmrImagePath());
                modelnew.setEmrFile(data);
                patientId = modelnew.getPatientId();
                appointmentId = modelnew.getAppointmentId();
                createdAt = modelnew.getCreatedAt();
                createdById=modelnew.getCreatedById();
                FileName=modelnew.getFileLabel();
                modelListnew.add(modelnew);
                AddEmrFileReq appointmentReq = new AddEmrFileReq();
                appointmentReq.setAddEmrFileModel(modelListnew);
                appointmentReq.setRequestHeader(requestMessageHeader);
                lstcnt = 1;
                new ApptNotesTask().execute(appointmentReq);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ApptNotesTask extends AsyncTask<AddEmrFileReq, Integer, AddEmrFileRes> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected AddEmrFileRes doInBackground(AddEmrFileReq... params) {
            AddEmrFileRes result = (AddEmrFileRes) HTTPUtils.getDataFromServer(params[0],
                    AddEmrFileReq.class, AddEmrFileRes.class, "AddEmrFilesAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(AddEmrFileRes result) {
            super.onPostExecute(result);
            try {

                if (result == null) {
                    Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                    return;
                } else {
//                  ?  VisirxApplication.aptEmrDAO.setProcessFlagAppointmentPrescription(patientId, appointmentId, createdAt,FileName,context);
                    Intent intentEmr = new Intent(VTConstants.NOTIFICATION_EMRFILE);
                    context.sendBroadcast(intentEmr);



                    Intent intent = new Intent(VTConstants.NOTIFICATION_EHD_IMAGE);
                    context.sendBroadcast(intent);
                    Log.d("EMRFILEUPLOADCHECKING", "UPLOADED");
                    if (modelList.size() > 0)

                    {
                        ArrayList<AddEmrFileModel> modelListnew = new ArrayList<AddEmrFileModel>();
                        AddEmrFileModel modelnew = modelList.get(lstcnt);

                        byte[] data = convertFileToByteArray(modelnew.getEmrImagePath());
                        modelnew.setEmrFile(data);
                        patientId = modelnew.getPatientId();
                        appointmentId = modelnew.getAppointmentId();
                        createdAt = modelnew.getCreatedAt();
                        createdById=modelnew.getCreatedById();
                        modelListnew.add(modelnew);
                        AddEmrFileReq appointmentReq = new AddEmrFileReq();
                        appointmentReq.setAddEmrFileModel(modelListnew);
                        appointmentReq.setRequestHeader(requestMessageHeader);
                        lstcnt = lstcnt + 1;
                        Intent intent1 = new Intent(VTConstants.NOTIFICATION_EMRFILE);
                        context.sendBroadcast(intent1);
                        Intent intent2 = new Intent(VTConstants.NOTIFICATION_EHD_IMAGE);
                        context.sendBroadcast(intent2);

                        new ApptNotesTask().execute(appointmentReq);
                    }




                }
                // ProcessAddprescriptionResponse(result);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }

    }

    public static byte[] convertFileToByteArray(String f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

}