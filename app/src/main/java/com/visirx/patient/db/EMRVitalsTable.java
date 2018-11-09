package com.visirx.patient.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AddEmrVitalsModel;
import com.visirx.patient.utils.VTConstants;

import java.util.ArrayList;

public class EMRVitalsTable {
	private static final String TAG = EMRVitalsTable.class.getName();
	private SQLiteDatabase db;
	public EMRVitalsTable(SQLiteDatabase mdb) {
		super();
		db = mdb;
	}

	//unit registration table
	public static final String PATIENT_ID="patient_id";
	public static final String APPOINTMENT_ID ="appt_id";
	public static final String CREATED_AT ="created_at";
	public static final String CREATED_ID ="createdById";
	//public static final String SEQUENCE = "sequence";
	public static final String VITAL_UNIT = "vital_unit";
	public static final String VITAL_GROUP = "vital_group";
	public static final String VITAL_KEY = "vital_key";
	public static final String VITAL_VALUE = " vital_value";
	public static final String MSG_ID = "msg_id";
	public static final String PROCESS_FLAG = "processed_flag";  

	public static final String TABLE_APPOINMENTS_EMR = "appoinments_emr_vital";
	//create table
	static String PRIMARYKEY_REG = PATIENT_ID + "," + APPOINTMENT_ID + "," + CREATED_AT;
	public static final String TABLE_APPTS_EMR_CREATE = " create table IF NOT EXISTS " +
			TABLE_APPOINMENTS_EMR +"(" +
			PATIENT_ID + " text not null, " +
			CREATED_ID + " text not null, " +
			APPOINTMENT_ID + " integer not null, " +
			CREATED_AT + " text not null, " +		 
			VITAL_VALUE + " text, " +
			VITAL_UNIT + " text , " +		 
			VITAL_GROUP + " text , " +		 
			VITAL_KEY + " text , " +
			MSG_ID + " text , " +
			PROCESS_FLAG + " integer, " +		
			"primary key("+ PRIMARYKEY_REG +")" +");";
	public long insertAppointmentEMRVital(AddEmrVitalsModel model){
		long flag  = 0;
		try {
			ContentValues initialValues = gerContentValues(model);
			Cursor mCursor =db.query(TABLE_APPOINMENTS_EMR, new String[] {
					PATIENT_ID,
					APPOINTMENT_ID},
					PATIENT_ID + "=? " +
							" AND " + APPOINTMENT_ID + "=?" +
							" AND " + CREATED_AT + "=?",
							new String[] {model.getPatientId(),
					Integer.toString(model.getAppointmentId()),
					model.getCreatedAt()},
					null,
					null,
					null,
					null);
			if(mCursor == null || mCursor.getCount() <= 0){
				if(mCursor != null){
					mCursor.close();
				}
				initialValues.put(PATIENT_ID, model.getPatientId());
				initialValues.put(APPOINTMENT_ID, model.getAppointmentId());
				initialValues.put(CREATED_AT, model.getCreatedAt());
				flag = db.insert(TABLE_APPOINMENTS_EMR, null, initialValues);
				LogTrace.w(TAG,  flag + " Insert appts emr : "+ model.getPatientId());
			}else{
				flag = db.update(TABLE_APPOINMENTS_EMR, initialValues ,
						PATIENT_ID + "=? " +
								" AND " + APPOINTMENT_ID + "=?" +
								" AND " + CREATED_AT + "=?",
								new String[] {model.getPatientId(),Integer.toString(model.getAppointmentId()),	model.getCreatedAt()}); 

				LogTrace.w(TAG,  flag + "  appts emr updated : "+ model.getPatientId());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogTrace.e(TAG,  "Error inserting appts emr information");
		}
		return flag;
	}
	public ArrayList<AddEmrVitalsModel> GetEMRVital(){
		ArrayList<AddEmrVitalsModel> modelList = new ArrayList<AddEmrVitalsModel>();
		try {
			Cursor mCursor =db.query(TABLE_APPOINMENTS_EMR, new String[]{
							PATIENT_ID,
							APPOINTMENT_ID,
							CREATED_ID,
							CREATED_AT,
							VITAL_VALUE,
							VITAL_UNIT,
							VITAL_GROUP,
							VITAL_KEY,
							PROCESS_FLAG},
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
				AddEmrVitalsModel model = getModel(mCursor); 
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
	public ArrayList<AddEmrVitalsModel> GetPatientEMRVital(String patientId, int appointmentId){
		ArrayList<AddEmrVitalsModel> modelList = new ArrayList<AddEmrVitalsModel>();
		try {
			Cursor mCursor =db.query(TABLE_APPOINMENTS_EMR, new String[] {
					PATIENT_ID ,
					APPOINTMENT_ID ,
					CREATED_ID,
					CREATED_AT,
					VITAL_VALUE,
					VITAL_UNIT,
					VITAL_GROUP,
					VITAL_KEY,
					PROCESS_FLAG},
					PATIENT_ID + "=? " +
							" AND " + APPOINTMENT_ID + "=?" ,
							new String[] {patientId,Integer.toString(appointmentId)},
							null,
							null,
							CREATED_AT + " DESC" ,
							null);
			if(mCursor == null || mCursor.getCount() <= 0){
				if(mCursor != null)
					mCursor.close();
				return modelList;
			}
			mCursor.moveToFirst();

			for(int counter = 0; counter < mCursor.getCount();counter++){
				AddEmrVitalsModel model = getModel(mCursor); 
				modelList.add(model);
				mCursor.moveToNext();
			}
			mCursor.close();
		} catch (Exception e) {			 
			e.printStackTrace();
		}
		return modelList;
	}
	public ArrayList<AddEmrVitalsModel> GetPatientEMRVital(String patientId, int appointmentId, String VitalGroup){
		ArrayList<AddEmrVitalsModel> modelList = new ArrayList<AddEmrVitalsModel>();
		try {
			Cursor mCursor =db.query(TABLE_APPOINMENTS_EMR, new String[] {
					PATIENT_ID ,
					APPOINTMENT_ID ,
					CREATED_ID,
					CREATED_AT,
					VITAL_VALUE,
					VITAL_UNIT,
					VITAL_GROUP,
					VITAL_KEY,
					PROCESS_FLAG},
					PATIENT_ID + "=? " +
							" AND " + APPOINTMENT_ID + "=?"+
							" AND " + VITAL_GROUP + "=?",
							new String[] {patientId,Integer.toString(appointmentId), VitalGroup},
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
				AddEmrVitalsModel model = getModel(mCursor); 
				modelList.add(model);
				mCursor.moveToNext();
			}
			mCursor.close();
		} catch (Exception e) {			 
			e.printStackTrace();
		}
		return modelList;
	}
	private AddEmrVitalsModel getModel(Cursor mCursor){
		AddEmrVitalsModel model = new AddEmrVitalsModel();
		try {
			model.setPatientId(mCursor.getString(0));	
			model.setAppointmentId(mCursor.getInt(1));				
			model.setCreatedById(mCursor.getString(2));				
			model.setCreatedAt(mCursor.getString(3));
			model.setVitalValue(mCursor.getString(4));
			model.setVitalUnit(mCursor.getString(5));
			model.setVitalGroup(mCursor.getString(6));
			model.setVitalKey(mCursor.getString(7)); 
			model.setProcessedFlag(mCursor.getInt(8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	private ContentValues gerContentValues(AddEmrVitalsModel model) {
		ContentValues initialValues = new ContentValues();
		try {
			initialValues.put(CREATED_ID, model.getCreatedById()); 	
			initialValues.put(VITAL_VALUE, model.getVitalValue()); 
			initialValues.put(VITAL_UNIT, model.getVitalUnit());	
			initialValues.put(VITAL_GROUP, model.getVitalGroup());	
			initialValues.put(VITAL_KEY, model.getVitalKey());				
			initialValues.put(PROCESS_FLAG, VTConstants.PROCESSED_FLAG_SENT_RESPONSE);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return initialValues;
	}
	public ArrayList<AddEmrVitalsModel> GetEMRVitalNotSent(){
		ArrayList<AddEmrVitalsModel> modelList = new ArrayList<AddEmrVitalsModel>();
		try {
			Cursor mCursor =db.query(TABLE_APPOINMENTS_EMR, new String[] {
					PATIENT_ID ,
					APPOINTMENT_ID ,
					CREATED_ID,
					CREATED_AT,
					VITAL_VALUE, 
					VITAL_UNIT,
					VITAL_GROUP,
					VITAL_KEY,
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
				AddEmrVitalsModel model = getModel(mCursor); 
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
	public int UpdateProcessFlagSent(AddEmrVitalsModel model,
			int processedFlag, String msgID) {
		int flag = 0;
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put(PROCESS_FLAG, processedFlag);
			initialValues.put(MSG_ID, msgID);

			flag = db.update(TABLE_APPOINMENTS_EMR, initialValues ,
					PATIENT_ID + "=? " +
							" AND " + APPOINTMENT_ID + "=?" +
							" AND " + CREATED_AT + "=?",
							new String[] {model.getPatientId(),Integer.toString(model.getAppointmentId()),	model.getCreatedAt()});

			LogTrace.w(TAG, flag + " EMR Updated : " + msgID + " Flag : " + processedFlag);
		} catch (Exception e) {
			e.printStackTrace();
			LogTrace.e(TAG, "Error updating emr");
		}
		return flag;
	}
	public int UpdateProcessFlagResponse(String msgID,int processedFlag) {
		int flag = 0;
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put(PROCESS_FLAG, processedFlag);

			flag = db.update(TABLE_APPOINMENTS_EMR, initialValues ,
					MSG_ID + " = \"" + msgID + "\"",null);

			LogTrace.w(TAG,  flag + " Update EMR Flag Res : "+ msgID + " flag : " + processedFlag);
		} catch (Exception e) {
			e.printStackTrace();
			LogTrace.e(TAG,  "Error updating EMR Response");
		}
		return flag;
	}
	public ArrayList<String> GetPatientEMRVitaleMax(String patientId, int appointmentId){
		
		ArrayList<String> arrayList = new ArrayList<String>();
		String maxDate = "";
		Cursor mCursor =db.query(TABLE_APPOINMENTS_EMR, new String[] {CREATED_AT},
				PATIENT_ID + " = ?" + " AND " + APPOINTMENT_ID +" =?",new String[] {patientId,String.valueOf(appointmentId)},null,null,null,null);
		if (mCursor != null)  
		
		if (mCursor.moveToFirst()) {  
            do {  
            	maxDate = mCursor.getString(mCursor.getColumnIndex(CREATED_AT));
            } while (mCursor.moveToNext());  
		}
		arrayList.add(maxDate);
		if(mCursor.getCount()== 0)
		{
			arrayList.clear();	
		}
		mCursor.close();
		
		return arrayList;
	}
	

	//DateFormat.GetFormattedDate_YYYYMMDD(model.getCreatedAt());
	public ArrayList<AddEmrVitalsModel> GetEMRVitalNotSentByDate(String patientId, int appointmentId, String date){
		ArrayList<AddEmrVitalsModel> modelList = new ArrayList<AddEmrVitalsModel>();
		try {

			//String date = DateFormat.GetFormattedDate_YYYYMMDD(createdAt);
			String fromdate =date + " " + "00:00";
			String todate =date + " " + "23:59";

			Cursor mCursor =db.query(TABLE_APPOINMENTS_EMR, new String[] {
					PATIENT_ID ,
					APPOINTMENT_ID ,
					CREATED_ID,
					CREATED_AT,
					VITAL_VALUE, 
					VITAL_UNIT,
					VITAL_GROUP,
					VITAL_KEY,
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
				AddEmrVitalsModel model = getModel(mCursor); 
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

	public void deleteRecordById(ArrayList<String> arrayList) {
		System.out.println("VITAL"+arrayList);

		try {
			for (int i = 0; i < arrayList.size(); i++) {
				db.delete(TABLE_APPOINMENTS_EMR, APPOINTMENT_ID + " = ?",
						new String[]{arrayList.get(i)});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
