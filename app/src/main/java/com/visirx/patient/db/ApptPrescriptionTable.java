package com.visirx.patient.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.ApptPrescriptionModel;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.util.ArrayList;

public class ApptPrescriptionTable {
    private static final String TAG = ApptPrescriptionTable.class.getName();
    private SQLiteDatabase db;

    public ApptPrescriptionTable(SQLiteDatabase mdb) {
        super();
        db = mdb;
    }

    //rony prescription GC fix - starts

    //unit registration table
    public static final String PATIENT_ID = "patient_id";
    public static final String APPOINTMENT_ID = "appt_id";
    public static final String CREATED_AT = "created_at";
    public static final String CREATED_ID = "createdById";
    public static final String PROCESSED_FLAG = "processed_flag";
    public static final String IMAGE_THUMBNAIL_PATH = " imageThumbnailPath";
    public static final String FILE_TYPE = "file_type";
    public static final String FILE_MIME_TYPE = "filemime_type";
    public static final String FILE_GROUP = "file_group";
    public static final String FILE_LABEL = "file_label";
    public static final String CREATED_AT_SERVER = "created_at_server";
    //	public static final String PRESCRIPTION_IMAGE = "prescription_image";
    public static final String SYMPTOMS = "symtoms";
    public static final String PRESCRIPTION_IMAGEPATH = "prescription_imagepath";
    public static final String IS_DELETED = "is_deleted";

    //rony 1.3.4 starts
    public static final String IS_PHARMACY_ASSIGNED = "is_pharmacy_assigned"; // 0 - not assigned to any pharmacy || 1 - assigned to pharmacy
    public static final String PHARMACY_ID = "pharmacy_id";


    public static final String TABLE_APPOINMENTS_PRESC = "appoinments_prescription_new";
    //create table
    static String PRIMARYKEY_REG = PATIENT_ID + "," + APPOINTMENT_ID + "," + CREATED_AT;

    public static final String TABLE_APPTS_PRESC_CREATE = " create table IF NOT EXISTS " +
            TABLE_APPOINMENTS_PRESC + "(" +
            PATIENT_ID + " text not null, " +
            CREATED_ID + " text not null, " +
            APPOINTMENT_ID + " integer not null, " +
            CREATED_AT + " text not null, " +
            CREATED_AT_SERVER + " text not null, " +
            FILE_TYPE + " text not null, " +
            FILE_MIME_TYPE + " text not null, " +
            FILE_GROUP + " text , " +
            FILE_LABEL + " text , " +
            IMAGE_THUMBNAIL_PATH + " text, " +
            PRESCRIPTION_IMAGEPATH + "  text, " +
            IS_PHARMACY_ASSIGNED + "  integer DEFAULT 0 , " +
            PHARMACY_ID + "  text, " +
            SYMPTOMS + " text , " +
            PROCESSED_FLAG + " integer, " +
            IS_DELETED + " text , " +
            "primary key(" + PRIMARYKEY_REG + ")" + ");";


    //rony 1.3.4 ends

