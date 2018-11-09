package com.visirx.patient.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.visirx.patient.R;

/**
 * Created by aa on 19.1.16.
 */
public class Popup {


    public static void ShowMessage(Context context, String msg, int duration) {
        try {
            TextView textView = new TextView(context);
            textView.setBackgroundColor(Color.LTGRAY);
            textView.setTextColor(Color.BLACK);
            textView.setPadding(10, 10, 10, 10);
            textView.setText(msg);
            Toast toastView = new Toast(context);
            toastView.setView(textView);
            toastView.setDuration(duration);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void ShowSuccessMessage(Context context, int msg, int duration) {
        try {
            String strMsg = context.getResources().getString(msg);
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.toast_layout_success, null);
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(strMsg);
            Toast toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(duration);
            toast.setView(layout);
            toast.show();
        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void ShowErrorMessage(Context context, int msg, int duration) {
        try {
            String strMsg = context.getResources().getString(msg);
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.toast_layout_error, null);
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(strMsg);
            Toast toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(duration);
            toast.setView(layout);
            toast.show();
        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void ShowErrorMessageString(Context context, String strMsg, int duration) {
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.toast_layout_error, null);
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(strMsg);
            Toast toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(duration);
            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void ShowErrorMessageWithString(Context context, int msg, int duration, String name) {
        try {
            String strMsg = context.getResources().getString(msg);
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.toast_layout_error, null);
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(strMsg + name);
            Toast toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(duration);
            toast.setView(layout);
            toast.show();
        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
