package com.visirx.patient.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AppointmentNoteModel;
import com.visirx.patient.utils.VTConstants;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AppointmentNoteModel;
import com.visirx.patient.utils.VTConstants;

import java.util.ArrayList;

/**
 * Created by aa on 21.1.16.
 */
public class AppointmentNotesTable {

    private static final String TAG = AppointmentNotesTable.class.getName();
    private SQLiteDatabase db;

    public AppointmentNotesTable(SQLiteDatabase mdb) {
        super();
        db = mdb;
    }

    //unit registration table
    public static final String PATIENT_ID = "patient_id";
    public static final String APPOINTMENT_ID = "appt_id";
    public static final String CREATED_AT = "created_at";
    public static final String CREATED_AT_SERVER = "created_at_server";
    public static final String CREATED_ID = "createdById";
    public static final String CREATED_NAME = "createdByName";
    public static final String NOTES = "notes";
    public static final String PATIENT_NAME = "patient_name";
    public static final String PROCESSED_FLAG = "processed_flag";

    public static final String TABLE_APPOINMENTS_NOTES = "appoinments_notes";
    static String PRIMARYKEY_REG = PATIENT_ID + "," + APPOINTMENT_ID + "," + CREATED_AT;

    public static final String TABLE_APPTS_NOTES_CREATE = " create table IF NOT EXISTS " +
            TABLE_APPOINMENTS_NOTES + "(" +
            PATIENT_ID + " text not null, " +
            CREATED_ID + " text not null, " +
            APPOINTMENT_ID + " integer not null, " +
            CREATED_AT + " text not null, " +
            CREATED_AT_SERVER + " text not null, " +
            CREATED_NAME + " text , " +
            NOTES + " text, " +
            PATIENT_NAME + " text, " +
            PROCESSED_FLAG + " integer, " +
            "primary key(" + PRIMARYKEY_REG + ")" + ");";


    public long insertAppointmentNotes(AppointmentNoteModel model, int processFlag) {
        long flag = 0;
        try {
            Log.d("CREATED_AT_SERVER_DB", "CREATED_AT_SERVER    :" + model.getCreatedAtServer());
            ContentValues initialValues = gerContentValues(model, processFlag);
            Cursor mCursor = db.query(TABLE_APPOINMENTS_NOTES, new String[]{
                            PATIENT_ID,
                            APPOINTMENT_ID},
                    PATIENT_ID + "=? " +
                            " AND " + APPOINTMENT_ID + "=?" +
                            " AND " + CREATED_AT + "=?",
                    new String[]{model.getPatientId(),
                            Integer.toString(model.getAppointmentId()),
                            model.getCreatedAt()},
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }


                initialValues.put(PATIENT_ID, model.getPatientId());
                initialValues.put(APPOINTMENT_ID, model.getAppointmentId());
                initialValues.put(CREATED_AT, model.getCreatedAt());
                initialValues.put(CREATED_AT_SERVER, model.getCreatedAtServer());

                flag = db.insert(TABLE_APPOINMENTS_NOTES, null, initialValues);
                LogTrace.w(TAG, flag + " Insert appts notes : " + model.getPatientId());
            } else {
                flag = db.update(TABLE_APPOINMENTS_NOTES, initialValues,
                        PATIENT_ID + "=? " +
                                " AND " + APPOINTMENT_ID + "=?" +
                                " AND " + CREATED_AT + "=?",
                        new String[]{model.getPatientId(), Integer.toString(model.getAppointmentId()), model.getCreatedAt()});
                LogTrace.w(TAG, flag + "  appts notes updated : " + model.getPatientId());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts notes information");
        }
        return flag;
    }

