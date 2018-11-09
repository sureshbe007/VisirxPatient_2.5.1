package com.visirx.patient.common;

import android.util.Log;

/**
 * Created by aa on 18.1.16.
 */
public class LogTrace {


    public static final int NONE = 0;
    public static final int ERRORS_ONLY = 1;
    public static final int ERRORS_WARNINGS = 2;
    public static final int ERRORS_WARNINGS_INFO = 3;
    public static final int ERRORS_WARNINGS_INFO_DEBUG = 4;

    public static final int LOGGING_LEVEL = 4;        // Errors + warnings + info + debug (default)

    public static final String TAG = "VisirxApp";

    public static void e(String tag, String msg) {
        if (LOGGING_LEVEL >= 1) Log.e(TAG, msg);
    }

    public static void e(String tag, String msg, Exception e) {
        if (LOGGING_LEVEL >= 1) Log.e(TAG, msg, e);
    }

    public static void w(String tag, String msg) {
        if (LOGGING_LEVEL >= 2) Log.w(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (LOGGING_LEVEL >= 3) Log.i(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (LOGGING_LEVEL >= 4) Log.d(TAG, msg);
    }

}
