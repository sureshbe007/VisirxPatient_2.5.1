package com.visirx.patient.common.provider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.CancelAppointmentReq;
import com.visirx.patient.api.CancelAppointmentRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.CancelAppointmentModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.util.List;

/**
 * Created by Suresh on 07-03-2016.
 */
public class CancelAppointmentProvider {
    Context context;
    SharedPreferences sharedPreferences;
    static final String TAG = FindDoctorProvider.class.getName();
    int AppointmentID;

    public CancelAppointmentProvider(Context context) {
        super();
        this.context = context;

    }

    public void CancelAppointmentReq(int AppointmentID) {
        CancelAppointmentReq cancelAppointmentReq = null;
        try {
            this.AppointmentID = AppointmentID;
            cancelAppointmentReq = new CancelAppointmentReq();
            sharedPreferences = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            RequestHeader requestMessageHeader = new RequestHeader();
            requestMessageHeader.setUserId(sharedPreferences.getString(VTConstants.USER_ID, null));
            List<CancelAppointmentModel> list = VisirxApplication.aptDAO.GetCanceledAppointments(AppointmentID);
            System.out.println("CCCCCCCCCC" + list.get(0).getLastUpdatedAt() + "=====" + list.get(0).getLastUpdatedBy());
            cancelAppointmentReq.setCaModelList(list);
            cancelAppointmentReq.setRequestHeader(requestMessageHeader);
//            System.out.println("AAAAAA" + AppointmentID);
            new CancelAppointAsyn(context).execute(cancelAppointmentReq);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }

    }

    private class CancelAppointAsyn extends AsyncTask<CancelAppointmentReq, Integer, CancelAppointmentRes> {
        ProgressDialog ringProgressDialog;
        Context context;

        public CancelAppointAsyn(Context context) {
            this.context = context;
            ringProgressDialog = new ProgressDialog(context);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWait();
        }

        private void showWait() {
//            ringProgressDialog.setTitle("please wait Search a Doctor, ...");
//            ringProgressDialog.show();
            ringProgressDialog = ProgressDialog.show(
                    ((Activity) context),
                    "", "Please wait cancel an Appointment, ...", true);
            System.out.println("DOC 2");
        }

        @Override
        protected CancelAppointmentRes doInBackground(CancelAppointmentReq... params) {
            CancelAppointmentRes result = (CancelAppointmentRes) HTTPUtils.getDataFromServer(params[0], CancelAppointmentReq.class,
                    CancelAppointmentRes.class, "CancelAppointmentAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(CancelAppointmentRes result) {
            super.onPostExecute(result);
            cancelWait();
            Cancelprocess(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }
    }

    public void Cancelprocess(CancelAppointmentRes result) {
        try {
            if (result == null) {

                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Logger.w("CANCEAPPOINTPROVIDER", "getResponseCode -1     " + result.getResponseHeader().getResponseCode());

                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {
                VisirxApplication.aptDAO.cancelAppointment(AppointmentID);

                Intent allappointment = new Intent(VTConstants.NOTIFICATION_APPTS);
                context.sendBroadcast(allappointment);
                Intent cancelappointment = new Intent(VTConstants.CANCELAPPOINT_BROADCAST);
                context.sendBroadcast(cancelappointment);
                Logger.w("CANCEAPPOINTPROVIDER", "getResponseCode    " + result.getResponseHeader().getResponseCode());
                Logger.w("CANCEAPPOINTPROVIDER", "AppointmentID     "+AppointmentID);
//                Intent allDocIntent = new Intent(VTConstants.NOTIFICATION_APPTS);
//                context.sendBroadcast(allDocIntent);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }
}
