package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.IntegralChange;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

/**
 * From ShopApp
 * Created by wxq on 17/4/5.
 * <p>
 * Beautiful Life ～
 */

public class IntegralChangeAdapter extends CommonAdapter<IntegralChange.DataBean> {


    public IntegralChangeAdapter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_integral_change;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IntegralChange.DataBean detail = list.get(position);
        Glide.with(context).load(detail.getImgurl()).into(holder.getImageView(R.id.shopimag));
        holder.getTextView(R.id.shopname).setText(detail.getName());
        holder.getTextView(R.id.addtiem).setText(DateUtils.timet(detail.getAddtime(), "yyyy-MM-dd  HH:mm"));
        holder.getTextView(R.id.integral).setText("-" + detail.getIntegral());
        holder.getTextView(R.id.shopsum).setText(String.format("数量：%s件", detail.getNum()));

    }
}
