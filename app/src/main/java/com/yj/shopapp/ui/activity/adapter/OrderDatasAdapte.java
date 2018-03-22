package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.text.Html;

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

public class OrderDatasAdapte extends CommonAdapter<OrderDatesBean.DataBean> {
    public OrderDatasAdapte(Context context, List list) {
        super(context, list);
    }

    public OrderDatasAdapte(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.test;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDatesBean.DataBean bean = list.get(position);
        holder.getTextView(R.id.comm_text).setText(Html.fromHtml(bean.getName() + ":" + bean.getItemnum() + "件 " + "<font color=red>" + "￥" + bean.getMoney() + "</font>"));
    }
}
