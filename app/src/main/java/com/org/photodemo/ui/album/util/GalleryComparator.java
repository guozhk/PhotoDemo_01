package com.org.photodemo.ui.album.util;


import com.org.photodemo.ui.album.bean.GalleryEntity;

import java.util.Comparator;

/**
 * Created by JIDONGDONG on 2015/7/2.
 */
public class GalleryComparator implements Comparator<GalleryEntity> {
    @Override
    public int compare(GalleryEntity galleryEntity, GalleryEntity t1) {
        if (galleryEntity.getCount() > t1.getCount()) {
            return -1;
        } else if (galleryEntity.getCount() < t1.getCount()) {
            return 1;
        } else {
            return 0;
        }
    }
}
