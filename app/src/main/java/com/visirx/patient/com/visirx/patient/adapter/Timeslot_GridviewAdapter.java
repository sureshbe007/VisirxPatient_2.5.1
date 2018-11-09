package com.visirx.patient.com.visirx.patient.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.visirx.patient.R;

import java.util.ArrayList;


/**
 * Created by Suresh on 20-02-2016.
 */
public class Timeslot_GridviewAdapter extends BaseAdapter {
    private ArrayList<String> timeslot;
    private Activity activity;

    public Timeslot_GridviewAdapter(Activity activity, ArrayList<String> timeslot) {
        super();
        this.timeslot = timeslot;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return timeslot.size();
    }

    @Override
    public Object getItem(int position) {
        return timeslot.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {

        public TextView timeShow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();
        if (convertView == null) {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.gridview_row, null);
            view.timeShow = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        view.timeShow.setText(timeslot.get(position));
        return convertView;

    }
}
