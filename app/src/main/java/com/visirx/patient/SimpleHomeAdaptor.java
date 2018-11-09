package com.visirx.patient;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by aa on 21.1.16.
 */
public class SimpleHomeAdaptor extends SimpleAdapter {


    private int mLastClicked = -1;

    public SimpleHomeAdaptor(Context context,
                             List<? extends Map<String, ?>> data, int resource, String[] from,
                             int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        return view;
    }

    @Override
    public boolean areAllItemsEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        if (mLastClicked == 1 && position == mLastClicked) {
            return false;
        }
        return true;
    }

    public void setLastClicked(int lastClicked) {
        mLastClicked = lastClicked;
    }
}
