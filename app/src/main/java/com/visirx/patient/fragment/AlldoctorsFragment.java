package com.visirx.patient.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.com.visirx.patient.adapter.RecyclerAdapter_AllDoctors;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;

import java.util.List;

public class AlldoctorsFragment extends Fragment {
    String TAG = PrescriptionFragment.class.getName();
    RecyclerView recyclerView;
    TextView textView;
    List<FindDoctorModel> findDoctorModelList;
    RecyclerAdapter_AllDoctors adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alldoctors, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        getActivity().registerReceiver(receiverUpdate, new IntentFilter(VTConstants.ALL_DOCTOR));
        getActivity().registerReceiver(receiverUpdate, new IntentFilter(VTConstants.NOTIFICATION_APPTS));

        init();
    }

    private void init() {

        findDoctorModelList = VisirxApplication.customerDAO.getAvailableDoctor();
        textView = (TextView) getView().findViewById(R.id.txt_info);
        recyclerView = (RecyclerView) getView().findViewById(R.id.my_recycler_view);

        if (findDoctorModelList != null) {
            if (findDoctorModelList.size() > 0) {
                adapter = new RecyclerAdapter_AllDoctors(getContext(), findDoctorModelList);
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
            }
        } else {
            textView.setVisibility(View.VISIBLE);
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

                findDoctorModelList = VisirxApplication.customerDAO.getAvailableDoctor();
                if (findDoctorModelList != null) {

                    for (int i = 0; i < findDoctorModelList.size(); i++) {

//                        EventChecking.myDoctors(findDoctorModelList.get(i).getDoctorId(), "" +
//                                findDoctorModelList.get(i).getDoctorFirstName() + " " + findDoctorModelList.get(i).getDoctorLastName(), findDoctorModelList.size(), context);

                    }
                    RecyclerAdapter_AllDoctors adapter = new RecyclerAdapter_AllDoctors(getContext(), findDoctorModelList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter.notifyDataSetChanged();

                    if (findDoctorModelList.size() > 0) {
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setVisibility(View.VISIBLE);
                    }

                } else {
                    textView.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }

        }
    };
}
