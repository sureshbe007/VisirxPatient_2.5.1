package com.visirx.patient.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.CustomerProfileModel;
import com.visirx.patient.model.RegisterModel;
import com.visirx.patient.utils.VTConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aa on 18.1.16.
 */
public class UsersRegTable {

    private SQLiteDatabase db;
    private static final String TAG = UsersRegTable.class.getName();

    public UsersRegTable(SQLiteDatabase mdb) {
        super();
        db = mdb;
    }

    //Register process
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NMAE = "last_name";
    public static final String USER_NAME = "user_name";
    private static final String USER_ID = "user_id";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String PASSWORD = "password";
    public static final String CONFORM_PASSWORD = "conform_password";
    private static final String USER_ADDRESS = "user_address";
    private static final String USER_ZIPCODE = "user_zipcode";
    private static final String USER_GENDER = "user_gender";
    private static final String USER_DOB = "user_dob";
    private static final String USER_LASTUPDATED = "user_lastupdated";
    private static final String USER_IMAGE = "user_image";
    private static final String USER_THUMBIMAGE = "user_thumbimage";
    private static final String USER_ROLEID = "user_roleid";
    private static final String USER_HEIGHT = "user_height";
    private static final String USER_WEIGHT = "user_weight";

    public static final String TABLE_REG_USERS = "register_users";
    static String PRIMARYKEY_REG = PHONE_NUMBER;
    String result;
    DateFormat df;
    String birthDate;

    public static final String TABLE_REG_USERS_CREATE = " create table IF NOT EXISTS " +
            TABLE_REG_USERS + "(" +
            FIRST_NAME + " text not null," +
            LAST_NMAE + " text not null, " +
            USER_NAME + " text, " +
            USER_ID + " text , " +
            EMAIL + " text," +
            PHONE_NUMBER + " text," +
            PASSWORD + " text, " +
            CONFORM_PASSWORD + " text," +
            USER_ADDRESS + " text, " +
            USER_ZIPCODE + " text," +
            USER_GENDER + " text, " +
            USER_DOB + " text ," +
            USER_LASTUPDATED + " text," +
            USER_IMAGE + " text," +
            USER_THUMBIMAGE + " text," +
            USER_ROLEID + " text, " +
            USER_HEIGHT + " text, " +
            USER_WEIGHT + " text, " +
            "primary key(" + PRIMARYKEY_REG + ")" + ");";


    public RegisterModel GetUsers() {
        RegisterModel registerModel = null;
        try {
            Cursor mCursor = db.query(TABLE_REG_USERS, new String[]{
                            FIRST_NAME,
                            LAST_NMAE,
                            USER_NAME,
                            USER_ID,
                            EMAIL,
                            PHONE_NUMBER,
                            PASSWORD,
                            CONFORM_PASSWORD,
                            USER_ADDRESS,
                            USER_ZIPCODE,
                            USER_GENDER,
                            USER_DOB,
                            USER_LASTUPDATED,
                            USER_IMAGE,
                            USER_THUMBIMAGE,
                            USER_ROLEID},
                    null,
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return registerModel;
            }
            mCursor.moveToFirst();
            registerModel = new RegisterModel();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                registerModel.setFirstName(mCursor.getString(1));
                registerModel.setLastName(mCursor.getString(2));
                registerModel.setUserName(mCursor.getString(3));
                registerModel.setUserId(mCursor.getString(4));
                registerModel.setEmailId(mCursor.getString(5));
                registerModel.setPhoneNumber(mCursor.getString(6));
                registerModel.setPassword(mCursor.getString(7));
                registerModel.setConformpassword(mCursor.getString(8));
                registerModel.setUserAddress(mCursor.getString(9));
                registerModel.setZipcode(mCursor.getString(10));
                registerModel.setGender(mCursor.getString(11));
                // String to  Date
                registerModel.setDob(mCursor.getString(12));
                registerModel.setLastUpdated(mCursor.getString(13));
                registerModel.setImage(mCursor.getString(14));
                registerModel.setImageThumbnailPath(mCursor.getString(15));
                registerModel.setRoleID(mCursor.getString(16));
                mCursor.moveToNext();
            }
            mCursor.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return registerModel;
    }


