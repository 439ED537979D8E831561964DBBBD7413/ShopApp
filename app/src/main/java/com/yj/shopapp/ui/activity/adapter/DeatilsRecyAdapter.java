package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.RecordRedPack;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

import java.util.List;

/**
 * Created by LK on 2017/10/23.
 */

public class DeatilsRecyAdapter extends CommonAdapter {
    public DeatilsRecyAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.redails_activity;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecordRedPack redPack = (RecordRedPack) list.get(position);
        holder.getTextView(R.id.change_time).setText(DateUtils.times(redPack.getChangetime()));
        holder.getTextView(R.id.shopname).setText(redPack.getShopname());
        holder.getTextView(R.id.money).setText("+" + redPack.getReward()+"元");
        int index = Integer.parseInt(redPack.getStatus());
        holder.getTextView(R.id.Speed_of_progress).setText(context.getResources().getStringArray(R.array.schedule)[index-1]);
    }


}
