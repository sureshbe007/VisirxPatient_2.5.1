
package com.visirx.patient.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.com.visirx.patient.adapter.Timeslot_GridviewAdapter;
import com.visirx.patient.com.visirx.patient.adapter.callBack;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.AlldoctorsListProvider;
import com.visirx.patient.common.provider.LogoutProvider;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.AvailableTimeslotsModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookAppointActivity extends AppCompatActivity implements callBack, View.OnClickListener {

    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    Button ConformAppoint;
    String values = "AB";
    // Time slot creation
    private Timeslot_GridviewAdapter mAdapter;
    private ArrayList<String> times;
    GridView time_gridview;
    static final String TAG = BookAppointActivity.class.getName();
    RadioGroup appointType;
    RadioButton Vertiual, Walkin;
    EditText Symptoms, Symptoms1, AppointDate;
    String symptomsOne, symptomsTwo, walkin_type, Walin_date, timeslot;
    GridViewAdapter gridViewAdapter;
    String doctorId;
    String doctorFees;
    List<AvailableTimeslotsModel> availableTimeslotsModelList = null;
    TextView UserName, UserID, selectedTime;
    int yearStr;
    int monthStr;
    int dayStr;
    boolean isOkayClicked;
    int previousIndex = 0;
    CircleImageView circleImageView;
    NormalFont Token_head, ToeknSystem, dispalymsg;
    int ResponseCode;
    int res;
    Button Ok;
    public boolean offer_alerbox;
    String MainOffers;
    String SubOffers;
    boolean ISNoFee;
    android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_bookappoint);
        registerReceiver(receiverUpdate,
                new IntentFilter(VTConstants.TIMSLOT_BROADCAST));
        if (getIntent() != null) {
            doctorId = getIntent().getStringExtra(VTConstants.DOCTOR_ID);
            doctorFees = getIntent().getStringExtra(VTConstants.DOCTOR_FEES);
        }
        initvalues();
        navigation();

    }

    public void datePicker(View v) {


        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(BookAppointActivity.this.getCurrentFocus().getWindowToken(), 0);
        final Calendar c = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                if (isOkayClicked) {
                    yearStr = selectedYear;
                    monthStr = selectedMonth;
                    dayStr = selectedDay;
                }
                isOkayClicked = false;
            }
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                BookAppointActivity.this, datePickerListener,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.cancel();
                            isOkayClicked = false;
                        }
                    }
                });
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (VTConstants.checkAvailability(BookAppointActivity.this)) {
                            SnackBarhide();
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                isOkayClicked = true;
                                DatePicker datePicker = datePickerDialog
                                        .getDatePicker();
                                datePickerListener.onDateSet(datePicker,
                                        datePicker.getYear(),
                                        datePicker.getMonth(),
                                        datePicker.getDayOfMonth());

                                final Date currentDate = new Date();
                                SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");

                                Date selectedDate = new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth());

                                int compare = dFormat.format(selectedDate).compareTo(dFormat.format(currentDate));

                                if (compare >= 0) {
                                    Log.d("BOOKAPP","compareDate Valid :  "+dFormat.format(selectedDate));
                                    AppointDate.setText(dFormat.format(selectedDate));

                                    selectedTime.setText("Selected Date : " + dFormat.format(selectedDate));
                                    getTimeSlot(doctorId, dFormat.format(selectedDate));

                                } else {
                                    Log.d("BOOKAPP","compareDate Valid :  "+dFormat.format(selectedDate));
                                    DisplayToast("Select Valid Date!");
                                    AppointDate.setText("");
                                    dispalymsg.setVisibility(View.VISIBLE);
                                    Token_head.setVisibility(View.GONE);
                                    ToeknSystem.setVisibility(View.GONE);
                                    time_gridview.setVisibility(View.GONE);
                                }

                                ConformAppoint.setEnabled(false);
                                ConformAppoint.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybutton_graylight));
                                time_gridview.setVisibility(View.GONE);
                                ToeknSystem.setVisibility(View.GONE);
                            }
                        } else {
                            SnackBar();
                        }
                    }

                });
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    private void getTimeSlot(String doctorId, String curretDate) {
        try {
            AlldoctorsListProvider alldoctorsListProvider = new AlldoctorsListProvider(getApplicationContext(), this);
            alldoctorsListProvider.ListallDoctorReq(doctorId, curretDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initvalues() {
        // Navigation FirstName and Id LastName
        navigationView = (NavigationView) findViewById(R.id.book_navigation_view);
        UserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserName);
        UserID = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserdid);
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        UserName.setText(sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME, "null"));
        UserID.setText(sharedPreferences.getString(VTConstants.USER_ID, "null"));
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        String userId = sharedPreferences.getString(VTConstants.USER_ID, null);
        availableTimeslotsModelList = new ArrayList<AvailableTimeslotsModel>();
        registerReceiver(receiverUpdate, new IntentFilter(VTConstants.BOOK_APPOINT_BROADCAST));
//
        appointType = (RadioGroup) findViewById(R.id.radioGroup11);
        Token_head = (NormalFont) findViewById(R.id.timeslotLable);
        selectedTime = (TextView) findViewById(R.id.selecteddate);
        dispalymsg = (NormalFont) findViewById(R.id.dispalymsg);
        ToeknSystem = (NormalFont) findViewById(R.id.ToeknSystem);
        Vertiual = (RadioButton) findViewById(R.id.btnVertiual);
        Walkin = (RadioButton) findViewById(R.id.btnValkin);
        Symptoms = (EditText) findViewById(R.id.EtxtsymptomsHead);
        Symptoms1 = (EditText) findViewById(R.id.EtxtsymptomsHead2);
        AppointDate = (EditText) findViewById(R.id.etxtmy_dataofBirth);
        toolbar = (Toolbar) findViewById(R.id.bookappoint_toolbar1);
        setSupportActionBar(toolbar);
        time_gridview = (GridView) findViewById(R.id.grid_timeslot);
        gridViewAdapter = new GridViewAdapter();
        time_gridview.setAdapter(gridViewAdapter);
        ConformAppoint = (Button) findViewById(R.id.conformappoint);
        ConformAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VTConstants.checkAvailability(BookAppointActivity.this)) {
                    SnackBarhide();
                    bookappoint();
                } else {
                    SnackBar();
                }
            }
        });
        //start:display profile image
        circleImageView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.patient_profile_image);
        String patientID = sharedPreferences.getString(VTConstants.USER_ID, null);
        String thumbnail_photo = VisirxApplication.userRegisterDAO.getUserThumnailpath(patientID);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void dataLoad(List<AvailableTimeslotsModel> list, String reservationMode, int responseCode,
                         boolean offerStatus, String minOffer, String subOffer,boolean isNofee) {
        ResponseCode = responseCode;
        offer_alerbox = offerStatus;
        MainOffers = minOffer;
        SubOffers = subOffer;
        ISNoFee = isNofee;

        if (offer_alerbox) {
            Discountalertbox(MainOffers, SubOffers);
        }

        String reservationMode1 = reservationMode;
        if (reservationMode1.equalsIgnoreCase("TOKEN")) {
            dispalymsg.setVisibility(View.GONE);
            Token_head.setVisibility(View.VISIBLE);
            Token_head.setText("Available Token ");
            ToeknSystem.setVisibility(View.VISIBLE);
            ToeknSystem.setText(list.get(0).getTimeslot());
            timeslot = list.get(0).getTimeslot();
            time_gridview.setVisibility(View.GONE);
        } else {
            dispalymsg.setVisibility(View.GONE);
            Token_head.setVisibility(View.VISIBLE);
            Token_head.setText("Available Times ");
            ToeknSystem.setVisibility(View.GONE);
            time_gridview.setVisibility(View.VISIBLE);
        }
        availableTimeslotsModelList = list;
        gridViewAdapter.notifyDataSetChanged();

        res = 1;

        if (availableTimeslotsModelList.size() > 0) {
//            time_gridview.setVisibility(View.VISIBLE);

            System.out.println("ResponseCode_STATUS" + ResponseCode);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                ConformAppoint.setEnabled(true);
                ConformAppoint.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybutton));
            } else {
                ConformAppoint.setEnabled(true);
                ConformAppoint.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybutton));
            }
        } else {

            ConformAppoint.setEnabled(false);
            ConformAppoint.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybutton_graylight));
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnrRootLayout:
                try {
                    previousIndex = (int) view.getTag();
                    gridViewAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;

        }
    }


    private void Discountalertbox(String mainoff, String suboff) {
        LayoutInflater li = LayoutInflater.from(BookAppointActivity.this);
        View promptsView = li.inflate(R.layout.discoutbox, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(BookAppointActivity.this);
        Ok = (Button) promptsView.findViewById(R.id.discountok);
        NormalFont mainoffer = (NormalFont) promptsView.findViewById(R.id.mainoffer);
        NormalFont suboffer = (NormalFont) promptsView.findViewById(R.id.suboffer);

        mainoffer.setText(mainoff);
        suboffer.setText(suboff);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent dashboardActivity = new Intent(BookAppointActivity.this, DashBoardActivity.class);
//                startActivity(dashboardActivity);
//                dashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    public class GridViewAdapter extends BaseAdapter {

        public class ViewHolderClass {
            public TextView textView1;
            public LinearLayout lnrRootLayout;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return availableTimeslotsModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return availableTimeslotsModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolderClass viewHolderClass;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.book_appmnt_list, null);
                new ViewHolder(convertView);
                viewHolderClass = new ViewHolderClass();
                viewHolderClass.textView1 = (TextView) convertView.findViewById(R.id.lbltime);
                viewHolderClass.lnrRootLayout = (LinearLayout) convertView.findViewById(R.id.lnrRootLayout);
                convertView.setTag(viewHolderClass);
            } else {
                viewHolderClass = (ViewHolderClass) convertView.getTag();
            }
            viewHolderClass.textView1.setText(availableTimeslotsModelList.get(position).getTimeslot());
            viewHolderClass.lnrRootLayout.setOnClickListener(BookAppointActivity.this);
            viewHolderClass.lnrRootLayout.setTag(position);
            if (previousIndex == position) {
                viewHolderClass.lnrRootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.gridbutton_bookappoint));
