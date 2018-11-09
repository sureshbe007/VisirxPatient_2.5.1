
package com.visirx.patient.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.com.visirx.patient.adapter.FindDoctorCallback;
import com.visirx.patient.com.visirx.patient.adapter.RecyclerAdapter_AddDoctors;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.FindDoctorProvider;
import com.visirx.patient.common.provider.LogoutProvider;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdddoctorActivity extends AppCompatActivity implements FindDoctorCallback {

    private List<FindDoctorModel> findDoctorList;
    RecyclerAdapter_AddDoctors recyclerAdapter_addDoctors;
    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    String values = "AB";
    RecyclerView recyclerView_adddoctor;
    Button adddoctor;
    //    SearchView search_dcotor;
    EditText search_dcotor;
    TextView serchbtn, doctorFind, UserName, UserID;
    String doctorName;
    ProgressBar progressBar;
    List<FindDoctorModel> AlldoctorList;
    int ResponseValues;
    CircleImageView circleImageView;
    static final String TAG = FindDoctorProvider.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddoctor);
        adddoctor = (Button) findViewById(R.id.addbutton);
        initvalues();
        Navigation();
//        showCard();
    }

    public void initvalues() {
        // Navigation FirstName and Id LastName
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        navigationView = (NavigationView) findViewById(R.id.add_navigation_view);
        UserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserName);
        UserID = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserdid);
        UserName.setText(sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME, "null"));
        UserID.setText(sharedPreferences.getString(VTConstants.USER_ID, "null"));
        recyclerView_adddoctor = (RecyclerView) findViewById(R.id.add_recycler_view);
        registerReceiver(receiverUpdate, new IntentFilter(VTConstants.FIND_DOCTOR_BRODCAST));
        serchbtn = (TextView) findViewById(R.id.ser);
        doctorFind = (TextView) findViewById(R.id.no_doctor);
        search_dcotor = (EditText) findViewById(R.id.searchView1);
        progressBar = (ProgressBar) findViewById(R.id.adddcotor_process);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#369575"), android.graphics.PorterDuff.Mode.SRC_ATOP);
        serchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (search_dcotor.getText().toString().trim() != null && !search_dcotor.getText().toString().trim().isEmpty()) {
                    doctorName = search_dcotor.getText().toString().trim();
                    // Fabric Search Events

                    sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                    if (VTConstants.checkAvailability(AdddoctorActivity.this)) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(AdddoctorActivity.this.getCurrentFocus().getWindowToken(), 0);
                        SnackBarhide();
                        findDoctor();

                    } else {
                        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(AdddoctorActivity.this.getCurrentFocus().getWindowToken(), 0);
                        SnackBar();
                    }

                } else {
                    Toast.makeText(AdddoctorActivity.this, " Enter Doctor ID / Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search_dcotor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (search_dcotor.getText().toString().trim() != null && !search_dcotor.getText().toString().trim().isEmpty()) {
                        doctorName = search_dcotor.getText().toString().trim();
                        // Fabric Event Searchung
                        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
//                        EventChecking.TracKSearchStatus(""+"User ID:  "+sharedPreferences.getString(VTConstants.USER_ID, null) + "      Search Event:     " + doctorName);
//                        EventChecking.TracKSearchStatus(doctorName);
                        if (VTConstants.checkAvailability(AdddoctorActivity.this)) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(AdddoctorActivity.this.getCurrentFocus().getWindowToken(), 0);
                            SnackBarhide();
                            findDoctor();
                        } else {
                            InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(AdddoctorActivity.this.getCurrentFocus().getWindowToken(), 0);
                            SnackBar();
                        }

                    } else {
                        Toast.makeText(AdddoctorActivity.this, " Enter Doctor ID / Name", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

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


    public void findDoctor() {
        try {
            System.out.println("FindDocotor"+doctorName);
            Log.d("ADD","findDoctor() :  "+doctorName);
            FindDoctorProvider findDoctorProvider = new FindDoctorProvider(AdddoctorActivity.this, this);
            findDoctorProvider.FindDoctorReq(doctorName);
            InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(AdddoctorActivity.this.getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("DOC 300::" + e.getMessage());
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    // Navigation Drawer
    public void Navigation() {
        toolbar = (Toolbar) findViewById(R.id.addDocotor_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBar actionBar = getSupportActionBar();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(AdddoctorActivity.this.getCurrentFocus().getWindowToken(), 0);
            }

            public void onDrawerOpened(View drawerView) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(AdddoctorActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        navigationView = (NavigationView) findViewById(R.id.add_navigation_view);
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
                        drawerLayout.closeDrawer(navigationView);
                        return true;
                    case R.id.MyProfile:
                        Intent MyProfile = new Intent(getApplicationContext(), MyProfileActivity.class);
                        startActivity(MyProfile);
                        return true;
                    case R.id.Help:
                        Intent Help = new Intent(getApplicationContext(), HelpActivity.class);
                        startActivity(Help);
                        return true;
                    case R.id.logout:
                        LogoutDialog();
                        return true;
                    default:
//                        Toast.makeText(getApplicationContext(), "App Version: 1.3.6", Toast.LENGTH_SHORT).show();
                        return true;
                }

            }

        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverUpdate);
    }

    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                Toast.makeText(getApplicationContext(), "receiverUpdate Recivr", Toast.LENGTH_LONG);
                recyclerAdapter_addDoctors.notifyDataSetChanged();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }
    };

    public void ToastDisplay(String str) {
        Toast.makeText(AdddoctorActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(getApplicationContext(),
                DashBoardActivity.class);
        in.putExtra("msg", "NA");
        startActivity(in);
        finish();
        super.onBackPressed();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
//        inflater.inflate(R.menu.emr_menu, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }

    public void SnackBar() {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext())
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
                , AdddoctorActivity.this);
    }

    public void SnackBarhide() {
        SnackbarManager.dismiss();
    }

    @Override
    public void doctorlist(List<FindDoctorModel> findDoctorModels, int responseCode) {

        AlldoctorList = findDoctorModels;
        ResponseValues = responseCode;
        if (ResponseValues == 0) {

            recyclerView_adddoctor.setVisibility(View.VISIBLE);
            recyclerAdapter_addDoctors = new RecyclerAdapter_AddDoctors(AdddoctorActivity.this, AlldoctorList);
            recyclerView_adddoctor.setAdapter(recyclerAdapter_addDoctors);
            recyclerView_adddoctor.setHasFixedSize(true);
            recyclerView_adddoctor.setLayoutManager(new LinearLayoutManager(AdddoctorActivity.this));
            recyclerAdapter_addDoctors = new RecyclerAdapter_AddDoctors(getApplicationContext(), AlldoctorList);
            recyclerAdapter_addDoctors.notifyDataSetChanged();
        } else {

            recyclerView_adddoctor.setVisibility(View.GONE);
            recyclerAdapter_addDoctors.notifyDataSetChanged();

        }
    }


    public void LogoutDialog() {
        LayoutInflater li = LayoutInflater.from(AdddoctorActivity.this);
        View promptsView = li.inflate(R.layout.logout_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdddoctorActivity.this);
        Button negativeButton = (Button) promptsView.findViewById(R.id.logout_no);
        Button positiveButton = (Button) promptsView.findViewById(R.id.logout_yes);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.cancel();
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutClick();
                alertDialog.cancel();

            }
        });
    }

    public void logoutClick() {

        sharedPreferences = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.GCM_FILENAME, 0);
        EventChecking.myLogout(sharedPreferences.getString(VTConstants.USER_ID, "null"), sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME, "null"), true);
        LogoutProvider logoutProvider = new LogoutProvider(AdddoctorActivity.this);
        logoutProvider.SendLogoutReq(sharedPreferences.getString(VTConstants.GCM_ID, null));
//            android.os.Process.killProcess(android.os.Process.myPid());
//                                                        prePosition = -1;

    }
}
