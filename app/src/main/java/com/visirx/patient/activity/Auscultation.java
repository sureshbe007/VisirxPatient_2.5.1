package com.visirx.patient.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;


import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.ImageDownloaderProvider;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.model.AppointmentPatientModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Auscultation extends AppCompatActivity {

    String TAG = Auscultation.class.getName();

    private List<AddEmrFileModel> emrAudioFileList = null;
    EMRFileAdapter adapter = null;
    private MediaPlayer myPlayer;
    int reservationNumber = -1;
    int trycount = 0;
    AppointmentPatientModel appointmentPatientModel = null;
    String date = null;
    GridView grid;

    String createdById = "";
    SharedPreferences loggedPreferance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auscultation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  rony - EMRFRAGMENT GC - Starts
        this.registerReceiver(receiverUpdate,
                new IntentFilter(VTConstants.NOTIFICATION_EMRFILE));
        //  rony - EMRFRAGMENT GC - Ends
        if (getIntent().getExtras() != null) {
            reservationNumber = getIntent().getIntExtra(
                    VTConstants.APPTMODEL_KEY, -1);
            appointmentPatientModel = VisirxApplication.aptDAO
                    .GetAppointmentsByID(reservationNumber);
            date = getIntent().getStringExtra(VTConstants.DATE);
        }
        loggedPreferance = getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
        createdById = loggedPreferance.getString(VTConstants.USER_ID, "Not set");
        emrAudioFileList = VisirxApplication.aptEmrDAO.GetPatientEMRFile(
                createdById,
                appointmentPatientModel.getReservationNumber(), date);
        AppContext.emrAudioFileList = emrAudioFileList;
        adapter = new EMRFileAdapter();
        GridView gridView = (GridView) findViewById(R.id.gridView_emrauscultation);
        gridView.setAdapter(adapter);
//        setHeading("Auscultation");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                AddEmrFileModel item = emrAudioFileList.get(position);
                Intent intent = new Intent(Auscultation.this,
                        MediaPlayerActivity.class);
                intent.putExtra(VTConstants.AUSCULTATION_DATE, appointmentPatientModel.getDate());
                intent.putExtra(VTConstants.FILE_APPOINTMENTID, appointmentPatientModel.getReservationNumber());
                intent.putExtra(VTConstants.FILE_NAME, item.getFileLabel());
                intent.putExtra(VTConstants.FILE_TYPE, item.getFileType());
                intent.putExtra(VTConstants.FILE_DATA, position);// TODO
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private final class EMRFileAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return emrAudioFileList.size();
        }

        @Override
        public AddEmrFileModel getItem(int position) {
            return emrAudioFileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getBaseContext(),
                        R.layout.emr_auscultationlist, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            AddEmrFileModel model = getItem(position);
            holder.txtFileName.setText((position + 1) + "."
                    + model.getFileLabel());
            if (+model.getFileLabel().toString().length() <= 5) {
                holder.txtFileName.setText((position + 1) + "."
                        + model.getFileLabel());
            } else {
                holder.txtFileName.setText((position + 1) + "."
                        + (model.getFileLabel().substring(0, 6) + "..."));
            }
            // byte[] data = Base64.decode(model.getImageThumbnail(),
            // Base64.DEFAULT);
            // rony - EMRFRAGMENT GC - Starts -

			/*byte[] data = model.getEmrFile();*/
            String audioFilePath = model.getEmrImagePath();
            Log.d("SPIN", "Audio Path : " + audioFilePath);
            if (audioFilePath != null) {
                File audioFile = new File(audioFilePath);
                //Uri emrAudioUri = Uri.fromFile(audioFile);
                if (!audioFile.exists()) {
                    //download from server.
                    if (trycount == 0) {
                        ImageDownloaderProvider dwnlodProvider = new ImageDownloaderProvider(
                                Auscultation.this,
                                VTConstants.NOTIFICATION_EMRFILE);
                        dwnlodProvider.SendAudioReq(
                                model.getAppointmentId(),
                                model.getCreatedAt(),
                                model.getPatientId(),
                                model.getFileMimeType());
                        trycount++;
                    }
                }

            } else {
                //download from server.
                //download from server.
                if (trycount == 0) {
                    ImageDownloaderProvider dwnlodProvider = new ImageDownloaderProvider(
                            Auscultation.this,
                            VTConstants.NOTIFICATION_EMRFILE);
                    dwnlodProvider.SendAudioReq(
                            model.getAppointmentId(),
                            model.getCreatedAt(),
                            model.getPatientId(),
                            model.getFileMimeType());
                    trycount++;
                }

            }
            holder.txtDate.setText(DateFormat.GetFormattedDateTimeStr(model
                    .getCreatedAt()));
            holder.txtDuraion.setText(GetDuration(audioFilePath, getBaseContext(),
                    model.getFileType()));
            // rony - EMRFRAGMENT GC - Ends
            return convertView;
        }

        class ViewHolder {
            NormalFont txtFileName;
            NormalFont txtDate;
            NormalFont txtDuraion;

            public ViewHolder(View view) {
                txtFileName = (NormalFont) view
                        .findViewById(R.id.lblAusName);
                txtDate = (NormalFont) view.findViewById(R.id.lblAusDate);
                txtDuraion = (NormalFont) view
                        .findViewById(R.id.lblAusSec);
                view.setTag(this);
            }
        }
    }
    // rony - EMRFRAGMENT GC - Starts

    private String GetDuration(String filepath, Context context,
                               String fileType) {
        String duration = null;
        try {

			/*String filepath = context.getCacheDir().getAbsolutePath()
                    + "/DemoFile." + fileType;

			path.deleteOnExit();

			FileOutputStream fos = new FileOutputStream(path);
			fos.write(mp3SoundByteArray);
			fos.close();
*/
            Log.d("SPIN", "Audio Path : filepath : " + filepath);
            myPlayer = new MediaPlayer();
            myPlayer.setDataSource(filepath);
            myPlayer.prepare();
            // myPlayer.start();
            long milliDuration = myPlayer.getDuration();
            duration = String.format(
                    "%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes(milliDuration),
                    TimeUnit.MILLISECONDS.toSeconds(milliDuration)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(milliDuration)));
            // mediaPlayer.release();
        } catch (Exception ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
        return duration;
    }
//  rony - EMRFRAGMENT GC - Starts

    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d("SPIN", "Appts EMR Grid view broadcast received...");
                trycount--;
                emrAudioFileList = VisirxApplication.aptEmrDAO.GetPatientEMRFile(
                        createdById,
                        appointmentPatientModel.getReservationNumber(),  date);
                AppContext.emrAudioFileList = emrAudioFileList;
                //adapter = new  EMRFileAdapter();
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogTrace.e(TAG, e.getMessage());
            }
        }
    };

    @Override
    public void onDestroy() {
        try {
            this.unregisterReceiver(receiverUpdate);
            if (myPlayer != null) {
                myPlayer.release();
                myPlayer = null;
            }

        } catch (Exception e) {
            // e.printStackTrace();
        }
        super.onDestroy();
    }


}
