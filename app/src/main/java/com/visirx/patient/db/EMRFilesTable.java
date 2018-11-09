package com.visirx.patient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.util.ArrayList;

public class EMRFilesTable {
    private static final String TAG = EMRFilesTable.class.getName();
    private SQLiteDatabase db;


    public EMRFilesTable(SQLiteDatabase mdb) {
        super();
        db = mdb;

    }
    // rony - EMRFRAGMENT GC  - Starts

    //unit registration table
    public static final String PATIENT_ID = "patient_id";
    public static final String APPOINTMENT_ID = "appt_id";
    public static final String CREATED_AT = "created_at";
    public static final String CREATED_ID = "createdById";
    public static final String FILE_TYPE = "file_type";
    public static final String FILE_MIME_TYPE = "filemime_type";
    public static final String FILE_GROUP = "file_group";
    public static final String FILE_LABEL = "file_label";
    public static final String IMAGE_THUMBNAIL_PATH = " image_thumbnail_path";
    public static final String EMR_FILE_PATH = "emr_file_path";
    public static final String MSG_ID = "msg_id";
    public static final String PROCESS_FLAG = "processed_flag";
    public static final String IS_DELETED = "is_deleted";
    public static final String CREATED_AT_SERVER = "created_at_server";

    public static final String TABLE_APPOINMENTS_EMR = "appoinments_emrfile";
    //create table
    static String PRIMARYKEY_REG = PATIENT_ID + "," + APPOINTMENT_ID + "," + CREATED_AT;
    public static final String TABLE_APPTS_EMR_CREATE = " create table IF NOT EXISTS " +
            TABLE_APPOINMENTS_EMR + "(" +
            PATIENT_ID + " text not null, " +
            CREATED_ID + " text not null, " +
            APPOINTMENT_ID + " integer not null, " +
            CREATED_AT + " text not null, " +
            CREATED_AT_SERVER + " text not null, " +
            IMAGE_THUMBNAIL_PATH + " text, " +
            EMR_FILE_PATH + " text, " +
            FILE_TYPE + " text , " +
            FILE_MIME_TYPE + " text , " +
            FILE_GROUP + " text , " +
            FILE_LABEL + " text , " +
            MSG_ID + " text , " +
            PROCESS_FLAG + " integer, " +
            IS_DELETED + " text, " +
            "primary key(" + PRIMARYKEY_REG + ")" + ");";


    public boolean insertAppointmentEMR(AddEmrFileModel model, int processFlag) {
        long flag = 0;
        boolean isEmrFileUpate = false;
        String emrImgThumbPath = null;
        try {
            ContentValues initialValues = gerContentValues(model, processFlag);
            Cursor mCursor = db.query(TABLE_APPOINMENTS_EMR, new String[]{
                            PATIENT_ID,
                            APPOINTMENT_ID, IMAGE_THUMBNAIL_PATH},
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
                initialValues.put(EMR_FILE_PATH, model.getEmrImagePath());
                initialValues.put(IMAGE_THUMBNAIL_PATH, model.getEmrImagePath());
                flag = db.insert(TABLE_APPOINMENTS_EMR, null, initialValues);
                LogTrace.w(TAG, flag + " Insert appts emr : " + model.getPatientId());
                isEmrFileUpate = true;
                Log.d("SPIN", "TABLE_APPTS_EMR_CREATE : new emr file inserted . Sync required . " + isEmrFileUpate);
            } else {
                Log.d("SPIN", "TABLE_APPTS_EMR_CREATE : Existing record -> update.");
                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    emrImgThumbPath = mCursor.getString(2);
                    mCursor.moveToNext();
                }
                if ((emrImgThumbPath != null && !VTConstants.CheckFileExists(emrImgThumbPath)) || (emrImgThumbPath == null)) {
                    isEmrFileUpate = true;
                    Log.d("SPIN", "else 2 : no change in timestamps .File is missing from directory.sync required.");
                } else {
                    isEmrFileUpate = false;
                }
                flag = db.update(TABLE_APPOINMENTS_EMR, initialValues,
                        PATIENT_ID + "=? " +
                                " AND " + APPOINTMENT_ID + "=?" +
                                " AND " + CREATED_AT + "=?",
                        new String[]{model.getPatientId(), Integer.toString(model.getAppointmentId()), model.getCreatedAt()});
                LogTrace.w(TAG, flag + "  appts emr updated : " + model.getPatientId());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts emr information");
        }
        return isEmrFileUpate;
    }


