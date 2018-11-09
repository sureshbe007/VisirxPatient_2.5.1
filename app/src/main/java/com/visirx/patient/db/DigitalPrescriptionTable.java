package com.visirx.patient.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.DigitalprescriptionDataModel;

import java.util.ArrayList;

/**
 * Created by Lenovo on 6/29/2016.
 */
public class DigitalPrescriptionTable {


    private static final String TAG = DigitalPrescriptionMainTable.class.getName();
    private SQLiteDatabase db;

    public DigitalPrescriptionTable(SQLiteDatabase mdb) {
        super();
        db = mdb;
    }

    public static final String APPOINTMENT_ID = "appt_id";
    public static final String PATIENT_ID = "patient_id";
    public static final String CREATED_AT_MAINTABLE = "created_at_maintable";
    public static final String DRUG_NAME = "drug_name";
    public static final String DOSAGE = "dosage";
    public static final String FOODTIME = "foodtime";
    public static final String MORNING = "morning";
    public static final String NOON = "noon";
    public static final String EVENING = "evening";
    public static final String NIGHT = "night";

    public static final String CREATED_AT = "created_at";

    public static final String DURATION = "duration";
    public static final String NOTES = "notes";


    public static final String TABLE_DIGITAL_PRESCRIPTION = "table_digital_prescription";

    //create table
    static String PRIMARYKEY = PATIENT_ID + "," + APPOINTMENT_ID + "," + CREATED_AT_MAINTABLE + "," + CREATED_AT;
    public static final String TABLE_DIGITAL_PRESCRIPRION_CREATE = " create table IF NOT EXISTS " +
            TABLE_DIGITAL_PRESCRIPTION + "(" +
            PATIENT_ID + " text not null, " +
            APPOINTMENT_ID + " integer not null, " +
            CREATED_AT_MAINTABLE + " text not null, " +
            DRUG_NAME + " text , " +
            DOSAGE + " text , " +
            FOODTIME + " text , " +
            MORNING + " integer DEFAULT 0, " +
            NOON + " integer DEFAULT 0, " +
            EVENING + " integer DEFAULT 0, " +
            NIGHT + " integer DEFAULT 0, " +
            CREATED_AT + " text , " +
            NOTES + " text , " +
            DURATION + " text , " +
            "primary key(" + PRIMARYKEY + ")" + ");";


    public boolean insertPrescription(DigitalprescriptionDataModel model, String patientID, String AppointmentID, String createdAtMainTable) {
        long flag = 0;
        boolean isUpdated = false;
        String prescImgPath = null;
        try {
            Log.d("DigitalPrescription", "insertPrescription ()  :");
            Log.d("DigitalPrescription", "  insertPrescription   patientid          :" + patientID);
            Log.d("DigitalPrescription", " insertPrescription     AppointmentID      :" + AppointmentID);
            Log.d("DigitalPrescription", " insertPrescription     createdAtMainTable    :" + createdAtMainTable);

            ContentValues initialValues = gerContentValues(model, patientID, AppointmentID, createdAtMainTable);
//            initialValues.put(PROCESSED_FLAG,pROCESSED_FLAG);
            Cursor mCursor = db.query(TABLE_DIGITAL_PRESCRIPTION, new String[]{
                            PATIENT_ID, APPOINTMENT_ID},
                    PATIENT_ID + "=? " +
                            " AND " + APPOINTMENT_ID + "=?" +
                            " AND " + CREATED_AT_MAINTABLE + "=?" + " AND " + CREATED_AT + "=?",
                    new String[]{patientID,
                            AppointmentID,
                            createdAtMainTable,model.getCreatedAt()
                    },
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }

                Log.d("DigitalMainTable", "  db.insert:");


                flag = db.insert(TABLE_DIGITAL_PRESCRIPTION, null, initialValues);
                Log.d("PriscriptionTable", "mCursor.getCount()  IF  " + mCursor.getCount());
                Log.d("PriscriptionTable", "   flag  status  :" + flag);
                isUpdated = true;


            } else {

                Log.d("PriscriptionTable", " db.update :");
                mCursor.moveToFirst();
//                for (int counter = 0; counter < mCursor.getCount(); counter++) {
//                    prescImgPath = mCursor.getString(0);
//                    mCursor.moveToNext();
//                }
                flag = db.update(TABLE_DIGITAL_PRESCRIPTION, initialValues,
                        PATIENT_ID + "=? " +
                                " AND " + APPOINTMENT_ID + "=?" +
                                " AND " + CREATED_AT_MAINTABLE + "=?" + " AND " + CREATED_AT + "=?",
                        new String[]{patientID, AppointmentID, createdAtMainTable,model.getCreatedAt()});

                Log.d("PriscriptionTable", " update status :" + flag);


                isUpdated = true;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts notes information");
            isUpdated = false;
        }

        return isUpdated;
    }

