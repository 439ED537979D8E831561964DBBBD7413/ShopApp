package com.yj.shopapp.ui.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.NewOrder;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.ui.activity.shopkeeper.SOrderDatesActivity;
import com.yj.shopapp.util.CommonUtils;

import java.util.List;

/**
 * Created by LK on 2017/12/19.
 *
 * @author LK
 */

public class SNewOrderAdpter extends Common2Adapter<NewOrder> {
    private LinearLayoutManager linearLayoutManager;
    private float scrollX, scrollY;

    public SNewOrderAdpter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        switch (viewType) {
            case 1:
                return R.layout.stab_oeder_recycleritem;
            case 2:
                return R.layout.bugoodsorderitem;
            default:
                break;
        }
        return 0;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NewOrder order = list.get(position);
        if (isBuGoodsOrder(position)) {
            holder.getTextView(R.id.orderNumber).setText(String.format("订单：%s", order.getIteminfo().getItemnumber()));
            holder.getTextView(R.id.order_status).setText(Contants.OrderStadus[Integer.parseInt(order.getStatus())]);
            holder.getTextView(R.id.bgoodsname).setText(order.getIteminfo().getName());
            holder.getTextView(R.id.bgoodsspesc).setText(String.format("规格：%1$s%2$s", order.getIteminfo().getSpecs(), order.getIteminfo().getUnit()));
            holder.getTextView(R.id.bgoodsprice).setText(String.format("￥%s", order.getIteminfo().getUnitprice()));
            holder.getTextView(R.id.bgoodsnum).setText(String.format("x%s", order.getIteminfo().getItemcount()));
            holder.getTextView(R.id.allnum).setText(String.format("共%1$s%2$s商品", order.getIteminfo().getItemcount(), order.getIteminfo().getUnit()));
            fontLarger(String.format("总金额：￥%s", order.getIteminfo().getMoneysum()), holder.getTextView(R.id.allmonry), 4);
            Glide.with(context).load(order.getIteminfo().getImgurl()).into(holder.getImageView(R.id.shopimag));
        } else {
            holder.getTextView(R.id.orderNo).setText(String.format("订单：%s", order.getOid()));
            holder.getTextView(R.id.orderStatus).setText(Contants.OrderStadus[Integer.parseInt(order.getStatus())]);
            if (order.getCoupon() == 0) {
                holder.getTextView(R.id.total_num).setText(Html.fromHtml("共" + "<font color=>" + order.getSumnum() + "</font>" + "件商品"));
            } else {
                holder.getTextView(R.id.total_num).setText(String.format("共%d件商品    已优惠￥%.2f", order.getSumnum(), (double) order.getCoupon()));
            }
            fontLarger(String.format("应付金额：￥%s", order.getMoney()), holder.getTextView(R.id.Amount_payable), 5);

            final RecyclerView recyclerView = (RecyclerView) holder.getView(R.id.orderItem_Recy);
            SorderItemAdapter adapter = new SorderItemAdapter(context, order.getData());
            linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setOnTouchListener((v, event) -> {
                if (event.getAction() == event.ACTION_DOWN) {
                    scrollX = event.getX();
                    scrollY = event.getY();
                }
                if (event.getAction() == event.ACTION_UP) {
                    if (Math.abs(scrollX - event.getX()) <= 10 && Math.abs(scrollY - event.getY()) <= 10) {
                        Bundle bundle = new Bundle();
                        bundle.putString("oid", order.getOid());
                        CommonUtils.goActivity(context, SOrderDatesActivity.class, bundle);
                    }
                }
                return false;
            });
            holder.getImageView(R.id.lift_bt).setVisibility(order.getData().size() > 4 ? View.VISIBLE : View.GONE);
            holder.getImageView(R.id.right_bt).setVisibility(order.getData().size() > 4 ? View.VISIBLE : View.GONE);
            holder.getImageView(R.id.lift_bt).setOnClickListener(v -> recyclerView.smoothScrollToPosition(0));
            holder.getImageView(R.id.right_bt).setOnClickListener(v -> recyclerView.smoothScrollToPosition(order.getData().size() - 1));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isBuGoodsOrder(position)) {
            return 2;
        } else {
            return 1;
        }
    }

    private boolean isBuGoodsOrder(int position) {
        return Integer.parseInt(list.get(position).getSale_id()) > 0;
    }

    private void fontLarger(String test, TextView textView, int index) {
        SpannableStringBuilder builder = new SpannableStringBuilder(test);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#01abff"));
        builder.setSpan(span, index, test.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new RelativeSizeSpan(1.2f), index, test.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if (textView != null) {
            textView.setText(builder);
        }
    }

}
