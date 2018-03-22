package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.IntegralDetail;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

/**
 * From ShopApp
 * Created by wxq on 17/4/5.
 * <p>
 * Beautiful Life ～
 */

public class IntegralAdapter extends CommonAdapter<IntegralDetail.DataBean> {

    public IntegralAdapter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_integral_detail;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IntegralDetail.DataBean detail = list.get(position);
        Glide.with(context).load(detail.getImgurl()).into(holder.getImageView(R.id.shopimag));
        holder.getTextView(R.id.shopname).setText(detail.getName());
        holder.getTextView(R.id.addtiem).setText(DateUtils.timet(detail.getAddtime()));
        holder.getTextView(R.id.integral).setText(detail.getIntegral() + "积分");
        holder.getTextView(R.id.status).setText(Contants.StatusList[Integer.parseInt(detail.getStatus())]);
        holder.getTextView(R.id.shopsum).setText("数量：" + detail.getNum());
        holder.getTextView(R.id.sumintegral).setText("总积分：" + detail.getSumintegral());

    }


}
