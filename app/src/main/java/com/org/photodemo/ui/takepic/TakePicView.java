package com.org.photodemo.ui.takepic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.org.photodemo.R;
import com.org.photodemo.util.FileUtil;
import com.org.photodemo.util.MyLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gzhenkai on 15/10/24.
 */
public class TakePicView extends LinearLayout implements SurfaceHolder.Callback{

    MyLogger myLogger=MyLogger.getMyLogger();

    private static final int ORIGINAL_DATE=0;//原始数据
    private static final int ZIPORSCALE_DATE=1;//压缩 放缩数据
    private static final int JPG_DATE=2;//jpg数据


    private SurfaceView sfPreView;
    private RelativeLayout relativeLayoutButtom;
    private Button btnTakePic;
    private SurfaceHolder mSurfaceHolder;

    private Camera mCamera;

    private Context mContext;
    private CamcorderProfile mProfile;
    private Camera.Parameters mParameters;


    public TakePicView(Context context) {
        this(context, null);
    }

    public TakePicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TakePicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext=context;
        LayoutInflater.from(context).inflate(R.layout.take_pic, this);
        sfPreView=(SurfaceView)findViewById(R.id.sfv_take_pic);
        relativeLayoutButtom=(RelativeLayout)findViewById(R.id.relative_take);
        btnTakePic=(Button)findViewById(R.id.btn_take);
        mSurfaceHolder=sfPreView.getHolder();
        sfPreView.getHolder().setKeepScreenOn(true);// 屏幕常亮

        mSurfaceHolder.addCallback(this);
        // 针对低于3.0的Android
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btnTakePic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTakePick();
            }
        });

        prepareWork();

    }

    private void prepareWork(){

        readVideoPreferences();
        // 创建文件夹
       /* File file = new File(Ppath);
        if (!file.exists()) {
            file.mkdir();
        }*/
        // 获取Camera实例
        mCamera = getCamera();
        if (mCamera != null) {
            // 因为android不支持竖屏录制，所以需要顺时针转90度，让其游览器显示正常
            mCamera.setDisplayOrientation(90);
            mCamera.lock();
            initCameraParameters();
        }

    }


    /**
     * 获取摄像头实例
     * @return
     */
    private Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }


    private void startTakePick(){
        myLogger.e("startTakePic  mCamer:"+mCamera);
        mCamera.takePicture(null,null,null,new CustomPictureCallback(JPG_DATE));
    }


    /**
     * 设置摄像头参数
     */
    private void readVideoPreferences() {
        boolean videoQualityHigh = false;
        mProfile = CamcorderProfile.get(videoQualityHigh ? CamcorderProfile.QUALITY_HIGH : CamcorderProfile.QUALITY_LOW);
        mProfile.videoFrameWidth = (int) (mProfile.videoFrameWidth * 2.0f);
        mProfile.videoFrameHeight = (int) (mProfile.videoFrameHeight * 2.0f);
        mProfile.videoBitRate = 256000 * 3;

        CamcorderProfile highProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        mProfile.videoCodec = highProfile.videoCodec;
        mProfile.audioCodec = highProfile.audioCodec;
        mProfile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
    }




    /**
     * 设置摄像头参数
     */
    private void initCameraParameters() {
        // 初始化摄像头参数
        mParameters = mCamera.getParameters();
        myLogger.e("mCamera:"+mCamera);
        myLogger.e("mParameters:"+mParameters);
        myLogger.e("mProfile :"+mProfile );
        myLogger.e("mProfile.videoFrameWidth :"+mProfile.videoFrameWidth );
        myLogger.e("mProfile.videoFrameHeight :"+mProfile.videoFrameHeight );


        mParameters.setPreviewSize(mProfile.videoFrameWidth, mProfile.videoFrameHeight);
        mParameters.setPreviewFrameRate(mProfile.videoFrameRate);
        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        // 设置白平衡参数。
        //mParameters.setWhiteBalance("auto");
        // 参数设置颜色效果。
        //mParameters.setColorEffect("none");

        try {
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放Camera
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();// 停掉原来摄像头的预览
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 异常处理
     *
     */
    private void recodError() {
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


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }


    class  CustomPictureCallback implements Camera.PictureCallback{
        private int picturCallbackType;
        public CustomPictureCallback(int type){
            this.picturCallbackType=type;
        }
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            myLogger.e("picturCallbackType: " +picturCallbackType);
            if(picturCallbackType==ORIGINAL_DATE){

            }else if(picturCallbackType==ZIPORSCALE_DATE){

            }else if(picturCallbackType==JPG_DATE){

                //save the picture to sdcard
                File pictureFile = new File(FileUtil.getCacheFolder()+File.separator+System.currentTimeMillis() + ".jpg");
                if (pictureFile == null){
                   myLogger.e( "Error creating media file, check storage permissions: ");
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    myLogger.e("File not found: " + e.getMessage());
                } catch (IOException e) {
                    myLogger.e( "Error accessing file: " + e.getMessage());
                }

            }

            camera.startPreview();


        }
    }




}
