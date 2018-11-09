package com.visirx.patient.visirxav.util;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;


import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPingManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smackx.ping.PingFailedListener;

import java.util.concurrent.TimeUnit;

public class ChatPingAlarmManager {

    private static final String TAG = ChatPingAlarmManager.class.getSimpleName();
    private static final String PING_ALARM_ACTION = "com.quickblox.chat.ping.ACTION";

    private static final BroadcastReceiver ALARM_BROADCAST_RECEIVER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "Ping Alarm broadcast received");

            if (enabled) {
                Log.d(TAG, "Calling pingServer for connection ");
                final QBPingManager pingManager = QBChatService.getInstance().getPingManager();
                // Android BroadcastReceivers have a timeout of 60 seconds.
                // The connections reply timeout may be higher, which causes
                // timeouts of the broadcast receiver and a subsequent ANR
                // of the App of the broadcast receiver. We therefore need
                // to call pingServerIfNecessary() in a new thread to avoid
                // this. It could happen that the device gets back to sleep
                // until the Thread runs, but that's a risk we are willing
                // to take into account as it's unlikely.
                if (pingManager != null) {
                    pingManager.pingServer(new QBEntityCallback<Void>() {
                        @Override
                        public void onSuccess(Void result, Bundle params) {
                        }

                        @Override
                        public void onError(QBResponseException responseException) {
                            if (pingFailedListener != null ) {
                                pingFailedListener.pingFailed();
                            }
                        }
                    });
                }

            } else {
                Log.d(TAG, "NOT calling pingServerIfNecessary (disabled) on connection ");
            }
        }
    };


    private static Context sContext;
    private static PendingIntent sPendingIntent;
    private static AlarmManager sAlarmManager;
    private static boolean enabled = true;
    private static ChatPingAlarmManager instance;
    private final QBPingManager pingManager;
    private static PingFailedListener pingFailedListener;

    public static void setEnabled(boolean enabled){

        ChatPingAlarmManager.enabled = enabled;
    }

    private ChatPingAlarmManager() {
        pingManager = QBChatService.getInstance().getPingManager();

    }

    public void addPingListener(PingFailedListener pingFailedListener){
        this.pingFailedListener = pingFailedListener;
    }

    public static synchronized ChatPingAlarmManager getInstanceFor() {
        if (instance == null) {
            instance = new ChatPingAlarmManager();
        }
        return instance;
    }

    /**
     * Register a pending intent with the AlarmManager to be broadcasted every
     * half hour and register the alarm broadcast receiver to receive this
     * intent. The receiver will check all known questions if a ping is
     * Necessary when invoked by the alarm intent.
     *
     * @param context
     */
    public static void onCreate(Context context) {
        sContext = context;
        context.registerReceiver(ALARM_BROADCAST_RECEIVER, new IntentFilter(PING_ALARM_ACTION));
        sAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        sPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(PING_ALARM_ACTION), 0);
        sAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + TimeUnit.SECONDS.toMillis(10),
                TimeUnit.SECONDS.toMillis(10), sPendingIntent);
    }

    /**
     * Unregister the alarm broadcast receiver and cancel the alarm.
     */
    public static void onDestroy() {
        if (sContext != null) {
            sContext.unregisterReceiver(ALARM_BROADCAST_RECEIVER);
        }
        if (sAlarmManager != null) {
            sAlarmManager.cancel(sPendingIntent);
        }
        pingFailedListener = null;
        instance = null;
    }
}
