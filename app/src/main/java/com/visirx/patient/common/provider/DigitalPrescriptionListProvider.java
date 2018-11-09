package com.visirx.patient.common.provider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.DigitalPrescriptionListReq;
import com.visirx.patient.api.DigitalPrescriptionListRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AppDigitalprescriptionModel;
import com.visirx.patient.model.DigitalprescriptionDataModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Lenovo on 6/29/2016.
 */
public class DigitalPrescriptionListProvider {


    static final String TAG = PrescriptionListProvider.class.getName();
    Context context;
    SharedPreferences sharedPreferences;
    String PatientID;

    public DigitalPrescriptionListProvider(Context context) {
        super();
        this.context = context;
    }


    public void SendDigitalPrescriptionListReq(int appointmentId, String patientId, String presMaxDate) {
        try {
            Log.d("DigitalPREListProvider", " SendDigitalPrescriptionListReq   appointmentId" + appointmentId);
            Log.d("DigitalPREListProvider", " SendDigitalPrescriptionListReq   patientId" + patientId);
            Log.d("DigitalPREListProvider", " SendDigitalPrescriptionListReq   presMaxDate " + presMaxDate);

            sharedPreferences = context.getSharedPreferences(
                    VTConstants.LOGIN_PREFRENCES_NAME, 0);
            sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            PatientID = sharedPreferences.getString(VTConstants.USER_ID, null);
            if (VTConstants.checkAvailability(context)) {
                RequestHeader requestMessageHeader = new RequestHeader();
                requestMessageHeader.setUserId(PatientID);

                DigitalPrescriptionListReq digitalPrescriptionListReq = new DigitalPrescriptionListReq();
                digitalPrescriptionListReq.setAppointmentId(appointmentId);
                digitalPrescriptionListReq.setPatientId(patientId);
                digitalPrescriptionListReq.setPrescriptionLastUpdated(presMaxDate);
                digitalPrescriptionListReq.setRequestHeader(requestMessageHeader);

                new ApptPrescriptionTask().execute(digitalPrescriptionListReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }


    private class ApptPrescriptionTask extends AsyncTask<DigitalPrescriptionListReq, Integer, DigitalPrescriptionListRes> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("DigitalPREListProvider", "   onPreExecute ");

        }

        @Override
        protected DigitalPrescriptionListRes doInBackground(DigitalPrescriptionListReq... params) {
            DigitalPrescriptionListRes result = (DigitalPrescriptionListRes) HTTPUtils.getDataFromServer(params[0], DigitalPrescriptionListReq.class,
                    DigitalPrescriptionListRes.class, "GetDigitalprescriptionListAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(DigitalPrescriptionListRes result) {
            super.onPostExecute(result);
            ProcessPrescriptionListResponse(result);
            Log.d("DigitalPREListProvider", "   onPostExecute 212121 ");
        }

        private void ProcessPrescriptionListResponse(DigitalPrescriptionListRes result) {
            try {

                Log.d("DigitalPREListProvider", "   ProcessPrescriptionListResponse ");
                // TODO Auto-generated method stub
                if (result == null) {

                    Log.d("DigitalPREListProvider", "   result == null ");
                    Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                    return;
                } else if (result.getResponseHeader().getResponseCode() != 0) {

                    Log.d("DigitalPREListProvider", "   result !=0 " + result.getResponseHeader().getResponseCode());
                    Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                    //Popup.ShowErrorMessage(context,R.string.error_trip_list,Toast.LENGTH_LONG);
                    return;
                } else {

                    try {

//                   Log.d("DigitalPREListProvider","    != null   "+ result.getResponseHeader().getResponseCode() );
//                    Log.d("DigitalPREListProvider","   result.getDpGetResData().size() BEFORE  "+ result.getDpGetResData().size());
                        if (result.getDpGetResData() != null)

                            Log.d("DigitalPREListProvider", "   result.getDpGetResData().size() AFTER " + result.getDpGetResData().size());
                        for (int i = 0; i < result.getDpGetResData().size(); i++) {
                            AppDigitalprescriptionModel model = result.getDpGetResData().get(i);
                            model.setPatientId(model.getPatientId());
                            model.setCreatedAtclient(model.getCreatedAtclient());
                            model.setCreatedAtServer(model.getCreatedAtServer());
                            model.setAppointmentId(model.getAppointmentId());
                            model.setPrescriptionId(model.getPrescriptionId());
                            model.setIsDeleted(model.getIsDeleted());
                            model.setProcessFlag(VTConstants.PROCESSED_FLAG_SENT);

                            boolean isMainTableUpdated = VisirxApplication.digitalMainTableDAO.insertPrescription(model);

                            if (isMainTableUpdated) {

//                            DigitalprescriptionDataModel model, String patientID, String AppointmentID, String createdatclient
                                for (int j = 0; j < result.getDpGetResData().get(i).getDpDataModel().size(); j++) {
                                    DigitalprescriptionDataModel digitalprescriptionDataModel = result.getDpGetResData().get(i).getDpDataModel().get(j);

                                    digitalprescriptionDataModel.setCreatedAt(digitalprescriptionDataModel.getCreatedAt());
                                    digitalprescriptionDataModel.setDrugName(digitalprescriptionDataModel.getDrugName());
                                    digitalprescriptionDataModel.setDosage(digitalprescriptionDataModel.getDosage());
                                    digitalprescriptionDataModel.setMedicineIntake(digitalprescriptionDataModel.getMedicineIntake());
                                    digitalprescriptionDataModel.setMorning(digitalprescriptionDataModel.getMorning());
                                    digitalprescriptionDataModel.setNoon(digitalprescriptionDataModel.getNoon());
                                    digitalprescriptionDataModel.setEvening(digitalprescriptionDataModel.getEvening());
                                    digitalprescriptionDataModel.setNight(digitalprescriptionDataModel.getNight());

//

                                    boolean insertSubTableFlag = VisirxApplication.digitaTableDAO.insertPrescription(digitalprescriptionDataModel,

                                            result.getDpGetResData().get(i).getPatientId(), String.valueOf(result.getDpGetResData().get(i).getAppointmentId()), result.getDpGetResData().get(i).getCreatedAtclient());

                                    if (insertSubTableFlag) {
                                        Log.d("DigitalPREListProvider", " SUBTABLE ENTRY SUCCESSFULL");
                                    } else {
                                        Log.d("DigitalPREListProvider", " SUBTABLE NOT ENTRY SUCCESSFULL");
                                    }


                                }

                                Log.d("DigitalPREListProvider", " MAINTABLE ENTRY SUCCESSFULL");
                            } else {
                                Log.d("DigitalPREListProvider", " MAINTABLE ENTRY NOT SUCCESSFULL");

                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(VTConstants.NOTIFICATION_APPTS_PRESC);
                context.sendBroadcast(intent);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }


        }


    }

}
