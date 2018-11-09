package com.visirx.patient.common.provider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.Image;
import com.visirx.patient.fragment.PrescriptionFragment;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.VTConstants;

import java.io.File;

/**
 * Created by Administrator on 24-05-2016.
 */
public class DownloadFileProvider {

    static final String TAG = DownloadFileProvider.class.getName();
    public Context context;
    public SharedPreferences loggedPreferance;
    public int appointmentId;
    public String createdAt;
    public String patientId;
    public String providerEmrRequestaction;
    public String providerPresRdequestaction;
    public String imageFlag;
    public String mimeType;
    public String notificationTitle;
    public String url = "";

    public DownloadFileProvider(Context context, String notificationTitle) {
        this.context = context;
        this.notificationTitle = notificationTitle;
    }

    public void downloadEmrFile(int appointmentId, String createdAt, String patientId, String providerEmrRequestaction,
                                String imageFlag, String mimeType) {
        try {
            this.appointmentId = appointmentId;
            this.createdAt = createdAt;
            this.patientId = patientId;
            this.imageFlag = imageFlag;
            this.providerEmrRequestaction = providerEmrRequestaction;
            this.mimeType = mimeType;
            downloadEmrImage();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void downloadPresFile(int appointmentId, String createdAt, String patientId, String providerPresRdequestaction,
                                 String imageFlag, String mimeType) {
        try {
            this.appointmentId = appointmentId;
            this.createdAt = createdAt;
            this.patientId = patientId;
            this.imageFlag = imageFlag;
            this.providerPresRdequestaction = providerPresRdequestaction;
            this.mimeType = mimeType;
            downloadPresImage();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void downloadPresImage() {
        String createdAtModified = VTConstants.replace(createdAt);
        String requestPart = "DownloadMediaFileServlet?"
                + VTConstants.PROVIDER_PATIENTID + "=" + patientId + "&"
                + VTConstants.PROVIDER_CREATEDAT + "=" + createdAtModified + "&"
                + VTConstants.PROVIDER_APPOINTMETID + "="
                + appointmentId + "&" + VTConstants.PROVIDER_REQUESTACTION + "=" + providerPresRdequestaction
                + "&" + VTConstants.GET_IMAGE_TYPE + "=" + imageFlag;


        if (VTConstants.IMAGE_MIME_FORMAT.contains(mimeType) && imageFlag.equals(VTConstants.THUMBNAIL_FLAG)) {
            url = GetPresThumbnailFileStorageUrl();
        } else {
            url = GetPresFileStorageUrl();
        }
        Log.d("SPIN", "Inside downloadImage() : url received - " + requestPart);


        Log.d("SPIN", "Inside downloadImage() : url received - " + url);

        Ion.with(context)
                .load(HTTPUtils.gURLBase + requestPart)

                .write(new File(url))

                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {

                        try
                        {
                        if (imageFlag.equals(VTConstants.THUMBNAIL_FLAG) && PrescriptionFragment.DOWNLOAD_COUNT_PRES > 0) {
                            PrescriptionFragment.DOWNLOAD_COUNT_PRES--;
                        }

                        if (e != null) {
                            Log.d("SPIN", "inside onCompleted() :  Caught exception." + e.getMessage());
                            e.printStackTrace();
                        }

                        Log.d("SPIN", "inside onCompleted() :  emr file download success.");

                        boolean isUpdatedInDb = false;
                        if (VTConstants.IMAGE_MIME_FORMAT.contains(mimeType) && imageFlag.equals(VTConstants.THUMBNAIL_FLAG)) {
                            isUpdatedInDb = VisirxApplication.aptPresDAO.updatePrescriptionThumbImagePath(url, patientId, appointmentId, createdAt);
                        } else {
                            isUpdatedInDb = VisirxApplication.aptPresDAO.updatePrescFullImagePath(url, patientId, appointmentId, createdAt);
                        }


                        if (isUpdatedInDb) {
                            Log.d("SPIN", "inside onCompleted() :  emr file path update Process Success.");

                            Intent intent = new Intent(notificationTitle);
                            context.sendBroadcast(intent);

                        } else {
                            Log.d("SPIN", "emr thumb image update Process failed.");
                        }

                    }catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                }
                });

    }

    private void downloadEmrImage() {

        String createdAtModified = VTConstants.replace(createdAt);

        String requestPart = "DownloadMediaFileServlet?"
                + VTConstants.PROVIDER_PATIENTID + "=" + patientId + "&"
                + VTConstants.PROVIDER_CREATEDAT + "=" + createdAtModified + "&"
                + VTConstants.PROVIDER_APPOINTMETID + "="
                + appointmentId + "&" + VTConstants.PROVIDER_REQUESTACTION + "=" + providerEmrRequestaction
                + "&" + VTConstants.GET_IMAGE_TYPE + "=" + imageFlag;


        if (VTConstants.IMAGE_MIME_FORMAT.contains(mimeType) && imageFlag.equals(VTConstants.THUMBNAIL_FLAG)) {
            url = GetEmrThumbnailFileStorageUrl();
        } else {
            url = GetEmrFileStorageUrl();
        }
        Log.d("SPIN", "Inside downloadImage() : url received - " + requestPart);


        Log.d("SPIN", "Inside downloadImage() : url received - " + url);

        Ion.with(context)
                .load(HTTPUtils.gURLBase + requestPart)

                .write(new File(url))

                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        try {

                            if (imageFlag.equals(VTConstants.THUMBNAIL_FLAG) && Image.DOWNLOAD_COUNT_EMR > 0) {
                                Image.DOWNLOAD_COUNT_EMR--;
                            }

                            if (e != null) {
                                Log.d("SPIN", "inside onCompleted() :  Caught exception." + e.getMessage());
                                e.printStackTrace();
                            }

                            Log.d("SPIN", "inside onCompleted() :  emr file download success.");

                            boolean isUpdatedInDb = false;
                            if (VTConstants.IMAGE_MIME_FORMAT.contains(mimeType) && imageFlag.equals(VTConstants.THUMBNAIL_FLAG)) {
                                isUpdatedInDb = VisirxApplication.aptEmrDAO.updateEmrThumbImagePath(url, patientId, appointmentId, createdAt);
                            } else {
                                isUpdatedInDb = VisirxApplication.aptEmrDAO.updateEmrFullImagePath(url, patientId, appointmentId, createdAt);
                            }


                            if (isUpdatedInDb) {
                                Log.d("SPIN", "inside onCompleted() :  emr file path update Process Success.");

                                Intent intent = new Intent(notificationTitle);
                                context.sendBroadcast(intent);

                            } else {
                                Log.d("SPIN", "emr thumb image update Process failed.");
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }


                });

    }


    private String GetEmrFileStorageUrl() {

        String imageUrl = null;
        try {

            String validTimeString = VTConstants.replaceWhiteSpace(createdAt);

            //create profile image thumbnail directory if needed.
            String storageDirectoryPath = VTConstants.createDirectoryEMRImages();
            //String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.getDefault()).format(new Date());

            imageUrl = storageDirectoryPath + File.separator + mimeType + "_" + patientId + validTimeString + "." + mimeType;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageUrl;
    }


    private String GetEmrThumbnailFileStorageUrl() {

        String imageUrl = null;
        try {

            String validTimeString = VTConstants.replaceWhiteSpace(createdAt);

            //create profile image thumbnail directory if needed.
            String storageDirectoryPath = VTConstants.createDirectoryEMRImageThumbnail();
            //String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.getDefault()).format(new Date());

            imageUrl = storageDirectoryPath + File.separator + mimeType + "_" + patientId + validTimeString + "." + mimeType;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageUrl;
    }


    private String GetPresFileStorageUrl() {

        String imageUrl = null;
        try {

            String validTimeString = VTConstants.replaceWhiteSpace(createdAt);

            //create profile image thumbnail directory if needed.
            String storageDirectoryPath = VTConstants.createDirectoryPrescription();
            //String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.getDefault()).format(new Date());

            imageUrl = storageDirectoryPath + File.separator + mimeType + "_" + patientId + validTimeString + "." + mimeType;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageUrl;
    }

    private String GetPresThumbnailFileStorageUrl() {

        String imageUrl = null;
        try {

            String validTimeString = VTConstants.replaceWhiteSpace(createdAt);
            //create profile image thumbnail directory if needed.
            String storageDirectoryPath = VTConstants.createDirectoryPrescriptionThumbnail();
            //String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",Locale.getDefault()).format(new Date());

            imageUrl = storageDirectoryPath + File.separator + mimeType + "_" + patientId + validTimeString + "." + mimeType;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageUrl;
    }


}
