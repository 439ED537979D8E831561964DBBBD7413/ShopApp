package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Color;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

import java.util.List;

/**
 * Created by LK on 2017/12/13.
 *
 * @author LK
 */

public class NewGoodRecyAdpter extends CommonAdapter<Industry> {
    private int mCurrent = -1;

    public NewGoodRecyAdpter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.laberview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mCurrent == position) {
            holder.getTextView(R.id.tabView).setTextColor(Color.parseColor("#2693FC"));
        } else {
            holder.getTextView(R.id.tabView).setTextColor(Color.parseColor("#7a7f85"));
        }
        holder.getTextView(R.id.tabView).setText(list.get(position).getName());
    }

    public void setTvColor(int position) {
        mCurrent = position;
        notifyDataSetChanged();
    }
}
