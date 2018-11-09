package com.visirx.patient.common.provider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.visirx.patient.ParamedicDetailsReq;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.ParamedicDetailsRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Suresh on 22-02-2016.
 */
public class ParamedicDetailsProvider {
    static final String TAG = ParamedicDetailsProvider.class.getName();
    Context context;
    SharedPreferences loggedPreferance;

    public ParamedicDetailsProvider(Context context) {
        super();
        this.context = context;
    }

    public void SendParamedicDetailsReq(String customerId, int appointmentId,
                                        int isAssigned, String nurseUpdatedTime) {
        try {
            loggedPreferance = context.getSharedPreferences(
                    VTConstants.PREFS_LOGGEDUSERID, 0);
            if (VTConstants.checkAvailability(context)) {
                RequestHeader requestMessageHeader = new RequestHeader();
                /*requestMessageHeader.setUserId(VisirxApplication.appContext
                        .getLoggedUser().getUserId());*/
                requestMessageHeader.setUserId(loggedPreferance.getString(VTConstants.LOGGED_USERID, "Not set"));
                ParamedicDetailsReq paraReq = new ParamedicDetailsReq();
                paraReq.setAppointmentId(appointmentId);
                paraReq.setPatientId(customerId);
                paraReq.setIsNurseAssigned(isAssigned);
                paraReq.setNurseLastUpdated(nurseUpdatedTime);
                paraReq.setRequestHeader(requestMessageHeader);
                new ParamedicDetailsTask().execute(paraReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }

    }

    public class ParamedicDetailsTask extends
            AsyncTask<ParamedicDetailsReq, Integer, ParamedicDetailsRes> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ParamedicDetailsRes doInBackground(
                ParamedicDetailsReq... params) {
            ParamedicDetailsRes result = (ParamedicDetailsRes) HTTPUtils
                    .getDataFromServer(params[0], ParamedicDetailsReq.class,
                            ParamedicDetailsRes.class,
                            "GetParamedicForAppointmentAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(ParamedicDetailsRes result) {
            super.onPostExecute(result);
            ProcessParamedicDetailResponse(result);
        }

    }

    public void ProcessParamedicDetailResponse(ParamedicDetailsRes result) {
        try {
            // TODO Auto-generated method stub
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect,
                        Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result
                                .getResponseHeader().getResponseMessage(),
                        Toast.LENGTH_LONG);
                // Popup.ShowErrorMessage(context,R.string.error_trip_list,Toast.LENGTH_LONG);
                return;
            } else {
                LogTrace.w(
                        TAG,
                        "paramedicModel From Web  : isNurseUpdated : "
                                + result.getParamedicDetailsModel().isNurseUpdated() + " : isNurseAssigned : " + result.getParamedicDetailsModel().isNurseAssigned());
                if (result.getParamedicDetailsModel().isNurseAssigned()) {
                    Log.d("SPIN", "IS Nurse Assigned : true");
                    if ((result.getParamedicDetailsModel().isNurseAssigned()
                            && result.getParamedicDetailsModel()
                            .isNurseUpdated())) {
                        Log.d("SPIN", "IS Nurse Assigned & is updated: true");
                        // rony - PrescriptionGC - Starts
                        boolean isUpdated = VisirxApplication.aptDAO
                                .updateParamedicDetailsForAppt(result
                                        .getParamedicDetailsModel());
                        if (isUpdated) {
                            Log.d("SPIN", "Customer base data updated. Sync Required.");
                            ImageDownloaderProvider dwnlodProvider = new ImageDownloaderProvider(context, VTConstants.NOTIFICATION_NURSE_ASSIGNED);
                            dwnlodProvider.SendProfileImgReq(result.getParamedicDetailsModel().getNurseId());


                        }
                    }
                } else {
                    Log.d("SPIN", "IS Nurse not Assigned : true");
                    if ((!result.getParamedicDetailsModel().isNurseAssigned()
                            && !result.getParamedicDetailsModel()
                            .isNurseUpdated())) {
                        Log.d("SPIN", "IS Nurse not Assigned & is not updated: true");
                        boolean isUpdated = VisirxApplication.aptDAO
                                .updateParamedicDetailsForAppt(result
                                        .getParamedicDetailsModel());

                    }

                }
                Intent intent = new Intent(
                        VTConstants.NOTIFICATION_NURSE_ASSIGNED);
                context.sendBroadcast(intent);
                // rony - PrescriptionGC - Ends
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

}
