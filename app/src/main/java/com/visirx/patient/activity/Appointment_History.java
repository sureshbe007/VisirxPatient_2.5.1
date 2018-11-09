package com.visirx.patient.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.AppointmentProvider;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.AppointmentModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.VTConstants;

import java.util.List;

public class Appointment_History extends AppCompatActivity {

    String TAG = Appointment_History.class.getName();
    ListView listview = null;
    ApptHistoryAdapter adapter = null;
    List<AppointmentModel> historyList = null;
    String customerId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmenthistory);
        VTConstants.HISTORYCLICKED = 0;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Appointment History");

        if (getIntent() != null) {
            customerId = getIntent().getStringExtra(VTConstants.CUSTOMER_ID);
        }

        AppointmentProvider provider = new AppointmentProvider(this);
        provider.SendApptsHistoryReq(customerId);

//        historyList = VisirxApplication.aptDAO.GetAppointmentsPrevious(customerId, null);


        adapter = new ApptHistoryAdapter();
        listview = (ListView) findViewById(R.id.card_listView);
        listview.setAdapter(adapter);

        registerReceiver(receiverUpdate, new IntentFilter(VTConstants.NOTIFICATION_APPT_HISTORY));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                VTConstants.HISTORYITEMCLICKED = 1;
                AppointmentModel item = historyList.get(position);
                Intent intent = new Intent(Appointment_History.this, PatientDetailsActivity.class);
                intent.putExtra(VTConstants.APPTMODEL_KEY, item.getReservationNumber());
//                intent.putExtra("FLAG_APT_HIS", -1);
//                 finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            VTConstants.HISTORYITEMCLICKED = 0;
            VTConstants.HISTORYCLICKED = 0;
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class ApptHistoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return historyList.size();
        }

        @Override
        public AppointmentModel getItem(int position) {
            return historyList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getBaseContext(),
                        R.layout.appmnt_history_list_item, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            AppointmentModel model = getItem(position);
            holder.txtDateStatus.setText(DateFormat.GetFormattedDateStr(model.getDate()) + " - " + model.getStatus());
            holder.txtSymptoms.setText(model.getSymptoms());
            return convertView;
        }

        class ViewHolder {
            MediumFont txtDateStatus ;
            NormalFont txtSymptoms ;
            public ViewHolder(View view) {
                txtDateStatus = (MediumFont)view.findViewById(R.id.lblApmntHisStatus);
                txtSymptoms = (NormalFont)view.findViewById(R.id.lblApmntHisDate);
                view.setTag(this);
            }
        }
    }

    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LogTrace.i(TAG, "Appts History broadcast received...");
//                historyList = VisirxApplication.aptDAO.GetAppointmentsPrevious(customerId,null);

                if(historyList.size() > 0 ){
                    adapter.notifyDataSetChanged();
                }
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
            unregisterReceiver(receiverUpdate);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        super.onDestroy();
    }
}