package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.OrderDatesBean;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

import java.util.List;

/**
 * Created by LK on 2017/12/20.
 *
 * @author LK
 */

public class OrderDatasAdapte1 extends CommonAdapter<OrderDatesBean.CouponBran> {
    public OrderDatasAdapte1(Context context, List list) {
        super(context, list);
    }
    public OrderDatasAdapte1(Context context) {
        super(context);
    }
    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.twotextview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getTextView(R.id.remark).setText(list.get(position).getRemark());
        holder.getTextView(R.id.money).setText(list.get(position).getMoney());
    }
}
