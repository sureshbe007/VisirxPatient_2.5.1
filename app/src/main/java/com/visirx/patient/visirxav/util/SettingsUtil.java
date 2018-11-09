package com.visirx.patient.visirxav.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCMediaConfig;
import com.visirx.patient.R;

import java.util.List;

/**
 * QuickBlox team
 */
public class SettingsUtil {

    private static final String TAG = SettingsUtil.class.getSimpleName();

    private static void setSettingsForMultiCall(List<QBUser> users) {
        if (users.size() <= 2) {
            int width = QBRTCMediaConfig.getVideoWidth();
            if (width > QBRTCMediaConfig.VideoQuality.VGA_VIDEO.width) {
                QBRTCMediaConfig.setVideoWidth(QBRTCMediaConfig.VideoQuality.VGA_VIDEO.width);
                QBRTCMediaConfig.setVideoHeight(QBRTCMediaConfig.VideoQuality.VGA_VIDEO.height);
            }
        } else {
            //set to minimum settings
            QBRTCMediaConfig.setVideoWidth(QBRTCMediaConfig.VideoQuality.QBGA_VIDEO.width);
            QBRTCMediaConfig.setVideoHeight(QBRTCMediaConfig.VideoQuality.QBGA_VIDEO.height);
            QBRTCMediaConfig.setVideoHWAcceleration(false);
            QBRTCMediaConfig.setVideoCodec(null);
        }
    }

    public static void setSettingsStrategy(List<QBUser> users, SharedPreferences sharedPref, Context context) {
        setCommonSettings(sharedPref, context);
        if (users.size() == 1) {
            setSettingsFromPreferences(sharedPref, context);
        } else {
            setSettingsForMultiCall(users);
        }
    }

    private static void setSettingsFromPreferences(SharedPreferences sharedPref, Context context) {

        // Check HW codec flag.
        boolean hwCodec = sharedPref.getBoolean(context.getString(R.string.pref_hwcodec_key),
                Boolean.valueOf(context.getString(R.string.pref_hwcodec_default)));

        QBRTCMediaConfig.setVideoHWAcceleration(hwCodec);
        // Get video resolution from settings.
        int resolutionItem = Integer.parseInt(sharedPref.getString(context.getString(R.string.pref_resolution_key),
                "0"));
        Log.e(TAG, "resolutionItem =: " + resolutionItem);
        setVideoQuality(resolutionItem);

        // Get camera fps from settings.
        int cameraFps = Integer.parseInt(getPreferenceString(sharedPref, context, R.string.pref_fps_key, "0"));

        QBRTCMediaConfig.setVideoFps(cameraFps);

        // Get start bitrate.
        String bitrateTypeDefault = context.getString(R.string.pref_startbitrate_default);
        String bitrateType = sharedPref.getString(
                context.getString(R.string.pref_startbitrate_key), bitrateTypeDefault);
        if (!bitrateType.equals(bitrateTypeDefault)) {
            String bitrateValue = sharedPref.getString(context.getString(R.string.pref_startbitratevalue_key),
                    context.getString(R.string.pref_startbitratevalue_default));
            int startBitrate = Integer.parseInt(bitrateValue);
            QBRTCMediaConfig.setVideoStartBitrate(startBitrate);
        }

        int videoCodecItem = Integer.parseInt(getPreferenceString(sharedPref, context, R.string.pref_videocodec_key, "0"));
        for (QBRTCMediaConfig.VideoCodec codec : QBRTCMediaConfig.VideoCodec.values()) {
            if (codec.ordinal() == videoCodecItem) {
                Log.e(TAG, "videoCodecItem =: " + codec.getDescription());
                QBRTCMediaConfig.setVideoCodec(codec);
                break;
            }
        }

    }


    private static void setCommonSettings(SharedPreferences sharedPref, Context context) {

        String audioCodecDescription = getPreferenceString(sharedPref, context, R.string.pref_audiocodec_key,
                R.string.pref_audiocodec_def);
        QBRTCMediaConfig.AudioCodec audioCodec = QBRTCMediaConfig.AudioCodec.ISAC.getDescription()
                .equals(audioCodecDescription) ?
                QBRTCMediaConfig.AudioCodec.ISAC : QBRTCMediaConfig.AudioCodec.OPUS;
        Log.e(TAG, "audioCodec =: " + audioCodec.getDescription());
        QBRTCMediaConfig.setAudioCodec(audioCodec);

        setAudioSettings(sharedPref, context);
    }

    private static void setAudioSettings(SharedPreferences sharedPref, Context context) {
        // Check Disable Audio Processing flag.
        boolean noAudioProcessing = getPreferenceBoolean(sharedPref, context,
                R.string.pref_noaudioprocessing_key,
                R.string.pref_noaudioprocessing_default);
        QBRTCMediaConfig.setAudioProcessingEnabled(!noAudioProcessing);


        // Check Enable AEC dump flag.
        boolean aecDump = getPreferenceBoolean(sharedPref, context,
                R.string.pref_aecdump_key,
                R.string.pref_aecdump_default);

        QBRTCMediaConfig.setAecDumpEnabled(aecDump);

        // Check Disable built-in AEC flag.
        boolean disableBuiltInAEC = getPreferenceBoolean(sharedPref, context,
                R.string.pref_disable_built_in_aec_key,
                R.string.pref_disable_built_in_aec_default);

        QBRTCMediaConfig.setUseBuildInAEC(!disableBuiltInAEC);

        // Check OpenSL ES enabled flag.
        boolean useOpenSLES = getPreferenceBoolean(sharedPref, context,
                R.string.pref_opensles_key,
                R.string.pref_opensles_default);
        QBRTCMediaConfig.setUseOpenSLES(useOpenSLES);
    }

    private static void setVideoQuality(int resolutionItem) {
        if (resolutionItem != -1) {
            setVideoFromLibraryPreferences(resolutionItem);
        }
    }

    private static void setVideoFromLibraryPreferences(int resolutionItem) {
        for (QBRTCMediaConfig.VideoQuality quality : QBRTCMediaConfig.VideoQuality.values()) {
            if (quality.ordinal() == resolutionItem) {
                Log.e(TAG, "resolution =: " + quality.height + ":" + quality.width);
                QBRTCMediaConfig.setVideoHeight(quality.height);
                QBRTCMediaConfig.setVideoWidth(quality.width);

            }
        }
    }

    private static String getPreferenceString(SharedPreferences sharedPref, Context context, int StrRes, int StrResDefValue) {
        return sharedPref.getString(context.getString(StrRes),
                context.getString(StrResDefValue));
    }

    private static String getPreferenceString(SharedPreferences sharedPref, Context context, int StrRes, String defValue) {
        return sharedPref.getString(context.getString(StrRes),
                defValue);
    }

    private static boolean getPreferenceBoolean(SharedPreferences sharedPref, Context context, int StrRes, int strResDefValue) {
        return sharedPref.getBoolean(context.getString(StrRes),
                Boolean.valueOf(context.getString(strResDefValue)));
    }

    public static boolean isInitiatorSessionOwner(SharedPreferences defaultSharedPrefs, Context context) {
        return defaultSharedPrefs.getBoolean(context.getString(R.string.pref_initiator_behaviour_key),
                Boolean.valueOf(context.getString(R.string.pref_initiator_behaviour_default)));
    }
}
