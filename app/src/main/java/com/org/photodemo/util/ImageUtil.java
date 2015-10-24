package com.org.photodemo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;

/**
 * Created by GZK on 2015/10/14.
 */
public class ImageUtil {

    private static final int imgDefaultWidth = 150;
    private static final int imgDefaultHeight = 150;

    /**
     * 描述：获取原图
     *
     * @param file File对象
     * @return Bitmap 图片
     */
    public static Bitmap originalImg(File file) {

        Bitmap resizeBmp = null;


        try {
            if (file.length() > 400 * 1024) { // 图片大小超过400K时压缩,以免出现OOM
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), options);
                int rate = options.outHeight / options.outWidth;
                options.outWidth -= 50;
                options.outHeight -= 50 * rate;
                //避免图片过大 强制 缩小
                if (options.outWidth > imgDefaultWidth) {
                    options.outWidth = imgDefaultWidth;
                }
                if (options.outHeight > imgDefaultHeight) {
                    options.outHeight = imgDefaultHeight;
                }

                resizeBmp = scaleImg(file, options.outWidth,
                        options.outHeight);

            } else {
                resizeBmp = BitmapFactory.decodeFile(file.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resizeBmp;
    }

    /**
     * 描述：缩放图片.压缩
     *
     * @param file      File对象
     * @param newWidth  新图片的宽
     * @param newHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap scaleImg(File file, int newWidth, int newHeight) {
        Bitmap resizeBmp = null;
        if (newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), opts);
        // inSampleSize=2表示图片宽高都为原来的二分之一，即图片为原来的四分之一
        // 缩放可以将像素点打薄
        int srcWidth = opts.outWidth; // 获取图片的原始宽度
        int srcHeight = opts.outHeight;// 获取图片原始高度
        int destWidth = 0;
        int destHeight = 0;
        // 缩放的比例
        double ratio = 0.0;
        if (srcWidth < newWidth || srcHeight < newHeight) {
            ratio = 0.0;
            destWidth = srcWidth;
            destHeight = srcHeight;
            // 按比例计算缩放后的图片大小
        } else if (srcWidth > newWidth) {
            ratio = (double) srcWidth / newWidth;
            destWidth = newWidth;
            destHeight = (int) (srcHeight / ratio);
        } else if (srcHeight > newHeight) {
            ratio = (double) srcHeight / newHeight;
            destHeight = newHeight;
            destWidth = (int) (srcWidth / ratio);
        }
        // 缩放的比例，缩放是很难按准备的比例进行缩放的，通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
        if (ratio > 1) {
            opts.inSampleSize = (int) ratio;
        } else {
            opts.inSampleSize = 1;
        }

        //避免图片过大 强制 缩小
        if (opts.outWidth > imgDefaultWidth) {
            opts.outWidth = imgDefaultWidth;
        }
        if (opts.outHeight > imgDefaultHeight) {
            opts.outHeight = imgDefaultHeight;
        }
        // 设置大小
        opts.outHeight = destHeight;
        opts.outWidth = destWidth;
        // 创建内存
        opts.inJustDecodeBounds = false;
        // 使图片不抖动
        opts.inDither = false;
        resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
        // 为了获取到预定的大小,还有做一步放大处理
        if (resizeBmp != null) {
            if (resizeBmp.getWidth() != newWidth
                    || resizeBmp.getHeight() != newHeight) {
                resizeBmp = scaleImg(resizeBmp, newWidth, newHeight);
            }
        }
        return resizeBmp;
    }


    /**
     * 描述：缩放图片,不压缩的缩放.
     *
     * @param bitmap    the bitmap
     * @param newWidth  新图片的宽
     * @param newHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap scaleImg(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
        }
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= 0 || height <= 0) {
            return null;
        }
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                true);
        return newBm;
    }

}
