package com.visirx.patient.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.DigitalprescriptionDataModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.VTConstants;

import java.text.SimpleDateFormat;
import java.util.List;

public class DigitalPrescriptionViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    AppAdapter adapter = null;
    ListView Medicinelist;
    NormalFont appDate;
    NormalFont appId;
    SimpleDateFormat inputFormatter, outputFormatter;
    int reservationNumber, filePosition;
    String patientId, createdAtclient, appointmentDate;
    List<DigitalprescriptionDataModel> digitalprescriptionDataModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digitalprescriptionview);
        toolbar = (Toolbar) findViewById(R.id.toolbar_digitalview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Medicinelist = (ListView) findViewById(R.id.medicinListView);
        appId = (NormalFont) findViewById(R.id.prescription_appId);
        appDate = (NormalFont) findViewById(R.id.prescription_appDate);


        if (getIntent() != null) {

            reservationNumber = getIntent().getIntExtra(VTConstants.APPTMODEL_KEY, -1);
            patientId = getIntent().getStringExtra(VTConstants.PATIENT_ID);
            createdAtclient = getIntent().getStringExtra(VTConstants.CREATEDAT_CLIENT);
            appointmentDate = getIntent().getStringExtra(VTConstants.APPOINTMENT_DATE);
            filePosition = getIntent().getIntExtra(VTConstants.FILE_DATA, -1);
            Log.d("PrescriptionView", "reservationNumber   :  " + reservationNumber);
            Log.d("PrescriptionView", "patientId   :     " + patientId);
            Log.d("PrescriptionView", "createdAt   :     " + createdAtclient);
            Log.d("PrescriptionView", "appointmentDate   :     " + appointmentDate);
            Log.d("PrescriptionView", "filePosition   :     " + filePosition);

        }

        try {

            appId.setText("APPOINTMENT ID : " + String.valueOf(reservationNumber));
//            appDate.setText(appointmentDate);
            appDate.setText(DateFormat.GetFormattedDateStr(appointmentDate));

            digitalprescriptionDataModelList = VisirxApplication.digitaTableDAO.getPriscriptionList(patientId, String.valueOf(reservationNumber), createdAtclient);
            Log.d("PrescriptionView", "  digitalprescriptionDataModelList.size()  : " + digitalprescriptionDataModelList.size());

            adapter = new AppAdapter();
            Medicinelist.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return digitalprescriptionDataModelList.size();
        }

        @Override
        public DigitalprescriptionDataModel getItem(int position) {
            return digitalprescriptionDataModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(DigitalPrescriptionViewActivity.this, R.layout.dispalyprescription, null);
                new ViewHolder(convertView);
            }

            try {

                final ViewHolder holder = (ViewHolder) convertView.getTag();
                DigitalprescriptionDataModel model = getItem(position);
                Log.d("VIEW", "getDrugName  : " + model.getDrugName());
                Log.d("VIEW", "getDosage  : " + model.getDosage());
                Log.d("VIEW", "getFoodTime  : " + model.getMedicineIntake());
                Log.d("VIEW", "position   : " + String.valueOf(position + 1));

                holder.positionnumber.setText(String.valueOf(position + 1));
                holder.drugnames.setText(digitalprescriptionDataModelList.get(position).getDrugName());
                holder.Dosage.setText(digitalprescriptionDataModelList.get(position).getDosage());

                holder.duration.setText(String.valueOf(digitalprescriptionDataModelList.get(position).getDuration()));

                if (digitalprescriptionDataModelList.get(position).getNotes() != null && !digitalprescriptionDataModelList.get(position).getNotes().isEmpty()) {
                    Log.d("PrescriptionView", "pupUpsave IF       :     ");
                    holder.Notes.setText(digitalprescriptionDataModelList.get(position).getNotes());
                } else {
                    holder.Notes.setText("-");
                    Log.d("PrescriptionView", "pupUpsave ELSE       :     ");

                }
                holder.FoodType.setText(digitalprescriptionDataModelList.get(position).getMedicineIntake());


                if (digitalprescriptionDataModelList.get(position).getMorning() != 0) {
                    holder.m.setText(" M ");
                } else {
                    holder.m.setText("-");
                }
                if (digitalprescriptionDataModelList.get(position).getNoon() != 0) {
                    holder.a.setText(" A ");
                } else {
                    holder.a.setText("-");
                }
                if (digitalprescriptionDataModelList.get(position).getEvening() != 0) {
                    holder.e.setText(" E ");
                } else {
                    holder.e.setText("-");
                }
                if (digitalprescriptionDataModelList.get(position).getNight() != 0) {
                    holder.n.setText(" N ");
                } else {
                    holder.n.setText("-");
                }



            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        class ViewHolder {

            NormalFont drugnames;
            NormalFont positionnumber;
            NormalFont Dosage;
            NormalFont duration;
            NormalFont Notes;
            NormalFont FoodType;
            NormalFont m;
            NormalFont a;
            NormalFont e;
            NormalFont n;


            public ViewHolder(View view) {
                drugnames = (NormalFont) view.findViewById(R.id.drugnamesss1);
                positionnumber = (NormalFont) view.findViewById(R.id.card_position);
                Dosage = (NormalFont) view.findViewById(R.id.dosagesss1);
                duration = (NormalFont) view.findViewById(R.id.tabletssss);
                Notes = (NormalFont) view.findViewById(R.id.notesss);
                FoodType = (NormalFont) view.findViewById(R.id.foodtypesss1);
                m = (NormalFont) view.findViewById(R.id.timingsss);
                a = (NormalFont) view.findViewById(R.id.timingsss1);
                e = (NormalFont) view.findViewById(R.id.timingsss2);
                n = (NormalFont) view.findViewById(R.id.timingsss3);
                view.setTag(this);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);


    }
}
