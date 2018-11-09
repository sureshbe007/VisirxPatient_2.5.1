package com.visirx.patient.com.visirx.patient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.visirx.patient.R;

/**
 * Created by aa on 11.2.16.
 */
public class BookAppmntAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] time;

    public BookAppmntAdapter(Context c, String[] time) {
        mContext = c;
        this.time = time;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return time.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.book_appmnt_list, null);
            TextView tvtitle = (TextView) grid.findViewById(R.id.lbltime);
            tvtitle.setText(time[position]);

        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}