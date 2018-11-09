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
import com.visirx.patient.api.FindDoctorReq;
import com.visirx.patient.api.FindDoctorRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.com.visirx.patient.adapter.FindDoctorCallback;
import com.visirx.patient.com.visirx.patient.adapter.RecyclerAdapter_AddDoctors;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suresh on 28-02-2016.
 */
public class FindDoctorProvider {


    Context context;
    SharedPreferences sharedPreferences;
    static final String TAG = FindDoctorProvider.class.getName();
    FindDoctorCallback findDoctorCallback;

    public FindDoctorProvider(Context context, FindDoctorCallback findDoctorCallback) {
        super();
        this.context = context;
        this.findDoctorCallback = findDoctorCallback;
    }

    public void FindDoctorReq(String doctorName) {
        FindDoctorReq findDoctorReq = new FindDoctorReq();
        sharedPreferences = context.getSharedPreferences(
                VTConstants.LOGIN_PREFRENCES_NAME, 0);

        RequestHeader requestMessageHeader = new RequestHeader();
        requestMessageHeader.setUserId(sharedPreferences.getString(VTConstants.USER_ID, null));
        findDoctorReq.setDoctorId(doctorName);
        findDoctorReq.setRequestHeader(requestMessageHeader);
        Log.d("FindDoctorProvider", "doctorName:  " + doctorName);
        Log.d("FindDoctorProvider","User ID    :   " +sharedPreferences.getString(VTConstants.USER_ID, null));
        new AddDoctorAsyn(context).execute(findDoctorReq);


    }

    private class AddDoctorAsyn extends AsyncTask<FindDoctorReq, Integer, FindDoctorRes> {
        ProgressDialog ringProgressDialog;
        Context context;

        public AddDoctorAsyn(Context context) {
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
                    "", "please wait Search a Doctor, ...", true);

        }

        @Override
        protected FindDoctorRes doInBackground(FindDoctorReq... params) {
            FindDoctorRes result = (FindDoctorRes) HTTPUtils.getDataFromServer(params[0], FindDoctorReq.class,
                    FindDoctorRes.class, "FindDoctorServlet");
            return result;
        }

        @Override
        protected void onPostExecute(FindDoctorRes result) {
            super.onPostExecute(result);
            cancelWait();
            searchDoctorprocess(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }
    }

    public void searchDoctorprocess(FindDoctorRes result) {

        try {
            Log.d("FindDoctorProvider", "searchDoctorprocess:  " + result);
            if (result == null) {
                Log.d("FindDoctorProvider", "result == null:  " + result);
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Log.d("FindDoctorProvider", "result =-1 :  " + result);

                Logger.w("TIMESLOTRESULT  -1 : ", "DoctorName " + result.getResponseHeader().getResponseMessage());
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                findDoctorCallback.doctorlist(result.getFindDoctorModel(),-1);
                return;
            } else {

                Log.d("FindDoctorProvider", "result ==0 :  " + result);
                if (result.getFindDoctorModel().size() > 0) {
                    for (int i = 0; i < result.getFindDoctorModel().size(); i++) {
                        EventChecking.SearchDoctor(result.getFindDoctorModel().get(i).getDoctorId(),
                                "" + result.getFindDoctorModel().get(i).getDoctorFirstName() + "" + result.getFindDoctorModel().get(i).getDoctorLastName(), context);

                    }
                }
                findDoctorCallback.doctorlist(result.getFindDoctorModel(),0);
                Logger.w("FINDOCTOR", "DocNAme  " + result.getFindDoctorModel().get(0).getDoctorFirstName());
                Logger.w("FINDOCTOR", "getDoctorId    " + result.getFindDoctorModel().get(0).getDoctorId());
                Logger.w("FINDOCTOR", "getDoctorfee  " + result.getFindDoctorModel().get(0).getDoctorfee());
                Logger.w("FINDOCTOR", "getDoctorSpecialization   " + result.getFindDoctorModel().get(0).getDoctorSpecialization());
                Intent intent = new Intent(VTConstants.FIND_DOCTOR_BRODCAST);
                context.sendBroadcast(intent);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

}



