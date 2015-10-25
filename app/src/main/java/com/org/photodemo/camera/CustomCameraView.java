package com.org.photodemo.camera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.org.photodemo.R;

import java.io.IOException;

/**
 * Created by gzhenkai on 15/10/25.
 */
public class CustomCameraView extends LinearLayout implements SurfaceHolder.Callback{

    private SurfaceView surfaceViewPreviewDisplay;
    private RelativeLayout relativeLayoutCameraTop;
    private ImageView imgCameraBack;
    private ImageView imgSwitchCameraFlashlight;
    private ImageView imgSwitchCameraFrontOrBack;
    private RelativeLayout relativeProgressbar;
    private ImageView imgVideoTimeAtLast;
    private ImageView imgTimeStart;
    private LinearLayout linearLayoutProgressContain;


    private SurfaceHolder mSurfaceHolder;

    CustomCameraUtil customCameraUtil;



    private Context mContext;
    public CustomCameraView(Context context) {
        this(context, null);
    }

    public CustomCameraView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;

        LayoutInflater.from(context).inflate(R.layout.custom_carema,this);
        surfaceViewPreviewDisplay=(SurfaceView)findViewById(R.id.surfaceview_display_preview);
        relativeLayoutCameraTop=(RelativeLayout)findViewById(R.id.relative_camera_top);
        imgCameraBack=(ImageView)findViewById(R.id.img_switch_camera_back);
        imgSwitchCameraFlashlight=(ImageView)findViewById(R.id.img_switch_camera_flashlight);
        imgSwitchCameraFrontOrBack=(ImageView)findViewById(R.id.img_switch_camera_front_or_back);
        relativeProgressbar=(RelativeLayout)findViewById(R.id.relative_progressbar);
        imgVideoTimeAtLast=(ImageView)findViewById(R.id.img_video_time_atlast);
        imgTimeStart=(ImageView)findViewById(R.id.img_time_start);
        linearLayoutProgressContain=(LinearLayout)findViewById(R.id.linearlayout_seekbar);

        mSurfaceHolder=surfaceViewPreviewDisplay.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);// 屏幕常亮
        mSurfaceHolder.addCallback(this);
        // 针对低于3.0的Android
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        customCameraUtil=new CustomCameraUtil(mSurfaceHolder);
        customCameraUtil.setmCustomCameraInterface(new CustomCameraUtil.CustomCameraInterface() {
            @Override
            public void cameraError() {
                dialogError();
            }

            @Override
            public void cameraSwitch() {

            }

            @Override
            public void cameraStartPreview() {

            }
        });

    }

    public void doTakePic(){
        customCameraUtil.takePic();
    }
    public void doRecordVideo(){
        customCameraUtil.stopRecord();
    }


    /**
     * 异常处理
     *
     */
    private void dialogError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("该设备暂不支持视频录制");
        builder.setTitle("出错啦");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //TODO
                // finish();
            }

        });
        builder.create().show();

    }

    /**
     * 设置camera显示取景画面,并预览
     * @param holder
     */
    private void setStartPreview(SurfaceHolder holder) {
        try {
            if (customCameraUtil.getmCamera() != null) {
                customCameraUtil.getmCamera().setPreviewDisplay(holder);
                customCameraUtil.getmCamera().startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
            dialogError();
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        customCameraUtil.releaseCamera();
    }




}
