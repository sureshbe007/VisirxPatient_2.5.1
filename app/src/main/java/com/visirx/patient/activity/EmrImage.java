package com.visirx.patient.activity;

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
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.DownloadFileProvider;
import com.visirx.patient.common.provider.ImageDownloaderProvider;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.customview.TouchImageView;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.model.AppointmentModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class EmrImage extends AppCompatActivity {
    String TAG = EmrImage.class.getName();
    public String name = null;
    public String duration = null;
    public int position = -1;
    private byte[] imageData = null;
    public TouchImageView imageView = null;
    // RAMESH
    public Toolbar toolbar;
    // rony - EMRFRAGMENT GC - Starts
    public String date;
    public List<AddEmrFileModel> emrImageFileList = null;
    public int appointmentId = 0;
    public AppointmentPatientModel appointmentPatientModel = null;
    public String createdById = "";
    public SharedPreferences loggedPreferance;

    public ViewPager viewPager;
    public FullScreenImageAdapter fullScreenImageAdapter = null;


    // rony - EMRFRAGMENT GC - Ends
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emr_image);
        // RAMESH
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.patient_emrViewPager);

        this.registerReceiver(receiverUpdate, new IntentFilter(VTConstants.NOTIFICATION_EMR_FULL_IMAGE));
        //  rony - EMRFRAGMENT GC - Ends
        if (getIntent() != null) {
            try {
                name = getIntent().getStringExtra(VTConstants.FILE_NAME);
                toolbar.setTitle(name);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                appointmentId = getIntent().getIntExtra(VTConstants.FILE_APPOINTMENTID, -1);
                position = getIntent().getIntExtra(VTConstants.FILE_DATA, -1);
                date = getIntent().getStringExtra(VTConstants.FILE_DATE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        loggedPreferance = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        createdById = loggedPreferance.getString(VTConstants.USER_ID, "Not set");
        emrImageFileList = AppContext.emrImageFileList;


        if (name == null) {
            name = VisirxApplication.appContext.getLoggedUser().getFirstName() +
                    " " + VisirxApplication.appContext.getLoggedUser().getLastName();
        }

        if (position > -1) {
            initializeViews(position);
        }
        // rony - EMRFRAGMENT GC - Starts
        if (appointmentId > 0) {
            appointmentPatientModel = VisirxApplication.aptDAO.GetAppointmentsByID(appointmentId);
        }
        // rony - EMRFRAGMENT GC - Ends

        try {
            fullScreenImageAdapter = new FullScreenImageAdapter();
            viewPager.setAdapter(fullScreenImageAdapter);
            viewPager.setCurrentItem(position);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {


                    toolbar.setTitle(emrImageFileList.get(position).getFileLabel());
                    initializeViews(position);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews(int currentposition) {
        try {
            // rony - EMRFRAGMENT GC - Starts


            AddEmrFileModel item = emrImageFileList.get(currentposition);
            if (VTConstants.checkAvailability(getApplicationContext())) {
                snackBarHide();
                if (emrImageFileList.get(currentposition).getEmrImagePath() == null) {

                    DownloadFileProvider downloadFileProvide = new DownloadFileProvider(EmrImage.this, VTConstants.NOTIFICATION_EMR_FULL_IMAGE);
                    downloadFileProvide.downloadEmrFile(item.getAppointmentId(),
                            item.getCreatedAt(),
                            item.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                            VTConstants.FULL_IMAGE_FLAG, item.getFileMimeType());

                } else {
                    File file = new File(emrImageFileList.get(currentposition).getEmrImagePath());
                    if (file.exists()) {
                        // file exists : loading part will be done by viewpager adpater.
                    } else {

                        DownloadFileProvider downloadFileProvide = new DownloadFileProvider(EmrImage.this, VTConstants.NOTIFICATION_EMR_FULL_IMAGE);
                        downloadFileProvide.downloadEmrFile(item.getAppointmentId(),
                                item.getCreatedAt(),
                                item.getPatientId(), VTConstants.PROVIDER_EMR_REQUESTACTION,
                                VTConstants.FULL_IMAGE_FLAG, item.getFileMimeType());
                    }
                }

            } else {
                snackBarShow();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class FullScreenImageAdapter extends PagerAdapter {

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return emrImageFileList.size();
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
            TouchImageView imgDisplay;
            ProgressBar progressBar;

            ImageView viewPagerimageView;
            Button viewPagerbutton;

            View viewLayout = View.inflate(EmrImage.this, R.layout.layout_fullscreen_image, null);

            viewPagerimageView = (ImageView) viewLayout.findViewById(R.id.viewer_file_image);
            viewPagerbutton = (Button) viewLayout.findViewById(R.id.viewer_file_button);
            imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
            progressBar = (ProgressBar) viewLayout.findViewById(R.id.emrViewpagerProgress);
            progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#379674"), android.graphics.PorterDuff.Mode.SRC_ATOP);
            try {


                if (emrImageFileList.get(position).getEmrImagePath() != null) {

                    File file = new File(emrImageFileList.get(position).getEmrImagePath());
                    if (!file.exists()) {
                        Log.d("FULLIMAGEADAPTE", " file.exists()" + emrImageFileList.get(position).getEmrImagePath());
                        initializeViews(position);
                        progressBar.setVisibility(View.VISIBLE);
                        Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.visirx_default);
                        imgDisplay.setImageBitmap(defaultIcon);

                    } else {
                        Log.d("FULLIMAGEADAPTE", " else" + emrImageFileList.get(position).getEmrImagePath());
                        progressBar.setVisibility(View.GONE);
                        if (VTConstants.PDF_FILE_FORMAT.contains(emrImageFileList.get(position).getFileMimeType())) {

                            imgDisplay.setVisibility(View.GONE);
                            viewPagerimageView.setVisibility(View.VISIBLE);
                            viewPagerbutton.setVisibility(View.VISIBLE);
                            viewPagerimageView.setImageResource(R.drawable.ic_new_pdf);


                        } else if (VTConstants.DOC_FILE_FORMAT.contains(emrImageFileList.get(position).getFileMimeType())) {

                            imgDisplay.setVisibility(View.GONE);
                            viewPagerimageView.setVisibility(View.VISIBLE);
                            viewPagerbutton.setVisibility(View.VISIBLE);
                            viewPagerimageView.setImageResource(R.drawable.ic_doc);

                        } else if (VTConstants.TXT_FILE_FORMAT.contains(emrImageFileList.get(position).getFileMimeType())) {


                            imgDisplay.setVisibility(View.GONE);
                            viewPagerimageView.setVisibility(View.VISIBLE);
                            viewPagerbutton.setVisibility(View.VISIBLE);
                            viewPagerimageView.setImageResource(R.drawable.ic_txt);


                        } else if (VTConstants.XLSX_FILE_FORMAT.contains(emrImageFileList.get(position).getFileMimeType())) {

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
                            Bitmap bitmap = BitmapFactory.decodeFile(emrImageFileList.get(position).getEmrImagePath(), options);
                            imgDisplay.setImageBitmap(bitmap);
                        }
                    }
                } else {
                    initializeViews(position);
                    progressBar.setVisibility(View.VISIBLE);
                    Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.visirx_default);
                    imgDisplay.setImageBitmap(defaultIcon);
                }
                if (!VTConstants.checkAvailability(EmrImage.this)) {
                    progressBar.setVisibility(View.GONE);
                }

                viewPagerbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (VTConstants.FILE_MIME_FORMAT.contains(emrImageFileList.get(position).getFileMimeType())) {

                            try {

                                if (emrImageFileList.get(position).getEmrImagePath() == null) {
                                    //give defaut image
//                                holder.imageThumnail.setImageResource(R.drawable.visirx_default);
                                    Toast.makeText(EmrImage.this, "file does not exists.", Toast.LENGTH_SHORT).show();
                                } else {

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
                                        Toast.makeText(EmrImage.this, "file does not exists", Toast.LENGTH_SHORT).show();
                                    }


                                }

                            } catch (ActivityNotFoundException e) {

                                //Do  to create alert box for application not found

                                Toast.makeText(EmrImage.this, " Application not found  to view",
                                        Toast.LENGTH_SHORT).show();
                            }
                            catch(Exception e)
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

    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LogTrace.i(TAG, "Appts EMR broadcast received...");
                emrImageFileList = VisirxApplication.aptEmrDAO.
                        GetPatientEMRFile(createdById, appointmentPatientModel.getReservationNumber(), date);
                AppContext.emrImageFileList = emrImageFileList;
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

    private void snackBarShow() {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext())
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
                , EmrImage.this);

    }

    private void snackBarHide() {
        SnackbarManager.dismiss();
    }

}
