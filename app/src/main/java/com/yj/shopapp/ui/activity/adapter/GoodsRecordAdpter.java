package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.text.Html;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.OrderRecord;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2018/3/19.
 *
 * @author LK
 */

public class GoodsRecordAdpter extends Common2Adapter<OrderRecord> {


    public GoodsRecordAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.view_goodrecord;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderRecord record = list.get(position);
        Glide.with(context).load(record.getImgurl()).into(holder.getImageView(R.id.shopimag));
        holder.getTextView(R.id.shopname).setText(record.getName());
        holder.getTextView(R.id.shopnumber).setText(String.format("条    码：%s", record.getItemnumber()));
        holder.getTextView(R.id.shopsum).setText(String.format("总数量：%1$s%2$s", record.getNum(), record.getUnit()));
        holder.getTextView(R.id.shopspec).setText(String.format("规    格：%1$s/%2$s", record.getSpecs(), record.getUnit()));
        holder.getTextView(R.id.shoptotal).setText(Html.fromHtml("总价：" + "<font color=red>" + "￥" + record.getMoney() + "</font>"));
    }

}