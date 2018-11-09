package com.visirx.patient.common.provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.AddCareTeamReq;
import com.visirx.patient.api.AddCareTeamRes;
import com.visirx.patient.api.CareTeamReq;
import com.visirx.patient.api.CareTeamRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Suresh on 09-03-2016.
 */
public class CareteamToServerProvider {

    Context context;
    SharedPreferences sharedPreferences;
    static final String TAG = FindDoctorProvider.class.getName();
    List<AppointmentPatientModel> allappointmentList;
    //    AllappointmentListCallback allappointmentListCallback;
    List<FindDoctorModel> findDoctorModels;
    String CurrentDate;

    public CareteamToServerProvider(Context context) {
        super();
        this.context = context;
    }

    public void CareteamToServerReq(String DoctorID) {
        AddCareTeamReq addCareTeamReq = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date date = new Date();
            CurrentDate = dateFormat.format(date);
            sharedPreferences = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            RequestHeader requestMessageHeader = new RequestHeader();
            addCareTeamReq = new AddCareTeamReq();
            addCareTeamReq.setPerfomerId(DoctorID);
            addCareTeamReq.setCreatedAt(CurrentDate);
            addCareTeamReq.setCustomerId(sharedPreferences.getString(VTConstants.USER_ID, "null"));
            addCareTeamReq.setCreatedById(sharedPreferences.getString(VTConstants.USER_ID, "null"));
            requestMessageHeader.setUserId(sharedPreferences.getString(VTConstants.USER_ID, "null"));
            addCareTeamReq.setRequestHeader(requestMessageHeader);
            if (VTConstants.checkAvailability(context)) {
                new AddcareteamToServer(context).execute(addCareTeamReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("DOC 300::" + e.getMessage());
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }

    }

    private class AddcareteamToServer extends AsyncTask<AddCareTeamReq, Integer, AddCareTeamRes> {

        ProgressDialog ringProgressDialog;
        Context context;

        public AddcareteamToServer(Context context) {
            this.context = context;
            ringProgressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWait();
        }

        private void showWait() {

        }

        @Override
        protected AddCareTeamRes doInBackground(AddCareTeamReq... params) {
            AddCareTeamRes result = (AddCareTeamRes) HTTPUtils.getDataFromServer(params[0], AddCareTeamReq.class,
                    AddCareTeamRes.class, "AddCareTeamServlet");
            return result;
        }

        @Override
        protected void onPostExecute(AddCareTeamRes result) {
            super.onPostExecute(result);
            cancelWait();
            processBookAppointment(result);
        }


        private void cancelWait() {
        }

    }

    public void processBookAppointment(AddCareTeamRes result) {
        try {

            if (result == null) {

                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {

                Logger.w("CareteamToServerProvider","CAREteamResponse ERROR"+result.getResponseHeader().getResponseCode());
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {

                Logger.w("CareteamToServerProvider","CAREteamResponse"+result.getResponseHeader().getResponseMessage());
                Intent intent = new Intent(VTConstants.ALL_DOCTOR);
                context.sendBroadcast(intent);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }


}
