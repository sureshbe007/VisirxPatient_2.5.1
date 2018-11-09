package com.visirx.patient.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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

import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.com.visirx.patient.adapter.PatientBaseAdaptor;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.LogoutProvider;
import com.visirx.patient.fragment.EMRFragment;
import com.visirx.patient.fragment.NotesFragment;
import com.visirx.patient.fragment.PatientProfileFragment;
import com.visirx.patient.fragment.PrescriptionFragment;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientDetailsActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    //    public ListView drawerList;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    PatientBaseAdaptor adapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    String values = "AB";
    int reservationNumber;
    TextView UserName, UserID;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_details);
        // ViewPager  for Patient Profile
        toolbar = (Toolbar) findViewById(R.id.toolbar_myProfile);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.Details_navigation_view);
        UserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserName);
        UserID = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserdid);
//        ToolbarName=(TextView)findViewById(R.id.dash_patientName);
//        ToolbarId=(TextView)findViewById(R.id.dash_visirxId);
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        UserName.setText(sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME, "null"));
        UserID.setText(sharedPreferences.getString(VTConstants.USER_ID, "null"));
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        viewPager = (ViewPager) findViewById(R.id.pager__myProfile);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout__myProfile);
        viewpager();
        navigation();
        //rony 1.3.5 starts
        Log.d("SPIN", "Inside PatientDetailsActivity : onCreate()");
        if (getIntent() != null) {
            Log.d("SPIN", "Inside PatientDetailsActivity : onCreate() : intent not null 1");
            Log.d("SPIN", "Inside PatientDetailsActivity : onCreate() : intent not null 1 : " + getIntent().getStringExtra(VTConstants.APPOINTMENT_ID));
            reservationNumber = Integer.parseInt(getIntent().getStringExtra(VTConstants.APPOINTMENT_ID));

        }
        if (getIntent() != null && getIntent().getStringExtra(VTConstants.POSITION) != null) {
            Log.d("SPIN", "Inside PatientDetailsActivity : onCreate() : intent not null 2");
            Log.d("SPIN", "Inside PatientDetailsActivity : onCreate() :" + getIntent().getStringExtra(VTConstants.APPOINTMENT_ID));
            Log.d("SPIN", "Inside PatientDetailsActivity : onCreate() :" + getIntent().getStringExtra(VTConstants.POSITION));
            int pos = Integer.parseInt(getIntent().getStringExtra(VTConstants.POSITION));
            viewPager.setCurrentItem(pos);
        }
        Log.d("SPIN", "Inside PatientDetailsActivity : onCreate() : " + reservationNumber);

        //rony 1.3.5 ends


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

    public void viewpager() {
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 4"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new PatientBaseAdaptor(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                try {
                    viewPager.setCurrentItem(tab.getPosition());
                    InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }


//                    if (viewPager.getCurrentItem() == 0)
//                    {
//                        // Hide the keyboard.
//                        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
//                                .hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
//                    }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    // Navigation Drawer

    public void navigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View view) {
//                getSupportActionBar().setTitle(R.string.MyProfile);

                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(PatientDetailsActivity.this.getCurrentFocus().getWindowToken(), 0);
            }

            public void onDrawerOpened(View drawerView) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(PatientDetailsActivity.this.getCurrentFocus().getWindowToken(), 0);
//                getSupportActionBar().setTitle(R.string.MyProfile_action);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        navigationView = (NavigationView) findViewById(R.id.Details_navigation_view);
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


    public void LogoutDialog() {
        LayoutInflater li = LayoutInflater.from(PatientDetailsActivity.this);
        View promptsView = li.inflate(R.layout.logout_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PatientDetailsActivity.this);
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
        LogoutProvider logoutProvider = new LogoutProvider(PatientDetailsActivity.this);
        logoutProvider.SendLogoutReq(sharedPreferences.getString(VTConstants.GCM_ID, null));
//            android.os.Process.killProcess(android.os.Process.myPid());
//                                                        prePosition = -1;

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

    public class PatientBaseAdaptor extends FragmentStatePagerAdapter {

        int mNumOfTabs;
        CharSequence Titles[] = {"PATIENT", "EMR", "CHAT", "PRESCRIPTION"};
        //        int reservationNumber = -1;
        int aptHis = 1;

        public PatientBaseAdaptor(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
//                PatientProfileFragment tab1 = new PatientProfileFragment();
//                return tab1;
                    return PatientProfileFragment.newInstance(reservationNumber, aptHis);
                case 1:
//                EMRFragment tab2 = new EMRFragment();
                    return EMRFragment.newInstance(reservationNumber);
                case 2:
//                NotesFragment tab3 = new NotesFragment();
                    return NotesFragment.newInstance(reservationNumber);
                case 3:
//                PrescriptionFragment tab4 = new PrescriptionFragment();
                    return PrescriptionFragment.newInstance(reservationNumber);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "" + Titles[position];
        }
    }


}