    public ArrayList<AppointmentNoteModel> GetNotes() {
        ArrayList<AppointmentNoteModel> modelList = new ArrayList<AppointmentNoteModel>();
        try {
            Cursor mCursor = db.query(TABLE_APPOINMENTS_NOTES, new String[]{
                            PATIENT_ID,
                            APPOINTMENT_ID,
                            CREATED_ID,
                            CREATED_AT,
                            CREATED_NAME,
                            NOTES,
                            PATIENT_NAME},
                    PROCESSED_FLAG + " = \"" + 0 + "\"",
                    null,
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
                AppointmentNoteModel model = new AppointmentNoteModel();
                model.setPatientId(mCursor.getString(0));
                model.setAppointmentId(mCursor.getInt(1));
                model.setCreatedById(mCursor.getString(2));
                model.setCreatedAt(mCursor.getString(3));
                model.setCreatedByName(mCursor.getString(4));
                model.setNotes(mCursor.getString(5));
                model.setPatientName(mCursor.getString(6));
                modelList.add(model);
                mCursor.moveToNext();
            }
            Log.d("GetNotes", "GetNotes count :" + mCursor.getCount());
            mCursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return modelList;
    }

    public ArrayList<AppointmentNoteModel> GetPatientNotes(String patientId, int appointmentid) {
        ArrayList<AppointmentNoteModel> modelList = new ArrayList<AppointmentNoteModel>();
        try {
            Cursor mCursor = db.query(TABLE_APPOINMENTS_NOTES, new String[]{
                            PATIENT_ID,
                            APPOINTMENT_ID,
                            CREATED_ID,
                            CREATED_AT,
                            CREATED_NAME,
                            NOTES,
                            PATIENT_NAME, PROCESSED_FLAG},
                    PATIENT_ID + " = \"" + patientId + "\"" + " AND " + APPOINTMENT_ID + " = \"" + String.valueOf(appointmentid) + "\"",
                    null,
                    null,
                    null,
                    CREATED_AT_SERVER + " ASC");
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return modelList;
            }
            mCursor.moveToFirst();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                AppointmentNoteModel model = new AppointmentNoteModel();
                model.setPatientId(mCursor.getString(0));
                model.setAppointmentId(mCursor.getInt(1));
                model.setCreatedById(mCursor.getString(2));
                model.setCreatedAt(mCursor.getString(3));
                model.setCreatedByName(mCursor.getString(4));
                model.setNotes(mCursor.getString(5));
                model.setPatientName(mCursor.getString(6));
                model.setProcessFlag(mCursor.getInt(7));
                modelList.add(model);
                Log.d("ANROIDCHECK", "CURSOR" + mCursor.getString(2));
                mCursor.moveToNext();
            }
            mCursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return modelList;
    }

    private ContentValues gerContentValues(AppointmentNoteModel model, int processFlag) {
        ContentValues initialValues = new ContentValues();
        try {
            Log.d("ANROIDCHECK", "" + model.getCreatedById());
            initialValues.put(CREATED_ID, model.getCreatedById());
            initialValues.put(CREATED_NAME, model.getCreatedByName());
            initialValues.put(NOTES, model.getNotes());
            initialValues.put(PROCESSED_FLAG, processFlag);
            initialValues.put(PATIENT_NAME, model.getPatientName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }

    public ArrayList<String> getMaxDate(String patientId, int appoinmentid) {
        ArrayList<String> arrayList = new ArrayList<String>();
        String maxDate = "";
        Cursor mCursor = db.query(TABLE_APPOINMENTS_NOTES, new String[]{CREATED_AT_SERVER},
                PATIENT_ID + " = ?" + " AND " + APPOINTMENT_ID + " =?" + " AND " + CREATED_ID + " !=?",
                new String[]{String.valueOf(patientId), String.valueOf(appoinmentid), String.valueOf(patientId)},
                null, null, null,
                null);
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

    }

    public void SetprocessFlagUpdate(String patientid,
                                     int appoinmentid,
                                     String createdAt,
                                     String createAtserver) {
        try {
            ContentValues values = new ContentValues();
            values.put(PROCESSED_FLAG, 1);
            values.put(CREATED_AT_SERVER, createAtserver);
            db.update(TABLE_APPOINMENTS_NOTES, values, PATIENT_ID + " = ?" + " AND " + APPOINTMENT_ID + " =?" + " AND " + CREATED_AT + " =?",
                    new String[]{String.valueOf(patientid), String.valueOf(appoinmentid), createdAt});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}