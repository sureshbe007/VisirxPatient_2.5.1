package com.visirx.patient.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.AppointmentNotesListProvider;
import com.visirx.patient.common.provider.AppointmentNotesProvider;
import com.visirx.patient.customview.MediumFont;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.AppointmentNoteModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotesFragment extends Fragment {

    String TAG = NotesFragment.class.getName();
    ListView listView = null;
    ArrayList<AppointmentNoteModel> notesList = null;
    AppAdapter adapter = null;
    //String patientId = null;
    AppointmentPatientModel appointmentPatientModel = null;
    TextView textView = null;
    ProgressBar progressBar = null;
    SimpleDateFormat inputFormatter, outputFormatter;
    Date date = null;
    String createdById = "";
    SharedPreferences loggedPreferance;

    //Ramesh Modification
    EditText docNotes;
    Button senButton;
    Typeface typeface;
    NormalFont appId;
    NormalFont appDate;

    public static NotesFragment newInstance(int reservationNumber) {

        NotesFragment fragment = new NotesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(VTConstants.APPTMODEL_KEY, reservationNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    public NotesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int reservationNumber = getArguments().getInt(VTConstants.APPTMODEL_KEY, -1);
        appointmentPatientModel = VisirxApplication.aptDAO.GetAppointmentsByID(reservationNumber);
        getActivity().registerReceiver(receiverUpdate, new IntentFilter(VTConstants.NOTIFICATION_APPTS_NOTES));

        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {

            initView();
        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private void initView() {
        try {


            inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            outputFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

            appDate = (NormalFont) getActivity().findViewById(R.id.notesappDate);
            appId = (NormalFont) getActivity().findViewById(R.id.notesappId);

            appId.setText("APPOINTMENT ID :" + appointmentPatientModel.getReservationNumber());
            appDate.setText("  " + DateFormat.GetFormattedDateStr(appointmentPatientModel.getDate()));

            docNotes = (EditText) getActivity().findViewById(R.id.messageEdit);
            senButton = (Button) getActivity().findViewById(R.id.chatSendButton);

            loggedPreferance = getActivity().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
            createdById = loggedPreferance.getString(VTConstants.USER_ID, "Not set");

            //rony 1.3.5 starts
            notesList = VisirxApplication.aptNotesDAO.GetPatientNotes(createdById, appointmentPatientModel.getReservationNumber());

            GetNotesFromServerForUser(createdById, getActivity(), appointmentPatientModel.getReservationNumber());
            //rony 1.3.5 ends
            adapter = new AppAdapter();
            listView = (ListView) getActivity().findViewById(R.id.messagesContainer);

            listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); //ramesh
            listView.setAdapter(adapter);
            //ramesh
            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(adapter.getCount() - 1);
                }
            });
            //ramesh
            adapter.notifyDataSetChanged();

            if (VTConstants.PROGRESSSTATUS_NOTES == 1) {
//          progressBar.setVisibility(View.VISIBLE);
//          textView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            } else {

//          progressBar.setVisibility(View.GONE);
                if (adapter.getCount() == 0) {
//             textView.setVisibility(View.VISIBLE);
                } else {
//             textView.setVisibility(View.GONE);
                }
            }
            if (VTConstants.HISTORYITEMCLICKED == 1) {
                senButton.setVisibility(View.GONE);
                docNotes.setVisibility(View.GONE);
            } else {
                senButton.setVisibility(View.VISIBLE);
                docNotes.setVisibility(View.VISIBLE);
            }

            senButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //rony 1.3.5 starts
                    // Toast.makeText(getActivity(), "Send Messge", Toast.LENGTH_LONG).show();
                    //rony 1.3.5 ends
                    sendMessage();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    private void GetNotesFromServerForUser(String createdById, Context context, int appointmentId) {

        ArrayList<String> notesMaxdateList = VisirxApplication.aptNotesDAO
                .getMaxDate(createdById,
                        appointmentId);
        String notesMaxDate = "";
        if (notesMaxdateList.size() == 0) {
            notesMaxDate = null;
        } else {
            notesMaxDate = notesMaxdateList.get(0);
        }

        if (VTConstants.checkAvailability(getActivity())) {
            AppointmentNotesListProvider provider = new AppointmentNotesListProvider(context);
            provider.SendApptsReq(appointmentId,
                    createdById, notesMaxDate);
        }

    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return notesList.size();
        }

        @Override
        public AppointmentNoteModel getItem(int position) {
            return notesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.notes_list_item, null);
                new ViewHolder(convertView);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            AppointmentNoteModel model = getItem(position);
//       holder.txtDate.setText(" " +DateFormat.GetFormattedDateTimeStr(model.getCreatedAt()));
//       holder.txtName.setText(model.getCreatedByName());
            try {
                date = inputFormatter.parse(model.getCreatedAt());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            try {

                if (createdById.equalsIgnoreCase(model.getCreatedById()))
                {
                    holder.statusTick.setVisibility(View.VISIBLE);
                    if (model.getProcessFlag() == 0) {
                        holder.statusTick.setImageResource(R.drawable.ic_done);
                    } else {
                        holder.statusTick.setImageResource(R.drawable.ic_done_all);
                    }
                    holder.txtTime.setText(DateFormat.GetFormattedDateTimeStr(model.getCreatedAt()) + " " + DateFormat.GetFormattedTimeStr(outputFormatter.format(date)));
                    holder.txtName.setText(model.getCreatedByName());
                    holder.txtNotes.setText(model.getNotes());

                    holder.contentWithBackground.setBackgroundResource(R.drawable.msg_green1);

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBackground.getLayoutParams();
                    layoutParams.gravity = Gravity.RIGHT;
                    holder.contentWithBackground.setLayoutParams(layoutParams);

                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    holder.content.setLayoutParams(lp);

                    layoutParams = (LinearLayout.LayoutParams) holder.txtName.getLayoutParams();
                    layoutParams.gravity = Gravity.RIGHT;
                    holder.txtName.setLayoutParams(layoutParams);

                    layoutParams = (LinearLayout.LayoutParams) holder.txtNotes.getLayoutParams();
                    layoutParams.gravity = Gravity.RIGHT;
                    holder.txtNotes.setLayoutParams(layoutParams);

                    layoutParams = (LinearLayout.LayoutParams) holder.txtTime.getLayoutParams();
                    layoutParams.gravity = Gravity.RIGHT;
                    holder.txtTime.setLayoutParams(layoutParams);
                } else {
                    holder.statusTick.setVisibility(View.GONE);
                    holder.txtName.setText(model.getCreatedByName());
                    holder.txtNotes.setText(model.getNotes());
                    holder.txtTime.setText(DateFormat.GetFormattedDateTimeStr(model.getCreatedAt()) + "  " + DateFormat.GetFormattedTimeStr(outputFormatter.format(date)));

                    holder.contentWithBackground.setBackgroundResource(R.drawable.msg_white);

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBackground.getLayoutParams();
                    layoutParams.gravity = Gravity.LEFT;
                    holder.contentWithBackground.setLayoutParams(layoutParams);

                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
                    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    holder.content.setLayoutParams(lp);

                    layoutParams = (LinearLayout.LayoutParams) holder.txtName.getLayoutParams();
                    layoutParams.gravity = Gravity.LEFT;
                    holder.txtName.setLayoutParams(layoutParams);

                    layoutParams = (LinearLayout.LayoutParams) holder.txtNotes.getLayoutParams();
                    layoutParams.gravity = Gravity.LEFT;
                    holder.txtNotes.setLayoutParams(layoutParams);


                    layoutParams = (LinearLayout.LayoutParams) holder.txtTime.getLayoutParams();
                    layoutParams.gravity = Gravity.LEFT;
                    holder.txtTime.setLayoutParams(layoutParams);

                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            MediumFont txtName;
            NormalFont txtNotes;
            NormalFont txtTime;
            LinearLayout contentWithBackground;
            LinearLayout content;
            ImageView statusTick;

            public ViewHolder(View view) {
//          txtDate = (RobotoRegular)view.findViewById(R.id.date_text);
                txtName = (MediumFont) view.findViewById(R.id.name);
                txtNotes = (NormalFont) view.findViewById(R.id.notes);
                txtTime = (NormalFont) view.findViewById(R.id.time);
                statusTick = (ImageView) view.findViewById(R.id.statusTick);
                contentWithBackground = (LinearLayout) view.findViewById(R.id.contentWithBackground);
                content = (LinearLayout) view.findViewById(R.id.content);
                view.setTag(this);
            }
        }
    }

    void sendMessage() {


        if (docNotes.getText() == null || docNotes.getText().toString().trim().length() <= 0 || docNotes.getText().equals("")) {
            Popup.ShowErrorMessageString(getActivity(), "Enter the notes", Toast.LENGTH_SHORT);
            return;
        }
//        appointmentPatientModel = VisirxApplication.aptDAO.GetAppointmentsByID(appointmentPatientModel.getReservationNumber());
        String name = appointmentPatientModel.getFirstName() + " " + appointmentPatientModel.getLastName();
        SharedPreferences loggedPreferance;
        loggedPreferance = getActivity().getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        AppointmentNoteModel model = new AppointmentNoteModel();

        //rony 1.3.5 starts
        model.setPatientId(loggedPreferance.getString(VTConstants.USER_ID, null));

        model.setAppointmentId(appointmentPatientModel.getReservationNumber());
        model.setNotes(docNotes.getText().toString());

        String createdAt = DateFormat.getDateStr(new Date());
        model.setCreatedAt(createdAt);
        model.setCreatedAtServer(createdAt);

        model.setCreatedById(loggedPreferance.getString(VTConstants.USER_ID, null));
        model.setCreatedByName(loggedPreferance.getString(VTConstants.LOGGED_USER_FULLNAME, null));

        model.setPatientName(loggedPreferance.getString(VTConstants.LOGGED_USER_FULLNAME, null));
//rony 1.3.5 ends
        long flag = VisirxApplication.aptNotesDAO.insertAppointmentNotes(model, VTConstants.PROCESSED_FLAG_NOT_SENT);
        if (flag > 0) {
            AppointmentNotesProvider notesProvider = new AppointmentNotesProvider(getActivity());
            notesProvider.SendApptsReq();

//            Popup.ShowSuccessMessage(getActivity(), R.string.notes_saved_sucessfully, Toast.LENGTH_SHORT);
//       finish();

            Intent intent = new Intent(VTConstants.NOTIFICATION_APPTS_NOTES);
            getContext().sendBroadcast(intent);
        }
        docNotes.setText("");
        if (VTConstants.checkAvailability(getActivity())) {
            snackBarHide();
        } else {
            snackBarShow();
        }

    }

    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
//          progressBar.setVisibility(View.GONE);
                LogTrace.i(TAG, "Appts Notes download broadcast received...");
                //rony 1.3.5 starts
                notesList = VisirxApplication.aptNotesDAO.GetPatientNotes(createdById, appointmentPatientModel.getReservationNumber());
                //rony 1.3.5 ends
                if (notesList.size() > 0) {
                    adapter.notifyDataSetChanged();
//             textView.setVisibility(View.GONE);
                } else {
//             textView.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }
    };

    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(receiverUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void snackBarShow() {
        SnackbarManager.show(
                Snackbar.with(getActivity())
                        .text("No Internet connection...")
                        .actionLabel("Close")
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)//
                        .color(Color.BLACK)
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                Log.d(TAG, "Undoing something");

                            }
                        })
                , getActivity());

    }

    private void snackBarHide() {
        SnackbarManager.dismiss();
    }
}