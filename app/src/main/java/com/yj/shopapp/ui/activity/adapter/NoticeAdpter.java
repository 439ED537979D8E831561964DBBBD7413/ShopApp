package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.BuGood;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.CommonUtils;

/**
 * Created by LK on 2018/3/29.
 *
 * @author LK
 */

public class NoticeAdpter extends CommonAdapter<BuGood> {
    public NoticeAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.noticeitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BuGood good = list.get(position);
        holder.getTextView(R.id.storename).setText(CommonUtils.getStarString(good.getShopname(), 1, 2));
        holder.getTextView(R.id.details).setText(String.format("兑换了%1$s%2$s%3$s", good.getNum(), good.getUnit(), good.getName()));
        holder.getTextView(R.id.time).setText(good.getDate());
    }
}
