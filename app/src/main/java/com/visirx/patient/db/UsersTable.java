package com.visirx.patient.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.LoginModel;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by aa on 21.1.16.
 */
public class UsersTable {

    private static final String TAG = UsersTable.class.getName();
    private SQLiteDatabase db;

    public UsersTable(SQLiteDatabase mdb) {
        super();
        db = mdb;
    }

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String STATUS = "status";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String PASSWORD = "password";
    public static final String IMAGE_THUMBNAIL_PATH = "image_thumbnail_path";
    public static final String IMAGE_PATH = "image_path";
    public static final String LAST_UPDATED = "last_updated";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String DESCRIPTION = "description";
    private static final String SPECIALIZATION = "specialization";
    private static final String ROLEID = "roleId";
    public static final String TABLE_LOGGED_IN_USER = "logged_in_user";
    static String PRIMARYKEY_REG = USER_ID;
    public static final String TABLE_USERS_CREATE = " create table IF NOT EXISTS " +
            TABLE_LOGGED_IN_USER + "(" +
            USER_ID + " text not null, " +
            STATUS + " integer not null, " +
            USER_NAME + " text not null, " +
            EMAIL + " text not null, " +
            PHONE_NUMBER + " text, " +
            PASSWORD + " text, " +
            IMAGE_THUMBNAIL_PATH + " text, " +
            IMAGE_PATH + " text, " +
            LAST_UPDATED + " text, " +
            FIRST_NAME + " text, " +
            LAST_NAME + " text, " +
            DESCRIPTION + " text, " +
            SPECIALIZATION + " text, " +
            ROLEID + " text, " +
            "primary key(" + PRIMARYKEY_REG + ")" + ");";

    public boolean insertLogin(LoginModel loginModel) {
        long flag = 0;
        boolean isUpdated = false;
        String lastUpdatedDb = null, imgThumbPath = null;
        try {
            ContentValues initialValues = gerContentValues(loginModel);
            Cursor mCursor = db.query(TABLE_LOGGED_IN_USER, new String[]
                            {
                                    USER_ID,
                                    LAST_UPDATED,
                                    IMAGE_THUMBNAIL_PATH,
                                    STATUS},
                    null,
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }
                flag = db.insert(TABLE_LOGGED_IN_USER, null, initialValues);
                isUpdated = true;

            }
            else {
                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    lastUpdatedDb = mCursor.getString(1);
                    imgThumbPath = mCursor.getString(2);
                    mCursor.moveToNext();
                }
                if (lastUpdatedDb.equals(loginModel.getLastUpdated())) {
                    if (imgThumbPath != null && !VTConstants.CheckFileExists(imgThumbPath)) {
                        isUpdated = true;
                    } else {
                        isUpdated = false;
                    }
                } else {
                    isUpdated = true;
                }
                flag = db.update(TABLE_LOGGED_IN_USER, initialValues, null, null);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting user information");
        }
        return isUpdated;
    }

    public LoginModel GetUsers() {
        LoginModel model = null;
        try {
            Cursor mCursor = db.query(TABLE_LOGGED_IN_USER, new String[]
                            {
                                    USER_ID,
                                    STATUS,
                                    USER_NAME,
                                    EMAIL,
                                    PHONE_NUMBER,
                                    PASSWORD,
                                    LAST_UPDATED,
                                    IMAGE_THUMBNAIL_PATH,
                                    FIRST_NAME,
                                    LAST_NAME,
                                    DESCRIPTION,
                                    SPECIALIZATION,
                                    ROLEID},
                    null,
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return model;
            }
            mCursor.moveToFirst();
            model = new LoginModel();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                model.setUserId(mCursor.getString(0));
                mCursor.moveToNext();
            }
            mCursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return model;
    }

    private ContentValues gerContentValues(LoginModel loginModel) {
        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(USER_ID, loginModel.getUserId());
            initialValues.put(USER_NAME, loginModel.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }

    public boolean updateLoggedUserThumbImagePath(String imageUrl, String userId) {
        try {
            int flag;
            String existingImagePath = null;
            ContentValues initialValues = new ContentValues();
            initialValues.put(IMAGE_THUMBNAIL_PATH, imageUrl);
            Cursor mCursor = db.query(TABLE_LOGGED_IN_USER, new String[]{
                            IMAGE_THUMBNAIL_PATH
                    },
                    null,
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
                flag = db.update(TABLE_LOGGED_IN_USER, initialValues, null, null);
                LogTrace.w(TAG, flag + " Users table updated : " + userId);
                if (existingImagePath == null || existingImagePath == "") {
                    Log.d("SPIN", "Inside updateLoggedUserThumbImagePath :no image path in the sqlite table.");
                } else {
                    Log.d("SPIN", "Inside updateLoggedUserThumbImagePath :Delete the image in the path if exists.");
                    // rony Dashboard GC - starts
                    //delete old image from directory.
                    VTConstants.DeleteFileFromPath(existingImagePath);
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

    public LoginModel GetLoggedUserFromDb() {
        LoginModel lmodel = new LoginModel();
        //String imagePath,loggedUserName;
        try {
            Cursor mCursor = db.query(TABLE_LOGGED_IN_USER, new String[]
                            {
                                    FIRST_NAME,
                                    LAST_NAME,
                                    SPECIALIZATION,
                                    DESCRIPTION,
                                    IMAGE_THUMBNAIL_PATH,
                                    IMAGE_PATH
                            },
                    null,
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }
                Log.d("SPIN", "Inside updateLoggedUserThumbImagePath : user table empty");

            } else {
                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    lmodel.setFirstName(mCursor.getString(0));
                    lmodel.setLastName(mCursor.getString(1));
//                    lmodel.setImageThumbnailPath(mCursor.getString(4));
                    mCursor.moveToNext();
                }
                return lmodel;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
