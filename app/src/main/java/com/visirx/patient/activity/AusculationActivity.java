package com.visirx.patient.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.customview.NunitoTextView;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.model.AppointmentParamedicModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AusculationActivity extends BaseActivity {


    private List<AddEmrFileModel> emrAudioFileList = null;
    EMRFileAdapter adapter = null;
    private MediaPlayer myPlayer;
    int reservationNumber = -1;
    AppointmentParamedicModel appointmentModel = null;
    String date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ausculation);
        if (getIntent().getExtras() != null) {
            reservationNumber = getIntent().getIntExtra(VTConstants.APPTMODEL_KEY, -1);
            // hide by suresh for temp check..
//            appointmentModel = VisirxApplication.aptDAO.GetAppointmentsByID(reservationNumber);
            date = getIntent().getStringExtra(VTConstants.DATE);
        }
//        emrAudioFileList = VisirxApplication.aptEmrDAO.
//                GetPatientEMRFile(appointmentModel.getCustomerId(), appointmentModel.getReservationNumber(),"Auscultation",date);
//        AppContext.emrAudioFileList = emrAudioFileList;
        adapter = new EMRFileAdapter();
        GridView gridView = (GridView) findViewById(R.id.gridView_ausculation);
        gridView.setAdapter(adapter);
        setHeading("Auscultation");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                AddEmrFileModel item = emrAudioFileList.get(position);
                Intent intent = new Intent(getBaseContext(), MediaPlayerActivity.class);
                intent.putExtra(VTConstants.FILE_NAME, item.getFileLabel());
                intent.putExtra(VTConstants.FILE_TYPE, item.getFileType());
                intent.putExtra(VTConstants.FILE_DATA, position);//TODO
                startActivity(intent);
            }
        });
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
                        R.layout.ausculation_grid_item, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            AddEmrFileModel model = getItem(position);
            if (model.getFileLabel().toString().length() <= 5) {
                holder.txtFileName.setText((position + 1) + "." + model.getFileLabel());
            } else {
                holder.txtFileName.setText((position + 1) + "." + (model.getFileLabel().substring(0, 6) + "..."));
            }
            //byte[] data = Base64.decode(model.getImageThumbnail(), Base64.DEFAULT);
            byte[] data = model.getEmrFile();
            holder.txtDate.setText(DateFormat.GetFormattedDateTimeStr(model.getCreatedAt()));
            holder.txtDuraion.setText(GetDuration(data, getBaseContext(), model.getFileType()));
            return convertView;
        }

        class ViewHolder {
            NunitoTextView txtFileName;
            NunitoTextView txtDate;
            NunitoTextView txtDuraion;

            public ViewHolder(View view) {
                txtFileName = (NunitoTextView) view.findViewById(R.id.text_file_name);
                txtDate = (NunitoTextView) view.findViewById(R.id.textDate);
                txtDuraion = (NunitoTextView) view.findViewById(R.id.textDuration);
                view.setTag(this);
            }
        }
    }

    private String GetDuration(byte[] mp3SoundByteArray, Context context, String fileType) {
        String duration = null;
        try {
            // create temp file that will hold byte array
            /*String filepath = Environment.getExternalStorageDirectory().
					getAbsolutePath() + "/DemoFile." + fileType;
			File path=new File(filepath);*/
			/*byte[] mp3SoundByteArray = null;
			if(bytearray != null){
				mp3SoundByteArray = Base64.decode(bytearray, Base64.DEFAULT);
			}		*/
            String filepath = context.getCacheDir().getAbsolutePath() + "/DemoFile." + fileType;
            File path = new File(filepath);
            path.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(mp3SoundByteArray);
            fos.close();
            myPlayer = new MediaPlayer();
            myPlayer.setDataSource(filepath);
            myPlayer.prepare();
            //myPlayer.start();
            long milliDuration = myPlayer.getDuration();
            duration = String.format("%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes(milliDuration),
                    TimeUnit.MILLISECONDS.toSeconds(milliDuration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliDuration))
            );
            //mediaPlayer.release();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
        return duration;
    }

    @Override
    public void onDestroy() {
        if (myPlayer != null) {
            myPlayer.release();
            myPlayer = null;
        }
        super.onDestroy();
    }


}
