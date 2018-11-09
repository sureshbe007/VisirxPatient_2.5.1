package com.visirx.patient.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.OtpReq;
import com.visirx.patient.api.RegisterReq;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.com.visirx.patient.adapter.Callbackui;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.RegisterProvider;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.RegisterModel;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements Callbackui {
    // Main Activity.. Views for Forgot PasswordLink in Register
    LinearLayout main_forgotlayout;
    RelativeLayout main_Loginlayout;
    CheckBox termsbox;
    EditText UserName, Email, Phone, Pass, ConPass, EtextOTP;
    TextView txtOtResend;
    Button btnSignUp, btnOnePass, Backbutton;
    String username, email, phone, pass, conpass, userOtp, Gcmid;
    LinearLayout RegLayout, TermsLinerLayout, register_LinearLayout;
    RelativeLayout VeryfyLayout, Reg_Parent;
    private String reg;
    private GoogleCloudMessaging gcm;
    SharedPreferences sharedPreferences;
    String response;
    ScrollView parentScroll;
    boolean Checkboxstatus;
    boolean OtpFlag = true;
    WebView webView;
    static final String TAG = Register.class.getName();
    NormalFont TermsagreeText;
    // OTP Read
    String phoneNumber, senderNum;
    String strMessage = "";
    SmsMessage currentMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedPreferences = getSharedPreferences(
                VTConstants.GCM_FILENAME, 0);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(OtpReceiver, filter);
    }

    public void initvalues() {

        // Terms and Condtions Page
        //Scroll view
        parentScroll = (ScrollView) findViewById(R.id.scroll);
        TermsLinerLayout = (LinearLayout) findViewById(R.id.TermsLiner);
        Reg_Parent = (RelativeLayout) findViewById(R.id.Reg_Parent);

        //  Terms and Condtion Webview

        termsbox = (CheckBox) findViewById(R.id.termCondtion);
        webView = (WebView) findViewById(R.id.Webview);
        // EidtText..
        UserName = (EditText) findViewById(R.id.userName);
        Email = (EditText) findViewById(R.id.userEmail);
        Phone = (EditText) findViewById(R.id.userPhone);
        Pass = (EditText) findViewById(R.id.userPass);
        ConPass = (EditText) findViewById(R.id.userConPass);
        EtextOTP = (EditText) findViewById(R.id.OtpEtxt1);
        // Button...
        btnSignUp = (Button) findViewById(R.id.btnRegister_pat);
        btnOnePass = (Button) findViewById(R.id.btnVeryCode);
        Backbutton = (Button) findViewById(R.id.Backbtn);
        //Textview...
        txtOtResend = (TextView) findViewById(R.id.txtotpresend);
        // layout...
        RegLayout = (LinearLayout) findViewById(R.id.register);
        VeryfyLayout = (RelativeLayout) findViewById(R.id.codeveryfy);
        main_forgotlayout = (LinearLayout) findViewById(R.id.USerID_passlayout);

        main_Loginlayout = (RelativeLayout) findViewById(R.id.Loginlayout);
        final SharedPreferences sharedPreferences = getSharedPreferences(
                VTConstants.GCM_FILENAME, 0);
        TermsagreeText = (NormalFont) findViewById(R.id.TermsagreeText);
        Linkify.addLinks(TermsagreeText, Linkify.ALL);
        SpannableString contentPass = new SpannableString("I accept terms and conditions");
        contentPass.setSpan(new UnderlineSpan(), 0, contentPass.length(), 0);
        TermsagreeText.setText(contentPass);

        TermsagreeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (VTConstants.checkAvailability(Register.this)) {

                    SnackBarhide();
                    Reg_Parent.setVisibility(View.GONE);
                    TermsLinerLayout.setVisibility(View.VISIBLE);
                    webView.setWebViewClient(new MyBrowser());
                    webView.getSettings().setLoadsImagesAutomatically(true);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    webView.loadUrl("https://app.visirx.in/visirx/termsnconditions.html");
                } else {
                    SnackBar();
                }
            }
        });

        Backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TermsLinerLayout.setVisibility(View.GONE);
                Reg_Parent.setVisibility(View.VISIBLE);

            }
        });

        // txt....OTP Resend
        Linkify.addLinks(txtOtResend, Linkify.ALL);
        SpannableString otpresend = new SpannableString("Resend OTP?");
        otpresend.setSpan(new UnderlineSpan(), 0, otpresend.length(), 0);
        txtOtResend.setText(otpresend);
        txtOtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VTConstants.checkAvailability(Register.this)) {
                    SnackBarhide();
                    OtpResendServer();


                } else {
                    SnackBar();
                }
            }
        });
        // After conform SighUp by SoftkeyPad
        ConPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (VTConstants.checkAvailability(Register.this)) {
                        SnackBarhide();
                        ValidateServer();
                    } else {
                        SnackBar();
                    }

                }
                return false;
            }
        });
        EtextOTP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (VTConstants.checkAvailability(Register.this)) {
                        SnackBarhide();
                        otpServer();

                    } else {
                        SnackBar();
                    }


                }
                return false;
            }
        });
        // Btn.... Registration Process btn
        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!VTConstants.checkAvailability(Register.this)) {
                    SnackBar();
                } else {
                    SnackBarhide();
//                    if ((reg == null || reg.isEmpty())
//                            && !sharedPreferences.getBoolean(
//                            VTConstants.GCM_STATUS, false)
//                            && VTConstants.checkAvailability(Register.this))
//                    {
//                        Popup.ShowErrorMessage(Register.this,
//                                R.string.wait_gcmProcess, Toast.LENGTH_SHORT);
//                    } else {
                    ValidateServer();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ConPass.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

//                    }
                }
            }
        });
        // btn.....One Time Password
        btnOnePass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (VTConstants.checkAvailability(Register.this)) {
                    SnackBarhide();
                    otpServer();

                } else {
                    SnackBar();
                }
            }
        });
    }
    // Register GCM

