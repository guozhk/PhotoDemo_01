package com.org.photodemo.ui.recordvdeo;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import android.widget.ImageView;
import com.org.photodemo.R;
import com.org.photodemo.base.BaseActivity;
import android.widget.RelativeLayout.LayoutParams;
/**
 * Created by GZK on 2015/10/23.
 */
public class VideoPlayerActivity extends BaseActivity{
    /**传过来的路径*/
    private String path;
    /**视频控件*/
    private VideoView videoview;
    /**播放按钮*/
    private ImageView imgStart;
    /**容器*/
    private RelativeLayout relative;
    @Override
    public int doGetContentViewId() {
        return R.layout.activity_video_palyer;
    }

    @Override
    public void doInitSubView(View view) {
        videoview=queryViewById(R.id.videoView);
        imgStart=queryViewById(R.id.img_start);
        relative=queryViewById(R.id.relative,true);

        int width = getWindowManager().getDefaultDisplay().getWidth();
        LayoutParams layoutParam = (LayoutParams) videoview.getLayoutParams();
        // 高：宽 4 : 3
        layoutParam.height = width / 3 * 4;
        // 隐藏多少dp才能让屏幕显示正常像素
        //layoutParam.topMargin = -(width / 3 * 4 - width - DisplayUtil.dip2px(VideoNewActivity.this, 44));
        videoview.setLayoutParams(layoutParam);


    }

    @Override
    public void doInitData() {
        if (getIntent().getExtras()!=null) {
            path = getIntent().getExtras().getString("path");
        }
        if(!TextUtils.isEmpty(path)){
            videoview.setVideoPath(path);
            videoview.requestFocus();
        }


    }

    @Override
    public void onClick(View v) {
if(v.getId()==R.id.relative){
    if (videoview.isPlaying()) {
        videoview.pause();
        imgStart.setVisibility(View.VISIBLE);
    } else {
        videoview.start();
        imgStart.setVisibility(View.GONE);
    }
}
    }
}
