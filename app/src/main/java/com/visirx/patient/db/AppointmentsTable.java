package com.visirx.patient.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AppointmentModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.CancelAppointmentModel;
import com.visirx.patient.model.CloseAppointmentModel;
import com.visirx.patient.model.FeeModel;
import com.visirx.patient.model.FindDoctorModel;

import com.visirx.patient.model.ParamedicDetailsModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.VTConstants;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aa on 21.1.16.
 */
public class AppointmentsTable {

    private static final String TAG = AppointmentsTable.class.getName();
    private SQLiteDatabase db;
    private Context context;
    SharedPreferences sharedPreferences;
    String CurrentDate;

    public AppointmentsTable(SQLiteDatabase mdb, Context context) {
        super();
        db = mdb;
        this.context = context;
    }

    // Appointment related details 17 Columns here..
    public static final String RESERVATION_NO = "reservation_no";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String STATUS = "status";
    public static final String ADDRESS = "address";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String SYMPTOMS = "symptoms";
    public static final String LAST_MODIFIED_TIME = "lastUpdatedBy";
    public static final String LAST_MODIFIED_USER = "lastUpdatedAt";
    // paramedic user details
    public static final String IS_NURSE_ASSIGNED = "isNurseAssigned";
    public static final String NURSE_LAST_UPDATED = "nurseLastUpdated";
    public static final String NURSE_ID = "nurseId";
    public static final String PERFORMER_ID = "performer_id";
    public static final String DOC_FEE = "doctorFee";
    public static final String VISIRX_FEES = "visirx_fees";
    public static final String TRANSATION_NO = "transation_no";
    public static final String ZIPCODE = "zipcode";
    public static final String APPOINTMENT_TYPE = "appointment_type";
    public static final String TABLE_APPOINMENTS = "appoinments";
    // create table
    static String PRIMARYKEY_REG = RESERVATION_NO;

    public static final String TABLE_APPTS_CREATE = " create table IF NOT EXISTS "
            + TABLE_APPOINMENTS
            + "("
            + CUSTOMER_ID
            + " text, "
            + STATUS
            + " integer, "
            + ADDRESS
            + " text , "
            + DATE
            + " text, "
            + TIME
            + " text, "
            + SYMPTOMS
            + " text, "
            + RESERVATION_NO
            + " integer, "
            + LAST_MODIFIED_TIME
            + " text, "
            + LAST_MODIFIED_USER
            + " text, "
            + PERFORMER_ID
            + " text, "
            + IS_NURSE_ASSIGNED
            + " integer, "
            + NURSE_ID
            + " text, "
            + NURSE_LAST_UPDATED
            + " text, "
            + DOC_FEE
            + " integer, "
            + VISIRX_FEES
            + " integer, "
            + TRANSATION_NO
            + " text, "
            + ZIPCODE
            + " text, "
            + APPOINTMENT_TYPE
            + " text, "
            + "primary key("
            + PRIMARYKEY_REG
            + ")" + ");";


    public long insertAppointment(String doctorId,
                                  String date, String time, int appointmentId,
                                  String PatientID, String Symptoms,
                                  String appointmentType, String appointmentStatus) {
        long flag = 0;
        try {
            ContentValues initialValues = gerContentValues(doctorId, date, time, appointmentId, PatientID, Symptoms, appointmentType, appointmentStatus);
            Cursor mCursor = db.query(TABLE_APPOINMENTS, new String[]{RESERVATION_NO}, RESERVATION_NO + " = " + appointmentId, null, null, null, null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }
                flag = db.insert(TABLE_APPOINMENTS, null, initialValues);
                LogTrace.w(TAG, flag + " Insert Appts : " + appointmentId);

            } else {
                flag = db.update(TABLE_APPOINMENTS, initialValues, RESERVATION_NO + "=? ", new String[]{Integer.toString(appointmentId)});
                LogTrace.w(TAG, flag + " Users Appts updated : " + appointmentId);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error insertAppointment");
        }
        return flag;
    }

