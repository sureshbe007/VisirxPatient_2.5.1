package com.visirx.patient.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.DeleteEhdProvider;
import com.visirx.patient.common.provider.DownloadFileProvider;
import com.visirx.patient.common.provider.ImageDownloaderProvider;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.DeleteEhdModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Image extends AppCompatActivity {
    String TAG = Image.class.getName();
    public static int DOWNLOAD_COUNT_EMR = 0;
    private List<AddEmrFileModel> emrImageFileList = null;
    EMRFileAdapter adapter = null;
    int reservationNumber = -1;
    AppointmentPatientModel appointmentPatientModel = null;
    String date = null;
    Date gridDate = null;
    String createdById = "";
    SimpleDateFormat inputFormatter, outputFormatter;
    SharedPreferences loggedPreferance;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Images");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        outputFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        loggedPreferance = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        createdById = loggedPreferance.getString(VTConstants.USER_ID, "Not set");
        //  rony - EMRFRAGMENT GC - Starts
        this.registerReceiver(receiverUpdate, new IntentFilter(VTConstants.NOTIFICATION_EMRFILE));
        //  rony - EMRFRAGMENT GC - Ends
        if (getIntent().getExtras() != null) {
            reservationNumber = getIntent().getIntExtra(VTConstants.APPTMODEL_KEY, -1);
            appointmentPatientModel = VisirxApplication.aptDAO.GetAppointmentsByID(reservationNumber);
            date = getIntent().getStringExtra(VTConstants.DATE);
        }
        emrImageFileList = VisirxApplication.aptEmrDAO.
                GetPatientEMRFile(createdById, appointmentPatientModel.getReservationNumber(), date);
        AppContext.emrImageFileList = emrImageFileList;
        adapter = new EMRFileAdapter();
        GridView gridView = (GridView) findViewById(R.id.gridView_emrimage);
        gridView.setAdapter(adapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                AddEmrFileModel item = emrImageFileList.get(position);
//                //  rony - EMRFRAGMENT GC - Starts
//                Intent intent = new Intent(getBaseContext(), EmrImage.class);
//                intent.putExtra(VTConstants.FILE_NAME, item.getFileLabel());
//                intent.putExtra(VTConstants.FILE_DATE, date);
//                intent.putExtra(VTConstants.FILE_APPOINTMENTID, reservationNumber);
//                intent.putExtra(VTConstants.FILE_DATA, position);//TODO
//                startActivity(intent);
//                //  rony - EMRFRAGMENT GC - Ends
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private final class EMRFileAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return emrImageFileList.size();
        }

        @Override
        public AddEmrFileModel getItem(int position) {
            return emrImageFileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getBaseContext(),
                        R.layout.emr_imagelist, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            final AddEmrFileModel model = getItem(position);
//
            try {
                gridDate = inputFormatter.parse(model.getCreatedAt());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            if (VTConstants.PDF_FILE_FORMAT.contains(model.getFileMimeType()))

            {
                Log.d("SPINS", "Inside pdf : " );
                String filePath = model.getEmrImagePath();
                if (filePath != null) {
                    Log.d("SPIN", "File path : " + filePath);

                    File file = new File(filePath);

                    if (file.exists()) {
                        holder.imageThumnail.setImageResource(R.drawable.ic_new_pdf);
                    } else {

                        // do the download request if internet available.

                        if(DOWNLOAD_COUNT_EMR < VTConstants.EMR_THUMBNAIL_DWNLD_COUNT) {

                            DOWNLOAD_COUNT_EMR++;
                            DownloadFileProvider downloadFileProvide = new DownloadFileProvider(Image.this, VTConstants.NOTIFICATION_EMRFILE);
                            downloadFileProvide.downloadEmrFile(model.getAppointmentId(),
                                    model.getCreatedAt(),
                                    model.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                                    VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());

                        }
                        // if internet available, set the icon to progress bar.
                        Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.visirx_default);

                        holder.imageThumnail.setImageBitmap(defaultIcon);
                    }
                }else {

                    // do the download request if internet available.

                    if(DOWNLOAD_COUNT_EMR < VTConstants.EMR_THUMBNAIL_DWNLD_COUNT) {

                        DOWNLOAD_COUNT_EMR++;
                        DownloadFileProvider downloadFileProvide = new DownloadFileProvider(Image.this, VTConstants.NOTIFICATION_EMRFILE);
                        downloadFileProvide.downloadEmrFile(model.getAppointmentId(),
                                model.getCreatedAt(),
                                model.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                                VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());

                    }
                    // if internet available, set the icon to progress bar.
                    Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.visirx_default);

                    holder.imageThumnail.setImageBitmap(defaultIcon);
                }

            }
            else if (VTConstants.DOC_FILE_FORMAT.contains(model.getFileMimeType())) {
                String filePath = model.getEmrImagePath();
                if (filePath != null) {
                    Log.d("SPIN", "File path : " + filePath);

                    File file = new File(filePath);

                    if (file.exists()) {
                        holder.imageThumnail.setImageResource(R.drawable.ic_doc);
                    } else {

                        // do the download request if internet available.
                        if(DOWNLOAD_COUNT_EMR < VTConstants.EMR_THUMBNAIL_DWNLD_COUNT) {

                            DOWNLOAD_COUNT_EMR++;
                            DownloadFileProvider downloadFileProvide = new DownloadFileProvider(Image.this, VTConstants.NOTIFICATION_EMRFILE);
                            downloadFileProvide.downloadEmrFile(model.getAppointmentId(),
                                    model.getCreatedAt(),
                                    model.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                                    VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
                        }

                        // if internet available, set the icon to progress bar.
                        Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.visirx_default);

                        holder.imageThumnail.setImageBitmap(defaultIcon);
                    }
                } else {

                    // do the download request if internet available.
                    if(DOWNLOAD_COUNT_EMR < VTConstants.EMR_THUMBNAIL_DWNLD_COUNT) {

                        DOWNLOAD_COUNT_EMR++;
                        DownloadFileProvider downloadFileProvide = new DownloadFileProvider(Image.this, VTConstants.NOTIFICATION_EMRFILE);
                        downloadFileProvide.downloadEmrFile(model.getAppointmentId(),
                                model.getCreatedAt(),
                                model.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                                VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
                    }

                    // if internet available, set the icon to progress bar.
                    Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.visirx_default);

                    holder.imageThumnail.setImageBitmap(defaultIcon);
                }

            }
            else if (VTConstants.TXT_FILE_FORMAT.contains(model.getFileMimeType())) {
                String filePath = model.getEmrImagePath();
                if (filePath != null) {
                    Log.d("SPIN", "File path : " + filePath);

                    File file = new File(filePath);

                    if (file.exists()) {
                        holder.imageThumnail.setImageResource(R.drawable.ic_txt);
                    } else {

                        // do the download request if internet available.
                        if(DOWNLOAD_COUNT_EMR < VTConstants.EMR_THUMBNAIL_DWNLD_COUNT) {

                            DOWNLOAD_COUNT_EMR++;
                            DownloadFileProvider downloadFileProvide = new DownloadFileProvider(Image.this, VTConstants.NOTIFICATION_EMRFILE);
                            downloadFileProvide.downloadEmrFile(model.getAppointmentId(),
                                    model.getCreatedAt(),
                                    model.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                                    VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
                        }
                        // if internet available, set the icon to progress bar.
                        Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.visirx_default);

                        holder.imageThumnail.setImageBitmap(defaultIcon);
                    }
                }else {

                    // do the download request if internet available.
                    if(DOWNLOAD_COUNT_EMR < VTConstants.EMR_THUMBNAIL_DWNLD_COUNT) {

                        DOWNLOAD_COUNT_EMR++;
                        DownloadFileProvider downloadFileProvide = new DownloadFileProvider(Image.this, VTConstants.NOTIFICATION_EMRFILE);
                        downloadFileProvide.downloadEmrFile(model.getAppointmentId(),
                                model.getCreatedAt(),
                                model.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                                VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
                    }
                    // if internet available, set the icon to progress bar.
                    Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.visirx_default);

                    holder.imageThumnail.setImageBitmap(defaultIcon);
                }

            } else if (VTConstants.XLSX_FILE_FORMAT.contains(model.getFileMimeType())) {
                String filePath = model.getEmrImagePath();
                if (filePath != null) {
                    Log.d("SPIN", "File path : " + filePath);

                    File file = new File(filePath);

                    if (file.exists()) {
                        holder.imageThumnail.setImageResource(R.drawable.ic_xlsx);
                    } else {

                        // do the download request if internet available.
                        if(DOWNLOAD_COUNT_EMR < VTConstants.EMR_THUMBNAIL_DWNLD_COUNT) {

                            DOWNLOAD_COUNT_EMR++;
                            DownloadFileProvider downloadFileProvide = new DownloadFileProvider(Image.this, VTConstants.NOTIFICATION_EMRFILE);
                            downloadFileProvide.downloadEmrFile(model.getAppointmentId(),
                                    model.getCreatedAt(),
                                    model.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                                    VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
                        }
                        // if internet available, set the icon to progress bar.
                        Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.visirx_default);

                        holder.imageThumnail.setImageBitmap(defaultIcon);
                    }
                }else {

                    // do the download request if internet available.
                    if(DOWNLOAD_COUNT_EMR < VTConstants.EMR_THUMBNAIL_DWNLD_COUNT) {

                        DOWNLOAD_COUNT_EMR++;
                        DownloadFileProvider downloadFileProvide = new DownloadFileProvider(Image.this, VTConstants.NOTIFICATION_EMRFILE);
                        downloadFileProvide.downloadEmrFile(model.getAppointmentId(),
                                model.getCreatedAt(),
                                model.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                                VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
                    }
                    // if internet available, set the icon to progress bar.
                    Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.visirx_default);

                    holder.imageThumnail.setImageBitmap(defaultIcon);
                }

            }

            else if (VTConstants.IMAGE_MIME_FORMAT.contains(model.getFileMimeType()))
            {

                String thumbnail_photo = model.getEmrImageThumbnailPath();
                //Load the file from sd card stored path - starts


                if (thumbnail_photo != null) {
                    // Toast.makeText(getApplicationContext(),"Path is not null",Toast.LENGTH_SHORT).show();
                    Log.d("SPIN", "Image path : " + thumbnail_photo);

                    File imgFile = new File(thumbnail_photo);
                    Uri emrThumbImgUri = Uri.fromFile(imgFile);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = null;

                        try {
                            myBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), emrThumbImgUri);
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        holder.imageThumnail.setImageBitmap(myBitmap);

                        //Load the file from sd card stored path - ends
                    } else {

                        if(DOWNLOAD_COUNT_EMR < VTConstants.EMR_THUMBNAIL_DWNLD_COUNT) {

                            DOWNLOAD_COUNT_EMR++;
                            DownloadFileProvider downloadFileProvide = new DownloadFileProvider(Image.this, VTConstants.NOTIFICATION_EMRFILE);
                            downloadFileProvide.downloadEmrFile(model.getAppointmentId(),
                                    model.getCreatedAt(),
                                    model.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                                    VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
                        }

                        //Toast.makeText(getApplicationContext(),"PHOTO path Null",Toast.LENGTH_SHORT).show();
                        Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.visirx_default);

                        holder.imageThumnail.setImageBitmap(defaultIcon);
                    }

                }else {

                    if(DOWNLOAD_COUNT_EMR < VTConstants.EMR_THUMBNAIL_DWNLD_COUNT) {

                        DOWNLOAD_COUNT_EMR++;
                        DownloadFileProvider downloadFileProvide = new DownloadFileProvider(Image.this, VTConstants.NOTIFICATION_EMRFILE);
                        downloadFileProvide.downloadEmrFile(model.getAppointmentId(),
                                model.getCreatedAt(),
                                model.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                                VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
                    }

                    //Toast.makeText(getApplicationContext(),"PHOTO path Null",Toast.LENGTH_SHORT).show();
                    Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.visirx_default);

                    holder.imageThumnail.setImageBitmap(defaultIcon);
                }
            }

            else {
                Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.visirx_default);
                holder.imageThumnail.setImageBitmap(defaultIcon);
            }

            if (createdById.equalsIgnoreCase(model.getCreatedById())) {
                holder.statusIcon.setVisibility(View.VISIBLE);
                if (model.getProcessedFlag() == 0) {
                    holder.trash.setVisibility(View.INVISIBLE);
                    holder.statusIcon.setImageResource(R.drawable.ic_done);
                } else {
                    holder.trash.setVisibility(View.VISIBLE);
                    holder.statusIcon.setImageResource(R.drawable.ic_done_all);

                }

                holder.createdBy.setText(" " + loggedPreferance.getString(VTConstants.LOGGED_USER_FULLNAME, null).toUpperCase());
            } else {
                holder.trash.setVisibility(View.INVISIBLE);
                holder.statusIcon.setVisibility(View.INVISIBLE);
                holder.createdBy.setText("" + VisirxApplication.customerDAO.getName(model.getCreatedById()).toUpperCase());
            }

            if (model.getFileLabel() != null) {
                if (model.getFileLabel().toString().length() <= 5) {
                    holder.txtFileName.setText((position + 1) + "." + model.getFileLabel());
                } else {
                    holder.txtFileName.setText((position + 1) + "." + (model.getFileLabel().substring(0, 6) + "..."));
                }
            }


            try {

                holder.trash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    holder.trash.setTag(model.getPatientId()+"/"+model.getCreatedAt()+"/"+reservationNumber);
                        deleteAlert(model.getPatientId(), model.getCreatedAt(), reservationNumber);
                        Log.d("IMGEDELETE", "" + model.getPatientId());
                        Log.d("IMGEDELETE", "" + model.getCreatedAt());

                        Log.d("IMGEDELETE", "" + reservationNumber);


                    }
                });
                holder.imageThumnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), EmrImage.class);
                        intent.putExtra(VTConstants.FILE_NAME, model.getFileLabel());
                        intent.putExtra(VTConstants.FILE_DATE, date);
                        intent.putExtra(VTConstants.FILE_APPOINTMENTID, reservationNumber);
                        intent.putExtra(VTConstants.FILE_DATA, position);//TODO
                        startActivity(intent);

                    }


                });
            } catch (Exception e) {
                e.printStackTrace();
            }
