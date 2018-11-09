package com.visirx.patient.common.provider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.AppointmentParamedicReq;
import com.visirx.patient.api.AppointmentParamedicRes;
import com.visirx.patient.api.AppointmentReq;
import com.visirx.patient.api.AppointmentRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Askar on 1/21/2016.
 */
public class AppointmentProvider {

    static final String TAG = AppointmentProvider.class.getName();
    Context context;
    SharedPreferences loggedPreferance;

    public AppointmentProvider(Context context) {
        super();
        this.context = context;
    }

    public void SendApptsReq() {
        try {
            loggedPreferance = context.getSharedPreferences(
                    VTConstants.PREFS_LOGGEDUSERID, 0);
            VTConstants.PROGRESSSTATUS_DASHBOARD = 1;
            if (VTConstants.checkAvailability(context)) {
                RequestHeader requestMessageHeader = new RequestHeader();
//				requestMessageHeader.setUserId(VisirxApplication.appContext
//						.getLoggedUser().getUserId());
//				requestMessageHeader.setRoleId(VisirxApplication.appContext
//						.getLoggedUser().getRoleId());
                requestMessageHeader.setUserId(loggedPreferance.getString(VTConstants.LOGGED_USERID, "Not set"));
                requestMessageHeader.setRoleId(loggedPreferance.getString(VTConstants.LOGGED_ROLEID, "Not set"));
                AppointmentReq appointmentReq = new AppointmentReq();
                appointmentReq.setRequestHeader(requestMessageHeader);
                new ApptsTask().execute(appointmentReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    public void SendApptsReqWithAppointmentid(String appointmentid) {
        try {
            loggedPreferance = context.getSharedPreferences(
                    VTConstants.PREFS_LOGGEDUSERID, 0);
            if (VTConstants.checkAvailability(context)) {
                RequestHeader requestMessageHeader = new RequestHeader();
//				requestMessageHeader.setUserId(VisirxApplication.appContext
//						.getLoggedUser().getUserId());
//				requestMessageHeader.setRoleId(VisirxApplication.appContext
//						.getLoggedUser().getRoleId());
                requestMessageHeader.setUserId(loggedPreferance.getString(VTConstants.LOGGED_USERID, "Not set"));
                requestMessageHeader.setRoleId(loggedPreferance.getString(VTConstants.LOGGED_ROLEID, "Not set"));
                AppointmentReq appointmentReq = new AppointmentReq();
                appointmentReq.setRequestHeader(requestMessageHeader);
                appointmentReq.setAppointmentid(appointmentid);
                new ApptsTask().execute(appointmentReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    public void SendApptsHistoryReq(String patientId) {
        try {
            loggedPreferance = context.getSharedPreferences(
                    VTConstants.PREFS_LOGGEDUSERID, 0);
            if (VTConstants.checkAvailability(context)) {
                RequestHeader requestMessageHeader = new RequestHeader();
//				requestMessageHeader.setUserId(VisirxApplication.appContext
//						.getLoggedUser().getUserId());
//				requestMessageHeader.setRoleId(VisirxApplication.appContext
//						.getLoggedUser().getRoleId());
                requestMessageHeader.setUserId(loggedPreferance.getString(VTConstants.LOGGED_USERID, "Not set"));
                requestMessageHeader.setRoleId(loggedPreferance.getString(VTConstants.LOGGED_ROLEID, "Not set"));
                AppointmentReq appointmentReq = new AppointmentReq();
                appointmentReq.setRequestHeader(requestMessageHeader);
                appointmentReq.setPatientId(patientId);
                new ApptsHistoryTask().execute(appointmentReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private class ApptsTask extends
            AsyncTask<AppointmentReq, Integer, AppointmentRes> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected AppointmentRes doInBackground(AppointmentReq... params) {
            AppointmentRes result = (AppointmentRes) HTTPUtils
                    .getDataFromServer(params[0], AppointmentReq.class,
                            AppointmentRes.class, "AppointmentDetailsServlet");
            return result;
        }

        @Override
        protected void onPostExecute(AppointmentRes result) {
            super.onPostExecute(result);
            ProcessAppointmentResponse(result);
        }

    }

    public void ProcessAppointmentResponse(AppointmentRes result) {
        try {
            VTConstants.PROGRESSSTATUS_DASHBOARD = 0;
            // TODO Auto-generated method stub
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect,
                        Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result
                                .getResponseHeader().getResponseMessage(),
                        Toast.LENGTH_LONG);
                return;
            } else {
                // rony Dashboard GC - starts
//
//                VisirxApplication.aptDAO.insertAppointment(result
//                        .getAppointmentModel(),context);
                // rony Dashboard GC - ends
            }
            Intent intent = new Intent(VTConstants.NOTIFICATION_APPTS);
            context.sendBroadcast(intent);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private class ApptsHistoryTask extends
            AsyncTask<AppointmentReq, Integer, AppointmentRes> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected AppointmentRes doInBackground(AppointmentReq... params) {
            AppointmentRes result = (AppointmentRes) HTTPUtils
                    .getDataFromServer(params[0], AppointmentReq.class,
                            AppointmentRes.class, "ApptHistoryServlet");
            return result;
        }

        @Override
        protected void onPostExecute(AppointmentRes result) {
            super.onPostExecute(result);
            ProcessAppointmentHistoryResponse(result);
        }

    }

    public void ProcessAppointmentHistoryResponse(AppointmentRes result) {
        try {
            // TODO Auto-generated method stub
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect,
                        Toast.LENGTH_SHORT);
                // return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result
                                .getResponseHeader().getResponseMessage(),
                        Toast.LENGTH_LONG);
                // return;
            } else {
                // rony Dashboard GC - starts
//                VisirxApplication.aptDAO.insertAppointment(result
//                        .getAppointmentModel(),context);
                // rony Dashboard GC - Ends
            }
            Intent intent = new Intent(VTConstants.NOTIFICATION_APPT_HISTORY);
            context.sendBroadcast(intent);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

}
