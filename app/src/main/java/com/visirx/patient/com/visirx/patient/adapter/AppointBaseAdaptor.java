package com.visirx.patient.com.visirx.patient.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.visirx.patient.fragment.AllappointmentFragment;
import com.visirx.patient.fragment.AlldoctorsFragment;

/**
 * Created by suresh on 2/2/2016.
 */
public class AppointBaseAdaptor extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    CharSequence BookAptTitles[] = {"All Doctors ", " Appointments"};

    public AppointBaseAdaptor(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AlldoctorsFragment tab1 = new AlldoctorsFragment();
                return tab1;
            case 1:
                AllappointmentFragment tab2 = new AllappointmentFragment();
                return tab2;
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
        return "" + BookAptTitles[position];
    }
}