    public boolean insertAppointmentPrescription(ApptPrescriptionModel model, int pROCESSED_FLAG) {
        long flag = 0;
        boolean isPrescriptionFileUpate = false;
        String prescImgPath = null;
        try {
            ContentValues initialValues = gerContentValues(model);
            initialValues.put(PROCESSED_FLAG, pROCESSED_FLAG);
            Cursor mCursor = db.query(TABLE_APPOINMENTS_PRESC, new String[]{
                            PRESCRIPTION_IMAGEPATH},
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

                Log.d("SPIN", "Inside insertAppointmentPrescription : insert :");

                initialValues.put(PATIENT_ID, model.getPatientId());
                initialValues.put(APPOINTMENT_ID, model.getAppointmentId());
                initialValues.put(CREATED_AT, model.getCreatedAt());
                initialValues.put(IS_DELETED, VTConstants.IS_NOT_DELETED);
                initialValues.put(PRESCRIPTION_IMAGEPATH, model.getPreimageName());
                initialValues.put(IMAGE_THUMBNAIL_PATH, model.getPreThumbImageName());
                Log.d("SPIN", "Inside sendPrescription : 3 - insert full img path :" + model.getPreimageName());
                Log.d("SPIN", "Inside sendPrescription : 3 - insert thumb img path :" + model.getPreThumbImageName());


                flag = db.insert(TABLE_APPOINMENTS_PRESC, null, initialValues);

                Log.d("SPIN", "Inside insertAppointmentPrescription : insert status  :" + flag);
                Log.d("SPIN", "Inside sendPrescription : 3 - insert path :" + model.getPreimageName());

                LogTrace.w(TAG, flag + " Insert appts notes : " + model.getPatientId());

                isPrescriptionFileUpate = true;


            } else {

                Log.d("SPIN", "Inside insertAppointmentPrescription : update:");
                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    prescImgPath = mCursor.getString(0);
                    mCursor.moveToNext();
                }

                if ((prescImgPath != null && !VTConstants.CheckFileExists(prescImgPath)) || (prescImgPath == null)) {
                    isPrescriptionFileUpate = true;
                    Log.d("SPIN", "File is missing from directory.sync required.");
                } else {
                    Log.d("SPIN", "File found from directory.sync not required.");
                    isPrescriptionFileUpate = false;
                }

                flag = db.update(TABLE_APPOINMENTS_PRESC, initialValues,
                        PATIENT_ID + "=? " +
                                " AND " + APPOINTMENT_ID + "=?" +
                                " AND " + CREATED_AT + "=?",
                        new String[]{model.getPatientId(), Integer.toString(model.getAppointmentId()), model.getCreatedAt()});

                Log.d("SPIN", "Inside insertAppointmentPrescription : update status :" + flag);


                LogTrace.w(TAG, flag + "  appts notes updated : " + model.getPatientId());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts notes information");
        }
        return isPrescriptionFileUpate;
    }

    public long setProcessFlagAppointmentPrescription(String patientId, int appointmentId, String createdAt) {
        long flag = 0;

        try {

            ContentValues initialValues = new ContentValues();

            initialValues.put(PROCESSED_FLAG, VTConstants.PROCESSED_FLAG_SENT);

            flag = db.update(TABLE_APPOINMENTS_PRESC, initialValues,
                    PATIENT_ID + "=? " + " AND " + APPOINTMENT_ID + "=?" + " AND " + CREATED_AT + "=?",
                    new String[]{patientId, Integer.toString(appointmentId), createdAt});

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts notes information");
        }
        return flag;
    }


