package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.wbeen.Goods;

/**
 * Created by huang on 2016/9/19.
 */
public class WStopGoodsAdapter extends Common2Adapter<Goods> {
    private OnItemChildViewOnClickListener listener;
    private int stop_status;

    public WStopGoodsAdapter(Context context, OnItemChildViewOnClickListener listener) {
        super(context);
        this.listener = listener;
    }

    public WStopGoodsAdapter(Context context, OnItemChildViewOnClickListener listener, int s) {
        super(context);
        this.listener = listener;
        this.stop_status = s;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.wstop_goods_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Goods goods = list.get(position);
        holder.getTextView(R.id.goodsnameTv).setText(goods.getName());
        holder.getTextView(R.id.priceTv).setText(Html.fromHtml("<font color=#909090>" + "金额: " + "</font>" + goods.getPrice()));
        holder.getTextView(R.id.specsTv).setText(String.format("库存: %s", goods.getItemsum()));
        Glide.with(context).load(goods.getImgurl()).into(holder.getSimpleDraweeView(R.id.simpleDraweeView));
        holder.getTextView(R.id.delect_tv).setOnClickListener((v) -> listener.onChildViewClickListener(v, position));
        holder.getView(R.id.stopgoods_recycler_item).setOnClickListener((v) -> listener.onChildViewClickListener(v, position));
        holder.getImageView(R.id.top_status_imag).setVisibility(goods.getSale_status().equals("0") ? View.GONE : View.VISIBLE);
        if (stop_status == 0) {
            holder.getTextView(R.id.delect_tv).setBackground(context.getResources().getDrawable(R.drawable.goodcar_bg_2));
        } else {
            holder.getTextView(R.id.delect_tv).setBackground(context.getResources().getDrawable(R.drawable.goodcar_bg3));
        }
    }

}