    public long insertAllAppointment(List<AppointmentPatientModel> appointmentPatientModels1)
    {
        long flag = 0;
        try {
            for (int i = 0; i < appointmentPatientModels1.size(); i++) {
                AppointmentPatientModel appointmentPatientModel = appointmentPatientModels1.get(i);
                ContentValues initialValues = gerContentValues(appointmentPatientModel);
                Cursor mCursor = db.query(TABLE_APPOINMENTS, new String[]{RESERVATION_NO}, RESERVATION_NO + " = " + appointmentPatientModel.getReservationNumber(), null, null, null, null);
                if (mCursor == null || mCursor.getCount() <= 0)
                {
                    if (mCursor != null) {
                        mCursor.close();
                    }
                    initialValues.put(RESERVATION_NO, appointmentPatientModel.getReservationNumber());
                    flag = db.insert(TABLE_APPOINMENTS, null, initialValues);

                }
                else {
                    flag = db.update(TABLE_APPOINMENTS, initialValues, RESERVATION_NO + "=? ", new String[]{Integer.toString(appointmentPatientModel.getReservationNumber())});

                }
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error insertAllAppointment");
        }
        return flag;
    }

    public ArrayList<AppointmentPatientModel> getAllappt() {
        ArrayList<AppointmentPatientModel> List = new ArrayList<AppointmentPatientModel>();
        try {
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            String UserID = sharedPreferences.getString(VTConstants.USER_ID, null);
            Cursor cursor = db.query(TABLE_APPOINMENTS, new String[]{
                            PERFORMER_ID, RESERVATION_NO
                            , DATE, TIME, DOC_FEE
                            , STATUS, APPOINTMENT_TYPE},
                    CUSTOMER_ID + "=? " + " AND " + STATUS + "=?",
                    new String[]{UserID, "Scheduled"},
                    null,
                    null,
                    " CAST(" + RESERVATION_NO + " as integer) DESC ",
                    null);
            if (cursor == null || cursor.getCount() <= 0) {
                if (cursor != null)
                    cursor.close();
                return List;
            }
            cursor.moveToFirst();
            for (int counter = 0; counter < cursor.getCount(); counter++) {

                AppointmentPatientModel appointmentModel = new AppointmentPatientModel();
                appointmentModel.setPerfomerid(cursor.getString(cursor.getColumnIndex(PERFORMER_ID)));
                appointmentModel.setReservationNumber(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RESERVATION_NO))));
                appointmentModel.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                appointmentModel.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                appointmentModel.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                appointmentModel.setAppointmentType(cursor.getString(cursor.getColumnIndex(APPOINTMENT_TYPE)));
                List.add(appointmentModel);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error getAllappt");
        }
        return List;
    }

    public ArrayList<AppointmentPatientModel> getAppointmentByDoctor(String DoctorId) {

        ArrayList<AppointmentPatientModel> List = new ArrayList<AppointmentPatientModel>();
        try {
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            String UserID = sharedPreferences.getString(VTConstants.USER_ID, null);
            System.out.println("DoctorId  :    "+DoctorId  +"UserID  :"+UserID);
            String Doctor= "select performer_id, reservation_no,date,time,status,appointment_type from appoinments where performer_id "+"='"+ DoctorId +"' and "+"customer_id "+" ='"+ UserID +"'";
              Cursor cursor = db.rawQuery(Doctor,null);
            if (cursor == null || cursor.getCount() <= 0) {
                if (cursor != null)
                    cursor.close();
                return List;
            }
            cursor.moveToFirst();
            for (int counter = 0; counter < cursor.getCount(); counter++) {

                AppointmentPatientModel appointmentModel = new AppointmentPatientModel();
                appointmentModel.setPerfomerid(cursor.getString(cursor.getColumnIndex(PERFORMER_ID)));
                appointmentModel.setReservationNumber(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RESERVATION_NO))));
                appointmentModel.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                appointmentModel.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                appointmentModel.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                appointmentModel.setAppointmentType(cursor.getString(cursor.getColumnIndex(APPOINTMENT_TYPE)));
                List.add(appointmentModel);
                cursor.moveToNext();
           System.out.println("DoctorId  getCount :"+cursor.getCount());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error getAppointmentByDoctor");
        }
        return List;
    }

    public ArrayList<AppointmentPatientModel> getCancelCompletedappint(String UserId) {
        ArrayList<AppointmentPatientModel> List = new ArrayList<AppointmentPatientModel>();
        try {

            Cursor cursor = db.query(TABLE_APPOINMENTS, new String[]{
                            PERFORMER_ID, RESERVATION_NO
                            , DATE, TIME, DOC_FEE
                            , STATUS, APPOINTMENT_TYPE},
                    CUSTOMER_ID + "=? " + " AND ( " + STATUS + "=?" + " OR " + STATUS + "=? )",
                    new String[]{UserId, "Cancelled", "Completed"},
                    null,
                    null,
                    " CAST(" + RESERVATION_NO + " as integer) DESC ",
                    null);

            if (cursor == null || cursor.getCount() <= 0) {
                if (cursor != null)
                    cursor.close();
                return List;
            }
            cursor.moveToFirst();
            for (int counter = 0; counter < cursor.getCount(); counter++) {
                AppointmentPatientModel appointmentModel = new AppointmentPatientModel();
                appointmentModel.setPerfomerid(cursor.getString(cursor.getColumnIndex(PERFORMER_ID)));
                appointmentModel.setReservationNumber(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RESERVATION_NO))));
                appointmentModel.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                appointmentModel.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                appointmentModel.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                appointmentModel.setAppointmentType(cursor.getString(cursor.getColumnIndex(APPOINTMENT_TYPE)));
                List.add(appointmentModel);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error getAllappt");
        }
        return List;
    }


    public AppointmentPatientModel GetAppointmentsByID(int reservationNumber)
    {
        AppointmentPatientModel model = new AppointmentPatientModel();
        try {
            Cursor mCursor = db.query(TABLE_APPOINMENTS, new String[]{
                            PERFORMER_ID, STATUS, ADDRESS, DATE, APPOINTMENT_TYPE,
                            TIME, SYMPTOMS, RESERVATION_NO,
                            IS_NURSE_ASSIGNED, NURSE_ID, DOC_FEE,CUSTOMER_ID},
                    RESERVATION_NO + " = " + reservationNumber, null, null, null
                    , "CASE " + "WHEN " + STATUS + "= 'Ready' THEN 1 "
                            + "WHEN " + STATUS + "= 'Scheduled' THEN 2 "
                            + "WHEN " + STATUS + "= 'Cancelled' THEN 3 "
                            + "WHEN " + STATUS + "= 'Closed' THEN 4 "
                            + "ELSE 5 END," + TIME);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return model;
            }
            mCursor.moveToFirst();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                model = getModelFromCurser(mCursor);
                mCursor.moveToNext();
            }
            mCursor.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return model;
    }


    private AppointmentPatientModel getModelFromCurser(Cursor mCursor) {
        AppointmentPatientModel model = new AppointmentPatientModel();
        FindDoctorModel findDoctorModel = new FindDoctorModel();
        model.setPerfomerid(mCursor.getString(mCursor.getColumnIndex(PERFORMER_ID)));
        model.setStatus(mCursor.getString(mCursor.getColumnIndex(STATUS)));
        model.setAddress(mCursor.getString(mCursor.getColumnIndex(ADDRESS)));
        model.setDate(mCursor.getString(mCursor.getColumnIndex(DATE)));
        model.setTime(mCursor.getString(mCursor.getColumnIndex(TIME)));
        model.setSymptoms(mCursor.getString(mCursor.getColumnIndex(SYMPTOMS)));
        model.setReservationNumber(mCursor.getInt(mCursor.getColumnIndex(RESERVATION_NO)));
        model.setAppointmentType(mCursor.getString(mCursor.getColumnIndex(APPOINTMENT_TYPE)));
        model.setDoctorfee(mCursor.getColumnIndex(DOC_FEE));
        findDoctorModel = VisirxApplication.customerDAO.GetCustomerDetailsForID(mCursor.getString(mCursor.getColumnIndex(PERFORMER_ID)));
        if (findDoctorModel != null) {
            model.setFirstName(findDoctorModel.getDoctorFirstName());
            model.setLastName(findDoctorModel.getDoctorLastName());
            model.setDoctorDescription(findDoctorModel.getDoctorDescription());
            model.setDoctorSpecialization(findDoctorModel.getDoctorSpecialization());
            model.setCustomerImageThumbnailPath(findDoctorModel.getCustomerImageThumbnailPath());
        }
        return model;
    }

    private ContentValues gerContentValues(String doctorId, String date, String time, int appointmentId
            , String patientId, String Symptoms, String appointmentType, String appointmentStatus) {
        ContentValues initialValues = new ContentValues();
        try {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String currentTime = ft.format(dNow);
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            String UserID = sharedPreferences.getString(VTConstants.USER_ID, null);
            initialValues.put(CUSTOMER_ID, patientId);
            initialValues.put(DATE, date);
            initialValues.put(TIME, time);
            initialValues.put(PERFORMER_ID, doctorId);
            initialValues.put(SYMPTOMS, Symptoms);
            initialValues.put(APPOINTMENT_TYPE, appointmentType);
            initialValues.put(RESERVATION_NO, appointmentId);
            initialValues.put(LAST_MODIFIED_USER, UserID);
            initialValues.put(LAST_MODIFIED_TIME, currentTime);
            initialValues.put(STATUS, appointmentStatus);
            initialValues.put(APPOINTMENT_TYPE, appointmentType);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }

    private ContentValues gerContentValues(AppointmentPatientModel model) {
        ContentValues initialValues = new ContentValues();
        try {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String currentTime = ft.format(dNow);
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            String UserID = sharedPreferences.getString(VTConstants.USER_ID, null);
            initialValues.put(CUSTOMER_ID, UserID);
            initialValues.put(DATE, model.getDate());
            initialValues.put(TIME, model.getTime());
            initialValues.put(PERFORMER_ID, model.getPerfomerid());
            initialValues.put(SYMPTOMS, model.getSymptoms());
            initialValues.put(APPOINTMENT_TYPE, model.getAppointmentType());
            initialValues.put(RESERVATION_NO, model.getReservationNumber());
            initialValues.put(LAST_MODIFIED_USER, UserID);
            initialValues.put(LAST_MODIFIED_TIME, currentTime);
            initialValues.put(STATUS, model.getStatus());
            initialValues.put(ADDRESS, model.getAddress());
            initialValues.put(DOC_FEE, String.valueOf(model.getDoctorfee()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }

    public long cancelAppointment(int reservationId) {
        int flag = 0;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(STATUS, VTConstants.APPT_CANCELLED);
            initialValues.put(LAST_MODIFIED_TIME, DateFormat.getDateStr(new Date()));
            flag = db.update(TABLE_APPOINMENTS, initialValues,
                    RESERVATION_NO + "=?"
                    , new String[]{Integer.toString(reservationId)});
            Log.d("TRIGER ", "SUCEESS");
        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e("TRIGER ", "Failure");
        }
        return flag;
    }

    public ArrayList<CancelAppointmentModel> GetCanceledAppointments(int appointmentId) {
        ArrayList<CancelAppointmentModel> modelList = new ArrayList<CancelAppointmentModel>();
        try {
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            String userId = sharedPreferences.getString(VTConstants.USER_ID, null);
            Cursor mCursor = db.query(TABLE_APPOINMENTS,
                    new String[]{CUSTOMER_ID, RESERVATION_NO, LAST_MODIFIED_TIME, LAST_MODIFIED_USER},
                    RESERVATION_NO + "=?", new String[]{Integer.toString(appointmentId)}, null, null, null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return modelList;
            }
            mCursor.moveToFirst();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                CancelAppointmentModel model = new CancelAppointmentModel();
                model.setCustomerId(userId);
                model.setPerformerId(userId);
                model.setReservationNumber(mCursor.getInt(mCursor.getColumnIndex(RESERVATION_NO)));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date date = new Date();
                CurrentDate = dateFormat.format(date);
                model.setLastUpdatedAt(CurrentDate);
                model.setLastUpdatedBy(userId);
                modelList.add(model);
                mCursor.moveToNext();
            }
            mCursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error GetCanceledAppointments");
        }
        return modelList;
    }

    public ArrayList<FeeModel> GetDocFeeAppointments(int reservationNumber) {
        ArrayList<FeeModel> modelList = new ArrayList<FeeModel>();
        try {
            Cursor mCursor = db.query(TABLE_APPOINMENTS, new String[]{
                    RESERVATION_NO, LAST_MODIFIED_TIME, LAST_MODIFIED_USER, DOC_FEE}
                    , RESERVATION_NO + " = \"" + reservationNumber + "\"", null, null, null, null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return modelList;
            }
            mCursor.moveToFirst();
            FeeModel model = new FeeModel();
            model.setAppointmentid(mCursor.getInt(0));
            model.setLastUpdatedAt(mCursor.getString(1));
            model.setLastUpdatedBy(mCursor.getString(2));
            model.setDoctorFee(mCursor.getString(3));
            modelList.add(model);
            mCursor.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, "Error GetDocFeeAppointments");
        }
        return modelList;
    }

    public ArrayList<CloseAppointmentModel> GetCompletedAppointments(int appointmentId) {
        ArrayList<CloseAppointmentModel> modelList = new ArrayList<CloseAppointmentModel>();
        try {
            Cursor mCursor = db.query(TABLE_APPOINMENTS, new String[]{
                            RESERVATION_NO, PERFORMER_ID, CUSTOMER_ID,
                            LAST_MODIFIED_USER, LAST_MODIFIED_TIME}
                    , STATUS + "=? " + " AND " + RESERVATION_NO + "=?",
                    new String[]{VTConstants.APPT_COMPLETE, Integer.toString(appointmentId)}, null, null, null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                return modelList;
            }
            mCursor.moveToFirst();
            for (int counter = 0; counter < mCursor.getCount(); counter++) {
                CloseAppointmentModel model = new CloseAppointmentModel();
                model.setAppointmentid(mCursor.getString(0));
                model.setPerformerId(mCursor.getString(1));
                model.setCustomerId(mCursor.getString(2));
                model.setLastUpdatedBy(mCursor.getString(3));
                model.setLastUpdatedAt(mCursor.getString(4));
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

    public void patientStatusUpdate(String status, int appointmentId)
    {
        ContentValues contentValues = new ContentValues();
        try {
            Log.d("CANCELLEDTESTING", "" + status);
            Log.d("CANCELLEDTESTING", "" + appointmentId);
            contentValues.put(STATUS, status);
            db.update(TABLE_APPOINMENTS, contentValues,
                    RESERVATION_NO + " = ?", new String[]{String.valueOf(appointmentId)});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean updateParamedicDetailsForAppt(ParamedicDetailsModel paramedicDetailsModel) {
        ContentValues contentValues = new ContentValues();
        contentValues = getParamedicContentValues(paramedicDetailsModel);
        boolean isUpdated = false;
        try {
            Cursor mCursor = db.query(TABLE_APPOINMENTS, new String[]{
                    RESERVATION_NO, STATUS}, RESERVATION_NO + " = "
                    + paramedicDetailsModel.getAppointmentId(), null, null, null, null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null) {
                    mCursor.close();
                }
                LogTrace.w(TAG, " updateParamedicDetailsForAppt : No row found for Appt ID : " + paramedicDetailsModel.getAppointmentId());

            } else {
                db.update(TABLE_APPOINMENTS, contentValues, RESERVATION_NO + "=? "
                        , new String[]{Integer.toString(paramedicDetailsModel.getAppointmentId())});
                LogTrace.w(TAG,
                        "Appointment updated  with new nurse details for id : "
                                + paramedicDetailsModel.getAppointmentId());
                if (paramedicDetailsModel.isNurseAssigned()) {
                    AppointmentModel apptModel = new AppointmentModel();
                    apptModel.setCustomerId(paramedicDetailsModel.getNurseId());
                    apptModel.setFirstName(paramedicDetailsModel.getNurseFirstName());
                    apptModel.setLastName(paramedicDetailsModel.getNurseLastName());
                    apptModel.setDobString(paramedicDetailsModel.getNurseDob());
                    apptModel.setGender(paramedicDetailsModel.getNurseGender());
                    apptModel.setMobileNumber(paramedicDetailsModel.getNurseMobileNumber());
                    apptModel.setBaseDataLastUpdated(paramedicDetailsModel.getNurseDataLastUpdated());
                } else {
                    Log.d("SPIN", "Nurse not Assigned : ");
                }
                return isUpdated;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    private ContentValues getParamedicContentValues(ParamedicDetailsModel model) {
        ContentValues initialValues = new ContentValues();
        try {
            initialValues.put(IS_NURSE_ASSIGNED, model.isNurseAssigned() ? 1 : 0);
            initialValues.put(NURSE_ID, model.getNurseId());
            initialValues.put(NURSE_LAST_UPDATED, model.getLastUpdatedTimestamp());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return initialValues;
    }

    public ParamedicDetailsModel GetParamedicDetailsByID(int reservationNumber) {
        ParamedicDetailsModel paraModel = new ParamedicDetailsModel();
        AppointmentModel apptModel = null;
        try {
            Cursor mCursor = db.query(TABLE_APPOINMENTS, new String[]{
                            IS_NURSE_ASSIGNED, NURSE_ID, NURSE_LAST_UPDATED}, RESERVATION_NO + "=? ",
                    new String[]{Integer.toString(reservationNumber)}, null,
                    null, null);
            if (mCursor == null || mCursor.getCount() <= 0) {
                if (mCursor != null)
                    mCursor.close();
                LogTrace.w(TAG,
                        "Inside GetParamedicDetailsByID : 0 rows fetched for apppt id : "
                                + reservationNumber);
                return null;
            }
            mCursor.moveToFirst();
            if (mCursor.getCount() == 1) {
                paraModel = getModelParamedicFromCurser(mCursor);
                if (paraModel.isNurseAssigned()) {
                    if (apptModel != null) {
                        paraModel.setNurseFirstName(apptModel.getFirstName());
                        paraModel.setNurseLastName(apptModel.getLastName());
                        paraModel.setNurseAge(apptModel.getAge());
                        paraModel.setNurseGender(apptModel.getGender());
                        paraModel.setNurseMobileNumber(apptModel.getMobileNumber());
                        paraModel.setNurseThumbImgPath(apptModel.getCustomerImageThumbnailPath());
                    }
                }

            }
            mCursor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return paraModel;
    }

    private ParamedicDetailsModel getModelParamedicFromCurser(Cursor mCursor) {
        ParamedicDetailsModel model = new ParamedicDetailsModel();
        boolean isAssigned = false;
        if (mCursor.getInt(0) == 1) {
            isAssigned = true;
        }
        model.setIsNurseAssigned(isAssigned);
        model.setNurseId(mCursor.getString(1));
        model.setLastUpdatedTimestamp((mCursor.getString(2)));
        return model;
    }

    public void deleteappointment(String doctorID,String patientId,String appointmentid)
    {
        Log.d("APPOINTMENTABLE"," DELETE:"+doctorID);
        Log.d("APPOINTMENTABLE", " DELETE:" + patientId);
        Log.d("APPOINTMENTABLE", " DELETE:" + appointmentid);
//        int value = db.delete(TABLE_APPOINMENTS, PERFORMER_ID + "=? " + " AND " + CUSTOMER_ID + "=?" + " AND " + RESERVATION_NO + "=?", new String[] {doctorID,patientId,appointmentid });
//        Log.d("NEWALERCHECK", " DELETE:" + value);
    }

}
