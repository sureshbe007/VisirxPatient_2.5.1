package com.visirx.patient.common.provider;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.DashBoardActivity;
import com.visirx.patient.activity.MyProfileActivity;
import com.visirx.patient.api.GcmReq;
import com.visirx.patient.api.GcmRes;
import com.visirx.patient.api.LoginReq;
import com.visirx.patient.api.LoginRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.CustomerProfileModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Suresh on 01-03-2016.
 */
public class LoginProvider {

    Context context;

    //rony 1.3.5 starts
    SharedPreferences sharedPreferences, gcmPreferences;
    boolean gcmStatus = false;
    String gcmId = null;
    private String reg;
    private GoogleCloudMessaging gcm;
    //rony 1.3.5 ends

    static final String TAG = LoginProvider.class.getName();
    String UserName, Password,gcmID;
    PendingIntent pendingIntent;

    public LoginProvider(Context context) {
        super();
        this.context = context;
    }

    public void LoginReqest(String userName, String password,String gcmID) {

        this.UserName = userName;
        this.Password = password;
        this.gcmID = gcmID;

        Log.e("LoginProvider", "Token2"+gcmID   +"===="+userName   +"======="+  password);
        LoginReq loginReq = null;
        try {
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);

//            gcmPreferences = context.getSharedPreferences(VTConstants.GCM_FILENAME, 0);
//
//            gcmStatus = gcmPreferences.getBoolean(VTConstants.GCM_STATUS, false);
//            gcmId = gcmPreferences.getString(VTConstants.GCM_ID, null);
//
//            //update gcm id for user if not exists
//
//            if (gcmStatus && gcmId != null) {
//                Log.d("SPIN", "Gcm is set for this user." + userName);
//            } else {
//                Log.d("SPIN", "Gcm is NOT SET for this user." + userName);
//
//                registerGcm();
//
//            }

            RequestHeader requestMessageHeader = new RequestHeader();
            loginReq = new LoginReq();
            loginReq.setUserName(UserName);
            loginReq.setPassword(Password);
            loginReq.setGcmid(gcmID);

            requestMessageHeader.setUserId(sharedPreferences.getString(VTConstants.USER_ID, null));
            loginReq.setRequestHeader(requestMessageHeader);
            new LogonTask(context).execute(loginReq);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }

    }

    private class LogonTask extends AsyncTask<LoginReq, Integer, LoginRes> {
        ProgressDialog ringProgressDialog;
        Context context;

        public LogonTask(Context context) {

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
                    "", "Signing in, ...", true);

        }

        @Override
        protected LoginRes doInBackground(LoginReq... params) {
            LoginRes result = (LoginRes) HTTPUtils.getDataFromServer(params[0], LoginReq.class, LoginRes.class, "CustomerUserLoginServlet");
            return result;
        }

        @Override
        protected void onPostExecute(LoginRes result) {
            super.onPostExecute(result);
            cancelWait();
            ProcessLoginResponse(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }
    }

    public void ProcessLoginResponse(LoginRes result) {


        try {


            if (result == null) {

                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {


                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {


                if (!TextUtils.isEmpty(result.getLoginModel().getFirstName()) && !TextUtils.isEmpty(result.getLoginModel().getLastName())
                        && !TextUtils.isEmpty(result.getLoginModel().getPhoneNumber()) && !TextUtils.isEmpty(result.getLoginModel().getEmailId())
                        && !TextUtils.isEmpty(result.getLoginModel().getZipcode()) && !TextUtils.isEmpty(result.getLoginModel().getUserAddress())
                        && !result.getLoginModel().getDob().equalsIgnoreCase("1900-01-01") && !TextUtils.isEmpty(result.getLoginModel().getDob())) {

                    sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(VTConstants.USER_ID, result.getLoginModel().getUserId());
                    editor.putString(VTConstants.USER_NAME, UserName);
                    editor.putString(VTConstants.PASSWORD, Password);
                    editor.putBoolean(VTConstants.LOGIN_STATUS, true);
                    editor.putString(VTConstants.LOGGED_USER_FULLNAME, result.getLoginModel().getFirstName() + " " + result.getLoginModel().getLastName());
                    editor.putString(VTConstants.TERMS_CONDTION, "");
                    editor.putString(VTConstants.PRIVACY_POLIcy, "");
                    editor.putString(VTConstants.CONTACT_US, "");
                    editor.commit();

//                    EventChecking.TracKLoginStatus(result.getLoginModel().getUserId(), true);
                    System.out.println("LOGIN PROVIDER  111  " + result.getLoginModel().getUserId() + "    " + result.getLoginModel().getFirstName());

                    //  Fabric  Method for  Login
                    EventChecking.myLogin(result.getLoginModel().getUserId(), result.getLoginModel().getFirstName() + " " + result.getLoginModel().getLastName(), true);
                    //send the generated gcm id to server for storage purpose.
                    if (gcmStatus && gcmId != null) {

                    } else {
                        gcmtoServer();
                    }

                    CustomerProfileModel customerProfileModel;
                    customerProfileModel = new CustomerProfileModel();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    Date date = new Date();
                    String CurrentDate = dateFormat.format(date);

                    customerProfileModel.setCustomerId(result.getLoginModel().getUserId());
                    customerProfileModel.setCustomerFirstName(result.getLoginModel().getFirstName());
                    customerProfileModel.setCustomerLastName(result.getLoginModel().getLastName());
                    customerProfileModel.setCustomerEmail(result.getLoginModel().getEmailId());
                    customerProfileModel.setCustomerMobileNumber(result.getLoginModel().getPhoneNumber());
                    customerProfileModel.setCustomerAddress(result.getLoginModel().getUserAddress());

                    customerProfileModel.setCustomerZipcode(result.getLoginModel().getZipcode());
                    customerProfileModel.setCustomerDateOfBirth(result.getLoginModel().getDob());

                    customerProfileModel.setCustomerGender(result.getLoginModel().getGender());
                    customerProfileModel.setCustomerPhoto(null);
//                    customerProfileModel.setCustomerPhoto(result.getLoginModel().get);
                    customerProfileModel.setLastCreated(CurrentDate);
                    customerProfileModel.setCustomerHeight("0");
                    customerProfileModel.setCustomerWeight("0");
                    VisirxApplication.userRegisterDAO.insertuserDetails(customerProfileModel);

                    ImageDownloaderProvider dwnlodProvider = new ImageDownloaderProvider(context, VTConstants.NOTIFICATION_LOGIN_USER_PROFILE);
                    dwnlodProvider.SendProfileImgReq(result.getLoginModel().getUserId());

                    Intent myprofile = new Intent(context, DashBoardActivity.class);
                    context.startActivity(myprofile);

                  //  QuickBlox(result.getLoginModel().getUserId(), result.getLoginModel().getFirstName());

                } else {

                    sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(VTConstants.USER_ID, result.getLoginModel().getUserId());
                    editor.putString(VTConstants.USER_NAME, UserName);
                    editor.putString(VTConstants.PASSWORD, Password);
                    editor.putBoolean(VTConstants.LOGIN_STATUS, true);
                    editor.putString(VTConstants.LOGGED_USER_FULLNAME, "");
                    editor.commit();

                    //send the generated gcm id to server for storage purpose.
                    if (gcmStatus && gcmId != null) {
                        Log.d("SPIN", "Gcm is set for this user.No need to update in server ." + UserName);
                    } else {
                        Log.d("SPIN", "Gcm is NOT SET for this user. sending update request to server." + UserName);

                        gcmtoServer();

                    }
                    CustomerProfileModel customerProfileModel;
                    customerProfileModel = new CustomerProfileModel();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    Date date = new Date();
                    String CurrentDate = dateFormat.format(date);

                    customerProfileModel.setCustomerId(result.getLoginModel().getUserId());

                    if (TextUtils.isEmpty(result.getLoginModel().getFirstName())) {
                        customerProfileModel.setCustomerFirstName("");
                    } else {
                        customerProfileModel.setCustomerFirstName(result.getLoginModel().getFirstName());
                    }
                    if (TextUtils.isEmpty(result.getLoginModel().getLastName())) {
                        customerProfileModel.setCustomerLastName("");
                    } else {
                        customerProfileModel.setCustomerLastName(result.getLoginModel().getLastName());
                    }

                    customerProfileModel.setCustomerEmail(result.getLoginModel().getEmailId());
                    customerProfileModel.setCustomerMobileNumber(result.getLoginModel().getPhoneNumber());

                    customerProfileModel.setCustomerAddress("");
                    customerProfileModel.setCustomerZipcode("");
                    customerProfileModel.setCustomerDateOfBirth("");
                    customerProfileModel.setCustomerGender("");
                    customerProfileModel.setCustomerPhoto(null);
                    customerProfileModel.setLastCreated(CurrentDate);
                    customerProfileModel.setCustomerHeight("0");
                    customerProfileModel.setCustomerWeight("0");

                    VisirxApplication.userRegisterDAO.insertuserDetails(customerProfileModel);

                    VisirxApplication.userRegisterDAO.updateLoggedUserThumbImagePath("", result.getLoginModel().getUserId());

                    System.out.println("LOGIN PROVIDER  222  " + result.getLoginModel().getUserId() + "    " + result.getLoginModel().getFirstName());
                    //  Fabric  Method for  Login
                    EventChecking.myLogin(result.getLoginModel().getUserId(), "FirstTime Login", true);
                    Intent myprofile = new Intent(context, MyProfileActivity.class);
                    context.startActivity(myprofile);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    // rony 1.3.5 starts
    private void registerGcm() {

        if (checkPlayServices()) {

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    String msg = "";
                    String regid = "";
                    try {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging
                                    .getInstance(context);
                        }
                        regid = gcm.register(VTConstants.SENDER_ID);
                        Log.d("GCM BG", msg);
                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();
                        Log.e("GCM BG", msg);
                    }
                    System.out.println("GCMID==" + regid);
                    reg = regid;

                    sharedPreferences = context.getSharedPreferences(
                            VTConstants.GCM_FILENAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(VTConstants.GCM_ID, reg);
                    editor.putBoolean(VTConstants.GCM_STATUS, true);
                    editor.commit();
                    Log.d("SPIN", "inside register gcm: gcm id : " + reg);


                    return null;
                }
            }.execute(null, null, null);

        }
        // return reg;
    }

    // playService Check

    private boolean checkPlayServices() {

        if (!deviceHasGoogleAccount()) {
//            DispalyToast("Add Google Account to your device ");
        }

        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
                        VTConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("GCM", "This device is not supported.");

            }
            return false;
        }
        return true;
    }
    // Google Account Check

    private boolean deviceHasGoogleAccount() {
        AccountManager accMan = AccountManager.get(context);
        Account[] accArray = accMan.getAccountsByType("com.google");
        return accArray.length >= 1 ? true : false;
    }
    // Single Toast for all place

    public void DispalyToast(String Str) {
        Toast.makeText(context, "" + Str, Toast.LENGTH_SHORT).show();
    }

    public void gcmtoServer() {

        Log.d("SPIN", "inside gcmtoServer: gcm id : ");
        GcmReq gcmReq = null;
        try {

            if (VTConstants.checkAvailability(context)) {

                gcmReq = new GcmReq();
                gcmPreferences = context.getSharedPreferences(VTConstants.GCM_FILENAME, 0);
                sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                gcmReq.setGcmid(gcmPreferences.getString(VTConstants.GCM_ID, null));
                gcmReq.setUsername(sharedPreferences.getString(VTConstants.USER_NAME, null));

                Log.d("SPIN", "Inside loginprovider : gcm id :" + gcmPreferences.getString(VTConstants.GCM_ID, null) + " || user name : " + sharedPreferences.getString(VTConstants.USER_NAME, null));

                new GcmTask(context).execute(gcmReq);

            } else {


            }
        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }

    }

    private class GcmTask extends AsyncTask<GcmReq, Integer, GcmRes> {
        Context context;
        ProgressDialog ringProgressDialog = null;

        public GcmTask(Context context) {
            System.out.println("LLL GCM 1");
            this.context = context;
            ringProgressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "PROCESS1");
            showWait();
            System.out.println("LLL GCM 2");
        }

        private void showWait() {
//            ringProgressDialog = ProgressDialog.show(context, "",
//                    "GCM  in Process , ...", true);

        }

        @Override
        protected GcmRes doInBackground(GcmReq... params) {
            GcmRes result = (GcmRes) HTTPUtils.getDataFromServer(params[0],
                    GcmReq.class, GcmRes.class, "GcmRegistrationServlet");
            return result;
        }

        @Override
        protected void onPostExecute(GcmRes result) {
            super.onPostExecute(result);
            cancelWait();
            ProcessGcmResponse(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
            Log.d(TAG, "PROCESS2");
        }

    }

    public void ProcessGcmResponse(GcmRes result) {

        System.out.println("GCM===ASYN" + result.getResponseHeader().getResponseCode());
        try {
            if (result == null) {
                Log.d(TAG, "PROCESS3");
                Popup.ShowErrorMessage(context, R.string.error_server_connect,
                        Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {

                System.out.println("LLL GCM 3");
                Popup.ShowErrorMessageString(context, result.getResponseHeader()
                        .getResponseMessage(), Toast.LENGTH_LONG);
                Log.d(TAG, "PROCESS4");
                return;
            } else {
                System.out.println("LLL GCM 4");
                System.out.println("GCM_Sent to Server");

            }
        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    // rony 1.3.5 ends
}