package com.org.photodemo.record;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.org.photodemo.R;
import com.org.photodemo.base.BaseActivity;
import com.org.photodemo.util.DensityUtil;
import com.org.photodemo.util.FileUtil;

import java.io.File;
import java.io.IOException;

public class VideoRecordActivity extends BaseActivity implements SurfaceHolder.Callback {

    /**
     * **********控件定义 satrt***************
     */
    private SurfaceView surfaceVedioView;
    private ImageView imBack;//后退按钮
    private ImageView imSwitchCameraFlashLight;//闪光灯按钮
    private ImageView imgSwitchCameraFrontOrBack;//切换前后摄像头
    private RelativeLayout relativeLayoutVideoNewLayout;//进度条布局
    private ImageView imSeekbarBg;//进度条背景
    private LinearLayout linearLayoutSeekbar;//进度条 容器
    private ImageView imSeekbarStart;//进度条start
    private ImageView imVideoDelete;//视频删除按钮
    private ImageButton imbtnVideoRecord;//开始录制按钮
    private TextView tvRecordTip;//录制提示
    private ImageView imMoreVedio;//查看更多录制视频；
    private ImageView imPlayVedio;//播放录制视频

    /**
     * **********控件定义 end***************
     */

    //
    /** 屏幕宽度 */
    private int mScreenWidth;
    /** 视频最大支持15秒 */
    public static final int RECORD_VIDEO_TIME_MAX = 15;
    /** 视频最少必须5秒 */
    public static final int RECORD_VIDEO_TIME_MIN = 5;

    /** 录制视频保存文件 */
    private String vedioPath;
    /** 录制视频的类 */
    private MediaRecorder mMediaRecorder;
    /** 摄像头对象 */
    private Camera mCamera;
    /** 摄像头参数 */
    private Camera.Parameters mParameters;//
    /** 视频输出质量 */
    private CamcorderProfile mProfile;
    /** 刷新界面的回调 */
    private SurfaceHolder mHolder;
    /** 1表示后置，0表示前置 */
    private int cameraPosition = 1;
    /** 路径 */
    //private String Ppath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/videoTest/";


    @Override
    public int doGetContentViewId() {
        return R.layout.activity_video_record;
    }

    @Override
    public void doInitSubView(View view) {
        surfaceVedioView = queryViewById(R.id.surfaceview_vedio_display);
        imBack = queryViewById(R.id.img_record_back, true);
        imSwitchCameraFlashLight = queryViewById(R.id.img_switch_camera_flashlight, true);
        imgSwitchCameraFrontOrBack = queryViewById(R.id.img_switch_camera_front_or_back, true);
        relativeLayoutVideoNewLayout = queryViewById(R.id.video_new_relative);
        imSeekbarBg = queryViewById(R.id.img_video_time_atlast);
        linearLayoutSeekbar = queryViewById(R.id.video_new_seekbar);
        imSeekbarStart = queryViewById(R.id.img_video_time_start);
        imVideoDelete = queryViewById(R.id.img_video_delete, true);
        imbtnVideoRecord = queryViewById(R.id.img_record_video_start, true);
        imMoreVedio = queryViewById(R.id.img_more_video, true);
        imPlayVedio = queryViewById(R.id.img_play_video, true);

        mScreenWidth=getWindowManager().getDefaultDisplay().getWidth();
        RelativeLayout.LayoutParams layoutParam = ( RelativeLayout.LayoutParams) surfaceVedioView.getLayoutParams();
        // 高：宽 4 : 3
        layoutParam.height = mScreenWidth / 3 * 4;
        // 隐藏多少dp才能让屏幕显示正常像素
        layoutParam.topMargin = -(mScreenWidth / 3 * 4 - mScreenWidth - DensityUtil.dip2px(this, 44));
        surfaceVedioView.setLayoutParams(layoutParam);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imSeekbarBg.getLayoutParams();
        layoutParams.leftMargin = mScreenWidth / RECORD_VIDEO_TIME_MAX * RECORD_VIDEO_TIME_MIN;
        imSeekbarBg.setLayoutParams(layoutParams);

        imbtnVideoRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startRecord();

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        prepareWork();
    }

