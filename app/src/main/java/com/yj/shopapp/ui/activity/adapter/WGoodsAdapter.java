package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.wbeen.Goods;

/**
 * Created by jm on 2016/4/25.
 */
public class WGoodsAdapter extends Common2Adapter<Goods> {

    public WGoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.wtab_goods_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Goods goods = list.get(position);
        holder.getTextView(R.id.goodsnameTv).setText(goods.getName());
        holder.getTextView(R.id.localhost_tv).setText(String.format("位置：%s", goods.getLocalhost()));
        holder.getTextView(R.id.priceTv).setText(Html.fromHtml("<font color=#909090>" + "金额：" + "</font>" + goods.getPrice()));
        holder.getTextView(R.id.specsTv).setText(String.format("库存：%s", goods.getItemsum()));
        Glide.with(context).load(goods.getImgurl()).apply(new RequestOptions().override(180, 180)).into(holder.getSimpleDraweeView(R.id.simpleDraweeView));
        holder.getImageView(R.id.top_status_imag).setVisibility(goods.getSale_status().equals("1") ? View.GONE : View.VISIBLE);
    }

}
