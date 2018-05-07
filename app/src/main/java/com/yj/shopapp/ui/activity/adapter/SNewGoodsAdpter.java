package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.ui.activity.Interface.GoodsItemListenter;

import java.util.List;

/**
 * Created by LK on 2018/2/27.
 *
 * @author LK
 */

public class SNewGoodsAdpter extends CommonAdapter<Goods> implements View.OnClickListener {
    private boolean isshow = false;
    private int num = 0;
    private boolean del_btn = false;
    private GoodsItemListenter listenter;

    public SNewGoodsAdpter(Context context) {
        super(context);
    }

    public SNewGoodsAdpter(Context context, List list) {
        super(context, list);
    }

    public SNewGoodsAdpter(Context context, List list, int num) {
        super(context, list);
        this.num = num;
    }

    public SNewGoodsAdpter(Context context, List list, boolean isshow) {
        super(context, list);
        this.isshow = isshow;
    }

    public SNewGoodsAdpter(Context context, boolean del_btn) {
        super(context);
        this.del_btn = del_btn;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.stab_goods_item;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Goods goods = list.get(position);
        holder.getTextView(R.id.addcartTv).setOnClickListener(this);
        holder.getTextView(R.id.addcartTv).setTag(position);
        holder.getTextView(R.id.goodsnameTv).setText(goods.getName());
        holder.getTextView(R.id.specsTv).setText("规格：" + goods.getSpecs());
        holder.getView(R.id.itemview).setOnClickListener(this);
        holder.getView(R.id.itemview).setTag(position);
        holder.getTextView(R.id.shopnum).setText(String.format("数量%1$s%2$s", goods.getItemsum(), goods.getUnit()));
        Glide.with(context).load(goods.getImgurl()).apply(new RequestOptions().placeholder(R.drawable.load).centerCrop()).into(holder.getSimpleDraweeView(R.id.simpleDraweeView));

        if (!(goods.getIs_show_price().equals(""))) {
            if (Integer.parseInt(goods.getIs_show_price()) == 0) {
                holder.getTextView(R.id.priceTv).setText("");
            } else {
                holder.getTextView(R.id.priceTv).setText(Html.fromHtml("<font color=red>" + "￥" + goods.getPrice() + "</font>"));
            }
        } else {
            holder.getTextView(R.id.priceTv).setText(Html.fromHtml("<font color=red>" + "￥" + goods.getPrice() + "</font>"));
        }

        if (!goods.getSale_status().equals("")) {
            if (goods.getSale_status().equals("0")) {
                holder.getSimpleDraweeView(R.id.top_simpleDraweeView).setVisibility(View.VISIBLE);
                holder.getTextView(R.id.addcartTv).setBackgroundResource(R.drawable.goodcar_bg_2);
                holder.getTextView(R.id.addcartTv).setText("正在补货中");
                holder.getTextView(R.id.addcartTv).setClickable(false);
                holder.getSimpleDraweeView(R.id.top_simpleDraweeView).setImageResource(R.drawable.pause);
            } else {
                holder.getTextView(R.id.addcartTv).setBackgroundResource(R.drawable.goodcar_bg3);
                holder.getTextView(R.id.addcartTv).setText("加入购物车");
                if (isshow) {
                    holder.getSimpleDraweeView(R.id.top_simpleDraweeView).setImageResource(R.drawable.img_ic_new);
                } else {
                    holder.getSimpleDraweeView(R.id.top_simpleDraweeView).setVisibility(View.GONE);
                }
            }
        } else {
            holder.getSimpleDraweeView(R.id.top_simpleDraweeView).setVisibility(View.GONE);
        }

        if (num != 0) {
            holder.getTextView(R.id.money_lowgood).setText("特价:");
        }

        if (!goods.getSales_price().equals("") && !goods.getSale_status().equals("")) {
            if (!"0".equals(goods.getSales_price()) && goods.getSale_status().equals("1")) {
                holder.getSimpleDraweeView(R.id.top_simpleDraweeView).setVisibility(View.VISIBLE);
                holder.getSimpleDraweeView(R.id.top_simpleDraweeView).setImageResource(R.drawable.img_ic_promotion);
                holder.getTextView(R.id.priceTv).setText("￥" + goods.getSales_price());
                holder.getTextView(R.id.sales_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.getTextView(R.id.sales_price).setText("￥" + goods.getPrice());
            } else {
                holder.getTextView(R.id.sales_price).setText("");
            }
        } else {
            holder.getTextView(R.id.sales_price).setText("");
            holder.getSimpleDraweeView(R.id.top_simpleDraweeView).setVisibility(View.GONE);
        }
        if (del_btn) {
            holder.getView(R.id.choose_re).setVisibility(View.VISIBLE);
            holder.getCheckBox(R.id.checkBox).setChecked(goods.isSelected());
            holder.getCheckBox(R.id.checkBox).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenter.onCheckBoxClick(position, holder.getCheckBox(R.id.checkBox).isChecked());
                }
            });
        }

    }

    public void setListenter(GoodsItemListenter listenter) {
        this.listenter = listenter;
    }

    @Override
    public void onClick(View v) {
        listenter.onClick(v, (Integer) v.getTag());
    }
}
