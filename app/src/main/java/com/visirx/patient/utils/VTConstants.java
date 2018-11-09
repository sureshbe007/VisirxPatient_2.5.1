package com.visirx.patient.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.FindDoctorModel;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aa on 19.1.16.
 */
public class VTConstants {
    static String TAG = VTConstants.class.getName();

    //   DEVELOPMENT  NEW  GCM SENDERID
    public static final String SENDER_ID = "234629699742";
    public static String protocol_version = "1";
//    public static final String REGISTER_PREFRENCES = "register_remember";


    //to avoid the issue with the space when sending timestamp through GET method
    public static String replaceWhiteSpace(String createdAt) {

        createdAt = createdAt.replaceAll(" ", "_");
        createdAt = createdAt.replaceAll(":", "_");
        createdAt = createdAt.replaceAll("&", "_");
        createdAt = createdAt.replaceAll("@", "_");
        createdAt = createdAt.replaceAll("#", "_");
        createdAt = createdAt.replaceAll("=", "_");
        createdAt = createdAt.replaceAll("~", "_");
        return createdAt;
    }

    public static String CALL_TYPE_AUDIO = "audio";
    public static String CALL_TYPE_VIDEO = "video";

    public static int EMR_THUMBNAIL_DWNLD_COUNT = 1;
    public static int EHD_THUMBNAIL_DWNLD_COUNT = 1;
    public static int PRES_THUMBNAIL_DWNLD_COUNT = 1;
    public static String PROVIDER_RESPONSEHEADER = "responseHeader";
    public static String PROVIDER_REPONSECODE = "responseCode";
    public static String PROVIDER_RESPONSEMESSAGE = "responseMessage";
    public static final String IMAGE_MIME_FORMAT = "jpeg gif bmp png jpg";
    public static final String FILE_MIME_FORMAT = "pdf docx txt xlsx";
    public static final String PDF_FILE_FORMAT = "pdf";
    public static final String DOC_FILE_FORMAT = "docx";
    public static final String TXT_FILE_FORMAT = "txt";
    public static final String XLSX_FILE_FORMAT = "xlsx";
    public static final String SUPPORT_FILE_FORMAT = "jpeg gif bmp png jpg pdf docx txt xlsx";
    public static String PROVIDER_CREATEDAT = "createdAt";
    public static String PROVIDER_PATIENTID = "patientId";
    public static String PROVIDER_APPOINTMETID = "appointmentId";
    public static String PROVIDER_CREATEDBYID = "createdById";
    public static String PROVIDER_FILEGROUP = "fileGroup";
    public static String PROVIDER_FILELABEL = "fileLabel";
    public static String PROVIDER_FILEMIMETYPE = "fileMimeType";
    public static String PROVIDER_FILETYPE = "fileType";
    public static String PROVIDER_USERID = "userId";

    public static String PROVIDER_EMR_REQUESTACTION = "EMR_FILE";
    public static String PROVIDER_PRES_REQUESTACTION = "PRESCRIPTION_FILE";
    public static String PROVIDER_REQUESTACTION = "requestAction";

    // SD CardPermission
    // SD CardPermission
    public static final int REQUEST_WRITE_STORAGE = 112;
    public static final String READ_SMS = "android.permission.READ_SMS";
    public static final String SD_CARD_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final String LOGIN_COUNT = "login_count";
    public static final String PROFILE_STATUS = "profile_status";
    public static final String LOG_OUT = "logout";
    public static final String OTP_STATS = "otp_status";
    public static final String TERMS_CONDTIONS = "terms_condtions";

    // For Foloder Creation
    public static final String FILESEPARATOR = File.separator;
    public static final String ROOTFOLDER = "VISIRX";
    public static final String PROFILE_FOLDER = "PROFILE";
    public static final String THUMBNAIL_FOLDER = "THUMBNAIL";
    public static final String SUBFOLDER = "PRESCRIPTION";
    public static String ALLAPPOINTMENT_LIST = null;
    public static List<FindDoctorModel> findDoctorModels;
    //servlet name - which returns images
    public static final String GET_IMAGE_SERVLET = "GetProfileImageForUserServlet";
    public static final String GET_PRESCRIPTION_IMAGE_SERVLET = "GetPrescriptionImageServlet";
    public static final String GET_IMAGE_USERID = "userid";
    public static final String GET_IMAGE_TYPE = "imagetype";

    //image quality decesion parameters
    public static final String THUMBNAIL_FLAG = "THUMBNAIL";
    public static final String FULL_IMAGE_FLAG = "FULL_IMAGE";

