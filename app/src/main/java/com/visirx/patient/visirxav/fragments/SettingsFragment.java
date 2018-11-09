package com.visirx.patient.visirxav.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.visirx.patient.R;



/**
 * QuickBlox team
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