    public CustomerProfileModel getUserDetails(String userId) {
        CustomerProfileModel customerProfileModel = null;
        try {
            Cursor mCursor = db.query(TABLE_REG_USERS, new String[]{
                            FIRST_NAME,
                            LAST_NMAE,
                            USER_NAME,
                            USER_ID,
                            EMAIL,
                            PHONE_NUMBER,
                            PASSWORD,
                            CONFORM_PASSWORD,
                            USER_ADDRESS,
                            USER_ZIPCODE,
                            USER_GENDER,
                            USER_DOB,
                            USER_LASTUPDATED,
                            USER_ROLEID,
                            USER_HEIGHT,
                            USER_WEIGHT,
                            USER_THUMBIMAGE},
                    USER_ID + " = '" + userId + "'",
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return customerProfileModel;
            }
            mCursor.moveToFirst();
            customerProfileModel = new CustomerProfileModel();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                customerProfileModel.setCustomerFirstName(mCursor.getString(mCursor.getColumnIndex(FIRST_NAME)));
                customerProfileModel.setCustomerLastName(mCursor.getString(mCursor.getColumnIndex(LAST_NMAE)));
                customerProfileModel.setCustomerId(mCursor.getString(mCursor.getColumnIndex(USER_ID)));
                customerProfileModel.setCustomerEmail(mCursor.getString(mCursor.getColumnIndex(EMAIL)));
                customerProfileModel.setCustomerMobileNumber(mCursor.getString(mCursor.getColumnIndex(PHONE_NUMBER)));
                customerProfileModel.setCustomerAddress(mCursor.getString(mCursor.getColumnIndex(USER_ADDRESS)));
                customerProfileModel.setCustomerZipcode(mCursor.getString(mCursor.getColumnIndex(USER_ZIPCODE)));
                customerProfileModel.setCustomerDateOfBirth(mCursor.getString(mCursor.getColumnIndex(USER_DOB)));
                customerProfileModel.setCustomerGender(mCursor.getString(mCursor.getColumnIndex(USER_GENDER)));
                customerProfileModel.setCustomerHeight(mCursor.getString(mCursor.getColumnIndex(USER_HEIGHT)));

                System.out.println("getUserDetails:::" + mCursor.getString(mCursor.getColumnIndex(USER_THUMBIMAGE)));

                customerProfileModel.setUser_thumimage(mCursor.getString(mCursor.getColumnIndex(USER_THUMBIMAGE)));
                customerProfileModel.setLastCreated(mCursor.getString(mCursor.getColumnIndex(USER_LASTUPDATED)));
                // String to  Date
                customerProfileModel.setCustomerWeight("0");
                customerProfileModel.setLastCreated("0");
                mCursor.moveToNext();
            }
            mCursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return customerProfileModel;
    }

    //
    public String getUserThumnailpath(String userId) {
        String UserThumnailpath = "";
        try {
            Cursor mCursor1 = db.query(TABLE_REG_USERS, new String[]{USER_THUMBIMAGE}, USER_ID + " = '" + userId + "'", null, null, null, null);
            if (mCursor1 == null || mCursor1.getCount() <= 0) {
                if (mCursor1 != null)
                    mCursor1.close();
                return null;
            }


            if (mCursor1 != null || mCursor1.getCount() > 0) {
                mCursor1.moveToFirst();
                UserThumnailpath = mCursor1.getString(0);
            }
            mCursor1.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error getUserAddress :::" + e.getMessage().toString());
        }
        return UserThumnailpath;
    }


    public String getUserAddress(String userId) {
        String userAddress = "";
        try {
            Cursor mCursor1 = db.query(TABLE_REG_USERS, new String[]{USER_ADDRESS, USER_ZIPCODE}, USER_ID + " = '" + userId + "'", null, null, null, null);
            if (mCursor1 != null || mCursor1.getCount() > 0) {
                mCursor1.moveToFirst();
                userAddress = mCursor1.getString(0);
            }
            mCursor1.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error getUserAddress :::" + e.getMessage().toString());
        }
        return userAddress;
    }


