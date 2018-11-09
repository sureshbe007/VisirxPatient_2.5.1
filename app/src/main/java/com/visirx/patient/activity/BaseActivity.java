package com.visirx.patient.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.visirx.patient.R;
import com.visirx.patient.common.LogTrace;

public class BaseActivity extends AppCompatActivity {

    String TAG = BaseActivity.class.getName();
    String header = "";
    protected TextView txtHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_base2);

			/*TextView textView = (TextView)findViewById(R.id.title_bar);
            textView.setText("Add Notes");
			 */
            ImageView imageView = (ImageView) findViewById(R.id.backView);
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogTrace.e(TAG, e.getMessage());
        }
    }

    protected void setHeading(String heading) {
        header = heading;
        if (txtHeading == null)
            txtHeading = (TextView) findViewById(R.id.title_bar);
        if (txtHeading != null)
            txtHeading.setText(heading);
    }
}
