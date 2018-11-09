package com.visirx.patient.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.Appointment_History;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.ParamedicDetailsProvider;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.customview.RoundImage;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.CustomerProfileModel;
import com.visirx.patient.model.ParamedicDetailsModel;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PatientProfileFragment extends Fragment {

    String TAG = PatientProfileFragment.class.getName();
    AppointmentPatientModel appointmentModel = null;
    RoundImage roundedImage = null;
    ArrayAdapter<String> adapter = null;
    public static LinearLayout apnmntdetails;
    ParamedicDetailsModel paramedicModel = null;
    public static int apmnt = 1;
    int reservationNumber;
    SharedPreferences sharedPreferences;
    String PatientID, PatientAddress;
    CustomerProfileModel customerProfileModel;


    public static PatientProfileFragment newInstance(int reservationNumber, int aptHis) {
        PatientProfileFragment fragment = new PatientProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(VTConstants.APPTMODEL_KEY, reservationNumber);
        fragment.setArguments(bundle);
        apmnt = aptHis;
        return fragment;
    }

    public PatientProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            getActivity().registerReceiver(receiverParamedicDetails,
                    new IntentFilter(VTConstants.NOTIFICATION_NURSE_ASSIGNED));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        sharedPreferences = getActivity().getSharedPreferences(
                VTConstants.LOGIN_PREFRENCES_NAME, 0);
        sharedPreferences = getActivity().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        PatientID = sharedPreferences.getString(VTConstants.USER_ID, "null");
        Log.d("FINALCHECK  patientID", "" + PatientID);
//        PatientAddress = VisirxApplication.userRegisterDAO.getUserAddress(PatientID);
        customerProfileModel = VisirxApplication.userRegisterDAO.getUserDetails(PatientID);
        reservationNumber = getArguments().getInt(VTConstants.APPTMODEL_KEY, -1);
        Log.d("FINALCHECK ", "" + reservationNumber);
        appointmentModel = VisirxApplication.aptDAO.GetAppointmentsByID(reservationNumber);
        paramedicModel = new ParamedicDetailsModel();
        paramedicModel = VisirxApplication.aptDAO.GetParamedicDetailsByID(reservationNumber);
        View v = inflater.inflate(R.layout.fragment_patientprofile, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private void initView() {
        apnmntdetails = (LinearLayout) getActivity().findViewById(R.id.apt_layout);
        if (apmnt == -1) {
            if (apnmntdetails != null) {
                apnmntdetails.setVisibility(View.GONE);
            }
        }
        ImageView imageView1 = (ImageView) getActivity().findViewById(R.id.profile_image);
        //rony Dashboard GC - starts
        String thumbnail_photo = appointmentModel.getCustomerImageThumbnailPath();
        if (thumbnail_photo != null) {
            Log.d("SPIN", "Image path : for id : " + appointmentModel.getReservationNumber());
            Log.d("SPIN", "Image path :" + thumbnail_photo);
            //Load the file from sd card stored path - starts
            File imgFile = new File(thumbnail_photo);
            Uri profImgThumbUri = Uri.fromFile(imgFile);
            if (imgFile.exists()) {
                Bitmap myBitmap = null;
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), profImgThumbUri);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                imageView1.setImageBitmap(myBitmap);
            } else {
                Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.default_image);
                imageView1.setImageBitmap(defaultIcon);
            }

        } else {
            Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
                    R.drawable.default_image);
            imageView1.setImageBitmap(defaultIcon);
        }
        //rony Dashboard GC - ends
        NormalFont textView = (NormalFont) getActivity().findViewById(R.id.patient_name);
        textView.setText(appointmentModel.getFirstName() + " " + appointmentModel.getLastName());
        NormalFont textViewAge = (NormalFont) getActivity().findViewById(R.id.person_age);
        textViewAge.setText(appointmentModel.getAge() + ", ");
        NormalFont textViewGender = (NormalFont) getActivity().findViewById(R.id.person_gender);
        textViewGender.setText(appointmentModel.getGender());
        NormalFont textViewSymtoms = (NormalFont) getActivity().findViewById(R.id.symt_content);
        NormalFont textViewID = (NormalFont) getActivity().findViewById(R.id.visirx_id);
        textViewID.setText(appointmentModel.getPerfomerid());

        System.out.println("SYMPTOMS" + appointmentModel.getSymptoms());
