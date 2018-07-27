package com.yj.shopapp.ui.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.wbeen.SPlist;

/**
 * Created by jm on 2016/4/25.
 */
public class SalesAdapter extends Common2Adapter<SPlist> {
    public SalesAdapter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.wactivity_sales_item;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SPlist sPlist = list.get(position);
        holder.getTextView(R.id.orderName).setText(sPlist.getName());
        holder.getTextView(R.id.orderStatus).setText(Contants.SalesStatusString[Integer.parseInt(sPlist.getStatus())]);
        holder.getTextView(R.id.orderStatus).setTextColor(context.getResources().getColor(Contants.OrderStatusColor[Integer.parseInt(sPlist.getStatus())]));
        holder.getTextView(R.id.startDate).setText(DateUtils.timed(sPlist.getTime1()) + " - " + DateUtils.timed(sPlist.getTime2()));
        holder.getTextView(R.id.orderContent).setText(Html.fromHtml("促销价:" + "<font color=#FE3000>" + "￥" + sPlist.getUnitprice() + "</font>"));
        if (sPlist.getImgurl() != null) {
            Glide.with(context).load(sPlist.getImgurl()).into(holder.getSimpleDraweeView(R.id.simpleDraweeView));
        }
    }

}
