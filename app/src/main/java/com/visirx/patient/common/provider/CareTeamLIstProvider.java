package com.visirx.patient.common.provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.CareTeamReq;
import com.visirx.patient.api.CareTeamRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Suresh on 09-03-2016.
 */
public class CareTeamLIstProvider {
    Context context;
    SharedPreferences sharedPreferences;
    static final String TAG = FindDoctorProvider.class.getName();

    public CareTeamLIstProvider(Context context) {
        super();
        this.context = context;
    }

    public void CareteamReq(String PatientID) {
        CareTeamReq careTeamReq = null;
        try {
            sharedPreferences = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            RequestHeader requestMessageHeader = new RequestHeader();
            careTeamReq = new CareTeamReq();
            careTeamReq.setCustomerId(PatientID);
            requestMessageHeader.setUserId(sharedPreferences.getString(VTConstants.USER_ID, null));
            careTeamReq.setRequestHeader(requestMessageHeader);
            if (VTConstants.checkAvailability(context)) {
                new allcareteamAsyn(context).execute(careTeamReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("DOC 300::" + e.getMessage());
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }

    }

    private class allcareteamAsyn extends AsyncTask<CareTeamReq, Integer, CareTeamRes> {

        ProgressDialog ringProgressDialog;
        Context context;

        public allcareteamAsyn(Context context) {
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
        protected CareTeamRes doInBackground(CareTeamReq... params) {
            CareTeamRes result = (CareTeamRes) HTTPUtils.getDataFromServer(params[0], CareTeamReq.class,
                    CareTeamRes.class, "CareTeamServlet");
            return result;
        }

        @Override
        protected void onPostExecute(CareTeamRes result) {
            super.onPostExecute(result);
            cancelWait();
            processCareTeam(result);
        }


        private void cancelWait() {
        }

    }

    public void processCareTeam(CareTeamRes result) {
        try {
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Logger.w("CARETEAMLISTPROVIDER", "getApptModel  -1 " + result.getResponseHeader().getResponseCode());
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {

                Logger.w("CARETEAMLISTPROVIDER", "getApptModel   " + result.getApptModel().size());


                for (int i = 0; i < result.getApptModel().size(); i++) {
                    VisirxApplication.customerDAO.insertFromservletCustomerDetails(result.getApptModel().get(i));
                    Logger.w("CARETEAMLISTPROVIDER","Doctor Id ::" + result.getApptModel().get(i).getPerfomerid() + " Customer Id ::" + result.getApptModel().get(i).getCustomerid());

                }


                Intent intent = new Intent(VTConstants.ALLAPPOINTMENT_BROADCAST);
                context.sendBroadcast(intent);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

}
