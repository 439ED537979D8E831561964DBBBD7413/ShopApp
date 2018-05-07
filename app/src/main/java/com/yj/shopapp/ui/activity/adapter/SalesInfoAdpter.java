package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.SalesInfo;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

/**
 * Created by LK on 2018/4/5.
 *
 * @author LK
 */

public class SalesInfoAdpter extends CommonAdapter<SalesInfo> {
    public SalesInfoAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.salesinfoitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SalesInfo info = list.get(position);
        holder.getTextView(R.id.stroename).setText(info.getShopname());
        holder.getTextView(R.id.itemTime).setText(DateUtils.timet(info.getAddtime(), "yyyy年MM月dd日  HH: mm"));
        holder.getTextView(R.id.itemdetails).setText(String.format("抢购%1$s%2$s%3$s", info.getItemcount(), info.getUnit(), info.getName()));
    }
}
