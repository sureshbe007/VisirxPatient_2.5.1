package com.visirx.patient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.visirx.patient.AppointBaseAdaptor;
import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.LogoutProvider;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.VTConstants;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    AppointBaseAdaptor adapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    SharedPreferences sharedPreferences;
    int AppointmentNumber = -1;
    int aptHis = 1;
    int PagerPossion;
    String values = "AB";
    String values1 = "A";
    String userId = null;
    TextView UserName, UserID;
    CircleImageView circleImageView;
    int TabPosition, TabPosition3;
    Bundle extras1, extras2, extras;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        toolbar = (Toolbar) findViewById(R.id.Appointtoolbar);
        setSupportActionBar(toolbar);

        registerReceiver(receiverUpdate, new IntentFilter(VTConstants.NOTIFICATION_LOGIN_USER_PROFILE));

        navigationView = (NavigationView) findViewById(R.id.dash_navigation_view);
        UserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserName);
        UserID = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserdid);
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        UserName.setText(sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME, "null"));
        UserID.setText(sharedPreferences.getString(VTConstants.USER_ID, "null"));
        viewPager = (ViewPager) findViewById(R.id.Appintpagerpager);
        tabLayout = (TabLayout) findViewById(R.id.Appointtab_layout);


        //start:display profile image
        circleImageView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.patient_profile_image);
        userId = sharedPreferences.getString(VTConstants.USER_ID, null);
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

        Swipviewer();
        Navigation();

        // Tab Posstion Fixed Based on Selection


        extras2 = getIntent().getExtras();
        if (extras2 != null && extras2.getInt(VTConstants.POSITION) != -1) {

            Logger.w("DASHBOARD", "Doctor_Book_Appoint" + extras2.getInt(VTConstants.POSITION));

            int pos = extras2.getInt(VTConstants.POSITION);
            viewPager.setCurrentItem(pos);
        }


        if (getIntent() != null && getIntent().getIntExtra(VTConstants.TAB_POSITION, -1) != -1) {
            System.out.println("getIntent Default");

            TabPosition = getIntent().getIntExtra(VTConstants.TAB_POSITION, -1);
            viewPager.setCurrentItem(TabPosition);
        }


        extras1 = getIntent().getExtras();
        if (extras1 != null && extras1.getString("visirx") != null) {

            String message = extras1.getString("visirx");

            if (message.equalsIgnoreCase("AB")) {

                viewPager.setCurrentItem(1);
            } else {

                viewPager.setCurrentItem(0);
            }
        }
        // Doctor Complete the Appointment Notification
        extras = getIntent().getExtras();
        if (extras != null && extras.getInt(VTConstants.COMPLETED_POSITION, -1) != -1) {

            TabPosition3 = extras.getInt(VTConstants.COMPLETED_POSITION, -1);
            viewPager.setCurrentItem(TabPosition3);
        }

        if (getIntent() != null) {
            AppointmentNumber = getIntent().getIntExtra(VTConstants.APPTMODEL_KEY, -1);
            aptHis = getIntent().getIntExtra("FLAG_APT_HIS", 1);
        }
        if (getIntent() != null && getIntent().getIntExtra(VTConstants.POSITION, -1) != -1) {
            PagerPossion = getIntent().getIntExtra(VTConstants.POSITION, -1);
            viewPager.setCurrentItem(PagerPossion);
        }


    }

    // SwipeView for All Doctors and All appointment Tabtrin
    public void Swipviewer() {
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new AppointBaseAdaptor(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

//                tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#262d20"));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
//    Navigation bar for  DashBoard

    public void Navigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBar actionBar = getSupportActionBar();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(DashBoardActivity.this.getCurrentFocus().getWindowToken(), 0);

            }

            public void onDrawerOpened(View drawerView) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(DashBoardActivity.this.getCurrentFocus().getWindowToken(), 0);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        navigationView = (NavigationView) findViewById(R.id.dash_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        viewPager.setCurrentItem(0);
                        drawerLayout.closeDrawer(navigationView);
                        return true;
                    case R.id.MyProfile:
                        Intent MyProfile = new Intent(getApplicationContext(), MyProfileActivity.class);
                        startActivity(MyProfile);
                        return true;
                    case R.id.BookApt:

                        viewPager.setCurrentItem(1);
                        drawerLayout.closeDrawer(navigationView);
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
        try {
            ExitDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ExitDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashBoardActivity.this);
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage("Do you want to exit?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        System.exit(0);

                    }
                });
        // on pressing cancel button
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverUpdate);
    }

    BroadcastReceiver receiverUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                // Toast.makeText(getApplicationContext(),"BR",Toast.LENGTH_SHORT).show();
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


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    public void LogoutDialog() {
        LayoutInflater li = LayoutInflater.from(DashBoardActivity.this);
        View promptsView = li.inflate(R.layout.logout_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashBoardActivity.this);
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
        LogoutProvider logoutProvider = new LogoutProvider(DashBoardActivity.this);
        logoutProvider.SendLogoutReq(sharedPreferences.getString(VTConstants.GCM_ID, null));
//            android.os.Process.killProcess(android.os.Process.myPid());
//                                                        prePosition = -1;

    }

}