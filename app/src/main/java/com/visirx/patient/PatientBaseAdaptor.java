package com.visirx.patient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.visirx.patient.fragment.NotesFragment;
import com.visirx.patient.fragment.PatientProfileFragment;
import com.visirx.patient.fragment.PrescriptionFragment;

/**
 * Created by suresh on 2/1/2016.
 */
public class PatientBaseAdaptor extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    CharSequence Titles[] = {"PATIENTS", "EMR ", "NOTES", "PRESCRIPTION"};

    public PatientBaseAdaptor(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PatientProfileFragment t1 = new PatientProfileFragment();
                return t1;
            case 1:
//                EMRFragment t2 = new EMRFragment();
//                return t2;
            case 2:
                NotesFragment t3 = new NotesFragment();
                return t3;
            case 3:
                PrescriptionFragment t4 = new PrescriptionFragment();
                return t4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "" + Titles[position];
    }
}
