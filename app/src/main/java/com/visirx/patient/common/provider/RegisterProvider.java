package com.visirx.patient.common.provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.api.GcmReq;
import com.visirx.patient.api.GcmRes;
import com.visirx.patient.api.OtpReq;
import com.visirx.patient.api.OtpRes;
import com.visirx.patient.api.RegisterReq;
import com.visirx.patient.api.RegisterRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.com.visirx.patient.adapter.Callbackui;
import com.visirx.patient.com.visirx.patient.adapter.callBack;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.Register;
import com.visirx.patient.utils.VTConstants;

import javax.security.auth.callback.Callback;

/**
 * Created by Suresh on 01-03-2016.
 */
public class RegisterProvider {

    Context context;
    Callbackui callbackui;
    SharedPreferences sharedPreferences;
    static final String TAG = RegisterProvider.class.getName();
    String username, email, mobileNumber, password, conformpassword;
    Button Ok;

    public RegisterProvider(Context context, Callbackui callbackui) {
        super();
        this.callbackui = callbackui;
        this.context = context;
    }
    // Register process

    public void RegisterReq(String username, String email, String mobileNumber, String password, String conformpassword) {
        RegisterReq regReq = null;
        try {
            this.username = username;
            this.email = email;
            this.mobileNumber = mobileNumber;
            this.password = password;
            this.conformpassword = conformpassword;
            VTConstants.USER_NAME1 = username;

            regReq = new RegisterReq();
            RequestHeader requestMessageHeader = new RequestHeader();
            regReq = new RegisterReq();
            regReq.setUserName(username);
            regReq.setEmail(email);
            regReq.setPhone(mobileNumber);
            regReq.setPass(password);
            regReq.setConPass(conformpassword);
            new RegisterAsyn(context).execute(regReq);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }


    private class RegisterAsyn extends AsyncTask<RegisterReq, Integer, RegisterRes> {

        Context context;
        ProgressDialog ringProgressDialog = null;

        public RegisterAsyn(Context context) {
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
                    "", "Sign Up in process, ...", true);

        }

        @Override
        protected RegisterRes doInBackground(RegisterReq... params) {
            RegisterRes result = (RegisterRes) HTTPUtils.getDataFromServer(params[0], RegisterReq.class,
                    RegisterRes.class, "RegisterCustomerAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(RegisterRes result) {
            super.onPostExecute(result);
            cancelWait();
            ProcessRegisterResponse(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();

        }
    }

    public void ProcessRegisterResponse(RegisterRes result) {
        try {
            // TODO Auto-generated method stub
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {
                String response = String.valueOf(result.getResponseHeader().getResponseCode());
                callbackui.hide(response);
//                StartHomePage(result.getRegisterModel());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }


    public void OtpReq(String userName, String otp) {
        OtpReq otpreq = null;
        try {
            otpreq = new OtpReq();
            otpreq.setUserName(userName);
            otpreq.setOtp(Integer.parseInt(otp));

            if (VTConstants.checkAvailability(context)) {
                new OtpAsyn(context).execute(otpreq);
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }


    private class OtpAsyn extends AsyncTask<OtpReq, Integer, OtpRes> {

        Context context;
        ProgressDialog ringProgressDialog = null;

        public OtpAsyn(Context context) {

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
                    "", "Checking OTP ...", true);

        }

        @Override
        protected OtpRes doInBackground(OtpReq... params) {
            OtpRes result = (OtpRes) HTTPUtils.getDataFromServer(params[0], OtpReq.class,
                    OtpRes.class, "ValidateOtpAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(OtpRes result) {
            super.onPostExecute(result);
            cancelWait();
            Otpresponse(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }
    }

    public void Otpresponse(OtpRes result) {
//        System.out.println("FFF===" + result.getResponseHeader().getResponseCode());

        try {
            // TODO Auto-generated method stub
            if (result == null) {

                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {
                sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(VTConstants.USER_ID, result.getUserId());
                editor.putBoolean(VTConstants.TERMS_CONDTIONS, true);
                editor.commit();
                sharedPreferences = context.getSharedPreferences(VTConstants.REGISTER_PREFRENCES, 0);
                SharedPreferences.Editor registerEditor = sharedPreferences.edit();
                registerEditor.putBoolean(VTConstants.REGISTER_STATUS, true);
                registerEditor.commit();
//                 gcmtoServer();
                // Fabric Custom SignUp

                System.out.println("REGPROVIDER  1111"+result.getUserId()+"   USNER NAME  "+result.getUserId());
                EventChecking.mySignUp(result.getUserId(), true);
                OtpSuccessalert();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

//    public void gcmtoServer() {
//        GcmReq gcmReq = null;
//        try {
//            if (VTConstants.checkAvailability(context)) {
//                sharedPreferences = context.getSharedPreferences(
//                        VTConstants.GCM_FILENAME, 0);
//                gcmReq = new GcmReq();
//                gcmReq.setGcmid(sharedPreferences.getString(VTConstants.GCM_ID, null));
//                gcmReq.setUsername(VTConstants.USER_NAME1);
//                new GcmTask(context).execute(gcmReq);
//            } else {
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogTrace.e(TAG, e.getMessage());
//        }
//
//    }
//
//    private class GcmTask extends AsyncTask<GcmReq, Integer, GcmRes> {
//        Context context;
//        ProgressDialog ringProgressDialog = null;
//
//        public GcmTask(Context context) {
//            this.context = context;
//            ringProgressDialog = new ProgressDialog(context);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Log.d(TAG, "PROCESS1");
//            showWait();
//
//        }
//
//        private void showWait() {
////            ringProgressDialog = ProgressDialog.show(context, "",
////                    "GCM  in Process , ...", true);
//        }
//
//        @Override
//        protected GcmRes doInBackground(GcmReq... params) {
//            GcmRes result = (GcmRes) HTTPUtils.getDataFromServer(params[0],
//                    GcmReq.class, GcmRes.class, "GcmRegistrationServlet");
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(GcmRes result) {
//            super.onPostExecute(result);
//            cancelWait();
//            ProcessGcmResponse(result);
//        }
//
//        private void cancelWait() {
//            ringProgressDialog.dismiss();
//            Log.d(TAG, "PROCESS2");
//        }
//
//    }
//
//    public void ProcessGcmResponse(GcmRes result) {
//        try {
//            if (result == null) {
//                Log.d(TAG, "PROCESS3");
//                Popup.ShowErrorMessage(context, R.string.error_server_connect,
//                        Toast.LENGTH_SHORT);
//                return;
//            } else if (result.getResponseHeader().getResponseCode() != 0) {
//                Popup.ShowErrorMessageString(context, result.getResponseHeader()
//                        .getResponseMessage(), Toast.LENGTH_LONG);
//                Log.d(TAG, "PROCESS4");
//                return;
//            } else {
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogTrace.e(TAG, e.getMessage());
//        }
//    }

    public void otpResend(String username, String email, String mobileNumber, String password, String conformpassword)
    {
        RegisterReq regReq = null;
        try {
            RequestHeader requestMessageHeader = new RequestHeader();
            regReq = new RegisterReq();
            regReq.setUserName(username);
            regReq.setEmail(email);
            regReq.setPhone(mobileNumber);
            regReq.setPass(password);
            regReq.setConPass(conformpassword);
            if (VTConstants.checkAvailability(context)) {
                new OtpResendAsyn(context).execute(regReq);
            } else {
//                StartAppOffline();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private class OtpResendAsyn extends AsyncTask<RegisterReq, Integer, RegisterRes> {

        Context context;
        ProgressDialog ringProgressDialog = null;

        public OtpResendAsyn(Context context) {
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
                    "", "Checking OTP ...", true);

        }

        @Override
        protected RegisterRes doInBackground(RegisterReq... params) {
            RegisterRes result = (RegisterRes) HTTPUtils.getDataFromServer(params[0], RegisterReq.class,
                    RegisterRes.class, "ResendOtpAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(RegisterRes result) {
            super.onPostExecute(result);
            cancelWait();
            OtpResend(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }
    }

    public void OtpResend(RegisterRes result) {
        try {
            // TODO Auto-generated method stub
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {


                Log.w("OTPRESEND","OTPRESEND log  "+result.getRegisterModels().get(0).getUserId());
                EventChecking.mySignUp(result.getRegisterModels().get(0).getUserId(), true);
                String response = String.valueOf(result.getResponseHeader().getResponseCode());
                callbackui.hide(response);
//                RegLayout.setVisibility(View.GONE);
//                VeryfyLayout.setVisibility(View.VISIBLE);
//                StartHomePage(result.getRegisterModel());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }


    private void OtpSuccessalert() {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.otpsuccess_alertbox, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        Ok = (Button) promptsView.findViewById(R.id.ok);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.w(TAG, " Ok button Pressed");
                Intent logInt = new Intent(context, MainActivity.class);
                context.startActivity(logInt);

            }
        });
        alertDialog.show();

    }
}