    public static File file = null;
    public static String PRESCRIPTIONIMAHENAME = "image_name";

    public static final String NOTIFICATION_SERCHDOCTOR = "notification_searchdoctor";
    public static final String APPT_READY = "Ready";
    public static final String APPT_SCHEDULED = "Scheduled";
    public static final String APPT_NOSHOW = "No show";
    public static final String APPT_CANCELLED = "Cancelled";
    public static final String APPT_COMPLETE = "Completed";

    // Tab Posstion from addDcotor to all doctorList
    public static final String TAB_POSITION = "tab_position";
    public static final String COMPLETED_POSITION = "completed_position";
    public static final String NOTIFICATION_LOGIN_USER_PROFILE = "login_user_profile";
    public static final String NOTIFICATION_APPT_HISTORY = "appointment_history";

    // Patient App Constants
    //GCM ID
    public static final String GCM_STATUS = "gcm_status";
    public static final String NEW_GCM_STATUS = "new_gcm_status";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PREFS_FILE_NAME = "gcm_function";
    public static final String GCM_FILENAME = "gcm_function";
    public static final String GCM_ID = "gcm_id";
    public static final String PRESCRIPTION_FROMDOCTOR = "prescription_fromdoctor";
    //    public static final String GCM_TO_SERVER= "gcm_to_server";
    // Preferance  For Login
    public static final String LOGIN_PREFRENCES_NAME = "login_remember";
    public static final String REGISTER_PREFRENCES = "register_preferance";
    public static final String QB_PREFRENCES_NAME = "login_remember";

    public static final String USER_NAME = "user_Name";
    public static final String USER_ID = "user_id";
    public static final String AGREEMENT = "agreement";
    public static final String PASSWORD = "password";
    public static final String LOGIN_STATUS = "login_status";
    public static final String REGISTER_STATUS = "register_status";
    public static String USER_NAME1 = "user_Name";
    public static final String PROFILR_PREFERENCE = "profile_Preference";
    public static final String PROFILR_STATUS = "profile_status";
    public static String DATE_BIRTH = "dateof_birth";
    public static final String TERMS_CONDTION = "terms_condtion";
    public static final String PRIVACY_POLIcy = "privacy_policy";
    public static final String CONTACT_US = "contact_us";
    public static final String QB_ALARAM_STATUS = "qb_alaram_status";

    //    public static final String EMAIL_ID = "email_id";
//    public static final String PHONE_NUMBER = "phone_number";
//    public static final String CONFORM_PASSWORD = "conform_password";
    public static final String NOTIFICATION_APPTS = "appointment_notify";
    public static final String SAVED_IMAGE_PREFIX = "IMG_";
    //EMR Image
    public static final String NOTIFICATION_EMRFILE = "emr_file";
    public static final String APPTMODEL_KEY = "apptmodel_key";
    public static final String DATE = "date";
    public static final String PREFS_LOGGEDUSERID = "LoggedUserId";
    public static final String LOGGED_USER_FULLNAME = "logged_user_full_name";

    public static final String FILE_NAME = "file_name";
    public static final String FILE_DURATION = "file_duration";
    public static final String FILE_DATA = "file_data";
    public static final String PRESCRIPTION_VIEW = "prescription_view";
    public static final String FILE_TYPE = "file_type";
    public static final String FILE_DATE = "appointmentdate";
    public static final String FILE_APPOINTMENTID = "appointmentid";

    public static final String GET_EMR_IMAGE_SERVLET = "GetEmrImageServlet";
    public static final String GET_EMR_AUDIO_SERVLET = "GetEmrAudioServlet";

    public static final String GET_EMR_IMAGE_PATIENTID = "patientid";
    public static final String GET_EMR_IMAGE_APPOINTMENTID = "appointmentid";
    public static final String GET_EMR_IMAGE_CREATED_AT = "createdat";
    public static final String IS_NOT_DELETED = "N";

    public static final String EMR_FOLDER = "EMR";

    public static final String IMAGES_FOLDER = "IMAGES";
    public static final String AUSCULTATION_FOLDER = "AUDIO";
    public static final String LOGGED_USERID = "logged_userid";
    public static final String APPT_VISITED = "Visited";
    public static final String LOGGED_ROLEID = "logged_rolid";

