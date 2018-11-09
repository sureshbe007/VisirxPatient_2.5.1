package com.visirx.patient.common.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.AppointmentNotesReq;
import com.visirx.patient.api.AppointmentNotesRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AppointmentNoteModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.util.ArrayList;

/**
 * Created by Suresh on 22-02-2016.
 */
public class AppointmentNotesProvider {
    ApptNotesTask apptNotesTask;
    static final String TAG = AppointmentNotesProvider.class.getName();
    Context context;
    AppointmentNoteModel appointmentNoteModel;
    SharedPreferences loggedPreferance;

    public AppointmentNotesProvider(Context context) {
        super();
        this.context = context;
    }

    public void SendApptsReq() {
        try {
            loggedPreferance = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            if (VTConstants.checkAvailability(context)) {
                RequestHeader requestMessageHeader = new RequestHeader();
//				requestMessageHeader.setUserId(VisirxApplication.appContext.getLoggedUser().getUserId());
                requestMessageHeader.setUserId(loggedPreferance.getString(VTConstants.USER_ID, null));
                ArrayList<AppointmentNoteModel> modelList = VisirxApplication.aptNotesDAO.GetNotes();
                System.out.println("modelList" + modelList.size());
                appointmentNoteModel = modelList.get(0);
                AppointmentNotesReq appointmentReq = new AppointmentNotesReq();
                appointmentReq.setNoteModelList(modelList);
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

    private class ApptNotesTask extends AsyncTask<AppointmentNotesReq, Integer, AppointmentNotesRes> {

        //rony start
        AppointmentNotesReq sendReq;

        public ApptNotesTask(AppointmentNotesReq request) {
            sendReq = request;
        }
        //ronyend

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected AppointmentNotesRes doInBackground(AppointmentNotesReq... params) {
            AppointmentNotesRes result = (AppointmentNotesRes) HTTPUtils.getDataFromServer(/*params[0]*/sendReq, AppointmentNotesReq.class,
                    AppointmentNotesRes.class, "AddAppointmentNotesAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(AppointmentNotesRes result) {
            super.onPostExecute(result);
            ProcessAppointmentNotesResponse(result);
        }

    }

    public void ProcessAppointmentNotesResponse(AppointmentNotesRes result) {
        try {
            // TODO Auto-generated method stub
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                //Popup.ShowErrorMessage(context,R.string.error_trip_list,Toast.LENGTH_LONG);
                return;
            } else {
                VisirxApplication.aptNotesDAO.SetprocessFlagUpdate(appointmentNoteModel.getPatientId(),
                        appointmentNoteModel.getAppointmentId(),
                        appointmentNoteModel.getCreatedAt(),
                        result.getCreatedAtServer());
            }
            Intent intent = new Intent(VTConstants.NOTIFICATION_APPTS_NOTES);
            context.sendBroadcast(intent);

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }


}
