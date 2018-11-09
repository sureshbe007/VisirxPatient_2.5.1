package com.visirx.patient.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.model.AddEmrVitalsModel;
import com.visirx.patient.model.EMRModel;
import com.visirx.patient.model.LoginModel;
import com.visirx.patient.model.RegisterModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.VTConstants;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by aa on 20.1.16.
 */
public class AppContext {

    private RegisterModel registerUser;
    private LoginModel loggedUser;
    private LoginModel logiedUser;
    public static List<AddEmrFileModel> emrImageFileList = null;
    public static List<AddEmrFileModel> emrAudioFileList = null;

    public static int randInt() {
        // Usually this can be a field rather than a method variable
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt(((Integer.MAX_VALUE - 1) - 1) + 1) + 1;
        return randomNum;
    }

    public RegisterModel getRegisterUser() {
        return registerUser;
    }

    public void setRegisterUser(RegisterModel registerUser) {
        this.registerUser = registerUser;
    }

    public LoginModel getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(LoginModel loggedUser) {
        this.loggedUser = loggedUser;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static List<EMRModel> emrList(ArrayList<AddEmrFileModel> emrmodelList) {
        ArrayList<EMRModel> modelList = new ArrayList<EMRModel>();
        if (emrmodelList != null && emrmodelList.size() > 0) {
            int ausculation = 0;
            int xrays = 0;
            String preDate = null;
            boolean sync = true;
            String preCreatedAt = null;
            for (int i = 0; i < emrmodelList.size(); i++) {
                AddEmrFileModel model = emrmodelList.get(i);
//                String currDate = DateFormat.GetFormattedDate_YYYYMMDD(model.getCreatedAt());
//                if (i == 0) {
//                    preDate = currDate;
//                    preCreatedAt = model.getCreatedAt();
//                }
                //  if (currDate.equals(preDate)) {
                if (model.getFileGroup().equalsIgnoreCase("Auscultation")) {
                    ausculation++;
                } else
//                if (model.getFileGroup().equalsIgnoreCase("Image"))
                {
                    xrays++;
                }
            }
                   /* if (model.getProcessedFlag() != VTConstants.PROCESSED_FLAG_SENT_RESPONSE) {
                        sync = false;
                    }*/
               // }
               // if ((!currDate.equals(preDate)) || (i == emrmodelList.size() - 1)) {
                    //int size = VisirxApplication.aptVitalDAO.GetEMRVitalNotSentByDate(model.getPatientId(), model.getAppointmentId(), preDate).size();
//                    modelList.add(getEMRModel("AUSCULTATION", ausculation + " Report", null, preCreatedAt, sync));
                    modelList.add(getEMRModel("IMAGE", xrays + " Report", preCreatedAt, preCreatedAt, sync));
//                    modelList.add(getEMRModel("VITAL SIGN", size + " Report", "", preCreatedAt, sync));
                   /* modelList.add(null);
                    ausculation = 0;
                    xrays = 0;
                    preDate = currDate;
                    sync = true;
                    preCreatedAt = model.getCreatedAt();
                    if (model.getFileGroup().equalsIgnoreCase("Auscultation")) {
                        ausculation++;
                    } else if (model.getFileGroup().equalsIgnoreCase("Image")) {
                        xrays++;
                    }
                    if (model.getProcessedFlag() != VTConstants.PROCESSED_FLAG_SENT_RESPONSE) {
                        sync = false;
                    }*/
                //}

            //}

        }
        return modelList;

    }

    public static List<EMRModel> emrListVitals(String patientId, int appointmentId) {
        ArrayList<EMRModel> modelList = new ArrayList<EMRModel>();
        int ausculation = 0;
        int xrays = 0;
        String preDate = null;
        boolean sync = true;
        String preCreatedAt = null;
        ArrayList<AddEmrVitalsModel> emrmodelList = VisirxApplication.aptVitalDAO.GetPatientEMRVital(patientId, appointmentId);
        for (int i = 0; i < emrmodelList.size(); i++) {
            AddEmrVitalsModel model = emrmodelList.get(i);
            String currDate = DateFormat.GetFormattedDate_YYYYMMDD(model.getCreatedAt());
            if (i == 0) {
                preDate = currDate;
                preCreatedAt = model.getCreatedAt();
            }
            if (model.getProcessedFlag() != VTConstants.PROCESSED_FLAG_SENT_RESPONSE) {
                sync = false;
            }
            if ((!currDate.equals(preDate)) || (i == emrmodelList.size() - 1)) {
                int size = VisirxApplication.aptVitalDAO.GetEMRVitalNotSentByDate(model.getPatientId(), model.getAppointmentId(), preDate).size();
//                modelList.add(getEMRModel("AUSCULTATION", ausculation + " Report", null, preCreatedAt, sync));
                modelList.add(getEMRModel("IMAGE", xrays + " Report", preCreatedAt, preCreatedAt, sync));
//                modelList.add(getEMRModel("VITAL SIGN", size + " Report", "", preCreatedAt, sync));
                modelList.add(null);
                ausculation = 0;
                xrays = 0;
                preDate = currDate;
                sync = true;
                preCreatedAt = model.getCreatedAt();
            }

        }
        return modelList;
    }

    private static EMRModel getEMRModel(String header, String result, String date, String emrDate, boolean sync) {
        EMRModel model = new EMRModel();
        model.setDate(date);
        model.setEmrDate(emrDate);
        model.setHeader(header);
        model.setResult(result);
        model.setSyched(sync);
        return model;
    }
//        public static List<EMRModel> emrListVitals (String patientId, int appointmentId)
//        {
//
//            ArrayList<EMRModel> modelList = new ArrayList<EMRModel>();
//            int ausculation = 0;
//            int xrays = 0;
//            String preDate = null;
//            boolean sync = true;
//            String preCreatedAt = null;
//            ArrayList<AddEmrVitalsModel> emrmodelList = VisirxApplication.aptVitalDAO.GetPatientEMRVital(patientId, appointmentId);
//            for(int i=0; i < emrmodelList.size();i++){
//                AddEmrVitalsModel model = emrmodelList.get(i);
//                String currDate = DateFormat.GetFormattedDate_YYYYMMDD(model.getCreatedAt());
//                if(i==0){
//                    preDate =currDate;
//                    preCreatedAt = model.getCreatedAt();
//                }
//
//                if(model.getProcessedFlag() != VTConstants.PROCESSED_FLAG_SENT_RESPONSE){
//                    sync = false;
//                }
//
//                if((!currDate.equals(preDate)) || (i== emrmodelList.size()-1)){
//                    int size = 	VisirxApplication.aptVitalDAO.GetEMRVitalNotSentByDate(model.getPatientId(), model.getAppointmentId(), preDate).size();
//
//                    modelList.add(getEMRModel("AUSCULTATION", ausculation +" Report",null, preCreatedAt,sync));
//                    modelList.add(getEMRModel("IMAGE", xrays+ " Report", preCreatedAt,preCreatedAt, sync));
//                    modelList.add(getEMRModel("VITAL SIGN", size + " Report" ,"",preCreatedAt, sync));
//                    modelList.add(null);
//                    ausculation = 0;
//                    xrays = 0;
//                    preDate = currDate;
//                    sync = true;
//                    preCreatedAt = model.getCreatedAt();
//                }
//
//            }
//            return modelList;
//        }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setMessage(Message);
        return dialog;
    }
}