//                viewHolderClass.lnrRootLayout.setBackgroundColor(Color.parseColor("#369575"));
                viewHolderClass.textView1.setTextColor(Color.parseColor("#FFFFFF"));
                timeslot = viewHolderClass.textView1.getText().toString();
            } else {
                viewHolderClass.lnrRootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.gridbutton_bookappointgray));
                viewHolderClass.textView1.setTextColor(Color.parseColor("#333333"));
            }
            final int ppt = position;
            viewHolderClass.lnrRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousIndex = (int) ppt;
                    gridViewAdapter.notifyDataSetChanged();
                    timeslot = viewHolderClass.textView1.getText().toString();
                    Toast.makeText(getApplicationContext(), "Your selected time " + timeslot, Toast.LENGTH_SHORT).show();
                }
            });
            viewHolderClass.textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousIndex = (int) ppt;
                    gridViewAdapter.notifyDataSetChanged();
                    timeslot = viewHolderClass.textView1.getText().toString();
                    Toast.makeText(getApplicationContext(), "Your selected time " + timeslot, Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        class ViewHolder {

            TextView tvtitle;

            public ViewHolder(View view) {
                tvtitle = (TextView) view.findViewById(R.id.lbltime);
                tvtitle.setTextColor(Color.parseColor("#369575"));
                view.setTag(this);
            }
        }
    }

    public void bookappoint() {
        try {
            if (IsValidate()) {
                int id = appointType.getCheckedRadioButtonId();
                View radioButton = appointType.findViewById(id);
                int radioId = appointType.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) appointType.getChildAt(radioId);
                walkin_type = (String) btn.getText();
                symptomsOne = Symptoms.getText().toString().trim();
                symptomsTwo = Symptoms1.getText().toString().trim();

                if (!symptomsTwo.trim().toString().isEmpty()) {

                    symptomsTwo = symptomsTwo;

                } else {

                    symptomsTwo = "";

                }
                Walin_date = AppointDate.getText().toString().trim();
                String gradiogrup = btn.getText().toString();
                Intent intent = new Intent(BookAppointActivity.this, ConformAppointmentActivity.class);
                intent.putExtra(VTConstants.DOCTORID, doctorId);
                intent.putExtra(VTConstants.APPOINT_DATE, Walin_date);
                intent.putExtra(VTConstants.APPOINTMENT_TIME, timeslot);
                intent.putExtra(VTConstants.SYMPTOMS1, symptomsOne);
                intent.putExtra(VTConstants.SYMPTOMS2, symptomsTwo);
                intent.putExtra(VTConstants.APPOINTMENT_TYPE, gradiogrup);
                intent.putExtra(VTConstants.DOCTOR_FEES, doctorFees);
                intent.putExtra(VTConstants.OFFER_APPOINTMNET, "" + offer_alerbox);
                intent.putExtra(VTConstants.ISNOFEE, ISNoFee);
                System.out.println("ConformAppointmentActivity   " + "" + offer_alerbox);
                startActivity(intent);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean IsValidate() {
        try {
            if (Symptoms.getText().toString().trim().isEmpty() || Symptoms.getText().toString().length() <= 0) {
                DisplayToast("Enter the Symptom ");
                return false;
            }

            if (appointType.getCheckedRadioButtonId() == -1) {
                DisplayToast("Select Appointment Mode");
                return false;
            }
            if (AppointDate.getText().toString() == null || AppointDate.getText().toString().length() <= 0) {
                DisplayToast("Select the Date");
                return false;
            }
            if (timeslot == null || timeslot.toString().trim().isEmpty() || timeslot.toString().length() <= 0) {
                DisplayToast("Slot not available");
                return false;
            }


        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
        return true;
    }
// Navigation Drawer

    public void navigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBar actionBar = getSupportActionBar();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(BookAppointActivity.this.getCurrentFocus().getWindowToken(), 0);

            }

            public void onDrawerOpened(View drawerView) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(BookAppointActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        navigationView = (NavigationView) findViewById(R.id.book_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        Intent dash = new Intent(getBaseContext(), DashBoardActivity.class);
                        startActivity(dash);
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
                    case R.id.MyProfile:
                        Intent MyProfile = new Intent(getApplicationContext(), MyProfileActivity.class);
                        startActivity(MyProfile);
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

    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }
    };

    public void DisplayToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        try {

            Intent in = new Intent(getApplicationContext(),
                    DashBoardActivity.class);
            in.putExtra("msg", "NA");
            startActivity(in);
            finish();

        } catch (Exception e) {
            e.printStackTrace();

        }

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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverUpdate);
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
                , BookAppointActivity.this);
    }

    public void SnackBarhide() {
        SnackbarManager.dismiss();
    }


    public void LogoutDialog() {
        LayoutInflater li = LayoutInflater.from(BookAppointActivity.this);
        View promptsView = li.inflate(R.layout.logout_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BookAppointActivity.this);
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
        LogoutProvider logoutProvider = new LogoutProvider(BookAppointActivity.this);
        logoutProvider.SendLogoutReq(sharedPreferences.getString(VTConstants.GCM_ID, null));
//            android.os.Process.killProcess(android.os.Process.myPid());
//                                                        prePosition = -1;

    }
}



