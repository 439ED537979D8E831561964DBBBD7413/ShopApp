package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.text.Html;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.OrderRecord;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

/**
 * Created by LK on 2018/3/19.
 *
 * @author LK
 */

public class GoodsRecordAdpter extends CommonAdapter<OrderRecord> {


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
        holder.getTextView(R.id.shopname).setText(record.getName());
        holder.getTextView(R.id.orderNumber).setText(String.format("订\t单\t号\t\t：%s", record.getOid()));
        holder.getTextView(R.id.shopnumber).setText(String.format("条\t\t\t\t码\t：%s", record.getItemnumber()));
        holder.getTextView(R.id.order_time).setText(String.format("下单时间：%s", DateUtils.timet(record.getAddtime())));
        holder.getTextView(R.id.shopsum).setText(String.format("数量：%1$s%2$s", record.getItemcount(), record.getUnit()));
        holder.getTextView(R.id.shopspec).setText(String.format("规格：%s", record.getSpecs()));
        holder.getTextView(R.id.shopprice).setText(String.format("单价：￥%s", record.getUnitprice()));
        holder.getTextView(R.id.shoptotal).setText(Html.fromHtml("小计："+"<font color=red>"+"￥"+record.getMoneysum()+"</font>"));
    }

}