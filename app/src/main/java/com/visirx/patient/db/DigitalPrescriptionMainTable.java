package com.visirx.patient.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AppDigitalprescriptionModel;
import com.visirx.patient.model.DigitalprescriptionDataModel;
import com.visirx.patient.utils.VTConstants;

import java.util.ArrayList;

/**
 * Created by Lenovo on 6/29/2016.
 */
public class DigitalPrescriptionMainTable {

    private Context context;

    private static final String TAG = DigitalPrescriptionMainTable.class.getName();
    private SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    String PatientID;
    public DigitalPrescriptionMainTable(SQLiteDatabase mdb, Context context) {
        super();
        db = mdb;
        this.context = context;

    }

    public static final String APPOINTMENT_ID = "appt_id";
    public static final String PATIENT_ID = "patient_id";
    public static final String CREATED_AT_CLIENT = "created_at_client";
    public static final String CREATED_BY = "created_by";
    public static final String IS_DELETED = "is_deleted";
    public static final String CREATED_AT_SERVER = "created_at_server";
    public static final String PRESCRIPTION_ID = "prescription_id";
    public static final String PROCESSED_FLAG = "processed_flag";

    public static final String TABLE_MAINDIGITAL_PRESCRIPTIONMAIN = "table_maindigital_prescriptionmain";

    static String PRIMARYKEY = PATIENT_ID + "," + APPOINTMENT_ID + "," + CREATED_AT_CLIENT;
    public static final String TABLE_MAINDIGITAL_PRESCRIPTION_CREATE = " create table IF NOT EXISTS " +
            TABLE_MAINDIGITAL_PRESCRIPTIONMAIN + "(" +
            PATIENT_ID + " text not null, " +
            APPOINTMENT_ID + " integer not null, " +
            CREATED_AT_CLIENT + " text not null, " +
            CREATED_BY + " text , " +
            IS_DELETED + " text , " +
            CREATED_AT_SERVER + " text , " +
            PROCESSED_FLAG + " integer, " +
            PRESCRIPTION_ID + " integer , " +
            "primary key(" + PRIMARYKEY + ")" + ");";


    public boolean insertPrescription(AppDigitalprescriptionModel model) {
        long flag = 0;
        boolean isUpdated = false;
        try {
            Log.d("DigitalMainTable", "insertPrescription    START :");
            ContentValues initialValues = gerContentValues(model);
            Cursor mCursor = db.query(TABLE_MAINDIGITAL_PRESCRIPTIONMAIN, new String[]{
                            PATIENT_ID, APPOINTMENT_ID},
                    PATIENT_ID + "=? " +
                            " AND " + APPOINTMENT_ID + "=?" +
                            " AND " + CREATED_AT_CLIENT + "=?",
                    new String[]{model.getPatientId(),
                            Integer.toString(model.getAppointmentId()),
                            model.getCreatedAtclient()},
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }

                flag = db.insert(TABLE_MAINDIGITAL_PRESCRIPTIONMAIN, null, initialValues);
                Log.d("DigitalMainTable", " MainTable insert Query status  :" + flag);
                isUpdated = true;

            } else {

                flag = db.update(TABLE_MAINDIGITAL_PRESCRIPTIONMAIN, initialValues,
                        PATIENT_ID + "=? " +
                                " AND " + APPOINTMENT_ID + "=?" +
                                " AND " + CREATED_AT_CLIENT + "=?",
                        new String[]{model.getPatientId(), Integer.toString(model.getAppointmentId()), model.getCreatedAtclient()});

                Log.d("DigitalMainTable", " update status :" + flag);
                isUpdated = true;

                LogTrace.w(TAG, flag + "  Update PrescriptionTable : " + model.getPatientId());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts notes information");
            isUpdated = false;
        }
        return isUpdated;
    }


