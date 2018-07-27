package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.HotIndex;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;

/**
 * Created by LK on 2018/4/8.
 *
 * @author LK
 */
public class HotIndexAdpter extends Common2Adapter<HotIndex> implements View.OnClickListener {
    private OnItemChildViewOnClickListener listener;

    public HotIndexAdpter(Context context, OnItemChildViewOnClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.hotshopitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HotIndex index = list.get(position);
        Glide.with(context).load(index.getImgurl()).into(holder.getSimpleDraweeView(R.id.shopimag));
        holder.getTextView(R.id.shopname).setText(index.getName());
        holder.getTextView(R.id.goodAddress_Tv).setText(String.format("位置：%s", index.getLocalhost()));
        holder.getTextView(R.id.account_tv).setText(String.format("规格：%s", index.getSpecs()));
        holder.getTextView(R.id.allprice).setText(Html.fromHtml("金额：" + "<b>" + "<font color=red>" + "￥" + index.getPrice() + "</font>" + "</b>"));
        holder.getTextView(R.id.shopnum).setText(String.format("剩余：%1$s%2$s", index.getItemsum(), index.getUnit()));
        holder.getTextView(R.id.goCarlist).setOnClickListener(this);
        holder.getView(R.id.itemview).setOnClickListener(this);
        holder.getTextView(R.id.goCarlist).setTag(position);
        holder.getView(R.id.itemview).setTag(position);
        if (index.getSale_status().equals("0")) {
            holder.getTextView(R.id.goCarlist).setText("正在补货中");
            holder.getTextView(R.id.goCarlist).setBackgroundResource(R.drawable.goodcar_bg_2);
        } else {
            holder.getTextView(R.id.goCarlist).setText("加入购物车");
            holder.getTextView(R.id.goCarlist).setBackgroundResource(R.drawable.goodcar_bg3);
        }

    }

    @Override
    public void onClick(View v) {
        listener.onChildViewClickListener(v, (Integer) v.getTag());
    }
}
