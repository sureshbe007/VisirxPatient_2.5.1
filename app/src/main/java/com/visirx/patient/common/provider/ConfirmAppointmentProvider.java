package com.visirx.patient.common.provider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.DashBoardActivity;
import com.visirx.patient.activity.MyProfileActivity;
import com.visirx.patient.api.ConfirmAppointmentReq;
import com.visirx.patient.api.ConfirmAppointmentRes;
import com.visirx.patient.api.FindDoctorReq;
import com.visirx.patient.api.FindDoctorRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.com.visirx.patient.adapter.FindDoctorCallback;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.ConfirmAppointmentModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by reveilleadmin on 3/12/2016.
 */
public class ConfirmAppointmentProvider {

    Context context;
    SharedPreferences sharedPreferences;
    String AppointmentID;
    String TrancationId;
    String TxAmount;
    Button Ok;

    public ConfirmAppointmentProvider(Context context) {
        super();
        this.context = context;

    }

    public void ConfirmAppointmentReq(ConfirmAppointmentModel confirmAppointmentModel) {

        ConfirmAppointmentReq confirmAppointmentReq = new ConfirmAppointmentReq();
        sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        String userId = sharedPreferences.getString(VTConstants.USER_ID, null);
        RequestHeader requestMessageHeader = new RequestHeader();
        requestMessageHeader.setUserId(userId);
        confirmAppointmentReq.setConfirmAppointmentModel(confirmAppointmentModel);
        confirmAppointmentReq.setRequestHeader(requestMessageHeader);
        AppointmentID = confirmAppointmentModel.getOrderid();
        TrancationId = confirmAppointmentModel.getTxnid();
        TxAmount = confirmAppointmentModel.getTxnamount();
        new ConfirmAppointmentTaskAsyn(context).execute(confirmAppointmentReq);

    }

    private class ConfirmAppointmentTaskAsyn extends AsyncTask<ConfirmAppointmentReq, Integer, ConfirmAppointmentRes> {
        Context context;

        public ConfirmAppointmentTaskAsyn(Context context) {
            this.context = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ConfirmAppointmentRes doInBackground(ConfirmAppointmentReq... params) {
            ConfirmAppointmentRes result = (ConfirmAppointmentRes) HTTPUtils.getDataFromServer(params[0], ConfirmAppointmentReq.class,
                    ConfirmAppointmentRes.class, "ConfirmAppointmentServlet");
            return result;
        }

        @Override
        protected void onPostExecute(ConfirmAppointmentRes result) {
            super.onPostExecute(result);
            try {
                if (result == null) {
                    Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                    return;
                } else if (result.getResponseHeader().getResponseCode() == -1) {
                    Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                    return;
                } else {
                    ///Success popup
                    //  Toast.makeText(context, "Appointment booked successful ", Toast.LENGTH_LONG).show();
                    //Redirect to Home Page
                    Paymentsuccess();
//                    showSimplePopUp();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
//            LogTrace.e(TAG, e.getMessage());
            }

        }


        private void Paymentsuccess() {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.appoinmentsuccess, null);
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
            NormalFont AppointmentId = (NormalFont) promptsView.findViewById(R.id.appointmentID);
            NormalFont transactionId = (NormalFont) promptsView.findViewById(R.id.transationId);
            NormalFont amount = (NormalFont) promptsView.findViewById(R.id.amount);
            Ok = (Button) promptsView.findViewById(R.id.ok);
            AppointmentId.setText(": " + AppointmentID);
            transactionId.setText(": " + TrancationId);
            amount.setText(": " + "₹ " + TxAmount);
            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder.setCancelable(false);
            final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            Ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                          // Fabric for Appointment Success
                    EventChecking.appointmentSuccess(AppointmentID,TrancationId,TxAmount,context);
                    Intent dashboardActivity = new Intent(context, DashBoardActivity.class);
                    context.startActivity(dashboardActivity);
                    dashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                }
            });
            alertDialog.show();

        }

    }
}
