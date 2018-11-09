package com.visirx.patient.common.provider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.api.LogoutReq;
import com.visirx.patient.api.LogoutRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Suresh on 30-05-2016.
 */
public class LogoutProvider {


    Context context;
    SharedPreferences loggedPreferance;

    public LogoutProvider(Context context) {
        super();
        this.context = context;
    }

    public void SendLogoutReq(String gcmId) {
        try {
            if (VTConstants.checkAvailability(context)) {

                loggedPreferance = context.getSharedPreferences(
                        VTConstants.LOGIN_PREFRENCES_NAME, 0);
                LogoutReq logoutReq = new LogoutReq();
                RequestHeader requestHeader = new RequestHeader();
                requestHeader.setUserId(loggedPreferance.getString(VTConstants.USER_ID, null));
                Log.d("LogoutProvider", "inside : GCM ID  :" + gcmId);
                Log.d("LogoutProvider", "inside : USER ID  :" + loggedPreferance.getString(VTConstants.USER_ID, null));
                logoutReq.setRequestHeader(requestHeader);
                logoutReq.setGcmId(gcmId);
                //    new NewLogout().execute("");
                new LogoutProcess().execute(logoutReq);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private class LogoutProcess extends AsyncTask<LogoutReq, Integer, LogoutRes> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected LogoutRes doInBackground(LogoutReq... params) {
            LogoutRes result = null;
            try {
                result = (LogoutRes) HTTPUtils.getDataFromServer(params[0], LogoutReq.class,
                        LogoutRes.class, "LogoutAppUserServlet");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(LogoutRes result) {
            super.onPostExecute(result);
            
            LogoutResponseStatus(result);
        }

    }

    public void LogoutResponseStatus(LogoutRes result) {
        try {
            Log.d("LogoutProvider", "inside : LogoutResponseStatus () :  "+result.toString());
            // TODO Auto-generated method stub
            if (result == null) {
                Log.d("LogoutProvider", "inside : result == null : ");
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Log.d("LogoutProvider", "inside : result !=0 :");
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {
                Log.d("LogoutProvider", "inside : result ==0 : Success "+result.getResponseHeader().getResponseCode() );
                loggedPreferance = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                loggedPreferance.edit().putString("username", "")
                        .putString("password", "")
                        .putBoolean(VTConstants.LOGIN_STATUS, false).commit();

                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                Toast.makeText(context, "Successfully logged out.!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