    public ArrayList<ApptPrescriptionModel> GetPrescription(int ReservationNumber) {
        ArrayList<ApptPrescriptionModel> modelList = new ArrayList<ApptPrescriptionModel>();
        try {
            Cursor mCursor = db.query(TABLE_APPOINMENTS_PRESC, new String[]{
                            PATIENT_ID,
                            APPOINTMENT_ID,
                            CREATED_ID,
                            CREATED_AT,
                            PRESCRIPTION_IMAGEPATH,
                            SYMPTOMS},
                    APPOINTMENT_ID + " = ?" + " AND " + PROCESSED_FLAG + " =?",
                    new String[]{Integer.toString(ReservationNumber), Integer.toString(VTConstants.PROCESSED_FLAG_NOT_SENT)},
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
                ApptPrescriptionModel model = new ApptPrescriptionModel();
                model.setPatientId(mCursor.getString(0));
                model.setAppointmentId(mCursor.getInt(1));
                model.setCreatedById(mCursor.getString(2));
                model.setCreatedAt(mCursor.getString(3));
                model.setPreimageName(mCursor.getString(4));
                model.setSymptoms(mCursor.getString(5));
                modelList.add(model);
                mCursor.moveToNext();
            }
            mCursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return modelList;
    }

    public ArrayList<ApptPrescriptionModel> GetPatientPrescription(String patientId, int appointmentid) {
        ArrayList<ApptPrescriptionModel> modelList = new ArrayList<ApptPrescriptionModel>();
        try {
            Cursor mCursor = db.query(TABLE_APPOINMENTS_PRESC, new String[]{
                            PATIENT_ID,
                            APPOINTMENT_ID,
                            CREATED_ID,
                            CREATED_AT,
                            PRESCRIPTION_IMAGEPATH,
                            IMAGE_THUMBNAIL_PATH,
                            SYMPTOMS, PROCESSED_FLAG,FILE_TYPE,FILE_MIME_TYPE,FILE_GROUP,FILE_LABEL},
                    PATIENT_ID + " = \"" + patientId + "\"" + " AND " + APPOINTMENT_ID + " = \"" + String.valueOf(appointmentid) + "\"",
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

            for (int counter = 0; counter < mCursor.getCount(); counter++)
            {
                ApptPrescriptionModel model = new ApptPrescriptionModel();


                model.setPatientId(mCursor.getString(0));
                model.setAppointmentId(mCursor.getInt(1));
                model.setCreatedById(mCursor.getString(2));
                model.setCreatedAt(mCursor.getString(3));
                model.setPreimageName(mCursor.getString(4));
                model.setPreThumbImageName(mCursor.getString(5));
                model.setSymptoms(mCursor.getString(6));
                model.setProcessFlag(mCursor.getInt((7)));
                model.setFileType(mCursor.getString((8)));
                model.setFileMimeType(mCursor.getString((9)));
                model.setFileGroup(mCursor.getString((10)));
                model.setFileLabel(mCursor.getString(11));
                modelList.add(model);

                Log.d("PRESCRIPTIONDECHECKURL","setFileType"+mCursor.getString((8)));
                Log.d("PRESCRIPTIONDECHECKURL","setFileMimeType"+mCursor.getString((9)));
                Log.d("PRESCRIPTIONDECHECKURL","setFileGroup"+mCursor.getString((10)));
                Log.d("PRESCRIPTIONDECHECKURL","setFileLabel"+mCursor.getString((11)));
                Log.d("PRESCRIPTIONDECHECKURL","setPreimageName"+mCursor.getString((4)));
                Log.d("PRESCRIPTIONDECHECKURL","setPreThumbImageName"+mCursor.getString((5)));
                mCursor.moveToNext();
            }
            mCursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return modelList;
    }

    private ContentValues gerContentValues(ApptPrescriptionModel model) {
        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(CREATED_ID, model.getCreatedById());
            //initialValues.put(PRESCRIPTION_IMAGEPATH, model.getPreimageName());
            initialValues.put(SYMPTOMS, model.getSymptoms());
            initialValues.put(CREATED_AT_SERVER, model.getCreatedAtServer());
            initialValues.put(FILE_TYPE, model.getFileType());
            initialValues.put(FILE_MIME_TYPE, model.getFileMimeType());
            initialValues.put(FILE_LABEL, model.getFileLabel());
            initialValues.put(FILE_GROUP, model.getFileGroup());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }

    public boolean updatePrescFullImagePath(String fullImgPath,
                                            String patientId, int appointmentId, String createdAt) {

        Log.d("SPIN", "Inside updatePrescFullImagePath : appt id : " + appointmentId);
        long flag = 0;
        boolean isPrescFileUpate = false;
        String prescImgPathInDb = null;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(PRESCRIPTION_IMAGEPATH, fullImgPath);

            Cursor mCursor = db.query(TABLE_APPOINMENTS_PRESC, new String[]{
                            PRESCRIPTION_IMAGEPATH},
                    PATIENT_ID + "=? " +
                            " AND " + APPOINTMENT_ID + "=?" +
                            " AND " + CREATED_AT + "=?",
                    new String[]{patientId,
                            Integer.toString(appointmentId),
                            createdAt},
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }

                isPrescFileUpate = false;
                Log.d("SPIN", "TABLE_APPOINMENTS_PRESC : No row with specified created at & appt id. Result : " + appointmentId);

            } else {

                Log.d("SPIN", "TABLE_APPOINMENTS_PRESC : Existing record -> update.");
                Log.d("SPIN", "Inside updatePrescFullImagePath : Row : count " + mCursor.getCount());

                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    prescImgPathInDb = mCursor.getString(0);
                    mCursor.moveToNext();
                }


                flag = db.update(TABLE_APPOINMENTS_PRESC, initialValues,
                        PATIENT_ID + "=? " +
                                " AND " + APPOINTMENT_ID + "=?" +
                                " AND " + CREATED_AT + "=?",
                        new String[]{patientId, Integer.toString(appointmentId), createdAt});

                LogTrace.w(TAG, flag + "  appts emr updated : " + patientId);

                if (prescImgPathInDb == null || prescImgPathInDb == "") {
                    Log.d("SPIN", "Inside updatePrescFullImagePath :no image path in the sqlite table.");
                } else {

                    Log.d("SPIN", "Inside updatePrescFullImagePath :Delete the image in the path if exists.");
                    //delete old image from directory.
                    //VTConstants.DeleteFileFromPath(emrImgPathInDb);
                }

                isPrescFileUpate = true;

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts emr information");
        }
        return isPrescFileUpate;

    }

    public boolean updatePrescriptionThumbImagePath(String thumbImgPath,
                                                    String patientId, int appointmentId, String createdAt) {

        Log.d("SPIN", "Inside updatePrescriptionThumbImagePath : appt id : " + appointmentId);
        long flag = 0;
        boolean isPrescFileUpate = false;
        String prescImgPathInDb = null;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(IMAGE_THUMBNAIL_PATH, thumbImgPath);

            Cursor mCursor = db.query(TABLE_APPOINMENTS_PRESC, new String[]{
                            IMAGE_THUMBNAIL_PATH},
                    PATIENT_ID + "=? " +
                            " AND " + APPOINTMENT_ID + "=?" +
                            " AND " + CREATED_AT + "=?",
                    new String[]{patientId,
                            Integer.toString(appointmentId),
                            createdAt},
                    null,
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }

                isPrescFileUpate = false;
                Log.d("SPIN", "TABLE_APPOINMENTS_PRESC :updatePrescriptionThumbImagePath : No row with specified created at & appt id. Result : " + appointmentId);

            } else {

                Log.d("SPIN", "TABLE_APPOINMENTS_PRESC : Existing record -> update.");
                Log.d("SPIN", "Inside updatePrescriptionThumbImagePath : Row : count " + mCursor.getCount());

                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    prescImgPathInDb = mCursor.getString(0);
                    mCursor.moveToNext();
                }


                flag = db.update(TABLE_APPOINMENTS_PRESC, initialValues,
                        PATIENT_ID + "=? " +
                                " AND " + APPOINTMENT_ID + "=?" +
                                " AND " + CREATED_AT + "=?",
                        new String[]{patientId, Integer.toString(appointmentId), createdAt});

                LogTrace.w(TAG, flag + "  appts emr updated : " + patientId);

                if (prescImgPathInDb == null || prescImgPathInDb == "") {
                    Log.d("SPIN", "Inside updatePrescFullImagePath :no image path in the sqlite table.");
                } else {

                    Log.d("SPIN", "Inside updatePrescFullImagePath :Delete the image in the path if exists.");
                    //delete old image from directory.
                    //VTConstants.DeleteFileFromPath(emrImgPathInDb);
                }

                isPrescFileUpate = true;

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts emr information");
        }
        return isPrescFileUpate;

    }

    //rony 1.3.4 starts
    public boolean updatePharmacyDataInPrescriptionTable(String pharamcyId, int appointmentId) {
        boolean returnVal = false;
        long flag = 0;
        try {
            Log.d("SPIN", "Inside updatePharmacyDataInPrescriptionTable : pharmacy id : " + pharamcyId);


            ContentValues initialValues = new ContentValues();
            initialValues.put(IS_PHARMACY_ASSIGNED, 1);
            initialValues.put(PHARMACY_ID, pharamcyId);

            flag = db.update(TABLE_APPOINMENTS_PRESC, initialValues,
                    APPOINTMENT_ID + "=? " + " AND " + IS_DELETED + "=?",
                    new String[]{Integer.toString(appointmentId), VTConstants.IS_NOT_DELETED});

            Log.d("SPIN", "Inside updatePharmacyDataInPrescriptionTable : Update query status : " + flag);

            if (flag != -1) {
                returnVal = true;
            } else {
                returnVal = false;
            }

        } catch (Exception e) {
            Log.d("SPIN", "Inside updatePharmacyDataInPrescriptionTable : Caught exception for pharmacy : " + pharamcyId);
            e.printStackTrace();
            returnVal = false;
        }

        return returnVal;
    }

    public boolean CheckPrescriptionAlreadyShared(int appointmentId) {
        boolean returnVal = false;
        int flag = 0;
        try {
            Log.d("SPIN", "Inside CheckPrescriptionAlreadyShared : starts");

            Cursor mCursor = db.query(TABLE_APPOINMENTS_PRESC, new String[]{
                            IS_PHARMACY_ASSIGNED},
                    APPOINTMENT_ID + "=? " + " AND " + IS_DELETED + "=?" + " AND " + IS_PHARMACY_ASSIGNED + "=?",
                    new String[]{Integer.toString(appointmentId), VTConstants.IS_NOT_DELETED, Integer.toString(0)},
                    null,
                    null,
                    null);
            if (mCursor == null || mCursor.getCount() <= 0) {

                Log.d("SPIN", "Inside CheckPrescriptionAlreadyShared : total unshared prescriptions found : " + mCursor.getCount());

                //return false , so that app send mail request to server.
                returnVal = true;
            } else {

                Log.d("SPIN", "Inside CheckPrescriptionAlreadyShared : total unshared prescriptions found : " + mCursor.getCount());
                //return true , so that no need to send mail request to server.
                returnVal = false;
            }
            if (mCursor != null)
                mCursor.close();

            Log.d("SPIN", "Inside CheckPrescriptionAlreadyShared : Ends");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SPIN", "Error CheckPrescriptionAlreadyShared information");
        }

        return returnVal;
    }