    public boolean insertuserDetails(CustomerProfileModel model) {
        long flag1 = 0;
        boolean isUpdated = false;
        try {
            ContentValues initialValues1 = gerContentValues(model);
            Cursor mCursor1 = db.query(TABLE_REG_USERS, new String[]
                    {PHONE_NUMBER}, PHONE_NUMBER + "=? "
                    , new String[]{String.valueOf(model.getCustomerMobileNumber())}, null, null, null, null);
            if (mCursor1 == null || mCursor1.getCount() <= 0) {
                if (mCursor1 != null) {
                    mCursor1.close();
                }
                flag1 = db.insert(TABLE_REG_USERS, null, initialValues1);
                isUpdated = true;

            } else {
                flag1 = db.update(TABLE_REG_USERS, initialValues1, PHONE_NUMBER + "=? ", new String[]{String.valueOf(model.getCustomerMobileNumber())});

            }

            mCursor1.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting customer information");
        }
        return isUpdated;
    }


    public boolean updateLoggedUserThumbImagePath(String imageUrl, String userId) {

        LogTrace.w(TAG, " updateLoggedUserThumbImagePath userid : " + userId);
        LogTrace.w(TAG, " updateLoggedUserThumbImagePath imageUrl : " + imageUrl);

        try {
            int flag;

            String existingImagePath = null;

            Cursor mCursor = db.query(TABLE_REG_USERS
                    , new String[]{USER_THUMBIMAGE},
                    USER_ID + "= '" + userId + "'",
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }
                Log.d("SPIN", "Inside updateLoggedUserThumbImagePath : user table empty");
                return false;
            } else {
                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    existingImagePath = mCursor.getString(0);
                    mCursor.moveToNext();
                }

                LogTrace.w(TAG, " updateLoggedUserThumbImagePath userid : " + userId);
                LogTrace.w(TAG, " updateLoggedUserThumbImagePath imageUrl : " + imageUrl);

                ContentValues initialValues = new ContentValues();
                initialValues.put(USER_THUMBIMAGE, imageUrl);

                flag = db.update(TABLE_REG_USERS, initialValues, USER_ID + " = ?", new String[]{userId});

                LogTrace.w(TAG, flag + " Users table updated : " + userId);

                if (existingImagePath == null || existingImagePath == "") {
                    Log.d("SPIN", "Inside updateLoggedUserThumbImagePath :no image path in the sqlite table.");
                } else {
                    Log.d("SPIN", "Inside updateLoggedUserThumbImagePath :Delete the image in the path if exists.");
                    // rony Dashboard GC - starts
                    //delete old image from directory.
                    //VTConstants.DeleteFileFromPath(existingImagePath);
                    // rony Dashboard GC - ends
                }
                return true;

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting user information");
        }
        return false;
    }


    private ContentValues gerContentValues(CustomerProfileModel customerProfileModel) {
        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(FIRST_NAME, customerProfileModel.getCustomerFirstName());
            initialValues.put(LAST_NMAE, customerProfileModel.getCustomerLastName());
            initialValues.put(USER_ID, customerProfileModel.getCustomerId());
            initialValues.put(EMAIL, customerProfileModel.getCustomerEmail());
            initialValues.put(PHONE_NUMBER, customerProfileModel.getCustomerMobileNumber());
            initialValues.put(USER_ADDRESS, customerProfileModel.getCustomerAddress());
            initialValues.put(USER_ZIPCODE, customerProfileModel.getCustomerZipcode());
            initialValues.put(USER_GENDER, customerProfileModel.getCustomerGender());
            initialValues.put(USER_DOB, customerProfileModel.getCustomerDateOfBirth());
            initialValues.put(USER_LASTUPDATED, customerProfileModel.getLastCreated());
            initialValues.put(USER_IMAGE, customerProfileModel.getCustomerPhoto());
            initialValues.put(USER_GENDER, customerProfileModel.getCustomerGender());
            initialValues.put(USER_HEIGHT, "0");
            initialValues.put(USER_WEIGHT, "0");
            // Date  to  String
        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }
}
