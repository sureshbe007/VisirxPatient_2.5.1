package com.visirx.patient.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.fragment.PatientProfileFragment;


public class Home extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    SharedPreferences sharedPreferences;
    TextView UserName, UserID;
    String navigation_name, navigation_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navmenu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setSupportActionBar(toolbar);
//        UserName=(TextView)findViewById(R.id.Navigation_username);
//        UserID=(TextView)findViewById(R.id.Navigation_email);
        UserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
        UserID = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
        //Initializing NavigationView
        UserName.setText("Suresh");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
//                    case R.id.dashboard:
////                        PatientProfileFragment patientProfileFragment = new PatientProfileFragment();
////                        android.support.v4.app.FragmentTransaction dashboardfragmentTransaction = getSupportFragmentManager().beginTransaction();
////                        dashboardfragmentTransaction.replace(R.id.frame, patientProfileFragment);
////                        dashboardfragmentTransaction.commit();
////                        getSupportActionBar().setTitle("Dashboard");
//                        return true;
//                    case R.id.BookApt:
////                        BookappointFragment bookappoint = new BookappointFragment();
////                        android.support.v4.app.FragmentTransaction bookaapoinmentTransation = getSupportFragmentManager().beginTransaction();
////                        bookaapoinmentTransation.replace(R.id.frame, bookappoint);
////                        bookaapoinmentTransation.commit();
////                        getSupportActionBar().setTitle("Book Appointment");
//                        return true;
//                    case R.id.addDocotor:
////                        AddDoctorFragment adddoctor = new AddDoctorFragment();
////                        android.support.v4.app.FragmentTransaction adddocotorfrgmenttransaction = getSupportFragmentManager().beginTransaction();
////                        adddocotorfrgmenttransaction.replace(R.id.frame, adddoctor);
////                        adddocotorfrgmenttransaction.commit();
////                        getSupportActionBar().setTitle("Add Doctor");
//                        return true;
//
//                    case R.id.MyProfile:
////                        PatientProfileFragment patientprofile = new PatientProfileFragment();
////                        android.support.v4.app.FragmentTransaction patientfragmentTransation = getSupportFragmentManager().beginTransaction();
////                        patientfragmentTransation.replace(R.id.frame, patientprofile);
////                        patientfragmentTransation.commit();
////                        getSupportActionBar().setTitle("My Profile");
////                        return true;
//                    case R.id.logout:
////                        try {
////                            sharedPreferences = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
////                            sharedPreferences.edit().putString("username", "")
////                                    .putString("password", "")
////                                    .putBoolean(VTConstants.LOGIN_STATUS, false).commit();
////
////                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                            startActivity(intent);
////                            finish();
////                            android.os.Process.killProcess(android.os.Process.myPid());
//////                                                        prePosition = -1;
////
////                        } catch (Exception e) {
////                            LogTrace.e("", e.getMessage());
////                            e.printStackTrace();
////                        }
////                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//
//                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
//                super.onDrawerClosed(drawerView);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
//
//                super.onDrawerOpened(drawerView);
//            }
//        };
        //Setting the actionbarToggle to drawer layout
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
//        actionBarDrawerToggle.syncState();
    }

}
