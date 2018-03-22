package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.text.Html;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.OrderDatesBean;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2017/12/20.
 *
 * @author LK
 */

public class OrderFragmentAdapte extends CommonAdapter<OrderDatesBean.ItemlistBean> {

    public OrderFragmentAdapte(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.orderfragmentlayout;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDatesBean.ItemlistBean bean = list.get(position);
        holder.getTextView(R.id.itemname).setText(bean.getItemname());
        holder.getTextView(R.id.itemsum).setText(String.format("数量：%s", bean.getItemcount()));
        holder.getTextView(R.id.itemmoery).setText(Html.fromHtml("小计：" + "<font color=red>" + "￥" + bean.getMoneysum() + "</font>"));
        holder.getTextView(R.id.unitprice).setText("￥" + bean.getUnitprice());
        Glide.with(context).load(bean.getImageUrl()).apply(new RequestOptions().override(180,180).centerCrop()).into(holder.getImageView(R.id.itemimg));
    }
}
