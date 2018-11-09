package com.visirx.patient.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.visirx.patient.R;

public class CalendarFragment extends Fragment {

    CalendarView calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_calendar_fragment, container, false);
        calendar = (CalendarView) v.findViewById(R.id.calendar);
        return v;
    }
}
