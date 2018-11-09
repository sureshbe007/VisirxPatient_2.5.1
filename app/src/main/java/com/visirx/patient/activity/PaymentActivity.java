package com.visirx.patient.activity;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.VTConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView doctorName, specialist, visitMode, Date, Time, ammount, Billsummary;
    Button paybtn;
    String DoctorID;
    String AppointmentID;
    AppointmentPatientModel appointmentPatientModel;
    FindDoctorModel findDoctorModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initvalues();
    }

    public void initvalues() {
        if (getIntent() != null) {
            AppointmentID = (getIntent().getStringExtra(VTConstants.APPOINTMENTID));
            DoctorID = (getIntent().getStringExtra(VTConstants.DOCTORID));
            System.out.println("ZZZZZZZZZZZ 4   " + AppointmentID + "==========" + DoctorID);

        }
        appointmentPatientModel = VisirxApplication.aptDAO.GetAppointmentsByID(Integer.parseInt(AppointmentID));
        findDoctorModel = VisirxApplication.customerDAO.GetCustomerDetailsForID(DoctorID);
        doctorName = (TextView) findViewById(R.id.payment_docName);
        specialist = (TextView) findViewById(R.id.payment_specialist);
        visitMode = (TextView) findViewById(R.id.visit_mode2);
        Time = (TextView) findViewById(R.id.BookedTime);
        Date = (TextView) findViewById(R.id.BookedDate);
        ammount = (TextView) findViewById(R.id.Ammount1);
        paybtn = (Button) findViewById(R.id.payment_online);
        toolbar = (Toolbar) findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.Payment_pending);
        // Value Setting
//        doctorName.setText(findDoctorModel.getDoctorFirstName() + "" + findDoctorModel.getDoctorLastName());
//        specialist.setText(findDoctorModel.getDoctorSpecialization());
        visitMode.setText(appointmentPatientModel.getAppointmentType());
        Time.setText(appointmentPatientModel.getTime());
        Date.setText(appointmentPatientModel.getDate());
        ammount.setText("" + appointmentPatientModel.getDoctorfee());
// Summary Link
        Billsummary = (TextView) findViewById(R.id.payment_summarylist);
        Linkify.addLinks(Billsummary, Linkify.ALL);
        SpannableString contentPass = new SpannableString("View bill summary");
        contentPass.setSpan(new UnderlineSpan(), 0, contentPass.length(), 0);
        Billsummary.setText(contentPass);
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimplePopUp();
            }
        });
        Billsummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showSimplePopUp();
                Toast.makeText(getApplicationContext(), "in Progress", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSimplePopUp() {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("PAYMENT SUMMARY");
//        helpBuilder.setMessage("Appointment ID: 2525\n\n Dr.name: Jhon joe\n\n Appointment Date:1 8-2-16\n\n Payment due:500");
        helpBuilder.setMessage("Appointment ID: 055 \n\n  " +
                "Doctor Fee                   :Rs 500\n \n " +
                "Platfrom Fee                 :Rs 50 \n\n  " +
                "Total Amount               :Rs 550\n\n" +
                "(Inclusive of 14.5% Service Tax )");
        helpBuilder.setPositiveButton("Pay Now",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        payMent();
                    }
                });
        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    private String initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        return "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

    }

    void payMent() {
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();
        // these are mandatory parameters
        paramMap.put("ORDER_ID", initOrderId());
        paramMap.put("MID", "WorldP64425807474247");
        paramMap.put("CUST_ID", "CUST23657");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "worldpressplg");
        paramMap.put("TXN_AMOUNT", "1");
        paramMap.put("THEME", "merchant");
        paramMap.put("EMAIL", "abc@gmail.com");
        paramMap.put("MOBILE_NO", "123");
        PaytmOrder Order = new PaytmOrder(paramMap);
        PaytmMerchant Merchant = new PaytmMerchant(
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");
        Service.initialize(Order, Merchant, null);
        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     Bundle inResponse) {
                        // This method gets called if transaction failed. //
                        // Here in this case transaction is completed, but with
                        // a failure. // Error Message describes the reason for
                        // failure. // Response bundle contains the merchant
                        // response parameters.
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
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
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                });
    }
}