//        String[] symptoms = appointmentModel.getSymptoms().toString().trim().split("||");
//
//        if (!appointmentModel.getSymptoms().toString().isEmpty()) {
//            if (symptoms.length > 0) {
//                textViewSymtoms.setText(symptoms[0]);
//            }
//            if (symptoms.length > 1) {
//                textViewSymtoms.setText(symptoms[0] + "\n" + symptoms[1]);
//            }
//
//        }
        textViewSymtoms.setText(appointmentModel.getSymptoms());

        NormalFont textViewAddress = (NormalFont) getActivity().findViewById(R.id.addr_content);


        String[] parts = customerProfileModel.getCustomerAddress().toString().trim().split("~");
        textViewAddress.setText("");


        if (parts.length > 0) {
            textViewAddress.setText(parts[0] + "\n" + customerProfileModel.getCustomerZipcode());
        }
        if (parts.length > 1) {
            textViewAddress.setText(parts[0] + "\n" + parts[1] + "\n" + customerProfileModel.getCustomerZipcode());
        }
        if (parts.length > 2) {

            if(parts[1].length()==0)
            {

                textViewAddress.setText(parts[0] + "\n" + parts[2] + "\n" + customerProfileModel.getCustomerZipcode());
            }
            else {

                textViewAddress.setText(parts[0] + "\n" + parts[1].trim() + "\n" + parts[2] + "\n" + customerProfileModel.getCustomerZipcode());
            }

        }

//        TextView viewAll = (TextView) getActivity().findViewById(R.id.view_all_apt);
//        viewAll.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Appointment_History.class);
//                intent.putExtra(VTConstants.CUSTOMER_ID, appointmentModel.getPerfomerid());
//                VTConstants.HISTORYCLICKED = 1;
//                getActivity().startActivity(intent);
//            }
//        });
        if (VTConstants.checkAvailability(getActivity())) {
            //Garbage collection fix
        /*AppointmentProvider provider1 = new AppointmentProvider(getActivity());
        provider1.SendApptsHistoryReq(appointmentModel.getCustomerId());*/
            //Garbage collection fix
            String nurseUpdatedTime = paramedicModel.getLastUpdatedTimestamp();
            int isAssigned = 0;
            if (nurseUpdatedTime == null) {
                isAssigned = 0;
            } else {
                isAssigned = 1;
            }
            ParamedicDetailsProvider provider2 = new ParamedicDetailsProvider(getActivity());
            provider2.SendParamedicDetailsReq(appointmentModel.getPerfomerid(), appointmentModel.getReservationNumber(), isAssigned, nurseUpdatedTime);

        }