    public ArrayList<AddEmrFileModel> GetEMRFile() {
        ArrayList<AddEmrFileModel> modelList = new ArrayList<AddEmrFileModel>();
        try {
            Cursor mCursor = db.query(TABLE_APPOINMENTS_EMR, new String[]{
                            PATIENT_ID,
                            APPOINTMENT_ID,
                            CREATED_ID,
                            CREATED_AT,
                            IMAGE_THUMBNAIL_PATH,
                            EMR_FILE_PATH,
                            FILE_TYPE,
                            FILE_MIME_TYPE,
                            FILE_GROUP,
                            FILE_LABEL,
                            PROCESS_FLAG},
                    null,
                    null,
                    null,
                    null,
                    null);
            // rony - EMRFRAGMENT GC  - Ends
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return modelList;
            }
            mCursor.moveToFirst();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                AddEmrFileModel model = getModel(mCursor);
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
    // rony - EMRFRAGMENT GC  - Starts

    public ArrayList<AddEmrFileModel> GetPatientEMRFile(String patientId, int appointmentId) {
        ArrayList<AddEmrFileModel> modelList = new ArrayList<AddEmrFileModel>();
        try {
            Log.d("SPIN", "inside GetPatientEMRFile  1 : " + patientId);
            Log.d("SPIN",  "inside GetPatientEMRFile  2 : "  + appointmentId);

            Cursor mCursor = db.query(TABLE_APPOINMENTS_EMR, new String[]{PATIENT_ID, APPOINTMENT_ID, CREATED_ID,
                            CREATED_AT, IMAGE_THUMBNAIL_PATH, EMR_FILE_PATH, FILE_TYPE, FILE_MIME_TYPE, FILE_GROUP, FILE_LABEL, PROCESS_FLAG},
                    PATIENT_ID + "=? " + " AND " + APPOINTMENT_ID + "=?", new String[]{patientId, Integer.toString(appointmentId)},
                    null, null, CREATED_AT + " DESC",
                    null);
            Log.d("SPIN",  "inside GetPatientEMRFile  3 : "  + mCursor.getCount());
            // rony - EMRFRAGMENT GC  - Ends
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return modelList;
            }
            mCursor.moveToFirst();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                AddEmrFileModel model = getModel(mCursor);
                modelList.add(model);
                mCursor.moveToNext();
            }
            mCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelList;
    }

