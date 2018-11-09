package com.visirx.patient.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.visirx.patient.com.visirx.patient.adapter.RecyclerAdapter_Cancelappointment;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;

import java.util.List;

public class CancelappointmentFragment extends Fragment {

    RecyclerAdapter_Cancelappointment recyclerAdapter_cancelappointment;
    List<AppointmentPatientModel> cancelappointmentList;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView_cancel;
    String UserID;
    TextView Noappointment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cancelappointment1, container, false);
        getActivity().registerReceiver(receiverUpdate, new IntentFilter(VTConstants.CANCELAPPOINT_BROADCAST));
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView_cancel = (RecyclerView) getView().findViewById(R.id.cancel_appoint_recycler_view);
        Noappointment = (TextView) getView().findViewById(R.id.txt_cancelappoint);
        sharedPreferences = getActivity().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        UserID = sharedPreferences.getString(VTConstants.USER_ID, null);
        cancelappointmentList = VisirxApplication.aptDAO.getCancelCompletedappint(UserID);

        if (cancelappointmentList != null) {

            for (int i = 0; i < cancelappointmentList.size(); i++) {

//                EventChecking.canceledAppointments(cancelappointmentList.get(i).getPerfomerid(), cancelappointmentList.get(i).getReservationNumber(), cancelappointmentList.get(i).getStatus(), String.valueOf(cancelappointmentList.size()), getActivity());
            }

            recyclerAdapter_cancelappointment = new RecyclerAdapter_Cancelappointment(getContext(), cancelappointmentList);
            recyclerView_cancel.setAdapter(recyclerAdapter_cancelappointment);
            recyclerView_cancel.setHasFixedSize(true);
            recyclerView_cancel.setLayoutManager(new LinearLayoutManager(getContext()));
            Noappointment.setVisibility(View.GONE);

            if (cancelappointmentList.size() > 0) {
                Noappointment.setVisibility(View.GONE);
            } else {
                Noappointment.setVisibility(View.VISIBLE);
            }
        } else {
            Noappointment.setVisibility(View.VISIBLE);
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
                cancelappointmentList = VisirxApplication.aptDAO.getCancelCompletedappint(UserID);
                if (cancelappointmentList != null) {
                    if (cancelappointmentList.size() > 0) {
                        recyclerAdapter_cancelappointment = new RecyclerAdapter_Cancelappointment(getContext(), cancelappointmentList);
                        recyclerView_cancel.setAdapter(recyclerAdapter_cancelappointment);
                        recyclerView_cancel.setHasFixedSize(true);
                        recyclerView_cancel.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerAdapter_cancelappointment.notifyDataSetChanged();
                        Noappointment.setVisibility(View.GONE);
                    } else {
                        Noappointment.setVisibility(View.VISIBLE);
                    }
                } else {
                    Noappointment.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
