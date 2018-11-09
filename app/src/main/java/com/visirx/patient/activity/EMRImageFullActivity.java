package com.visirx.patient.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.visirx.patient.R;
import com.visirx.patient.VisirxApplication;
import com.visirx.patient.common.AppContext;
import com.visirx.patient.customview.TouchImageView;
import com.visirx.patient.model.AddEmrFileModel;
import com.visirx.patient.utils.VTConstants;

import java.util.List;

public class EMRImageFullActivity extends BaseActivity {

    String name = null;
    String duration = null;
    int position = -1;
    private byte[] imageData = null;
    TouchImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emrimage_full);
        if (getIntent() != null) {
            name = getIntent().getStringExtra(VTConstants.FILE_NAME);
            //duration = getIntent().getStringExtra(VTConstants.FILE_DURATION);
            position = getIntent().getIntExtra(VTConstants.FILE_DATA, -1);
        }
        if (name == null) {
            name = VisirxApplication.appContext.getLoggedUser().getFirstName() +
                    " " + VisirxApplication.appContext.getLoggedUser().getLastName();
        }
        setHeading(name);
        if (position > -1) {
            initializeViews();
        }
    }

    public void initializeViews() {
        try {
            List<AddEmrFileModel> emrImageFileList = AppContext.emrImageFileList;
            AddEmrFileModel item = emrImageFileList.get(position);
            //SendFullImageReq(item.getCreatedAt(), item.getPatientId());
            imageView = (TouchImageView) findViewById(R.id.picture_emr_full);
//            imageData = item.getEmrFile();
//            if(imageData != null){
//                ///byte[] bytearray = Base64.decode(imageData, Base64.DEFAULT);
//                Bitmap b = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//                if(b != null){
//                    imageView.setImageBitmap(b);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
