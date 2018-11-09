package com.visirx.patient.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.com.visirx.patient.adapter.AllappointmentListCallback;
import com.visirx.patient.com.visirx.patient.adapter.RecyclerAdapter_Allappointment;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.AllappointListProvider;
import com.visirx.patient.common.provider.CareTeamLIstProvider;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.AppointmentModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.CustomerProfileModel;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;

import java.util.List;

public class AllappointmentFragment extends Fragment {
    String TAG = AllappointmentFragment.class.getName();
    List<AppointmentPatientModel> AllappoinmetList;
    List<FindDoctorModel> AllappointDoctorList;
    RecyclerView recyclerView_allappoint;
    TextView Noappointment;
    SharedPreferences sharedPreferences;
    String UserID;
    String UserFullName;
    CustomerProfileModel customerProfileModel;
    RecyclerAdapter_Allappointment adapter;
    int AppointmentNumber = -1;
    int aptHis = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_allapoinment, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().registerReceiver(receiverUpdate, new IntentFilter(VTConstants.NOTIFICATION_APPTS));
        init();
    }

    private void init() {

        recyclerView_allappoint = (RecyclerView) getView().findViewById(R.id.appoint_recycler_view);
        Noappointment = (TextView) getView().findViewById(R.id.txt_noappoint);
        sharedPreferences = getActivity().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        // User ID
        UserID = sharedPreferences.getString(VTConstants.USER_ID, null);
        // Appointments From Table
        AllappoinmetList = VisirxApplication.aptDAO.getAllappt();
        // Customer  From UserRegTable
        customerProfileModel = VisirxApplication.userRegisterDAO.getUserDetails(UserID);
        UserFullName = customerProfileModel.getCustomerFirstName() + " " + customerProfileModel.getCustomerLastName();
        int totalappoint = AllappoinmetList.size();
        System.out.println("ALLAPPOINT    UserID:  " + UserID + "FULLNAME:   " + UserFullName + "   " + totalappoint);


        if (AllappoinmetList != null) {

            if (AllappoinmetList.size() > 0) {
                for (int i = 0; i < AllappoinmetList.size(); i++) {
//                    EventChecking.myTotalappointments(AllappoinmetList.get(i).getPerfomerid(), AllappoinmetList.get(i).getReservationNumber(), AllappoinmetList.get(i).getStatus(),String.valueOf(AllappoinmetList.size()), getActivity());
                }
                adapter = new RecyclerAdapter_Allappointment(getContext(), AllappoinmetList);
                recyclerView_allappoint.setAdapter(adapter);
                recyclerView_allappoint.setHasFixedSize(true);
                recyclerView_allappoint.setLayoutManager(new LinearLayoutManager(getContext()));
                Noappointment.setVisibility(View.GONE);
            } else {
                Noappointment.setVisibility(View.VISIBLE);
            }
        } else {
            Noappointment.setVisibility(View.VISIBLE);
        }


        if (VTConstants.checkAvailability(getActivity())) {
            CareTeamLIstProvider careTeamLIstProvider = new CareTeamLIstProvider(getActivity());
            careTeamLIstProvider.CareteamReq(UserID);
            AllappointListProvider allappointListProvider = new AllappointListProvider(getActivity());
            allappointListProvider.AllappointmentReq(UserID);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiverUpdate);
    }


    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                Log.d("TRIGER", "Appts Notes download broadcast received...");
                AllappoinmetList = VisirxApplication.aptDAO.getAllappt();

                if (AllappoinmetList != null) {

                    adapter = new RecyclerAdapter_Allappointment(getContext(), AllappoinmetList);
                    recyclerView_allappoint.setAdapter(adapter);
                    recyclerView_allappoint.setHasFixedSize(true);
                    recyclerView_allappoint.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter.notifyDataSetChanged();

                    if (AllappoinmetList.size() > 0) {
                        Noappointment.setVisibility(View.GONE);
                    } else {
                        Noappointment.setVisibility(View.VISIBLE);
                    }
                } else {
                    Noappointment.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }
    };
}
