package com.org.photodemo.camera;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.view.SurfaceHolder;
import android.widget.ListView;

import com.org.photodemo.R;
import com.org.photodemo.util.FileUtil;
import com.org.photodemo.util.MyLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gzhenkai on 15/10/25.
 */
public class CustomCameraUtil {

    MyLogger myLogger=MyLogger.getMyLogger();

    private static final int ORIGINAL_DATE=0;//原始数据
    private static final int ZIPORSCALE_DATE=1;//压缩 放缩数据
    private static final int JPG_DATE=2;//jpg数据
    //照相机实例
    private Camera mCamera;
    //
    private CamcorderProfile mProfile;
    private Camera.Parameters mParameters;
    //录制对象
    private MediaRecorder mMediaRecorder;
    //视频输出路径
    private String vedioPath;
    //显示视频 holder
    private SurfaceHolder mHolder;
    //前置 后置摄像头
    private int cameraPosition;

    CustomCameraInterface mCustomCameraInterface;




    public CustomCameraUtil(SurfaceHolder mHolder){
        this.mHolder=mHolder;
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
     * 切换摄像头
     */

    private void switchCamera() {
        // 切换前后摄像头
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
            if (cameraPosition == 1) {
                // 现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                    // CAMERA_FACING_BACK后置
                    // 前置摄像头时必须关闭闪光灯，不然会报错
                    if (mParameters != null) {
                        if (mParameters.getFlashMode() != null && mParameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            img_flashlight.setImageResource(R.drawable.img_video_new_flashlight_close);
                        }
                        if (mCamera != null) {
                            mCamera.setParameters(mParameters);
                        }
                    }

                    // 释放Camera
                    releaseCamera();

                    // 打开当前选中的摄像头
                    mCamera = Camera.open(i);
                    mCamera.setDisplayOrientation(90);
                    mCamera.lock();

                    // 通过surfaceview显示取景画面
                    setStartPreview(mHolder);

                    mCustomCameraInterface();

                    cameraPosition = 0;

                    break;
                }
            } else {
                // 现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                    // CAMERA_FACING_BACK后置
                    // 释放Camera
                    releaseCamera();
                    // 打开当前选中的摄像头
                    mCamera = Camera.open(i);
                    mCamera.setDisplayOrientation(90);
                    mCamera.lock();

                    // 通过surfaceview显示取景画面
                    setStartPreview(mHolder);

                    cameraPosition = 1;

                    break;
                }
            }

        }
    }


    /**
     * 关闭打开闪光灯
     */
    private void switchFlashLight(){

        if (cameraPosition != 0) {// 前置摄像头的时候不能切换闪光灯
            if (mParameters != null) {
                if (mParameters.getFlashMode() != null && mParameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    img_flashlight.setImageResource(R.drawable.img_video_new_flashlight_close);
                } else if (mParameters.getFlashMode() != null && mParameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)) {
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    img_flashlight.setImageResource(R.drawable.img_video_new_flashlight_open);
                }
                if (mCamera != null) {
                    mCamera.setParameters(mParameters);
                }
            }
        }

    }





    /**
     * 释放Camera
     */
    protected void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();// 停掉原来摄像头的预览
            mCamera.release();
            mCamera = null;
        }
    }


    public Camera getmCamera(){
        return mCamera;
    }


    /**
     * 拍照  参数暂时这样处理
     */
    protected void takePic(){
        //TODO
        mCamera.takePicture(null,null,null,new CustomPictureCallback(JPG_DATE));
    }

    /**
     * 自定拍照后回调函数
     */
    class  CustomPictureCallback implements Camera.PictureCallback{
        private int picturCallbackType;
        public CustomPictureCallback(int type){
            this.picturCallbackType=type;
        }
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            myLogger.e("picturCallbackType: " +picturCallbackType);
            if(picturCallbackType==ORIGINAL_DATE){
               //TODO
            }else if(picturCallbackType==ZIPORSCALE_DATE){
                //TODO
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
            //调用摄像头
            camera.startPreview();
        }
    }


    /**
     * 开始录制
     */
    public void startRecord() {
        try {
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

          /*  mMediaRecorder.setOnInfoListener(new MOnInfoListener() {

                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {

                }
            });

            mMediaRecorder.setOnErrorListener(new OnErrorListener() {

                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    recodError();
                }
            });*/

            // 第6步:根据以上配置准备MediaRecorder

            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            mCustomCameraInterface.cameraError();
        } catch (IOException e) {
            e.printStackTrace();
            mCustomCameraInterface.cameraError();
        } catch (RuntimeException e) {
            e.printStackTrace();
            mCustomCameraInterface.cameraError();
        }

    }



    /**
     * 结束录制
     */
    public void stopRecord() {
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
                mCustomCameraInterface.cameraError();
            }
        }
    }



    public interface CustomCameraInterface{
        void cameraError();
        void cameraSwitch();
        void cameraStartPreview();

    }


    public CustomCameraInterface getmCustomCameraInterface() {
        return mCustomCameraInterface;
    }

    public void setmCustomCameraInterface(CustomCameraInterface mCustomCameraInterface) {
        this.mCustomCameraInterface = mCustomCameraInterface;
    }






}
