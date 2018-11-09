package com.visirx.patient.utils;

import android.util.Log;

/**
 * Created by Suresh on 04-04-2016.
 */

public class Logger {

    private static final boolean DEBUG = true;
    private static String TAG = "VisiRx_Logger";

    public static void d(String s1, String s2) {
        if (DEBUG) {
            Log.d(s1, s2);
        }
    }

    public static void v(String s1, String s2) {
        if (DEBUG) {
            Log.v(s1, s2);
        }
    }

    public static void i(String s1, String s2) {
        if (DEBUG) {
            Log.i(s1, s2);
        }
    }

    public static void e(String s1, String s2) {
        if (DEBUG) {
            Log.e(s1, s2);
        }
    }

    public static void w(String s1, String s2) {
        if (DEBUG) {
            Log.w(s1, s2);
        }
    }

}
