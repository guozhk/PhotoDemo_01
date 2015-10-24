package com.org.photodemo.base;

import android.view.View;

/**
 * Created by GZK on 2015/10/19.
 */
public interface IActivityInterface {
    /**
     * 获取视图
     *
     * @return int
     */
    public int doGetContentViewId();

    /**
     * 初始化视图 控件
     *
     * @param view view
     */
    public void doInitSubView(View view);

    /**
     * 初始化数据
     */
    public void doInitData();
}
