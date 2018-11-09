package com.visirx.patient.common.provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.activity.BookAppointActivity;
import com.visirx.patient.api.GetTimeslotsForPerformerReq;
import com.visirx.patient.api.GetTimeslotsForPerformerRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.com.visirx.patient.adapter.TimeSlotCallback;
import com.visirx.patient.com.visirx.patient.adapter.callBack;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AvailableTimeslotsModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Suresh on 28-02-2016.
 */
public class AlldoctorsListProvider {

    Context context;
    SharedPreferences sharedPreferences;
    String CurrentDate;
    callBack callback;
    TimeSlotCallback timecallback;
    List<AvailableTimeslotsModel> temp;
    static final String TAG = FindDoctorProvider.class.getName();

    public AlldoctorsListProvider(Context context, callBack callback) {
        super();
        this.context = context;
        this.callback = callback;
    }

    public void ListallDoctorReq(String PerformerID, String Time) {
        GetTimeslotsForPerformerReq getTimeslotsForPerformerReq = null;
        try {
            sharedPreferences = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            getTimeslotsForPerformerReq = new GetTimeslotsForPerformerReq();

            getTimeslotsForPerformerReq.setPerformerId(PerformerID);
            getTimeslotsForPerformerReq.setAppointmentDate(Time);
            RequestHeader requestMessageHeader = new RequestHeader();
            requestMessageHeader.setUserId(sharedPreferences.getString(VTConstants.USER_ID, null));
            getTimeslotsForPerformerReq.setRequestHeader(requestMessageHeader);
            if (VTConstants.checkAvailability(context)) {
                new getTimeslot().execute(getTimeslotsForPerformerReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("DOC 300::" + e.getMessage());
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }

    }


    private class getTimeslot extends AsyncTask<GetTimeslotsForPerformerReq, Integer, GetTimeslotsForPerformerRes> {
//        ProgressDialog ringProgressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWait();
        }

        private void showWait() {
//            ringProgressDialog = ProgressDialog.show(context,
//                    "", "please wait Booking Appointment, ...", true);
            System.out.println("B 2");
        }

        @Override
        protected GetTimeslotsForPerformerRes doInBackground(GetTimeslotsForPerformerReq... params) {
            GetTimeslotsForPerformerRes result = (GetTimeslotsForPerformerRes) HTTPUtils.getDataFromServer(params[0], GetTimeslotsForPerformerReq.class,
                    GetTimeslotsForPerformerRes.class, "GetTimeslotsForPerformerAppServlet");
//            LoginRes result = (LoginRes) HTTPUtils.getDataFromServer(params[0], LoginReq.class,
//                    LoginRes.class, "UserLoginServlet");
            return result;
        }

        @Override
        protected void onPostExecute(GetTimeslotsForPerformerRes result) {
            super.onPostExecute(result);
            cancelWait();
            processBookAppointment(result);
        }


        private void cancelWait() {
//            ringProgressDialog.dismiss();
        }

    }


    public void processBookAppointment(GetTimeslotsForPerformerRes result) {
        try {
            if (result == null) {

                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                temp = null;

                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);

                return;
            } else {


                callback.dataLoad(result.getAvailableTimeslotsModel(), result.getReservationMode(),
                        result.getResponseHeader().getResponseCode(), result.isOfferAvailable(), result.getOfferMainText(), result.getOfferSubText(),result.isNoFee());
                Intent intent = new Intent(VTConstants.TIMSLOT_BROADCAST);
                context.sendBroadcast(intent);


            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//            LogTrace.e(TAG, e.getMessage());
        }
    }

}
