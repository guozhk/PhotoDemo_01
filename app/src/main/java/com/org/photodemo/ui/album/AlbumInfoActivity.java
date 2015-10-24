package com.org.photodemo.ui.album;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.org.photodemo.R;
import com.org.photodemo.base.BaseActivity;
import com.org.photodemo.ui.album.bean.PhotoGridItemEntity;
import com.org.photodemo.ui.album.util.PhotoSelectUtil;

import java.util.List;

/**
 * 显示相册图片
 */
public class AlbumInfoActivity extends BaseActivity {
    private GridView gvAlbumPhoto;
    private AlbumInfoAdapter mAlbumInfoAdapter;
    private List<PhotoGridItemEntity> mPhotoGridList;

    private int bucketId;//相册ID

    @Override
    protected void doBefore() {
        super.doBefore();
        Intent intent= this.getIntent();
        bucketId=intent.getIntExtra("bucketId", 0);
        if(bucketId==0){
            Toast.makeText(this,"相册参数错误",Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }

    }

    @Override
    public int doGetContentViewId() {
        return R.layout.activity_album_info;
    }

    @Override
    public void doInitSubView(View view) {
        gvAlbumPhoto = queryViewById(R.id.gv_album_photo);

    }

    @Override
    public void doInitData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPhotoGridList= PhotoSelectUtil.queryPhotoByBUCKETID(AlbumInfoActivity.this, bucketId);
                Message msg=new Message();
                msg.what=0;
                mHandler.sendMessageDelayed(msg,500);
            }
        }).start();
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                if( msg.what==0){
                    mAlbumInfoAdapter=new AlbumInfoAdapter(AlbumInfoActivity.this,mPhotoGridList);
                    gvAlbumPhoto.setAdapter(mAlbumInfoAdapter);
                }
        }
    };

    @Override
    public void onClick(View v) {

    }


}
