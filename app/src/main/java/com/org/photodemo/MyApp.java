package com.org.photodemo;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.org.photodemo.util.FileUtil;

import java.io.File;

/**
 * Created by GZK on 2015/10/21.
 */
public class MyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        setImageLoaderConfig();
    }

    /**
     * 设置Imageloader配置
     */
    void setImageLoaderConfig() {
        try {

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MyApp.this)
                    .diskCache(new UnlimitedDiskCache(new File(FileUtil.getCacheFolder())))
                    .diskCacheSize(100 * 1024 * 1024)
                    .diskCacheFileCount(100)
                    .build();
            ImageLoader.getInstance().init(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
