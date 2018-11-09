package com.visirx.patient.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.visirx.patient.R;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MediaPlayerActivity extends AppCompatActivity {

    String name = null;
    String fileType = null;
    String data = null;
    private android.media.MediaPlayer mediaPlayer;
    private double timeElapsed = 0, finalTime = 0;
    private Handler durationHandler = new Handler();
    private SeekBar seekbar;
    public TextView txtDuration;

    //RAMESH
    Toolbar toolbar;

    // rony - EMRFRAGMENT GC - Starts
    //private byte[] audioData = null;
    private String audioFilePath = null;
    // rony - EMRFRAGMENT GC - Ends
    private ImageView imagePlay = null;
    private boolean isPlaying = false;
    int position = -1;
    NormalFont appId;
    NormalFont appDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appId = (NormalFont) findViewById(R.id.auscul_appId);
        appDate = (NormalFont) findViewById(R.id.auscul_appDate);
        if (getIntent() != null) {
            name = getIntent().getStringExtra(VTConstants.FILE_NAME);
            toolbar.setTitle(name);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appId.setText("APP ID :" + getIntent().getIntExtra(VTConstants.FILE_APPOINTMENTID, -1));
            appDate.setText("DATE :" + DateFormat.GetFormattedDateStr(getIntent().getStringExtra(VTConstants.AUSCULTATION_DATE)));
            fileType = getIntent().getStringExtra(VTConstants.FILE_TYPE);
            position = getIntent().getIntExtra(VTConstants.FILE_DATA, -1);
        }
        Log.d("SPIN", "NAME : " + name);
        Log.d("SPIN", "fileType : " + fileType);
        Log.d("SPIN", "position : " + position);
        List<AddEmrFileModel> emrImageFileList = AppContext.emrAudioFileList;
        AddEmrFileModel item = emrImageFileList.get(position);
        // rony - EMRFRAGMENT GC - Starts - commented - should fix later
        audioFilePath = item.getEmrImagePath();
        Log.d("SPIN", "audioFilePath : " + audioFilePath);
//        setHeading(name);
        //initialize views
        if (audioFilePath != null) {
            initializeViews();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void deleteExternalStoragePrivateFile(String path) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(path);
        if (file != null) {
            file.delete();
        }
    }

    // rony - EMRFRAGMENT GC - Starts
    public void initializeViews() {
        try {


			/*String filepath = getCacheDir().getAbsolutePath()+ "/PlayDemoFile." + fileType;
            File path=new File(audioFilePath);
			path.deleteOnExit();

			FileOutputStream fos = new FileOutputStream(path);
			fos.write(audioData);
			fos.close(); */
            mediaPlayer = new android.media.MediaPlayer();
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            // rony - EMRFRAGMENT GC - Ends
            finalTime = mediaPlayer.getDuration();
            txtDuration = (NormalFont) findViewById(R.id.file_duration);
            //txtDuration.setText(AppContext.GetDuration(audioData, getBaseContext()));
            seekbar = (SeekBar) findViewById(R.id.seekBar);
            imagePlay = (ImageView) findViewById(R.id.playButton);
            imagePlay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!isPlaying) {
                        play();
                        imagePlay.setImageResource(android.R.drawable.ic_media_pause);
                        isPlaying = true;
                    } else {
                        pause();
                        imagePlay.setImageResource(android.R.drawable.ic_media_play);
                        isPlaying = false;
                    }
                }
            });
            seekbar.setMax((int) finalTime);
            seekbar.setClickable(true);
            play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // play mp3 song
    public void play() {
        mediaPlayer.start();
        timeElapsed = mediaPlayer.getCurrentPosition();
        seekbar.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateSeekBarTime, 100);
    }

    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get current position
            timeElapsed = mediaPlayer.getCurrentPosition();
            //set seekbar progress
            seekbar.setProgress((int) timeElapsed);
            //set time remaing
            double timeRemaining = finalTime - timeElapsed;
            txtDuration.setText(String.format("%d: %d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining),
                    TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);
        }
    };

    // pause mp3 song
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        durationHandler.removeCallbacks(updateSeekBarTime);
        super.onDestroy();
    }

}
