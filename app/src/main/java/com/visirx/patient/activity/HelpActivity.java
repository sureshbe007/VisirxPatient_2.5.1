package com.visirx.patient.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class HelpActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    String values = "AB";
    TextView UserName, UserID;
    WebView webView;
    CircleImageView circleImageView;
    NormalFont contact, policy, condtion, about_MuliText;
    boolean webflag = false;
    static final String TAG = MyProfileActivity.class.getName();
    private String about = "  VisiRx platform allows a doctor to attend to their patients@home. " +
            "VisiRx allows patients to remote consult with their family doctors through audio and video chats." +
            "VisiRx further allows a doctor to setup an eco-system of services to provide their patients at home the best health care they deserve," +
            " with paramedic services, home delivery of medicine and home lab testing.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initvalues();
        Navigation();
    }

    public void initvalues() {
        // Navigation FirstName and Id LastName
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        navigationView = (NavigationView) findViewById(R.id.help_navigation_view);
        UserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserName);
        UserID = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserdid);
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        UserName.setText(sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME, "null"));
        UserID.setText(sharedPreferences.getString(VTConstants.USER_ID, "null"));
        webView = (WebView) findViewById(R.id.Webview);
        webView.setWebViewClient(new MyBrowser());
        contact = (NormalFont) findViewById(R.id.contact2);
        policy = (NormalFont) findViewById(R.id.policy2);
        condtion = (NormalFont) findViewById(R.id.condition2);
        about_MuliText = (NormalFont) findViewById(R.id.abouUS);
        about_MuliText.setText(about);
        // Contact US
        Linkify.addLinks(contact, Linkify.ALL);
        SpannableString contentPass1 = new SpannableString("https://app.visirx.in/visirx/contactus.html");
        contentPass1.setSpan(new UnderlineSpan(), 0, contentPass1.length(), 0);
        contentPass1.setSpan(new ForegroundColorSpan(Color.parseColor("#369575")), 0, contentPass1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ;
        contact.setText(contentPass1);
        contact.setTag(contentPass1);
        contact.setOnClickListener(clickListener);
        Linkify.addLinks(policy, Linkify.ALL);
        SpannableString contentPass2 = new SpannableString("https://app.visirx.in/visirx/privacypolicy.html");
        contentPass2.setSpan(new UnderlineSpan(), 0, contentPass2.length(), 0);
        contentPass2.setSpan(new ForegroundColorSpan(Color.parseColor("#369575")), 0, contentPass2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        policy.setText(contentPass2);
        policy.setTag(contentPass2);
        policy.setOnClickListener(clickListener);
        Linkify.addLinks(condtion, Linkify.ALL);
        SpannableString contentPass = new SpannableString("https://app.visirx.in/visirx/termsnconditions.html");
        contentPass.setSpan(new UnderlineSpan(), 0, contentPass.length(), 0);
        contentPass.setSpan(new ForegroundColorSpan(Color.parseColor("#369575")), 0, contentPass.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        condtion.setText(contentPass);
        condtion.setTag(contentPass);
        condtion.setOnClickListener(clickListener);
        //start:display profile image
        circleImageView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.patient_profile_image);
        String userId = sharedPreferences.getString(VTConstants.USER_ID, null);
        String thumbnail_photo = VisirxApplication.userRegisterDAO.getUserThumnailpath(userId);
        if (thumbnail_photo != null) {
            //Load the file from sd card stored path - starts
            File imgFile = new File(thumbnail_photo);
            Uri profImgThumbUri = Uri.fromFile(imgFile);
            if (imgFile.exists()) {
                Bitmap myBitmap = null;
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), profImgThumbUri);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                circleImageView.setImageBitmap(myBitmap);
            } else {
                Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.default_image);
                circleImageView.setImageBitmap(defaultIcon);
            }
        } else {
            Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.default_image);
            circleImageView.setImageBitmap(defaultIcon);
        }
        //end:display profile image
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (VTConstants.checkAvailability(HelpActivity.this)) {
                SnackBarhide();
                webView.setVisibility(View.VISIBLE);
                webflag = true;
                String webUrl = ((View) v).getTag().toString();
                System.out.println("IMGURL" + webUrl);
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.loadUrl(webUrl);
            } else {
                SnackBarShow();

            }
        }
    };

    // Navigation in Help
    public void Navigation() {
        toolbar = (Toolbar) findViewById(R.id.Help_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBar actionBar = getSupportActionBar();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View view) {
            }

            public void onDrawerOpened(View drawerView) {
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        navigationView = (NavigationView) findViewById(R.id.help_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        Intent dash = new Intent(getApplicationContext(), DashBoardActivity.class);
                        startActivity(dash);
                        return true;
                    case R.id.BookApt:
                        Intent BookApt = new Intent(getBaseContext(), DashBoardActivity.class);
                        BookApt.putExtra("visirx", values);
                        startActivity(BookApt);
                        return true;
                    case R.id.addDocotor:
                        Intent addDocotor = new Intent(getApplicationContext(), AdddoctorActivity.class);
                        startActivity(addDocotor);
                        return true;
                    case R.id.MyProfile:
                        Intent MyProfile = new Intent(getApplicationContext(), MyProfileActivity.class);
                        startActivity(MyProfile);
                        return true;
                    case R.id.Help:
                        drawerLayout.closeDrawer(navigationView);
//                        finish();
                        return true;
                    case R.id.logout:
                        try {
                            sharedPreferences = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                            EventChecking.myLogout(sharedPreferences.getString(VTConstants.USER_ID, "null"),sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME, "null"),true);
                            sharedPreferences.edit().putString("username", "")
                                    .putString("password", "")
                                    .putBoolean(VTConstants.LOGIN_STATUS, false).commit();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
//                                                        prePosition = -1;
                        } catch (Exception e) {
                            LogTrace.e("", e.getMessage());
                            e.printStackTrace();
                        }
                        return true;
                    default:
//                        Toast.makeText(getApplicationContext(), "App Version: 1.3.6", Toast.LENGTH_SHORT).show();
                        return true;
                }

            }

        });
    }



    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (webflag) {
            webView.setVisibility(View.GONE);
            webView.clearView();
            webflag = false;
        } else {
            super.onBackPressed();
        }


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
                , HelpActivity.this);

    }

    public void SnackBarhide() {
        SnackbarManager.dismiss();
    }

}
