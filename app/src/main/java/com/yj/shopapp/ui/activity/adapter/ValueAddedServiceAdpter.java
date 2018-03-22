package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.ServiceOrder;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

/**
 * Created by LK on 2018/1/15.
 *
 * @author LK
 */

public class ValueAddedServiceAdpter extends CommonAdapter<ServiceOrder> {
    public ValueAddedServiceAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.addedserviceitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServiceOrder order = list.get(position);
        Glide.with(context).load(order.getImgurl()).into(holder.getSimpleDraweeView(R.id.shopimag));
        holder.getTextView(R.id.stroename).setText(order.getShop() + "-" + order.getClassX());
        holder.getTextView(R.id.order_status).setText(Contants.OrderDrawerw[Integer.parseInt(order.getStatus())]);
        holder.getTextView(R.id.orderNumber).setText(String.format("订单号：%s", order.getOrder()));
        holder.getTextView(R.id.spesc).setText(String.format("规格：%s", order.getSpecs()));
        holder.getTextView(R.id.order_time).setText(DateUtils.timet(order.getAddtime()));
        holder.getTextView(R.id.price).setText("￥"+order.getUnitprice());
        holder.getTextView(R.id.num).setText("x" + order.getNum());
        holder.getTextView(R.id.allprice).setText("￥"+order.getMoney());
        holder.getTextView(R.id.Total_amount).setText(String.format("共%1$s%2$s   合计：%3$s", order.getNum(), order.getUnit(), order.getMoney()));
        holder.getTextView(R.id.shopname).setText(order.getName());
        TextView price = holder.getTextView(R.id.unitprice);
        price.setText("￥"+order.getMoney());
        price.getPaint().setAntiAlias(true);
        price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

    }

}
