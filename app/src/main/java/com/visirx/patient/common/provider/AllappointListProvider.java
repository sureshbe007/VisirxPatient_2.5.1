package com.visirx.patient.common.provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.AppointmentPatientReq;
import com.visirx.patient.api.AppointmentPatientRes;
import com.visirx.patient.api.GetTimeslotsForPerformerRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.com.visirx.patient.adapter.AllappointmentListCallback;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.CustomerProfileModel;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.util.Collections;
import java.util.List;

/**
 * Created by Suresh on 04-03-2016.
 */
public class AllappointListProvider {
    Context context;
    SharedPreferences sharedPreferences;
    static final String TAG = FindDoctorProvider.class.getName();
    private FindDoctorModel findDoctorModel;
    private List<AppointmentPatientModel> appointmentPatientModel;

    public AllappointListProvider(Context context) {
        super();
        this.context = context;
//        this.allappointmentListCallback=allappointmentListCallback;
    }

    public void AllappointmentReq(String PatientID) {
        AppointmentPatientReq appointmentPatientReq = null;
        try {
            sharedPreferences = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            RequestHeader requestMessageHeader = new RequestHeader();
            appointmentPatientReq = new AppointmentPatientReq();
            appointmentPatientReq.setAppointmentid(VTConstants.ALLAPPOINTMENT_LIST);
            appointmentPatientReq.setPatientId(PatientID);
            requestMessageHeader.setUserId(sharedPreferences.getString(VTConstants.USER_ID, null));
            appointmentPatientReq.setRequestHeader(requestMessageHeader);
            if (VTConstants.checkAvailability(context)) {
                new allAppointmentAsyn(context).execute(appointmentPatientReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }

    }

    private class allAppointmentAsyn extends AsyncTask<AppointmentPatientReq, Integer, AppointmentPatientRes> {

        ProgressDialog ringProgressDialog;
        Context context;

        public allAppointmentAsyn(Context context) {
            this.context = context;
            ringProgressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWait();
        }

        private void showWait() {
//            ringProgressDialog = ProgressDialog.show(context,
//                    "", "please wait Appointment Loading.....", true);

        }

        @Override
        protected AppointmentPatientRes doInBackground(AppointmentPatientReq... params) {
            AppointmentPatientRes result = (AppointmentPatientRes) HTTPUtils.getDataFromServer(params[0], AppointmentPatientReq.class,
                    AppointmentPatientRes.class, "AppointmentServletPatient");
            return result;
        }

        @Override
        protected void onPostExecute(AppointmentPatientRes result) {
            super.onPostExecute(result);
            cancelWait();
            processBookAppointment(result);
        }


        private void cancelWait() {
            ringProgressDialog.dismiss();

        }

    }

    public void processBookAppointment(AppointmentPatientRes result) {
        try {
            if (result == null) {

                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {

                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {

//                for (int i=0;i<result.getApptModel().size();i++)
//                {
//
//                    appointmentPatientModel=VisirxApplication.aptDAO.getAppointmentByDoctor(result.getApptModel().get(i).getPerfomerid());
//                    System.out.println("appointmentPatientModel  getStatus    " + appointmentPatientModel.get(i).getStatus());
//                    System.out.println("appointmentPatientModel  getAppointmentType    " + appointmentPatientModel.get(i).getAppointmentType());
//                    System.out.println("appointmentPatientModel  getDate    " + appointmentPatientModel.get(i).getDate());
//                }


                VisirxApplication.aptDAO.insertAllAppointment(result.getApptModel());
                Intent intent = new Intent(VTConstants.NOTIFICATION_APPTS);
                context.sendBroadcast(intent);
                Intent Cancel = new Intent(VTConstants.CANCELAPPOINT_BROADCAST);
                context.sendBroadcast(Cancel);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }
}
