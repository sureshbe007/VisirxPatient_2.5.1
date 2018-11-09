package com.visirx.patient.com.visirx.patient.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.PatientDetailsActivity;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.fragment.RecyclerViewHolder_Cancelappointment;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Suresh on 15-03-2016.
 */
public class RecyclerAdapter_Cancelappointment extends RecyclerView.Adapter<RecyclerViewHolder_Cancelappointment> {

    Context context;
    LayoutInflater inflater;
    List<FindDoctorModel> finddoctorList;
    List<AppointmentPatientModel> cancelaptList;
    AppointmentPatientModel parModel;
    int AppointmentId;
    String PatientID;
    SharedPreferences sharedPreferences;
    MediumFont Doctorname;
    NormalFont Dsecription, Specialist, appointmentid, date, time;
    Button negativeButton, positiveButton;
    String del_DoctorName, del_Description, del_Specialist, del_visiRxID, del_appointmentID, del_Date, del_time;

    public RecyclerAdapter_Cancelappointment(Context context, List<AppointmentPatientModel> cancelaptList) {
        this.context = context;
        this.cancelaptList = cancelaptList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerViewHolder_Cancelappointment onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cancelappointment_list_item, parent, false);
        RecyclerViewHolder_Cancelappointment viewHolder = new RecyclerViewHolder_Cancelappointment(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder_Cancelappointment holder, int position) {
        sharedPreferences = context.getSharedPreferences(
                VTConstants.LOGIN_PREFRENCES_NAME, 0);
        sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        PatientID = sharedPreferences.getString(VTConstants.USER_ID, "null");
        AppointmentId = cancelaptList.get(position).getReservationNumber();
        parModel = cancelaptList.get(position);
        finddoctorList = VisirxApplication.customerDAO.Getcustomer(cancelaptList.get(position).getPerfomerid());
        try {
            if (cancelaptList != null) {

                // Fabric for Canceled Appointment

                if (finddoctorList != null) {

                    holder.name.setText(finddoctorList.get(0).getDoctorFirstName() + " " + finddoctorList.get(0).getDoctorLastName());
                    holder.status.setText(finddoctorList.get(0).getDoctorDescription());
                    holder.speciallist.setText(finddoctorList.get(0).getDoctorSpecialization());
                    holder.DoctorID.setText(finddoctorList.get(0).getDoctorId());
                    holder.appointmentid.setText(String.valueOf(AppointmentId));
                    holder.appointmentid.setTag(String.valueOf(AppointmentId));
                    holder.Date.setText(parModel.getDate());
                    holder.Appoint_type.setText(parModel.getAppointmentType());
                    holder.Appointstatus.setText(parModel.getStatus());

                    // setTag for Long Press
                    holder.name.setTag(finddoctorList.get(0).getDoctorFirstName() + " " + finddoctorList.get(0).getDoctorLastName());
                    holder.DoctorID.setTag(finddoctorList.get(0).getDoctorId());
                    holder.appointmentid.setTag(String.valueOf(AppointmentId));
                    holder.status.setTag(finddoctorList.get(0).getDoctorDescription());
                    holder.speciallist.setTag(finddoctorList.get(0).getDoctorSpecialization());
                    holder.Date.setTag(parModel.getDate());
                    holder.time.setTag(parModel.getTime());

                    if (parModel.getTime().contains("T")) {
                        holder.AppointHead.setText("Token:");
                        holder.time.setText(parModel.getTime());
                    } else {
                        holder.AppointHead.setText("Time:");
                        holder.time.setText(parModel.getTime());
                    }
                    if (parModel.getAppointmentType().equalsIgnoreCase("V")) {
                        holder.Appoint_type.setText("Virtual");
                        holder.Appoint_type.setTextColor(Color.parseColor("#369575"));
                    } else {
                        holder.Appoint_type.setText("Walkin");
                        holder.Appoint_type.setTextColor(Color.parseColor("#369575"));
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent profile = new Intent(context, PatientDetailsActivity.class);
                            profile.putExtra(VTConstants.APPOINTMENT_ID, holder.appointmentid.getTag().toString());
                            context.startActivity(profile);

                        }
                    });
                    //  holder.imageView.setTag(holder);
                    String thumbnail_photo = finddoctorList.get(0).getCustomerImageThumbnailPath();
                    if (thumbnail_photo != null) {
                        //Load the file from sd card stored path - starts
                        File imgFile = new File(thumbnail_photo);
                        Uri profImgThumbUri = Uri.fromFile(imgFile);
                        if (imgFile.exists()) {
                            Bitmap myBitmap = null;
                            try {
                                myBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), profImgThumbUri);
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            holder.imageView.setImageBitmap(myBitmap);
                        } else {
                            Bitmap defaultIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_image);
                            holder.imageView.setImageBitmap(defaultIcon);
                        }

                    } else {
                        Bitmap defaultIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_image);
                        holder.imageView.setImageBitmap(defaultIcon);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                Log.w("CANCEl_ITEM_ID", "  getAdapterPosition      " + holder.getAdapterPosition());
//                Log.w("CANCEl_ITEM_ID", "  name      " + holder.name.getTag().toString());
//                Log.w("CANCEl_ITEM_ID", "  appointmentid " + holder.appointmentid.getTag().toString());
//                Log.w("CANCEl_ITEM_ID", "  DoctorID " + holder.DoctorID.getTag().toString());
//                Log.w("CANCEl_ITEM_ID", "  status " + holder.status.getTag().toString());
//                Log.w("CANCEl_ITEM_ID", "  speciallist " + holder.speciallist.getTag().toString());
//                Log.w("CANCEl_ITEM_ID", "  Date " + holder.Date.getTag().toString());
//                Log.w("CANCEl_ITEM_ID", "  time " + holder.time.getTag().toString());
//
//                // getTag values for alert box
//                del_DoctorName = holder.name.getTag().toString();
//                del_visiRxID = holder.DoctorID.getTag().toString();
//                del_Description = holder.status.getTag().toString();
//                del_Specialist = holder.speciallist.getTag().toString();
//                del_appointmentID = holder.appointmentid.getTag().toString();
//                del_Date = holder.Date.getTag().toString();
//                del_time = holder.time.getTag().toString();
//
//                deleteDailog(holder.getAdapterPosition());
//
//                return false;
//            }
//        });

    }

    private void deleteDailog(final int position) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.deleteappointment_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        Doctorname = (MediumFont) promptsView.findViewById(R.id.Doctorname);
        Dsecription = (NormalFont) promptsView.findViewById(R.id.Dsecription);
        Specialist = (NormalFont) promptsView.findViewById(R.id.Specialist);
        appointmentid = (NormalFont) promptsView.findViewById(R.id.appointmentid);
        date = (NormalFont) promptsView.findViewById(R.id.date);
        time = (NormalFont) promptsView.findViewById(R.id.time);

        // Set Values in Dialog
        Doctorname.setText(del_DoctorName);
        Dsecription.setText(del_Description);
        Specialist.setText(del_Specialist);
        appointmentid.setText(del_appointmentID);
        date.setText(del_Date);
        time.setText(del_time);

        negativeButton = (Button) promptsView.findViewById(R.id.cancel_no);
        positiveButton = (Button) promptsView.findViewById(R.id.cancel_yes);
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
//                delete(del_visiRxID,PatientID, del_appointmentID, position);
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }


    public void delete(String doctorID, String PatientID,String appointID, int position)
    {
    //removes the row
        Log.w("CANCEl_ITEM_ID", " POSITIVE doctorName   "+ doctorID);
        Log.w("CANCEl_ITEM_ID", " POSITIVE appointID   " + PatientID);
        Log.w("CANCEl_ITEM_ID", " POSITIVE position    " + appointID);
        Log.w("CANCEl_ITEM_ID", " POSITIVE position    " + position);
        VisirxApplication.aptDAO.deleteappointment(doctorID,PatientID, appointID);

        notifyItemRemoved(position);
    }



    @Override
    public int getItemCount() {
        return cancelaptList.size();
    }
}
