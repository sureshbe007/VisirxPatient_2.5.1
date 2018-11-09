package com.visirx.patient.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.ImageDownloaderProvider;
import com.visirx.patient.model.AppointmentModel;
import com.visirx.patient.model.CareTeamModel;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.VTConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Suresh on 14-02-2016.
 */
public class CustomersTable {

    private static final String TAG = CustomersTable.class.getName();
    private SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    private Context context;

    public CustomersTable(SQLiteDatabase mdb, Context context) {
        super();
        db = mdb;
        this.context = context;
    }
    public static final String CUSTOMER_ID = "customer_id";
    public static final String USER_ID = "user_id";
    public static final String FISRT_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    //public static final String CUSTOMER_PHOTO = "photo";
    public static final String CUSTOMER_GENDER = "gender";
    private static final String DATE_OF_BIRTH = "date_of_birth";
    public static final String CUSTOMER_MOBILE = "mobile_number";
    public static final String IMAGE_THUMBNAIL_PATH = "thumbnail_path";
    public static final String IMAGE_PATH = "image_path";
    private static final String LAST_UPDATED = "last_updated";

    private static final String USER_ROLE = "user_role";
    public static final String CUSTOMER_SEPCIALIZATION = "specialiation";
    public static final String CUSTOMER_DESCRIPTION = "description";
    public static final String CUSTOMER_ADDRESS = "address";
    public static final String CUSTOMER_ZIPCODE = "zipcode";
    public static final String CUSTOMER_FEES = "customer_fees";
    public static final String VISIRX_FEES = "visirx_fees";
    public static final String CARTEAM_STATUS = "careteam_status";
    public static final String IS_DELETED = "is_deleted";

    static String PRIMARYKEY_REG = USER_ID+","+CUSTOMER_ID;

    public static final String TABLE_CUSTOMERS = "customers_new";
    public static final String TABLE_CUSTOMERS_CREATE = " create table IF NOT EXISTS " +
            TABLE_CUSTOMERS + "(" +
            USER_ID + " text not null , " +
            CUSTOMER_ID + " text not null , " +
            FISRT_NAME + " text not null, " +
            LAST_NAME + " text not null, " +
            DATE_OF_BIRTH + " text, " +
            CUSTOMER_GENDER + " text not null , " +
            CUSTOMER_MOBILE + " text, " +
            IMAGE_THUMBNAIL_PATH + " text , " +
            IMAGE_PATH + " text , " +
            USER_ROLE + " text , " +
            CUSTOMER_SEPCIALIZATION + " text , " +
            CUSTOMER_DESCRIPTION + " text , " +
            CUSTOMER_ADDRESS + " text , " +
            CUSTOMER_ZIPCODE + " text , " +
            CUSTOMER_FEES + " text , " +
            VISIRX_FEES + " text , " +
            LAST_UPDATED + " text ," +
            CARTEAM_STATUS + " INTEGER DEFAULT 0 ," +
            IS_DELETED + " INTEGER DEFAULT 0, " +
            "primary key(" + PRIMARYKEY_REG + ")" + ");";


