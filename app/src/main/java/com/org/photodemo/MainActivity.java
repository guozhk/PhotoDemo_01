package com.org.photodemo;

import android.content.Intent;

import android.net.Uri;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.org.photodemo.base.BaseActivity;

import com.org.photodemo.record.VideoRecordActivity;
import com.org.photodemo.ui.recordvdeo.RecorderVedioActivity;
import com.org.photodemo.ui.album.AlbumActivity;
import com.org.photodemo.ui.album.AlbumInfoAdapter;
import com.org.photodemo.ui.album.bean.PhotoGridItemEntity;
import com.org.photodemo.util.MyLogger;
import com.org.photodemo.util.PicSelectedUtil;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends BaseActivity {
    private Button btnAlbum;
    private Button btnTakePhonto;
    private Button btnRecordVedio;

    private GridView gvSelectedPic;

    private Uri mTokePhoneUri=null;

    MyLogger myLogger=MyLogger.getMyLogger();

    private final List<PhotoGridItemEntity> mInitPhotoGridList = new ArrayList<PhotoGridItemEntity>();

    private AlbumInfoAdapter mAlbumInfoAdapter;



    @Override
    public int doGetContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void doInitSubView(View view) {
        btnAlbum = queryViewById(R.id.btn_album, true);
        btnTakePhonto = queryViewById(R.id.btn_take_photo, true);
        btnRecordVedio = queryViewById(R.id.btn_record_vedio, true);

        gvSelectedPic = queryViewById(R.id.gv_selected_pic);



    }

    @Override
    public void doInitData() {
        mAlbumInfoAdapter = new AlbumInfoAdapter(MainActivity.this, mInitPhotoGridList);
        gvSelectedPic.setAdapter(mAlbumInfoAdapter);
    }


    private void  updateSelectedPic(  List<PhotoGridItemEntity> mPhotoGridList){
        if (mAlbumInfoAdapter != null && mInitPhotoGridList != null) {
            mInitPhotoGridList.clear();
            mInitPhotoGridList.addAll(mPhotoGridList);
            mAlbumInfoAdapter.notifyDataSetChanged();
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_album:

                Intent albumIntent = new Intent(MainActivity.this, AlbumActivity.class);
                startActivity(albumIntent);
                break;
            case R.id.btn_take_photo:
                mTokePhoneUri=PicSelectedUtil.doTakePhoto(MainActivity.this,MainActivity.this,PicSelectedUtil.requestcode_camera);
                break;
            case R.id.btn_record_vedio:
                //
                // Intent recordVedioIntent = new Intent(MainActivity.this, RecorderVedioActivity.class);
                Intent recordVedioIntent = new Intent(MainActivity.this, VideoRecordActivity.class);

                startActivity(recordVedioIntent);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myLogger.d("data:"+data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case PicSelectedUtil.requestcode_camera:
                    Uri uri = null;
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                    }
                    if (uri == null && mTokePhoneUri != null) {
                        uri = mTokePhoneUri;
                    }

                    if (uri != null) {
                        PicSelectedUtil.startPhotoZoom(this, this,
                                uri, 200, 200, PicSelectedUtil.requestcode_zoom);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
