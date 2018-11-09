package com.visirx.patient.common.provider;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Suresh on 22-05-2016.
 */
public class UploadFileProvider {


    Context context;
    //    SharedPreferences loggedPreferance;
    ArrayList<AddEmrFileModel> modelList;
    int lstcnt = 0;
    private String patientId = "0";
    private int reservationNumber;
    private int appointmentId = 0;
    private String createdAt = "";
    private String createdById = "";
    private String FileName = "";
    RequestHeader requestMessageHeader;
    String createdByid;
    String createdByUserName;
    SharedPreferences sharedPreferences;

    //        Context context;
    public UploadFileProvider(Context context) {
        super();
        this.context = context;

    }


    public void sendEMRAttachment(int reservationNumber) {
        try {

            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            modelList = VisirxApplication.aptEmrDAO.GetEMRFileNotSent(reservationNumber);
            createdByid = sharedPreferences.getString(VTConstants.USER_ID, null);
            createdByUserName = sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME, null);

            Log.d("FILESTORED", "sendEmrAttachment :. " + modelList);

            if (modelList.size() > 0) {
                AddEmrFileModel modelnew = modelList.get(lstcnt);
                lstcnt = 1;
                loademr(modelnew);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void loademr(AddEmrFileModel modelnew) {
        sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        //        Log.v("FILESTORED", " load :. " + modelList);
        Log.d("FILESTORED", "getFileLabel :. 1" + modelnew.getFileLabel());
        Log.d("FILESTORED", "getFileGroup :. 2" + modelnew.getFileGroup());
        Log.d("FILESTORED", "getFileMimeType :. 3" + modelnew.getFileMimeType());
        Log.d("FILESTORED", "getFileType :. 4" + modelnew.getFileType());

        patientId = modelnew.getPatientId();
        reservationNumber = modelnew.getAppointmentId();
        createdAt = modelnew.getCreatedAt();
        FileName = modelnew.getFileLabel();

        File fileToUpload = new File(modelnew.getEmrImagePath());
        Ion.with(context)
                .load(HTTPUtils.gURLBase + "UploadEmrFileServlet")
                .setHeader(VTConstants.PROVIDER_CREATEDAT, modelnew.getCreatedAt())
                .setHeader(VTConstants.PROVIDER_PATIENTID, modelnew.getPatientId())
                .setHeader(VTConstants.PROVIDER_APPOINTMETID, String.valueOf(modelnew.getAppointmentId()))
                .setHeader(VTConstants.PROVIDER_CREATEDBYID, modelnew.getCreatedById())
                .setHeader(VTConstants.PROVIDER_FILEGROUP, modelnew.getFileGroup())
                .setHeader(VTConstants.PROVIDER_FILELABEL, modelnew.getFileLabel())
                .setHeader(VTConstants.PROVIDER_FILEMIMETYPE, modelnew.getFileMimeType())
                .setHeader(VTConstants.PROVIDER_FILETYPE, modelnew.getFileType())
                .setHeader(VTConstants.PROVIDER_USERID, sharedPreferences.getString(VTConstants.USER_ID, null))

                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {

                    }
                })
//                .setTimeout(60  60  1000)
                .setMultipartFile("emrFile", modelnew.getFileType(), fileToUpload)
                .asJsonObject()
                // run a callback on completion
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            Log.d("FILESTORED", " On response from server : Inside onCompleted()");
                            if (e != null) {
                                Log.d("FILESTORED", " On response from server : Caught exception");
                                Toast.makeText(context, "Error uploading file", Toast.LENGTH_LONG).show();
                                return;
                            } else if (result == null) {

                                Log.d("FILESTORED", " On response from server : NULL");
                                // Log.d("JsonObjectResult", " Resut : " + result.toString());
                                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                                return;
                            } else {


                                JsonObject responseHeader = result.getAsJsonObject(VTConstants.PROVIDER_RESPONSEHEADER);
                                JsonElement responseCode = responseHeader.get(VTConstants.PROVIDER_REPONSECODE);
                                JsonElement responseMessage = responseHeader.get(VTConstants.PROVIDER_RESPONSEMESSAGE);

                                Log.d("FILESTORED", " On success responseHeader from server : " + responseHeader.toString());
                                Log.d("FILESTORED", " On success responseCode from server : " + responseCode.toString());
                                Log.d("FILESTORED", " On success responseMessage from server : " + responseMessage.toString());

                                if (responseCode.toString().equals("0")) {
//                                Toast.makeText(context, "File upload complete", Toast.LENGTH_LONG).show();
//                                Popup.ShowSuccessMessage(context, R.string.file_upload_success, Toast.LENGTH_SHORT);
                                    Log.d("FILESTORED", "FILESTORED :" + result.get("createdAtServer").toString());
                                    VisirxApplication.aptEmrDAO.setProcessFlagAppointmentPrescription(patientId, reservationNumber, createdAt, FileName, context, result.get("createdAtServer").toString());
                                    if (modelList.size() > lstcnt) {
                                        AddEmrFileModel modelnew = modelList.get(lstcnt);
                                        loademr(modelnew);

                                        lstcnt++;
                                    }

                                    Intent intent1 = new Intent(VTConstants.NOTIFICATION_EMRFILE);
                                    context.sendBroadcast(intent1);

                                    Intent ehdimageintent1 = new Intent(VTConstants.NOTIFICATION_EHD_IMAGE);
                                    context.sendBroadcast(ehdimageintent1);
                                } else {
                                    Popup.ShowErrorMessage(context, R.string.file_upload_not_completed, Toast.LENGTH_SHORT);
                                }

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
    }



}
