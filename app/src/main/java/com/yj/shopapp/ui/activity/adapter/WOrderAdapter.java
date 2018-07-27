package com.yj.shopapp.ui.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.ui.activity.shopkeeper.SOrderDetailActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.wbeen.WNewOrder;

/**
 * Created by jm on 2016/4/25.
 */
public class WOrderAdapter extends Common2Adapter<WNewOrder> {
    private float scrollX, scrollY;

    public WOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        switch (viewType) {
            case 1:
                return R.layout.wtab_oeder_recycleritem;
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
        WNewOrder order = list.get(position);
        if (isBuGoodsOrder(position)) {
            holder.getTextView(R.id.orderNumber).setText(String.format("订单号：%s", order.getOrder()));
            holder.getTextView(R.id.order_status).setText(Contants.OrderStadus[Integer.parseInt(order.getStatus())]);
            holder.getTextView(R.id.bgoodsname).setText(order.getIteminfo().getName());
            holder.getTextView(R.id.bgoodsspesc).setText(String.format("规格：%1$s%2$s", order.getIteminfo().getSpecs(), order.getIteminfo().getUnit()));
            holder.getTextView(R.id.bgoodsprice).setText(String.format("￥%s", order.getIteminfo().getUnitprice()));
            holder.getTextView(R.id.bgoodsnum).setText(String.format("x%s", order.getIteminfo().getItemcount()));
            holder.getTextView(R.id.allnum).setText(String.format("共%1$s%2$s商品", order.getIteminfo().getItemcount(), order.getIteminfo().getUnit()));
            fontLarger(String.format("总金额：￥%s", order.getIteminfo().getMoneysum()), holder.getTextView(R.id.allmonry), 4);
            Glide.with(context).load(order.getIteminfo().getImgurl()).into(holder.getImageView(R.id.shopimag));
        } else {
            holder.getTextView(R.id.orderNo).setText(String.format("订单号:%s", order.getOrder()));
            holder.getTextView(R.id.orderStatus).setText(Contants.OrderStadus[Integer.parseInt(order.getStatus())]);
            holder.getTextView(R.id.customer).setText(order.getAddress().getShopname());
            holder.getTextView(R.id.all_total).setText(String.format("总件数:%s件", order.getNum()));
            fontLarger(String.format("总金额:￥%s", order.getMoney()), holder.getTextView(R.id.all_money), 4);
            final RecyclerView recyclerView = (RecyclerView) holder.getView(R.id.orderItem_Recy);
            WorderItemAdapter adapter = new WorderItemAdapter(context, order.getClasslist());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
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
                        //跳转
                        Bundle bundle = new Bundle();
                        bundle.putString("oid", order.getId());
                        CommonUtils.goActivity(context, SOrderDetailActivity.class, bundle);
                    }
                }
                return false;
            });
            holder.getImageView(R.id.lift_bt).setVisibility(order.getClasslist().size() > 4 ? View.VISIBLE : View.GONE);
            holder.getImageView(R.id.right_bt).setVisibility(order.getClasslist().size() > 4 ? View.VISIBLE : View.GONE);
            holder.getImageView(R.id.lift_bt).setOnClickListener(v -> recyclerView.smoothScrollToPosition(0));
            holder.getImageView(R.id.right_bt).setOnClickListener(v -> recyclerView.smoothScrollToPosition(order.getClasslist().size() - 1));
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
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#FE3000"));
        builder.setSpan(span, index, test.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new RelativeSizeSpan(1.1f), index, test.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if (textView != null) {
            textView.setText(builder);
        }
    }
}
