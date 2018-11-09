package com.visirx.patient.com.visirx.patient.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.DashBoardActivity;
import com.visirx.patient.activity.DoctorProfileActivity;
import com.visirx.patient.common.provider.CareteamToServerProvider;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.fragment.RecyclerViewHolder_AddDoctors;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.VTConstants;
import java.util.List;

/**
 * Created by Suresh on 17-02-2016.
 */
public class RecyclerAdapter_AddDoctors extends RecyclerView.Adapter<RecyclerViewHolder_AddDoctors> {

    List<FindDoctorModel> findDoctorModelslist;
    Context context;
    LayoutInflater inflater;
    SharedPreferences sharedPreferences;
    String Doc_Id, Doc_Firstname, Doc_Lastname, Doc_specialist, Doc_Description, Doc_Fees;
    MediumFont doctorName;
    NormalFont Dsecription, Specialist, DocotrID, DoctorFees1;
    String dial_Name, dial_doctorID, dial_Dsecription, dail_speciallist, dail_doctorFees;
    Button negativeButton, positiveButton;
    String userId;

    public RecyclerAdapter_AddDoctors(Context context, List<FindDoctorModel> findDoctorModelslist) {
        this.findDoctorModelslist = findDoctorModelslist;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder_AddDoctors onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.adddoctor_list_item, parent, false);
        RecyclerViewHolder_AddDoctors viewHolder = new RecyclerViewHolder_AddDoctors(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder_AddDoctors holder, final int position) {
        if (findDoctorModelslist != null) {
            if (findDoctorModelslist.size() > 0) {
                if (findDoctorModelslist.get(position).getDoctorId() != null) {
                    Doc_Id = findDoctorModelslist.get(position).getDoctorId();
                } else {
                    Doc_Id = "-";
                }
                if (findDoctorModelslist.get(position).getDoctorFirstName() != null) {
                    Doc_Firstname = findDoctorModelslist.get(position).getDoctorFirstName();
                } else {
                    Doc_Firstname = "-";
                }
                if (findDoctorModelslist.get(position).getDoctorLastName() != null) {
                    Doc_Lastname = findDoctorModelslist.get(position).getDoctorLastName();
                } else {
                    Doc_Lastname = "-";
                }
                if (findDoctorModelslist.get(position).getDoctorDescription() != null) {
                    Doc_Description = findDoctorModelslist.get(position).getDoctorDescription();
                } else {
                    Doc_Description = "-";
                }
                if (findDoctorModelslist.get(position).getDoctorSpecialization() != null) {
                    Doc_specialist = findDoctorModelslist.get(position).getDoctorSpecialization();
                } else {
                    Doc_specialist = "-";
                }
                if (String.valueOf(findDoctorModelslist.get(position).getDoctorfee()) != null) {
                    Doc_Fees = String.valueOf(findDoctorModelslist.get(position).getDoctorfee());
                } else {
                    Doc_Fees = "-";
                }
                holder.name.setText(Doc_Firstname + " " + Doc_Lastname);
                holder.name.setTag(Doc_Firstname + " " + Doc_Lastname);
                holder.Dsecription.setText(Doc_Description);
                holder.Dsecription.setTag(Doc_Description);
                holder.speciallist.setText(Doc_specialist);
                holder.speciallist.setTag(Doc_specialist);
                holder.visirxID.setText(Doc_Id);
                holder.doctorFees.setText("₹ " + Doc_Fees);
                holder.doctorFees.setTag("₹ " + Doc_Fees);
                holder.addbtn.setTag(Doc_Id);

                sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                String userId = sharedPreferences.getString(VTConstants.USER_ID, null);
                boolean isDoctorAdded = VisirxApplication.customerDAO.checkDocttor(findDoctorModelslist.get(position).getDoctorId(), userId);

                Log.d("SPIN", findDoctorModelslist.get(position).getDoctorId() + " : " + isDoctorAdded);

                if (isDoctorAdded) {

                    Log.d("SPIN", "----> Status for doctor id : " + findDoctorModelslist.get(position).getDoctorId() + " : true");
                    System.out.println("CheckDoctor 1");
                    holder.addbtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.buttonshapediasble));
                    holder.addbtn.setEnabled(false);
                    holder.addbtn.setText("ADDED");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("ADDED","DOCTOR_ID: "+holder.doctorFees.getTag().toString());

                            Intent profile = new Intent(context, DoctorProfileActivity.class);
                            profile.putExtra(VTConstants.DOCTOR_ID, holder.addbtn.getTag().toString());
                            context.startActivity(profile);

                        }
                    });

                }
                else
                {
                    Log.d("SPIN", "----> Status for doctor id : " + findDoctorModelslist.get(position).getDoctorId() + " : false");
                    System.out.println("CheckDoctor 1");
                    holder.addbtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.buttonshape));
                    holder.addbtn.setEnabled(true);
                    holder.addbtn.setText("ADD");

                }

            } else {
                holder.addbtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.buttonshape));
                holder.addbtn.setText("ADD");
                holder.addbtn.setEnabled(true);
            }
            holder.addbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferences = context.getSharedPreferences(VTConstants.PROFILR_PREFERENCE, 0);
                    dial_doctorID = holder.addbtn.getTag().toString();
                    dial_Name = holder.name.getTag().toString();
                    dial_Dsecription = holder.Dsecription.getTag().toString();
                    dail_speciallist = holder.speciallist.getTag().toString();
                    dail_doctorFees = holder.doctorFees.getTag().toString();
                    addDoctor(dial_doctorID, position, holder.addbtn);


                }
            });
            String requestPart = VTConstants.GET_IMAGE_SERVLET + "?"
                    + VTConstants.GET_IMAGE_USERID + "=" + Doc_Id + "&"
                    + VTConstants.GET_IMAGE_TYPE + "="
                    + VTConstants.THUMBNAIL_FLAG;

            Picasso.with(context).load(HTTPUtils.gURLBase + requestPart).placeholder(R.drawable.default_image).into(holder.imageView);
        }
    }


    private void addDoctor(final String Doctor_ID, final int position, final Button addbtn) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.adddoctor_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        negativeButton = (Button) promptsView.findViewById(R.id.cancel_no);
        positiveButton = (Button) promptsView.findViewById(R.id.cancel_yes);
        doctorName = (MediumFont) promptsView.findViewById(R.id.Doctorname);
        Dsecription = (NormalFont) promptsView.findViewById(R.id.Dsecription);
        Specialist = (NormalFont) promptsView.findViewById(R.id.Specialist);
        DocotrID = (NormalFont) promptsView.findViewById(R.id.DocotrID);
        DoctorFees1 = (NormalFont) promptsView.findViewById(R.id.DoctorFees);
        DocotrID.setText(dial_doctorID);
        doctorName.setText(dial_Name);
        Dsecription.setText(dial_Dsecription);
        Specialist.setText(dail_speciallist);
        DoctorFees1.setText(dail_doctorFees);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addbtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.buttonshapediasble));
                addbtn.setEnabled(false);
                addbtn.setText("ADDED");
                CareteamToServerProvider careteamToServerProvider = new CareteamToServerProvider(context);
                careteamToServerProvider.CareteamToServerReq(Doctor_ID);

                System.out.println("ADDEDOCTORLIST DOCTOR ID :  "+dial_doctorID+  "  DOCTOR NAME :  "+dial_Name);
                // Fabric Added DoctorsList
                EventChecking.addedDoctor(dial_doctorID,dial_Name,context);

                Logger.w("ReyclerADapter_AddDoc", "ListSize     " + findDoctorModelslist.size());
                Logger.w("ReyclerADapter_AddDoc", "getDoctorFirstName     " + findDoctorModelslist.get(0).getDoctorFirstName());
                Logger.w("ReyclerADapter_AddDoc", "getDoctorId     " + findDoctorModelslist.get(0).getDoctorId());
                VisirxApplication.customerDAO.insertCustomerDetails(
                        findDoctorModelslist.get(position));
                Intent intent = new Intent(VTConstants.FIND_DOCTOR_BRODCAST);
                context.sendBroadcast(intent);
                Toast.makeText(context, " Doctor Added Successfully ", Toast.LENGTH_LONG).show();
                Intent dashboard = new Intent(context, DashBoardActivity.class);
                dashboard.putExtra(VTConstants.TAB_POSITION, 1);
                context.startActivity(dashboard);

                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }


    AdapterView.OnClickListener clickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewHolder_AddDoctors vholder = (RecyclerViewHolder_AddDoctors) v.getTag();
            int position = vholder.getPosition();


        }
    };

    @Override
    public int getItemCount() {
        return findDoctorModelslist.size();
//        return 1;
    }


}