//        final List<AppointmentModel> aptHistoryList = VisirxApplication.aptDAO
//                .GetAppointmentsPrevious(appointmentModel.getCustomerId(), "2");
//        if (aptHistoryList.size() > 0) {
//            final ArrayList<String> list = new ArrayList<String>();
//            for (int i = 0; i < aptHistoryList.size(); ++i) {
//                AppointmentModel model = aptHistoryList.get(i);
//                list.add(DateFormat.GetFormattedDateStr(model.getDate()) + " - " + model.getStatus());
//            }
//            adapter = new ArrayAdapter<String>(getActivity(), R.layout.preview_history_list_item, R.id.textViewItem,list);
//            ListView listView = (ListView) getActivity().findViewById(R.id.appt_hist_listview);
//            listView.setAdapter(adapter);
//            listView.setDivider(null);
//            listView.setDividerHeight(0);
//            // PATIENT HISTORY ACTIVITY
//
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    AppointmentModel item = aptHistoryList.get(position);
//                    Intent intent = new Intent(getActivity(), Appointment_History.class);
////                    intent.putExtra(VTConstants.APPTMODEL_KEY, item.getReservationNumber());
//                    intent.putExtra(VTConstants.CUSTOMER_ID, appointmentModel.getCustomerId());
////                    intent.putExtra("FLAG_APT_HIS", -1);
//                    VTConstants.HISTORYCLICKED = 1;
//                    startActivity(intent);
//
//                }
//            });
//
//        } else {
//            AppointmentProvider provider = new AppointmentProvider(getActivity());
//            provider.SendApptsHistoryReq(appointmentModel.getCustomerId());
//        }
//
//
//        DisplayParamedicData();
//
//        LogTrace.w(
//                TAG,
//                "paramedicModel PRINT  : "
//                        + paramedicModel.getNurseFirstName() + " : Assigned : " + paramedicModel.isNurseAssigned());
//
//
    }

    private void DisplayParamedicData() {
        paramedicModel = new ParamedicDetailsModel();
        paramedicModel = VisirxApplication.aptDAO.GetParamedicDetailsByID(reservationNumber);
        if (paramedicModel.isNurseAssigned()) {
            ImageView imageNurse = (ImageView) getActivity().findViewById(R.id.nurse_photo);
            imageNurse.setVisibility(View.VISIBLE);
            // rony - PrescriptionGC - Starts
            String thumbnail_photo = paramedicModel.getNurseThumbImgPath();
            if (thumbnail_photo != null) {
                Log.d("SPIN", "Image path : for id : " + paramedicModel.getAppointmentId());
                Log.d("SPIN", "Image path :" + thumbnail_photo);
                //Load the file from sd card stored path - starts
                File imgFile = new File(thumbnail_photo);
                Uri profImgThumbUri = Uri.fromFile(imgFile);
                if (imgFile.exists()) {
                    Bitmap myBitmap = null;
                    try {
                        myBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), profImgThumbUri);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    imageNurse.setImageBitmap(myBitmap);
                } else {
                    Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
                            R.drawable.default_image);
                    imageNurse.setImageBitmap(defaultIcon);
                }

            } else {
                Bitmap defaultIcon = BitmapFactory.decodeResource(getActivity().getResources(),
                        R.drawable.default_image);
                imageNurse.setImageBitmap(defaultIcon);
            }

         /*byte[] photoNurse = paramedicModel.getNursePhotoByte();
            if (photoNurse != null) {
            byte[] bytearray = Base64.decode(photoNurse, Base64.DEFAULT);

            Bitmap b = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
            if (b != null) {
               int width = (int) getResources().getDimension(R.dimen.persion_image);
               roundedImage = new RoundImage(AppContext.getResizedBitmap(b, width, width));
               imageNurse.setImageDrawable(roundedImage);
            }
         }*/
            // rony - PrescriptionGC - Ends
            MediumFont textViewNurseName = (MediumFont) getActivity().findViewById(R.id.nurse_name);
            textViewNurseName.setVisibility(View.VISIBLE);
            textViewNurseName.setText(paramedicModel.getNurseFirstName() + " " + paramedicModel.getNurseLastName());
            NormalFont textViewNurseAge = (NormalFont) getActivity().findViewById(R.id.nurse_age);
            textViewNurseAge.setVisibility(View.VISIBLE);
            textViewNurseAge.setText(Integer.toString(paramedicModel.getNurseAge()));
            NormalFont Comma2 = (NormalFont) getActivity().findViewById(R.id.lblComma2);
            Comma2.setVisibility(View.VISIBLE);
            NormalFont textViewNurseGender = (NormalFont) getActivity().findViewById(R.id.nurse_gender);
            textViewNurseGender.setVisibility(View.VISIBLE);
            textViewNurseGender.setText(", " + paramedicModel.getNurseGender());
            NormalFont textViewIDNurse = (NormalFont) getActivity().findViewById(R.id.nvx_id);
            textViewIDNurse.setVisibility(View.VISIBLE);
            textViewIDNurse.setText("Visirx ID: ");
            NormalFont textViewIDNurse1 = (NormalFont) getActivity().findViewById(R.id.nursevisirx_id);
            textViewIDNurse1.setVisibility(View.VISIBLE);
            textViewIDNurse1.setText(paramedicModel.getNurseId());
// CALLING FUNTIONALITY
            ImageView imView = (ImageView) getActivity().findViewById(R.id.calltoParamedic);
            imView.setVisibility(View.VISIBLE);
            imView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (paramedicModel.getNurseMobileNumber() != null) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + paramedicModel.getNurseMobileNumber()));
                        startActivity(callIntent);
                    }
                }
            });

        } else {
            ImageView imageNurse = (ImageView) getActivity().findViewById(R.id.nurse_photo);
            imageNurse.setVisibility(View.GONE);
            MediumFont textViewNurseName = (MediumFont) getActivity().findViewById(R.id.nurse_name);
            textViewNurseName.setText("Yet to be assigned.");
            NormalFont textViewNurseAge = (NormalFont) getActivity().findViewById(R.id.nurse_age);
            textViewNurseAge.setVisibility(View.GONE);
            NormalFont textViewNurseGender = (NormalFont) getActivity().findViewById(R.id.nurse_gender);
            textViewNurseGender.setVisibility(View.GONE);
            NormalFont textViewIDNurse = (NormalFont) getActivity().findViewById(R.id.nvx_id);
            textViewIDNurse.setVisibility(View.GONE);
            NormalFont textViewIDNurse1 = (NormalFont) getActivity().findViewById(R.id.nursevisirx_id);
            textViewIDNurse1.setVisibility(View.GONE);
            ImageView imView = (ImageView) getActivity().findViewById(R.id.calltoParamedic);
            imView.setVisibility(View.GONE);
            NormalFont Comma2 = (NormalFont) getActivity().findViewById(R.id.lblComma2);
            Comma2.setVisibility(View.GONE);
        }

    }

    private BroadcastReceiver receiverParamedicDetails = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("SPIN", "START PAINT PARAMEDIC DATA.");
            try {
                DisplayParamedicData();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("SPIN", "START PAINT PARAMEDIC DATA CAUGHT EXCEPTION : " + e.getMessage());
            }
        }
    };

    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(receiverParamedicDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}