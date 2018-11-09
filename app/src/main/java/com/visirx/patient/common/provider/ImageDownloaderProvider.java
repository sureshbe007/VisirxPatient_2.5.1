package com.visirx.patient.common.provider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.api.EMRAudioRes;
import com.visirx.patient.api.EMRFileReq;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Suresh on 14-02-2016.
 */
public class ImageDownloaderProvider {


    Context context;

    //for SendProfileImgReq()
    String userId;
    String notificationTitle;

    //for SendEmrThumbImgReq()
    int appointmentId;
    String createdAt;
    String patientId;
    String imageTypeFlag;

    //for audio saving process.
    String filemimetype;

    public ImageDownloaderProvider(Context context, String notificationTitle) {
        super();
        this.context = context;
        this.notificationTitle = notificationTitle;
    }

    public void SendProfileImgReq(String userId) {
        try {

            if (VTConstants.checkAvailability(context)) {
                new profileImgTask().execute(userId);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void SendEmrImgReq(int appointmentId, String createdAt,
                              String patientId, String ImageTypeFlag) {
        this.appointmentId = appointmentId;
        this.createdAt = createdAt;
        this.patientId = patientId;
        this.imageTypeFlag = ImageTypeFlag;
        new emrImgTask().execute();

    }

    private class emrImgTask extends AsyncTask<String, Integer, Bitmap> {

        Bitmap emrBitmap = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.d("SPIN", "inside emrImgTask : appt id  : " + appointmentId);
            //userId = params[0];
            //fill blank space with %20 - to avoid the issue with the space when sending timestamp through GET method
            String createdAtModified = VTConstants.replace(createdAt);
            String requestPart = VTConstants.GET_EMR_IMAGE_SERVLET + "?"
                    + VTConstants.GET_EMR_IMAGE_PATIENTID + "=" + patientId + "&"
                    + VTConstants.GET_EMR_IMAGE_CREATED_AT + "=" + createdAtModified + "&"
                    + VTConstants.GET_EMR_IMAGE_APPOINTMENTID + "="
                    + appointmentId + "&" + VTConstants.GET_IMAGE_TYPE + "=" + imageTypeFlag;
            Log.d("SPIN", "inside emrImgTask : sending url : " + HTTPUtils.gURLBase + requestPart);
            try {
                Log.d("SPIN", "inside emrImgTask : send picasso request. start");
                emrBitmap = Picasso.with(context)
                        .load(HTTPUtils.gURLBase + requestPart).get();
                Log.d("SPIN", "inside emrImgTask : send picasso request. Ends");

            } catch (IOException e) {
                Log.d("SPIN", "inside emrImgTask : send picasso request. Exception : ");
                e.printStackTrace();
            }
            return emrBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
//rony 1.3.5.1 starts
            try {
                Log.d("SPIN", "Download Emr thumb img  from server  Done.");
                //Toast.makeText(context, "download Done", Toast.LENGTH_SHORT).show();
                if (imageTypeFlag.equals(VTConstants.THUMBNAIL_FLAG)) {
                    if (emrBitmap != null) {
                        Log.d("SPIN", "emr thumb image  : valid bitmap received");
                        String thumbPath = saveEmrImageThumbnail(emrBitmap);
                        Log.d("SPIN", "emr thumb image  : stored thumb path : " + thumbPath);
                        boolean isUpdatedInDb = VisirxApplication.aptEmrDAO.updateEmrThumbImagePath(thumbPath, patientId, appointmentId, createdAt);
                        if (isUpdatedInDb) {
                            Log.d("SPIN", "emr thumb image update Process Success.----------"+  notificationTitle);
                            Intent intent = new Intent(notificationTitle);
                            context.sendBroadcast(intent);

                        } else {
                            Log.d("SPIN", "emr thumb image update Process failed.");
                        }
                    } else {
                        Log.d("SPIN", "emr thumb image  : NULL bitmap received");
                    }

                } else if (imageTypeFlag.equals(VTConstants.FULL_IMAGE_FLAG)) {
                    if (emrBitmap != null) {
                        String fullImgPath = saveEmrFullImage(emrBitmap);
                        boolean isUpdatedInDb = VisirxApplication.aptEmrDAO.updateEmrFullImagePath(fullImgPath, patientId, appointmentId, createdAt);
                        if (isUpdatedInDb) {
                            Log.d("SPIN", "emr full image update Process success.");
                            Intent intent1 = new Intent(notificationTitle);
                            context.sendBroadcast(intent1);

                        } else {
                            Log.d("SPIN", "emr full image update Process failed.");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //rony 1.3.5.1 ends
        }

    }

    private class profileImgTask extends AsyncTask<String, Integer, Bitmap> {

        private Bitmap map = null;

        // rony - EMRFRAGMENT GC - Ends
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Log.d("SPIN", "inside profileImgTask : user id : " + params[0]);
            userId = params[0];
            //http://192.168.0.110:8080/VisiRx/GetProfileImageForUserServlet?userid=000002&imagetype=THUMBNAIL
            String requestPart = VTConstants.GET_IMAGE_SERVLET + "?"
                    + VTConstants.GET_IMAGE_USERID + "=" + params[0] + "&"
                    + VTConstants.GET_IMAGE_TYPE + "="
                    + VTConstants.THUMBNAIL_FLAG;
            Log.d("SPIN", "inside profileImgTask : sending url : " + HTTPUtils.gURLBase + requestPart);
            System.out.println("==PPP  loginModel 4" + requestPart);
            try {
                map = Picasso.with(context)
                        .load(HTTPUtils.gURLBase + requestPart).get();

            } catch (IOException e) {
                Log.d("SPIN", "INVALID BITMAP RETRIEVED FROM SERVER.");
                //e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            Log.d("SPIN", "Download profile img thumbnail from server  Done.");
            //Toast.makeText(context, "download Done", Toast.LENGTH_SHORT).show();
            Log.d("SPIN", "Inside profile img req  :begning  :."+notificationTitle );
            if (notificationTitle.equals(VTConstants.NOTIFICATION_LOGIN_USER_PROFILE)) {
                if (map != null)
                    Log.d("SPIN", "IMAGE_NOTIFY 1  :."+notificationTitle );
                {
                    String thumbPath = saveProfileImageThumbnail(map);
                    boolean isUpdatedInDb = VisirxApplication.userRegisterDAO.updateLoggedUserThumbImagePath(thumbPath, userId);
                    if (isUpdatedInDb) {

                        Log.d("SPIN", "updateLoggedUserThumbImagePath from SQLITE  :."+notificationTitle );
                        //set the path to application variable
//                        VisirxApplication.appContext.getLoggedUser().setImageThumbnailPath(thumbPath);
                        Intent intent = new Intent(notificationTitle);
                        context.sendBroadcast(intent);
                    } else {
                        Log.d("SPIN", "Process failed.");
                    }
                }
            } else if (notificationTitle.equals(VTConstants.NOTIFICATION_APPTS)) {
                if (map != null) {
                    String thumbPath = saveProfileImageThumbnail(map);

                    boolean isUpdatedInDb = VisirxApplication.customerDAO.updateCustomerThumbImagePath(thumbPath, userId);

                    if (isUpdatedInDb) {

                        Log.d("SPIN", "Inside profile img req  :thumb path :."+thumbPath );
                        Log.d("SPIN", "Inside profile img req  :receiver :."+notificationTitle );

                        Intent intent = new Intent(notificationTitle);
                        context.sendBroadcast(intent);
                    } else {
                        Log.d("SPIN", "Process failed.");
                    }

                }
            } else if (notificationTitle.equals(VTConstants.NOTIFICATION_NURSE_ASSIGNED)) {
                if (map != null) {
                    String thumbPath = saveProfileImageThumbnail(map);

                    boolean isUpdatedInDb = VisirxApplication.customerDAO.updateCustomerThumbImagePath(thumbPath, userId);

                    if (isUpdatedInDb) {

                        Intent intent = new Intent(notificationTitle);
                        context.sendBroadcast(intent);
                    } else {
                        Log.d("SPIN", "Process failed.");
                    }

                }

            }
        }

    }

    private String saveProfileImageThumbnail(Bitmap mapmap) {
        String imageUrl = "";
        try {
            if(mapmap!=null){


            //create profile image thumbnail directory if needed.
            String storageDirectoryPath = VTConstants.createDirectoryProfileImageThumnail();
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
            imageUrl = storageDirectoryPath + File.separator + VTConstants.SAVED_IMAGE_PREFIX + userId + timeStamp + ".jpg";
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            mapmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            File F = new File(imageUrl);

            F.createNewFile();
            FileOutputStream fos = new FileOutputStream(F);
            fos.write(bytes.toByteArray());
            fos.close();
            }
            else {
                Bitmap defaultIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_image);
            }
			/*Toast.makeText(context, "image saved successfully...",
                    Toast.LENGTH_SHORT).show();*/
            Log.d("SPIN", "Writing profile img thumbnail to phone memory  Done.");


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageUrl;
    }

    private String saveEmrImageThumbnail(Bitmap mapmap) {
        //create profile image thumbnail directory if needed.
        String storageDirectoryPath = VTConstants.createDirectoryEMRImageThumbnail();
        Log.d("SPIN", "Image thum dir path : " + storageDirectoryPath);
        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.getDefault()).format(new Date());
        String imageUrl = storageDirectoryPath + File.separator + VTConstants.SAVED_IMAGE_PREFIX + patientId + createdAt + ".jpg";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mapmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File F = new File(imageUrl);
        try {
            F.createNewFile();
            FileOutputStream fos = new FileOutputStream(F);
            fos.write(bytes.toByteArray());
            fos.close();

			/*Toast.makeText(context, "image saved successfully...",
                    Toast.LENGTH_SHORT).show();*/
            Log.d("SPIN", "Writing profile img thumbnail to phone memory  Done.");


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageUrl;
    }

    private String saveEmrFullImage(Bitmap mapmap) {
        //create profile image thumbnail directory if needed.
        String storageDirectoryPath = VTConstants.createDirectoryEMRImages();
        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.getDefault()).format(new Date());
        String imageUrl = storageDirectoryPath + File.separator + VTConstants.SAVED_IMAGE_PREFIX + patientId + createdAt + ".jpg";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mapmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File F = new File(imageUrl);
        try {
            F.createNewFile();
            FileOutputStream fos = new FileOutputStream(F);
            fos.write(bytes.toByteArray());
            fos.close();

		/*Toast.makeText(context, "image saved successfully...",
                Toast.LENGTH_SHORT).show();*/
            Log.d("SPIN", "Writing Emr full img  to phone memory  Done.");


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageUrl;
    }

    public void SendAudioReq(int appointmentId2, String createdAt2,
                             String patientId2, String filetype) {
        this.appointmentId = appointmentId2;
        this.createdAt = createdAt2;
        this.patientId = patientId2;
        this.filemimetype = filetype;
        try {
            if (VTConstants.checkAvailability(context)) {
                EMRFileReq appointmentReq = new EMRFileReq();
                appointmentReq.setAppointmentId(String.valueOf(appointmentId));
                appointmentReq.setPatientId(patientId);
                appointmentReq.setEmrFilesLastUpdated(createdAt);
                new GetAudioFileTask().execute(appointmentReq);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }


    }

    private class GetAudioFileTask extends AsyncTask<EMRFileReq, Integer, EMRAudioRes> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showWait();
        }

        @Override
        protected EMRAudioRes doInBackground(EMRFileReq... params) {
            EMRAudioRes result = (EMRAudioRes) HTTPUtils.getDataFromServer(
                    params[0], EMRFileReq.class, EMRAudioRes.class,
                    "GetEMRAudioServlet");
            return result;
        }

        @Override
        protected void onPostExecute(EMRAudioRes result) {
            super.onPostExecute(result);
            ProcessEMRAudioResponse(result);
            // cancelWait();
        }

        private void ProcessEMRAudioResponse(EMRAudioRes result) {
            try {
                if (result == null) {
                    Popup.ShowErrorMessage(context, R.string.error_server_connect,
                            Toast.LENGTH_SHORT);
                    return;
                } else {
                    if (result.getEmrFile() != null) {
                        Log.d("SPIN", "Fetch audio : Success - store to file system.");
                        //create audio file directory if needed.
                        String storageDirectoryPath = VTConstants.createDirectoryEMRAuscultation();
                        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.getDefault()).format(new Date());
                        //remove fraction second to avoid error in .mp3 format
                        Date createdAtDate = VTConstants.datetimeFormat.parse(createdAt);
                        String modifiedCreatedAt = VTConstants.formateTimestamp(createdAtDate);
                        String audioFilePath = storageDirectoryPath + File.separator + VTConstants.SAVED_AUDIO_PREFIX + patientId + modifiedCreatedAt + "." + filemimetype;
                        try {
                            File path = new File(audioFilePath);
                            FileOutputStream fos = new FileOutputStream(path);
                            fos.write(result.getEmrFile());
                            fos.close();
                            boolean isUpdatedInDb = VisirxApplication.aptEmrDAO.updateEmrAudioPath(audioFilePath, patientId, appointmentId, createdAt);
                            if (isUpdatedInDb) {
                                Log.d("SPIN", "emr full image update Process success.");
                                Intent intent1 = new Intent(notificationTitle);
                                context.sendBroadcast(intent1);

                            } else {
                                Log.d("SPIN", "emr full image update Process failed.");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.d("SPIN", "Fetch audio : NULL");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    public void SendPrescriptionImgReq(int appointmentId2, String createdAt2,
                                       String patientId2, String fullImageFlag) {
        try {
            if (VTConstants.checkAvailability(context)) {
                this.appointmentId = appointmentId2;
                this.createdAt = createdAt2;
                this.patientId = patientId2;
                this.imageTypeFlag = fullImageFlag;
                new prescriptionImgTask().execute();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private class prescriptionImgTask extends AsyncTask<String, Integer, Bitmap> {

        Bitmap prescBitmap = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.d("SPIN", "inside prescriptionImgTask : appt id  : " + appointmentId);
            //userId = params[0];
            //fill blank space with %20 - to avoid the issue with the space when sending timestamp through GET method
            String createdAtModified = VTConstants.replace(createdAt);
            String requestPart = VTConstants.GET_PRESCRIPTION_IMAGE_SERVLET + "?"
                    + VTConstants.GET_EMR_IMAGE_PATIENTID + "=" + patientId + "&"
                    + VTConstants.GET_EMR_IMAGE_CREATED_AT + "=" + createdAtModified + "&"
                    + VTConstants.GET_EMR_IMAGE_APPOINTMENTID + "="
                    + appointmentId + "&" + VTConstants.GET_IMAGE_TYPE + "=" + imageTypeFlag;
            Log.d("SPIN", "inside prescriptionImgTask : sending url : " + HTTPUtils.gURLBase + requestPart);
            try {
                Log.d("SPIN", "inside prescriptionImgTask : send picasso request. start");
                prescBitmap = Picasso.with(context)
                        .load(HTTPUtils.gURLBase + requestPart).get();
                Log.d("SPIN", "inside prescriptionImgTask : send picasso request. Ends");

            } catch (IOException e) {
                Log.d("SPIN", "inside prescriptionImgTask : send picasso request. Exception : ");
                e.printStackTrace();
            }
            return prescBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            Log.d("SPIN", "Download prescriptionImgTask img  from server  Done.");
            //Toast.makeText(context, "download Done", Toast.LENGTH_SHORT).show();
            if (prescBitmap != null) {
                if (imageTypeFlag.equals(VTConstants.THUMBNAIL_FLAG)) {
                    String thumbPath = savePrescriptionThumbImage(prescBitmap);
                    boolean isUpdatedInDb = VisirxApplication.aptPresDAO.updatePrescriptionThumbImagePath(thumbPath, patientId, appointmentId, createdAt);
                    if (isUpdatedInDb) {
                        Log.d("SPIN", "emr thumb image update Process Success.");
                        Intent intent = new Intent(notificationTitle);
                        context.sendBroadcast(intent);

                    } else {
                        Log.d("SPIN", "emr thumb image update Process failed.");
                    }

                } else if (imageTypeFlag.equals(VTConstants.FULL_IMAGE_FLAG)) {
                    String fullImgPath = savePrescriptionFullImage(prescBitmap);
                    boolean isUpdatedInDb = VisirxApplication.aptPresDAO.updatePrescFullImagePath(fullImgPath, patientId, appointmentId, createdAt);
                    if (isUpdatedInDb) {
                        Log.d("SPIN", "emr full image update Process success.");
                        Intent intent1 = new Intent(notificationTitle);
                        context.sendBroadcast(intent1);

                    } else {
                        Log.d("SPIN", "emr full image update Process failed.");
                    }

                }
            }

        }

    }

    private String savePrescriptionThumbImage(Bitmap mapmap) {
        //create profile image thumbnail directory if needed.
        String storageDirectoryPath = VTConstants.createDirectoryPrescriptionThumbnail();
        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.getDefault()).format(new Date());
        String imageUrl = storageDirectoryPath + File.separator + VTConstants.SAVED_IMAGE_PREFIX + patientId + createdAt + ".jpg";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mapmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File F = new File(imageUrl);
        try {
            F.createNewFile();
            FileOutputStream fos = new FileOutputStream(F);
            fos.write(bytes.toByteArray());
            fos.close();

			/*Toast.makeText(context, "image saved successfully...",
                    Toast.LENGTH_SHORT).show();*/
            Log.d("SPIN", "Writing prescription thumb img  to phone memory  Done.");


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageUrl;
    }

    private String savePrescriptionFullImage(Bitmap mapmap) {
        //create profile image thumbnail directory if needed.
        String storageDirectoryPath = VTConstants.createDirectoryPrescription();
        //String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.getDefault()).format(new Date());
        String imageUrl = storageDirectoryPath + File.separator + VTConstants.SAVED_IMAGE_PREFIX + patientId + createdAt + ".png";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mapmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File F = new File(imageUrl);
        try {
            F.createNewFile();
            FileOutputStream fos = new FileOutputStream(F);
            fos.write(bytes.toByteArray());
            fos.close();

			/*Toast.makeText(context, "image saved successfully...",
                    Toast.LENGTH_SHORT).show();*/
            Log.d("SPIN", "Writing prescription full img  to phone memory  Done.");


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageUrl;
    }


}
