package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.BoughtGoods;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;

/**
 * Created by LK on 2018/8/18.
 *
 * @author LK
 */
public class BoughtRvAdpter extends Common2Adapter<BoughtGoods> {
    private OnItemChildViewOnClickListener listener;

    public BoughtRvAdpter(Context context, OnItemChildViewOnClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.iv_boughtgoods;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BoughtGoods goods = list.get(position);
        holder.getTextView(R.id.shop_name).setText(goods.getName());
        holder.getTextView(R.id.shop_specs).setText(String.format("规格:%s", goods.getSpecs()));
        holder.getTextView(R.id.shop_stock).setText(String.format("库存:%s", goods.getItemsum()));
        holder.getTextView(R.id.shop_price).setText(String.format("￥%s", goods.getPrice()));
        holder.getTextView(R.id.history_num).setText(String.format("已购买%s%s", goods.getNum(), goods.getUnit()));
        holder.getTextView(R.id.history_money).setText(String.format("金额为￥%s", goods.getMoney()));
        Glide.with(context).load(goods.getImgurl()).into(holder.getImageView(R.id.shop_imag));
        //holder.getTextView(R.id.add_card).setVisibility(goods.getDolistcart() == 1 ? View.VISIBLE : View.GONE);
        holder.getView(R.id.itemview).setOnClickListener(v -> listener.onChildViewClickListener(v, position));
        holder.getTextView(R.id.add_card).setOnClickListener(v -> listener.onChildViewClickListener(v, position));
        holder.getTextView(R.id.add_card).setBackgroundResource(goods.getDolistcart() == 1 ? R.drawable.goodcar_bg3 : R.drawable.goodcar_bg_2);
        if (goods.getDolistcart() == 1) {
            if (!goods.getSale_status().equals("")) {
                if (goods.getSale_status().equals("0")) {
                    holder.getImageView(R.id.top_imag).setVisibility(View.VISIBLE);
                    holder.getTextView(R.id.add_card).setBackgroundResource(R.drawable.goodcar_bg_2);
                    holder.getTextView(R.id.add_card).setText("正在补货中");
                    holder.getTextView(R.id.add_card).setClickable(false);
                    holder.getImageView(R.id.top_imag).setImageResource(R.drawable.pause);
                } else {
                    holder.getTextView(R.id.add_card).setBackgroundResource(R.drawable.goodcar_bg3);
                    holder.getTextView(R.id.add_card).setText("加入购物车");
                    holder.getImageView(R.id.top_imag).setVisibility(View.GONE);
                }
            } else {
                holder.getImageView(R.id.top_imag).setVisibility(View.GONE);
            }
        } else {
            holder.getTextView(R.id.add_card).setText("已下架");
        }
    }
}