    public ArrayList<AddEmrFileModel> GetPatientEMRFileDetails(String patientId, int appointmentId, String createdat) {
        ArrayList<AddEmrFileModel> modelList = new ArrayList<AddEmrFileModel>();
        try {
            Cursor mCursor = db.query(TABLE_APPOINMENTS_EMR, new String[]{PATIENT_ID, APPOINTMENT_ID, CREATED_ID,
                            CREATED_AT, IMAGE_THUMBNAIL_PATH, EMR_FILE_PATH, FILE_TYPE, FILE_MIME_TYPE, FILE_GROUP, FILE_LABEL, PROCESS_FLAG},
                    PATIENT_ID + "=? " + " AND " + APPOINTMENT_ID + "=?" + " AND " + CREATED_AT + "=?", new String[]{patientId, Integer.toString(appointmentId), createdat},
                    null, null, CREATED_AT + " DESC",
                    null);

            // rony - EMRFRAGMENT GC  - Ends
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return modelList;
            }
            mCursor.moveToFirst();

            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                AddEmrFileModel model = getModel(mCursor);
                modelList.add(model);
                mCursor.moveToNext();
            }
            mCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelList;
    }

    public ArrayList<String> GetPatientEMRFileMax(String patientId,
                                                  int appointmentId,String createdById) {
        ArrayList<String> arrayList = new ArrayList<String>();
        String maxDate = "";
        Cursor mCursor = db.query(TABLE_APPOINMENTS_EMR, new String[]{CREATED_AT_SERVER},
                PATIENT_ID + " = ?" + " AND " + APPOINTMENT_ID + " =?" + " AND " + PROCESS_FLAG + " =?"+ " AND " + CREATED_ID + " !=?",
                new String[]{patientId, String.valueOf(appointmentId), String.valueOf(VTConstants.PROCESSED_FLAG_SENT),createdById},
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
    // rony - EMRFRAGMENT GC  - Starts

    public ArrayList<AddEmrFileModel> GetPatientEMRFile(String patientId, int appointmentId,
                                                        String date) {
        ArrayList<AddEmrFileModel> modelList = new ArrayList<AddEmrFileModel>();
        try {
            String fromdate = date + " " + "00:00";
            String todate = date + " " + "23:59";
            Cursor mCursor = db.query(TABLE_APPOINMENTS_EMR, new String[]{
                            PATIENT_ID,
                            APPOINTMENT_ID,
                            CREATED_ID,
                            CREATED_AT,
                            IMAGE_THUMBNAIL_PATH,
                            EMR_FILE_PATH,
                            FILE_TYPE,
                            FILE_MIME_TYPE,
                            FILE_GROUP,
                            FILE_LABEL,
                            PROCESS_FLAG},
                    PATIENT_ID + "=? " +
                            " AND " + APPOINTMENT_ID + "=?"
                           ,
                    new String[]{patientId, Integer.toString(appointmentId)},
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
                AddEmrFileModel model = getModel(mCursor);
                modelList.add(model);
                mCursor.moveToNext();
            }
            mCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelList;
    }


    private AddEmrFileModel getModel(Cursor mCursor) {
        AddEmrFileModel model = new AddEmrFileModel();
        try {
            model.setPatientId(mCursor.getString(0));
            model.setAppointmentId(mCursor.getInt(1));
            model.setCreatedById(mCursor.getString(2));
            model.setCreatedAt(mCursor.getString(3));
            /*byte[] thumbnail = mCursor.getBlob(4);
            if( thumbnail== null){
				thumbnail = new String("").getBytes();
			}*/
            model.setEmrImageThumbnailPath(mCursor.getString(4));
            model.setEmrImagePath(mCursor.getString(5));
            model.setFileType(mCursor.getString(6));
            model.setFileMimeType(mCursor.getString(7));
            model.setFileGroup(mCursor.getString(8));
            model.setFileLabel(mCursor.getString(9));
            model.setProcessedFlag(mCursor.getInt(10));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
    // rony - EMRFRAGMENT GC  - Starts

    private ContentValues gerContentValues(AddEmrFileModel model, int processFlag) {
        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(CREATED_ID, model.getCreatedById());
            // rony - EMRFRAGMENT GC  - Starts
            //initialValues.put(IMAGE_THUMBNAIL, model.getImageThumbnail());
            //initialValues.put(EMR_FILE, model.getEmrFile());
            initialValues.put(IS_DELETED, VTConstants.IS_NOT_DELETED);
            // rony - EMRFRAGMENT GC  - Ends
            initialValues.put(FILE_TYPE, model.getFileType());
            initialValues.put(FILE_MIME_TYPE, model.getFileMimeType());
            initialValues.put(FILE_GROUP, model.getFileGroup());
            initialValues.put(FILE_LABEL, model.getFileLabel());

            initialValues.put(PROCESS_FLAG, processFlag);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }
    // rony - EMRFRAGMENT GC  - Starts


	/*public ArrayList<AddEmrFileModel> GetEMRFileNotSent(){
        ArrayList<AddEmrFileModel> modelList = new ArrayList<AddEmrFileModel>();
		try {
			Cursor mCursor =db.query(TABLE_APPOINMENTS_EMR, new String[] {
					PATIENT_ID ,
					APPOINTMENT_ID ,
					CREATED_ID,
					CREATED_AT,
					IMAGE_THUMBNAIL,
					EMR_FILE,
					FILE_TYPE,
					FILE_MIME_TYPE,
					FILE_GROUP,
					FILE_LABEL,
					PROCESS_FLAG},
					PROCESS_FLAG + "!=" + VTConstants.PROCESSED_FLAG_SENT_RESPONSE ,
					null,
					null,
					null,
					null,
					null);
			if(mCursor == null || mCursor.getCount() <= 0){
				if(mCursor != null)
					mCursor.close();
				return modelList;
			}
			mCursor.moveToFirst();

			for(int counter = 0; counter < mCursor.getCount();counter++){
				AddEmrFileModel model = getModel(mCursor);
				modelList.add(model);
				mCursor.moveToNext();
			}
			mCursor.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modelList;
	}*/
    // rony - EMRFRAGMENT GC  - Ends

    public int UpdateProcessFlagSent(AddEmrFileModel model,
                                     int processedFlag, String msgID) {
        int flag = 0;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(PROCESS_FLAG, processedFlag);
            initialValues.put(MSG_ID, msgID);
            flag = db.update(TABLE_APPOINMENTS_EMR, initialValues,
                    PATIENT_ID + "=? " +
                            " AND " + APPOINTMENT_ID + "=?" +
                            " AND " + CREATED_AT + "=?",
                    new String[]{model.getPatientId(), Integer.toString(model.getAppointmentId()), model.getCreatedAt()});
            LogTrace.w(TAG, flag + " EMR Updated : " + msgID + " Flag : " + processedFlag);
        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, "Error updating emr");
        }
        return flag;
    }

    public int UpdateProcessFlagResponse(String msgID, int processedFlag) {
        int flag = 0;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(PROCESS_FLAG, processedFlag);
            flag = db.update(TABLE_APPOINMENTS_EMR, initialValues,
                    MSG_ID + " = \"" + msgID + "\"", null);
            LogTrace.w(TAG, flag + " Update EMR Flag Res : " + msgID + " flag : " + processedFlag);
        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, "Error updating EMR Response");
        }
        return flag;
    }
    // rony - EMRFRAGMENT GC  - Starts

    //DateFormat.GetFormattedDate_YYYYMMDD(model.getCreatedAt());
    /*public ArrayList<AddEmrFileModel> GetEMRFileNotSentByDate(String patientId, int appointmentId, String createdAt){
        ArrayList<AddEmrFileModel> modelList = new ArrayList<AddEmrFileModel>();
		try {

			String date = DateFormat.GetFormattedDate_YYYYMMDD(createdAt);
			String fromdate =date + " " + "00:00";
			String todate =date + " " + "23:59";

			Cursor mCursor =db.query(TABLE_APPOINMENTS_EMR, new String[] {
					PATIENT_ID ,
					APPOINTMENT_ID ,
					CREATED_ID,
					CREATED_AT,
					IMAGE_THUMBNAIL,
					EMR_FILE,
					FILE_TYPE,
					FILE_MIME_TYPE,
					FILE_GROUP,
					FILE_LABEL,
					PROCESS_FLAG},
					PATIENT_ID + "=? " +
							" AND " + APPOINTMENT_ID + "=?" +
							" AND " + CREATED_AT + ">=?" +
							" AND " + CREATED_AT + "<=?",
							new String[] {patientId,Integer.toString(appointmentId), fromdate, todate},
							null,
							null,
							null,
							null);
			if(mCursor == null || mCursor.getCount() <= 0){
				if(mCursor != null)
					mCursor.close();
				return modelList;
			}
			mCursor.moveToFirst();

			for(int counter = 0; counter < mCursor.getCount();counter++){
				AddEmrFileModel model = getModel(mCursor);
				modelList.add(model);
				mCursor.moveToNext();
			}
			mCursor.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modelList;
	}*/
//    RAMESH CHANGES
    public ArrayList<AddEmrFileModel> GetEMRFileNotSent(int ReservationNumber) {
        ArrayList<AddEmrFileModel> modelList = new ArrayList<AddEmrFileModel>();
        try {


            Cursor mCursor = db.query(TABLE_APPOINMENTS_EMR, new String[]{PATIENT_ID, APPOINTMENT_ID, CREATED_ID,
                            CREATED_AT, IMAGE_THUMBNAIL_PATH, EMR_FILE_PATH, FILE_TYPE, FILE_MIME_TYPE, FILE_GROUP, FILE_LABEL, PROCESS_FLAG},
                    PROCESS_FLAG + "=? " + " AND " + APPOINTMENT_ID + "=?", new String[]{Integer.toString(VTConstants.PROCESSED_FLAG_NOT_SENT), Integer.toString(ReservationNumber)},
                    null, null, CREATED_AT + " DESC",
                    null);


            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return modelList;
            }
            mCursor.moveToFirst();

            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                AddEmrFileModel model = getModel(mCursor);
                modelList.add(model);
                mCursor.moveToNext();
            }
            mCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelList;
    }


    public boolean updateEmrThumbImagePath(String thumbPath, String patientId,
                                           int appointmentId, String createdAt) {
        Log.d("SPIN", "Inside updateEmrThumbImagePath : appt id : " + appointmentId);
        long flag = 0;
        boolean isEmrFileUpate = false;
        String emrImgThumbPathInDb = null;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(IMAGE_THUMBNAIL_PATH, thumbPath);
            Cursor mCursor = db.query(TABLE_APPOINMENTS_EMR, new String[]{
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
                isEmrFileUpate = false;
                Log.d("SPIN", "TABLE_APPTS_EMR_ : No row with specified created at & appt id. Result : " + appointmentId);

            } else {
                Log.d("SPIN", "TABLE_APPTS_EMR : Existing record -> update.");
                Log.d("SPIN", "Inside updateEmrThumbImagePath : Row : count " + mCursor.getCount());
                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    emrImgThumbPathInDb = mCursor.getString(0);
                    mCursor.moveToNext();
                }
                flag = db.update(TABLE_APPOINMENTS_EMR, initialValues,
                        PATIENT_ID + "=? " +
                                " AND " + APPOINTMENT_ID + "=?" +
                                " AND " + CREATED_AT + "=?",
                        new String[]{patientId, Integer.toString(appointmentId), createdAt});
                LogTrace.w(TAG, flag + "  appts emr updated : " + patientId);
                if (emrImgThumbPathInDb == null || emrImgThumbPathInDb == "") {
                    Log.d("SPIN", "Inside updateEmrThumbImagePath :no image path in the sqlite table.");
                } else {
                    Log.d("SPIN", "Inside updateEmrThumbImagePath :Delete the image in the path if exists.");
                    //delete old image from directory.
                    //VTConstants.DeleteFileFromPath(emrImgThumbPathInDb);
                }
                isEmrFileUpate = true;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts emr information");
        }
        return isEmrFileUpate;

    }


    public boolean updateEmrFullImagePath(String fullImgPath, String patientId,
                                          int appointmentId, String createdAt) {
        Log.d("SPIN", "Inside updateEmrFullImagePath : appt id : " + appointmentId);
        long flag = 0;
        boolean isEmrFileUpate = false;
        String emrImgPathInDb = null;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(EMR_FILE_PATH, fullImgPath);
            Cursor mCursor = db.query(TABLE_APPOINMENTS_EMR, new String[]{
                            EMR_FILE_PATH},
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
                isEmrFileUpate = false;
                Log.d("SPIN", "TABLE_APPTS_EMR_ : No row with specified created at & appt id. Result : " + appointmentId);

            } else {
                Log.d("SPIN", "TABLE_APPTS_EMR : Existing record -> update.");
                Log.d("SPIN", "Inside updateEmrFullImagePath : Row : count " + mCursor.getCount());
                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    emrImgPathInDb = mCursor.getString(0);
                    mCursor.moveToNext();
                }
                flag = db.update(TABLE_APPOINMENTS_EMR, initialValues,
                        PATIENT_ID + "=? " +
                                " AND " + APPOINTMENT_ID + "=?" +
                                " AND " + CREATED_AT + "=?",
                        new String[]{patientId, Integer.toString(appointmentId), createdAt});
                LogTrace.w(TAG, flag + "  appts emr updated : " + patientId);
                if (emrImgPathInDb == null || emrImgPathInDb == "") {
                    Log.d("SPIN", "Inside updateEmrFullImagePath :no image path in the sqlite table.");
                } else {
                    Log.d("SPIN", "Inside updateEmrFullImagePath :Delete the image in the path if exists.");
                    //delete old image from directory.
                    //VTConstants.DeleteFileFromPath(emrImgPathInDb);
                }
                isEmrFileUpate = true;

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts emr information");
        }
        return isEmrFileUpate;

    }


    public boolean updateEmrAudioPath(String audioFilePath, String patientId,
                                      int appointmentId, String createdAt) {
        Log.d("SPIN", "Inside updateEmrAudioPath : appt id : " + appointmentId);
        long flag = 0;
        boolean isEmrFileUpate = false;
        String emrAudioPathInDb = null;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(EMR_FILE_PATH, audioFilePath);
            Cursor mCursor = db.query(TABLE_APPOINMENTS_EMR, new String[]{
                            EMR_FILE_PATH},
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
                isEmrFileUpate = false;
                Log.d("SPIN", "TABLE_APPTS_EMR_ : No row with specified created at & appt id. Result : " + appointmentId);

            } else {
                Log.d("SPIN", "TABLE_APPTS_EMR : Existing record -> update.");
                Log.d("SPIN", "Inside updateEmrAudioPath : Row : count " + mCursor.getCount());
                mCursor.moveToFirst();
                for (int counter = 0; counter < mCursor.getCount(); counter++) {
                    emrAudioPathInDb = mCursor.getString(0);
                    mCursor.moveToNext();
                }
                flag = db.update(TABLE_APPOINMENTS_EMR, initialValues,
                        PATIENT_ID + "=? " +
                                " AND " + APPOINTMENT_ID + "=?" +
                                " AND " + CREATED_AT + "=?",
                        new String[]{patientId, Integer.toString(appointmentId), createdAt});
                LogTrace.w(TAG, flag + "  appts emr updated : " + patientId);
                if (emrAudioPathInDb == null || emrAudioPathInDb == "") {
                    Log.d("SPIN", "Inside updateEmrAudioPath :no audio file path in the sqlite table.");
                } else {
                    Log.d("SPIN", "Inside updateEmrAudioPath :Delete the image in the path if exists.");
                    //delete old image from directory.
                    //VTConstants.DeleteFileFromPath(emrAudioPathInDb);
                }
                isEmrFileUpate = true;

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts emr information");
        }
        return isEmrFileUpate;

    }

    public void deleteRecordById(ArrayList<String> arrayList) {
        System.out.println("EMR" + arrayList);
        Cursor mCursor = null;
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                mCursor = db.query(TABLE_APPOINMENTS_EMR, new String[]{
                                EMR_FILE_PATH, IMAGE_THUMBNAIL_PATH},
                        APPOINTMENT_ID + " = \"" + arrayList.get(i) + "\"",
                        null,
                        null,
                        null,
                        null,
                        null);
                if (mCursor.moveToFirst()) {
                    do {
                        deleteImages(mCursor.getString(0), mCursor.getString(1));
                    }
                    while (mCursor.moveToNext());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteImages(String emrFullImage, String emrThumnill) {
        try {
            if (emrFullImage != null && !emrFullImage.isEmpty()) {
                File presFile = new File(emrFullImage);
                if (presFile.exists()) {
                    presFile.delete();
                    Log.d(TAG, "DELETED");
                }
            }
            if (emrThumnill != null && !emrThumnill.isEmpty()) {
                File thumFile = new File(emrThumnill);
                if (thumFile.exists()) {
                    thumFile.delete();
                    Log.d(TAG, "DELETED");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


// RAMESH CHANGES

    public long setProcessFlagAppointmentPrescription(String patientId, int appointmentId, String createdAt, String filename, Context context, String createdAtServer) {
        long flag = 0;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(PROCESS_FLAG, VTConstants.PROCESSED_FLAG_SENT);
            initialValues.put(CREATED_AT_SERVER, createdAtServer);

            flag = db.update(TABLE_APPOINMENTS_EMR, initialValues,
                    PATIENT_ID + "=? " + " AND " + APPOINTMENT_ID + "=?" + " AND " + CREATED_AT + "=?",
                    new String[]{patientId, Integer.toString(appointmentId), createdAt});

            System.out.println("EMRONLINE 1 " + String.valueOf(appointmentId) + " ==== " + filename);
            EventChecking.emrImageOnline(String.valueOf(appointmentId), filename, "Online", context);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error inserting appts notes information");
        }
        return flag;
    }
    // rony - EMRFRAGMENT GC  - Ends

    // RAMESH CHANGES

    public void deleteEhdRecordFromTable(String patientId, String appointmentid, String cratedat) {
        try {
            Log.d("NEWALERCHECK", " DELETE:" + patientId);
            Log.d("NEWALERCHECK", " DELETE:" + appointmentid);
            Log.d("NEWALERCHECK", " DELETE:" + cratedat);
            int value = db.delete(TABLE_APPOINMENTS_EMR, PATIENT_ID + "=? " + " AND " + APPOINTMENT_ID + "=?" + " AND " + CREATED_AT + "=?", new String[]{patientId, appointmentid, cratedat});
            Log.d("NEWALERCHECK", " DELETE:" + value);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
