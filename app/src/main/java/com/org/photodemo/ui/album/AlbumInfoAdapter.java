package com.org.photodemo.ui.album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.org.photodemo.R;
import com.org.photodemo.ui.album.bean.PhotoGridItemEntity;

import java.util.List;

/**
 * Created by GZK on 2015/10/22.
 */
public class AlbumInfoAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<PhotoGridItemEntity> mPhotoGridItemList;

    public AlbumInfoAdapter(Context mContext, List<PhotoGridItemEntity> mPhotoGridItemList) {
        this.mContext = mContext;
        this.mPhotoGridItemList = mPhotoGridItemList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mPhotoGridItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPhotoGridItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView =mInflater.inflate(R.layout.item_album_info, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_item_album_info);
           // holder.textView = (TextView) convertView.findViewById(R.id.gallery_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PhotoGridItemEntity photoGridItemEntity=mPhotoGridItemList.get(position);
        ImageLoader.getInstance().displayImage("file://"+photoGridItemEntity.getPath(),holder.imageView);



        return convertView;
    }

    public class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }

}
