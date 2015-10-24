package com.org.photodemo.ui.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.org.photodemo.R;
import com.org.photodemo.ui.album.bean.GalleryEntity;
import com.org.photodemo.util.ImageUtil;
import com.org.photodemo.util.MyLogger;

import java.io.File;
import java.util.List;

/**
 * Created by GZK on 2015/10/21.
 */
public class AlbumAdapter extends BaseAdapter {
    private Context mContext;
    private List<GalleryEntity> mGalleryList;
    private LayoutInflater mInflater;

    public AlbumAdapter(Context mContext, List<GalleryEntity> mGalleryList) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mGalleryList = mGalleryList;
    }

    public List<GalleryEntity> getmGalleryList() {
        return mGalleryList;
    }


    @Override
    public int getCount() {
        return mGalleryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGalleryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView =mInflater.inflate(R.layout.item_album, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.gallery_image);
            holder.textView = (TextView) convertView.findViewById(R.id.gallery_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GalleryEntity item = mGalleryList.get(position);

        MyLogger.getMyLogger().e("item.getImagePath():" + item.getImagePath());
       /* Bitmap bitmap=ImageUtil.originalImg(new File(item.getImagePath()));
        MyLogger.getMyLogger().e(bitmap);
         holder.imageView.setImageBitmap(bitmap);*/

        ImageLoader.getInstance().displayImage("file://"+item.getImagePath(), holder.imageView);

        String name = item.getBucketName();
        if (item.getImagePath().toUpperCase().contains("DCIM/CAMERA")) {
            name ="系统相册";
        }
        if (name.length() > 7) {
            name = name.substring(0, 7) + "...";
        }
        holder.textView.setText(name + "(" + item.getCount() + ")");
        return convertView;
    }

    public class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }


}
