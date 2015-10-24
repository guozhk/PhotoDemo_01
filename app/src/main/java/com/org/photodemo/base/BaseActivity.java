package com.org.photodemo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by GZK on 2015/10/19.
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener, IActivityInterface {
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        doBefore();
        View contentView = getLayoutInflater().inflate(doGetContentViewId(), null);
        setContentView(contentView);
        doInitSubView(contentView);
        doInitData();
    }


    protected void init() {
        mContext = this;
    }

    /**
     * 预留方法
     */
    protected void doBefore() {
    }

    protected <T extends View> T queryViewById(int viewId) {
        if (viewId > 0 && mContext != null) {
            return (T) ((Activity) mContext).findViewById(viewId);
        }
        return null;
    }

    protected <T extends View> T queryViewById(View parentView, int viewId) {
        if (viewId > 0 && parentView != null) {
            return (T) parentView.findViewById(viewId);
        }
        return null;
    }

    /**
     * 查找View并且添加点击事件监听
     *
     * @param viewId viewid
     * @param clicklistener listener
     * @param <T> T
     * @return view
     */
    protected <T extends View> T queryViewById(int viewId, boolean clicklistener) {
        if (viewId > 0) {
            T view = (T) findViewById(viewId);
            if (clicklistener && view != null)
                addOnClickListener(view);
            return view;
        }
        return null;
    }

    /**
     * set OnClickListener for every View
     *
     * @param views view
     */
    protected void addOnClickListener(View... views) {
        View.OnClickListener listener = (View.OnClickListener) ((Activity) mContext);
        if (listener != null)
            for (int i = 0; i < views.length; i++)
                views[i].setOnClickListener(listener);
    }

    /**
     * 根据资源Id设置当前资源ID对应的View点击事件监听
     *
     * @param resIds id
     */
    protected void addOnClickListener(int... resIds) {
        View.OnClickListener listener = (View.OnClickListener) ((Activity) mContext);
        if (listener != null) {
            for (int i = 0; i < resIds.length; i++) {
                queryViewById(resIds[i], true);
            }
        }
    }


}
