package com.visirx.patient.com.visirx.patient.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.activity.BookAppointActivity;
import com.visirx.patient.activity.DashBoardActivity;
import com.visirx.patient.activity.DoctorProfileActivity;
import com.visirx.patient.api.GetTimeslotsForPerformerReq;
import com.visirx.patient.api.GetTimeslotsForPerformerRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.AlldoctorsListProvider;
import com.visirx.patient.common.provider.DeleteDoctorProvider;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.fragment.RecyclerViewHolder_AllDoctors;
import com.visirx.patient.model.FindDoctorModel;
import com.visirx.patient.utils.EventChecking;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Suresh on 09-02-2016.
 */
public class RecyclerAdapter_AllDoctors extends RecyclerView.Adapter<RecyclerViewHolder_AllDoctors> {

    static final String TAG = RecyclerAdapter_AllDoctors.class.getName();

    SharedPreferences sharedPreferences;
    List<FindDoctorModel> findDoctorModelList;
    Context context;
    String CurrentDate, delete_DocID, delete_Name, delete_Description, delete_Spcialist;
    LayoutInflater inflater;
    MediumFont doctorName;
    NormalFont Dsecription, Specialist, DocotrID, DoctorFees1;
    String doc_DoctorName, doc_visiRxID, doc_Description, doc_Specialist;
    Button negativeButton, positiveButton;

    public RecyclerAdapter_AllDoctors(Context context, List<FindDoctorModel> findDoctorModelList) {
        this.context = context;
        this.findDoctorModelList = findDoctorModelList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerViewHolder_AllDoctors onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.alldoctors_list_item, parent, false);
        RecyclerViewHolder_AllDoctors viewHolder = new RecyclerViewHolder_AllDoctors(v);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder_AllDoctors holder, int position) {
        if (findDoctorModelList != null) {

            if (findDoctorModelList.size() > 0) {

                // Fabric All Doctors List

                holder.docor_name.setText(findDoctorModelList.get(position).getDoctorFirstName() + " " + findDoctorModelList.get(position).getDoctorLastName());
                holder.doctor_status.setText(findDoctorModelList.get(position).getDoctorDescription());
                holder.doctor_speciallist.setText(findDoctorModelList.get(position).getDoctorSpecialization());
                holder.doctor_visiRxID.setText(findDoctorModelList.get(position).getDoctorId());
                holder.doctor_visiRxID.setTag(findDoctorModelList.get(position).getDoctorId());
                holder.Doctor_fees.setText("\u20B9 " + String.valueOf(findDoctorModelList.get(position).getDoctorfee()));
                holder.Doctor_fees.setTag(String.valueOf(findDoctorModelList.get(position).getDoctorfee()));
                holder.consultbtn.setTag(findDoctorModelList.get(position).getDoctorId());

                // SetTag for Alert box
                holder.docor_name.setTag(findDoctorModelList.get(position).getDoctorFirstName() + " " + findDoctorModelList.get(position).getDoctorLastName());
                holder.doctor_status.setTag(findDoctorModelList.get(position).getDoctorDescription());
                holder.doctor_speciallist.setTag(findDoctorModelList.get(position).getDoctorSpecialization());
                holder.consultbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(context, BookAppointActivity.class);
                            intent.putExtra(VTConstants.DOCTOR_ID, holder.doctor_visiRxID.getTag().toString());
                            intent.putExtra(VTConstants.DOCTOR_FEES, holder.Doctor_fees.getTag().toString());
                            context.startActivity(intent);
//                    ((Activity)context).finish();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            LogTrace.e(TAG, e.getMessage());
                        }
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profile = new Intent(context, DoctorProfileActivity.class);
                        profile.putExtra(VTConstants.DOCTOR_ID, holder.doctor_visiRxID.getTag().toString());
                        context.startActivity(profile);

                    }


                });
                holder.deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doc_visiRxID = holder.doctor_visiRxID.getTag().toString();
                        doc_DoctorName = holder.docor_name.getTag().toString();
                        doc_Description = holder.doctor_status.getTag().toString();
                        doc_Specialist = holder.doctor_speciallist.getTag().toString();

                        Log.w("CANCEl_ITEM_ID", "doc_visiRxID  " + doc_visiRxID);
                        DeleteDoctor(doc_visiRxID, holder.getAdapterPosition());

                    }
                });

                String thumbnail_photo = findDoctorModelList.get(position).getCustomerImageThumbnailPath();
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
    }


    private void DeleteDoctor(final String docID, final int position) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.deletedoctor_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        negativeButton = (Button) promptsView.findViewById(R.id.cancel_no);
        positiveButton = (Button) promptsView.findViewById(R.id.cancel_yes);
        doctorName = (MediumFont) promptsView.findViewById(R.id.Doctorname);
        Dsecription = (NormalFont) promptsView.findViewById(R.id.Dsecription);
        Specialist = (NormalFont) promptsView.findViewById(R.id.Specialist);
        DocotrID = (NormalFont) promptsView.findViewById(R.id.DocotrID);
        DoctorFees1 = (NormalFont) promptsView.findViewById(R.id.DoctorFees);

        doctorName.setText(doc_DoctorName);
        DocotrID.setText(doc_visiRxID);
        Dsecription.setText(doc_Description);
        Specialist.setText(doc_Specialist);
//        DoctorFees1.setText(dail_doctorFees);
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

                if (VTConstants.checkAvailability(context)) {

                    SnackBarhide();
                    DeleteDoctorProvider deleteDoctorProvider = new DeleteDoctorProvider(context);
                    deleteDoctorProvider.DeleteDoctorRequest(docID);

                    // Fabric for Delete the Doctor
                    EventChecking.deletedDoctors(doc_visiRxID, doc_DoctorName, context);
                    alertDialog.cancel();
                } else {
                    SnackBar();
                }


            }
        });
        alertDialog.show();
    }


    @Override
    public int getItemCount() {
        return findDoctorModelList.size();
    }

    public void SnackBar() {
        SnackbarManager.show(
                Snackbar.with(context) // context
                        .text("No InterNet Connection...") // text to display
                        .actionLabel("Close")
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)//
                        .color(Color.BLACK) // change the background color// action button label
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                Log.d(TAG, "Undoing something");

                            }
                        }) // action button's ActionClickListener
                , (Activity) context);

    }

    public void SnackBarhide() {
        SnackbarManager.dismiss();
    }
}