    public boolean insertCustomerDetails(FindDoctorModel model) {

        long flag1 = 0;
        boolean isUpdated = false;

        sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        String patientID = sharedPreferences.getString(VTConstants.USER_ID, null);

        try {
            ContentValues initialValues1 = getContentValues(model);
            Cursor mCursor1 = db.query(TABLE_CUSTOMERS, new String[]
                    {USER_ID}, USER_ID + "=? " + " AND " + CUSTOMER_ID + "=?"
                    , new String[]{model.getDoctorId(),patientID}, null, null, null, null);

            if (mCursor1 == null || mCursor1.getCount() <= 0) {
                if (mCursor1 != null) {
                    mCursor1.close();
                }
                flag1 = db.insert(TABLE_CUSTOMERS, null, initialValues1);
                isUpdated = true;

            } else {

                ContentValues contentValues = new ContentValues();
                contentValues.put(IS_DELETED, 0);
                flag1 = db.update(TABLE_CUSTOMERS, contentValues,
                        USER_ID + "=? "+ " AND " + CUSTOMER_ID + "=?", new String[]{model.getDoctorId(),patientID});
                Logger.w("CUSTOMER", "UPDATE_FLAG" + flag1);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting customer information");
        }
        return isUpdated;
    }

     //  insert all doctors from Server  in login
    public boolean insertFromservletCustomerDetails(CareTeamModel model) {
        long flag1 = 0;
        boolean isUpdated = false;
        String lastUpdatedDb = "", imgThumbPath = "";
        Logger.w("CUSTOMER","CareTeamModel getPerfomerid    "+model.getPerfomerid());
        try {

            ContentValues initialValues1 = getContentValues(model);
            Cursor mCursor1 = db.query(TABLE_CUSTOMERS, new String[]
                    {USER_ID,
                            IMAGE_THUMBNAIL_PATH,
                            LAST_UPDATED}, USER_ID + "=? " + " AND " + CUSTOMER_ID + "=?"
                    , new String[]{model.getPerfomerid(),model.getCustomerid()}, null, null, null, null);


            if (mCursor1 == null || mCursor1.getCount() <= 0) {
                if (mCursor1 != null) {
                    mCursor1.close();
                }
                flag1 = db.insert(TABLE_CUSTOMERS, null, initialValues1);
                Logger.w("CUSTOMER"," INSETED USER ID = "+flag1);
                isUpdated = true;

            } else {
                mCursor1.moveToFirst();
                for (int counter = 0; counter < mCursor1.getCount(); counter++) {
                    if (mCursor1.getString(2) != null) {
                        lastUpdatedDb = mCursor1.getString(2);
                    }
                    if (mCursor1.getString(1) != null) {
                        imgThumbPath = mCursor1.getString(1).toString();
                    }
                    mCursor1.moveToNext();
                }

                if (lastUpdatedDb.equals(model.getLastUpdatedAt())) {

                    //emr list - modify - rony
                    if ((imgThumbPath != null && !VTConstants.CheckFileExists(imgThumbPath)) || (imgThumbPath == null))
                    {
                        //emr list - modify - rony
                        isUpdated = true;
                        Log.d("SPIN", "else 2 : no change in timestamps .File is missing from directory.sync required.");
                    } else {
                        isUpdated = false;
                    }
                } else {
                    Log.d("SPIN", "else 3 : different timestamps.sync required.");
                    isUpdated = true;

                }


            }
            //Update doctor profile image
            if (isUpdated) {

                flag1 = db.update(TABLE_CUSTOMERS, initialValues1,USER_ID + "=? " +  " AND " + CUSTOMER_ID + "=?" , new String[]{model.getPerfomerid(),model.getCustomerid()});
                Log.d("SPIN", "TABLE_CUSTOMERS : Existing customer -> update success flag : " + flag1);

                Log.d("SPIN", "doctor base data updated. Sync Required.");
                ImageDownloaderProvider dwnlodProvider = new ImageDownloaderProvider(context, VTConstants.NOTIFICATION_APPTS);
                dwnlodProvider.SendProfileImgReq(model.getPerfomerid());

            } else {
                Log.d("SPIN", "doctor base data is upto date. No sync required.");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error insertFromservletCustomerDetails" + e.getMessage());
        }
        return isUpdated;
    }

//    public boolean setStatusFlag(String doctorid, int status) {
//        long flag2 = 0;
//        boolean isUpdated2 = false;
//        try {
//            ContentValues initialValues1 = getContentValues(doctorid);
//            Cursor mCursor1 = db.query(TABLE_CUSTOMERS, new String[]
//                    {USER_ID}, USER_ID + "=? ", new String[]{doctorid}, null, null, null, null);
//            if (mCursor1 == null || mCursor1.getCount() <= 0) {
//                if (mCursor1 != null) {
//                    mCursor1.close();
//                }
//                flag2 = db.insert(TABLE_CUSTOMERS, null, initialValues1);
//                isUpdated2 = true;
//
//            } else {
//                flag2 = db.update(TABLE_CUSTOMERS, initialValues1,
//                        USER_ID + "=? ", new String[]{doctorid});
//            }
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            LogTrace.e(TAG, "Error setStatusFlag");
//        }
//        return isUpdated2;
//    }


    public boolean checkDocttor(String DoctorId, String customerId) {
        boolean check = false;
        Cursor mCursor = db.query(TABLE_CUSTOMERS, new String[]
                        {
                                USER_ID,
                        },
                USER_ID + "=?" + " AND " + CUSTOMER_ID + "=?" + " AND " + IS_DELETED + "=?", new String[]{DoctorId, customerId, "0"},
                null,
                null,
                null,
                null);

        if (mCursor == null || mCursor.getCount() <= 0) {
            if (mCursor != null)
                mCursor.close();
            return false;
        }


        if (mCursor.getCount() > 0) {
            check = true;
        }
        System.out.println("CheckDoctor 3" + mCursor.getCount());
        return check;
    }

    public ContentValues getContentValues(int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CARTEAM_STATUS, 1);
        return contentValues;
    }

    // Find a Single Doctor by ID   /// Suresh Patient App
    public ArrayList<FindDoctorModel> Getcustomer(String userId) {

        ArrayList<FindDoctorModel> modelList = new ArrayList<FindDoctorModel>();
        sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        String patientID = sharedPreferences.getString(VTConstants.USER_ID, null);

        try {
            Cursor mCursor = db.query(TABLE_CUSTOMERS, new String[]
                            {
                                    USER_ID,
                                    FISRT_NAME,
                                    LAST_NAME,
                                    CUSTOMER_GENDER,
                                    CUSTOMER_SEPCIALIZATION,
                                    CUSTOMER_DESCRIPTION,
                                    CUSTOMER_FEES,
                                    VISIRX_FEES,
                                    IMAGE_THUMBNAIL_PATH},
                    USER_ID + "=?" + " AND " + CUSTOMER_ID + "=?" , new String[]{userId,patientID},
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return modelList;
            }
            mCursor.moveToFirst();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                FindDoctorModel model = new FindDoctorModel();
                model.setDoctorId(mCursor.getString(mCursor.getColumnIndex(USER_ID)));
                model.setDoctorFirstName(mCursor.getString(mCursor.getColumnIndex(FISRT_NAME)));
                model.setDoctorLastName(mCursor.getString(mCursor.getColumnIndex(LAST_NAME)));
                model.setDoctorGender(mCursor.getString(mCursor.getColumnIndex(CUSTOMER_GENDER)));
                model.setDoctorSpecialization(mCursor.getString(mCursor.getColumnIndex(CUSTOMER_SEPCIALIZATION)));
                model.setDoctorDescription(mCursor.getString(mCursor.getColumnIndex(CUSTOMER_DESCRIPTION)));
                model.setDoctorfee(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(CUSTOMER_FEES))));
                model.setVisirxfee(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(VISIRX_FEES))));
                model.setCustomerImageThumbnailPath(mCursor.getString(mCursor.getColumnIndex(IMAGE_THUMBNAIL_PATH)));
                modelList.add(model);
                mCursor.moveToNext();
            }
            mCursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error Getcustomer");
        }
        return modelList;
    }

    public String getName(String userId) {
        Log.d("SPIN", "Inside updateCustomerThumbImagePath : customer table empty" + userId);

        sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        String patientID = sharedPreferences.getString(VTConstants.USER_ID, null);

        String userName = null;
        Cursor mCursor = db.query(TABLE_CUSTOMERS, new String[]{
                FISRT_NAME, LAST_NAME
        }, USER_ID + " = '"+ userId + "' AND " + CUSTOMER_ID + "='" + patientID + "'", null, null, null, null);
        if (mCursor == null || mCursor.getCount() <= 0) {
            if (mCursor != null) {
                mCursor.close();
            }
            Log.d("SPIN", "Inside updateCustomerThumbImagePath : customer table emptyIF");

        } else {
            Log.d("SPIN", "Inside updateCustomerThumbImagePath : user id : count ELSE" + mCursor.getCount());
            mCursor.moveToFirst();
            userName = mCursor.getString(mCursor.getColumnIndex(FISRT_NAME)) + " " + mCursor.getString(mCursor.getColumnIndex(LAST_NAME));
        }
        Log.d("SPIN", "Inside updateCustomerThumbImagePath : customer table empty" + userName);
        return userName;
    }

