package com.visirx.patient.common.provider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.EhdDeleteReq;
import com.visirx.patient.api.EhdDeleteRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.model.ApptPrescriptionModel;
import com.visirx.patient.model.DeleteEhdModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 25-03-2016.
 */
public class DeleteEhdProvider {

    public Context context;
    private List<DeleteEhdModel> deleteEhdModelsList;

    public SharedPreferences loggedPreferance;

    public DeleteEhdProvider(Context context) {
        this.context = context;
    }

    public void SenddeleteReq(List<DeleteEhdModel> deleteEhdModelsList) {
        try {

            this.deleteEhdModelsList = deleteEhdModelsList;
//            loggedPreferance = context.getSharedPreferences(VTConstants.PREFS_LOGGEDUSERID, 0);
            loggedPreferance = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            if (VTConstants.checkAvailability(context)) {
                RequestHeader requestMessageHeader = new RequestHeader();
                requestMessageHeader.setUserId(loggedPreferance.getString(VTConstants.USER_ID, null));

                EhdDeleteReq ehdDeleteReq = new EhdDeleteReq();
                ehdDeleteReq.setDeleteMultimediaList(deleteEhdModelsList);
                ehdDeleteReq.setRequestHeader(requestMessageHeader);
                new ApptsTask().execute(ehdDeleteReq);

            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private class ApptsTask extends AsyncTask<EhdDeleteReq, Integer, EhdDeleteRes> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected EhdDeleteRes doInBackground(EhdDeleteReq... params) {
            EhdDeleteRes result = (EhdDeleteRes) HTTPUtils.getDataFromServer(params[0], EhdDeleteReq.class,
                    EhdDeleteRes.class, "DeleteMultimediaFilesAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(EhdDeleteRes result) {
            super.onPostExecute(result);
            ProcessDocFeeAppointmentResponse(result);
        }

        private void ProcessDocFeeAppointmentResponse(EhdDeleteRes result) {

            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {
//                GET IMAGE PATH FROM DB TO DELETE FROM SDCARD


                if (result.getResponseHeader().getResponseCode() == 0) {


                    try {
                        if (deleteEhdModelsList.get(0).getMediaType().equalsIgnoreCase(VTConstants.FLAG_EHD_FILE)) {
                            ArrayList<AddEmrFileModel> addEmrFileModels = VisirxApplication.aptEmrDAO.GetPatientEMRFileDetails(deleteEhdModelsList.get(0).getPatientId(), deleteEhdModelsList.get(0).getAppointmentId(), deleteEhdModelsList.get(0).getCreatedAt());

                            Log.d("NEWALERCHECK", " THUMNILE PATH:" + addEmrFileModels.get(0).getEmrImageThumbnailPath());
                            Log.d("NEWALERCHECK", " EMRIMAGE PATH:" + addEmrFileModels.get(0).getEmrImagePath());

                            VisirxApplication.aptEmrDAO.deleteEhdRecordFromTable(deleteEhdModelsList.get(0).getPatientId(), String.valueOf(deleteEhdModelsList.get(0).getAppointmentId()), deleteEhdModelsList.get(0).getCreatedAt());
//                        PATIENT_ID + "," + APPOINTMENT_ID + "," + CREATED_AT;

                         if(addEmrFileModels.get(0).getEmrImageThumbnailPath() != null)
                         {
                             File ImageThumbnail = new File(addEmrFileModels.get(0).getEmrImageThumbnailPath());
                             if (ImageThumbnail.exists()) {
                                 Log.d("NEWALERCHECK", " THUMNILE DELTE:" + addEmrFileModels.get(0).getEmrImageThumbnailPath());
                                 ImageThumbnail.delete();
                             }

                         }
                          if(addEmrFileModels.get(0).getEmrImagePath() !=  null)
                          {
                              File Imageemr = new File(addEmrFileModels.get(0).getEmrImagePath());
                              if (Imageemr.exists()) {
                                  Log.d("NEWALERCHECK", " EMRIMAGE DELTE:" + addEmrFileModels.get(0).getEmrImagePath());
                                  Imageemr.delete();
                              }
                          }




                            Intent intent = new Intent(VTConstants.NOTIFICATION_EMRFILE);
                            context.sendBroadcast(intent);

                            Intent intent1 = new Intent(VTConstants.NOTIFICATION_EHD_IMAGE);
                            context.sendBroadcast(intent1);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }

    }

}


