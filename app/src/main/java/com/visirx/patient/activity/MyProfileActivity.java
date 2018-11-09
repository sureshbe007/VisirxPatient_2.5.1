package com.visirx.patient.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.MainActivity;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.com.visirx.patient.adapter.ProfileSave;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.LogoutProvider;
import com.visirx.patient.common.provider.MyProfileProvider;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.CustomerProfileModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.VTConstants;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity implements ProfileSave {

    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    SharedPreferences sharedPreferences;
    String values = "AB";
    TextView toolbar_title, UserName, UserID;
    RadioGroup genderRadio;
    RelativeLayout relative;
    EditText MyFirstName, MyLasttName, MyEmail, MyMobile, MyDate_ofbirth, MyAddress1, MyAddress2, MyAddress3, Zipcode, Myheight, Myweight;
    String firstname, lastname, email, mobile, dateof_birth, address1, address2, address3, zipcode, height, weight, gender, result;
    private int mDay;
    private int mMonth;
    private int mYear;
    String CurrentDate;
    CustomerProfileModel UserProfileModel;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    CircleImageView circleImageView, circleImageView1;
    String patientID;
    byte[] ThumUserImage;
    String imageThumb, lastCreated;
    static final String TAG = MyProfileActivity.class.getName();
    String address;
    //NormalFont
    String birthDate;
    static final String BirthDate = "birthdate";
    android.app.AlertDialog.Builder alertDialogBuilder;
    android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_my_profile);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        MyProfileActivity.this.registerReceiver(receiverUpdate, new IntentFilter(VTConstants.NOTIFICATION_LOGIN_USER_PROFILE));

        //Toast.makeText(getApplicationContext(),"oncreate",Toast.LENGTH_SHORT).show();

        initvalues();
        Navigation();

    }

    public void initvalues() {
        circleImageView = (CircleImageView) findViewById(R.id.list_avatar);
        navigationView = (NavigationView) findViewById(R.id.My_navigation_view);
        UserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserName);
        UserID = (TextView) navigationView.getHeaderView(0).findViewById(R.id.DashboardUserdid);
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        UserName.setText(sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME, "null"));
        UserID.setText(sharedPreferences.getString(VTConstants.USER_ID, "null"));
        patientID = sharedPreferences.getString(VTConstants.USER_ID, "null");
        relative = (RelativeLayout) findViewById(R.id.My_Image);
        genderRadio = (RadioGroup) findViewById(R.id.btnmy_gender);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        MyFirstName = (EditText) findViewById(R.id.etxtmy_firstName);
        MyLasttName = (EditText) findViewById(R.id.etxtmy_secodName);
        MyEmail = (EditText) findViewById(R.id.etxtmy_Email);
        MyMobile = (EditText) findViewById(R.id.etxtmy_Mobile);
        MyDate_ofbirth = (EditText) findViewById(R.id.etxtmy_dataofBirth);
        MyAddress1 = (EditText) findViewById(R.id.Etxtmy_address1);
        MyAddress2 = (EditText) findViewById(R.id.Etxtmy_address2);
        MyAddress3 = (EditText) findViewById(R.id.Etxtmy_address3);
        Zipcode = (EditText) findViewById(R.id.etxtmy_zipcode);
        Myheight = (EditText) findViewById(R.id.etxtmy_height);
        Myweight = (EditText) findViewById(R.id.etxtmy_weight);
        Myweight = (EditText) findViewById(R.id.etxtmy_weight);
        Zipcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if (!saveAlertbox()) {

                        ValidateMyProfileServer();
                    }

                }
                return false;
            }
        });

        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        String userId = sharedPreferences.getString(VTConstants.USER_ID, null);
        UserProfileModel = VisirxApplication.userRegisterDAO.getUserDetails(userId);
        if (UserProfileModel != null) {
            profileload();
            for (int i = 0; i < relative.getChildCount(); i++) {
                View child = relative.getChildAt(i);
                child.setEnabled(true);
            }
            toolbar_title.setText("Save");
        }

        else {
            for (int i = 0; i < relative.getChildCount(); i++) {
                View child = relative.getChildAt(i);
                child.setEnabled(false);
            }
            ValidateMyProfileServer();
            toolbar_title.setText("Edit");
        }
        toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAlertbox();
            }
        });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPop();
            }
        });
        //start:display profile image
        circleImageView1 = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.patient_profile_image);
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
                circleImageView1.setImageBitmap(myBitmap);
            } else {
                Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.default_image);
                circleImageView1.setImageBitmap(defaultIcon);
            }
        } else {
            Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.default_image);
            circleImageView1.setImageBitmap(defaultIcon);
        }
        //end:display profile image

    }

    //     Profile Image Set  and send to Server
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String profileThumbImage = VTConstants.createDirectoryProfileImageThumnail();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
        imageThumb = profileThumbImage + File.separator + VTConstants.SAVED_IMAGE_PREFIX + patientID + timeStamp + ".visirx";

        try {
            File f = null;
            f = new File(imageThumb);
            if (f.exists()) {
                f.delete();
            }
        } catch (Exception e) {
            LogTrace.w(TAG, "Error:::Delete existing profile - " + e.getMessage());
        }


        File destination = new File(imageThumb);
        FileOutputStream fo;
        try {
            ThumUserImage = bytes.toByteArray();
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        circleImageView.setImageBitmap(thumbnail);
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);

        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