//            holder.txtDate.setText(DateFormat.GetFormattedDateTimeStr(model.getCreatedAt()));

            holder.txtDate.setText(DateFormat.GetFormattedDateTimeStr(model.getCreatedAt()) + " " + DateFormat.GetFormattedTimeStr(outputFormatter.format(gridDate)));


            holder.imageThumnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (VTConstants.FILE_MIME_FORMAT.contains(emrImageFileList.get(position).getFileMimeType())) {

                        try {

                            if(emrImageFileList.get(position).getEmrImagePath() == null)
                            {
                                //give defaut image
//                                holder.imageThumnail.setImageResource(R.drawable.visirx_default);
                                Toast.makeText(Image.this,"file does not exists.",Toast.LENGTH_SHORT).show();
                            }else{

                                File file = new File(emrImageFileList.get(position).getEmrImagePath());

                                if (file.exists()) {
//                                    holder.imageThumnail.setImageResource(R.drawable.ic_new_pdf);
                                    File targetFile = new File(emrImageFileList.get(position).getEmrImagePath());
                                    Uri targetUri = Uri.fromFile(targetFile);
                                    Intent intent;
                                    intent = new Intent(Intent.ACTION_VIEW);
                                    Log.v("FILESTORED", "file content type :. 1 :" + emrImageFileList.get(position).getFileType().toString());
                                    intent.setDataAndType(targetUri, emrImageFileList.get(position).getFileType());
                                    startActivity(intent);
                                } else {

//                                    holder.imageThumnail.setImageResource(R.drawable.visirx_default);
                                    Toast.makeText(Image.this,"file does not exists",Toast.LENGTH_SHORT).show();
                                }


                            }

                        } catch (ActivityNotFoundException e) {

                            Toast.makeText(Image.this, " Application not found  to view",
                                    Toast.LENGTH_SHORT).show();
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    else {


                        Log.d("IMAGEPOSITION","position : image .java  :" +position);
                        Intent intent = new Intent(getBaseContext(), EmrImage.class);
                        intent.putExtra(VTConstants.FILE_NAME, model.getFileLabel());
                        intent.putExtra(VTConstants.FILE_DATE, date);
                        intent.putExtra(VTConstants.FILE_APPOINTMENTID, reservationNumber);
                        intent.putExtra(VTConstants.FILE_DATA, position);//TODO
//                        intent.putExtra(VTConstants.ACTIVITY_ACTION, "EMR");//TODO
                        startActivity(intent);
                    }


                }
            });



            return convertView;
        }

        class ViewHolder {
            NormalFont txtFileName;
            NormalFont txtDate;
            MediumFont createdBy;
            ImageView imageThumnail;
            ImageView trash;
            ImageView statusIcon;

            public ViewHolder(View view) {
                imageThumnail = (ImageView) view.findViewById(R.id.picture_emr);
                trash = (ImageView) view.findViewById(R.id.imagetrash);
                txtFileName = (NormalFont) view.findViewById(R.id.lblAusName);
                txtDate = (NormalFont) view.findViewById(R.id.lblAusDate);
                createdBy = (MediumFont) view.findViewById(R.id.createdBy);
                statusIcon = (ImageView) view.findViewById(R.id.imagetick);
                view.setTag(this);
            }
        }
    }

    private void deleteAlert(final String patientid, final String createdat, final int reservationnumber) {


        LayoutInflater li = LayoutInflater.from(Image.this);
        View promptsView = li.inflate(R.layout.delete_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Image.this);

        Button negativeButton = (Button) promptsView.findViewById(R.id.delete_no);
        Button positiveButton = (Button) promptsView.findViewById(R.id.delete_yes);


        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (VTConstants.checkAvailability(getApplicationContext()))

                {
                    snackBarHide();
                    try {
                        List<DeleteEhdModel> deleteEhdModelsList = new ArrayList<DeleteEhdModel>();
                        DeleteEhdModel deleteEhdModel = new DeleteEhdModel();
                        deleteEhdModel.setPatientId(patientid);
                        deleteEhdModel.setCreatedAt(createdat);
                        deleteEhdModel.setAppointmentId(reservationnumber);
                        deleteEhdModel.setMediaType(VTConstants.FLAG_EHD_FILE);
                        deleteEhdModelsList.add(deleteEhdModel);
                        DeleteEhdProvider deleteEhdProvider = new DeleteEhdProvider(Image.this);
                        deleteEhdProvider.SenddeleteReq(deleteEhdModelsList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    snackBarShow();
                }

                alertDialog.cancel();
            }
        });
    }
    //  rony - EMRFRAGMENT GC - Starts

    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d("SPIN", "Appts EMR Grid view broadcast received...");

                emrImageFileList = VisirxApplication.aptEmrDAO.
                        GetPatientEMRFile(createdById, appointmentPatientModel.getReservationNumber(), date);
                AppContext.emrImageFileList = emrImageFileList;

                for (int l = 0; l < emrImageFileList.size(); l++) {
                    Log.d("SPIN", "Thumb url for : " + l + " : " + emrImageFileList.get(l).getEmrImageThumbnailPath());
                }
                //adapter = new  EMRFileAdapter();
                adapter.notifyDataSetChanged();

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
    //  rony - EMRFRAGMENT GC - Ends

    private void snackBarShow() {
        SnackbarManager.show(
                com.nispok.snackbar.Snackbar.with(getApplicationContext()) // context
                        .text("No Internet connection...") /// text to display
                        .actionLabel("Close")
                        .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_INDEFINITE)//
                        .color(Color.BLACK) // change the background color// action button label
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(com.nispok.snackbar.Snackbar snackbar) {
                                Log.d(TAG, "Undoing something");

                            }
                        }) // action button's ActionClickListener
                , Image.this);

    }

    private void snackBarHide() {
        SnackbarManager.dismiss();
    }

}
