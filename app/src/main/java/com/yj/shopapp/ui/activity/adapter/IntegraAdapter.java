package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.ExcGoods;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.GlideCircleTransform;

import java.util.List;

/**
 * Created by LK on 2017/12/21.
 *
 * @author LK
 */

public class IntegraAdapter extends CommonAdapter<ExcGoods.DataBean> implements View.OnClickListener {
    private OnViewClickListener listener;

    public void setListener(OnViewClickListener listener) {
        this.listener = listener;
    }

    public IntegraAdapter(Context context, List list, OnViewClickListener listener) {
        super(context, list);
        this.listener = listener;
    }
    public IntegraAdapter(Context context, OnViewClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.integracontentview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExcGoods.DataBean goods = list.get(position);
        holder.getTextView(R.id.goodsName).setText(goods.getName());
        holder.getTextView(R.id.integral_num).setText(goods.getIntegral() + "积分");
        holder.getTextView(R.id.number).setText(goods.getNum() + "件");
        holder.getTextView(R.id.Redeem_now).setOnClickListener(this);
        holder.getTextView(R.id.Redeem_now).setTag(position);
        holder.getView(R.id.onItemclick).setOnClickListener(this);
        holder.getView(R.id.onItemclick).setTag(position);

        Glide.with(context).load(goods.getImgurl()).apply(new RequestOptions().centerCrop().transform(new GlideCircleTransform()))
                .into(holder.getImageView(R.id.Goods_imag));

    }

    @Override
    public void onClick(View v) {
        listener.onItemClick(v, (Integer) v.getTag());
    }

    public interface OnViewClickListener {
        void onItemClick(View view, int position);
    }
}