    private ContentValues gerContentValues(DigitalprescriptionDataModel model, String patientid, String AppointmentID, String createdAtclient) {
        ContentValues initialValues = new ContentValues();
        try {
//            Log.d("PriscriptionTable", "  gerContentValues  patientid          :" + patientid);
//            Log.d("PriscriptionTable", "  gerContentValues  AppointmentID      :" + AppointmentID);
//            Log.d("PriscriptionTable", "  gerContentValues    createdAtclient    :" + createdAtclient);
//            Log.d("PriscriptionTable", "  gerContentValues    Lastcreated()    :" + model.getCreatedAt());
            Log.d("PriscriptionTable", "   gerContentValues   getDrugName        :" + model.getDrugName());
            Log.d("PriscriptionTable", "  gerContentValues    getDosage          :" + model.getDosage());
            Log.d("PriscriptionTable", "  gerContentValues    getNoofTablets          :" + model.getDuration());
            Log.d("PriscriptionTable", "  gerContentValues    getNotes          :" + model.getNotes());
//            Log.d("PriscriptionTable", "  gerContentValues    FoodTimessss         :" + model.getFoodTime());
//            Log.d("PriscriptionTable", " gerContentValues     getMorning         :" + model.getMorning());
//            Log.d("PriscriptionTable", " gerContentValues     getNoon            :" + model.getNoon());
//            Log.d("PriscriptionTable", " gerContentValues      getEvening         :" + model.getEvening());
//            Log.d("PriscriptionTable", "  gerContentValues     getNight           :" + model.getNight());

            initialValues.put(APPOINTMENT_ID, AppointmentID);
            initialValues.put(PATIENT_ID, patientid);
            initialValues.put(CREATED_AT_MAINTABLE, createdAtclient);
            initialValues.put(CREATED_AT, model.getCreatedAt());
            initialValues.put(DRUG_NAME, model.getDrugName());
            initialValues.put(DOSAGE, model.getDosage());
            initialValues.put(FOODTIME, model.getMedicineIntake());
            initialValues.put(MORNING, model.getMorning());
            initialValues.put(NOON, model.getNoon());
            initialValues.put(EVENING, model.getEvening());
            initialValues.put(NIGHT, model.getNight());
            initialValues.put(DURATION, model.getDuration());
            initialValues.put(NOTES, model.getNotes());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }



    public ArrayList<DigitalprescriptionDataModel> getPriscriptionList(String patientId, String appointmentId, String createdAtclient) {
        ArrayList<DigitalprescriptionDataModel> List = new ArrayList<DigitalprescriptionDataModel>();
        try {
            Log.d("PriscriptionTable", "  getPriscriptionList  patientid          :" + patientId);
            Log.d("PriscriptionTable", "  getPriscriptionList  AppointmentID      :" + appointmentId);
            Log.d("PriscriptionTable", "  getPriscriptionList    createdAtclient    :" + createdAtclient);

            Cursor cursor = db.query(TABLE_DIGITAL_PRESCRIPTION, new String[]{
                            DRUG_NAME, DOSAGE,DURATION,NOTES, FOODTIME
                            , MORNING, NOON, EVENING
                            , NIGHT, CREATED_AT},
                    PATIENT_ID + "=? " + " AND " + APPOINTMENT_ID + "=?" + " AND " + CREATED_AT_MAINTABLE + "=?",
                    new String[]{patientId, appointmentId, createdAtclient},
                    null,
                    null,
                    null,
                    null);
            if (cursor == null || cursor.getCount() <= 0) {

                if (cursor != null)
                    cursor.close();
                Log.d("DigitalMainTable", "  getPriscriptionList  Curosr Count IF  :" + cursor.getCount());
                return List;
            }
            cursor.moveToFirst();
            for (int counter = 0; counter < cursor.getCount(); counter++) {

                DigitalprescriptionDataModel digitalprescriptionDataModel = new DigitalprescriptionDataModel();
                digitalprescriptionDataModel.setDrugName(cursor.getString(cursor.getColumnIndex(DRUG_NAME)));
                digitalprescriptionDataModel.setDosage(cursor.getString(cursor.getColumnIndex(DOSAGE)));
                digitalprescriptionDataModel.setDuration(cursor.getString(cursor.getColumnIndex(DURATION)));
                digitalprescriptionDataModel.setNotes(cursor.getString(cursor.getColumnIndex(NOTES)));
                digitalprescriptionDataModel.setMedicineIntake(cursor.getString(cursor.getColumnIndex(FOODTIME)));
                digitalprescriptionDataModel.setMorning(cursor.getInt(cursor.getColumnIndex(MORNING)));
                digitalprescriptionDataModel.setNoon(cursor.getInt(cursor.getColumnIndex(NOON)));
                digitalprescriptionDataModel.setEvening(cursor.getInt(cursor.getColumnIndex(EVENING)));
                digitalprescriptionDataModel.setNight(cursor.getInt(cursor.getColumnIndex(NIGHT)));
                digitalprescriptionDataModel.setCreatedAt(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                List.add(digitalprescriptionDataModel);
                Log.d("PriscriptionTable", "  getPriscriptionList  MORNING          :" + cursor.getString(cursor.getColumnIndex(MORNING)));
                Log.d("PriscriptionTable", "  getPriscriptionList  NOON          :" + cursor.getString(cursor.getColumnIndex(NOON)));
                Log.d("PriscriptionTable", "  getPriscriptionList  EVENING          :" + cursor.getString(cursor.getColumnIndex(EVENING)));
                Log.d("PriscriptionTable", "  getPriscriptionList  NIGHT          :" + cursor.getString(cursor.getColumnIndex(NIGHT)));
                Log.d("PriscriptionTable", "  getPriscriptionList  LAST_CREATED          :" + cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "getPriscriptionList  Error ");
            Log.d("DigitalMainTable", "  getPriscriptionList    END  :");
        }
        return List;
    }


    public ArrayList<DigitalprescriptionDataModel> getDigitalPrescriptionData(int appointmentId, String patientId, String createdAtclient) {

        ArrayList<DigitalprescriptionDataModel> subModellList = new ArrayList<DigitalprescriptionDataModel>();

        Log.d("DigitalPrescription", " SUB TABLE getDigitalPrescriptionDate        patientID   :" + appointmentId);
        Log.d("DigitalPrescription", " SUB TABLE getDigitalPrescriptionDate        patientID   :" + patientId);


        Cursor mCursor = db.query(TABLE_DIGITAL_PRESCRIPTION, new String[]{
                        CREATED_AT, DRUG_NAME, FOODTIME, DOSAGE, DURATION, NOTES, MORNING, NOON, EVENING, NIGHT,
                },
                PATIENT_ID + "=? " +
                        " AND " + APPOINTMENT_ID + "=?" +
                        " AND " + CREATED_AT_MAINTABLE + "=?",
                new String[]{patientId, Integer.toString(appointmentId), createdAtclient},
                null,
                null,
                null,
                null);

        if (mCursor == null || mCursor.getCount() <= 0) {
            if (mCursor != null) {
                mCursor.close();
            }
            Log.d("DigitalMainTable", "mCursor.getCount()  IF  " + mCursor.getCount());

        } else {
            Log.d("DigitalMainTable", "mCursor.getCount()  ELSE  " + mCursor.getCount());
            mCursor.moveToFirst();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {

                DigitalprescriptionDataModel appDigitalprescriptionModel = new DigitalprescriptionDataModel();
                appDigitalprescriptionModel.setDrugName(mCursor.getString(mCursor.getColumnIndex(DRUG_NAME)));
                appDigitalprescriptionModel.setMedicineIntake(mCursor.getString(mCursor.getColumnIndex(FOODTIME)));
                appDigitalprescriptionModel.setDosage(mCursor.getString(mCursor.getColumnIndex(DOSAGE)));
                appDigitalprescriptionModel.setNotes(mCursor.getString(mCursor.getColumnIndex(NOTES)));
                appDigitalprescriptionModel.setMorning(mCursor.getInt(mCursor.getColumnIndex(MORNING)));
                appDigitalprescriptionModel.setEvening(mCursor.getInt(mCursor.getColumnIndex(EVENING)));
                appDigitalprescriptionModel.setNoon(mCursor.getInt(mCursor.getColumnIndex(NOON)));
                appDigitalprescriptionModel.setNight(mCursor.getInt(mCursor.getColumnIndex(NIGHT)));
                appDigitalprescriptionModel.setCreatedAt(mCursor.getString(mCursor.getColumnIndex(CREATED_AT)));
                appDigitalprescriptionModel.setDuration(mCursor.getString(mCursor.getColumnIndex(DURATION)));
                appDigitalprescriptionModel.setNotes(mCursor.getString(mCursor.getColumnIndex(NOTES)));

                subModellList.add(appDigitalprescriptionModel);

                mCursor.moveToNext();
            }
            mCursor.close();
        }
        Log.d("DigitalPrescription", " SUB TABLE getDigitalPrescriptionDate  " + subModellList.size());

        return subModellList;

    }


    public void deleteSingleMedicineFromTable(String patientId, String appointmentid, String cratedatclient, String lastcreated) {
        try {
            boolean fag = false;
            Log.d("PriscriptionTable", "  deletePrescriptionFromTable    patientId      :" + patientId);
            Log.d("PriscriptionTable", "  deletePrescriptionFromTable    appointmentid  :" + appointmentid);
            Log.d("PriscriptionTable", " deletePrescriptionFromTable     cratedatclient  :" + cratedatclient);
            Log.d("PriscriptionTable", " deletePrescriptionFromTable     lastcreated    :" + lastcreated);
            int value = db.delete(TABLE_DIGITAL_PRESCRIPTION, PATIENT_ID + "=? " + " AND " + APPOINTMENT_ID + "=?" + " AND " + CREATED_AT_MAINTABLE + "=?" + " AND " + CREATED_AT + "=?", new String[]{patientId, appointmentid, cratedatclient, lastcreated});
            Log.d("PriscriptionTable", " DELETE:" + value);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void deletePrescriptionFromTable(String patientId, String appointmentid, String cratedatclient) {
        try {
            boolean fag = false;
            Log.d("PriscriptionTable", "  deletePrescriptionFromTable    patientId      :" + patientId);
            Log.d("PriscriptionTable", "  deletePrescriptionFromTable    appointmentid  :" + appointmentid);
            Log.d("PriscriptionTable", " deletePrescriptionFromTable     cratedatclient  :" + cratedatclient);
            int value = db.delete(TABLE_DIGITAL_PRESCRIPTION, PATIENT_ID + "=? " + " AND " + APPOINTMENT_ID + "=?" + " AND " + CREATED_AT_MAINTABLE + "=?", new String[]{patientId, appointmentid, cratedatclient});
            Log.d("NEWALERCHECK", " DELETE:" + value);

            Log.d("PriscriptionTable", " DELETE:" + value);
        } catch (Exception e) {
            e.getMessage();
        }
    }


}
