package com.org.photodemo.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.org.photodemo.R;
import com.org.photodemo.recordvdeo.MovieRecorderView;
import com.org.photodemo.ui.recordvdeo.VideoPlayerActivity;
import com.org.photodemo.util.MyLogger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;
public class RecorderVedioActivity extends Activity {


    private MovieRecorderView mRecorderView;
    private Button mShootBtn;
    private boolean isFinish = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_vedio);
        mRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
        mShootBtn = (Button) findViewById(R.id.shoot_button);

        mShootBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MyLogger.getMyLogger().e("ACTION_DOWN:");
                    mRecorderView.record(new MovieRecorderView.OnRecordFinishListener() {

                        @Override
                        public void onRecordFinish() {
                            handler.sendEmptyMessage(1);
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    MyLogger.getMyLogger().e("ACTION_UP:"+mRecorderView.getTimeCount());
                    if (mRecorderView.getTimeCount() > 1)
                        handler.sendEmptyMessage(1);
                    else {
                        if (mRecorderView.getVecordFile() != null)
                            mRecorderView.getVecordFile().delete();
                        mRecorderView.stop();
                        Toast.makeText(RecorderVedioActivity.this, "视频录制时间太短", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        isFinish = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = false;
        mRecorderView.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finishActivity();
        }
    };

    private void finishActivity() {
        MyLogger.getMyLogger().e("isFinish:"+isFinish);
        if (isFinish) {
            MyLogger.getMyLogger().e("isFinish:"+isFinish);

            mRecorderView.stop();
            //TODO
            //暂时注释掉
            Intent vedioPlayerIntent=new Intent(RecorderVedioActivity.this,VideoPlayerActivity.class);
            vedioPlayerIntent.putExtra("path",mRecorderView.getVecordFile().toString());
            startActivity(vedioPlayerIntent);
        }
    }

    /**
     * 录制完成回调
     *
     * @author liuyinjun
     *
     * @date 2015-2-9
     */
    public interface OnShootCompletionListener {
        public void OnShootSuccess(String path, int second);
        public void OnShootFailure();
    }
}
