package com.visirx.patient.common.provider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.PrescriptionListReq;
import com.visirx.patient.api.PrescriptionListRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Suresh on 22-02-2016.
 */
public class PrescriptionListProvider {

    static final String TAG = PrescriptionListProvider.class.getName();
    Context context;
    SharedPreferences loggedPreferance;

    public PrescriptionListProvider(Context context) {
        super();
        this.context = context;
    }

    public void SendApptPrescriptionReq(int appointmentId, String patientId, String presMaxDate) {
        try {

            Log.d("NEW_PRESCRIPTION 1","PROVIDER"+appointmentId);
            Log.d("NEW_PRESCRIPTION 2","PROVIDER"+patientId);
            Log.d("NEW_PRESCRIPTION 3","PROVIDER"+presMaxDate);
            loggedPreferance = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            if (VTConstants.checkAvailability(context)) {
                RequestHeader requestMessageHeader = new RequestHeader();
                //requestMessageHeader.setUserId(VisirxApplication.appContext.getLoggedUser().getUserId());
                requestMessageHeader.setUserId(loggedPreferance.getString(VTConstants.USER_ID, null));
                PrescriptionListReq appointmentReq = new PrescriptionListReq();
                appointmentReq.setAppointmentId(appointmentId);
                appointmentReq.setPatientId(patientId);
                appointmentReq.setPrescriptionLastUpdated(presMaxDate);
                appointmentReq.setRequestHeader(requestMessageHeader);
                new ApptPrescriptionTask().execute(appointmentReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private class ApptPrescriptionTask extends AsyncTask<PrescriptionListReq, Integer, PrescriptionListRes> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected PrescriptionListRes doInBackground(PrescriptionListReq... params) {
            PrescriptionListRes result = (PrescriptionListRes) HTTPUtils.getDataFromServer(params[0], PrescriptionListReq.class,
                    PrescriptionListRes.class, "PrescriptionDetailsListServlet");
            return result;
        }

        @Override
        protected void onPostExecute(PrescriptionListRes result) {
            super.onPostExecute(result);
            ProcessPrescriptionListResponse(result);
        }

    }

    public void ProcessPrescriptionListResponse(PrescriptionListRes result) {
        try {
            // TODO Auto-generated method stub
            if (result == null) {
                Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                return;
            } else if (result.getResponseHeader().getResponseCode() != 0) {
                Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                //Popup.ShowErrorMessage(context,R.string.error_trip_list,Toast.LENGTH_LONG);
                return;
            } else {
                Log.d("NEW_PRESCRIPTION 1","RESPONSE");

                for (int i = 0; i < result.getModelList().size(); i++) {
                    // rony prescription GC starts
                    Log.d("NEW_PRESCRIPTION","NEW_PRESCRIPTION ::"+result.getModelList().get(i).getCreatedAtServer());
                    boolean isUpdateReq = VisirxApplication.aptPresDAO.insertAppointmentPrescription(result.getModelList().get(i), VTConstants.PROCESSED_FLAG_SENT);
//                    if (isUpdateReq) {
//                        Log.d("SPIN",
//                                "Prescription Image file not found : Sync required for id"
//                                        + result.getModelList().get(i).getAppointmentId());
////                        ImageDownloaderProvider dwnlodProvider = new ImageDownloaderProvider(
////                                context,
////                                VTConstants.NOTIFICATION_APPTS_PRESC);
////                        dwnlodProvider.SendPrescriptionImgReq(
////                                result.getModelList().get(i).getAppointmentId(),
////                                result.getModelList().get(i).getCreatedAt(),
////                                result.getModelList().get(i).getPatientId(), VTConstants.THUMBNAIL_FLAG);
//                    }
                    // rony prescription GC Ends
                }
            }
            Intent intent = new Intent(VTConstants.NOTIFICATION_APPTS_PRESC);
            context.sendBroadcast(intent);


        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }
}
