package com.visirx.patient.common.provider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.AddprescriptionReq;
import com.visirx.patient.api.AddprescriptionRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.ApptPrescriptionModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by aa on 21.1.16.
 */
public class AddPrescriptionProvider {


    static final String TAG = AddPrescriptionProvider.class.getName();
    Context context;
    SharedPreferences loggedPreferance;

    public AddPrescriptionProvider(Context context) {
        super();
        this.context = context;
    }

    ArrayList<ApptPrescriptionModel> modelList;
    RequestHeader requestMessageHeader = new RequestHeader();
    int lstcnt = 0;
    private String patientId = "0";
    private int appointmentId = 0;
    private String createdAt = "";
    private int totalPrescriptionCount = 0;

    public void SendApptPrescriptionReq(int ReservationNumber, int prescriptionCount) {
        try {
            loggedPreferance = context.getSharedPreferences(
                    VTConstants.PREFS_LOGGEDUSERID, 0);
            if (VTConstants.checkAvailability(context)) {
//				requestMessageHeader.setUserId(VisirxApplication.appContext.getLoggedUser().getUserId());
                requestMessageHeader.setUserId(loggedPreferance.getString(VTConstants.LOGGED_USERID, "Not set"));
                modelList = VisirxApplication.aptPresDAO.GetPrescription(ReservationNumber);
                if (modelList.size() > 0) {
                    ArrayList<ApptPrescriptionModel> modelListnew = new ArrayList<ApptPrescriptionModel>();
                    ApptPrescriptionModel modelnew = modelList.get(lstcnt);
                    byte[] data = convertFileToByteArray(modelnew.getPreimageName());
                    modelnew.setPrescriptionImage(data);
                    patientId = modelnew.getPatientId();
                    appointmentId = modelnew.getAppointmentId();
                    createdAt = modelnew.getCreatedAt();
                    totalPrescriptionCount = prescriptionCount;
                    modelListnew.add(modelnew);
                    AddprescriptionReq appointmentReq = new AddprescriptionReq();
                    appointmentReq.setPrescriptionModelList(modelListnew);
                    appointmentReq.setRequestHeader(requestMessageHeader);
                    appointmentReq.setPrescriptionCount(totalPrescriptionCount);
                    lstcnt = 1;
                    new ApptNotesTask().execute(appointmentReq);
                }

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    public static byte[] convertFileToByteArray(String f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    private class ApptNotesTask extends AsyncTask<AddprescriptionReq, Integer, AddprescriptionRes> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected AddprescriptionRes doInBackground(AddprescriptionReq... params) {
            AddprescriptionRes result = (AddprescriptionRes) HTTPUtils.getDataFromServer(params[0],
                    AddprescriptionReq.class, AddprescriptionRes.class, "AddPrescriptionAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(AddprescriptionRes result) {
            super.onPostExecute(result);
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else {
                //if (result.getResponseHeader().getResponseCode() != 0)
                VisirxApplication.aptPresDAO.setProcessFlagAppointmentPrescription(patientId, appointmentId, createdAt);
                if (modelList.size() > lstcnt) {
                    ArrayList<ApptPrescriptionModel> modelListnew = new ArrayList<ApptPrescriptionModel>();
                    ApptPrescriptionModel modelnew = modelList.get(lstcnt);
                    byte[] data = convertFileToByteArray(modelnew.getPreimageName());
                    modelnew.setPrescriptionImage(data);
                    patientId = modelnew.getPatientId();
                    appointmentId = modelnew.getAppointmentId();
                    createdAt = modelnew.getCreatedAt();
                    modelListnew.add(modelnew);
                    AddprescriptionReq appointmentReq = new AddprescriptionReq();
                    appointmentReq.setPrescriptionModelList(modelListnew);
                    appointmentReq.setRequestHeader(requestMessageHeader);
                    appointmentReq.setPrescriptionCount(totalPrescriptionCount);
                    lstcnt = lstcnt + 1;
                    Intent intent = new Intent(VTConstants.NOTIFICATION_APPTS_PRESC);
                    context.sendBroadcast(intent);
                    new ApptNotesTask().execute(appointmentReq);

                }
                Intent intent = new Intent(VTConstants.NOTIFICATION_APPTS_PRESC);
                context.sendBroadcast(intent);

            }
            // ProcessAddprescriptionResponse(result);
        }

    }

    public void ProcessAddprescriptionResponse(AddprescriptionRes result) {
        try {
            // TODO Auto-generated method stub
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(),
                        Toast.LENGTH_LONG);
                // Popup.ShowErrorMessage(context,R.string.error_trip_list,Toast.LENGTH_LONG);
                return;
            } else {
                // VisirxApplication.aptDAO.insertAppointment(result.getAppointmentModel());
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }
}
