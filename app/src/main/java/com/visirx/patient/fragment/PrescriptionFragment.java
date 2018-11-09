package com.visirx.patient.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.net.ParseException;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.AddPrescriptionActivity;
import com.visirx.patient.activity.DigitalPrescriptionViewActivity;
import com.visirx.patient.activity.EmrImage;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.DigitalPrescriptionListProvider;
import com.visirx.patient.common.provider.DownloadFileProvider;
import com.visirx.patient.common.provider.ImageDownloaderProvider;
import com.visirx.patient.common.provider.PrescriptionListProvider;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.AppDigitalprescriptionModel;
import com.visirx.patient.model.AppointmentModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.ApptPrescriptionModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrescriptionFragment extends Fragment {

    // Declare Variables
    GridView grid;
    //    PrescriptionAdapter adapter;
    String[] title;
    String[] time;
    int[] viewimg;

    String TAG = PrescriptionFragment.class.getName();
    ListView listView = null;
    ArrayList<ApptPrescriptionModel> prescriptionList = null;
    //    AppAdapter adapter = null;
    AppointmentPatientModel appointmentPatientModel = null;
    int tryCount = 0;
    //public File paPrestionDir ;
    public String filePath = "";
    public TextView textView = null;
    SharedPreferences loggedPreferance;
    //public  File[] fileArray ;
    SimpleDateFormat inputFormatter, outputFormatter;
    Date date = null;
    ProgressBar progressBar = null;
    GridView gridView = null;
    NormalFont appDate;
    String createdById = null;
    NormalFont appId;
    DigitalAdapter adapter = null;
    int reservationNumber;
    private List<AppDigitalprescriptionModel> digitalprescriptionModel;
    public static int DOWNLOAD_COUNT_PRES = 0;
//    Menu menu;

    public static PrescriptionFragment newInstance(int reservationNumber) {
        PrescriptionFragment fragment = new PrescriptionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(VTConstants.APPTMODEL_KEY, reservationNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PrescriptionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        reservationNumber = getArguments().getInt(VTConstants.APPTMODEL_KEY, -1);
        Log.d("PRESCRIPTIONFRAGMENT", "onCreateView() : " + reservationNumber);
        appointmentPatientModel = VisirxApplication.aptDAO.GetAppointmentsByID(reservationNumber);
        getActivity().registerReceiver(receiverUpdate, new IntentFilter(VTConstants.NOTIFICATION_APPTS_PRESC));
        loggedPreferance = getActivity().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        createdById = loggedPreferance.getString(VTConstants.USER_ID, null);

        digitalprescriptionModel = VisirxApplication.digitalMainTableDAO.getTotalDigitalPrescription(createdById, reservationNumber);
        Log.d("FRAGMENT", "USER_ID :  digitalprescriptionModel.size()" + digitalprescriptionModel.size());
        return inflater.inflate(R.layout.tab_prescription, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("PRESCRIPTIONFRAGMENT", "onActivityCreated() ");
        appDate = (NormalFont) getActivity().findViewById(R.id.appDate);
        appId = (NormalFont) getActivity().findViewById(R.id.appId);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.prescriptionProgress);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#37d9a8"), android.graphics.PorterDuff.Mode.SRC_ATOP);
        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private void initView() {
        try {
            Log.d("PRESCRIPTIONFRAGMENT", "Inside prescription fragment : initView() : ");
            appId = (NormalFont) getActivity().findViewById(R.id.appId);
            appDate = (NormalFont) getActivity().findViewById(R.id.appDate);
            inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            outputFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Log.d("SPIN", "Inside prescription fragment : APPOINTMENT ID : " + appointmentPatientModel.getReservationNumber() + " . Date : " + DateFormat.GetFormattedDateStr(appointmentPatientModel.getDate()));
            appId.setText("APPOINTMENT ID : " + appointmentPatientModel.getReservationNumber());
            appDate.setText(" " + DateFormat.GetFormattedDateStr(appointmentPatientModel.getDate()));
            loggedPreferance = getActivity().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            createdById = loggedPreferance.getString(VTConstants.USER_ID, null);
//            prescriptionList = VisirxApplication.aptPresDAO.GetPatientPrescription(createdById, appointmentPatientModel.getReservationNumber());
//            Log.d("PRESCRIPTIONFRAGMENT", "Inside prescription fragment : prescriptionList : " + prescriptionList.size());
//            adapter = new AppAdapter();
            adapter = new DigitalAdapter();
            textView = (NormalFont) getActivity().findViewById(R.id.prescriptionData);
            gridView = (GridView) getActivity().findViewById(R.id.gridView_emrprescription);
            gridView.setAdapter(adapter);

            if (adapter.getCount()> 0)
            {
                progressBar.setVisibility(View.GONE);
                Log.d("PRESCRIPTIONFRAGMENT", "digitalprescriptionModel.size()   IF  : +");
                gridView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);

            } else {
                Log.d("PRESCRIPTIONFRAGMENT ", "digitalprescriptionModel.size()   ELSE  : +");
                gridView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);

            }
//            gridView.setAdapter(adapter);
//            //rony 1.3.6 starts
//            ArrayList<String> presMaxdateList = VisirxApplication.aptPresDAO.getMaxDate(createdById, appointmentPatientModel.getReservationNumber());
//            String presMaxDate = "";
//            if (presMaxdateList.size() == 0) {
//                presMaxDate = null;
//            } else {
//                presMaxDate = presMaxdateList.get(0);
//            }
//            Log.d("SPIN", "Max date of prescription from db : " + presMaxDate);
            Log.d("PRESCRIPTIONFRAGMENT", " createdById     initView() :" + createdById);
            Log.d("PRESCRIPTIONFRAGMENT", "ReservationNumber() initView() :" + appointmentPatientModel.getReservationNumber());

            ArrayList<String> presMaxdateList = VisirxApplication.digitalMainTableDAO.getMaxDate(createdById, appointmentPatientModel.getReservationNumber());
            String presMaxDate = "";
            if (presMaxdateList.size() == 0) {
                presMaxDate = null;
            } else {
                presMaxDate = presMaxdateList.get(0);
            }
            Log.d("PRESCRIPTIONFRAGMENT", "presMaxDate :" + presMaxDate);

            //fetch data from server if required. - starts
            if (VTConstants.checkAvailability(getActivity())) {
//                progressBar.setVisibility(View.VISIBLE);
//                PrescriptionListProvider prescriptionListProvider = new PrescriptionListProvider(getActivity());
//                prescriptionListProvider.SendApptPrescriptionReq(appointmentPatientModel.getReservationNumber(), createdById, presMaxDate);
                Log.d("PRESCRIPTIONFRAGMENT", " initView() presMaxDate :" + presMaxDate);
                Log.d("PRESCRIPTIONFRAGMENT", " initView() ReservationNumber :" + appointmentPatientModel.getReservationNumber());
                Log.d("PRESCRIPTIONFRAGMENT", "initView()   createdById :" + createdById);
                Log.d("PRESCRIPTIONFRAGMENT", " checkAvailability  BFRORE  PROVIDER CALL:");
                DigitalPrescriptionListProvider digitalPrescriptionListProvider = new DigitalPrescriptionListProvider(getActivity());
                digitalPrescriptionListProvider.SendDigitalPrescriptionListReq(appointmentPatientModel.getReservationNumber(), createdById, presMaxDate);
                Log.d("PRESCRIPTIONFRAGMENT", "  checkAvailability  AFTER  PROVIDER CALL :");

            } else {
                Log.d("PRESCRIPTIONFRAGMENT", " checkAvailability  ELSE :");

                if (adapter.getCount() == 0) {
                    gridView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    gridView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                }
            }
            //fetch data from server if required. - Ends
            //rony 1.3.6 ends
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
//                    Intent intent = new Intent(getActivity(), AddPrescriptionActivity.class);
//                    intent.putExtra(VTConstants.PRESCRIPTION_VIEW, true);
//                    intent.putExtra(VTConstants.APPTMODEL_KEY, appointmentPatientModel.getReservationNumber());
//                    intent.putExtra(VTConstants.PATIENT_DATE, appointmentPatientModel.getDate());
//                    intent.putExtra(VTConstants.PATIENT_ID, prescriptionList.get(position).getPatientId());
//                    intent.putExtra(VTConstants.FILE_DATA, position);
//                    startActivity(intent);

                    Log.d("PRESCRIPTIONFRAGMENT", "gridView click  :   ReservationNumber  :::" + appointmentPatientModel.getReservationNumber());
                    Log.d("PRESCRIPTIONFRAGMENT", "gridView  click:   createdById  or PATIENT_ID  :::" + createdById);
                    Log.d("PRESCRIPTIONFRAGMENT", "gridView  click:   getDate  :::" + appointmentPatientModel.getDate());
                    Log.d("PRESCRIPTIONFRAGMENT", "gridView  click :   getCreatedAtclient  :::" + digitalprescriptionModel.get(position).getCreatedAtclient());
                    Log.d("PRESCRIPTIONFRAGMENT", "gridView click :   position  :::" + position);

                    Intent intent = new Intent(getActivity(), DigitalPrescriptionViewActivity.class);
                    intent.putExtra(VTConstants.APPTMODEL_KEY, appointmentPatientModel.getReservationNumber());
                    intent.putExtra(VTConstants.PATIENT_ID, createdById);
                    intent.putExtra(VTConstants.APPOINTMENT_DATE, appointmentPatientModel.getDate());
                    intent.putExtra(VTConstants.CREATEDAT_CLIENT, digitalprescriptionModel.get(position).getCreatedAtclient());
                    intent.putExtra(VTConstants.FILE_DATA, position);
                    startActivity(intent);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }


//    class AppAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return prescriptionList.size();
//        }
//
//        @Override
//        public ApptPrescriptionModel getItem(int position) {
//            return prescriptionList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            //rony 1.3.4 starts
//            try {
//                if (convertView == null) {
//                    //rony 1.3.4 starts
//                    Log.d("SPIN", "INSIDE dashboardlistfragment getview :null ");
//                    LayoutInflater inflater = getLayoutInflater(null);
//                    convertView = inflater.inflate(R.layout.prescription_imagelist, parent, false);
//                /*
//                convertView = View.inflate(getActivity(), R.layout.prescription_imagelist, null);*/
//                    new ViewHolder(convertView);
//                    //rony 1.3.4 ends
//                }
//                Log.d("SPIN", "INSIDE prescriptionList getview :getview set. ");
//                SharedPreferences loggedPreferance = getActivity().getSharedPreferences(VTConstants.PREFS_LOGGEDUSERID, 0);
//                ViewHolder holder = (ViewHolder) convertView.getTag();
//                final ApptPrescriptionModel model = getItem(position);
////            holder.txtDate.setText(DateFormat.GetFormattedDateTimeStr(model.getCreatedAt()));
//                //holder.txtName.setText(VisirxApplication.appContext.getLoggedUser().getFirstName() + " " +  VisirxApplication.appContext.getLoggedUser().getLastName());
////            holder.txtName.setText(loggedPreferance.getString(VTConstants.LOGGED_USER_FULLNAME,"Not set"));
//                holder.txtName.setText("PRES:" + (position + 1));
//                try {
//                    date = inputFormatter.parse(model.getCreatedAt());
//                } catch (java.text.ParseException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                holder.txtTime.setText(DateFormat.GetFormattedDateTimeStr(model.getCreatedAt()) + " " + DateFormat.GetFormattedTimeStr(outputFormatter.format(date)));
//                if (model.getProcessFlag() == 0) {
//                    holder.imageTick.setImageResource(R.drawable.ic_done);
//                } else {
//                    holder.imageTick.setImageResource(R.drawable.ic_done_all);
//                }
//                holder.imageTick.setVisibility(View.INVISIBLE);
//
//                if (VTConstants.PDF_FILE_FORMAT.contains(model.getFileMimeType()))
//
//                {
//                    Log.d("SPINS", "Inside pdf : ");
//                    String filePath = model.getPreimageName();
//                    if (filePath != null) {
//                        Log.d("SPIN", "File path : " + filePath);
//
//                        File file = new File(filePath);
//
//                        if (file.exists()) {
//                            holder.imageThumnail.setImageResource(R.drawable.ic_new_pdf);
//                        } else {
//
//                            // do the download request if internet available.
//
//                            if (DOWNLOAD_COUNT_PRES < VTConstants.PRES_THUMBNAIL_DWNLD_COUNT) {
//
//                                DOWNLOAD_COUNT_PRES++;
//                                DownloadFileProvider downloadFileProvide = new DownloadFileProvider(getActivity(), VTConstants.NOTIFICATION_APPTS_PRESC);
//                                downloadFileProvide.downloadPresFile(model.getAppointmentId(),
//                                        model.getCreatedAt(),
//                                        model.getPatientId(), VTConstants.PROVIDER_PRES_REQUESTACTION,
//                                        VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
//
//                            }
//                            // if internet available, set the icon to progress bar.
//                            Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
//                                    R.drawable.visirx_default);
//
//                            holder.imageThumnail.setImageBitmap(defaultIcon);
//                        }
//                    } else {
//
//                        // do the download request if internet available.
//
//                        if (DOWNLOAD_COUNT_PRES < VTConstants.PRES_THUMBNAIL_DWNLD_COUNT) {
//
//                            DOWNLOAD_COUNT_PRES++;
//                            DownloadFileProvider downloadFileProvide = new DownloadFileProvider(getActivity(), VTConstants.NOTIFICATION_APPTS_PRESC);
//                            downloadFileProvide.downloadPresFile(model.getAppointmentId(),
//                                    model.getCreatedAt(),
//                                    model.getPatientId(), VTConstants.PROVIDER_PRES_REQUESTACTION,
//                                    VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
//
//                        }
//                        // if internet available, set the icon to progress bar.
//                        Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
//                                R.drawable.visirx_default);
//
//                        holder.imageThumnail.setImageBitmap(defaultIcon);
//                    }
//
//                } else if (VTConstants.DOC_FILE_FORMAT.contains(model.getFileMimeType())) {
//                    String filePath = model.getPreimageName();
//                    if (filePath != null) {
//                        Log.d("SPIN", "File path : " + filePath);
//
//                        File file = new File(filePath);
//
//                        if (file.exists()) {
//                            holder.imageThumnail.setImageResource(R.drawable.ic_doc);
//                        } else {
//
//                            // do the download request if internet available.
//                            if (DOWNLOAD_COUNT_PRES < VTConstants.PRES_THUMBNAIL_DWNLD_COUNT) {
//
//                                DOWNLOAD_COUNT_PRES++;
//                                DownloadFileProvider downloadFileProvide = new DownloadFileProvider(getActivity(), VTConstants.NOTIFICATION_APPTS_PRESC);
//                                downloadFileProvide.downloadPresFile(model.getAppointmentId(),
//                                        model.getCreatedAt(),
//                                        model.getPatientId(), VTConstants.PROVIDER_PRES_REQUESTACTION,
//                                        VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
//                            }
//
//                            // if internet available, set the icon to progress bar.
//                            Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
//                                    R.drawable.visirx_default);
//
//                            holder.imageThumnail.setImageBitmap(defaultIcon);
//                        }
//                    } else {
//
//                        // do the download request if internet available.
//                        if (DOWNLOAD_COUNT_PRES < VTConstants.PRES_THUMBNAIL_DWNLD_COUNT) {
//
//                            DOWNLOAD_COUNT_PRES++;
//                            DownloadFileProvider downloadFileProvide = new DownloadFileProvider(getActivity(), VTConstants.NOTIFICATION_APPTS_PRESC);
//                            downloadFileProvide.downloadPresFile(model.getAppointmentId(),
//                                    model.getCreatedAt(),
//                                    model.getPatientId(), VTConstants.PROVIDER_PRES_REQUESTACTION,
//                                    VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
//                        }
//
//                        // if internet available, set the icon to progress bar.
//                        Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
//                                R.drawable.visirx_default);
//
//                        holder.imageThumnail.setImageBitmap(defaultIcon);
//                    }
//
//                } else if (VTConstants.TXT_FILE_FORMAT.contains(model.getFileMimeType())) {
//                    String filePath = model.getPreimageName();
//                    if (filePath != null) {
//                        Log.d("SPIN", "File path : " + filePath);
//
//                        File file = new File(filePath);
//
//                        if (file.exists()) {
//                            holder.imageThumnail.setImageResource(R.drawable.ic_txt);
//                        } else {
//
//                            // do the download request if internet available.
//                            if (DOWNLOAD_COUNT_PRES < VTConstants.PRES_THUMBNAIL_DWNLD_COUNT) {
//
//                                DOWNLOAD_COUNT_PRES++;
//                                DownloadFileProvider downloadFileProvide = new DownloadFileProvider(getActivity(), VTConstants.NOTIFICATION_APPTS_PRESC);
//                                downloadFileProvide.downloadPresFile(model.getAppointmentId(),
//                                        model.getCreatedAt(),
//                                        model.getPatientId(), VTConstants.PROVIDER_PRES_REQUESTACTION,
//                                        VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
//                            }
//                            // if internet available, set the icon to progress bar.
//                            Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
//                                    R.drawable.visirx_default);
//
//                            holder.imageThumnail.setImageBitmap(defaultIcon);
//                        }
//                    } else {
//
//                        // do the download request if internet available.
//                        if (DOWNLOAD_COUNT_PRES < VTConstants.PRES_THUMBNAIL_DWNLD_COUNT) {
//
//                            DOWNLOAD_COUNT_PRES++;
//                            DownloadFileProvider downloadFileProvide = new DownloadFileProvider(getActivity(), VTConstants.NOTIFICATION_APPTS_PRESC);
//                            downloadFileProvide.downloadPresFile(model.getAppointmentId(),
//                                    model.getCreatedAt(),
//                                    model.getPatientId(), VTConstants.PROVIDER_PRES_REQUESTACTION,
//                                    VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
//                        }
//                        // if internet available, set the icon to progress bar.
//                        Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
//                                R.drawable.visirx_default);
//
//                        holder.imageThumnail.setImageBitmap(defaultIcon);
//                    }
//
//                } else if (VTConstants.XLSX_FILE_FORMAT.contains(model.getFileMimeType())) {
//                    String filePath = model.getPreimageName();
//                    if (filePath != null) {
//                        Log.d("SPIN", "File path : " + filePath);
//
//                        File file = new File(filePath);
//
//                        if (file.exists()) {
//                            holder.imageThumnail.setImageResource(R.drawable.ic_xlsx);
//                        } else {
//
//                            // do the download request if internet available.
//                            if (DOWNLOAD_COUNT_PRES < VTConstants.PRES_THUMBNAIL_DWNLD_COUNT) {
//
//                                DOWNLOAD_COUNT_PRES++;
//                                DownloadFileProvider downloadFileProvide = new DownloadFileProvider(getActivity(), VTConstants.NOTIFICATION_APPTS_PRESC);
//                                downloadFileProvide.downloadPresFile(model.getAppointmentId(),
//                                        model.getCreatedAt(),
//                                        model.getPatientId(), VTConstants.PROVIDER_PRES_REQUESTACTION,
//                                        VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
//                            }
//                            // if internet available, set the icon to progress bar.
//                            Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
//                                    R.drawable.visirx_default);
//
//                            holder.imageThumnail.setImageBitmap(defaultIcon);
//                        }
//                    } else {
//
//                        // do the download request if internet available.
//                        if (DOWNLOAD_COUNT_PRES < VTConstants.PRES_THUMBNAIL_DWNLD_COUNT) {
//
//                            DOWNLOAD_COUNT_PRES++;
//                            DownloadFileProvider downloadFileProvide = new DownloadFileProvider(getActivity(), VTConstants.NOTIFICATION_APPTS_PRESC);
//                            downloadFileProvide.downloadPresFile(model.getAppointmentId(),
//                                    model.getCreatedAt(),
//                                    model.getPatientId(), VTConstants.PROVIDER_PRES_REQUESTACTION,
//                                    VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
//                        }
//                        // if internet available, set the icon to progress bar.
//                        Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
//                                R.drawable.visirx_default);
//
//                        holder.imageThumnail.setImageBitmap(defaultIcon);
//                    }
//
//                } else if (VTConstants.IMAGE_MIME_FORMAT.contains(model.getFileMimeType())) {
//
//                    String thumbnail_photo = model.getPreThumbImageName();
//                    //Load the file from sd card stored path - starts
//
//
//                    if (thumbnail_photo != null) {
//                        // Toast.makeText(getActivity(),"Path is not null",Toast.LENGTH_SHORT).show();
//                        Log.d("SPIN", "Image path : " + thumbnail_photo);
//
//                        File imgFile = new File(thumbnail_photo);
//                        Uri emrThumbImgUri = Uri.fromFile(imgFile);
//
//                        if (imgFile.exists()) {
//
//                            Bitmap myBitmap = null;
//
//                            try {
//                                myBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), emrThumbImgUri);
//                            } catch (FileNotFoundException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                            holder.imageThumnail.setImageBitmap(myBitmap);
//
//                            //Load the file from sd card stored path - ends
//                        } else {
//
//                            if (DOWNLOAD_COUNT_PRES < VTConstants.PRES_THUMBNAIL_DWNLD_COUNT) {
//
//                                DOWNLOAD_COUNT_PRES++;
//                                DownloadFileProvider downloadFileProvide = new DownloadFileProvider(getActivity(), VTConstants.NOTIFICATION_APPTS_PRESC);
//                                downloadFileProvide.downloadPresFile(model.getAppointmentId(),
//                                        model.getCreatedAt(),
//                                        model.getPatientId(), VTConstants.PROVIDER_PRES_REQUESTACTION,
//                                        VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
//                            }
//
//                            //Toast.makeText(getActivity(),"PHOTO path Null",Toast.LENGTH_SHORT).show();
//                            Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
//                                    R.drawable.visirx_default);
//
//                            holder.imageThumnail.setImageBitmap(defaultIcon);
//                        }
//
//                    } else {
//
//                        if (DOWNLOAD_COUNT_PRES < VTConstants.PRES_THUMBNAIL_DWNLD_COUNT) {
//
//                            DOWNLOAD_COUNT_PRES++;
//                            DownloadFileProvider downloadFileProvide = new DownloadFileProvider(getActivity(), VTConstants.NOTIFICATION_APPTS_PRESC);
//                            downloadFileProvide.downloadPresFile(model.getAppointmentId(),
//                                    model.getCreatedAt(),
//                                    model.getPatientId(), VTConstants.PROVIDER_PRES_REQUESTACTION,
//                                    VTConstants.THUMBNAIL_FLAG, model.getFileMimeType());
//
//                        }
//
//                        //Toast.makeText(getActivity(),"PHOTO path Null",Toast.LENGTH_SHORT).show();
//                        Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
//                                R.drawable.visirx_default);
//
//                        holder.imageThumnail.setImageBitmap(defaultIcon);
//                    }
//                } else {
//                    Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
//                            R.drawable.visirx_default);
//                    holder.imageThumnail.setImageBitmap(defaultIcon);
//                }
//
//                holder.imageThumnail.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (VTConstants.FILE_MIME_FORMAT.contains(prescriptionList.get(position).getFileMimeType())) {
//
//                            try {
//
//                                if (prescriptionList.get(position).getPreimageName() == null) {
//                                    //give defaut image
////                                holder.imageThumnail.setImageResource(R.drawable.visirx_default);
//                                    Toast.makeText(getActivity(), "file does not exists.", Toast.LENGTH_SHORT).show();
//                                } else {
//
//                                    File file = new File(prescriptionList.get(position).getPreimageName());
//
//                                    if (file.exists()) {
////                                    holder.imageThumnail.setImageResource(R.drawable.ic_new_pdf);
//                                        File targetFile = new File(prescriptionList.get(position).getPreimageName());
//                                        Uri targetUri = Uri.fromFile(targetFile);
//                                        Intent intent;
//                                        intent = new Intent(Intent.ACTION_VIEW);
//                                        Log.v("FILESTORED", "file content type :. 1 :" + prescriptionList.get(position).getFileType().toString());
//                                        intent.setDataAndType(targetUri, prescriptionList.get(position).getFileType());
//                                        startActivity(intent);
//                                    } else {
//
////                                    holder.imageThumnail.setImageResource(R.drawable.visirx_default);
//                                        Toast.makeText(getActivity(), "file does not exists", Toast.LENGTH_SHORT).show();
//                                    }
//
//
//                                }
//
//                            } catch (ActivityNotFoundException e) {
//
//                                Toast.makeText(getActivity(), " Application not found  to view",
//                                        Toast.LENGTH_SHORT).show();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        } else {
//                            try {
//                                Log.d("IMAGEPOSITION", "position : image .java  :" + position);
////                                Intent intent = new Intent(getActivity(), AddPrescriptionActivity.class);
////                                intent.putExtra(VTConstants.FILE_NAME, model.getFileLabel());
////                                intent.putExtra(VTConstants.FILE_DATE, date);
////                                intent.putExtra(VTConstants.FILE_APPOINTMENTID, appointmentPatientModel.getReservationNumber());
////                                intent.putExtra(VTConstants.FILE_DATA, position);//TODO
//////                        intent.putExtra(VTConstants.ACTIVITY_ACTION, "EMR");//TODO
////                                startActivity(intent);
//
//                                Intent intent = new Intent(getActivity(), AddPrescriptionActivity.class);
//                                intent.putExtra(VTConstants.PRESCRIPTION_VIEW, true);
//                                intent.putExtra(VTConstants.APPTMODEL_KEY, appointmentPatientModel.getReservationNumber());
//                                intent.putExtra(VTConstants.PATIENT_DATE, appointmentPatientModel.getDate());
//                                intent.putExtra(VTConstants.PATIENT_ID, prescriptionList.get(position).getPatientId());
//                                intent.putExtra(VTConstants.FILE_DATA, position);
//                                startActivity(intent);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//
//                    }
//                });
//
//
//                return convertView;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.d("SPIN", "Inside prescription list getview : caught exception - " + e.getMessage());
//                //getView(position,convertView,parent);
//                return null;
//            }
//            //rony 1.3.4 ends
//        }
//
//        class ViewHolder {
//            NormalFont txtName;
//            NormalFont txtTime;
//            ImageView imageTick;
//            ImageView imageThumnail;
//
//            public ViewHolder(View view) {
//                txtName = (NormalFont) view.findViewById(R.id.text_prescription_name);
//                imageThumnail = (ImageView) view.findViewById(R.id.imgpreview);
//                txtTime = (NormalFont) view.findViewById(R.id.text_prescription_time);
//                imageTick = (ImageView) view.findViewById(R.id.imageTick);
//                view.setTag(this);
//            }
//        }
//    }


    class DigitalAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return digitalprescriptionModel.size();
        }

        @Override
        public AppDigitalprescriptionModel getItem(int position) {

            Log.d("PRESCRIPTIONFRAGMENT", "DigitalAdapter   getView"+digitalprescriptionModel.size());
            return digitalprescriptionModel.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                Log.d("PRESCRIPTIONFRAGMENT", "DigitalAdapter   getView");
                convertView = View.inflate(getActivity(), R.layout.prescription_imagelist, null);
                new ViewHolder(convertView);
            }

            final ViewHolder holder = (ViewHolder) convertView.getTag();
            try {
                final AppDigitalprescriptionModel model = getItem(position);
                try {
                    date=inputFormatter.parse(model.getCreatedAtclient());
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                holder.txtTime.setText(DateFormat.GetFormattedDateTimeStr(digitalprescriptionModel.get(position).getCreatedAtclient()) + " " + DateFormat.GetFormattedTimeStr(outputFormatter.format(date)));
                holder.txtName.setText("Prescription : " + (position + 1));

                if (model.getProcessFlag() == VTConstants.PROCESSED_FLAG_NOT_SENT) {
//                    holder.presTrash.setVisibility(View.GONE);
                    holder.imageTick.setImageResource(R.drawable.ic_done);
                } else {
//                    holder.presTrash.setVisibility(View.VISIBLE);
                    holder.imageTick.setImageResource(R.drawable.ic_done_all);
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            holder.presTrash.setVisibility(View.VISIBLE);


//            holder.presTrash.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//
//                        deleteAlert(digitalprescriptionModel.get(position).getPatientId(), digitalprescriptionModel.get(position).getCreatedAtclient(), digitalprescriptionModel.get(position).getAppointmentId());
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
            return convertView;
        }

        class ViewHolder {

            NormalFont txtName;
            NormalFont txtTime;
            ImageView imageTick;
            ImageView imageThumnail;
            ImageView presTrash;


            public ViewHolder(View view) {
                Log.d("PRESCRIPTIONFRAGMENT", "DigitalAdapter   ViewHolder");

                txtName = (NormalFont) view.findViewById(R.id.text_prescription_name);
                imageThumnail = (ImageView) view.findViewById(R.id.imgpreview);
                txtTime = (NormalFont) view.findViewById(R.id.text_prescription_time);
                imageTick = (ImageView) view.findViewById(R.id.imageTick);
//                presTrash = (ImageView) view.findViewById(R.id.prescitiontrash);
                view.setTag(this);
            }
        }
    }


    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {


                Log.d("PRESCRIPTIONFRAGMENT", "receiverUpdate download broadcast received...");
                progressBar.setVisibility(View.GONE);
                if (VTConstants.isInsertPrescriptionAction) {
                    // Toast.makeText(getActivity(), "Broadcast Received.isInsertPrescriptionAction", Toast.LENGTH_SHORT).show();
                    VTConstants.isInsertPrescriptionAction = false;
                } else {
                    //  Toast.makeText(getActivity(), "Broadcast Received.image downloaded.", Toast.LENGTH_SHORT).show();
                    tryCount--;
                }
                Log.d("PRESCRIPTIONFRAGMENT", " receiverUpdate   :   createdById  :::" + createdById);
                Log.d("PRESCRIPTIONFRAGMENT", " receiverUpdate   :   reservationNumber  :::" + reservationNumber);
                digitalprescriptionModel = VisirxApplication.digitalMainTableDAO.getTotalDigitalPrescription(createdById, reservationNumber);

                Log.d("PRESCRIPTIONFRAGMENT", " receiverUpdate   digitalprescriptionModel.size() :" + digitalprescriptionModel.size());
                textView.setVisibility(View.VISIBLE);
//                }
                if (adapter.getCount() == 0) {
                    Log.d("PRESCRIPTIONFRAGMENT", " receiverUpdate   IF  adapter.getCount() == 0  ");
                    textView.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                } else {

                    Log.d("PRESCRIPTIONFRAGMENT", " receiverUpdate   ELSE adapter.getCount() != 0");
                    textView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }
    };


    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(receiverUpdate);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        super.onDestroy();
    }

    private void snackBarShow() {
        SnackbarManager.show(
                Snackbar.with(getActivity()) // context
                        .text("No Internet connection...")
                        .actionLabel("Close")
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)//
                        .color(Color.BLACK) // change the background color// action button label
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                Log.d(TAG, "Undoing something");

                            }
                        }) // action button's ActionClickListener
                , getActivity());

    }

    private void snackBarHide() {
        SnackbarManager.dismiss();
    }
}
