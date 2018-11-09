package com.visirx.patient.com.visirx.patient.adapter;

import android.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.PatientDetailsActivity;
import com.visirx.patient.common.provider.CancelAppointmentProvider;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.fragment.AllappointmentFragment;
import com.visirx.patient.fragment.RecyclerViewHolder_Allappointment;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.Logger;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Suresh on 09-02-2016.
 */
public class RecyclerAdapter_Allappointment extends RecyclerView.Adapter<RecyclerViewHolder_Allappointment> {

    Context context;
    LayoutInflater inflater;
    List<FindDoctorModel> findDoctorModelList;
    List<AppointmentPatientModel> Allappointment;
    String DoctorID;
    int AppointmentId;
    Button negativeButton, positiveButton;
    String all_DoctorName, all_Description, all_Specialist, all_visiRxID, all_appointmentID, all_Date, all_time;
    NormalFont Dsecription, Specialist, appointmentid, date, time;
    MediumFont Doctorname;

    public RecyclerAdapter_Allappointment(Context context, List<AppointmentPatientModel> Allappointment) {
        this.context = context;
        this.Allappointment = Allappointment;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder_Allappointment onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.allappointment_list_item, parent, false);
        RecyclerViewHolder_Allappointment viewHolder = new RecyclerViewHolder_Allappointment(v);
//        v.setOnClickListener(clickListener);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder_Allappointment holder, int position) {
        AppointmentId = Allappointment.get(position).getReservationNumber();
        AppointmentPatientModel model = Allappointment.get(position);
//        EventChecking.myTotalappointments(model.getPerfomerid(), model.getReservationNumber(), model.getStatus(), context);
        findDoctorModelList = VisirxApplication.customerDAO.Getcustomer(Allappointment.get(position).getPerfomerid());
        if (findDoctorModelList != null) {
            if (findDoctorModelList.size() > 0) {
                holder.appoint_name.setText(findDoctorModelList.get(0).getDoctorFirstName() + "  " + findDoctorModelList.get(0).getDoctorLastName());
                holder.appoint_name.setTag(findDoctorModelList.get(0).getDoctorFirstName() + "  " + findDoctorModelList.get(0).getDoctorLastName());
                holder.Dsecription.setText(findDoctorModelList.get(0).getDoctorDescription());
                holder.Dsecription.setTag(findDoctorModelList.get(0).getDoctorDescription());
                holder.Specialist.setText(findDoctorModelList.get(0).getDoctorSpecialization());
                holder.Specialist.setTag(findDoctorModelList.get(0).getDoctorSpecialization());
                // for alertBox
                all_DoctorName = findDoctorModelList.get(0).getDoctorFirstName() + "  " + findDoctorModelList.get(0).getDoctorLastName();
                all_Description = findDoctorModelList.get(0).getDoctorDescription();
                all_Specialist = findDoctorModelList.get(0).getDoctorSpecialization();

                String thumbnail_photo = findDoctorModelList.get(0).getCustomerImageThumbnailPath();
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

        holder.DocotrID.setText(model.getPerfomerid());
        holder.appointID.setText(String.valueOf(AppointmentId));
        holder.AppointDate.setText(model.getDate());
        // SetTag

        holder.DocotrID.setTag(model.getPerfomerid());
        holder.appointID.setTag(String.valueOf(AppointmentId));
        holder.AppointDate.setTag(model.getDate());


        if (Allappointment.get(position).getTime().contains("T")) {

            holder.AppointHead.setText("Token:");
            holder.AppointTime.setText(Allappointment.get(position).getTime());
            holder.AppointTime.setTag(Allappointment.get(position).getTime());
        } else {

            holder.AppointHead.setText("Time:");
            holder.AppointTime.setText(Allappointment.get(position).getTime());
            holder.AppointTime.setTag(Allappointment.get(position).getTime());
        }
        holder.Appointstatus.setText("  " + model.getStatus());
        if (model.getAppointmentType().equalsIgnoreCase("V")) {
            holder.Appoint_type.setText("Virtual");
            holder.Appoint_type.setTextColor(Color.parseColor("#369575"));
        } else {
            holder.Appoint_type.setText("Walkin");
            holder.Appoint_type.setTextColor(Color.parseColor("#369575"));
        }
        holder.cancelappoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // GetTag
                all_DoctorName = holder.appoint_name.getTag().toString();
                all_Description = holder.Dsecription.getTag().toString();
                all_Specialist = holder.Specialist.getTag().toString();
                all_appointmentID = holder.appointID.getTag().toString();
                all_visiRxID = holder.DocotrID.getTag().toString();
                all_Date = holder.AppointDate.getTag().toString();
                all_time = holder.AppointTime.getTag().toString();
                ShowCancelDialog(holder.appointID.getTag().toString(), holder.appointID.getTag().toString());

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(context, PatientDetailsActivity.class);
                profile.putExtra(VTConstants.APPOINTMENT_ID, holder.appointID.getTag().toString());
                context.startActivity(profile);

            }
        });
    }

    @Override
    public int getItemCount() {
        return Allappointment.size();
    }

    private void ShowCancelDialog(String doctorID, final String appointmentId) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.cancel_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        Doctorname = (MediumFont) promptsView.findViewById(R.id.Doctorname);
        Dsecription = (NormalFont) promptsView.findViewById(R.id.Dsecription);
        Specialist = (NormalFont) promptsView.findViewById(R.id.Specialist);
        appointmentid = (NormalFont) promptsView.findViewById(R.id.appointmentid);
        date = (NormalFont) promptsView.findViewById(R.id.date);
        time = (NormalFont) promptsView.findViewById(R.id.time);


        // Set Values
        Doctorname.setText(all_DoctorName);
        Dsecription.setText(all_Description);
        Specialist.setText(all_Specialist);
        appointmentid.setText(all_appointmentID);
        date.setText(all_Date);

        if (all_time.contains("T")) {

            Logger.w("All_Appointmant", "Toekn" + all_time);
            time.setText("Toekn:" + all_time);
        } else {
            Logger.w("All_Appointmant", "atime" + all_time);
            time.setText("Time:" + all_time);
        }

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
                Intent intent = new Intent(context, AllappointmentFragment.class);
//                intent.putExtra(VTConstants.DOCTOR_ID, holder.tv5.getTag().toString());
                int appointID = Integer.parseInt(appointmentId);
                CancelAppointmentProvider cancelAppointmentProvider = new CancelAppointmentProvider(context);
                cancelAppointmentProvider.CancelAppointmentReq(appointID);
                Toast.makeText(context, "Your Appointment canceled Successfully", Toast.LENGTH_LONG).show();
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }


}
