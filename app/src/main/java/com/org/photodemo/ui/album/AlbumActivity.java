package com.org.photodemo.ui.album;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;

import com.org.photodemo.R;
import com.org.photodemo.base.BaseActivity;
import com.org.photodemo.ui.album.bean.GalleryEntity;
import com.org.photodemo.ui.album.util.PhotoSelectUtil;

import java.util.List;


public class AlbumActivity extends BaseActivity {
    private ListView lvAlbum;
    private AlbumAdapter mAlbumAdapter;


    @Override
    public void doInitSubView(View view) {
        lvAlbum=queryViewById(R.id.lv_album);

    }

    @Override
    public void doInitData() {

        List<GalleryEntity> galleryEntityList= PhotoSelectUtil.queryALLGalleryList(AlbumActivity.this);
        mAlbumAdapter= new AlbumAdapter(AlbumActivity.this,galleryEntityList);
        lvAlbum.setAdapter(mAlbumAdapter);
        lvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               int bucketId=mAlbumAdapter.getmGalleryList().get(position).getBucketId();
                Intent albumInfoIntent=new Intent(AlbumActivity.this,AlbumInfoActivity.class);
                albumInfoIntent.putExtra("bucketId",bucketId);
                startActivity(albumInfoIntent);
            }
        });

    }

    @Override
    public int doGetContentViewId() {
        return R.layout.activity_album;
    }

    @Override
    public void onClick(View v) {

    }
}
