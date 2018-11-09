package com.visirx.patient.common.provider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.DeleteDoctorReq;
import com.visirx.patient.api.DeleteDoctorRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Suresh on 04-04-2016.
 */
public class DeleteDoctorProvider {

    Context context;
    SharedPreferences sharedPreferences;
    static final String TAG = DeleteDoctorProvider.class.getName();
    String DoctorId;

    public DeleteDoctorProvider(Context context) {
        super();
        this.context = context;

    }


    public void DeleteDoctorRequest(String DoctorID) {
        DoctorId = DoctorID;
        DeleteDoctorReq deleteDoctorReq = new DeleteDoctorReq();
        sharedPreferences = context.getSharedPreferences(
                VTConstants.LOGIN_PREFRENCES_NAME, 0);


        RequestHeader requestMessageHeader = new RequestHeader();
        requestMessageHeader.setUserId(sharedPreferences.getString(VTConstants.USER_ID, null));
        deleteDoctorReq.setDoctorID(DoctorID);
        deleteDoctorReq.setRequestHeader(requestMessageHeader);
        new deleteDoctorAsyn(context).execute(deleteDoctorReq);

//        Logger.w("DELETEDOC_PROVIDER", "DOCTOR ID" + DoctorID);
//        Logger.w("DELETEDOC_PROVIDER", "USER ID" + sharedPreferences.getString(VTConstants.USER_ID, null));


    }

    private class deleteDoctorAsyn extends AsyncTask<DeleteDoctorReq, Integer, DeleteDoctorRes> {
        ProgressDialog ringProgressDialog;
        Context context;

        public deleteDoctorAsyn(Context context) {
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
                    "", "please wait, ...", true);

        }

        @Override
        protected DeleteDoctorRes doInBackground(DeleteDoctorReq... params) {
            DeleteDoctorRes result = (DeleteDoctorRes) HTTPUtils.getDataFromServer(params[0], DeleteDoctorReq.class,
                    DeleteDoctorRes.class, "RemoveCareTeamServlet");
            return result;
        }

        @Override
        protected void onPostExecute(DeleteDoctorRes result) {
            super.onPostExecute(result);
            cancelWait();
            DeleteDocotrProcess(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }
    }

    public void DeleteDocotrProcess(DeleteDoctorRes result) {

        try {

            if (result == null) {

                Logger.w("DELETEDOC_PROVIDER", "Null" + result.getResponseHeader().getResponseCode());
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {

                Logger.w("DELETEDOC_PROVIDER", "-1" + result.getResponseHeader().getResponseCode());
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {

                Logger.w("DELETEDOC_PROVIDER", "RESPONSE_STAUTS" + result.getResponseHeader().getResponseCode());
                Logger.w("DELETEDOC_PROVIDER", "DOCTOR ID" + DoctorId);


                VisirxApplication.customerDAO.UpdateDoctor(DoctorId);
//                Intent deletedoctor = new Intent(VTConstants.ALL_DOCTOR);
//                context.sendBroadcast(deletedoctor);
                Intent deletedoctor = new Intent(VTConstants.NOTIFICATION_APPTS);
                context.sendBroadcast(deletedoctor);


            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }
}
