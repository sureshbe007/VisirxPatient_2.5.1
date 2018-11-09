package com.visirx.patient.common.provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.api.ForgotNewpasswordReq;
import com.visirx.patient.api.ForgotNewpasswordRes;
import com.visirx.patient.api.ForgotPasswordOtpReq;
import com.visirx.patient.api.ForgotPasswordOtpRes;
import com.visirx.patient.api.ForgotPasswordReq;
import com.visirx.patient.api.ForgotPasswordRes;
import com.visirx.patient.api.RegisterRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.com.visirx.patient.adapter.NewPasswordCallBack;
import com.visirx.patient.com.visirx.patient.adapter.NewUserIdCallBack;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Suresh on 19-03-2016.
 */
public class ForgotPasswordProvider {

    Context context;
    static final String TAG = ForgotPasswordProvider.class.getName();

    NewPasswordCallBack newPasswordCallBack;
    NewUserIdCallBack newUserIdCallBack;

    public ForgotPasswordProvider(Context context, NewPasswordCallBack newPasswordCallBack, NewUserIdCallBack newUserIdCallBack) {
        super();
        this.newPasswordCallBack = newPasswordCallBack;
        this.newUserIdCallBack = newUserIdCallBack;
        this.context = context;
    }

    public void ForgotRequest(String userName) {
        try {
            ForgotPasswordReq forgotPasswordReq = new ForgotPasswordReq();
            forgotPasswordReq.setKeyword(userName);

            if (VTConstants.checkAvailability(context)) {
                new ForgotAsyn(context).execute(forgotPasswordReq);
            } else {
//                StartAppOffline();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }

    }

    private class ForgotAsyn extends AsyncTask<ForgotPasswordReq, Integer, ForgotPasswordRes> {

        Context context;
        ProgressDialog ringProgressDialog = null;

        public ForgotAsyn(Context context) {
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
                    "", "Please Wait....", true);

        }

        @Override
        protected ForgotPasswordRes doInBackground(ForgotPasswordReq... params) {
            ForgotPasswordRes result = (ForgotPasswordRes) HTTPUtils.getDataFromServer(params[0], ForgotPasswordReq.class,
                    ForgotPasswordRes.class, "ForgotPasswordAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(ForgotPasswordRes result) {
            super.onPostExecute(result);
            cancelWait();
            OtpResend(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }
    }

    public void OtpResend(ForgotPasswordRes result) {
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
                newPasswordCallBack.hideLayout(response);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    public void ForgotRequestOtp(String userName, int Otp) {
        try {
            ForgotPasswordOtpReq forgotPasswordReq = new ForgotPasswordOtpReq();
            forgotPasswordReq.setKeyword(userName);
            forgotPasswordReq.setOtp(Otp);
            if (VTConstants.checkAvailability(context)) {
                new ForgotNewAsyn(context).execute(forgotPasswordReq);
            } else {
//                StartAppOffline();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }


    }


    private class ForgotNewAsyn extends AsyncTask<ForgotPasswordOtpReq, Integer, ForgotPasswordOtpRes> {

        Context context;
        ProgressDialog ringProgressDialog = null;

        public ForgotNewAsyn(Context context) {
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
                    "", "Please Wait....", true);

        }

        @Override
        protected ForgotPasswordOtpRes doInBackground(ForgotPasswordOtpReq... params) {
            ForgotPasswordOtpRes result = (ForgotPasswordOtpRes) HTTPUtils.getDataFromServer(params[0], ForgotPasswordOtpReq.class,
                    ForgotPasswordOtpRes.class, "ForgotPasswordAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(ForgotPasswordOtpRes result) {
            super.onPostExecute(result);
            cancelWait();
            NewOtpResponse(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }
    }


    public void NewOtpResponse(ForgotPasswordOtpRes result) {
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
                newUserIdCallBack.NewUserId(result.getUserId(), response);
//                newPasswordCallBack.hideLayout(response);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    public void NewPasswordRequest(String UserID, String Password) {
        try {
            RequestHeader requestHeader = new RequestHeader();
            ForgotNewpasswordReq forgotNewpasswordReq = new ForgotNewpasswordReq();
            forgotNewpasswordReq.setNewPassword(Password);
            requestHeader.setUserId(UserID);
            forgotNewpasswordReq.setRequestHeader(requestHeader);
            System.out.println("NEWPASS 3" + UserID);
            System.out.println("NEWPASS 4" + Password);
            if (VTConstants.checkAvailability(context)) {
                new NewPassword(context).execute(forgotNewpasswordReq);
            } else {
//                StartAppOffline();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }


    }

    private class NewPassword extends AsyncTask<ForgotNewpasswordReq, Integer, ForgotNewpasswordRes> {

        Context context;
        ProgressDialog ringProgressDialog = null;

        public NewPassword(Context context) {
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
                    "", "Please Wait....", true);

        }

        @Override
        protected ForgotNewpasswordRes doInBackground(ForgotNewpasswordReq... params) {
            ForgotNewpasswordRes result = (ForgotNewpasswordRes) HTTPUtils.getDataFromServer(params[0], ForgotNewpasswordReq.class,
                    ForgotNewpasswordRes.class, "ForgetPasswordResetServlet");
            return result;
        }

        @Override
        protected void onPostExecute(ForgotNewpasswordRes result) {
            super.onPostExecute(result);
            cancelWait();
            Newpasswordresponse(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }
    }

    public void Newpasswordresponse(ForgotNewpasswordRes result) {
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
                System.out.println();
                Intent mainactivity = new Intent(context, MainActivity.class);
                context.startActivity(mainactivity);
//                newPasswordCallBack.hideLayout(response);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }
}
