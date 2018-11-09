package com.visirx.patient.fragment;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.AusculationActivity;
import com.visirx.patient.activity.Auscultation;
import com.visirx.patient.activity.VitalSign;
import com.visirx.patient.activity.VitalsActivity;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.AddEMRFileProvider;
import com.visirx.patient.common.provider.EMRFileListProvider;
import com.visirx.patient.customview.ActionEditText;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.EMRModel;
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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.content.SharedPreferences;
import android.graphics.Matrix;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;

import android.provider.MediaStore;

import android.util.Log;
import android.view.Gravity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Emr extends Fragment {
    String TAG = Emr.class.getName();
    List<EMRModel> modelList = null;
    AppointmentPatientModel appointmentPatientModel = null;
    ImageAdapter adapter = null;
    NormalFont textView = null;
    ProgressBar progressBar = null;
    GridView gridview = null;
    NormalFont appId;
    NormalFont appDate;
    String createdById = "";

    SharedPreferences loggedPreferance;

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static Emr newInstance(int reservationNumber) {
        Emr fragment = new Emr();
        Bundle bundle = new Bundle();
        bundle.putInt(VTConstants.APPTMODEL_KEY, reservationNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Emr() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_emr,
                container, false);
        int reservationNumber = getArguments().getInt(
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
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private void initView() {
        loggedPreferance = getActivity().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        createdById = loggedPreferance.getString(VTConstants.USER_ID, "Not set");
        ArrayList<AddEmrFileModel> emrmodelList = VisirxApplication.aptEmrDAO
                .GetPatientEMRFile(createdById,
                        appointmentPatientModel.getReservationNumber());
        Log.d("SPIN", "INSIDE EMR - initView - patient id : " + createdById);
        Log.d("SPIN", "INSIDE EMR - initView -resno :  " + appointmentPatientModel.getReservationNumber());
        Log.d("SPIN", "INSIDE EMR - initView - " + emrmodelList.size());
        ArrayList<String> emrFile = VisirxApplication.aptEmrDAO
                .GetPatientEMRFileMax(createdById,
                        appointmentPatientModel.getReservationNumber(),createdById);
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
            progressBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            gridview.setVisibility(View.GONE);
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
                    intent.putExtra(VTConstants.DATE, DateFormat
                            .GetFormattedDate_YYYYMMDD(model.getEmrDate()));
                    getActivity().startActivity(intent);
                } else if (model.getHeader().equalsIgnoreCase("IMAGE")) {
                    Intent intent = new Intent(getActivity(),
                            com.visirx.patient.activity.Image.class);
                    intent.putExtra(VTConstants.APPTMODEL_KEY,
                            appointmentPatientModel.getReservationNumber());
                    intent.putExtra(VTConstants.DATE, DateFormat
                            .GetFormattedDate_YYYYMMDD(model.getEmrDate()));
                    getActivity().startActivity(intent);
                } else if (model.getHeader().equalsIgnoreCase("VITAL SIGN")) {
                    Intent intent = new Intent(getActivity(),
                            VitalSign.class);
                    intent.putExtra(VTConstants.APPTMODEL_KEY,
                            appointmentPatientModel.getReservationNumber());
                    intent.putExtra(VTConstants.DATE, DateFormat
                            .GetFormattedDate_YYYYMMDD(model.getEmrDate()));
                    getActivity().startActivity(intent);
                }

            }
        });
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
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//
//        if (item.getItemId() == android.R.id.home)
//        {
////            if(VTConstants.HISTORYCLICKED == 1)
////            {
////                Intent intent = new Intent(getContext(), Appointment_History.class);
////                intent.putExtra(VTConstants.CUSTOMER_ID, appointmentModel.getCustomerId());
////                startActivity(intent);
////            }
////            else
////            {
//                getActivity().finish();
////            }
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
                    modelList = AppContext.emrListVitals(
                            createdById,
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
