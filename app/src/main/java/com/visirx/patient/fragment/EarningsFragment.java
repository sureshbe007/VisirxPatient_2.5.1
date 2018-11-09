package com.visirx.patient.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.visirx.patient.R;

public class EarningsFragment extends android.app.Fragment {

    //private AppAdapter mAdapter;
    String TAG = EarningsFragment.class.getName();

    static final String[] Months = new String[]{"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};

    TextView Totaptmnts, TotEar, TotOutAmnt, TextName, TextVisrxID;
    Spinner spinMonth, spinYear;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_earnings_fragment, container, false);
        return view;
    }


}
