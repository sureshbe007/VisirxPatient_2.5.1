package com.visirx.patient.common.provider;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.AppointmentNotesListReq;
import com.visirx.patient.api.EMRFileReq;
import com.visirx.patient.api.EMRFileRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

public class EMRFileListProvider {
    ApptNotesTask apptNotesTask;
    static final String TAG = EMRFileListProvider.class.getName();
    Context context;
    SharedPreferences loggedPreferance;

    public EMRFileListProvider(Context context) {
        super();
        this.context = context;
    }

    public void SendApptsReq(String appointmentId, String patientId, String emrFilemaxDate, String emrVitalmaxDate) {
        try {
            loggedPreferance = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            if (VTConstants.checkAvailability(context)) {
                RequestHeader requestMessageHeader = new RequestHeader();
//				requestMessageHeader.setUserId(VisirxApplication.appContext.getLoggedUser().getUserId());
                requestMessageHeader.setUserId(loggedPreferance.getString(VTConstants.USER_ID, null));
                EMRFileReq appointmentReq = new EMRFileReq();
                appointmentReq.setAppointmentId(appointmentId);
                appointmentReq.setPatientId(patientId);
                appointmentReq.setEmrFilesLastUpdated(emrFilemaxDate);
                appointmentReq.setEmrVitalsLastUpdated(emrVitalmaxDate);
                appointmentReq.setRequestHeader(requestMessageHeader);
                //new ApptNotesTask().execute(appointmentReq);
                apptNotesTask = new ApptNotesTask(appointmentReq);
                StartAsyncTaskInParallel(apptNotesTask);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void StartAsyncTaskInParallel(ApptNotesTask task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }

    private class ApptNotesTask extends AsyncTask<EMRFileReq, Integer, EMRFileRes> {
        ProgressDialog ringProgressDialog = null;

        //rony start
        EMRFileReq sendReq;

        public ApptNotesTask(EMRFileReq request) {
            sendReq = request;
        }
        //ronyend

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showWait();
        }

        @Override
        protected EMRFileRes doInBackground(EMRFileReq... params) {
            EMRFileRes result = (EMRFileRes) HTTPUtils.getDataFromServer(/*params[0]*/sendReq, EMRFileReq.class,
                    EMRFileRes.class, "GetEMRFileServlet");
            return result;
        }

        @Override
        protected void onPostExecute(EMRFileRes result) {
            super.onPostExecute(result);
            ProcessEMRFileResponse(result);
            //cancelWait();
        }

        private void showWait() {
            ringProgressDialog = ProgressDialog.show(context,
                    "", "", true);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }

    }

    public void ProcessEMRFileResponse(EMRFileRes result) {
        try {
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                //Popup.ShowErrorMessage(context,R.string.error_trip_list,Toast.LENGTH_LONG);
                return;
            } else {
                if (result.getModel() != null) {
                    for (int i = 0; i < result.getModel().getEmrFileModel().size(); i++) {
                        // rony - EMRFRAGMENT GC - Starts
                        AddEmrFileModel eachResult = result.getModel()
                                .getEmrFileModel().get(i);
                        boolean isEmrFileUpate = false;
                        isEmrFileUpate = VisirxApplication.aptEmrDAO
                                .insertAppointmentEMR(eachResult,VTConstants.PROCESSED_FLAG_SENT);
                        Log.d("SPIN", "Emr file thumbnail not found : "
                                + isEmrFileUpate);
                        if (isEmrFileUpate) {

                            Log.d("SPIN",
                                    "Emr file row inserted : ");

                        } else {
                            Log.d("SPIN",
                                    "Emr file is in sync : "
                                            + eachResult.getAppointmentId());

                        }
                        // rony - EMRFRAGMENT GC - Ends
                    }

                }

            }
            Intent intent = new Intent(VTConstants.NOTIFICATION_EMRFILE);
            context.sendBroadcast(intent);
            Intent intent1 = new Intent(VTConstants.NOTIFICATION_EHD_IMAGE);
            context.sendBroadcast(intent1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }
}
