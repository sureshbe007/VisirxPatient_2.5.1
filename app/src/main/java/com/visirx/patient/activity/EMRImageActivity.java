package com.visirx.patient.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.common.LogTrace;
import com.visirx.patient.common.provider.ImageDownloaderProvider;
import com.visirx.patient.customview.NunitoTextView;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.model.AppointmentModel;
import com.visirx.patient.model.AppointmentParamedicModel;
import com.visirx.patient.utils.DateFormat;
import com.visirx.patient.utils.VTConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class EMRImageActivity extends BaseActivity {

    private List<AddEmrFileModel> emrImageFileList = null;
    EMRFileAdapter adapter = null;
    int reservationNumber = -1;
    AppointmentParamedicModel appointmentModel = null;
    String date = null;
    SharedPreferences loggedPreferance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emrimage);

        if (getIntent().getExtras() != null) {
            reservationNumber = getIntent().getIntExtra(VTConstants.APPTMODEL_KEY, -1);
//            appointmentModel = VisirxApplication.aptDAO.GetAppointmentsByID(reservationNumber);
            date = getIntent().getStringExtra(VTConstants.DATE);
        }

//        emrImageFileList = VisirxApplication.aptEmrDAO.
//                GetPatientEMRFile(appointmentModel.getCustomerId(), appointmentModel.getReservationNumber(),"Image",date);
//        AppContext.emrImageFileList = emrImageFileList;

        adapter = new EMRFileAdapter();

        GridView gridView = (GridView) findViewById(R.id.gridView_emrimage);
        gridView.setAdapter(adapter);

        loggedPreferance = getSharedPreferences(
                VTConstants.PREFS_LOGGEDUSERID, 0);
        setHeading(loggedPreferance.getString(VTConstants.LOGGED_USER_FULLNAME, "Not set"));
//		setHeading(VisirxApplication.appContext.getLoggedUser().getFirstName() +
//				" " + VisirxApplication.appContext.getLoggedUser().getLastName());


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                AddEmrFileModel item = emrImageFileList.get(position);

                Intent intent = new Intent(getBaseContext(), EMRImageFullActivity.class);
                intent.putExtra(VTConstants.FILE_NAME, item.getFileLabel());
                //intent.putExtra(VTConstants.FILE_DURATION, item.duration);
                intent.putExtra(VTConstants.FILE_DATA, position);//TODO
                startActivity(intent);
            }
        });

    }

    private final class EMRFileAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return emrImageFileList.size();
        }

        @Override
        public AddEmrFileModel getItem(int position) {
            return emrImageFileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getBaseContext(),
                        R.layout.emr_image_grid_item, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            AddEmrFileModel model = getItem(position);

            // rony - EMRFRAGMENT GC - Starts

			/*byte[] photo = model.getImageThumbnail();
            if(photo != null){
				//byte[] bytearray = Base64.decode(photo, Base64.DEFAULT);
				Bitmap b = BitmapFactory.decodeByteArray(photo, 0,photo.length);
				if(b != null){
					holder.imageThumnail.setImageBitmap(b);
				}
			}*/

            String thumbnail_photo = model.getEmrImageThumbnailPath();
            //Load the file from sd card stored path - starts


            if (thumbnail_photo != null) {
                // Toast.makeText(getApplicationContext(),"Path is not null",Toast.LENGTH_SHORT).show();
                Log.d("SPIN", "Image path : " + thumbnail_photo);

                File imgFile = new File(thumbnail_photo);
                Uri emrThumbImgUri = Uri.fromFile(imgFile);

                if (imgFile.exists()) {

                    Bitmap myBitmap = null;

                    try {
                        myBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), emrThumbImgUri);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    holder.imageThumnail.setImageBitmap(myBitmap);

                    //Load the file from sd card stored path - ends
                } else {

                    // download the image from server.
                    ImageDownloaderProvider dwnlodProvider = new ImageDownloaderProvider(
                            EMRImageActivity.this,
                            VTConstants.NOTIFICATION_EMRFILE);
                    dwnlodProvider.SendEmrImgReq(
                            model.getAppointmentId(),
                            model.getCreatedAt(),
                            model.getPatientId(), VTConstants.THUMBNAIL_FLAG);

                    //Toast.makeText(getApplicationContext(),"PHOTO path Null",Toast.LENGTH_SHORT).show();
                    Bitmap defaultIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.default_image);

                    holder.imageThumnail.setImageBitmap(defaultIcon);
                }
            }


// rony - EMRFRAGMENT GC - Ends
			/*

			byte[] byteArray = model.getImageThumbnail();
			Bitmap b = BitmapFactory.decodeByteArray(byteArray, 0,byteArray.length);
			if(b != null){
				holder.imageThumnail.setImageBitmap(b);
			}*/
            if (model.getFileLabel() != null) {


                if (model.getFileLabel().toString().length() <= 5) {
                    holder.txtFileName.setText((position + 1) + "." + model.getFileLabel());
                } else {
                    holder.txtFileName.setText((position + 1) + "." + (model.getFileLabel().substring(0, 6) + "..."));
                }
            }
            holder.txtDate.setText(DateFormat.GetFormattedDateTimeStr(model.getCreatedAt()));
            return convertView;
        }

        class ViewHolder {
            NunitoTextView txtFileName;
            NunitoTextView txtDate;
            ImageView imageThumnail;

            public ViewHolder(View view) {
                imageThumnail = (ImageView) view.findViewById(R.id.picture_emr);
                txtFileName = (NunitoTextView) view.findViewById(R.id.text_EMRfile_name);
                txtDate = (NunitoTextView) view.findViewById(R.id.textEMRDuration);
                view.setTag(this);
            }
        }
    }


}