    public static final String FILE_TYPE_IMAGE = "IMAGE";
    public static final String FILE_TYPE_AUDIO = "AUDIO";
    public static final String NOTIFICATION_EMR_FULL_IMAGE = "emr_full_image";
    public static final String AUSCULTATION_DATE = "date";
    public static final String DOCTOR_ID = "doctor_id";
    public static final String DOCTOR_FEES = "doctor_fees";
    public static final String APPOINTMENT_ID = "appointment_id";
    public static final String OFFER_APPOINTMNET = "offer_appointment";
    public static final String ISNOFEE = "isnofee";
    //  from Doctor App for Chat Process

    public static final String NOTIFICATION_NURSE_ASSIGNED = "nurse_assigned";
    public static final String NOTIFICATION_APPTS_NOTES = "appointment_notes";
    public static int HISTORYCLICKED = 0;
    public static int HISTORYITEMCLICKED = 0;
    public static int PROGRESSSTATUS_DASHBOARD = 0;
    public static int PROGRESSSTATUS_EMR = 0;
    public static int PROGRESSSTATUS_NOTES = 0;
    public static int PROGRESSSTATUS_PRESCRIPTION = 0;
    public static int PROCESSED_FLAG_NOT_SENT = 0;
    public static int PROCESSED_FLAG_SENT = 1;
    public static int PROCESSED_FLAG_SENT_RESPONSE = 2;
    static public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final int USER_ROLE_PARAMEDIC = 5;
    public static final int USER_ROLE_CUSTOMER = 1;


    // Priscription
    public static boolean CAMERACLICK;
    public static final String NOTIFICATION_PRESCRIPTION_FULL_IMAGE = "prescription_full_image";
    public static final String PATIENT_ID = "patient_Id";
    public static final String PATIENT_DATE = "date";
    public static final String SAVED_AUDIO_PREFIX = "AUD_";

    public static final String PRESCRIPTION_FOLDER = "PRESCRIPTION";
    public static boolean isInsertPrescriptionAction = false;
    public static final String NOTIFICATION_APPTS_PRESC = "appointment_prescription";
    // Find Doctor...

    public static final String FIND_DOCTOR_BRODCAST = "find_doctor_broadcast";
    public static final String BOOK_APPOINT_BROADCAST = "book_appoint_bradcast";
    public static final String TIMSLOT_BROADCAST = "time_slot";
    public static final String ALLAPPOINTMENT_BROADCAST = "all_appointment";
    public static final String OTP_BROADCAST = "otp_broadcast";
    public static final String CANCELAPPOINT_BROADCAST = "cancel_appointment";
    public static final String ALL_DOCTOR = "all_docotor";
    public static final String DELETEDOCTOR_BROADCAST = "delete_doctor";

    //  Doctor ID and Appointment ID
    public static String DOCTORID = "doctorid";
    public static String APPOINTMENTID = "appoinmentid";
    public static String APPOINT_DATE = "appointment_date";
    public static String APPOINTMENT_TIME = "appointment_time";
    public static String SYMPTOMS1 = "symptoms";
    public static String SYMPTOMS2 = "symptoms1";
    public static String APPOINTMENT_TYPE = "appointmenttype";

    // Appointment Cancel Details
    public static final String RESERVATION_NO = "reservation_no";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String STATUS = "status";

    public static final String PG_MID = "MID";
    public static final String PG_CUSID = "cusId";
    public static final String PG_CHANNELID = "channelId";
    public static final String PG_INDUSTRYTYPEID = "industryTypeId";
    public static final String PG_WEBSITE = "website";
    public static final String PG_CHECKSUMHASH = "checkSumHash";
    public static final String PG_THEME = "THEME";


    //Cancel AppoinmentNotification
    public static String SUBTITTLE = "subtittle";
    public static String CREATED_AT_SERVER = "created_at_server";
    public static String PATIENTSTATUS = "patientstatus";
    public static String ACTION = "action";
    public static String TITTLE = "tittle";
    public static int APPOINMENTID = 0;
    public static String CREATEDAT = "createdat";
    public static String CREATEDBYID = "createdbyid";
    public static String CREATEDBYNAME = "createdbyname";
    public static String APPOINTMENTNOTE = "appointmentnote";
    public static String PATIENTNAME = "patientname";
    public static String PATIENTID = "patientid";
    public static String POSITION = "position";

    public static final String FLAG_EHD_FILE = "ehd_file";
    public static final String FLAG_PRECRIPTION_FILE = "prescription_file";
    public static final String NOTIFICATION_EHD_IMAGE = "ehd_image";
    public static String CREATEDAT_CLIENT = "createdat_clent";
    public static String APPOINTMENT_DATE = null;

    // PRofile Image Upload
    public static final String PROFILE_IMAGE = "PROFILE_IMAGE";


    DateFormat df;


