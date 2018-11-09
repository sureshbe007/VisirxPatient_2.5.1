package com.visirx.patient.common.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.BookAppointActivity;
import com.visirx.patient.activity.ConformAppointmentActivity;
import com.visirx.patient.activity.DashBoardActivity;
import com.visirx.patient.api.BookAppointmentReq;
import com.visirx.patient.api.BookAppointmentRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.fragment.AllappointmentFragment;
import com.visirx.patient.model.ConfirmAppointmentModel;
import com.visirx.patient.model.CustomerProfileModel;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Suresh on 28-02-2016.
 */
public class BookappointmentProvider {

    Context context;
    String PatientID;
    SharedPreferences sharedPreferences;
    CustomerProfileModel customerProfileModel;
    static final String TAG = FindDoctorProvider.class.getName();
    private String Offer_appointment;
    private boolean ISNofee;
    private String AppointmentID;
    private String MID;
    private String cusId;
    private String channelId;
    private String industryTypeId;
    private String website;
    private String theme;
    private String performerFee;
    private String visiRxFee;
    private String totalFee;
    private String serviceTax;
    private String generateChecksumUrl;
    private String verifyChecksumUrl;
    android.app.AlertDialog.Builder alertDialogBuilder;
    android.app.AlertDialog alertDialog;
    Button trayagain, Ok;

    public BookappointmentProvider(Context context) {
        super();
        this.context = context;
    }


