package com.yj.shopapp.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by LK on 2017/12/5.
 *
 * @author LK
 */

public class MyBanner extends com.youth.banner.Banner {
    private float lastX;
    private float lastY;

    public void setRefreshLayout(SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    private SwipeRefreshLayout refreshLayout;

    public MyBanner(Context context) {
        super(context);
    }

    public MyBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        float x = ev.getRawX();
        float y = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                refreshLayout.setEnabled(false);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getRawY();
                float deltaY = moveY - lastY;
                float deltaX = moveX - lastX;
                if (Math.tan(Math.abs(deltaY) / Math.abs(deltaX)) < Math.tan(45.0)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    refreshLayout.setEnabled(false);
                } else {
                    refreshLayout.setEnabled(true);
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                refreshLayout.setEnabled(true);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
