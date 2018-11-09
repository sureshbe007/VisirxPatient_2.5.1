package com.visirx.patient.common.provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.api.PerformerDetailsReq;
import com.visirx.patient.api.PerformerDetailsRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.com.visirx.patient.adapter.DoctorProfileCallBack;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Suresh on 19-03-2016.
 */
public class DoctorProfileProvider {

    Context context;
    static final String TAG = DoctorProfileProvider.class.getName();
    SharedPreferences sharedPreferences;
    String PatientID;
    DoctorProfileCallBack doctorProfileCallBack;

    public DoctorProfileProvider(Context context,DoctorProfileCallBack doctorProfileCallBack) {
        super();
        this.context = context;
        this.doctorProfileCallBack=doctorProfileCallBack;
    }

    public void DoctorReq(String DoctorID) {

        Log.d("Doctorr/provider", "inside intent");
        PerformerDetailsReq performerDetailsReq = null;
        try {
            performerDetailsReq = new PerformerDetailsReq();
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            PatientID = sharedPreferences.getString(VTConstants.USER_ID, "null");
            RequestHeader requestMessageHeader = new RequestHeader();
            performerDetailsReq.setPerformerId(DoctorID);
            requestMessageHeader.setUserId(PatientID);
            performerDetailsReq.setRequestHeader(requestMessageHeader);
            new DoctorProfileAsyn(context).execute(performerDetailsReq);
            System.out.println("DOC_IIIDDDDDD 1" + PatientID);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private class DoctorProfileAsyn extends AsyncTask<PerformerDetailsReq, Integer, PerformerDetailsRes> {
        ProgressDialog ringProgressDialog;
        Context context;

        public DoctorProfileAsyn(Context context) {
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
//                    "", "Signing in, ...", true);

        }

        @Override
        protected PerformerDetailsRes doInBackground(PerformerDetailsReq... params) {
            PerformerDetailsRes result = (PerformerDetailsRes) HTTPUtils.getDataFromServer(params[0],
                    PerformerDetailsReq.class, PerformerDetailsRes.class, "PerformerDetailsServlet");
            return result;
        }

        @Override
        protected void onPostExecute(PerformerDetailsRes result) {
            super.onPostExecute(result);
            cancelWait();
            ProcessdocotrResponse(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }
    }

    public void ProcessdocotrResponse(PerformerDetailsRes result) {
        try {
            if (result == null) {

                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {

                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {
                doctorProfileCallBack.DoctorProfile(result.getPerformerId(), result.getClinicalAddress(), result.getClinicalZipcode(), result.getPerformerWorkingTimeModel());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//            LogTrace.e(TAG, e.getMessage());
        }
    }
}