    public void crteateAppointmet(String symptoms, String timeslot, String date, String useraddress
            , String zipcode, String doctorid, String doctorfees, String appointMode, String appointmentoffer,boolean isNoFee) {
        BookAppointmentReq bookAppointmentReq = null;
        try {

            Log.d("BookappointmentProvider","crteateAppointmet() :"+isNoFee);
            Offer_appointment = appointmentoffer;
            ISNofee = isNoFee;
            sharedPreferences = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            PatientID = sharedPreferences.getString(VTConstants.USER_ID, "null");
            RequestHeader requestMessageHeader = new RequestHeader();
            requestMessageHeader.setUserId(PatientID);
            bookAppointmentReq = new BookAppointmentReq();
            bookAppointmentReq.setPerformerId(doctorid);
            bookAppointmentReq.setCustomerAddress(useraddress);
            bookAppointmentReq.setCustomerSymptoms(symptoms);
            bookAppointmentReq.setAppointmentType(appointMode);
            bookAppointmentReq.setCustomerZipcode(zipcode);
            bookAppointmentReq.setReservationDate(date);
            bookAppointmentReq.setReservedTimeslot(timeslot);
            bookAppointmentReq.setPaidAmount(Integer.parseInt(doctorfees));
            customerProfileModel = VisirxApplication.userRegisterDAO.getUserDetails(PatientID);
            bookAppointmentReq.setCustomerAddress(useraddress);
            bookAppointmentReq.setRequestHeader(requestMessageHeader);
            new bookappointment().execute(bookAppointmentReq);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("DOC 300::" + e.getMessage());
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private class bookappointment extends AsyncTask<BookAppointmentReq, Integer, BookAppointmentRes> {
//        ProgressDialog ringProgressDialog;
//        Context context;
//
//        public bookappointment(Context context) {
//            this.context = context;
//            ringProgressDialog = new ProgressDialog(context);
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWait();
        }

        private void showWait() {
//            ringProgressDialog = ProgressDialog.show(context,
//                    "", "please wait  confirm an Appointment, ...", true);
            System.out.println("B 2");
        }

        @Override
        protected BookAppointmentRes doInBackground(BookAppointmentReq... params) {
            BookAppointmentRes result = (BookAppointmentRes) HTTPUtils.getDataFromServer(params[0], BookAppointmentReq.class,
                    BookAppointmentRes.class, "BookAppointmentAppServlet");
//            LoginRes result = (LoginRes) HTTPUtils.getDataFromServer(params[0], LoginReq.class,
//                    LoginRes.class, "UserLoginServlet");
            return result;
        }

        @Override
        protected void onPostExecute(BookAppointmentRes result) {
            super.onPostExecute(result);
            cancelWait();
            processBookAppointment(result);
        }

        private void cancelWait() {
//            ringProgressDialog.dismiss();
        }
    }

    public void processBookAppointment(BookAppointmentRes result) {
        try {
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                return;
            } else {
                AppointmentID = result.getAppointmentId();
                MID = result.getMID();
                cusId = PatientID;
                channelId = result.getChannelId();
                industryTypeId = result.getIndustryTypeId();
                website = result.getWebsite();
                theme = result.getTheme();
                performerFee = result.getPerformerFee();
                visiRxFee = result.getVisiRxFee();
                totalFee = result.getTotalFee();
                serviceTax = result.getServiceTax();
                generateChecksumUrl = result.getGenerateChecksumUrl();
                verifyChecksumUrl = result.getVerifyChecksumUrl();


                if (Offer_appointment.equalsIgnoreCase("true") || ISNofee==true)
                {

                    Discountalertbox(AppointmentID);

                } else {
                    //       showSimplePopUp();
                    Feesumary(AppointmentID);
                }

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//            LogTrace.e(TAG, e.getMessage());
        }
    }


    void payMent() {

        PaytmPGService Service = PaytmPGService.getStagingService();
//        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<String, String>();
        // these are mandatory parameters
        paramMap.put("ORDER_ID", AppointmentID);
        paramMap.put("MID", MID);
        paramMap.put("CUST_ID", cusId);
        paramMap.put("CHANNEL_ID", channelId);
        paramMap.put("INDUSTRY_TYPE_ID", industryTypeId);
        paramMap.put("WEBSITE", website);
        paramMap.put("TXN_AMOUNT", totalFee);
        paramMap.put("THEME", theme);
        paramMap.put("EMAIL", "abc@gmail.com");
        paramMap.put("MOBILE_NO", "123");
        PaytmOrder Order = new PaytmOrder(paramMap);
        PaytmMerchant Merchant = new PaytmMerchant(
                generateChecksumUrl,
                verifyChecksumUrl);
        Service.initialize(Order, Merchant, null);
        Service.startPaymentTransaction(context, true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void someUIErrorOccurred(String inErrorMessage) {
                // Some UI Error Occurred in Payment Gateway Activity.
                // // This may be due to initialization of views in
                // Payment Gateway Activity or may be due to //
                // initialization of webview. // Error Message details
                // the error occurred.
                Intent dashboardActivity = new Intent(context, DashBoardActivity.class);
                context.startActivity(dashboardActivity);
                dashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Toast.makeText(context, "Some UI Error Occurred.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionSuccess(Bundle inResponse) {
                // After successful transaction this method gets called.
                // // Response bundle contains the merchant response
                // parameters.
                ConfirmAppointmentModel confirmAppointmentModel = new ConfirmAppointmentModel();
                confirmAppointmentModel.setStatus(inResponse.getString("STATUS"));
                confirmAppointmentModel.setBankname(inResponse.getString("BANKNAME"));
                confirmAppointmentModel.setOrderid(inResponse.getString("ORDERID"));
                confirmAppointmentModel.setTxnamount(inResponse.getString("TXNAMOUNT"));
                confirmAppointmentModel.setTxndate(inResponse.getString("TXNDATE"));
                confirmAppointmentModel.setMid(inResponse.getString("MID"));
                confirmAppointmentModel.setTxnid(inResponse.getString("TXNID"));
                confirmAppointmentModel.setRespcode(inResponse.getString("RESPCODE"));
                confirmAppointmentModel.setPaymentmode(inResponse.getString("PAYMENTMODE"));
                confirmAppointmentModel.setBanktxnid(inResponse.getString("BANKTXNID"));
                confirmAppointmentModel.setCurrency(inResponse.getString("CURRENCY"));
                confirmAppointmentModel.setGatewayname(inResponse.getString("GATEWAYNAME"));
                confirmAppointmentModel.setChecksumhash(inResponse.getString("IS_CHECKSUM_VALID"));
                confirmAppointmentModel.setRespmsg(inResponse.getString("RESPMSG"));
                ConfirmAppointmentProvider confirmAppointmentProvider = new ConfirmAppointmentProvider(context);
                confirmAppointmentProvider.ConfirmAppointmentReq(confirmAppointmentModel);
                Log.d("LOG", "Payment Transaction is successful " + inResponse);
                // Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionFailure(String inErrorMessage,
                                             Bundle inResponse) {
                // This method gets called if transaction failed. //
                // Here in this case transaction is completed, but with
                // a failure. // Error Message describes the reason for
                // failure. // Response bundle contains the merchant
                // response parameters.
//                String  ErrorStatus=inErrorMessage;
                Failure();
                Log.d("LOG", "Payment Transaction Failed " + "AAAA" + inResponse.toString());
                Toast.makeText(context, "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void networkNotAvailable() { // If network is not
                // available, then this
                // method gets called.
                Intent dashboardActivity = new Intent(context, DashBoardActivity.class);
                context.startActivity(dashboardActivity);
                dashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Toast.makeText(context, "Network Not Available", Toast.LENGTH_LONG).show();
            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {
                // This method gets called if client authentication
                // failed. // Failure may be due to following reasons //
                // 1. Server error or downtime. // 2. Server unable to
                // generate checksum or checksum response is not in
                // proper format. // 3. Server failed to authenticate
                // that client. That is value of payt_STATUS is 2. //
                // Error Message describes the reason for failure.
            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Intent dashboardActivity = new Intent(context, DashBoardActivity.class);
                context.startActivity(dashboardActivity);
                dashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Toast.makeText(context, "Error Loading WebPage", Toast.LENGTH_LONG).show();
            }

            // had to be added: NOTE
            @Override
            public void onBackPressedCancelTransaction() {
                // TODO Auto-generated method stub

                Intent dashboardActivity = new Intent(context, DashBoardActivity.class);
                context.startActivity(dashboardActivity);
                dashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Toast.makeText(context, "Payment Transaction Cancelled ", Toast.LENGTH_LONG).show();
            }

        });

    }


    private void Discountalertbox(String appointmentID) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.offerappointmentsuccess, null);
        NormalFont appointID = (NormalFont) promptsView.findViewById(R.id.appointmentID);
        appointID.setText(appointmentID);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        Ok = (Button) promptsView.findViewById(R.id.offerok);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboardActivity = new Intent(context, DashBoardActivity.class);
                context.startActivity(dashboardActivity);
                dashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void Feesumary(final String appointmentId) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.paymentsummary, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        NormalFont AppointID = (NormalFont) promptsView.findViewById(R.id.appointmentID);
        NormalFont DocotrFees = (NormalFont) promptsView.findViewById(R.id.doctorfees);
        NormalFont PlatformFees = (NormalFont) promptsView.findViewById(R.id.Platformfees);
        NormalFont TotalFees = (NormalFont) promptsView.findViewById(R.id.Totoalamouunt);
        Button Paynow = (Button) promptsView.findViewById(R.id.paynow);
        NormalFont tax = (NormalFont) promptsView.findViewById(R.id.tax);
        Button Cancelappoint = (Button) promptsView.findViewById(R.id.Cancelappoint);

        AppointID.setText(": " + appointmentId);
        DocotrFees.setText(": " + "₹ " + performerFee);
        PlatformFees.setText(": " + "₹ " + visiRxFee);
        TotalFees.setText(": " + "₹ " + totalFee);
//        tax.setText("(Inclusive of " + serviceTax + "%" + " Service Tax)");
        tax.setText("");
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        Paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VTConstants.checkAvailability(context)) {
                    SnackBarhide();
                    alertDialog.dismiss();
                    payMent();
                } else {
                    SnackBar();
                }


            }
        });
        Cancelappoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboardActivity = new Intent(context, DashBoardActivity.class);
                context.startActivity(dashboardActivity);
                dashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });
        alertDialog.show();

    }


    private void Failure() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.transactionfailure, null);
        alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        trayagain = (Button) promptsView.findViewById(R.id.tryagain);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        trayagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboardActivity = new Intent(context, DashBoardActivity.class);
                context.startActivity(dashboardActivity);
                dashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            }
        });
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    public void SnackBar() {
        SnackbarManager.show(
                Snackbar.with(context)
                        .text("No InterNet Connection...")
                        .actionLabel("Close")
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)//
                        .color(Color.BLACK)
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                Log.d(TAG, "Undoing something");

                            }
                        })
                , (Activity) context);
    }

    public void SnackBarhide() {
        SnackbarManager.dismiss();
    }


}
