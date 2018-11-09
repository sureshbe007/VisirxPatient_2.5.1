package com.visirx.patient.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.crashlytics.android.answers.SignUpEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Suresh on 29-04-2016.
 */
public class EventChecking {

    static String TAG = EventChecking.class.getName();
    private  Context context;
   private static SharedPreferences sharedPreferences;

    //   private static String PatientID;
    public EventChecking(Context context) {
        super();
        this.context = context;


    }

    interface Screens {
        String MY_LOGIN = "Login";
        String MY_LOGOUT = "Logout";
        String MY_SIGNUP = "Sign Up";
        String MY_TOTALAPPOINT = "Total Appointments";
        String ALL_DOCTORS = "CareTeam doctors";
        String MY_CANCELAPPOINTMENT = "Canceled Appointments";
        String SEARCH_DOCTOR = "Search doctors";
        String ADDED_DOCTORS = "Added doctors";
        String DELETED_DOCTORS = "Deleted doctors";
        String BOOK_APPOINTMENT = "Book appointment";
        String PAYMENT_SUCCESS = "Appointmnet success";
        String EMR_OFFLINE_SUCCESS = "Emr offline success";
        String EMR_ONLINE_SUCCESS = "Emr online success";
        String PRESCRIPTION_SUCCESS = "Prescription success";

    }

    interface SignupKeys {
        String USER_ID = "User Id";
        String STATUS = "Status";
        String DATE = "Date";
        String TIME = "Time";
    }

    interface LoginKeys {
        String USER_ID = "User Id";
        String DATE = "Date";
        String TIME = "Time";
        String STATUS = "Status";
        String NAME = "name";
    }

    interface LogoutKeys {
        String USER_ID = "User Id";
        String NAME = "name";
        String STATUS = "Status";
        String DATE = "Date";
        String TIME = "Time";


    }

    interface Allappointments {
        String USER_ID = "User Id";
        String DOCTOR_ID = "doctor_id";
        String APPOINTMENT_ID = "appointment_id";
        String ALLAPPOINT_STATUS = "allappointment_status";
        String TOTAL_APPOINTMENTS = "total appointments";
        String DATE = "Date";
        String TIME = "Time";

    }

    interface AlldoctorsKeys {
        String USER_ID = "User Id";
        String DOCTOR_ID = "doctor_id";
        String DOCTOR_NAME = "doctor_name";
        String TOTAL_DOCTOR = "total_doctor";
        String DATE = "Date";
        String TIME = "Time";

    }

    interface CanceledAppointsKeys {
        String USER_ID = "User Id";
        String DOCTOR_ID = "doctor_id";
        String APPOINTMENT_ID = "appointment_id";
        String CANCELED_APPOINT_STATUS = "canceled appointment Status";
        String TOTAL_APPOINTMENTS = "total appointments";
        String DATE = "Date";
        String TIME = "Time";

    }

    interface SearchDoctorKeys {
        String USER_ID = "User Id";
        String DOCTOR_ID = "Doctor id";
        String DOCTOR_NAME = "Doctor name";
        String DATE = "Date";
        String TIME = "Time";

    }

    interface AddedDoctorsKeys {
        String USER_ID = "User Id";
        String DOCTOR_ID = "Doctor id";
        String DOCTOR_NAME = "Doctor name";
        String DATE = "Date";
        String TIME = "Time";

    }

    interface DeletedDoctorsKeys {
        String USER_ID = "User Id";
        String DOCTOR_ID = "Doctor id";
        String DOCTOR_NAME = "Doctor name";
        String DATE = "Date";
        String TIME = "Time";

    }

    interface AppointmentsKeys {
        String USER_ID = "User Id";
        String DOCTOR_ID = "Doctor id";
        String DOCTOR_NAME = "Doctor name";
        String APPOINTMENT_DATE = "Appointment date";
        String APPOINTMENT_TIME = "Appointment time";

    }

    interface PaymentSuccessKeys {
        String USER_ID = "User Id";
        String APPOINTMENT_ID = "Appointment id";
        String TRANSATION_ID = "Transaction id";
        String TOTAL_AMOUNT = "Total amount";
        String DATE = "Date";
        String TIME = "Time";

    }

    interface EmrOfflineKeys {
        String USER_ID = "User id";
        String APPOINTMENT_ID = "Appointment id";
        String PATIENT_NAME = "Patient name";
        String IMAGE_NAME = "Image name";
        String IMAGE_STATUS = "Image status";
        String DATE = "Date";
        String TIME = "Time";

    }

    interface EmrOnlineKeys {
        String USER_ID = "User_Id";
        String APPOINTMENT_ID = "Appointment_id";
        String PATIENT_NAME = "Patient_name";
        String IMAGE_NAME = "Image_name";
        String IMAGE_STATUS = "Image Status";
        String DATE = "Date";
        String TIME = "Time";


    }

