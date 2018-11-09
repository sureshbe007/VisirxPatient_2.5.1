package com.visirx.patient.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.com.visirx.patient.adapter.DoctorProfileCallBack;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.DoctorProfileProvider;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.model.PerformerWorkingTimeModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;


import java.util.List;

public class DoctorProfileActivity extends AppCompatActivity implements DoctorProfileCallBack {

    public DrawerLayout drawerLayout;
    //    public ListView drawerList;
    private NavigationView navigationView;
    Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    SharedPreferences sharedPreferences;
    TextView UserName, UserID;
    String values = "AB";
    String doctorFees;
    String DoctorID;
    Button myDoctor_bookappoint;
    String call_DoctorID, call_address, call_zipcode;
    List<PerformerWorkingTimeModel> performerWorkingTimeModelList;
    MediumFont DoctorName, VisiRxID;
    NormalFont Description, Specialist, Address, DoctorFees;
    List<FindDoctorModel> findDoctorModelList;
    LinearLayout workingLayout, WorkingDays, Workinghours;
    String DoctorCurrentFees;
//    String[] workingDay = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorprofile);
        toolbar = (Toolbar) findViewById(R.id.docprofile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        Navigation();
        if (getIntent() != null) {
            DoctorID = (getIntent().getStringExtra(VTConstants.DOCTOR_ID));
        }

        initvalues();
    }

    public void initvalues() {
        workingLayout = (LinearLayout) findViewById(R.id.hours_Parent2);
        workingLayout = (LinearLayout) findViewById(R.id.hours_Parent2);
        DoctorName = (MediumFont) findViewById(R.id.my_DoctorName);
        VisiRxID = (MediumFont) findViewById(R.id.DocProfile_VisiRxID);
        Description = (NormalFont) findViewById(R.id.my_Description);
        Specialist = (NormalFont) findViewById(R.id.my_Specialist);
        Address = (NormalFont) findViewById(R.id.clinic_Address);
        Workinghours = (LinearLayout) findViewById(R.id.Workinghours);
        WorkingDays = (LinearLayout) findViewById(R.id.WorkingDay);
        DoctorFees = (NormalFont) findViewById(R.id.my_DoctorFees);
        myDoctor_bookappoint = (Button) findViewById(R.id.Doctor_bookappoint);
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        if (VTConstants.checkAvailability(DoctorProfileActivity.this)) {
            DoctorProfileProvider findDoctorProvider = new DoctorProfileProvider(DoctorProfileActivity.this, this);
            findDoctorProvider.DoctorReq(DoctorID);
        }


        try {
            findDoctorModelList = VisirxApplication.customerDAO.Getcustomer(DoctorID);
            DoctorName.setText("Dr." + findDoctorModelList.get(0).getDoctorFirstName() + "" + findDoctorModelList.get(0).getDoctorLastName());
            VisiRxID.setText(findDoctorModelList.get(0).getDoctorId());
            Description.setText(findDoctorModelList.get(0).getDoctorDescription());
            Specialist.setText(findDoctorModelList.get(0).getDoctorSpecialization());
            DoctorFees.setText("  ₹ " + String.valueOf(findDoctorModelList.get(0).getDoctorfee()));
            DoctorCurrentFees = String.valueOf(findDoctorModelList.get(0).getDoctorfee());
//        Workinghours.setText(performerWorkingTimeModelList.get(0).getWorkingTime());
            VisiRxID.setText(findDoctorModelList.get(0).getDoctorId());
            navigationView = (NavigationView) findViewById(R.id.Doctor_navigation_view);
            UserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserName);
            UserID = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserdid);

            UserName.setText(sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME, "null"));
            UserID.setText(sharedPreferences.getString(VTConstants.USER_ID, "null"));
            myDoctor_bookappoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), BookAppointActivity.class);
                    intent.putExtra(VTConstants.DOCTOR_ID, DoctorID);
                    intent.putExtra(VTConstants.DOCTOR_FEES, DoctorCurrentFees);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Navigation Drawer

    public void Navigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View view) {
            }

            public void onDrawerOpened(View drawerView) {
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        navigationView = (NavigationView) findViewById(R.id.Doctor_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        Intent dash = new Intent(getBaseContext(), DashBoardActivity.class);
                        dash.putExtra("visirx", values);
                        startActivity(dash);
                        finish();
                        return true;
                    case R.id.MyProfile:
                        Intent MyProfile = new Intent(getBaseContext(), MyProfileActivity.class);
                        startActivity(MyProfile);
                        finish();
                        return true;
                    case R.id.BookApt:
                        Intent BookApt = new Intent(getBaseContext(), DashBoardActivity.class);
                        BookApt.putExtra("visirx", values);
                        startActivity(BookApt);
                        finish();
                        return true;
                    case R.id.addDocotor:
                        Intent addDocotor = new Intent(getApplicationContext(), AdddoctorActivity.class);
                        startActivity(addDocotor);
                        finish();
                        return true;
                    case R.id.Help:
                        Intent Help = new Intent(getApplicationContext(), HelpActivity.class);
                        startActivity(Help);
                        finish();
                        return true;
                    case R.id.logout:
                        try {
                            sharedPreferences = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
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
    public void DoctorProfile(String id, String address, String zipcode, List<PerformerWorkingTimeModel> performerWorkingTimeModels) {
        call_DoctorID = id;
        call_address = address;
        call_zipcode = zipcode;
        performerWorkingTimeModelList = performerWorkingTimeModels;
        Address.setText(call_address);
        TextView[] Date = new TextView[performerWorkingTimeModelList.size()];
        TextView[] Time = new TextView[performerWorkingTimeModelList.size()];

        for (int i = 0; i < performerWorkingTimeModelList.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            Date[i] = new TextView(DoctorProfileActivity.this);
            Time[i] = new TextView(DoctorProfileActivity.this);
            Date[i].setPadding(5, 25, 5, 5);
            Time[i].setPadding(15, 25, 5, 5);
            Date[i].setText(performerWorkingTimeModelList.get(i).getWeekDay());
            Time[i].setText(performerWorkingTimeModelList.get(i).getWorkingTime());
            params.setMargins(5, 15, 10, 5);
            Date[i].setLayoutParams(params);
            Time[i].setLayoutParams(params);
            WorkingDays.addView(Date[i]);
            Workinghours.addView(Time[i]);
        }

    }
}