//    private String registerGcm() {
//        if (checkPlayServices()) {
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... params) {
//                    String msg = "";
//                    String regid = "";
//                    try {
//                        if (gcm == null) {
//                            gcm = GoogleCloudMessaging
//                                    .getInstance(getApplicationContext());
//                        }
//                        regid = gcm.register(VTConstants.SENDER_ID);
//                        Log.d("GCM BG", msg);
//                    } catch (IOException ex) {
//                        msg = "Error :" + ex.getMessage();
//                        Log.e("GCM BG", msg);
//                    }
//
//                    reg = regid;
//                    sharedPreferences = getSharedPreferences(
//                            VTConstants.GCM_FILENAME, 0);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString(VTConstants.GCM_ID, reg);
//                    editor.putBoolean(VTConstants.GCM_STATUS, true);
//                    editor.commit();
//                    return null;
//                }
//            }.execute(null, null, null);
//
//        }
//        return reg;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        initvalues();
//        parentScroll.setEnabled(false);
//        registerGcm();
    }

    // Register to Server
    private void ValidateServer() {
        if (IsValidate()) {
            RegisterReq regReq = null;
            try {
                username = UserName.getText().toString().trim();
                email = Email.getText().toString().trim();
                phone = Phone.getText().toString().trim();
                pass = Pass.getText().toString().trim();
                conpass = ConPass.getText().toString().trim();
//                Checkboxstatus = termsbox.isChecked();

                if (VTConstants.checkAvailability(this)) {
                    RegisterProvider alldoctorsListProvider = new RegisterProvider(Register.this, this);
                    alldoctorsListProvider.RegisterReq(username, email, phone, pass, conpass);
                } else {
                    StartAppOffline();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }

        }
    }

    private boolean IsValidate() {
        try {
            if (UserName.getText().toString() == null || UserName.getText().toString().length() <= 0) {
                DispalyToast("Enter the username");
                return false;
            }
            if (Email.getText().toString() == null || Email.getText().toString().length() <= 0) {
                DispalyToast("Enter the Email ID");
                return false;
            }
            if (Email.getText().toString() != null && Email.getText().toString().length() > 0 && !VTConstants.isValidMail(Email.getText().toString())) {
                DispalyToast("Enter the  Valid Email ID");
                return false;
            }
            if (Phone.getText().toString() == null || Phone.getText().toString().length() <= 0) {
                DispalyToast("Enter  Mobile Number");
                return false;
            }
            if (Phone.getText().toString().length() < 10) {
                DispalyToast("Enter 10 Digit Valid Mobile Number");
                return false;
            }
            if (Pass.getText().toString() == null || Pass.getText().toString().length() <= 0) {
                DispalyToast("Enter the password");
                return false;
            }
            if (ConPass.getText().toString() == null || ConPass.getText().toString().length() <= 0) {
                DispalyToast("Enter the Conform Paasword");
                return false;
            }
            if (!Pass.getText().toString().equals(ConPass.getText().toString())) {
                DispalyToast("password Mismatch");
                return false;
            }
            if (!termsbox.isChecked()) {
                DispalyToast("Accept the Terms and Conditions");
                return false;
            }

        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
        return true;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void StartHomePage(RegisterModel registerModel) {
        VTConstants.PROGRESSSTATUS_DASHBOARD = 1;
        AppContext appcontext = new AppContext();
        appcontext.setRegisterUser(registerModel);
        VisirxApplication.InitialiseApplication(appcontext,
                getApplicationContext());
        /* store user information into local database */
        // rony - login GC  - Starts
    }

    private void StartAppOffline() {
        try {
            if (IsValidate()) {
                RegisterModel model = VisirxApplication.userRegisterDAO.GetUsers();
                if (model != null) {
                    AppContext appcontext = new AppContext();
                    appcontext.setRegisterUser(model);
                    VisirxApplication.InitialiseApplication(appcontext,
                            getApplicationContext());
                    StartHomePage(model);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }
    //  Otp to Server

    private void otpServer() {
        if (isOtpVaild()) {
            OtpReq otpreq = null;
            try {
                userOtp = EtextOTP.getText().toString().trim();
                RegisterProvider alldoctorsListProvider = new RegisterProvider(Register.this, this);
                alldoctorsListProvider.OtpReq(username, userOtp);

            } catch (Exception e) {
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }

        }
    }
    // Otp Validdation

    boolean isOtpVaild() {
        String otpcheck = EtextOTP.getText().toString();
        try {
            if (otpcheck == null || otpcheck.length() <= 0) {
                DispalyToast("Enter the OTP");
                return false;
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
        return true;
    }

    // Hide the Layout Using Callback
    @Override
    public void hide(String hidevalue) {
        response = hidevalue;
        if (response == String.valueOf(0)) {
            RegLayout.setVisibility(View.GONE);
            VeryfyLayout.setVisibility(View.VISIBLE);
        } else {
            RegLayout.setVisibility(View.VISIBLE);
            VeryfyLayout.setVisibility(View.GONE);
        }
    }
    // Otp Resend Server

    private void OtpResendServer() {
        if (IsValidate()) {
            RegisterReq regReq = null;
            try {

                username = UserName.getText().toString().trim();
                email = Email.getText().toString().trim();
                phone = Phone.getText().toString().trim();
                pass = Pass.getText().toString().trim();
                conpass = ConPass.getText().toString().trim();
                RequestHeader requestMessageHeader = new RequestHeader();
                RegisterProvider alldoctorsListProvider = new RegisterProvider(Register.this, this);
                alldoctorsListProvider.otpResend(username, email, phone, pass, conpass);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }

        }
    }

    private BroadcastReceiver OtpReceiver = new BroadcastReceiver()

    {
        @Override
        public void onReceive(Context context, Intent intent) {


            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {


                        currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        senderNum = phoneNumber;
                        strMessage = currentMessage.getDisplayMessageBody();

                        try {
                            if (senderNum.contains("VisiRx")) {

                                Telephony.Sms.recivedSms(strMessage);
                                Pattern p = Pattern.compile("-?\\d+");
                                Matcher m = p.matcher(strMessage);
                                while (m.find()) {
                                    Log.w("OTPRECEVIER ", "OTPOnly" + m.group());
                                Sms.recivedSms(message );
                                    EtextOTP.setText(m.group());

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
    public void onBackPressed() {
        if (TermsLinerLayout.getVisibility() == View.VISIBLE)

        {
            TermsLinerLayout.setVisibility(View.GONE);
            Reg_Parent.setVisibility(View.VISIBLE);
        } else {
            if (response == String.valueOf(0)) {

                RegLayout.setVisibility(View.GONE);
                VeryfyLayout.setVisibility(View.VISIBLE);
            } else {

                RegLayout.setVisibility(View.VISIBLE);
                VeryfyLayout.setVisibility(View.GONE);
            }
        }
//        moveTaskToBack(true);
        if (Reg_Parent.getVisibility() == View.VISIBLE)

        {
            Intent login = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(login);
            finish();
        }


    }
    // Google Account Check

    @Override
    public void onDestroy() {
        super.onDestroy();
        Register.this.unregisterReceiver(OtpReceiver);
    }

//    private boolean deviceHasGoogleAccount() {
//        AccountManager accMan = AccountManager.get(this);
//        Account[] accArray = accMan.getAccountsByType("com.google");
//        return accArray.length >= 1 ? true : false;
//    }
    // Single Toast for all place

    public void DispalyToast(String Str) {
        Toast.makeText(getApplicationContext(), "" + Str, Toast.LENGTH_SHORT).show();
    }
    // playService Check

//    private boolean checkPlayServices() {
//        if (!deviceHasGoogleAccount()) {
////            DispalyToast("Add Google Account to your device ");
//
//        }
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        VTConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Log.i("GCM", "This device is not supported.");
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }

    public void SnackBar() {
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
                , Register.this);
    }

    public void SnackBarhide() {
        SnackbarManager.dismiss();
    }

}
