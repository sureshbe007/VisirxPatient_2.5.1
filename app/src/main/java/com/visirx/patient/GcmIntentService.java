package com.visirx.patient;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.visirx.patient.activity.DashBoardActivity;
import com.visirx.patient.activity.PatientDetailsActivity;
import com.visirx.patient.common.provider.AllappointListProvider;
import com.visirx.patient.common.provider.AppointmentNotesListProvider;
import com.visirx.patient.common.provider.DigitalPrescriptionListProvider;
import com.visirx.patient.common.provider.EMRFileListProvider;
import com.visirx.patient.common.provider.PrescriptionListProvider;
import com.visirx.patient.model.AppointmentNoteModel;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.VTConstants;


import java.util.ArrayList;

/**
 * Created by suresh on 1/29/2016.
 */
public class GcmIntentService extends IntentService {


    public static final String TAG = "GCM Demo";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    SharedPreferences loggedPreferance;
    SharedPreferences sharedPreferences;
    String createdById = null;

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.w("PUSH", "Intent Msg : " + String.valueOf(intent));
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        loggedPreferance = getBaseContext().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        createdById = loggedPreferance.getString(VTConstants.USER_ID, null);

        Log.d("GCM_INTENT", "");

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                System.out.println("GCM==ERROR" + messageType);

