package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.text.Html;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.ReturnGoodsBean;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2018/6/3.
 *
 * @author LK
 */
class Returngoodsadpter extends Common2Adapter<ReturnGoodsBean.ListBean> {
    public Returngoodsadpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.returngoods_recy_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReturnGoodsBean.ListBean bean = list.get(position);
        holder.getTextView(R.id.shopname).setText(bean.getName());
        holder.getTextView(R.id.shopnumber).setText(String.format("条码：%s", bean.getItemnumber()));
        holder.getTextView(R.id.shopspec).setText(String.format("规格：%s/%s", bean.getSpecs(), bean.getUnit()));
        holder.getTextView(R.id.buygoods_sum).setText(String.format("已购买：%s%s", bean.getStock_num(), bean.getUnit()));
        holder.getTextView(R.id.buygoods_money).setText(String.format("进货金额：￥%s", bean.getStock_money()));
        holder.getTextView(R.id.returngoods_num).setText(Html.fromHtml("已退货：" + "<font color=#fc2b32>" + bean.getNum() + "件" + "</font>"));
        holder.getTextView(R.id.returngoods_money).setText(Html.fromHtml("退货金额：" + "<font color=#fc2b32>" + "￥" + bean.getMoney() + "</font>"));
        Glide.with(context).load(bean.getImgurl()).into(holder.getImageView(R.id.shopimag));

    }

}