    interface PrescriptionKeys {
        String USER_ID = "User Id";
        String DOCTOR_NAME = "Doctor name";
        String IMAGE_NAME = "Image name";
        String IMAGE_STATUS = "Image status";
        String DATE = "Date";
        String TIME = "Time";

    }

    // CustomLoginStatus
    public static void myLogin(String UserID, String Name, boolean status) {

        try {

            System.out.println("myLogin UserID :   " + UserID + "   Usuer Name :   " + Name + "  Status :  " + status);
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            Answers.getInstance().logCustom(new CustomEvent(Screens.MY_LOGIN)
                            .putCustomAttribute(LoginKeys.USER_ID, UserID)
                            .putCustomAttribute(LoginKeys.NAME, Name)
                            .putCustomAttribute(LoginKeys.DATE, strDate)
                            .putCustomAttribute(LoginKeys.TIME, sdf.format(now))
                            .putCustomAttribute(LoginKeys.STATUS, String.valueOf(status))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CustomLoginStatus
    public static void myLogout(String UserID, String Name, boolean status) {

        try {

            System.out.println("myLogout :   " + UserID + "   Usuer Name :   " + Name + "  Status :  " + status);
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            Answers.getInstance().logCustom(new CustomEvent(Screens.MY_LOGOUT)
                            .putCustomAttribute(LogoutKeys.USER_ID, UserID)
                            .putCustomAttribute(LogoutKeys.NAME, Name)
                            .putCustomAttribute(LogoutKeys.DATE, strDate)
                            .putCustomAttribute(LogoutKeys.TIME, sdf.format(now))
                            .putCustomAttribute(LogoutKeys.STATUS, String.valueOf(status))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Customer SignUp Status
    public static void mySignUp(String UserId, boolean status) {

        try {

            System.out.println("mySignUp   " + UserId +"   Status   " + status);
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);

            Answers.getInstance().logCustom(new CustomEvent(Screens.MY_SIGNUP)
                            .putCustomAttribute(SignupKeys.USER_ID, UserId)
                            .putCustomAttribute(SignupKeys.STATUS, String.valueOf(status))
                            .putCustomAttribute(SignupKeys.DATE, strDate)
                            .putCustomAttribute(SignupKeys.TIME, sdf.format(now))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Customer Totalappointment Status
    public static void myTotalappointments(String DoctorID, int appointmentID, String appointmentstatus,String totalAppointments,Context context) {

        try {

            System.out.println("myTotalappointments :  USER ID  " + VTConstants.getUserID(context) + "    DoctorID:   " + DoctorID + "   appointmentID : "
                    + appointmentID +"  appointmentstatus "+appointmentstatus);

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            Answers.getInstance().logCustom(new CustomEvent(Screens.MY_TOTALAPPOINT)
                            .putCustomAttribute(Allappointments.USER_ID, VTConstants.getUserID(context))
                            .putCustomAttribute(Allappointments.DOCTOR_ID, DoctorID)
                            .putCustomAttribute(Allappointments.APPOINTMENT_ID, String.valueOf(appointmentID))
                            .putCustomAttribute(Allappointments.ALLAPPOINT_STATUS, appointmentstatus)
                            .putCustomAttribute(Allappointments.TOTAL_APPOINTMENTS, totalAppointments)
                            .putCustomAttribute(Allappointments.DATE, strDate)
                            .putCustomAttribute(Allappointments.TIME, sdf.format(now))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // My Doctor List
    public static void myDoctors(String DoctorID, String DoctorName, int totalDoctors, Context context1)
    {

        try {

            System.out.println("myDoctors :::::::   USER ID : "+ VTConstants.getUserID(context1)+ "    DoctorID  :   " + DoctorID +
                    "   DoctorName : " + DoctorName + "totalDoctors : " + totalDoctors);
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            Answers.getInstance().logCustom(new CustomEvent(Screens.ALL_DOCTORS)
                            .putCustomAttribute(AlldoctorsKeys.USER_ID, VTConstants.getUserID(context1))
                            .putCustomAttribute(AlldoctorsKeys.DOCTOR_ID, DoctorID)
                            .putCustomAttribute(AlldoctorsKeys.DOCTOR_NAME, DoctorName)
                            .putCustomAttribute(AlldoctorsKeys.TOTAL_DOCTOR, String.valueOf(totalDoctors))
                            .putCustomAttribute(AlldoctorsKeys.DATE, strDate)
                            .putCustomAttribute(AlldoctorsKeys.TIME, sdf.format(now))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Canceled Appointments
    public static void canceledAppointments( String DoctorID, int appointmentID,String appointmentstatus ,String totalappointments ,Context context) {

        try {

            System.out.println("canceledAppointments :::::::  USER ID  " + VTConstants.getUserID(context) + "    DoctorName:   " + DoctorID +
                    "appointmentID    "+appointmentID +"   appointmentstatus : " + appointmentstatus+"totalappointments : "+totalappointments);

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            Answers.getInstance().logCustom(new CustomEvent(Screens.MY_CANCELAPPOINTMENT)
                            .putCustomAttribute(CanceledAppointsKeys.USER_ID, VTConstants.getUserID(context))
                            .putCustomAttribute(CanceledAppointsKeys.DOCTOR_ID, DoctorID)
                            .putCustomAttribute(CanceledAppointsKeys.APPOINTMENT_ID, appointmentID)
                            .putCustomAttribute(CanceledAppointsKeys.CANCELED_APPOINT_STATUS, appointmentstatus)
                            .putCustomAttribute(CanceledAppointsKeys.TOTAL_APPOINTMENTS, totalappointments)
                            .putCustomAttribute(CanceledAppointsKeys.DATE, strDate)
                            .putCustomAttribute(CanceledAppointsKeys.TIME, sdf.format(now))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Search  Doctors
    public static void SearchDoctor( String DoctorId, String DoctorName,Context context) {

        try {

            System.out.println("SearchDoctorList :::::::  UserId  " + VTConstants.getUserID(context) + "    DoctorId:   " + DoctorId +
                    "   DoctorName : " + DoctorName );
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            Answers.getInstance().logCustom(new CustomEvent(Screens.SEARCH_DOCTOR)
                            .putCustomAttribute(SearchDoctorKeys.USER_ID, VTConstants.getUserID(context))
                            .putCustomAttribute(SearchDoctorKeys.DOCTOR_ID, DoctorId)
                            .putCustomAttribute(SearchDoctorKeys.DOCTOR_NAME, DoctorName)
                            .putCustomAttribute(SearchDoctorKeys.DATE, strDate)
                            .putCustomAttribute(SearchDoctorKeys.TIME, sdf.format(now))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Search  Doctors
    public static void addedDoctor( String DoctorId, String DoctorName,Context context) {

        try {

            System.out.println("addedDoctorslist :::::::  UserId  " + VTConstants.getUserID(context) + "    DoctorId:   " + DoctorId +
                    "   DoctorName : " + DoctorName );
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            Answers.getInstance().logCustom(new CustomEvent(Screens.ADDED_DOCTORS)
                            .putCustomAttribute(AddedDoctorsKeys.USER_ID, VTConstants.getUserID(context))
                            .putCustomAttribute(AddedDoctorsKeys.DOCTOR_ID, DoctorId)
                            .putCustomAttribute(AddedDoctorsKeys.DOCTOR_NAME, DoctorName)
                            .putCustomAttribute(AddedDoctorsKeys.DATE, strDate)
                            .putCustomAttribute(AddedDoctorsKeys.TIME, sdf.format(now))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Search  Doctors
    public static void deletedDoctors( String DoctorId, String DoctorName,Context context) {

        try {

            System.out.println("deletedDoctorsLIST :::::::  UserId  " + VTConstants.getUserID(context) + "    DoctorId:   " + DoctorId +
                    "   DoctorName : " + DoctorName );
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            Answers.getInstance().logCustom(new CustomEvent(Screens.DELETED_DOCTORS)
                            .putCustomAttribute(DeletedDoctorsKeys.USER_ID, VTConstants.getUserID(context))
                            .putCustomAttribute(DeletedDoctorsKeys.DOCTOR_ID, DoctorId)
                            .putCustomAttribute(DeletedDoctorsKeys.DOCTOR_NAME, DoctorName)
                            .putCustomAttribute(DeletedDoctorsKeys.DATE, strDate)
                            .putCustomAttribute(DeletedDoctorsKeys.TIME, sdf.format(now))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Current  Appointments
    public static void currentAppointments(String DoctorId, String DoctorName,String appointmentDate, String appointmentTime,Context context) {

        try {

            System.out.println("currentAppointments :::::::   USER ID " + VTConstants.getUserID(context) + "    DoctorName:   " + DoctorName + "    DoctorId :   " + DoctorId +
                    "  Date :  " + appointmentDate + "time:  " + appointmentTime);

            Answers.getInstance().logCustom(new CustomEvent(Screens.BOOK_APPOINTMENT)
                            .putCustomAttribute(AppointmentsKeys.USER_ID, VTConstants.getUserID(context))
                            .putCustomAttribute(AppointmentsKeys.DOCTOR_ID, DoctorId)
                            .putCustomAttribute(AppointmentsKeys.DOCTOR_NAME, DoctorName)
                            .putCustomAttribute(AppointmentsKeys.APPOINTMENT_DATE, appointmentDate)
                            .putCustomAttribute(AppointmentsKeys.APPOINTMENT_TIME, appointmentTime)
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Current  Appointments
    public static void appointmentSuccess(String appointmentID,String transactionID,String Totalamount, Context context) {

        try {

            System.out.println("appointmentSuccess :::::::   USER ID : " + VTConstants.getUserID(context)+" appointmentID : "+ appointmentID+" transactionID :  "+ transactionID );
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);

            Answers.getInstance().logCustom(new CustomEvent(Screens.PAYMENT_SUCCESS)
                            .putCustomAttribute(PaymentSuccessKeys.USER_ID, VTConstants.getUserID(context))
                            .putCustomAttribute(PaymentSuccessKeys.APPOINTMENT_ID, appointmentID)
                            .putCustomAttribute(PaymentSuccessKeys.TRANSATION_ID, transactionID)
                            .putCustomAttribute(PaymentSuccessKeys.TOTAL_AMOUNT, strDate)
                            .putCustomAttribute(PaymentSuccessKeys.DATE, Totalamount)
                            .putCustomAttribute(PaymentSuccessKeys.TIME, sdf.format(now))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EMR Image success Offline
    public static void emrImageOffline(String appointmentID, String patientName,String imageName, String status,Context context) {

        try {


            System.out.println("emrImageOffline :::::::   USER ID : " + VTConstants.getUserID(context) +"   imageName :  "+ imageName
            +"status :"+  status);
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);

            Answers.getInstance().logCustom(new CustomEvent(Screens.EMR_OFFLINE_SUCCESS)
                            .putCustomAttribute(EmrOfflineKeys.USER_ID, VTConstants.getUserID(context))
                            .putCustomAttribute(EmrOfflineKeys.APPOINTMENT_ID, appointmentID)
                            .putCustomAttribute(EmrOfflineKeys.PATIENT_NAME, patientName)
                            .putCustomAttribute(EmrOfflineKeys.IMAGE_NAME, imageName)
                            .putCustomAttribute(EmrOfflineKeys.IMAGE_STATUS, status)
                            .putCustomAttribute(EmrOfflineKeys.DATE, strDate)
                            .putCustomAttribute(EmrOfflineKeys.TIME, sdf.format(now))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EMR Image success Online
    public static void emrImageOnline(String AppointmentID,String imageName, String status,Context context) {

        try {

            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
              String UerfullName=sharedPreferences.getString(VTConstants.LOGGED_USER_FULLNAME,null);
            System.out.println("emrImageOnline :::::::   USER ID : " + VTConstants.getUserID(context)+" AppointmentID  : "+ AppointmentID +" imageName :  "+ imageName
            +"status:  "+ status+"UerfullName : "+UerfullName);
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);

            Answers.getInstance().logCustom(new CustomEvent(Screens.EMR_ONLINE_SUCCESS)
                            .putCustomAttribute(EmrOnlineKeys.USER_ID, VTConstants.getUserID(context))
                            .putCustomAttribute(EmrOfflineKeys.PATIENT_NAME, UerfullName)
                            .putCustomAttribute(EmrOfflineKeys.APPOINTMENT_ID, AppointmentID)
                            .putCustomAttribute(EmrOnlineKeys.IMAGE_NAME, imageName)
                            .putCustomAttribute(EmrOnlineKeys.IMAGE_STATUS, status)
                            .putCustomAttribute(EmrOnlineKeys.DATE, strDate)
                            .putCustomAttribute(EmrOnlineKeys.TIME, sdf.format(now))
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EMR Image success Online
    public static void prescription(String DoctorName,String imageName, String status,Context context) {

        try {

            System.out.println("prescription  :::::::   USER ID : " + VTConstants.getUserID(context)+" DoctorName  : "+ DoctorName +" imageName :  "+ imageName
                    +"status:  "+ status);
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);

//            Answers.getInstance().logCustom(new CustomEvent(Screens.PRESCRIPTION_SUCCESS)
//                            .putCustomAttribute(PrescriptionKeys.USER_ID, VTConstants.getUserID(context))
//                            .putCustomAttribute(PrescriptionKeys.DOCTOR_NAME, DoctorName)
//                            .putCustomAttribute(PrescriptionKeys.IMAGE_NAME, imageName)
//                            .putCustomAttribute(PrescriptionKeys.IMAGE_STATUS, status)
//                            .putCustomAttribute(PrescriptionKeys.DATE, strDate)
//                            .putCustomAttribute(PrescriptionKeys.TIME, sdf.format(now))
//            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