                Log.d("GCM TAG", "Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                System.out.println("GCM==DELETE" + messageType);
                Log.d("GCM TAG",
                        "Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                //rony 1.3.5 starts

                Context context = getBaseContext();

                VTConstants.ACTION = intent.getExtras().getString("action");


                if (VTConstants.ACTION.equalsIgnoreCase("ADD_APPOINTMENT_NOTE")) {
                    try {
                        Log.d("GcmIntentService", "ADD_APPOINTMENT_NOTE :IN");

                        Log.d("SPIN", "Inside GcmIntentService :got message from ADD_APPOINTMENT_NOTE");
                        VTConstants.TITTLE = intent.getExtras().getString("title");
                        VTConstants.SUBTITTLE = intent.getExtras().getString(
                                "subtitle");

                        VTConstants.APPOINMENTID = Integer.parseInt(intent.getExtras().getString("appointmentid"));
                        VTConstants.PATIENTID = intent.getExtras().getString(
                                "patientid");
                        VTConstants.CREATEDAT = intent.getExtras().getString(
                                "createdat");

                        VTConstants.CREATED_AT_SERVER = intent.getExtras().getString("created_at_server");
                        VTConstants.CREATEDBYID = intent.getExtras().getString(
                                "createdbyid");
                        VTConstants.CREATEDBYNAME = intent.getExtras().getString(
                                "createdbyname");
                        VTConstants.APPOINTMENTNOTE = intent.getExtras().getString(
                                "appointmentnote");
                        VTConstants.PATIENTNAME = intent.getExtras().getString(
                                "patientname");

                        ArrayList<String> notesMaxdateList = VisirxApplication.aptNotesDAO
                                .getMaxDate(createdById,
                                        VTConstants.APPOINMENTID);
                        String notesMaxDate = "";
                        if (notesMaxdateList.size() == 0) {
                            notesMaxDate = null;
                        } else {
                            notesMaxDate = notesMaxdateList.get(0);
                        }

                        if (VTConstants.checkAvailability(getBaseContext())) {
                            AppointmentNotesListProvider provider = new AppointmentNotesListProvider(context);
                            provider.SendApptsReq(VTConstants.APPOINMENTID,
                                    createdById, notesMaxDate);
                        }

                      /*  AppointmentNoteModel appointmentNoteModel = new AppointmentNoteModel();
                        appointmentNoteModel.setPatientId(VTConstants.PATIENTID);
                        appointmentNoteModel
                                .setAppointmentId(VTConstants.APPOINMENTID);
                        appointmentNoteModel.setCreatedAt(VTConstants.CREATEDAT);
                        appointmentNoteModel.setCreatedAtServer(VTConstants.CREATED_AT_SERVER);
                        appointmentNoteModel
                                .setCreatedById(VTConstants.CREATEDBYID);
                        appointmentNoteModel
                                .setCreatedByName(VTConstants.CREATEDBYNAME);
                        appointmentNoteModel.setNotes(VTConstants.APPOINTMENTNOTE);
                        appointmentNoteModel
                                .setPatientName(VTConstants.PATIENTNAME);

                        VisirxApplication.aptNotesDAO.insertAppointmentNotes(
                                appointmentNoteModel,
                                VTConstants.PROCESSED_FLAG_SENT);*/

                        generateNotification(context, VTConstants.ACTION,
                                VTConstants.TITTLE, VTConstants.SUBTITTLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (VTConstants.ACTION.equalsIgnoreCase("CANCEL_APPOINTMENT")) {
                    try {
                        Log.d("GcmIntentService", "CANCEL_APPOINTMENT :IN");
                        VTConstants.APPOINMENTID = Integer.parseInt(intent
                                .getExtras().getString("appointmentid"));
                        VTConstants.TITTLE = intent.getExtras().getString("title");
                        VTConstants.SUBTITTLE = intent.getExtras().getString(
                                "subtitle");
                        VTConstants.PATIENTSTATUS = intent.getExtras().getString("status");
                        VisirxApplication.aptDAO
                                .patientStatusUpdate(VTConstants.PATIENTSTATUS,
                                        VTConstants.APPOINMENTID);
                        generateNotification(context, VTConstants.ACTION,
                                VTConstants.TITTLE, VTConstants.SUBTITTLE);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }


                if (VTConstants.ACTION.equalsIgnoreCase("DELETE_EHD")) {

                    try {
                        Log.d("GcmIntentService", "DELETE_EHD :IN");
                        VTConstants.TITTLE = intent.getExtras().getString("title");
                        VTConstants.SUBTITTLE = intent.getExtras().getString("subtitle");
                        VTConstants.APPOINMENTID = Integer.parseInt(intent.getExtras().getString("appointmentid"));
                        VTConstants.PATIENTID = intent.getExtras().getString("patientid");
                        VTConstants.CREATEDAT = intent.getExtras().getString("createdat");


                        VisirxApplication.aptEmrDAO.deleteEhdRecordFromTable(VTConstants.PATIENTID, String.valueOf(VTConstants.APPOINMENTID), VTConstants.CREATEDAT);
                        Intent intent1 = new Intent(VTConstants.NOTIFICATION_EMRFILE);
                        context.sendBroadcast(intent1);

                        Intent ehdimageintent1 = new Intent(VTConstants.NOTIFICATION_EHD_IMAGE);
                        context.sendBroadcast(ehdimageintent1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                //  Doctor Bookappointment ID

                if (VTConstants.ACTION.equalsIgnoreCase("NEW_APPOINTMENT")) {

                    try {
                        Log.d("GcmIntentService", "NEW_APPOINTMENT :IN");
                        sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                        Logger.d("NEW_APPOINTMENT", "" + "INSIDE IF");

                        VTConstants.TITTLE = intent.getExtras().getString("title");
                        VTConstants.SUBTITTLE = intent.getExtras().getString("subtitle");
                        VTConstants.APPOINMENTID = Integer.parseInt(intent.getExtras().getString("appointmentid"));
                        VTConstants.PATIENTID = intent.getExtras().getString("patientid");
                        Logger.d("NEW_APPOINTMENT", "" + VTConstants.TITTLE);
                        Logger.d("NEW_APPOINTMENT", "" + VTConstants.SUBTITTLE);
                        Logger.d("NEW_APPOINTMENT", "" + VTConstants.APPOINMENTID);
                        Logger.d("NEW_APPOINTMENT", "" + VTConstants.PATIENTID);

                        generateNotification(context, VTConstants.ACTION,
                                VTConstants.TITTLE, VTConstants.SUBTITTLE);
                        AllappointListProvider allappointListProvider = new AllappointListProvider(context);
                        allappointListProvider.AllappointmentReq(sharedPreferences.getString(VTConstants.USER_ID, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


                if (VTConstants.ACTION.equalsIgnoreCase("DELETE_PRESCRIPTION")) {

                    try {
                        Log.d("GcmIntentService", "DELETE_PRESCRIPTION :IN");
                        Log.d("DELETE_PRESCRIPTION", "" + VTConstants.TITTLE);
                        Log.d("DELETE_PRESCRIPTION", "" + VTConstants.SUBTITTLE);
                        Log.d("DELETE_PRESCRIPTION", "" + VTConstants.APPOINMENTID);
                        Log.d("DELETE_PRESCRIPTION", "" + VTConstants.PATIENTID);
                        Log.d("DELETE_PRESCRIPTION", "" + VTConstants.CREATEDAT);

                        VTConstants.TITTLE = intent.getExtras().getString("title");
                        VTConstants.SUBTITTLE = intent.getExtras().getString("subtitle");
                        VTConstants.APPOINMENTID = Integer.parseInt(intent.getExtras().getString("appointmentid"));
                        VTConstants.PATIENTID = intent.getExtras().getString("patientid");
                        VTConstants.CREATEDAT = intent.getExtras().getString("createdat");


                        VisirxApplication.aptPresDAO.deletePrescriptionRecordFromTable(VTConstants.PATIENTID, String.valueOf(VTConstants.APPOINMENTID), VTConstants.CREATEDAT);
                        Intent intent1 = new Intent(VTConstants.NOTIFICATION_EMRFILE);
                        context.sendBroadcast(intent1);

                        Intent ehdimageintent1 = new Intent(VTConstants.NOTIFICATION_APPTS_PRESC);
                        context.sendBroadcast(ehdimageintent1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (VTConstants.ACTION.equalsIgnoreCase("NEW_DIGITAL_PRESCRIPTION"))
                {

                    try {
                        Log.d("NEW_DIGITAL", "NEW_DIGITAL_PRESCRIPTION  IN  : ");

                        Log.d("NEW_DIGITAL", "APPOINMENTID  :  " + VTConstants.APPOINMENTID);
                        Log.d("NEW_DIGITAL", " PATIENTID    : " + VTConstants.PATIENTID);
                        Log.d("NEW_DIGITAL", "CREATEDAT    : " + VTConstants.CREATEDAT);

                        VTConstants.APPOINMENTID = Integer.parseInt(intent.getExtras().getString("appointmentid"));
                        VTConstants.PATIENTID = intent.getExtras().getString("patientid");
                        VTConstants.CREATEDAT = intent.getExtras().getString("createdat");
                        Log.d("NEW_DIGITAL", " createdById     initView() :" + createdById);
                        Log.d("NEW_DIGITAL", "ReservationNumber() initView() :" + VTConstants.APPOINMENTID);

                        ArrayList<String> presMaxdateList = VisirxApplication.digitalMainTableDAO.getMaxDate( VTConstants.PATIENTID, VTConstants.APPOINMENTID);
                        String presMaxDate = "";
                        if (presMaxdateList.size() == 0) {
                            presMaxDate = null;
                        } else {
                            presMaxDate = presMaxdateList.get(0);
                        }
                        Log.d("NEW_DIGITAL", "presMaxDate :" + presMaxDate);
                        DigitalPrescriptionListProvider digitalPrescriptionListProvider = new DigitalPrescriptionListProvider(getBaseContext());
                        digitalPrescriptionListProvider.SendDigitalPrescriptionListReq(VTConstants.APPOINMENTID, VTConstants.PATIENTID, presMaxDate);

                        Intent ehdimageintent1 = new Intent(VTConstants.NOTIFICATION_APPTS_PRESC);
                        context.sendBroadcast(ehdimageintent1);
                        Log.d("DELETE_DIGITAL", "NEW_DIGITAL_PRESCRIPTION  END  : ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


                //  Delete the Digital Prescription

                if (VTConstants.ACTION.equalsIgnoreCase("DELETE_DIGITAL_PRESCRIPTION"))
                {

                    try {
                        Log.d("DELETE_DIGITAL", "DELTE_DIGITALPRESCRIPTION  IN  : ");
//                        Log.d("DELETE_PRESCRIPTION", "" + VTConstants.TITTLE);
//                        Log.d("DELETE_PRESCRIPTION", "" + VTConstants.SUBTITTLE);
                        Log.d("DELETE_DIGITAL", "" + VTConstants.APPOINMENTID);
                        Log.d("DELETE_DIGITAL", "" + VTConstants.PATIENTID);
                        Log.d("DELETE_DIGITAL", "" + VTConstants.CREATEDAT);
//
//                        VTConstants.TITTLE = intent.getExtras().getString("title");
//                        VTConstants.SUBTITTLE = intent.getExtras().getString("subtitle");
                        VTConstants.APPOINMENTID = Integer.parseInt(intent.getExtras().getString("appointmentid"));
                        VTConstants.PATIENTID = intent.getExtras().getString("patientid");
                        VTConstants.CREATEDAT = intent.getExtras().getString("createdat");
                        VisirxApplication.digitalMainTableDAO.deletePrescriptionFromMainTable(VTConstants.PATIENTID, String.valueOf(VTConstants.APPOINMENTID), VTConstants.CREATEDAT);
                        VisirxApplication.digitaTableDAO.deletePrescriptionFromTable(VTConstants.PATIENTID, String.valueOf(VTConstants.APPOINMENTID), VTConstants.CREATEDAT);
//                        Intent intent1 = new Intent(VTConstants.NOTIFICATION_EMRFILE);
//                        context.sendBroadcast(intent1);

                        Intent ehdimageintent1 = new Intent(VTConstants.NOTIFICATION_APPTS_PRESC);
                        context.sendBroadcast(ehdimageintent1);
                        Log.d("DELETE_DIGITAL", "DELTE_DIGITALPRESCRIPTION  END  : ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }




                // for Prescription
                if (VTConstants.ACTION.equalsIgnoreCase("NEW_PRESCRIPTION")) {

                    try {
                        Log.d("GcmIntentService", "NEW_PRESCRIPTION :IN");
                        Log.d("NEW_PRESCRIPTION 1", "" + intent.getExtras().getString("patientid"));
                        Log.d("NEW_PRESCRIPTION 2", "" + Integer.parseInt(intent.getExtras().getString("appointmentid")));
                        Log.d("NEW_PRESCRIPTION 3", "" + intent.getExtras().getString("title"));

                        VTConstants.APPOINMENTID = Integer.parseInt(intent
                                .getExtras().getString("appointmentid"));
                        VTConstants.TITTLE = intent.getExtras().getString("title");
                        VTConstants.PATIENTID = intent.getExtras().getString("patientid");
                        VTConstants.SUBTITTLE = intent.getExtras().getString("subtitle");

                        ArrayList<String> presMaxdateList = VisirxApplication.aptPresDAO
                                .getMaxDate(VTConstants.PATIENTID,
                                        VTConstants.APPOINMENTID);
                        String presMaxDate = "";
                        if (presMaxdateList.size() == 0) {
                            presMaxDate = null;
                        } else {
                            presMaxDate = presMaxdateList.get(0);
                        }
                        PrescriptionListProvider provider = new PrescriptionListProvider(
                                context);

                        provider.SendApptPrescriptionReq(VTConstants.APPOINMENTID,
                                VTConstants.PATIENTID, presMaxDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (VTConstants.ACTION.equalsIgnoreCase("CANCEL_APPOINTMENT")) {

                    try {
                        Log.d("GcmIntentService", "CANCEL_APPOINTMENT :IN");
                        VTConstants.APPOINMENTID = Integer.parseInt(intent
                                .getExtras().getString("appointmentid"));
                        VTConstants.TITTLE = intent.getExtras().getString("title");
                        VTConstants.SUBTITTLE = intent.getExtras().getString(
                                "subtitle");
                        VTConstants.PATIENTSTATUS = intent.getExtras().getString("status");
                        VisirxApplication.aptDAO
                                .patientStatusUpdate(VTConstants.PATIENTSTATUS,
                                        VTConstants.APPOINMENTID);
                        generateNotification(context, VTConstants.ACTION,
                                VTConstants.TITTLE, VTConstants.SUBTITTLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (VTConstants.ACTION.equalsIgnoreCase("COMPLETED_APPOINTMENT")) {
                    try {

                        Log.d("GcmIntentService", "COMPLETED_APPOINTMENT :IN");

                        VTConstants.APPOINMENTID = Integer.parseInt(intent.getExtras().getString("appointmentid"));
                        VTConstants.TITTLE = intent.getExtras().getString("title");
                        VTConstants.SUBTITTLE = intent.getExtras().getString(
                                "subtitle");
                        VTConstants.PATIENTSTATUS = intent.getExtras().getString(
                                "status");
                        VisirxApplication.aptDAO
                                .patientStatusUpdate(VTConstants.PATIENTSTATUS,
                                        VTConstants.APPOINMENTID);

                        generateNotification(context, VTConstants.ACTION,
                                VTConstants.TITTLE, VTConstants.SUBTITTLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (VTConstants.ACTION.equalsIgnoreCase("NEW_EMR_UPLOAD")) {

                    try {


                        Log.d("GcmIntentService", "NEW_EMR_UPLOAD :IN");
                        VTConstants.TITTLE = intent.getExtras().getString("title");
                        VTConstants.SUBTITTLE = intent.getExtras().getString(
                                "subtitle");

                        VTConstants.APPOINMENTID = Integer.parseInt(intent
                                .getExtras().getString("appointmentid"));

                        VTConstants.PATIENTID = intent.getExtras().getString(
                                "patientid");


                        Log.d("NEW_EMR_UPLOAD", " :" + VTConstants.APPOINMENTID);
                        Log.d("NEW_EMR_UPLOAD", " :" + VTConstants.PATIENTID);

                        ArrayList<String> emrFile = VisirxApplication.aptEmrDAO .GetPatientEMRFileMax(VTConstants.PATIENTID,
                                        VTConstants.APPOINMENTID,createdById);
                        ArrayList<String> emrVitals = VisirxApplication.aptVitalDAO .GetPatientEMRVitaleMax(VTConstants.PATIENTID,
                                        VTConstants.APPOINMENTID);
                        String emrFileMaxDate = "";
                        String emrVitalsMaxDate = "";
                        if (emrFile.size() == 0) {
                            emrFileMaxDate = null;
                        } else {
                            emrFileMaxDate = emrFile.get(0);
                        }
                        if (emrVitals.size() == 0) {
                            emrVitalsMaxDate = null;
                        } else {
                            emrVitalsMaxDate = emrVitals.get(0);
                        }
                        EMRFileListProvider emrFileListProvider = new EMRFileListProvider(
                                context);

                        emrFileListProvider.SendApptsReq(
                                String.valueOf(VTConstants.APPOINMENTID),
                                VTConstants.PATIENTID, emrFileMaxDate,
                                emrVitalsMaxDate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

        }
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

    //rony 1.3.5 starts
    private void generateNotification(Context context, String action, String tittle, String subtittle) {

        Log.d("SPIN", "Inside generateNotification :");
        Intent notificationIntent;

        try {
            if (action.equalsIgnoreCase("ADD_APPOINTMENT_NOTE")) {
                int NOTIFICATION_ID = 1;
                Log.d("SPIN", "Inside generateNotification :got message from ADD_APPOINTMENT_NOTE");
                Log.d("SPIN", "Inside generateNotification :APPTMODEL_KEY : " + VTConstants.APPOINMENTID);
                Log.d("SPIN", "Inside generateNotification : position - " + 2);
                notificationIntent = new Intent(context, PatientDetailsActivity.class);
                notificationIntent.putExtra(VTConstants.APPOINTMENT_ID, Integer.toString(VTConstants.APPOINMENTID));
                notificationIntent.putExtra(VTConstants.POSITION, Integer.toString(2));
//			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent intent = PendingIntent.getActivity(context, 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        context)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.visirx_logo)
                        .setTicker(context.getString(R.string.app_name))
                        .setContentTitle(tittle)
                        .setContentText(subtittle)
                        .setDefaults(
                                Notification.DEFAULT_LIGHTS
                                        | Notification.DEFAULT_VIBRATE
                                        | Notification.DEFAULT_SOUND)
                        .setContentIntent(intent);

                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
                Intent broadcast = new Intent(VTConstants.NOTIFICATION_APPTS_NOTES);
                context.sendBroadcast(broadcast);

            }

            if (action.equalsIgnoreCase("CANCEL_APPOINTMENT")) {
                System.out.println("CANCEL_APPOINTMENT 2");
                int NOTIFICATION_ID = 2;
                notificationIntent = new Intent(context, DashBoardActivity.class);
//                notificationIntent.putExtra("POSITION", 2);

                notificationIntent.putExtra(VTConstants.APPOINTMENT_ID, Integer.toString(VTConstants.APPOINMENTID));
                notificationIntent.putExtra(VTConstants.POSITION, 2);

                PendingIntent intent = PendingIntent.getActivity(context, 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        context)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.visirx_logo)
                        .setTicker(context.getString(R.string.app_name))
                        .setContentTitle(tittle)
                        .setContentText(subtittle)
                        .setDefaults(
                                Notification.DEFAULT_LIGHTS
                                        | Notification.DEFAULT_VIBRATE
                                        | Notification.DEFAULT_SOUND)
                        .setContentIntent(intent);

                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());

                Intent allappoiment = new Intent(VTConstants.NOTIFICATION_APPTS);
                context.sendBroadcast(allappoiment);
                Intent cancelappointment = new Intent(VTConstants.CANCELAPPOINT_BROADCAST);
                context.sendBroadcast(cancelappointment);
            }


            if (action.equalsIgnoreCase("COMPLETED_APPOINTMENT")) {


                int NOTIFICATION_ID = 3;
                notificationIntent = new Intent(context, DashBoardActivity.class);
//                notificationIntent.putExtra("POSITION", 1);

                notificationIntent.putExtra(VTConstants.APPOINTMENT_ID, Integer.toString(VTConstants.APPOINMENTID));
                notificationIntent.putExtra(VTConstants.COMPLETED_POSITION, 2);

                PendingIntent intent = PendingIntent.getActivity(context, 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        context)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.visirx_logo)
                        .setTicker(context.getString(R.string.app_name))
                        .setContentTitle(tittle)
                        .setContentText(subtittle)
                        .setDefaults(
                                Notification.DEFAULT_LIGHTS
                                        | Notification.DEFAULT_VIBRATE
                                        | Notification.DEFAULT_SOUND)
                        .setContentIntent(intent);

                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
                Intent allappoiment = new Intent(VTConstants.NOTIFICATION_APPTS);
                context.sendBroadcast(allappoiment);
                Intent cancelappointment = new Intent(VTConstants.CANCELAPPOINT_BROADCAST);
                context.sendBroadcast(cancelappointment);


            }
            if (VTConstants.ACTION.equalsIgnoreCase("AAA")) {

                int NOTIFICATION_ID = 4;
                notificationIntent = new Intent(context, DashBoardActivity.class);
                notificationIntent.putExtra("POSITION", Integer.parseInt(VTConstants.ACTION));

                PendingIntent intent = PendingIntent.getActivity(context, 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        context)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.visirx_logo)
                        .setTicker(context.getString(R.string.app_name))
                        .setContentTitle(tittle)
                        .setContentText(subtittle)
                        .setDefaults(
                                Notification.DEFAULT_LIGHTS
                                        | Notification.DEFAULT_VIBRATE
                                        | Notification.DEFAULT_SOUND)
                        .setContentIntent(intent);

                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
                Intent broad = new Intent(VTConstants.ALLAPPOINTMENT_BROADCAST);
                context.sendBroadcast(broad);
                Intent allappoiment = new Intent(VTConstants.NOTIFICATION_APPTS);
                context.sendBroadcast(allappoiment);

            }

            // Doctor Book Appointment
            if (action.equalsIgnoreCase("NEW_APPOINTMENT"))
            {

                Logger.d("GCM_INTENTSERVICE", "NEW_APPOINTMENT");
                int NOTIFICATION_ID = 5;
                notificationIntent = new Intent(context, DashBoardActivity.class);
//                notificationIntent.putExtra("POSITION", 2);

                notificationIntent.putExtra(VTConstants.APPOINTMENT_ID, Integer.toString(VTConstants.APPOINMENTID));
                notificationIntent.putExtra(VTConstants.POSITION, 1);

                PendingIntent intent = PendingIntent.getActivity(context, 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        context)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.visirx_logo)
                        .setTicker(context.getString(R.string.app_name))
                        .setContentTitle(tittle)
                        .setContentText(subtittle)
                        .setDefaults(
                                Notification.DEFAULT_LIGHTS
                                        | Notification.DEFAULT_VIBRATE
                                        | Notification.DEFAULT_SOUND)
                        .setContentIntent(intent);

                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());

//                Intent allappoiment = new Intent(VTConstants.ALLAPPOINTMENT_BROADCAST);
//                context.sendBroadcast(allappoiment);

                Intent DoctorBookapoint = new Intent(VTConstants.NOTIFICATION_APPTS);
                context.sendBroadcast(DoctorBookapoint);
                Logger.d("NEW_APPOINTMENT", "NOTIFICATION LOOP_END");

            }



            if(action.equalsIgnoreCase("QB_PING"))
            {
                Logger.d("QB_PING", "Notification generated : ");
                int NOTIFICATION_ID = 6;


                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        context)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.visirx_logo)
                        .setTicker(context.getString(R.string.app_name))
                        .setContentTitle(tittle)
                        .setContentText(subtittle)
                        .setDefaults(
                                Notification.DEFAULT_LIGHTS
                                        | Notification.DEFAULT_VIBRATE
                                        | Notification.DEFAULT_SOUND);


                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());

            }


        } catch (Exception e) {
            e.printStackTrace();
        }






    }

    //rony 1.3.5 ends


//        if (action_msg.equalsIgnoreCase("ADD_APPOINTMENT_NOTE")) {
//            notificationIntent = new Intent(context, PatientTabActivity.class);
//            notificationIntent.putExtra(VTConstants.APPTMODEL_KEY,
//                    VTConstants.APPOINMENTID);
//            notificationIntent.putExtra(VTConstants.POSITION, "2");
//
////			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent intent = PendingIntent.getActivity(context, 0,
//                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                    context)
//                    .setAutoCancel(true)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.drawable.visirx_logo)
//                    .setTicker(context.getString(R.string.app_name))
//                    .setContentTitle(tittle_msg)
//                    .setContentText(subtittle_msg)
//                    .setDefaults(
//                            Notification.DEFAULT_LIGHTS
//                                    | Notification.DEFAULT_VIBRATE
//                                    | Notification.DEFAULT_SOUND)
//                    .setContentIntent(intent);
//
//            NotificationManager manager = (NotificationManager) context
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.notify(0, builder.build());
//            Intent broadcast = new Intent(VTConstants.NOTIFICATION_APPTS_NOTES);
//            context.sendBroadcast(broadcast);
//
//        }
//
//        if (action_msg.equalsIgnoreCase("NEW_EMR_UPLOAD")) {
//            notificationIntent = new Intent(context, PatientTabActivity.class);
//            // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
//            // Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            notificationIntent.putExtra(VTConstants.POSITION, "1");
//            notificationIntent.putExtra(VTConstants.APPTMODEL_KEY,
//                    VTConstants.APPOINMENTID);
//
////			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            PendingIntent intent = PendingIntent.getActivity(context, 0,
//                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                    context)
//                    .setAutoCancel(true)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.drawable.visirx_logo)
//                    .setTicker(context.getString(R.string.app_name))
//                    .setContentTitle(tittle_msg)
//                    .setContentText(subtittle_msg)
//                    .setDefaults(
//                            Notification.DEFAULT_LIGHTS
//                                    | Notification.DEFAULT_VIBRATE
//                                    | Notification.DEFAULT_SOUND)
//                    .setContentIntent(intent);
//
//            NotificationManager manager = (NotificationManager) context
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.notify(0, builder.build());
//            Intent broadcast = new Intent(VTConstants.NOTIFICATION_APPTS);
//            context.sendBroadcast(broadcast);
//        }
//        if (action_msg.equalsIgnoreCase("STATUS_VISITED")) {
//            notificationIntent = new Intent(context, Home.class);
//            // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
//            // Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            notificationIntent.putExtra("RESET_POSITION",  -1);
//
//
////			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            PendingIntent intent = PendingIntent.getActivity(context, 0,
//                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                    context)
//                    .setAutoCancel(true)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.drawable.visirx_logo)
//                    .setTicker(context.getString(R.string.app_name))
//                    .setContentTitle(tittle_msg)
//                    .setContentText(subtittle_msg)
//                    .setDefaults(
//                            Notification.DEFAULT_LIGHTS
//                                    | Notification.DEFAULT_VIBRATE
//                                    | Notification.DEFAULT_SOUND)
//                    .setContentIntent(intent);
//
//            NotificationManager manager = (NotificationManager) context
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.notify(0, builder.build());
//            Intent broadcast = new Intent(VTConstants.NOTIFICATION_APPTS);
//            context.sendBroadcast(broadcast);
//
//        }
//
//        if (action_msg.equalsIgnoreCase("CANCEL_APPOINTMENT")) {
//            notificationIntent = new Intent(context, Home.class);
//            notificationIntent.putExtra("RESET_POSITION", -1);
//            // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
//            // Intent.FLAG_ACTIVITY_SINGLE_TOP);
////			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            PendingIntent intent = PendingIntent.getActivity(context, 0,
//                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                    context)
//                    .setAutoCancel(true)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.drawable.visirx_logo)
//                    .setTicker(context.getString(R.string.app_name))
//                    .setContentTitle(tittle_msg)
//                    .setContentText(subtittle_msg)
//                    .setDefaults(
//                            Notification.DEFAULT_LIGHTS
//                                    | Notification.DEFAULT_VIBRATE
//                                    | Notification.DEFAULT_SOUND)
//                    .setContentIntent(intent);
//
//            NotificationManager manager = (NotificationManager) context
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.notify(0, builder.build());
//            Intent broadcast = new Intent(VTConstants.NOTIFICATION_APPTS);
//            context.sendBroadcast(broadcast);
//        }
//        if (action_msg.equalsIgnoreCase("NEW_APPOINTMENT")) {
//            notificationIntent = new Intent(context, Home.class);
//            notificationIntent.putExtra("RESET_POSITION", -1);
//
////			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            PendingIntent intent = PendingIntent.getActivity(context, 0,
//                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                    context)
//                    .setAutoCancel(true)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.drawable.visirx_logo)
//                    .setTicker(context.getString(R.string.app_name))
//                    .setContentTitle(tittle_msg)
//                    .setContentText(subtittle_msg)
//                    .setDefaults(
//                            Notification.DEFAULT_LIGHTS
//                                    | Notification.DEFAULT_VIBRATE
//                                    | Notification.DEFAULT_SOUND)
//                    .setContentIntent(intent);
//
//            NotificationManager manager = (NotificationManager) context
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.notify(0, builder.build());
//            Intent broadcast = new Intent(VTConstants.NOTIFICATION_APPTS);
//            context.sendBroadcast(broadcast);
//        }
//        if (action_msg.equalsIgnoreCase("PARAMEDIC_UPDATE")) {
//            notificationIntent = new Intent(context, PatientTabActivity.class);
//            notificationIntent.putExtra("RESET_POSITION", "0");
//            notificationIntent.putExtra(VTConstants.APPTMODEL_KEY,
//                    VTConstants.APPOINMENTID);
//
////			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            PendingIntent intent = PendingIntent.getActivity(context, 0,
//                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                    context)
//                    .setAutoCancel(true)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.drawable.visirx_logo)
//                    .setTicker(context.getString(R.string.app_name))
//                    .setContentTitle(tittle_msg)
//                    .setContentText(subtittle_msg)
//                    .setDefaults(
//                            Notification.DEFAULT_LIGHTS
//                                    | Notification.DEFAULT_VIBRATE
//                                    | Notification.DEFAULT_SOUND)
//                    .setContentIntent(intent);
//
//            NotificationManager manager = (NotificationManager) context
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.notify(0, builder.build());
//            Intent broadcast = new Intent(VTConstants.NOTIFICATION_NURSE_ASSIGNED);
//            context.sendBroadcast(broadcast);
//        }
//
//    }
}
