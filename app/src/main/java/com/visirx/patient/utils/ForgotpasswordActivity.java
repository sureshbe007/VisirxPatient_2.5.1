package com.visirx.patient.utils;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.com.visirx.patient.adapter.NewPasswordCallBack;
import com.visirx.patient.com.visirx.patient.adapter.NewUserIdCallBack;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.ForgotPasswordProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotpasswordActivity extends AppCompatActivity implements NewPasswordCallBack, NewUserIdCallBack {


    static final String TAG = ForgotpasswordActivity.class.getName();
    EditText userid, otpnumber, password1, password2;
    TextView ResendOtp;
    String USerID, OtpNumber, NewUserId, new_password, conformPassword;
    Button verfybtn, OTPCheck, ChangePAssword;
    SharedPreferences sharedPreferences;
    RelativeLayout OtpLayout;
    LinearLayout USerID_passlayout, NewpassParent;
    String Values;
    String ValuesUser;

    // OTP  Auto Read
    String phoneNumber, senderNum;
    String strMessage = "";
    SmsMessage currentMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(Forgot_OtpReceiver, filter);
        initvalues();
    }

    public void initvalues() {
        // Forgot Password
        ResendOtp = (TextView) findViewById(R.id.txtotpresend);
        userid = (EditText) findViewById(R.id.VerfyuserID);
        otpnumber = (EditText) findViewById(R.id.OtpEtxt);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        verfybtn = (Button) findViewById(R.id.btnVerify1);
        OTPCheck = (Button) findViewById(R.id.btnVeryCode);
        ChangePAssword = (Button) findViewById(R.id.Change);
        OtpLayout = (RelativeLayout) findViewById(R.id.codeveryfyLayout);
        USerID_passlayout = (LinearLayout) findViewById(R.id.USerID_passlayout);
        NewpassParent = (LinearLayout) findViewById(R.id.NewpassParent);
        verfybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VTConstants.checkAvailability(ForgotpasswordActivity.this)) {
                    SnackBarhide();
                    ServerValidation();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(verfybtn.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    SnackBarShow();

                }
            }
        });
        OTPCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VTConstants.checkAvailability(ForgotpasswordActivity.this)) {
                    SnackBarhide();
                    OTPServerValidation();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(OTPCheck.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    SnackBarShow();
                }
            }
        });
        ChangePAssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewPassword();
            }
        });


        userid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ServerValidation();
                }
                return false;
            }
        });

        otpnumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    OTPServerValidation();
                }
                return false;
            }
        });

        password2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    NewPassword();
                }
                return false;
            }
        });

        ResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (VTConstants.checkAvailability(ForgotpasswordActivity.this)) {
                    SnackBarhide();
                    ServerValidation();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ResendOtp.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    SnackBarShow();

                }
            }
        });
    }


    public void ServerValidation() {
        if (IsValidate()) {
            try {
                USerID = userid.getText().toString().trim();
                ForgotPasswordProvider forgotPasswordProvider = new ForgotPasswordProvider(ForgotpasswordActivity.this, this, this);
                forgotPasswordProvider.ForgotRequest(USerID);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }

    }


    private boolean IsValidate() {
        try {
            if (userid.getText().toString() == null || userid.getText().toString().length() <= 0) {
                DisplayToast("Enter the UserName or UserId ");
                return false;
            }

        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
        return true;
    }

    public void OTPServerValidation() {
        if (OTPIsValidate()) {
            try {
                OtpNumber = otpnumber.getText().toString().trim();
                ForgotPasswordProvider findDoctorProvider = new ForgotPasswordProvider(ForgotpasswordActivity.this, this, this);
                findDoctorProvider.ForgotRequestOtp(USerID, Integer.parseInt(OtpNumber));

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }

    }

    public void NewPassword() {
        if (NewsPasswordValidate()) {
            try {
                new_password = password1.getText().toString().trim();
                conformPassword = password2.getText().toString().trim();
                ForgotPasswordProvider findDoctorProvider = new ForgotPasswordProvider(ForgotpasswordActivity.this, this, this);
                findDoctorProvider.NewPasswordRequest(NewUserId, conformPassword);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }

    }

    private boolean OTPIsValidate() {
        try {
            if (otpnumber.getText().toString() == null || otpnumber.getText().toString().length() <= 0) {
                DisplayToast("Enter the OTP Number");
                return false;
            }


        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
        return true;
    }

    private boolean NewsPasswordValidate() {


        try {
            if (password1.getText().toString().isEmpty() || otpnumber.getText().toString().length() <= 0) {
                DisplayToast("Enter the Password");
                return false;
            }
            if (password2.getText().toString().isEmpty() || otpnumber.getText().toString().length() <= 0) {
                DisplayToast("Enter the ConfirmPassword");
                return false;
            }

            if (!password1.getText().toString().equals(password2.getText().toString())) {
                DisplayToast("password Mismatch");
                return false;
            }


        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    public void DisplayToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void SnackBarhide() {
        SnackbarManager.dismiss();
    }

    public void SnackBarShow() {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text("No InterNet Connection...") // text to display
                        .actionLabel("Close")
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)//
                        .color(Color.BLACK) // change the background color// action button label
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                Log.d(TAG, "Undoing something");

                            }
                        }) // action button's ActionClickListener
                , ForgotpasswordActivity.this);
    }


    private BroadcastReceiver Forgot_OtpReceiver = new BroadcastReceiver()

    {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.w("OTPRECEVIER  ", "BroadcastReceiver");
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {
                        Log.w("OTPRECEVIER  ", "FORLOOP");

                        currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        senderNum = phoneNumber;
                        strMessage = currentMessage.getDisplayMessageBody();

                        try {
                            if (senderNum.contains("VisiRx")) {

                                Log.w("OTPRECEVIER  ", "HP-VisiRx");
//                                Telephony.Sms.recivedSms(strMessage);
                                Pattern p = Pattern.compile("-?\\d+");
                                Matcher m = p.matcher(strMessage);
                                while (m.find()) {
                                    Log.w("OTPRECEVIER ", "OTPOnly" + m.group());
                                    otpnumber.setText(m.group());

                                }
                            }
                        } catch (Exception e) {

                        }

                    }
                }

            } catch (Exception e) {

            }
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        ForgotpasswordActivity.this.unregisterReceiver(Forgot_OtpReceiver);
    }

    @Override
    public void hideLayout(String str) {
        Values = str;

        if (Values == String.valueOf(0)) {
            USerID_passlayout.setVisibility(View.GONE);
            OtpLayout.setVisibility(View.VISIBLE);
        } else {
            USerID_passlayout.setVisibility(View.VISIBLE);
            OtpLayout.setVisibility(View.GONE);
        }
    }


    @Override

    // Back Pressed fixed
    public void onBackPressed() {

        if (USerID_passlayout.getVisibility() == View.VISIBLE) {
            NewpassParent.setVisibility(View.GONE);
            OtpLayout.setVisibility(View.GONE);
            USerID_passlayout.setVisibility(View.VISIBLE);
            Intent mainactive = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainactive);
            finish();
        } else {

            if (OtpLayout.getVisibility() == View.VISIBLE) {

                OtpLayout.setVisibility(View.VISIBLE);
                USerID_passlayout.setVisibility(View.GONE);
                NewpassParent.setVisibility(View.GONE);
            } else {
                OtpLayout.setVisibility(View.GONE);
                NewpassParent.setVisibility(View.VISIBLE);
                USerID_passlayout.setVisibility(View.GONE);
            }

        }

//        moveTaskToBack(true);
    }


    @Override
    public void NewUserId(String UserId, String responseCode) {
        ValuesUser = responseCode;
        NewUserId = UserId;
        if (ValuesUser == String.valueOf(0)) {

            NewpassParent.setVisibility(View.VISIBLE);
            OtpLayout.setVisibility(View.GONE);
        } else {

            NewpassParent.setVisibility(View.GONE);
            OtpLayout.setVisibility(View.VISIBLE);
        }
    }
}
