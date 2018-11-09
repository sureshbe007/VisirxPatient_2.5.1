package com.visirx.patient.com.visirx.patient.adapter;

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
    CharSequence Titles[] = {"PATIENT", "EHD", "NOTES", "PRESCRIPTION"};
    int reservationNumber = -1;
    int aptHis = 1;

    public PatientBaseAdaptor(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                PatientProfileFragment tab1 = new PatientProfileFragment();
//                return tab1;
                return PatientProfileFragment.newInstance(reservationNumber, aptHis);
            case 1:
//                EMRFragment tab2 = new EMRFragment();
//                return EMRFragment.newInstance(reservationNumber);
            case 2:
//                NotesFragment tab3 = new NotesFragment();
                return NotesFragment.newInstance(reservationNumber);
            case 3:
//                PrescriptionFragment tab4 = new PrescriptionFragment();
                return PrescriptionFragment.newInstance(reservationNumber);
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
