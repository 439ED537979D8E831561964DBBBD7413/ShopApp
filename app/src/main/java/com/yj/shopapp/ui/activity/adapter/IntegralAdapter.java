package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.widget.TextView;

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

public class IntegralAdapter extends CommonAdapter<IntegralDetail> {
    public IntegralAdapter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_integral_detail;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IntegralDetail integral = list.get(position);

        holder.getTextView(R.id.order_time).setText(DateUtils.timet(integral.getTime()));

        holder.getTextView(R.id.orderStatus).setText(String.format("【%1$s】  %2$s", Contants.OrderState[Integer.parseInt(integral.getType())], integral.getType_name()));

        holder.getTextView(R.id.ordermoney).setText(String.format("[金额] %s元", integral.getMoney()));
        TextView tv = holder.getTextView(R.id.integral);
        if ('-' == integral.getIntegral().charAt(0)) {
            tv.setTextColor(context.getResources().getColor(R.color.color3fb03e));
            tv.setText(integral.getIntegral());
        } else {
            tv.setTextColor(context.getResources().getColor(R.color.colorf3302e));
            tv.setText(String.format("+%s", integral.getIntegral()));
        }


    }


}
