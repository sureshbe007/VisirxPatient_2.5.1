package com.visirx.patient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.visirx.patient.fragment.AllappointmentFragment;
import com.visirx.patient.fragment.AlldoctorsFragment;
import com.visirx.patient.fragment.CancelappointmentFragment;


/**
 * Created by suresh on 2/2/2016.
 */
public class AppointBaseAdaptor extends FragmentStatePagerAdapter {

    int mNumOfTabs;
//    CharSequence BookAptTitles[] = {"APPOINTMENTS ", "DOCTORS", "HISTORY"};
    CharSequence BookAptTitles[] = {"MY DOCTORS ", "MY APPOINTMENTS", "HISTORY"};

    public AppointBaseAdaptor(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                AllappointmentFragment tab1 = new AllappointmentFragment();
                AlldoctorsFragment tab1 = new AlldoctorsFragment();
                return tab1;
            case 1:

                AllappointmentFragment tab2 = new AllappointmentFragment();
//                AlldoctorsFragment tab2 = new AlldoctorsFragment();
                return tab2;
            case 2:
                CancelappointmentFragment tab3 = new CancelappointmentFragment();
                return tab3;
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
