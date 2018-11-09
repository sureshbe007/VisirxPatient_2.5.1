package com.visirx.patient.common.provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.DashBoardActivity;
import com.visirx.patient.api.CustomerProfileDataReq;
import com.visirx.patient.api.CustomerProfileDataRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.com.visirx.patient.adapter.ProfileSave;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.CustomerProfileModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Suresh on 28-02-2016.
 */
public class MyProfileProvider {

    CustomerProfileModel customerProfileModel;
    Context context;
    ProfileSave profileSave;
    SharedPreferences sharedPreferences, sharedPreferences1;
    static final String TAG = FindDoctorProvider.class.getName();
    String FirstName, LastName;

    public MyProfileProvider(Context context, ProfileSave profileSave) {
        super();
        this.profileSave = profileSave;
        this.context = context;
    }


    public void myProfileReq(String firstname, String lastname, String email, String mobile, String dateof_birth,
                             String address, String zipcode, String height, String weight, String gender
                            , String CurrentDate, byte[] userImage) {
        CustomerProfileDataReq customerProfileDataReq = null;
        try {


            System.out.println("address::" + address );

            FirstName = firstname;
            LastName = lastname;
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            sharedPreferences1 = context.getSharedPreferences(VTConstants.REGISTER_PREFRENCES, 0);
            RequestHeader requestMessageHeader = new RequestHeader();
            requestMessageHeader.setUserId(sharedPreferences.getString(VTConstants.USER_ID, null));
            customerProfileDataReq = new CustomerProfileDataReq();
            customerProfileDataReq.setCustomerId(sharedPreferences.getString(VTConstants.USER_ID, null));
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            String userId = sharedPreferences.getString(VTConstants.USER_ID, null);

            customerProfileDataReq.setCustomerFirstName(firstname);
            customerProfileDataReq.setCustomerLastName(lastname);
            customerProfileDataReq.setCustomeremail(email);
            customerProfileDataReq.setCustomer_mobilenumber(mobile);
            customerProfileDataReq.setCustomerDateOfBirth(dateof_birth);
            customerProfileDataReq.setCustomerAddress(address);
            customerProfileDataReq.setCustomerZipcode(zipcode);
            customerProfileDataReq.setCustomer_height(height);
            customerProfileDataReq.setCustomer_weight(weight);
            customerProfileDataReq.setCustomerGender(gender);
            customerProfileDataReq.setLastCreated(CurrentDate);
            customerProfileDataReq.setCustomerPhoto(userImage);

            customerProfileModel = new CustomerProfileModel();
            customerProfileModel.setCustomerId(userId);
            customerProfileModel.setCustomerFirstName(firstname);
            customerProfileModel.setCustomerLastName(lastname);
            customerProfileModel.setCustomerEmail(email);
            customerProfileModel.setCustomerMobileNumber(mobile);
            customerProfileModel.setCustomerAddress(address);
            customerProfileModel.setCustomerZipcode(zipcode);
            customerProfileModel.setCustomerDateOfBirth(dateof_birth);
            customerProfileModel.setCustomerGender(gender);
            customerProfileModel.setCustomerPhoto(userImage);
            customerProfileModel.setCustomerHeight(height);
            customerProfileModel.setCustomerWeight(weight);
            customerProfileModel.setLastCreated(CurrentDate);

            customerProfileDataReq.setRequestHeader(requestMessageHeader);
            if (VTConstants.checkAvailability(context)) {
                System.out.println("MY 2");
                new ProfileAsyn().execute(customerProfileDataReq);
            } else {
                System.out.println("MY OFF  2");
//                    StartAppOffline();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }


    private class ProfileAsyn extends AsyncTask<CustomerProfileDataReq, Integer, CustomerProfileDataRes> {
        ProgressDialog ringProgressDialog = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWait();
        }

        private void showWait() {
            ringProgressDialog = ProgressDialog.show(context,
                    "", "Profile Update, ...", true);

        }

        @Override
        protected CustomerProfileDataRes doInBackground(CustomerProfileDataReq... params) {
            CustomerProfileDataRes result = (CustomerProfileDataRes) HTTPUtils.getDataFromServer(params[0], CustomerProfileDataReq.class,
                    CustomerProfileDataRes.class, "SaveCustomerProfileDataAppServlet");
//            LoginRes result = (LoginRes) HTTPUtils.getDataFromServer(params[0], LoginReq.class,
//                    LoginRes.class, "UserLoginServlet");
            return result;
        }

        @Override
        protected void onPostExecute(CustomerProfileDataRes result) {
            super.onPostExecute(result);
            cancelWait();
            System.out.println("LL 3");
            ProcessMYprofile(result);
        }

        private void cancelWait() {
            ringProgressDialog.dismiss();
        }
    }

    public void ProcessMYprofile(CustomerProfileDataRes result) {
        try {

            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {
                sharedPreferences = context.getSharedPreferences(VTConstants.PROFILR_PREFERENCE, 0);
                SharedPreferences.Editor profileEditor = sharedPreferences.edit();
                profileEditor.putBoolean(VTConstants.PROFILE_STATUS, true);
                profileEditor.commit();
                sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putString(VTConstants.LOGGED_USER_FULLNAME, FirstName + " " + LastName);
                editor1.commit();
                String response = String.valueOf(result.getResponseHeader().getResponseCode());
                VisirxApplication.userRegisterDAO.insertuserDetails(customerProfileModel);

                sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(VTConstants.PROFILE_STATUS, true);
                profileSave.saveProfile(response);
                Intent redirect = new Intent(context, DashBoardActivity.class);
                redirect.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(redirect);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//            LogTrace.e(TAG, e.getMessage());
        }
    }
}

