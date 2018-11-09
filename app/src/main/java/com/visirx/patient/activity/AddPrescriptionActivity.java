package com.visirx.patient.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.AddPrescriptionProvider;
import com.visirx.patient.common.provider.DownloadFileProvider;
import com.visirx.patient.common.provider.ImageDownloaderProvider;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.customview.TouchImageView;
import com.visirx.patient.model.AppointmentModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.ApptPrescriptionModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddPrescriptionActivity extends AppCompatActivity {
    String TAG = AddPrescriptionActivity.class.getName();
    private static final int CAMERA_REQUEST = 1888;
    SharedPreferences loggedPreferance;
    String createdById = null;
    TextView txtHeading;
    private ImageView imageView;
    AppointmentPatientModel appointmentPatientModel = null;
    byte[] byteArrayThumbnail = null;
    final int THUMBNAIL_SIZE = 64;
    boolean prescriptionView = false;
    int reservationNumber = -1;
    public Uri fileUri, prescriptionUri;
    public int MEDIA_TYPE_IMAGE = 1;
    public File mediaFile, prescriptionImgDir, preImagePath;
    ArrayList<ApptPrescriptionModel> prescriptionList = null;
    // rony - Prescription GC  - Starts
    public String imageFilePath = null;
    public String thumbPath = null;
    public String timeStamp;
    public String imageFileName = null;
    public String thumbFileName = null;

    public String patientId = null;
    public String createdAt = null;
    public int filePosition = -1;
    // rony - Prescription GC  - Ends

    boolean photoFlag = false;
    ProgressDialog ringProgressDialog = null;
    Toolbar toolbar;

    public ViewPager viewPager;
    public PresfullScreenImageAdapter fullScreenImageAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription);
        viewPager = (ViewPager) findViewById(R.id.patient_presViewPager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Images");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        patid = (NormalFont) findViewById(R.id.lblApmntid);
//        appDate = (NormalFont) findViewById(R.id.lbldate);
        //  rony - Prescription GC - Starts
        this.registerReceiver(receiverUpdate,
                new IntentFilter(VTConstants.NOTIFICATION_PRESCRIPTION_FULL_IMAGE));
        //  rony - Prescription GC - Ends
        if (getIntent() != null) {
            // rony - Prescription GC  - Starts
            imageFilePath = VTConstants.createDirectoryPrescription();
            thumbPath = VTConstants.createDirectoryPrescriptionThumbnail();
            // rony - Prescription GC  - Ends
            loggedPreferance = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            createdById = loggedPreferance.getString(VTConstants.USER_ID, null);
            prescriptionView = getIntent().getBooleanExtra(VTConstants.PRESCRIPTION_VIEW, false);
            reservationNumber = getIntent().getIntExtra(VTConstants.APPTMODEL_KEY, -1);
            patientId = getIntent().getStringExtra(VTConstants.PATIENT_ID);
            filePosition = getIntent().getIntExtra(VTConstants.FILE_DATA, -1);
//            patid.setText("APPOINMENT ID :" + reservationNumber);
//            appDate.setText("  " + DateFormat.GetFormattedDateStr(getIntent().getStringExtra(VTConstants.PATIENT_DATE)));
            Log.d("SPIN", "Inside AddPrescriptionActivity : getIntent:");
            if (reservationNumber > 0) {
                Log.d("SPIN", "Inside AddPrescriptionActivity : getIntent ResNo :" + reservationNumber);
                prescriptionList = VisirxApplication.aptPresDAO.GetPatientPrescription(patientId, reservationNumber);
                Log.d("SPIN", "Inside AddPrescriptionActivity : getIntent prescriptionList :" + prescriptionList.size());
            }

        }
        appointmentPatientModel = VisirxApplication.aptDAO.GetAppointmentsByID(reservationNumber);
//        this.imageView = (ImageView) this.findViewById(R.id.imagePrescription);
        if (appointmentPatientModel != null) {
            //CREATE DIRECTORY
            prescriptionImgDir = new File(imageFilePath);
            patientDirectory();

        }
        if (getIntent() != null && VTConstants.CAMERACLICK) {
            VTConstants.CAMERACLICK = false;
            try {
                photoFlag = false;
                // rony - Prescription GC  - Starts
                timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
                imageFileName = imageFilePath + VTConstants.FILESEPARATOR
                        + VTConstants.SAVED_IMAGE_PREFIX
                        + appointmentPatientModel.getReservationNumber()
                        + createdById + timeStamp
                        + ".jpg";
                thumbFileName = thumbPath + VTConstants.FILESEPARATOR
                        + VTConstants.SAVED_IMAGE_PREFIX
                        + appointmentPatientModel.getReservationNumber()
                        + createdById + timeStamp
                        + ".jpg";
                // rony - Prescription GC  - Ends
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } catch (Exception e) {
                LogTrace.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        // rony - Prescription GC  - Ends
        Button buttonSave = (Button) findViewById(R.id.savePrescription);
        Button buttonCancel = (Button) findViewById(R.id.cancelPrescription);
        //Action for prescription image display - alone
        if (prescriptionView) {

            try {

                fullScreenImageAdapter = new PresfullScreenImageAdapter();
                viewPager.setAdapter(fullScreenImageAdapter);
                viewPager.setCurrentItem(filePosition);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        toolbar.setTitle("PRES " + String.valueOf(position + 1));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();


            }

        }
//        if (appointmentPatientModel.getStatus().equalsIgnoreCase(VTConstants.APPT_CANCELLED)) {
//            buttonSave.setVisibility(View.GONE);
//            buttonCancel.setVisibility(View.GONE);
//        }

    }

    public class PresfullScreenImageAdapter extends PagerAdapter {

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return prescriptionList.size();
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((FrameLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View viewLayout = View.inflate(AddPrescriptionActivity.this, R.layout.layout_fullscreen_presimage, null);

            TouchImageView imgDisplay;
            ProgressBar progressBar;

            ImageView viewPagerimageView = (ImageView) viewLayout.findViewById(R.id.presviewer_file_image);
            Button viewPagerbutton = (Button) viewLayout.findViewById(R.id.presviewer_file_button);

            imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.pres_imgDisplay);

            progressBar = (ProgressBar) viewLayout.findViewById(R.id.viewpager_prespagerProgress);
            progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#379674"), android.graphics.PorterDuff.Mode.SRC_ATOP);

            try {


                if (prescriptionList.get(position).getPreimageName() != null) {

                    File file = new File(prescriptionList.get(position).getPreimageName());
                    if (!file.exists()) {
                        Log.d("SPIN", " file.exists()" + prescriptionList.get(position).getPreimageName());
                        imageDownload(position);
                        progressBar.setVisibility(View.VISIBLE);
                        Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.visirx_default);
                        imgDisplay.setImageBitmap(defaultIcon);

                    } else {
                        Log.d("FULLIMAGEADAPTE", " else" + prescriptionList.get(position).getPreimageName());
                        progressBar.setVisibility(View.GONE);
                        if (VTConstants.PDF_FILE_FORMAT.contains(prescriptionList.get(position).getFileMimeType())) {

                            imgDisplay.setVisibility(View.GONE);
                            viewPagerimageView.setVisibility(View.VISIBLE);
                            viewPagerbutton.setVisibility(View.VISIBLE);
                            viewPagerimageView.setImageResource(R.drawable.ic_new_pdf);


                        } else if (VTConstants.DOC_FILE_FORMAT.contains(prescriptionList.get(position).getFileMimeType())) {

                            imgDisplay.setVisibility(View.GONE);
                            viewPagerimageView.setVisibility(View.VISIBLE);
                            viewPagerbutton.setVisibility(View.VISIBLE);
                            viewPagerimageView.setImageResource(R.drawable.ic_doc);

                        } else if (VTConstants.TXT_FILE_FORMAT.contains(prescriptionList.get(position).getFileMimeType())) {


                            imgDisplay.setVisibility(View.GONE);
                            viewPagerimageView.setVisibility(View.VISIBLE);
                            viewPagerbutton.setVisibility(View.VISIBLE);
                            viewPagerimageView.setImageResource(R.drawable.ic_txt);


                        } else if (VTConstants.XLSX_FILE_FORMAT.contains(prescriptionList.get(position).getFileMimeType())) {

                            imgDisplay.setVisibility(View.GONE);
                            viewPagerimageView.setVisibility(View.VISIBLE);
                            viewPagerbutton.setVisibility(View.VISIBLE);
                            viewPagerimageView.setImageResource(R.drawable.ic_xlsx);

                        } else {
                            viewPagerimageView.setVisibility(View.GONE);
                            viewPagerbutton.setVisibility(View.GONE);
                            imgDisplay.setVisibility(View.VISIBLE);

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            Bitmap bitmap = BitmapFactory.decodeFile(prescriptionList.get(position).getPreimageName(), options);
                            imgDisplay.setImageBitmap(bitmap);
                        }
                    }
                } else {
                    imageDownload(position);
                    progressBar.setVisibility(View.VISIBLE);
                    Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.visirx_default);
                    imgDisplay.setImageBitmap(defaultIcon);
                }


                if (!VTConstants.checkAvailability(AddPrescriptionActivity.this)) {
                    progressBar.setVisibility(View.GONE);
                }

                viewPagerbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (VTConstants.FILE_MIME_FORMAT.contains(prescriptionList.get(position).getFileMimeType())) {

                            try {

                                if (prescriptionList.get(position).getPreimageName() == null) {
                                    //give defaut image
//                                holder.imageThumnail.setImageResource(R.drawable.visirx_default);
                                    Toast.makeText(AddPrescriptionActivity.this, "file does not exists.", Toast.LENGTH_SHORT).show();
                                } else {

                                    File file = new File(prescriptionList.get(position).getPreimageName());

                                    if (file.exists()) {
//                                    holder.imageThumnail.setImageResource(R.drawable.ic_new_pdf);
                                        File targetFile = new File(prescriptionList.get(position).getPreimageName());
                                        Uri targetUri = Uri.fromFile(targetFile);
                                        Intent intent;
                                        intent = new Intent(Intent.ACTION_VIEW);
                                        Log.v("FILESTORED", "file content type :. 1 :" + prescriptionList.get(position).getFileType().toString());
                                        intent.setDataAndType(targetUri, prescriptionList.get(position).getFileType());
                                        startActivity(intent);
                                    } else {

//                                    holder.imageThumnail.setImageResource(R.drawable.visirx_default);
                                        Toast.makeText(AddPrescriptionActivity.this, "file does not exists", Toast.LENGTH_SHORT).show();
                                    }


                                }

                            } catch (ActivityNotFoundException e) {

                                //Do  to create alert box for application not found

                                Toast.makeText(AddPrescriptionActivity.this, "No compatible applications found",
                                        Toast.LENGTH_SHORT).show();
                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }

                        }

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            ((ViewPager) container).addView(viewLayout);
            return viewLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((FrameLayout) object);

        }

    }

    public void imageDownload(int filePosition) {

        ApptPrescriptionModel prescModel = prescriptionList.get(filePosition);
        createdAt = prescModel.getCreatedAt();


        if (VTConstants.checkAvailability(AddPrescriptionActivity.this)) {
            snackBarHide();


            DownloadFileProvider downloadFileProvide = new DownloadFileProvider(AddPrescriptionActivity.this,
                    VTConstants.NOTIFICATION_PRESCRIPTION_FULL_IMAGE);
            downloadFileProvide.downloadPresFile(reservationNumber,
                    createdAt,
                    patientId, VTConstants.PROVIDER_PRES_REQUESTACTION,
                    VTConstants.FULL_IMAGE_FLAG, prescModel.getFileMimeType());

        } else {

            snackBarShow();

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
// rony - Prescription GC  - Starts


    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LogTrace.i(TAG, "Appts EMR broadcast received...");
                prescriptionList = VisirxApplication.aptPresDAO.GetPatientPrescription(patientId, reservationNumber);
                fullScreenImageAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }
    };

    @Override
    public void onDestroy() {
        try {
            this.unregisterReceiver(receiverUpdate);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        super.onDestroy();
    }

    public void CancelPrescription(View view) {
        try {
            photoFlag = false;
            File delete = new File(imageFileName);
            delete.delete();
            finish();
        } catch (Exception e) {
            LogTrace.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //rony - 1.3.4 - starts
        try {
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                photoFlag = true;
                Uri imgUri = Uri.fromFile(mediaFile);
                Bitmap bmp = null;
                try {
                    compressImage(imgUri.toString());
                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bmp);
                sendPrescription();
            } else {
                photoFlag = false;
                File delete = new File(imageFileName);
                delete.delete();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Popup.ShowErrorMessageString(getBaseContext(), "Something went wrong.Please try again.", Toast.LENGTH_SHORT);
        }
        //rony - 1.3.4 - Ends
    }

    private void sendPrescription() {
        try {
            if (!photoFlag) {
                Popup.ShowErrorMessageString(getBaseContext(), "No prescription image found to save", Toast.LENGTH_SHORT);
                return;
            }
            SharedPreferences loggedPreferance = getSharedPreferences(
                    VTConstants.PREFS_LOGGEDUSERID, 0);
            Log.d("SPIN", "Inside sendPrescription : 1");
            ApptPrescriptionModel model = new ApptPrescriptionModel();
            model.setAppointmentId(appointmentPatientModel.getReservationNumber());
            model.setCreatedAt(DateFormat.getDateStr(new Date()));
            model.setCreatedById(loggedPreferance.getString(VTConstants.LOGGED_USERID, null));
            //model.setCreatedById(VisirxApplication.appContext.getLoggedUser().getUserId());
            model.setPatientId(createdById);
            model.setSymptoms(appointmentPatientModel.getSymptoms());
            Log.d("SPIN", "Inside sendPrescription : 2 - media file :" + String.valueOf(mediaFile));
            Log.d("SPIN", "Inside sendPrescription : 3 - thumb file :" + thumbFileName);
            model.setPreimageName(String.valueOf(mediaFile));
            model.setPreThumbImageName(thumbFileName);
//			model.setSymptoms(appointmentModel.getPrescriptionImage());
//			model.setImageThumbnail(byteArrayThumbnail);
//			model.setPrescriptionImage(byteArray);
            // rony - Prescription GC  - Starts
            boolean flag = VisirxApplication.aptPresDAO.insertAppointmentPrescription(model, VTConstants.PROCESSED_FLAG_NOT_SENT);
            Log.d("SPIN", "Inside sendPrescription : 3 - insert flag :" + flag);
            if (flag) {
//                Popup.ShowSuccessMessage(this,R.string.prescription_saved_sucessfully,Toast.LENGTH_SHORT);
                finish();
                prescriptionList = VisirxApplication.aptPresDAO.GetPatientPrescription(createdById, appointmentPatientModel.getReservationNumber());
                AddPrescriptionProvider prescriptionProvider = new AddPrescriptionProvider(getBaseContext());
                prescriptionProvider.SendApptPrescriptionReq(appointmentPatientModel.getReservationNumber(), prescriptionList.size());
                VTConstants.isInsertPrescriptionAction = true;
                Intent intent = new Intent(VTConstants.NOTIFICATION_APPTS_PRESC);
                sendBroadcast(intent);
            }

        } catch (Exception e) {
            LogTrace.e(TAG, e.getMessage());
            e.printStackTrace();
        }

    }
    // rony - Prescription GC  - Ends
    // rony - Prescription GC  - Starts

    public String compressImage(String imageUri) throws IOException {
        String filePath = imageFileName;//getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
//		by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//		you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
//		max Height and width values of the compressed image is taken as 816x612
        float maxHeight = 1024.0f;
        float maxWidth = 768.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
//		width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
//		setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = 1; //calculateInSampleSize(options, actualWidth, actualHeight);
//		inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[16 * 1024];
        try {
//			load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
//		check the rotation of the image and display it properly
        ExifInterface exif;
        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                scaledBitmap.getWidth(), scaledBitmap.getHeight(), null,
                true);
        FileOutputStream imgOutputPath = null;
        FileOutputStream thumbimgOutputPath = null;
        String filename = filePath;
        File delete = new File(filePath);
        delete.delete();
        try {
            imgOutputPath = new FileOutputStream(filename);
//			write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, imgOutputPath);
            thumbimgOutputPath = new FileOutputStream(thumbFileName);
            //rony 1.3.4 starts
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 15, thumbimgOutputPath);
            //rony 1.3.4 ends
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;

    }
    /*public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
		return uriSting;

	}
	*/

    // rony - Prescription GC  - Ends
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    // rony - Prescription GC  - Starts

    public File getOutputMediaFile(int type) {
        if (!prescriptionImgDir.exists()) {
            if (!prescriptionImgDir.mkdirs()) {
                return null;
            }
        }
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(imageFileName);
        } else {
            return null;
        }
        return mediaFile;
    }

    public void patientDirectory() {
        if (!prescriptionImgDir.exists()) {
            prescriptionImgDir.mkdirs();
        }

    }
    // rony - Prescription GC  - Ends


    private void snackBarShow() {
        SnackbarManager.show(
                Snackbar.with(AddPrescriptionActivity.this)
                        .text("No Internet connection...")
                        .actionLabel("Close")
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)//
                        .color(Color.BLACK)
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                Log.d(TAG, "Undoing something");

                            }
                        })
                , AddPrescriptionActivity.this);

    }

    private void snackBarHide() {
        SnackbarManager.dismiss();
    }
}