//rony 1.3.4 ends


    public void deleteRecordById(ArrayList<String> arrayList) {
        System.out.println("SDCARDPATH" + arrayList);
        Cursor mCursor = null;
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                mCursor = db.query(TABLE_APPOINMENTS_PRESC, new String[]{
                                PRESCRIPTION_IMAGEPATH, IMAGE_THUMBNAIL_PATH},
                        APPOINTMENT_ID + " = \"" + arrayList.get(i) + "\"",
                        null,
                        null,
                        null,
                        null,
                        null);
                if (mCursor.moveToFirst()) {
                    do {
                        System.out.println("DELETEIMAGEPATH" + mCursor.getString(0));
                        System.out.println("DELETEIMAGEPATH" + mCursor.getString(1));
                        deleteImages(mCursor.getString(0), mCursor.getString(1));
                    }
                    while (mCursor.moveToNext());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteImages(String preImage, String thumImage) {

        try {
            if (preImage != null && !preImage.isEmpty()) {
                File presFile = new File(preImage);

                if (presFile.delete()) {
                    Log.d(TAG, "DELETED");
                }
            }
            if (thumImage != null && !thumImage.isEmpty()) {
                File thumFile = new File(thumImage);
                if (thumFile.delete()) {
                    Log.d(TAG, "DELETED");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //rony 1.3.6 starts
    public ArrayList<String> getMaxDate(String customerId, int reservationNumber) {
        String maxDate = "";
        ArrayList<String> arrayList = new ArrayList<String>();
        try {


            Cursor mCursor = db.query(TABLE_APPOINMENTS_PRESC, new String[]{CREATED_AT_SERVER},
                    PATIENT_ID + " = ?" + " AND " + APPOINTMENT_ID + " =?", new String[]{String.valueOf(customerId), String.valueOf(reservationNumber)},
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

//            Log.d("SPIN", "Inside getMaxDate : " + arrayList.get(0));

            return arrayList;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SPIN", "Inside getMaxDate : Caught Exception.");
        }


        return arrayList;
    }
    //rony 1.3.6 ends

    public void deletePrescriptionRecordFromTable(String patientId, String appointmentid, String cratedat)

    {
        Log.d("NEWALERCHECK", " DELETE:" + patientId);
        Log.d("NEWALERCHECK", " DELETE:" + appointmentid);
        Log.d("NEWALERCHECK", " DELETE:" + cratedat);
        int value = db.delete(TABLE_APPOINMENTS_PRESC, PATIENT_ID + "=? " + " AND " + APPOINTMENT_ID + "=?" + " AND " + CREATED_AT + "=?", new String[]{patientId, appointmentid, cratedat});
        Log.d("NEWALERCHECK", " DELETE:" + value);
    }
}
