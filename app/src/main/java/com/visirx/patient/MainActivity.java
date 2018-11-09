package com.visirx.patient;


import android.*;
import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.activity.DashBoardActivity;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.LoginProvider;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.CustomerProfileModel;
import com.visirx.patient.model.LoginModel;
import com.visirx.patient.utils.ForgotpasswordActivity;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.Register;
import com.visirx.patient.utils.VTConstants;
import com.visirx.patient.visirxav.activities.CallActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText txtuserName = null;
    EditText txtpassword = null;
    TextView txtSignUp = null;
    TextView txtforgotPass = null;
    Button btnSignIn, btnVerify;
    static int prePosition = -1;
    String userName = null;
    String password = null;
    LinearLayout Forgot_Pass_Layout;
    RelativeLayout loginParent, Main_ParentLogin;
    static final String TAG = MainActivity.class.getName();
    SharedPreferences sharedPreferences, sharedPreferences1;
    private GoogleApiClient client;
    private List<CustomerProfileModel> ProfileStatusList;
    CheckBox termsbox;
    private String reg;
    private GoogleCloudMessaging gcm;
    //    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    // Forgot Password
    EditText userid = null;
    EditText emailid = null;
    EditText mobilenumber = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_main);
        Log.d("SPIN", "Inside onCreate onCreate : ");
        sharedPreferences = MainActivity.this.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        sharedPreferences1 = MainActivity.this.getSharedPreferences(VTConstants.GCM_FILENAME, 0);
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        loginParent = (RelativeLayout) findViewById(R.id.Loginlayout);
        Main_ParentLogin = (RelativeLayout) findViewById(R.id.Main_Parent);

        if (!sharedPreferences1.getBoolean(VTConstants.NEW_GCM_STATUS, false)) {

            Log.d("MainActivity ", "NEW_GCM_STATUS :  ");
            registerGcm();
        }

