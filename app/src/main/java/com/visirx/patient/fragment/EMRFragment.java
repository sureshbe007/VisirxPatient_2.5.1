package com.visirx.patient.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.Auscultation;
import com.visirx.patient.activity.Image;
import com.visirx.patient.activity.VitalSign;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.EMRFileListProvider;
import com.visirx.patient.common.provider.UploadFileProvider;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.EMRModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.MyAlertBox;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EMRFragment extends Fragment {
    String TAG = EMRFragment.class.getName();
    List<EMRModel> modelList = null;
    AppointmentPatientModel appointmentPatientModel = null;
    ImageAdapter adapter = null;
    NormalFont textView = null;
    ProgressBar progressBar = null;
    GridView gridview = null;
    NormalFont appId;
    NormalFont appDate;
    String createdById = "";
    public static final int FILE_CODE = 1001;
    SharedPreferences loggedPreferance;
    private static final int CAMERA_REQUEST = 1888;
    public File mediaFile;
    public Uri fileUri;
    public int MEDIA_TYPE_IMAGE = 1;
    public String timeStamp;
    public String imageFileName = null;
    public String thumbFileName = null;
    Bitmap bmp = null;
    EditText fileName;
    ImageView imageView;
    int reservationNumber;
    MyAlertBox myAlertBox;

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static EMRFragment newInstance(int reservationNumber) {
        EMRFragment fragment = new EMRFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(VTConstants.APPTMODEL_KEY, reservationNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    public EMRFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.tab_emr,
                container, false);
        reservationNumber = getArguments().getInt(
                VTConstants.APPTMODEL_KEY, -1);
        appointmentPatientModel = VisirxApplication.aptDAO
                .GetAppointmentsByID(reservationNumber);
        getActivity().registerReceiver(receiverUpdate,
                new IntentFilter(VTConstants.NOTIFICATION_EMRFILE));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        appId = (NormalFont) getActivity().findViewById(R.id.emrappId);
        appDate = (NormalFont) getActivity().findViewById(R.id.emrappDate);
        appId.setText("APPOINTMENT ID : " + appointmentPatientModel.getReservationNumber());
        appDate.setText(" " + DateFormat.GetFormattedDateStr(appointmentPatientModel.getDate()));
        textView = (NormalFont) getActivity().findViewById(R.id.emrData);
        textView.setVisibility(View.GONE);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.emrProgress);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#37d9a8"), android.graphics.PorterDuff.Mode.SRC_ATOP);
        try {
            Log.d("SPIN", "INSIDE EMR - initView - try ");
            initView();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SPIN", "INSIDE EMR - initView - catch " + e.getMessage());
//            LogTrace.e(TAG, e.getMessage());
        }
    }

    private void initView() {
        myAlertBox = new MyAlertBox(getActivity());
        loggedPreferance = getActivity().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        createdById = loggedPreferance.getString(VTConstants.USER_ID, null);
        Log.d("SPIN", "INSIDE EMR - initView - patient id : " + createdById);
        Log.d("SPIN", "INSIDE EMR - initView -resno :  " + appointmentPatientModel.getReservationNumber());
        ArrayList<AddEmrFileModel> emrmodelList = VisirxApplication.aptEmrDAO.GetPatientEMRFile(createdById,
                appointmentPatientModel.getReservationNumber());
        Log.d("SPIN", "INSIDE EMR - initView - patient id : " + createdById);
        Log.d("SPIN", "INSIDE EMR - initView -resno :  " + appointmentPatientModel.getReservationNumber());
        Log.d("SPIN", "INSIDE EMR - initView - " + emrmodelList.size());

        ArrayList<String> emrFile = VisirxApplication.aptEmrDAO.GetPatientEMRFileMax(createdById, appointmentPatientModel.getReservationNumber(), createdById);
        ArrayList<String> emrVitals = VisirxApplication.aptVitalDAO
                .GetPatientEMRVitaleMax(createdById,
                        appointmentPatientModel.getReservationNumber());
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
        if (emrmodelList != null && emrmodelList.size() > 0) {
            modelList = AppContext.emrList(emrmodelList);
        } else {
            modelList = AppContext.emrListVitals(
                    createdById,
                    appointmentPatientModel.getReservationNumber());
        }
        Log.d("SPIN", "INSIDE EMR - initView -modelList : " + modelList.size());
        adapter = new ImageAdapter();
        gridview = (GridView) getActivity().findViewById(R.id.gridView1);
        gridview.setAdapter(adapter);
        // if(modelList.size() <= 0 )
        // {
        if (VTConstants.checkAvailability(getActivity())) {
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);
            EMRFileListProvider providerEMR = new EMRFileListProvider(
                    getActivity());
            providerEMR.SendApptsReq(
                    Integer.toString(appointmentPatientModel.getReservationNumber()),
                    createdById, emrFileMaxDate,
                    emrVitalsMaxDate);
        } else {
            progressBar.setVisibility(View.GONE);
            if (adapter.getCount() == 0) {
                gridview.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            } else {
                gridview.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }

        }
        // }
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                EMRModel model = modelList.get(position);
                if (model == null || model.getHeader() == null) {
                    return;
                }
                if (model.getHeader().equalsIgnoreCase("AUSCULTATION")) {
                    Intent intent = new Intent(getActivity(),
                            Auscultation.class);
                    intent.putExtra(VTConstants.APPTMODEL_KEY,
                            appointmentPatientModel.getReservationNumber());
                    intent.putExtra(VTConstants.DATE, appointmentPatientModel.getDate());
                    getActivity().startActivity(intent);
                } else if (model.getHeader().equalsIgnoreCase("IMAGE")) {
                    Intent intent = new Intent(getActivity(),
                            Image.class);
                    intent.putExtra(VTConstants.APPTMODEL_KEY,
                            appointmentPatientModel.getReservationNumber());
                    intent.putExtra(VTConstants.DATE, appointmentPatientModel.getDate());
                    getActivity().startActivity(intent);
                } else if (model.getHeader().equalsIgnoreCase("VITAL SIGN")) {
                    Intent intent = new Intent(getActivity(),
                            VitalSign.class);
                    intent.putExtra(VTConstants.APPTMODEL_KEY,
                            appointmentPatientModel.getReservationNumber());
                    intent.putExtra(VTConstants.DATE, appointmentPatientModel.getDate());
                    getActivity().startActivity(intent);
                }

            }
        });
    }

    private void createDirectory() {
        VTConstants.createDirectoryEMRImages();
        VTConstants.createDirectoryEMRImageThumbnail();
        VTConstants.createDirectoryEMRAuscultation();
    }


    public class ImageAdapter extends BaseAdapter {
        public int getCount() {
            return modelList.size();
        }

        public EMRModel getItem(int position) {
            return modelList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        @SuppressLint("NewApi")
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.emr_list, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            EMRModel model = getItem(position);
            if (model != null) {
                holder.layout.setVisibility(View.VISIBLE);
                holder.txtHeader.setText(model.getHeader());
                holder.txtResults.setText(model.getResult());
                if (model.getDate() == null) {
                    holder.txtheaderrow.setText("Appt ID: " + appointmentPatientModel.getReservationNumber());
                    holder.txtheaderrow.setGravity(Gravity.RIGHT);
                    holder.txtheaderrow.setVisibility(View.GONE);
                } else if (model.getDate() != null && model.getDate().length() > 5) {
                    String date = DateFormat.GetFormattedDateEMR(model.getDate());
                    holder.txtheaderrow.setTextSize(12);
                    holder.txtheaderrow.setText(date);
                    holder.txtheaderrow.setVisibility(View.GONE);
                } else {
                    holder.txtheaderrow.setVisibility(View.GONE);
                }
            } else {
                holder.txtheaderrow.setVisibility(View.GONE);
                holder.layout.setBackground(null);
                holder.layout.setClickable(false);
                holder.layout.setFocusable(false);
                holder.layout.setEnabled(false);
                holder.layout.setVisibility(View.GONE);

            }
            holder.txtheaderrow.setTag(position);
            return convertView;
        }

        class ViewHolder {

            MediumFont txtHeader;
            NormalFont txtResults;
            NormalFont txtheaderrow;
            FrameLayout layout;

            public ViewHolder(View view) {
                txtHeader = (MediumFont) view.findViewById(R.id.text_header);
                txtResults = (NormalFont) view.findViewById(R.id.text_result);
                txtheaderrow = (NormalFont) view.findViewById(R.id.text);
                layout = (FrameLayout) view.findViewById(R.id.framelayout);
                view.setTag(this);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ehd_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            getActivity().finish();
        }
        switch (item.getItemId()) {
            case R.id.ic_camera_alt:
                try {

                    attachementOption();
//                    startCamera();
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void startCamera() {
        timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
        imageFileName = VTConstants.createDirectoryEMRImages() + VTConstants.FILESEPARATOR
                + VTConstants.SAVED_IMAGE_PREFIX
                + appointmentPatientModel.getReservationNumber()
                + createdById + timeStamp
                + ".jpg";
        thumbFileName = VTConstants.createDirectoryEMRImageThumbnail() + VTConstants.FILESEPARATOR
                + VTConstants.SAVED_IMAGE_PREFIX
                + appointmentPatientModel.getReservationNumber()
                + createdById + timeStamp
                + ".jpg";

    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    public File getOutputMediaFile(int type) {
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(imageFileName);
        } else {
            return null;
        }
        return mediaFile;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == CAMERA_REQUEST) {
                if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
                    Uri imgUri = Uri.fromFile(mediaFile);
                    try {
                        compressImage(imgUri.toString());
                        bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imgUri);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                    createBuuilder(bmp);
                } else {
                    try {
                        File delete = new File(imageFileName);
                        delete.delete();
//                getActivity().finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            if (requestCode == FILE_CODE) {
                Log.d("FILESTORED", "FILE ATTACHEMENT");

//                if (VTConstants.checkAvailability(getActivity())) {
//                    snackBarHide();
                if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
                    if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                        // For JellyBean and above
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ClipData clip = data.getClipData();

                            if (clip != null) {
                                for (int i = 0; i < clip.getItemCount(); i++) {
                                    Uri uri = clip.getItemAt(i).getUri();
                                    // Do something with the URI

                                    storeFile(uri.getPath());

                                }
                            }
                            // For Ice Cream Sandwich
                        } else {

                            ArrayList<String> paths = data.getStringArrayListExtra
                                    (FilePickerActivity.EXTRA_PATHS);

                            if (paths != null) {
                                for (String path : paths) {
                                    Uri uri = Uri.parse(path);
                                    // Do something with the URI
//                                        storeFile(uri.getPath());
                                    storeFile(uri.getPath());

                                }
                            }
                        }

                    } else {
                        Uri uri = data.getData();
                        // Do something with the URI
//
                        storeFile(uri.getPath());

                    }

                }
//                } else {
//                    snackBarShow();
//                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String compressImage(String imageUri) throws IOException {
        String filePath = imageFileName;//getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
//    by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//    you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
//    max Height and width values of the compressed image is taken as 816x612
        float maxHeight = 1024.0f;
        float maxWidth = 768.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
//    width and height values are set maintaining the aspect ratio of the image
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
//    setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = 1; //calculateInSampleSize(options, actualWidth, actualHeight);
//    inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[16 * 1024];
        try {
//       load the bitmap from its path
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
//    check the rotation of the image and display it properly
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
//       write the compressed bitmap at the destination specified by filename.
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


    public void saveFileOnserver(String attachfilename, String attachfilePath, String mimeType, String contentType) {
        try {
//            Toast.makeText(getActivity(),"saveFileOnserver",Toast.LENGTH_SHORT).show();
            Log.d("FILESTORED", "saveFileOnserver :. 1 :" + attachfilename);
            Log.d("FILESTORED", "saveFileOnserver :. 2 :" + attachfilePath);
            Log.d("FILESTORED", "saveFileOnserver :. 3 :" + mimeType);
            Log.d("FILESTORED", "saveFileOnserver :. 4 :" + contentType);


            String createdByid = null;
            String createdByUserName = null;
            //String createdAt = null;

            AddEmrFileModel model = new AddEmrFileModel();


            loggedPreferance = getActivity().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            model.setCreatedById(loggedPreferance.getString(VTConstants.USER_ID, null));

            // RAMESH_SECRETORY_CHANGES_END

            String fileGroup = null;
            String fileContentType = null;
            if (VTConstants.IMAGE_MIME_FORMAT.contains(mimeType.toLowerCase())) {
                fileGroup = "Image";
                fileContentType = contentType;

                mediaFile = new File(attachfilePath);

                // Uri imgUri = Uri.fromFile(mediaFile);
                // compressImage(imgUri.toString());
                //Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imgUri);
                // createBuuilder(bmp);
                // return;
            } else if (VTConstants.DOC_FILE_FORMAT.contains(mimeType.toLowerCase())) {
                fileGroup = mimeType;
                fileContentType = "application/msword";
//                model.setEmrImageThumbnailPath(null);
            } else if (VTConstants.XLSX_FILE_FORMAT.contains(mimeType.toLowerCase())) {
                fileGroup = mimeType;
                fileContentType = "application/vnd.ms-excel";
//                model.setEmrImageThumbnailPath(null);
            } else {
                fileGroup = mimeType;
                fileContentType = contentType;
//                model.setEmrImageThumbnailPath(null);
            }
            String createdAtClient = DateFormat.getDateStr(new Date());
            model.setCreatedAt(createdAtClient);
            model.setCreatedAtServer(createdAtClient);
            model.setAppointmentId(appointmentPatientModel.getReservationNumber());
            model.setPatientId(loggedPreferance.getString(VTConstants.USER_ID, null));
            model.setFileType(fileContentType);
            model.setEmrImagePath(attachfilePath);
            model.setEmrImageThumbnailPath(null);
            model.setFileMimeType(mimeType);
            model.setFileGroup(fileGroup);
            model.setFileLabel(attachfilename);


            //Toast.makeText(getActivity(),model.getCreatedAtServer()+" : "+model.getCreatedAt(),Toast.LENGTH_LONG).show();

            boolean flag = VisirxApplication.aptEmrDAO.insertAppointmentEMR(model, VTConstants.PROCESSED_FLAG_NOT_SENT);
//            if (flag) {

//            crashLytics(createdByid, createdByUserName, "offline", mimeType, attachfilename, String.valueOf(appointmentModel.getReservationNumber()), createdAt);
//            Popup.ShowSuccessMessage(getActivity(), R.string.emr_saved_sucessfully, Toast.LENGTH_SHORT);

//
//            AddEMRFileProvider provider = new AddEMRFileProvider(getActivity());
//            provider.SendApptReq(appointmentModel.getReservationNumber());
            if (VTConstants.checkAvailability(getActivity())) {
                snackBarHide();
                UploadFileProvider uploadFileProvider = new UploadFileProvider(getActivity());
                uploadFileProvider.sendEMRAttachment(appointmentPatientModel.getReservationNumber());
            } else {
                snackBarShow();
            }


            Intent intent = new Intent(VTConstants.NOTIFICATION_EHD_IMAGE);
            getActivity().sendBroadcast(intent);
//

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void storeFile(String sourceLocationPath) {
        try {

            String fileName = "";
            String SAVED_FILE_PREFIX = "";
            String targetLoacationPath = "";
            String tempPath = sourceLocationPath;

            tempPath = VTConstants.replaceWhiteSpace(tempPath);

            File sourceLocation = new File(sourceLocationPath);

            timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());

            if (sourceLocation.exists()) {
                Log.d("FILESTORED", " Inside storeFile() : file exists : actual path : " + sourceLocationPath);
                Log.d("FILESTORED", " Inside storeFile() : file exists : temp path : " + tempPath);

                long length = sourceLocation.length();
                length = length / 1024;


                String mType = MimeTypeMap.getFileExtensionFromUrl(tempPath);
                if (VTConstants.SUPPORT_FILE_FORMAT.contains(MimeTypeMap.getFileExtensionFromUrl(tempPath))
                        && mType != null && !mType.isEmpty()) {


                    if (length <= 5120) {

                        String mimeType = MimeTypeMap.getFileExtensionFromUrl(tempPath);
                        Log.d("FILESTORED", " Inside storeFile() : file formatis valid  mimeType     : " + mimeType);
                        String contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeType);
                        Log.d("FILESTORED", " Inside storeFile() : file formatis valid  contentType  : " + contentType);
                        Log.d("FILESTORED", " Inside storeFile() : file formatis valid : " + MimeTypeMap.getFileExtensionFromUrl(tempPath));
                        fileName = sourceLocation.getName();
                        SAVED_FILE_PREFIX = MimeTypeMap.getFileExtensionFromUrl(tempPath);
                        targetLoacationPath = VTConstants.createDirectoryEMRImages() + VTConstants.FILESEPARATOR
                                + SAVED_FILE_PREFIX
                                + appointmentPatientModel.getReservationNumber()
                                + appointmentPatientModel.getPerfomerid() + timeStamp
                                + "_" + sourceLocation.getName();

                        File targetLocation = new File(targetLoacationPath);
                        InputStream in = new FileInputStream(sourceLocation);
                        OutputStream out = new FileOutputStream(targetLocation);

                        // Copy the bits from instream to outstream
                        byte[] buf = new byte[1024];
                        int len;

                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        in.close();
                        out.close();

                        if (VTConstants.IMAGE_MIME_FORMAT.contains(mimeType.toLowerCase())) {

                            imageFileName = targetLoacationPath;
                            thumbFileName = VTConstants.createDirectoryEMRImageThumbnail() + VTConstants.FILESEPARATOR
                                    + VTConstants.SAVED_IMAGE_PREFIX
                                    + appointmentPatientModel.getReservationNumber()
                                    + appointmentPatientModel.getPerfomerid() + timeStamp
                                    + ".jpg";
                            compressImage(targetLoacationPath);
                            bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(new File(targetLoacationPath)));
                            createBuuilder(bmp);
                            return;

                        }
                        Log.d("FILESTORED", "Copy file successful :. " + mimeType);
// NULL CHECKING
                        if (fileName != null && !fileName.isEmpty() && targetLoacationPath != null && !targetLoacationPath.isEmpty()
                                && mimeType != null && !mimeType.isEmpty() && contentType != null && !contentType.isEmpty()) {

                            saveFileOnserver(fileName, targetLoacationPath, mimeType, contentType);
                        } else {
//                        Toast.makeText(getActivity(), "File format not supported", Toast.LENGTH_SHORT).show();
                            myAlertBox.InfoAlerBox("User alert ", "This is an unsupported file format.", "OK");
                        }

                    } else {
                        Log.d("FILESTORED", "inside storeFile() : File size exceeds 5 mb");
                        myAlertBox.InfoAlerBox("User alert ", "File size exceeds 5 MB.", "OK");
                    }

                } else {
//                    Toast.makeText(getActivity(), "File format not supported", Toast.LENGTH_SHORT).show();
                    myAlertBox.InfoAlerBox("User alert ", "This is an unsupported file format.", "OK");
                    Log.d("FILESTORED", " Inside storeFile() : file format not supported : " + MimeTypeMap.getFileExtensionFromUrl(tempPath));
                }


            } else {
                Log.d("FILESTORED", "inside storeFile() : Copy file failed. Source file missing.");
                myAlertBox.InfoAlerBox("User alert ", "File not found", "OK");
            }
        } catch (Exception e) {
            e.printStackTrace();
            myAlertBox.InfoAlerBox("User alert ", "This is an unsupported file format.", "OK");
        }


    }

    private void createBuuilder(final Bitmap bitmap) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.ehd_image_capture, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        fileName = (EditText) promptsView.findViewById(R.id.fileName);
        Button negativeButton = (Button) promptsView.findViewById(R.id.file_cancel);
        Button positiveButton = (Button) promptsView.findViewById(R.id.file_yes);
        imageView = (ImageView) promptsView.findViewById(R.id.emrImage);
        imageView.setImageBitmap(bitmap);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                try {
                    File delete = new File(imageFileName);
                    if (delete.exists()) {
                        delete.delete();
//                        getActivity().finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileName.getText().toString().trim().length() > 0) {
                    try {
                        SaveImage(fileName.getText().toString());
//                         Fabric Image Offline
                        EventChecking.emrImageOffline(String.valueOf(appointmentPatientModel.getReservationNumber()), loggedPreferance.getString(VTConstants.LOGGED_USER_FULLNAME, null).toUpperCase(), fileName.getText().toString(), "Offline", getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    alertDialog.cancel();
                } else {
                    Popup.ShowErrorMessage(getActivity(), R.string.file_name,
                            Toast.LENGTH_SHORT);
                }
            }
        });


    }

    private void SaveImage(String fileName) {
        try {
//            Toast.makeText(getActivity(),"SaveImage",Toast.LENGTH_SHORT).show();
            loggedPreferance = getActivity().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            String createdAtServer = DateFormat.getDateStr(new Date());
            AddEmrFileModel model = new AddEmrFileModel();
            model.setAppointmentId(appointmentPatientModel.getReservationNumber());
            model.setCreatedAt(createdAtServer);
            model.setCreatedById(loggedPreferance.getString(VTConstants.USER_ID, null));
            model.setPatientId(createdById);
            model.setFileType("Image");
            model.setEmrImagePath(imageFileName);
            model.setEmrImageThumbnailPath(thumbFileName);
            model.setFileMimeType("png");
            model.setFileGroup("Image");
            model.setFileLabel(fileName);
            model.setCreatedAtServer(createdAtServer);


            boolean flag = VisirxApplication.aptEmrDAO.insertAppointmentEMR(model, VTConstants.PROCESSED_FLAG_NOT_SENT);

            Popup.ShowSuccessMessage(getActivity(), R.string.emr_saved_sucessfully, Toast.LENGTH_SHORT);


            UploadFileProvider uploadFileProvider = new UploadFileProvider(getActivity());
            uploadFileProvider.sendEMRAttachment(appointmentPatientModel.getReservationNumber());

            Intent intent = new Intent(VTConstants.NOTIFICATION_EMRFILE);
            getActivity().sendBroadcast(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LogTrace.i(TAG, "Appts EMR broadcast received...");
                progressBar.setVisibility(View.GONE);
                ArrayList<AddEmrFileModel> emrmodelList = VisirxApplication.aptEmrDAO
                        .GetPatientEMRFile(createdById,
                                appointmentPatientModel.getReservationNumber());
                if (emrmodelList != null && emrmodelList.size() > 0) {
                    modelList = AppContext.emrList(emrmodelList);
                } else {
                    modelList = AppContext.emrListVitals(createdById,
                            appointmentPatientModel.getReservationNumber());
                }
                if (modelList.size() > 0) {
                    textView.setVisibility(View.GONE);
                    gridview.setVisibility(View.VISIBLE);
                } else {
                    gridview.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }
    };


    private void attachementOption() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.emr_attach_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        LinearLayout cameraLayout = (LinearLayout) promptsView.findViewById(R.id.emr_camera_linear);
        LinearLayout attachLyout = (LinearLayout) promptsView.findViewById(R.id.emr_attachement_linear);

        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

                alertDialog.cancel();

            }
        });
        attachLyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickerIntent = new Intent(getActivity(), FilePickerActivity.class);
                pickerIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                pickerIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                pickerIntent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                pickerIntent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                startActivityForResult(pickerIntent, FILE_CODE);
                alertDialog.cancel();

            }
        });


    }


    private void snackBarShow() {
        SnackbarManager.show(
                Snackbar.with(getActivity())
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
                , getActivity());

    }

    private void snackBarHide() {
        SnackbarManager.dismiss();
    }

    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(receiverUpdate);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        super.onDestroy();
    }

}