//    public ArrayList<String> GetDoctorname(String userId) {
//        ArrayList<String> nameList = new ArrayList<String>();
//        try {
//            Cursor mCursor = db.query(TABLE_CUSTOMERS, new String[]
//                            {FISRT_NAME, LAST_NAME, CUSTOMER_SEPCIALIZATION},
//                    USER_ID + "=?", new String[]{userId}, null, null, null, null);
//            if (mCursor == null || mCursor.getCount() <= 0) {
//                if (mCursor != null)
//                    mCursor.close();
//                return nameList;
//            }
//            mCursor.moveToFirst();
//            for (int counter = 0; counter < mCursor.getCount(); counter++) {
//                nameList.add(mCursor.getString(mCursor.getColumnIndex(FISRT_NAME)));
//                nameList.add(mCursor.getString(mCursor.getColumnIndex(LAST_NAME)));
//                nameList.add(mCursor.getString(mCursor.getColumnIndex(CUSTOMER_SEPCIALIZATION)));
//                mCursor.moveToNext();
//            }
//            mCursor.close();
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            LogTrace.e(TAG, "Error GetDoctorname");
//        }
//        return nameList;
//    }


    private ContentValues getContentValues(FindDoctorModel model) {
        ContentValues initialValues = new ContentValues();
        try {
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            String patientID = sharedPreferences.getString(VTConstants.USER_ID, null);
            initialValues.put(CUSTOMER_ID, patientID);
            initialValues.put(USER_ID, model.getDoctorId());
            initialValues.put(FISRT_NAME, model.getDoctorFirstName());
            initialValues.put(LAST_NAME, model.getDoctorLastName());
            initialValues.put(CUSTOMER_GENDER, model.getDoctorGender());
            initialValues.put(CUSTOMER_SEPCIALIZATION, model.getDoctorSpecialization());
            initialValues.put(CUSTOMER_DESCRIPTION, model.getDoctorDescription());
            initialValues.put(CUSTOMER_FEES, model.getDoctorfee());
            initialValues.put(VISIRX_FEES, model.getVisirxfee());
            initialValues.put(IS_DELETED, "0");
//            Logger.w("CUSTOMER", "getDoctorId" + model.getDoctorId());
//            Logger.w("CUSTOMER", "getDoctorFirstName" + model.getDoctorFirstName());
//            Logger.w("CUSTOMER", "getDoctorDescription" + model.getDoctorDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }

    private ContentValues getContentValues(CareTeamModel model) {
        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(CUSTOMER_ID, model.getCustomerid());
            initialValues.put(USER_ID, model.getPerfomerid());
            initialValues.put(FISRT_NAME, model.getDoctorFirstName());
            initialValues.put(LAST_NAME, model.getDoctorLastName());
            initialValues.put(CUSTOMER_GENDER, model.getDoctorGender());
            initialValues.put(CUSTOMER_SEPCIALIZATION, model.getDoctorSpecialization());
            initialValues.put(CUSTOMER_DESCRIPTION, model.getDoctorDescription());
            initialValues.put(CUSTOMER_FEES, model.getDoctorFee());
            initialValues.put(VISIRX_FEES, model.getVisiRxFee());
            initialValues.put(LAST_UPDATED, model.getLastUpdatedAt());
            initialValues.put(IS_DELETED, model.getIsDeleted());

            Logger.w("CUSTOMER11 ", "getPerfomerid" + model.getPerfomerid());
            Logger.w("CUSTOMER11 ", "getDoctorFirstName" + model.getDoctorFirstName() + "  " + model.getDoctorLastName());
            Logger.w("CUSTOMER11 ", "getDoctorFirstName" + model.getDoctorFirstName() + "  " + model.getDoctorLastName());
            Logger.w("CUSTOMER11 ", "getDoctorFee" + model.getDoctorFee());
            Logger.w("CUSTOMER11 ", "IS_DELETED" + model.getIsDeleted());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }

    private ContentValues getContentValues(String doctorId) {
        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(USER_ID, doctorId);
            initialValues.put(CARTEAM_STATUS, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }

//
//    public static int AgeCalculation(String dobFromDb) {
//        int age = 0;
//        try {
//            Date dobDate = VTConstants.dateFormat.parse(dobFromDb);
//            Calendar dob = Calendar.getInstance();
//            dob.setTime(dobDate);
//            Calendar today = Calendar.getInstance();
//            age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//            if (today.get(Calendar.MONTH) <= dob.get(Calendar.MONTH)) {
//                if (today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH))
//                    age--;
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return age;
//    }

    public FindDoctorModel GetCustomerDetailsForID(String userId) {
        long flag = 0;
        FindDoctorModel findDoctorModel = new FindDoctorModel();
        sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        String patientID = sharedPreferences.getString(VTConstants.USER_ID, null);

        try {
            Cursor mCursor = db.query(TABLE_CUSTOMERS, new String[]
                    {
                            FISRT_NAME,
                            LAST_NAME,
                            DATE_OF_BIRTH,
                            CUSTOMER_GENDER,
                            CUSTOMER_MOBILE,
                            IMAGE_THUMBNAIL_PATH,
                            CUSTOMER_SEPCIALIZATION,
                            CUSTOMER_DESCRIPTION,
                            CUSTOMER_ADDRESS,
                            CUSTOMER_ZIPCODE
                    }, USER_ID + " = '" + userId + "' AND " + CUSTOMER_ID + "='" + patientID + "'", null, null, null, null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }
                return null;
            } else {
                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    findDoctorModel.setDoctorFirstName(mCursor.getString(mCursor.getColumnIndex(FISRT_NAME)));
                    findDoctorModel.setDoctorLastName(mCursor.getString(mCursor.getColumnIndex(LAST_NAME)));
                    findDoctorModel.setDoctorSpecialization(mCursor.getString(mCursor.getColumnIndex(CUSTOMER_SEPCIALIZATION)));
                    findDoctorModel.setDoctorDescription(mCursor.getString(mCursor.getColumnIndex(CUSTOMER_DESCRIPTION)));
                    findDoctorModel.setCustomerImageThumbnailPath(mCursor.getString(mCursor.getColumnIndex(IMAGE_THUMBNAIL_PATH)));
                    mCursor.moveToNext();
                }
                return findDoctorModel;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }




    public String getThumnailPath(String doctorId, String patientID) {
        String imagePath = null;

        try {
            Log.d("CustomerTable", "getThumnailPath() ");
            Cursor mCursor = db.query(TABLE_CUSTOMERS, new String[]
                    {
                            IMAGE_THUMBNAIL_PATH,
                    }, USER_ID + " = '" + doctorId + "' AND " + CUSTOMER_ID + "='" + patientID + "'", null, null, null, null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                Log.d("CustomerTable", "mCursor.getCount() "+mCursor.getCount());
                if (mCursor != null) {
                    mCursor.close();
                }
                return null;
            } else {
                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    imagePath = mCursor.getString(mCursor.getColumnIndex(IMAGE_THUMBNAIL_PATH));
                    mCursor.moveToNext();
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.d("CustomerTable", "imagePath  :  "+imagePath);

        return imagePath;
    }

    //RAMESH
    public ArrayList<FindDoctorModel> getAvailableDoctor() {

        ArrayList<FindDoctorModel> modelList = new ArrayList<FindDoctorModel>();
        try {
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            String userId = sharedPreferences.getString(VTConstants.USER_ID, null);

            Logger.w("CUSTOMER1", "USERID From preference   " + userId);
            Cursor mCursor = db.query(TABLE_CUSTOMERS, new String[]{
                            USER_ID,
                            FISRT_NAME,
                            LAST_NAME,
                            CUSTOMER_SEPCIALIZATION,
                            CUSTOMER_DESCRIPTION,
                            CUSTOMER_FEES,
                            VISIRX_FEES,
                            IMAGE_THUMBNAIL_PATH, IS_DELETED},
                    CUSTOMER_ID + "=?" + " AND " + IS_DELETED + "=?", new String[]{userId,"0"},
                    null,
                    null,
                    null,
                    null);


            Logger.w("CUSTOMER1", " mCursor.getCount()::" +  mCursor.getCount());

            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return modelList;
            }
            mCursor.moveToFirst();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                FindDoctorModel model = new FindDoctorModel();
                model.setDoctorId(mCursor.getString(mCursor.getColumnIndex(USER_ID)));
                model.setDoctorFirstName(mCursor.getString(mCursor.getColumnIndex(FISRT_NAME)));
                model.setDoctorLastName(mCursor.getString(mCursor.getColumnIndex(LAST_NAME)));
                model.setDoctorSpecialization(mCursor.getString(mCursor.getColumnIndex(CUSTOMER_SEPCIALIZATION)));
                model.setDoctorDescription(mCursor.getString(mCursor.getColumnIndex(CUSTOMER_DESCRIPTION)));
                model.setDoctorfee(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(CUSTOMER_FEES))));
                model.setCustomerImageThumbnailPath(mCursor.getString(mCursor.getColumnIndex(IMAGE_THUMBNAIL_PATH)));
                modelList.add(model);
                mCursor.moveToNext();

            }
            mCursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            Logger.w("getAvailableDoctor", " Error::" + e.getMessage());
        }
        return modelList;

    }
    //RAMESH


    public boolean updateCustomerThumbImagePath(String thumbPath, String userId) {
        try {
            int flag;
            String existingImagePath = null;
            ContentValues initialValues = new ContentValues();
            initialValues.put(IMAGE_THUMBNAIL_PATH, thumbPath);
            Cursor mCursor = db.query(TABLE_CUSTOMERS, new String[]
                    {IMAGE_THUMBNAIL_PATH
                    }, USER_ID + " = '"
                    + userId + "'", null, null, null, null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }
                return false;
            } else {
                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    existingImagePath = mCursor.getString(0);
                    mCursor.moveToNext();
                }

                flag = db.update(TABLE_CUSTOMERS, initialValues, USER_ID + "=?", new String[]{userId});
                if (existingImagePath == null || existingImagePath == "") {
                    Log.d("SPIN", "Inside updateCustomerThumbImagePath :no image path in the sqlite table.");
                } else {
                    Log.d("SPIN", "Inside updateLoggedUserThumbImagePath :Delete the image in the path if exists.");
                }
                return true;

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return false;
    }


    public void UpdateDoctor(String doctorID) {


        sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        String patientID = sharedPreferences.getString(VTConstants.USER_ID, null);

        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_DELETED, 1);
        db.update(TABLE_CUSTOMERS, contentValues,
                USER_ID + "=?" + " AND " + CUSTOMER_ID + "=?", new String[]{doctorID, patientID});

    }

}