package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Color;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2018/4/18.
 *
 * @author LK
 */
public class TopRecyAdpter extends Common2Adapter<Industry> {
    private int defItem = 0;

    public TopRecyAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.string;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == defItem) {
            holder.getView(R.id.bgView).setBackgroundColor(Color.parseColor("#EEEEEE"));
            holder.getTextView(R.id.comm_text).setText(list.get(position).getName());
            holder.getTextView(R.id.comm_text).setTextColor(context.getResources().getColor(R.color.color_01ABFF));
        } else {
            holder.getView(R.id.bgView).setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.getTextView(R.id.comm_text).setText(list.get(position).getName());
            holder.getTextView(R.id.comm_text).setTextColor(context.getResources().getColor(R.color.color333333));
        }

    }

    /**
     * 适配器中添加这个方法
     */
    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }
}