    private ContentValues gerContentValues(AppDigitalprescriptionModel model) {
        ContentValues initialValues = new ContentValues();
        try {
            Log.d("DigitalMainTable", "gerContentValues   START :");

            Log.d("DigitalMainTable", "CREATED_AT_CLIENT  before    :" + model.getCreatedAtclient());
            Log.d("DigitalMainTable", "PATIENT_ID    before    :" + model.getPatientId());
            Log.d("DigitalMainTable", "APPOINTMENT_ID  before    :" + model.getAppointmentId());
            Log.d("DigitalMainTable", "PROCESSED_FLAG    :" + model.getProcessFlag());
            Log.d("DigitalMainTable", "PRESCRIPTION_ID    :" + model.getPrescriptionId());
            Log.d("DigitalMainTable", "CREATED_AT_SERVER    :" + model.getCreatedAtServer());
            Log.d("DigitalMainTable", "PROCESS FLAG    :" + model.getProcessFlag());

            sharedPreferences = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            PatientID = sharedPreferences.getString(VTConstants.USER_ID, null);
            initialValues.put(CREATED_AT_CLIENT, model.getCreatedAtclient());
            initialValues.put(PATIENT_ID, model.getPatientId());
            initialValues.put(APPOINTMENT_ID, model.getAppointmentId());
            initialValues.put(CREATED_BY, PatientID);
            initialValues.put(PROCESSED_FLAG, model.getProcessFlag());
            initialValues.put(PRESCRIPTION_ID, model.getPrescriptionId());
            initialValues.put(CREATED_AT_SERVER, model.getCreatedAtServer());
            initialValues.put(IS_DELETED, model.getIsDeleted());





        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("DigitalMainTable", "gerContentValues   END :");
        return initialValues;
    }


    public ArrayList<String> getMaxDate(String customerId, int reservationNumber) {
        Log.d("DigitalMainTable", "getMaxDate START :");

        String maxDate = "";
        ArrayList<String> arrayList = new ArrayList<String>();
        try {

            Log.d("DigitalMainTable", "getMaxDate   customerId     :" + customerId);
            Log.d("DigitalMainTable", " getMaxDate   reservationNumber   :" + reservationNumber);

            Cursor mCursor = db.query(TABLE_MAINDIGITAL_PRESCRIPTIONMAIN, new String[]{CREATED_AT_SERVER},
                    PATIENT_ID + " = ?" + " AND " + APPOINTMENT_ID + " =?" + " AND " + PROCESSED_FLAG + " =?",
                    new String[]{String.valueOf(customerId), String.valueOf(reservationNumber), String.valueOf(VTConstants.PROCESSED_FLAG_SENT)},
                    null, null, null,
                    null);

//            db.query(TABLE_MAINDIGITAL_PRESCRIPTIONMAIN, new String [] {"MAX(created_at_server)"}, null, null, null, null, null);

            if (mCursor != null)

                if (mCursor.moveToFirst()) {
                    do {
                        maxDate = mCursor.getString(mCursor.getColumnIndex(CREATED_AT_SERVER));
                    } while (mCursor.moveToNext());
                }
            arrayList.add(maxDate);
            if (mCursor.getCount() == 0) {
                arrayList.clear();
            }
            mCursor.close();


            return arrayList;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SPIN", "Inside getMaxDate : Caught Exception.");
        }


        return arrayList;
    }


    public ArrayList<AppDigitalprescriptionModel> getTotalDigitalPrescription(String patientID, int appointmentID) {
        ArrayList<AppDigitalprescriptionModel> List = new ArrayList<AppDigitalprescriptionModel>();
        Log.d("DigitalMainTable", " getTotalDigitalPrescription        patientID   :" + patientID);
        Log.d("DigitalMainTable  : ", "  getTotalDigitalPrescription   appointmentID :" + appointmentID);


        Cursor mCursor = db.query(TABLE_MAINDIGITAL_PRESCRIPTIONMAIN, new String[]{
                        CREATED_AT_CLIENT, PROCESSED_FLAG
                }, PATIENT_ID + " =? AND " + APPOINTMENT_ID + "=? AND "+IS_DELETED +" =?",
                new String[]{patientID, Integer.toString(appointmentID),VTConstants.IS_NOT_DELETED},
                null, null, null);
        if (mCursor == null || mCursor.getCount() <= 0) {
            if (mCursor != null) {
                mCursor.close();
            }
            Log.d("DigitalMainTable", "mCursor.getCount()  IF  " + mCursor.getCount());

        } else {
            Log.d("DigitalMainTable", "mCursor.getCount()  ELSE  " + mCursor.getCount());
            mCursor.moveToFirst();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {

                AppDigitalprescriptionModel appDigitalprescriptionModel = new AppDigitalprescriptionModel();

                appDigitalprescriptionModel.setPatientId(patientID);
                appDigitalprescriptionModel.setAppointmentId(appointmentID);
                appDigitalprescriptionModel.setCreatedAtclient(mCursor.getString(mCursor.getColumnIndex(CREATED_AT_CLIENT)));
                appDigitalprescriptionModel.setCreatedAtclient(mCursor.getString(mCursor.getColumnIndex(CREATED_AT_CLIENT)));
                appDigitalprescriptionModel.setProcessFlag(mCursor.getInt(mCursor.getColumnIndex(PROCESSED_FLAG)));
                ArrayList<DigitalprescriptionDataModel> dataModel = VisirxApplication.digitaTableDAO.getDigitalPrescriptionData(appointmentID, patientID, appDigitalprescriptionModel.getCreatedAtclient());
                appDigitalprescriptionModel.setDpDataModel(dataModel);
                List.add(appDigitalprescriptionModel);
                Log.d("DigitalMainTable", "  getTotalDigitalPrescription :" + mCursor.getString(mCursor.getColumnIndex(CREATED_AT_CLIENT)));
                Log.d("DigitalMainTable", "  appDigitalprescriptionModel.Size() :" + List.size());
                mCursor.moveToNext();
            }
            mCursor.close();
        }

        return List;
    }


    public void deletePrescriptionFromMainTable(String patientId, String appointmentid, String cratedatclient) {
        try {

            Log.d("DigitalMainTable", "  deletePrescriptionFromMainTable    patientId      :" + patientId);
            Log.d("DigitalMainTable", "  deletePrescriptionFromMainTable    appointmentid  :" + appointmentid);
            Log.d("DigitalMainTable", " deletePrescriptionFromMainTable     cratedatclient :" + cratedatclient);
            int value = db.delete(TABLE_MAINDIGITAL_PRESCRIPTIONMAIN, PATIENT_ID + "=? " + " AND " + APPOINTMENT_ID + "=?" + " AND " + CREATED_AT_CLIENT + "=?", new String[] {patientId,appointmentid,cratedatclient });
            Log.d("DigitalMainTable", " DELETE:" + value);
        }
        catch (Exception e) {
            e.getMessage();
        }
    }
}