//        //  Profile Image
//        String profilePath = VTConstants.createDirectoryProfileImage();
//        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
//        String imageUrl = profilePath + File.separator + VTConstants.SAVED_IMAGE_PREFIX + patientID + timeStamp + ".jpg";
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.PNG, 100, bytes);
//
//        File F = new File(imageUrl);
//
//        try {
//            UserImage = bytes.toByteArray();
//            F.createNewFile();
//            FileOutputStream fos = new FileOutputStream(F);
//            fos.write(bytes.toByteArray());
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Profile ThumbImage
        String profileThumbImage = VTConstants.createDirectoryProfileImageThumnail();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
        imageThumb = profileThumbImage + File.separator + VTConstants.SAVED_IMAGE_PREFIX + patientID + timeStamp + ".visirx";

        try {
            File f = null;
            f = new File(imageThumb);
            if (f.exists()) {
                f.delete();
            }
        } catch (Exception e) {
            LogTrace.w(TAG, "Error:::Delete existing profile - " + e.getMessage());
        }

        ByteArrayOutputStream bytesThumbImage = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytesThumbImage);
        File ThumbFile = new File(imageThumb);
        try {
            ThumUserImage = bytesThumbImage.toByteArray();
            ThumbFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(ThumbFile);
            fos.write(bytesThumbImage.toByteArray());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            LogTrace.w(TAG, "Error:::save profile - " + e.getMessage());
        }
        circleImageView.setImageBitmap(bm);
    }
    // User can Select the Date of  Birth

    public void dateOfBirth(View v) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        final Date date1 = new Date();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String dateFormat1 = dFormat.format(date1);


        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                        SimpleDateFormat dFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                        String dateFormat = dFormat1.format(date);
                        int compare = date.compareTo(date1);

                        if (compare != 1) {

                            MyDate_ofbirth.setText(dateFormat);

                        } else {

                            MyDate_ofbirth.setText("");
                            DisplayToast("Enter Valid Date of Birth");

                        }

                    }
                }, mYear, mMonth, mDay);
        dpd.show();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    // Send User  Profile to Server
    private void ValidateMyProfileServer() {
        System.out.println("LL 1");
        if (IsValidate()) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date date = new Date();
                CurrentDate = dateFormat.format(date);
                int id = genderRadio.getCheckedRadioButtonId();
                View radioButton = genderRadio.findViewById(id);
                int radioId = genderRadio.indexOfChild(radioButton);

                RadioButton btn = (RadioButton) genderRadio.getChildAt(radioId);
                gender = (String) btn.getText();
                firstname = MyFirstName.getText().toString().trim();
                lastname = MyLasttName.getText().toString().trim();
                email = MyEmail.getText().toString().trim();
                mobile = MyMobile.getText().toString().trim();
                dateof_birth = MyDate_ofbirth.getText().toString().trim();
                address1 = MyAddress1.getText().toString().trim();
                address2 = MyAddress2.getText().toString().trim();
                address3 = MyAddress3.getText().toString().trim();

                address = address1 + "~" + address2 + "~" + address3;

                System.out.println("ADDRESSFIELDS 3" + address);
                zipcode = Zipcode.getText().toString().trim();
                height = Myheight.getText().toString().trim();
                weight = Myweight.getText().toString().trim();
                MyProfileProvider myProfileProvider = new MyProfileProvider(MyProfileActivity.this, this);

                if (ThumUserImage != null) {
                    sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                    String userId = sharedPreferences.getString(VTConstants.USER_ID, null);
                    VisirxApplication.userRegisterDAO.updateLoggedUserThumbImagePath(imageThumb, userId);
                }

                myProfileProvider.myProfileReq(firstname, lastname, email, mobile, dateof_birth,
                        address, zipcode, height, weight, gender, CurrentDate, ThumUserImage);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }

        }
    }


    private boolean IsValidate() {
        try {
            if (MyFirstName.getText().toString().trim().isEmpty() ||
                    MyFirstName.getText().toString().length() <= 0) {
                DisplayToast("Enter the FirstName");
                return false;
            }
            if (MyLasttName.getText().toString().trim().isEmpty() ||
                    MyLasttName.getText().toString().length() <= 0) {
                DisplayToast("Enter the LastName");
                return false;
            }
            if (MyEmail.getText().toString() == null ||
                    MyEmail.getText().toString().length() <= 0) {
                DisplayToast("Enter the Email ID");
                return false;
            }
            if (MyMobile.getText().toString() == null ||
                    MyMobile.getText().toString().length() <= 0) {
                DisplayToast("Enter the Mobile Number");
                return false;
            }
            if (MyMobile.getText().toString().length() < 10) {
                DisplayToast("Enter 10 Digit Valid Mobile Number");
                return false;
            }
            if (MyDate_ofbirth.getText().toString().length() <= 0 || MyDate_ofbirth.getText().toString().isEmpty()) {
                DisplayToast("Enter the Date of Birth");
                return false;
            }
            if (MyAddress1.getText().toString().trim().isEmpty() ||
                    MyAddress1.getText().toString().length() <= 0) {
                DisplayToast("Enter Address 1");
                return false;
            }

            if (Zipcode.getText().toString().trim().isEmpty() ||
                    Zipcode.getText().toString().length() <= 0) {
                DisplayToast("Enter the Zipcode");
                return false;
            }
            if (genderRadio.getCheckedRadioButtonId() == -1) {
                DisplayToast("Select the Gender");
                return false;
            }

        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
        return true;
    }


    public void Navigation() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBar actionBar = getSupportActionBar();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(MyProfileActivity.this.getCurrentFocus().getWindowToken(), 0);
            }

            public void onDrawerOpened(View drawerView) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(MyProfileActivity.this.getCurrentFocus().getWindowToken(), 0);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        navigationView = (NavigationView) findViewById(R.id.My_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (!IsValidate()) {

                    drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else {
                    if (!saveAlertbox()) {

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
                                drawerLayout.closeDrawer(navigationView);
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
                                LogoutDialog();
                                return true;
                            default:
//                        Toast.makeText(getApplicationContext(), "App Version: 1.3.6", Toast.LENGTH_SHORT).show();
                                return true;
                        }

                    }

                }
                return true;
            }

        });


    }


    public void profileload() {
        MyFirstName.setText(UserProfileModel.getCustomerFirstName());
        MyLasttName.setText(UserProfileModel.getCustomerLastName());
        MyEmail.setText(UserProfileModel.getCustomerEmail());
        MyMobile.setText(UserProfileModel.getCustomerMobileNumber());
        MyDate_ofbirth.setText(UserProfileModel.getCustomerDateOfBirth());


        System.out.println("UserProfileModel.getCustomerAddress()::" + UserProfileModel.getCustomerAddress());

        if (UserProfileModel.getCustomerAddress() != null) {
            String stringdata = UserProfileModel.getCustomerAddress().toString();

            String[] parts;
            parts = stringdata.split("~");
            try {


                if (parts.length > 0) {
                    MyAddress1.setText(parts[0]);


                }
                if (parts.length > 1) {
                    if (!parts[1].trim().isEmpty())

                    {
                        MyAddress2.setText(parts[1]);


                    } else {
                        MyAddress2.setText("");

                    }


                }
                if (parts.length > 2) {

                    if (!parts[2].trim().toString().isEmpty()) {

                        MyAddress3.setText(parts[2]);


                    } else {
                        MyAddress3.setText("");


                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            MyAddress1.setText("");
            MyAddress2.setText("");
            MyAddress3.setText("");
        }

        Zipcode.setText(UserProfileModel.getCustomerZipcode());
        Myheight.setText(UserProfileModel.getCustomerHeight());
        Myweight.setText(UserProfileModel.getCustomerWeight());
        lastCreated = UserProfileModel.getLastCreated();

        if (UserProfileModel.getCustomerGender().equalsIgnoreCase("MALE")) {
            ((RadioButton) genderRadio.getChildAt(0)).setChecked(true);
        } else if (UserProfileModel.getCustomerGender().equalsIgnoreCase("FEMALE")) {
            ((RadioButton) genderRadio.getChildAt(1)).setChecked(true);
        }

        //circleImageView.setImageBitmap(bm);

        String thumbnail_photo = UserProfileModel.getUser_thumimage();
        System.out.println("thumbnail_photo:::" + thumbnail_photo);

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
    }


    private void PhotoPop() {

        LayoutInflater li = LayoutInflater.from(MyProfileActivity.this);
        View promptsView = li.inflate(R.layout.imagechooser, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyProfileActivity.this);
        NormalFont Takephoto = (NormalFont) promptsView.findViewById(R.id.appointmentID);
        NormalFont FromLibrary = (NormalFont) promptsView.findViewById(R.id.doctorfees);
        NormalFont Cancel = (NormalFont) promptsView.findViewById(R.id.Platformfees);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);

        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        Takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
                alertDialog.hide();
            }
        });

        FromLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                alertDialog.hide();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
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
//Toast.makeText(getApplicationContext(),"BR",Toast.LENGTH_SHORT).show();
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
                        circleImageView1.setImageBitmap(myBitmap);
                    } else {
                        Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.default_image);
                        circleImageView1.setImageBitmap(defaultIcon);
                    }
                } else {
                    Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.default_image);
                    circleImageView1.setImageBitmap(defaultIcon);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    public void DisplayToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
        }
        return super.onOptionsItemSelected(menuItem);
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
                , MyProfileActivity.this);

    }

    public void SnackBarhide() {
        SnackbarManager.dismiss();
    }

    @Override
    public void saveProfile(String status) {

//        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
//        String userId = sharedPreferences.getString(VTConstants.USER_ID, null);
//        String status1 = String.valueOf(0);
//        UserProfileModel = VisirxApplication.userRegisterDAO.getUserDetails(userId);
//        if (UserProfileModel != null) {
//            profileload();
//            for (int i = 0; i < relative.getChildCount(); i++) {
//                View child = relative.getChildAt(i);
//                child.setEnabled(false);
//            }
//            toolbar_title.setText("Edit");
//        }
    }


    boolean saveAlertbox() {

        LayoutInflater li = LayoutInflater.from(MyProfileActivity.this);
        View promptsView = li.inflate(R.layout.profilesave_alertbox, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyProfileActivity.this);
        Button Yes = (Button) promptsView.findViewById(R.id.paynow);
        Button No = (Button) promptsView.findViewById(R.id.Cancelappoint);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VTConstants.checkAvailability(MyProfileActivity.this)) {
                    SnackBarhide();
                    alertDialog.dismiss();
                    toolbarSave();
                } else {
                    SnackBar();
                }

            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        return true;
    }

    public void toolbarSave() {

        try {
            if (IsValidate()) {

                if (toolbar_title.getText().toString().trim().equalsIgnoreCase("Edit")) {
                    for (int i = 0; i < relative.getChildCount(); i++) {
                        View child = relative.getChildAt(i);
                        child.setEnabled(true);
                    }
                    toolbar_title.setText("Save");

                } else {
                    ValidateMyProfileServer();
                    for (int i = 0; i < relative.getChildCount(); i++) {
                        View child = relative.getChildAt(i);
                        child.setEnabled(false);
                    }

                    if (IsValidate()) {
                        toolbar_title.setText("Edit");
                    } else {

                        for (int i = 0; i < relative.getChildCount(); i++) {
                            View child = relative.getChildAt(i);
                            child.setEnabled(true);
                        }
                        toolbar_title.setText("Save");
                    }

                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }

    }

    public void LogoutDialog() {
        LayoutInflater li = LayoutInflater.from(MyProfileActivity.this);
        View promptsView = li.inflate(R.layout.logout_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyProfileActivity.this);
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Log.d("MYPRO", "onBackPressed() :");
        saveAlertbox();
    }

    public void logoutClick() {
        sharedPreferences = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        sharedPreferences = getApplicationContext().getSharedPreferences(VTConstants.GCM_FILENAME, 0);
        EventChecking.myLogout(sharedPreferences.getString(VTConstants.USER_ID, "null"), sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME, "null"), true);
        LogoutProvider logoutProvider = new LogoutProvider(MyProfileActivity.this);
        logoutProvider.SendLogoutReq(sharedPreferences.getString(VTConstants.GCM_ID, null));
//            android.os.Process.killProcess(android.os.Process.myPid());
//                                                        prePosition = -1;

    }

}
