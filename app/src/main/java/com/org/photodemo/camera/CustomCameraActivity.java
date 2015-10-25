package com.org.photodemo.camera;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.org.photodemo.R;

public class CustomCameraActivity extends Activity {

    private Button btnTakepic;
    private Button btnRecordVedio;
    private CustomCameraView customCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);


        btnTakepic=(Button)findViewById(R.id.btn_custom_takepic);
        btnRecordVedio=(Button)findViewById(R.id.btn_custom_recordvideo);
        customCameraView=(CustomCameraView)findViewById(R.id.customcamera);

        btnTakepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCameraView.doTakePic();
            }
        });
        btnRecordVedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCameraView.doRecordVideo();
            }
        });
    }



}
