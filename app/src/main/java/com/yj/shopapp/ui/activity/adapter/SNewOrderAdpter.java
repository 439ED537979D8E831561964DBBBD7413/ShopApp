package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.text.Html;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.NewOrder;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.view.MyRecycleView;

import java.util.List;

/**
 * Created by LK on 2017/12/19.
 *
 * @author LK
 */

public class SNewOrderAdpter extends CommonAdapter<NewOrder> {

    public SNewOrderAdpter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.stab_oeder_recycleritem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewOrder order = list.get(position);
        holder.getTextView(R.id.orderNo).setText(order.getOid());
        holder.getTextView(R.id.orderStatus).setText(Contants.OrderStadus[Integer.parseInt(order.getStatus())]);
        holder.getTextView(R.id.total_num).setText(Html.fromHtml("共<font color='#ed961b'>" + order.getSumnum() + "</font>件商品"));
        if (order.getCoupon().equals("0")) {
            holder.getTextView(R.id.Amount_payable).setText(Html.fromHtml("应付金额：<font color='#ed961b'>" + "￥" + order.getMoney() + "</font>"));
        } else {
            holder.getTextView(R.id.Amount_payable).setText(Html.fromHtml("应付金额：<font color='#ed961b'>" + "￥" + order.getMoney() +
                    "</font>-优惠金额：<font color='#ed961b'>" + "￥" + order.getCoupon() + "</font>=实付金额：</font>-优惠金额：<font color='#ed961b'>" + "￥" + order.getReceipt() + "</font>"));
        }
        MyRecycleView recyclerView = (MyRecycleView) holder.getView(R.id.orderItem_Recy);
        SorderItemAdapter adapter = new SorderItemAdapter(context, order.getData());
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.setAdapter(adapter);
    }


}