    private void prepareWork(){
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

        // 安装一个SurfaceHolder.Callback
        mHolder = surfaceVedioView.getHolder();
        mHolder.addCallback(this);
        // 针对低于3.0的Android
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

//        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        readVideoPreferences();
    }

    /**
     * 设置摄像头参数
     */
    private void readVideoPreferences() {

        boolean videoQualityHigh = true;
        // 设置视频质量。
        Intent intent = getIntent();
        if (intent.hasExtra(MediaStore.EXTRA_VIDEO_QUALITY)) {
            int extraVideoQuality = intent.getIntExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            videoQualityHigh = (extraVideoQuality > 0);
        }

      //  videoQualityHigh = false;
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
     * 开始录制视频
     */
    private void startRecord() {
        try {
           // vedioPath = Ppath + System.currentTimeMillis() + ".mp4";
            vedioPath= FileUtil.getCacheFolder()+File.separator+System.currentTimeMillis() + ".mp4";
            mCamera.unlock();

            mMediaRecorder = new MediaRecorder();// 创建mediaRecorder对象
            mMediaRecorder.setCamera(mCamera);
            // 设置录制视频源为Camera(相机)
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setProfile(mProfile);
            // mMediaRecorder.setVideoSize(560,560);//设置视频大小（分辨率）
            mMediaRecorder.setVideoEncodingBitRate(1024 * 1024);// 设置视频一次写多少字节(可调节视频空间大小)
            // 最大期限
            mMediaRecorder.setMaxDuration(35 * 1000);
            // 第4步:指定输出文件 ， 设置视频文件输出的路径

            mMediaRecorder.setOutputFile(vedioPath);

            mMediaRecorder.setPreviewDisplay(mHolder.getSurface());

            // // 设置保存录像方向
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                if (cameraPosition == 1) {
                    //由于不支持竖屏录制，后置摄像头需要把视频顺时针旋转90度、、但是视频本身在电脑上看还是逆时针旋转了90度
                    mMediaRecorder.setOrientationHint(90);
                } else if (cameraPosition == 0) {
                    //由于不支持竖屏录制，前置摄像头需要把视频顺时针旋转270度、、而前置摄像头在电脑上则是顺时针旋转了90度
                    mMediaRecorder.setOrientationHint(270);
                }
            }

            mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {

                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {

                }
            });

            mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {

                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                     recodError();
                }
            });

            // 第6步:根据以上配置准备MediaRecorder
            mMediaRecorder.prepare();
          //  mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            recodError();
        } catch (IOException e) {
            e.printStackTrace();
            recodError();
        } catch (RuntimeException e) {
            e.printStackTrace();
            recodError();
        }
    }



    /**
     * 结束录制
     */
    private void stopRecord() {


        if (mMediaRecorder != null) {
            try {
                // 停止录像，释放camera
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setOnInfoListener(null);
                mMediaRecorder.stop();
                // 清除recorder配置
                mMediaRecorder.reset();
                // 释放recorder对象
                mMediaRecorder.release();
                mMediaRecorder = null;
                // 没超过3秒就删除录制所有数据

            } catch (Exception e) {
              e.printStackTrace();
            }
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

    /**
     * 设置摄像头参数
     */
    private void initCameraParameters() {
        // 初始化摄像头参数
        mParameters = mCamera.getParameters();
        mParameters.setPreviewSize(mProfile.videoFrameWidth, mProfile.videoFrameHeight);
        mParameters.setPreviewFrameRate(mProfile.videoFrameRate);
        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        // 设置白平衡参数。
        mParameters.setWhiteBalance("auto");
        // 参数设置颜色效果。
        mParameters.setColorEffect("none");

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

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    public void doInitData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_record_back:
                break;
            case R.id.img_switch_camera_flashlight:
                break;
            default:
                break;
        }
    }

    /**
     * 异常处理
     *
     */
    private void recodError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("该设备暂不支持视频录制");
        builder.setTitle("出错啦");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }

        });
        builder.create().show();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
