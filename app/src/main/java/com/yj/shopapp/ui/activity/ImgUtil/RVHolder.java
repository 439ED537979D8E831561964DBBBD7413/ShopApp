package com.yj.shopapp.ui.activity.ImgUtil;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lk on 2017/8/7.
 */

public class RVHolder extends RecyclerView.ViewHolder {
    private ViewHolder viewHolder;

    public RVHolder(View itemView) {
        super(itemView);
        viewHolder = ViewHolder.getViewHolder(itemView);
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

}

