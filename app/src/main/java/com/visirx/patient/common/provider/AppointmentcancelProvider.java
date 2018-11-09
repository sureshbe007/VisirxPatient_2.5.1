package com.visirx.patient.common.provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.api.CancelAppointmentReq;
import com.visirx.patient.api.CancelAppointmentRes;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Suresh on 05-03-2016.
 */
public class AppointmentcancelProvider {

    Context context;
    SharedPreferences sharedPreferences;
    static final String TAG = AppointmentcancelProvider.class.getName();

    public AppointmentcancelProvider(Context context) {
        super();
        this.context = context;
    }

    public void CancelAppointmentReq() {
        CancelAppointmentReq cancelAppointmentReq = null;
        try {
            cancelAppointmentReq = new CancelAppointmentReq();
//            cancelAppointmentReq.setCustomerId();
            if (VTConstants.checkAvailability(context)) {
                new CancelappoinAsyn(context).execute(cancelAppointmentReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }

    }

    private class CancelappoinAsyn extends AsyncTask<CancelAppointmentReq, Integer, CancelAppointmentRes> {

        ProgressDialog ringProgressDialog;
        Context context;

        public CancelappoinAsyn(Context context) {

            this.context = context;
            ringProgressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWait();
        }

        private void showWait() {
            ringProgressDialog = ProgressDialog.show(context,
                    "", "please wait......", true);

        }

        @Override
        protected CancelAppointmentRes doInBackground(CancelAppointmentReq... params) {
            CancelAppointmentRes result = (CancelAppointmentRes) HTTPUtils.getDataFromServer(params[0], CancelAppointmentReq.class,
                    CancelAppointmentRes.class, "CancelAppointmentAppServlet");
//            LoginRes result = (LoginRes) HTTPUtils.getDataFromServer(params[0], LoginReq.class,
//                    LoginRes.class, "UserLoginServlet");
            return result;
        }

        @Override
        protected void onPostExecute(CancelAppointmentRes result) {
            super.onPostExecute(result);
            cancelWait();
            processBookAppointment(result);
        }


        private void cancelWait() {
            ringProgressDialog.dismiss();

        }

    }

    public void processBookAppointment(CancelAppointmentRes result) {
        try {

            if (result == null) {

                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {

                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

}
