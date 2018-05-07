package com.yj.shopapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;

/**
 * Created by LK on 2018/4/18.
 *
 * @author LK
 */
public class XRecyclerView extends RecyclerView {
    public XRecyclerView(Context context) {
        super(context, null);
        init();
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        addOnScrollListener(new ImagAutoLoadScrollListrner());
    }

    public class ImagAutoLoadScrollListrner extends OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case SCROLL_STATE_IDLE:
                    try {
                        if (getContext() != null) Glide.with(getContext()).resumeRequests();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SCROLL_STATE_DRAGGING:
                case SCROLL_STATE_SETTLING:
                    try {
                        if (getContext() != null) Glide.with(getContext()).pauseAllRequests();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
