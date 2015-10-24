package com.org.photodemo.ui.album.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.org.photodemo.ui.album.bean.GalleryEntity;

import com.org.photodemo.ui.album.bean.PhotoGridItemEntity;
import com.org.photodemo.util.DateUtil;
import com.org.photodemo.util.MyLogger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;


/**
 * Created by GZK on 2015/10/21.
 */

public class PhotoSelectUtil {

    static MyLogger myLogger = MyLogger.getMyLogger();
    //系统相册
    public static final String SYSTEM_CAMERA_FOLDER = "DCIM/CAMERA";

    /**
     * 查询 所有相册问价夹 按照数量多少排序
     *
     * @param context contex
     * @return list
     */
    public static ArrayList<GalleryEntity> queryALLGalleryList(Context context) {
        ArrayList<GalleryEntity> galleryList = new ArrayList<GalleryEntity>();

        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.ORIENTATION, "COUNT(1) AS count"};
        String selection = "0==0) GROUP BY (" + MediaStore.Images.Media.BUCKET_ID;
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED;

        Cursor cur = context.getApplicationContext().getContentResolver().query(MediaStore
                .Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, sortOrder);
        if (cur != null && cur.moveToFirst()) {
            int idColumn = cur.getColumnIndex(MediaStore.Images.Media._ID);
            int imageIdColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
            int bucketIdColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int bucketNameColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int countColumn = cur.getColumnIndex("count");

            do {
                int id = cur.getInt(idColumn);
                String imagePath = cur.getString(imageIdColumn);
                int bucketId = cur.getInt(bucketIdColumn);
                String bucketName = cur.getString(bucketNameColumn);
                int orientation = cur.getInt(cur.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
                if (TextUtils.isEmpty(bucketName)) {
                    Uri mUri = Uri.parse("content://media/external/images/media");
                    Uri mImageUri = Uri.withAppendedPath(mUri, "" + id);
                    int deleteNUM = context.getApplicationContext().getContentResolver().delete(mImageUri, null, null);
                    myLogger.d(("imagePath:" + imagePath + ",deleteNUM:" + deleteNUM + ",mImageUri:" + mImageUri));
                    continue;
                }
                int count = cur.getInt(countColumn);
                GalleryEntity gallery = new GalleryEntity();
                gallery.setId(id);
                gallery.setImagePath(imagePath);
                gallery.setBucketId(bucketId);
                gallery.setBucketName(bucketName);

                gallery.setCount(count);
                gallery.setOrientation(orientation);
                galleryList.add(gallery);
            } while (cur.moveToNext());
            cur.close();
        }
        GalleryComparator comparator = new GalleryComparator();
        Collections.sort(galleryList, comparator);
        return galleryList;
    }



    /**
     * 查找相应相册里面的图片
     *
     * @param context  contex
     * @param bucketId id
     * @return list
     */

    public static ArrayList<PhotoGridItemEntity> queryPhotoByBUCKETID(Context context, int bucketId) {
        ArrayList<PhotoGridItemEntity> result = new ArrayList<PhotoGridItemEntity>();
        int section = 1;
        //需要查询的字段 date(date_added,'unixepoch','localtime')
        String[] projection = new String[]{MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_MODIFIED, MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.ORIENTATION};
        //查询条件
        String selection = MediaStore.Images.Media.BUCKET_ID + " = " + bucketId;
        //排序语句
//        String sortString = MediaStore.Images.Media.DATE_MODIFIED + " desc";
        String sortString = MediaStore.Images.Media.DATE_MODIFIED + " asc";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, selection, null, sortString);

        //boolean isAddCamera =false;
        String curdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));

        if (cursor != null) {
            cursor.getCount();
            while (cursor.moveToNext()) {
                long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                File curFile = new File(path);
                if ((!curFile.exists() || (curFile.length() <= 0))) {
                    Uri mUri = Uri.parse("content://media/external/images/media");
                    int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                    Uri mImageUri = Uri.withAppendedPath(mUri, "" + id);
                    int deleteNUM = context.getContentResolver().delete(mImageUri, null, null);
                    myLogger.d("image_path:" + path + ",deleteNUM:" + deleteNUM + ",mImageUri:" + mImageUri);
                    continue;
                }
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                String time = DateUtil.getDate(date);
                int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
                PhotoGridItemEntity item = new PhotoGridItemEntity(id, time, orientation, path);

                result.add(item);
            }
        }
        cursor.close();


        return result;
    }

    /**
     * 查询图库里面的图片按日期排序
     *
     * @param context context
     * @return list
     */
    public static ArrayList<PhotoGridItemEntity> queryALLPhoto(Context context) {
        Map<String, Integer> sectionMap = new HashMap<String, Integer>();
        ArrayList<PhotoGridItemEntity> result = new ArrayList<PhotoGridItemEntity>();
        int section = 1;
        //需要查询的字段 date(date_added,'unixepoch','localtime')
        String[] projection = new String[]{MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_MODIFIED, MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.ORIENTATION};
        //查询条件
        //       String selection = MediaStore.Images.Media.DATA +
        // " like '%" + PuTaoConstants.PAIAPI_PHOTOS_FOLDER + "%'";
        // + "%' or " + MediaStore.Images.Media.DATA + " like '%DCIM%' ";
        String selection = MediaStore.Images.Media.DATA + " like '%" + SYSTEM_CAMERA_FOLDER + "%'";

        //排序语句
        String sortString = MediaStore.Images.Media.DATE_MODIFIED + " desc";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, selection, null, sortString);

        //boolean isAddCamera =false;
        String curdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));

        if (cursor != null) {
            cursor.getCount();
            while (cursor.moveToNext()) {
                long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                File curFile = new File(path);
                if ((!curFile.exists() || (curFile.length() <= 0))) {
                    Uri mUri = Uri.parse("content://media/external/images/media");
                    int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                    Uri mImageUri = Uri.withAppendedPath(mUri, "" + id);
                    int deleteNUM = context.getContentResolver().delete(mImageUri, null, null);
                    myLogger.d("chen++++id:" + id + ",image_path:" + path + ",deleteNUM:"
                            + deleteNUM + ",mImageUri:" + mImageUri);
                    continue;
                }
                int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));

                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                String time = DateUtil.getDate(date);
                PhotoGridItemEntity item = new PhotoGridItemEntity(id, time, orientation, path);
                result.add(item);
            }
            cursor.close();
        }
        return result;
    }


}
