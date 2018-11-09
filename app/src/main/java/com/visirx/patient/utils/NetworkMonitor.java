package com.visirx.patient.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Suresh on 11-05-2016.
 */
public class NetworkMonitor extends BroadcastReceiver {

    private static NetworkMonitor instance = null;
    private static String TAG = "NetWork BroadcastReceiver ";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    public NetworkMonitor getInstance() {
        if (instance != null) {
            return instance;
        }
        throw new IllegalStateException("NetworkMonitor: call init() first");
    }

    public static synchronized NetworkMonitor init(Context context) {
        NetworkMonitor networkMonitor;
        synchronized (NetworkMonitor.class) {
            if (instance == null) {
                instance = new NetworkMonitor(context);
                Log.w("Testing", "NetworkMonitor initialised...");
            }
            networkMonitor = instance;
        }
        return networkMonitor;
    }

    private NetworkMonitor(Context context) {
        sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(VTConstants.NetworkKeys.NETWORK_STATUS, hasDataConnection(context));
        editor.commit();
    }

    public NetworkMonitor() {

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Network Change Received");
        Log.i(TAG, "Old Network Is Connected : " + sharedPreferences.getBoolean(VTConstants.NetworkKeys.NETWORK_STATUS, false));
        Log.i(TAG, "Old Network Connection Type: " + sharedPreferences.getString(VTConstants.NetworkKeys.NETWORK_TYPE,
                VTConstants.NetworkKeys.NONE));
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        updateStatus(activeNetwork);
        Log.w(TAG, "New Network Is Connected : " + sharedPreferences.getBoolean(VTConstants.NetworkKeys.NETWORK_STATUS, false));
        Log.w(TAG, "New Network Connection Type: " + sharedPreferences.getString(VTConstants.NetworkKeys.NETWORK_TYPE,
                VTConstants.NetworkKeys.NONE));


    }

    private static void updateStatus(NetworkInfo activeNetwork) {
        boolean isConnected;
        isConnected = !(activeNetwork == null || !activeNetwork.isConnectedOrConnecting());
        if (!isConnected) {
            //not connected
            Log.e(TAG, "Internet Not Connected");
            editor.putBoolean(VTConstants.NetworkKeys.NETWORK_STATUS, false);
            editor.putString(VTConstants.NetworkKeys.NETWORK_TYPE, VTConstants.NetworkKeys.NONE);
        } else {
            Log.e(TAG, "Internet Connected");
            editor.putBoolean(VTConstants.NetworkKeys.NETWORK_STATUS, true);
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                editor.putString(
                        VTConstants.NetworkKeys.NETWORK_TYPE, VTConstants.NetworkKeys.WIFI);
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                int networkType = activeNetwork.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        editor.putString(VTConstants.NetworkKeys.NETWORK_TYPE, VTConstants.NetworkKeys.TWO_G);
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        editor.putString(
                                VTConstants.NetworkKeys.NETWORK_TYPE
                                , VTConstants.NetworkKeys.THREE_G);
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        editor.putString(
                                VTConstants.NetworkKeys.NETWORK_TYPE
                                , VTConstants.NetworkKeys.FOUR_G);
                        break;
                    default:
                        editor.putString(
                                VTConstants.NetworkKeys.NETWORK_TYPE
                                , VTConstants.NetworkKeys.NONE);
                        break;
                }
            }
        }
        editor.commit();
    }


    public static boolean hasDataConnection(Context context) {

        NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        updateStatus(activeNetwork);

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


}

