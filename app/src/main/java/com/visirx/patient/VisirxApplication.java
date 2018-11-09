package com.visirx.patient;

import android.app.Application;
import android.content.Context;
import android.database.SQLException;
import com.crashlytics.android.Crashlytics;
import com.quickblox.core.QBSettings;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.db.AppointmentNotesTable;
import com.visirx.patient.db.AppointmentsTable;
import com.visirx.patient.db.ApptPrescriptionTable;
import com.visirx.patient.db.CustomersTable;
import com.visirx.patient.db.DBAdapter1;
import com.visirx.patient.db.DigitalPrescriptionMainTable;
import com.visirx.patient.db.DigitalPrescriptionTable;
import com.visirx.patient.db.EMRFilesTable;
import com.visirx.patient.db.EMRVitalsTable;
import com.visirx.patient.db.UsersRegTable;
import com.visirx.patient.db.UsersTable;
import com.visirx.patient.visirxav.definitions.Consts;

import io.fabric.sdk.android.Fabric;


/**
 * Created by aa on 18.1.16.
 */
public class VisirxApplication extends Application {
    private static DBAdapter1 dbHelper;
    public static AppContext appContext;
    static String TAG = VisirxApplication.class.getName();
    static Context context = null;
    public static UsersRegTable userRegisterDAO;
    public static UsersTable userDAO;
    public static AppointmentsTable aptDAO;
    public static AppointmentNotesTable aptNotesDAO;
    public static ApptPrescriptionTable aptPresDAO;
    public static EMRFilesTable aptEmrDAO;
    public static EMRVitalsTable aptVitalDAO;
    //rony Dashboard GC - starts
    public static CustomersTable customerDAO;

    // for Digital Prescription
    public static DigitalPrescriptionMainTable digitalMainTableDAO;
    public static DigitalPrescriptionTable digitaTableDAO;
    //rony Dashboard GC - ends
    // uncaught exception handler variable
    private Thread.UncaughtExceptionHandler defaultUEH;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
//        Fabric.with(this, new Crashlytics());
//        Fabric.with(this, new Crashlytics());
//		Fabric.with(this, new Crashlytics());
        try {

            QBSettings.getInstance().init(this, Consts.APP_ID, Consts.AUTH_KEY, Consts.AUTH_SECRET);
            QBSettings.getInstance().setAccountKey(Consts.ACCOUNT_KEY);

            defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
            // setup handler for uncaught exception
            Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
            context = this;
            SetupDatabaseIfNeeded();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
        runBackUP();
        ProcessStartup();
    }

    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    // here I do logging of exception to a db
                    LogTrace.e(TAG, "Uncaught exception is: " + ex.getMessage());
                    // re-throw critical exception further to the os (important)
                    defaultUEH.uncaughtException(thread, ex);
                }
            };

    public static void runBackUP() {
        try {
//            BackupDB();
        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private static void BackupDB() {
        if (dbHelper != null) {
            try {
                dbHelper.exportDB(context);
            }
            catch (Exception e) {
                LogTrace.e(TAG, e.getMessage());
            }
        }
        else {
            try {
                dbHelper = DBAdapter1.getInstance(context);
                dbHelper.open();
                dbHelper.createDatabase();
                dbHelper.exportDB(context);
            } catch (Exception e) {
                LogTrace.e(TAG, e.getMessage());
            }
        }

    }

    private static void SetupDatabaseIfNeeded() throws Exception {
        if (dbHelper == null) {
            try {
                dbHelper = DBAdapter1.getInstance(context);
                dbHelper.open();
                // dbHelper.createDatabase();
                //dbHelper.exportDB(context);
            } catch (Exception e) {
                LogTrace.e(TAG, e.getMessage());
                throw e;
            }
        }
    }

    public static void ProcessStartup() {
        try {
            try {
                SetupDatabaseIfNeeded();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
            userRegisterDAO = new UsersRegTable(dbHelper.getDataBase());
            customerDAO = new CustomersTable(dbHelper.getDataBase(), context);
            userDAO = new UsersTable(dbHelper.getDataBase());
            aptDAO = new AppointmentsTable(dbHelper.getDataBase(), context);
            aptNotesDAO = new AppointmentNotesTable(dbHelper.getDataBase());
            aptPresDAO = new ApptPrescriptionTable(dbHelper.getDataBase());
            aptEmrDAO = new EMRFilesTable(dbHelper.getDataBase());
            aptVitalDAO = new EMRVitalsTable(dbHelper.getDataBase());

            digitalMainTableDAO = new DigitalPrescriptionMainTable(dbHelper.getDataBase(),context);
            digitaTableDAO = new DigitalPrescriptionTable(dbHelper.getDataBase());
            //rony Dashboard GC - starts
            //rony Dashboard GC - Ends
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    public static void InitialiseApplication(AppContext acontext, Context context2) {
        try {
            appContext = acontext;
            if (context == null)
                context = context2;
            SetupDatabaseIfNeeded();
            ProcessStartup();
        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }
}
