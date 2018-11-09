package com.visirx.patient.common.provider;

import android.annotation.TargetApi;
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
import com.visirx.patient.api.AppointmentNotesListRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Rony on 12/03/2016.
 */
public class AppointmentNotesListProvider {

    ApptNotesTask apptNotesTask;

    static final String TAG = AppointmentNotesListProvider.class.getName();
    Context context;
    SharedPreferences loggedPreferance;

    public AppointmentNotesListProvider(Context context) {
        super();
        this.context = context;
    }

    public void SendApptsReq(int appointmentId, String patientId,
                             String notesMaxDate) {
        try {
            loggedPreferance = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            if (VTConstants.checkAvailability(context)) {
                VTConstants.PROGRESSSTATUS_NOTES = 1;
                RequestHeader requestMessageHeader = new RequestHeader();
//				requestMessageHeader.setUserId(VisirxApplication.appContext.getLoggedUser().getUserId());
                requestMessageHeader.setUserId(loggedPreferance.getString(VTConstants.USER_ID, null));
                AppointmentNotesListReq appointmentReq = new AppointmentNotesListReq();
                appointmentReq.setAppointmentId(appointmentId);
                appointmentReq.setPatientId(patientId);
                appointmentReq.setNotesLastUpdated(notesMaxDate);
                appointmentReq.setRequestHeader(requestMessageHeader);
                //new ApptNotesTask().execute(appointmentReq);
                apptNotesTask = new ApptNotesTask(appointmentReq);
                StartAsyncTaskInParallel(apptNotesTask);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("SPIN", e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void StartAsyncTaskInParallel(ApptNotesTask task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }

    private class ApptNotesTask
            extends
            AsyncTask<AppointmentNotesListReq, Integer, AppointmentNotesListRes> {

        //rony start
        AppointmentNotesListReq sendReq;

        public ApptNotesTask(AppointmentNotesListReq request) {
            sendReq = request;
        }
        //ronyend


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected AppointmentNotesListRes doInBackground(
                AppointmentNotesListReq... params) {
            AppointmentNotesListRes result = (AppointmentNotesListRes) HTTPUtils
                    .getDataFromServer(/*params[0]*/sendReq,
                            AppointmentNotesListReq.class,
                            AppointmentNotesListRes.class,
                            "AppointmentNotesListServlet");
            return result;
        }

        @Override
        protected void onPostExecute(AppointmentNotesListRes result) {
            super.onPostExecute(result);
            ProcessAppointmentNotesListResponse(result);
        }

    }

    public void ProcessAppointmentNotesListResponse(
            AppointmentNotesListRes result) {
        try {
            VTConstants.PROGRESSSTATUS_NOTES = 0;
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
                for (int i = 0; i < result.getNoteModelList().size(); i++) {
                    VisirxApplication.aptNotesDAO.insertAppointmentNotes(result
                                    .getNoteModelList().get(i),
                            VTConstants.PROCESSED_FLAG_SENT);
                }

            }
            Intent intent = new Intent(VTConstants.NOTIFICATION_APPTS_NOTES);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }
}
