package com.visirx.patient.visirxav.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCConfig;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.visirx.patient.R;
import com.visirx.patient.visirxav.activities.CallActivity;
import com.visirx.patient.visirxav.adapters.OpponentsAdapter;
import com.visirx.patient.visirxav.util.DialogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QuickBlox team
 */
public class OpponentsFragment extends Fragment implements View.OnClickListener, Serializable {

    private static final String TAG = OpponentsFragment.class.getSimpleName();
    private OpponentsAdapter opponentsAdapter;
    public static String login;
    private Button btnAudioCall;
    private Button btnVideoCall;
    private View view=null;
    private ProgressDialog progresDialog;
    private ListView opponentsList;

    public static OpponentsFragment getInstance() {
        return new OpponentsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        view = inflater.inflate(R.layout.fragment_opponents, container, false);

        if (savedInstanceState == null) {
            initUI(view);

            // Show dialog till opponents loading
            progresDialog = new ProgressDialog(getActivity()) {
                @Override
                public void onBackPressed() {
                    DialogUtil.showToast(getActivity(), "Wait until loading finish");
                }
            };
            progresDialog.setMessage("Load opponents ...");
            progresDialog.setCanceledOnTouchOutside(false);
            progresDialog.show();

            initOpponentListAdapter();
        }
        return view;
    }

    private void initOpponentListAdapter() {
        final ListView opponentsListView = (ListView) view.findViewById(R.id.opponentsList);

        List<QBUser> opponentsList = ((CallActivity) getActivity()).getOpponentsList();
        List<QBUser> userList = new ArrayList<>();
        if (opponentsList != null) {
            userList.addAll(opponentsList);
        }
        prepareUserList(opponentsListView, userList);
        progresDialog.dismiss();


    }

    private void prepareUserList(ListView opponentsList, List<QBUser> users) {
        int i = searchIndexLogginedUser(users);
        if (i >= 0)
            users.remove(i);

        // Prepare users list for simple adapter.
        //
        opponentsAdapter = new OpponentsAdapter(getActivity(), users);
        opponentsList.setAdapter(opponentsAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreate() from OpponentsFragment");
        super.onCreate(savedInstanceState);
    }

    private void initUI(View view) {

        ((CallActivity)getActivity()).initActionBar();
        login = getActivity().getIntent().getStringExtra("login");

        btnAudioCall = (Button)view.findViewById(R.id.btnAudioCall);
        btnVideoCall = (Button)view.findViewById(R.id.btnVideoCall);

        btnAudioCall.setOnClickListener(this);
        btnVideoCall.setOnClickListener(this);

        opponentsList = (ListView) view.findViewById(R.id.opponentsList);
    }

    @Override
    public void onClick(View v) {

        if (opponentsAdapter.getSelected().isEmpty()){
            DialogUtil.showToast(getActivity(), "Choose one opponent");
            return;
        }

        if (opponentsAdapter.getSelected().size() > QBRTCConfig.getMaxOpponentsCount()){
            DialogUtil.showToast(getActivity(), "Max number of opponents is 6");
            return;
        }
            QBRTCTypes.QBConferenceType qbConferenceType = null;

            //Init conference type
            switch (v.getId()) {
                case R.id.btnAudioCall:
                    qbConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
                    break;

                case R.id.btnVideoCall:
                    // get call type
                    qbConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;

                    break;
            }

        Map<String, String> userInfo = new HashMap<>();
            userInfo.put("any_custom_data", "some data");
            userInfo.put("my_avatar_url", "avatar_reference");

            ((CallActivity) getActivity())
                    .addConversationFragmentStartCall(opponentsAdapter.getSelected(),
                            qbConferenceType, userInfo);

    }

    @Override
    public void onPause() {
        super.onPause();
        if(progresDialog.isShowing()) {
            progresDialog.dismiss();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_opponents, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                ((CallActivity)getActivity()).logout();
                return true;
            case R.id.settings:
                ((CallActivity)getActivity()).showSettings();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static int searchIndexLogginedUser(List<QBUser> usersList) {
        int indexLogginedUser = -1;
        for (QBUser usr : usersList) {
            if (usr.getLogin().equals(login)) {
                indexLogginedUser = usersList.indexOf(usr);
                break;
            }
        }
        return indexLogginedUser;
    }
}