//        Intent intent = new Intent(getApplicationContext(), CallActivity.class);
//        startActivity(intent);

    }

    public void initvalues() {
        sharedPreferences = MainActivity.this.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        if (getIntent() != null) {
            prePosition = getIntent().getIntExtra("RESET_POSITION", 0);
        }

        ProfileStatusList = new ArrayList<CustomerProfileModel>();
        sharedPreferences = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        if (sharedPreferences.getBoolean(VTConstants.LOGIN_STATUS, false)) {

            Intent dash = new Intent(this, DashBoardActivity.class);
            startActivity(dash);
            creatVisirxDirectories();
        }
        termsbox = (CheckBox) findViewById(R.id.termCondtion);
        txtuserName = (EditText) findViewById(R.id.etxtuser);
        btnSignIn = (Button) findViewById(R.id.btnLogin_pat);
        txtpassword = (EditText) findViewById(R.id.etxtpass);
//        Forgot_Pass_Layout = (LinearLayout) findViewById(R.id.Forgot_passlayout);

//        btnVerify = (Button) findViewById(R.id.btnVerify);
//      SignUp txt Link
        txtSignUp = (TextView) findViewById(R.id.signin_pat_pat);
        Linkify.addLinks(txtSignUp, Linkify.ALL);
        SpannableString content = new SpannableString("Sign Up");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtSignUp.setText(content);
        // Password  txt Link
        txtforgotPass = (TextView) findViewById(R.id.forgetpassword_pat);
        Linkify.addLinks(txtforgotPass, Linkify.ALL);
        SpannableString contentPass = new SpannableString("Forgot Password?");
        contentPass.setSpan(new UnderlineSpan(), 0, contentPass.length(), 0);
        txtforgotPass.setText(contentPass);


        Log.e("Tag", "Token1");
        if (VisirxApplication.userDAO != null) {
            System.out.println("IIII");
            Log.e("Tag", "Token2");
        }
        Log.e("Tag", "Token3");
        // txt.... Link  For New user to SignUp
        txtforgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(getApplicationContext(), ForgotpasswordActivity.class);
                startActivity(reg);
                finish();

            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(getApplicationContext(), Register.class);
                startActivity(reg);
                finish();

            }
        });
        // txt...Link for ForgotProcess
        txtforgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reg = new Intent(getApplicationContext(), ForgotpasswordActivity.class);
                startActivity(reg);
                finish();
            }
        });
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        String userId = sharedPreferences.getString(VTConstants.USER_ID, null);
        CustomerProfileModel model = VisirxApplication.userRegisterDAO.getUserDetails(userId);
        if (model != null) {

            SharedPreferences settings = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            String name = settings.getString("username", "");
            String pwd = settings.getString("password", "");
            if (name != null && !name.equals("")) {
                txtuserName.setText(name);
                if ((pwd != null && !pwd.equals(""))) {
                    txtpassword.setText(pwd);
                    StartAppOffline();
                } else {
                    txtpassword.requestFocus();
                }
            }
        }

        txtpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (VTConstants.checkAvailability(MainActivity.this)) {

                        SnackBarhide();
                        ValidateServer();
                    } else {

                        SnackBar();
                    }
                }
                return false;
            }
        });
        Typeface font_style = Typeface.createFromAsset(getAssets(), "Nunito-Regular.ttf");
        //  SignIn Button
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VTConstants.checkAvailability(MainActivity.this)) {
                    SnackBarhide();
                    if ((reg == null || reg.isEmpty()) && !sharedPreferences1.getBoolean(VTConstants.NEW_GCM_STATUS, false)) {

                        registerGcm();
                        Popup.ShowErrorMessage(MainActivity.this, R.string.error_gcm, Toast.LENGTH_LONG);
                    } else {

                        ValidateServer();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(btnSignIn.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                } else {
//                    ValidateServer();
                    SnackBar();
                }
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        final View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
            }
        };
    }


    private void ValidateServer() {
        if (IsValidate()) {
            String GCMID = null;
            try {
                sharedPreferences = getSharedPreferences(
                        VTConstants.GCM_FILENAME, 0);
                userName = txtuserName.getText().toString().trim();
                password = txtpassword.getText().toString().trim();
                GCMID = sharedPreferences.getString(VTConstants.GCM_ID, null);
                LoginProvider findDoctorProvider = new LoginProvider(MainActivity.this);
                findDoctorProvider.LoginReqest(userName, password, GCMID);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }
    }

    private void StartAppOffline() {
        try {

            if (IsValidate()) {
                LoginModel model = VisirxApplication.userDAO.GetUsers();
                // for testing
                //model.setTrackerEnabled(0);
                //model.setHosEnabled(0);
                System.out.println("OOFF 3" + model);
                if (model != null && validateLogin(model)) {

                    AppContext appcontext = new AppContext();
                    appcontext.setLoggedUser(model);
                    VisirxApplication.InitialiseApplication(appcontext, getApplicationContext());
                    StartHomePage(model);
                    RememberPasswordForTesting();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private boolean validateLogin(LoginModel model) {

        userName = txtuserName.getText().toString().trim();
        password = txtpassword.getText().toString().trim();
        if (!userName.equalsIgnoreCase(model.getUserName()) &&
                !password.equalsIgnoreCase(model.getPassword())) {
            Popup.ShowErrorMessageString(this, "Invalid Username/Password", Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private void RememberPasswordForTesting() {
        SharedPreferences settings = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        settings.edit().putString("username", userName)
                .putString("password", password)
                .putBoolean(VTConstants.LOG_OUT, false).commit();
    }

    private void StartHomePage(LoginModel loginModel) {

        AppContext appcontext = new AppContext();
        appcontext.setLoggedUser(loginModel);
        VisirxApplication.InitialiseApplication(appcontext, getApplicationContext());
        /*store user information into local database */
        boolean isUpdated = VisirxApplication.userDAO.insertLogin(loginModel);
    }

    private boolean IsValidate() {
        try {
            if (txtuserName.getText().toString() == null || txtuserName.getText().toString().length() <= 0) {
                DisplayToast("Enter the username");
                return false;
            }
            if (txtpassword.getText().toString() == null || txtpassword.getText().toString().length() <= 0) {
                DisplayToast("Enter the password");
                return false;
            }
            if (txtpassword.getText().toString() == null || txtpassword.getText().toString().length() <= 0) {
                DisplayToast("Enter the password");
                return false;
            }


        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
        return true;
    }


    @Override
    protected void onResume() {

        initvalues();


        // SD card Permission
        try {

            checkAndRequestPermissions();

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private boolean checkAndRequestPermissions() {
        int permission_SdCard = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_SMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS);
        int permission_CAMERA = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int permission_RECORDAUDIO = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);
        int permission_AUDIO_MODIFY = ContextCompat.checkSelfPermission(this, android.Manifest.permission.MEDIA_CONTENT_CONTROL);

        // List  to add Permissions
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permission_SdCard != PackageManager.PERMISSION_GRANTED) {

            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permission_SMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }

        if (permission_CAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (permission_RECORDAUDIO != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }

//        if (permission_AUDIO_MODIFY != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.MEDIA_CONTENT_CONTROL);
//        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    // SD card Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
//                    Popup.ShowErrorMessageString(this, "Storage permissions granted.", Toast.LENGTH_LONG);
                } else
                {
//                    Popup.ShowSuccessMessage(this, R.string.PERMISSION_DENIED, Toast.LENGTH_LONG);
                }
            }

        }

        Log.d("SPIN", "Inside onRequestPermissionsResult completed : ");

    }

    public void DisplayToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void creatVisirxDirectories() {
        //create all root directories
        VTConstants.createDirectoryPrescription();
        VTConstants.createDirectoryProfileImage();
        VTConstants.createDirectoryProfileImageThumnail();
        VTConstants.createDirectoryPrescriptionThumbnail();
        // rony - login GC  - Ends
        VTConstants.createDirectoryEMRImages();
        VTConstants.createDirectoryEMRImageThumbnail();
        VTConstants.createDirectoryEMRAuscultation();
    }


    // Register GCM

    private String registerGcm() {
        if (checkPlayServices()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    String msg = "";
                    String regid = "";
                    try {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging
                                    .getInstance(getApplicationContext());
                        }
                        regid = gcm.register(VTConstants.SENDER_ID);
                        Log.d("GCM BG", msg);
                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();
                        Log.e("GCM BG", msg);
                    }

                    reg = regid;
                    if (reg != null) {
                        sharedPreferences = getSharedPreferences(
                                VTConstants.GCM_FILENAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(VTConstants.GCM_ID, reg);
                        editor.putBoolean(VTConstants.NEW_GCM_STATUS, true);
                        editor.commit();

                    }

                    return null;
                }
            }.execute(null, null, null);

        }
        return reg;
    }

    private boolean checkPlayServices() {
        if (!deviceHasGoogleAccount()) {
//            DispalyToast("Add Google Account to your device ");

        }
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        VTConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("GCM", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private boolean deviceHasGoogleAccount() {
        AccountManager accMan = AccountManager.get(this);
        Account[] accArray = accMan.getAccountsByType("com.google");
        return accArray.length >= 1 ? true : false;
    }

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
                , MainActivity.this);
    }

    public void SnackBarhide() {
        SnackbarManager.dismiss();
    }


    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.visirx.patient/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.visirx.patient/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }
}
