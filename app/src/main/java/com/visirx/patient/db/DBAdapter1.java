package com.visirx.patient.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by aa on 18.1.16.
 */
public class DBAdapter1 {

    private static final String TAG = DBAdapter1.class.getName();
    //data base
    private static final String DATABASE_NAME = "Visirxdb";
    private static final int DATABASE_VERSION = 5;
    private static final int DATABASE_PREVIOUS_VERSION = 4;
    @SuppressLint("SdCardPath")
    public static final String DB_FULL_PATH = "/data/data/com.visirx.patient/databases/" + DATABASE_NAME;

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter1(Context ctx, boolean dummy) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static DBAdapter1 mDBAdapter = null;

    public static DBAdapter1 getInstance(Context ctx) {
        if (mDBAdapter == null) {
            mDBAdapter = new DBAdapter1(ctx, false);
        }
        return mDBAdapter;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Logger.w("SPIN", "onCreate:");
                Log.d("VISIRX_DATABASE", "onCreate");
                db.execSQL(CustomersTable.TABLE_CUSTOMERS_CREATE);
                Log.d("VISIRX_DATABASE", "TABLE_CUSTOMERS_CREATE");
                db.execSQL(UsersRegTable.TABLE_REG_USERS_CREATE);
                db.execSQL(UsersTable.TABLE_USERS_CREATE);
                Log.d("VISIRX_DATABASE", "TABLE_REG_USERS_CREATE");
                db.execSQL(AppointmentsTable.TABLE_APPTS_CREATE);
                Log.d("VISIRX_DATABASE", "TABLE_APPTS_CREATE");
                db.execSQL(AppointmentNotesTable.TABLE_APPTS_NOTES_CREATE);
                Log.d("VISIRX_DATABASE", "onCreate");
                db.execSQL(ApptPrescriptionTable.TABLE_APPTS_PRESC_CREATE);
                Log.d("VISIRX_DATABASE", "TABLE_APPTS_PRESC_CREATE");
//				db.execSQL(ApptPrescriptionTable.PRESCRIPTION_IMAGE);
                db.execSQL(EMRFilesTable.TABLE_APPTS_EMR_CREATE);
                Log.d("VISIRX_DATABASE", "TABLE_APPTS_EMR_CREATE");
                db.execSQL(EMRVitalsTable.TABLE_APPTS_EMR_CREATE);
                Log.d("VISIRX_DATABASE", "TABLE_APPTS_EMR_CREATE - vitals table");

// DigitalPrescriptionTable
                db.execSQL(DigitalPrescriptionMainTable.TABLE_MAINDIGITAL_PRESCRIPTION_CREATE);
                Log.d("VISIRX_DATABASE", "TABLE_MAINDIGITAL_PRESCRIPTION_CREATE");

                db.execSQL(DigitalPrescriptionTable.TABLE_DIGITAL_PRESCRIPRION_CREATE);
                Log.d("VISIRX_DATABASE", "TABLE_DIGITAL_PRESCRIPRION_CREATE");
                //rony Dashboard GC - starts
//                db.execSQL(CustomersTable.TABLE_CUSTOMERS_CREATE);
                Log.d("VISIRX_DATABASE", "TABLE_CUSTOMERS_CREATE");
                //rony Dashboard GC - Ends
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                //TODO Do something that based on the requirement( alter table)
                Log.d("SPIN", "Database exists - upgrade old version :" + oldVersion + " with version :" + newVersion);
                Log.d("SPIN", "Database exists - upgrade old version :" + oldVersion + " with version :" + newVersion);
//                db.execSQL("ALTER TABLE " + ApptPrescriptionTable.TABLE_APPOINMENTS_PRESC + " ADD COLUMN " +
//                        ApptPrescriptionTable.SYMPTOMS + " text ");
//
                int upgradeTo = oldVersion + 1;

                Log.d("SPIN", "_Before while" + upgradeTo);
                while (upgradeTo <= newVersion) {
                    Log.d("SPIN", "Before switch" + upgradeTo);


                    switch (upgradeTo) {
                        case 2:

                            Log.d("SPIN", "switch_upgradeTo" + upgradeTo);
                            db.execSQL("ALTER TABLE " + CustomersTable.TABLE_CUSTOMERS + " ADD COLUMN " +
                                    CustomersTable.IS_DELETED + " text ");
                            break;
                        case 3:
                            Log.d("SPIN", "switch_upgradeTo : " + upgradeTo);
                            db.execSQL("DROP TABLE customers;");
                            db.execSQL(CustomersTable.TABLE_CUSTOMERS_CREATE);
                            Log.d("SPIN", "Completed the upgrade to 3 process");
                            break;
                        case 4:
                            Log.d("SPIN", "Completed the upgrade to 4 process Start ");

//                            db.execSQL("ALTER TABLE " + ApptPrescriptionTable.TABLE_APPOINMENTS_PRESC + " ADD COLUMN " + ApptPrescriptionTable.CREATED_AT_SERVER + " text ");
//                            db.execSQL("ALTER TABLE " + ApptPrescriptionTable.TABLE_APPOINMENTS_PRESC + " ADD COLUMN " + ApptPrescriptionTable.FILE_TYPE + " text ");
//                            db.execSQL("ALTER TABLE " + ApptPrescriptionTable.TABLE_APPOINMENTS_PRESC + " ADD COLUMN " + ApptPrescriptionTable.FILE_MIME_TYPE + " text ");
//                            db.execSQL("ALTER TABLE " + ApptPrescriptionTable.TABLE_APPOINMENTS_PRESC + " ADD COLUMN " + ApptPrescriptionTable.FILE_GROUP + " text ");
//                            db.execSQL("ALTER TABLE " + ApptPrescriptionTable.TABLE_APPOINMENTS_PRESC + " ADD COLUMN " + ApptPrescriptionTable.FILE_LABEL + " text ");

                            db.execSQL("DROP TABLE appoinments_prescription");
                            db.execSQL(ApptPrescriptionTable.TABLE_APPTS_PRESC_CREATE);
                            db.execSQL("ALTER TABLE " + AppointmentNotesTable.TABLE_APPOINMENTS_NOTES + " ADD COLUMN " + AppointmentNotesTable.CREATED_AT_SERVER + " text ");
                            db.execSQL("ALTER TABLE " + EMRFilesTable.TABLE_APPOINMENTS_EMR + " ADD COLUMN " + EMRFilesTable.CREATED_AT_SERVER + " text ");
                            Log.d("SPIN", "Completed the upgrade to 4 process end ");
                            break;


                        case 5:
                            Log.d("SPIN", " upgrade to 5 process START ");
                            db.execSQL(DigitalPrescriptionMainTable.TABLE_MAINDIGITAL_PRESCRIPTION_CREATE);
                            db.execSQL(DigitalPrescriptionTable.TABLE_DIGITAL_PRESCRIPRION_CREATE);
                            Log.d("SPIN", "upgrade to 5 process END ");
                            break;
                    }
                    upgradeTo++;
                }

            } catch (SQLException e) {
                Log.d("SPIN", "inside onupgrade : caught SQLException." + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                Log.d("SPIN", "Caught exception while upgrading sqlite db.");
                e.printStackTrace();
            }
        }
    }

    public void createDatabase() {
        try {
            if (checkDataBase()) {
                Logger.w("SPIN", "Database exists - upgrade old version :" + DATABASE_PREVIOUS_VERSION + " with version :" + DATABASE_VERSION);
                Log.d("SPIN", "Database exists - upgrade old version :" + DATABASE_PREVIOUS_VERSION + " with version :" + DATABASE_VERSION);
//                Logger.w("DBADAPTER", "createDatabase" + DATABASE_PREVIOUS_VERSION + "" + DATABASE_VERSION);
                DBHelper.onUpgrade(db, DATABASE_PREVIOUS_VERSION, DATABASE_VERSION);

            } else {
                DBHelper.onCreate(db);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null ? true : false;
    }

    public void upgradeDatabase() {
        //DBHelper.onUpgrade(db,DATABASE_VERSION, 2);
    }

    public DBAdapter1 open() throws SQLException {
        try {
            db = DBHelper.getWritableDatabase();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    public void close() {
        try {
            DBHelper.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SQLiteDatabase getDataBase() throws SQLException {
        return db;
    }

    public void exportDB(Context context) {
        try {
            String versionName = getVersionNumber(context);
            if (versionName.equals("")) {
                versionName = VTConstants.protocol_version;
            }
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.visirx.patient"
                        + "//databases//" + DATABASE_NAME;
                String backupDBPath = "VisiRxDB-" + versionName + ".backup";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getVersionNumber(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionName;
    }
}