    interface NetworkKeys {
        String NETWORK_STATUS = "Network_Status";
        String NETWORK_TYPE = "Network_Type";
        String NONE = "NONE";
        String WIFI = "WIFI";
        String TWO_G = "2G";
        String THREE_G = "3G";
        String FOUR_G = "4G";

    }

    public static boolean checkAvailability(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                return true;
            } else {
                String reason = "";
                if (netInfo != null) {
                    reason = netInfo.getReason();
                }
                LogTrace.i(TAG, "Network not availale : " + reason);
            }
            NetworkInfo i = cm.getActiveNetworkInfo();
            if (i == null)
                return false;
            if (!i.isConnected())
                return false;
            if (!i.isAvailable())
                return false;
        } catch (Exception e) {
            LogTrace.i(TAG, "Error checking network information");
            e.printStackTrace();
        }
        return false;
    }

    public static String getRootPath() {
        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        return extStorageDirectory;
    }


    public static String getUserID(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = ((Activity) context).getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        System.out.println("UUUUSER_ID  " + sharedPreferences.getString(VTConstants.USER_ID, "null"));
        return sharedPreferences.getString(VTConstants.USER_ID, null);
    }


    public static void createDirectory() {
        file = new File(getRootPath() + FILESEPARATOR + ROOTFOLDER
                + FILESEPARATOR + SUBFOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
//    public static String ProfileDicrectroy() {
//        String path = getRootPath() + FILESEPARATOR + ROOTFOLDER
//                + FILESEPARATOR + PROFILE_IMAGE +FILESEPARATOR + THUMBNAIL_FOLDER;
//
//        file = new File(path);
//
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//
//        return path;
//    }

    public static String createDirectoryEMRAuscultation() {
        String path = getRootPath() + FILESEPARATOR + ROOTFOLDER
                + FILESEPARATOR + EMR_FOLDER + FILESEPARATOR + AUSCULTATION_FOLDER;
        file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String createDirectoryPrescriptionThumbnail() {
        String path = getRootPath() + FILESEPARATOR + ROOTFOLDER
                + FILESEPARATOR + PRESCRIPTION_FOLDER + FILESEPARATOR + THUMBNAIL_FOLDER;
        file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    static public  SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String formateTimestamp(Date date) {
        long modifiedCreatedAt = date.getDate() + date.getTime();
        return Long.toString(modifiedCreatedAt);

    }

    public static String createDirectoryPrescription() {
        String path = getRootPath() + FILESEPARATOR + ROOTFOLDER
                + FILESEPARATOR + PRESCRIPTION_FOLDER;
        file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static boolean isValidMail(String email2) {
        boolean check;
        Pattern p;
        Matcher m;
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        p = Pattern.compile(EMAIL_STRING);
        m = p.matcher(email2);
        check = m.matches();
        return check;
    }

    // rony Dashboard GC - Starts
    public static boolean CheckFileExists(String existingImagePath) {
        File fCheck = new File(existingImagePath);
        if (fCheck.exists()) {
            return true;
        } else {
            return false;
        }

    }

    public static void DeleteFileFromPath(String existingImagePath) {
        File fdelete = new File(existingImagePath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.d("SPIN", "File from path is deleted successfully. : " + existingImagePath);
            } else {
                Log.d("SPIN", "File from path couldn't get deleted successfully. : " + existingImagePath);
            }
        } else {
            Log.d("SPIN", "File dosent exists in the specified path : " + existingImagePath);
        }


    }

    public static String createDirectoryProfileImageThumnail() {
        String path = getRootPath() + FILESEPARATOR + ROOTFOLDER
                + FILESEPARATOR + PROFILE_FOLDER + FILESEPARATOR + THUMBNAIL_FOLDER;
        file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String replace(String createdAt) {
        return createdAt.replaceAll(" ", "%20");
    }


    public static String createDirectoryEMRImageThumbnail() {
        String path = getRootPath() + FILESEPARATOR + ROOTFOLDER
                + FILESEPARATOR + EMR_FOLDER + FILESEPARATOR + IMAGES_FOLDER + FILESEPARATOR + THUMBNAIL_FOLDER;
        file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    public static String createDirectoryEMRImages() {
        String path = getRootPath() + FILESEPARATOR + ROOTFOLDER
                + FILESEPARATOR + EMR_FOLDER + FILESEPARATOR + IMAGES_FOLDER;
        file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;

    }

    public static String createDirectoryProfileImage() {
        String path = getRootPath() + FILESEPARATOR + ROOTFOLDER
                + FILESEPARATOR + PROFILE_FOLDER;
        file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


}
