package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.ShopListData;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.GlideCircleTransform;

/**
 * Created by LK on 2018/1/8.
 *
 * @author LK
 */

public class ShopListAdpter extends CommonAdapter<ShopListData.DataBean> {
    public ShopListAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.shoplistadpterview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopListData.DataBean bean = list.get(position);
        holder.getTextView(R.id.itemname).setText(bean.getName());
        holder.getTextView(R.id.itemnumber).setText(bean.getItemnumber());
        holder.getTextView(R.id.itemprice).setText(Html.fromHtml("￥" + "<s>" + bean.getPrice() + "</s>" + "促销:" + "<font color=red >" + bean.getSales_price() + "</font>"));
        holder.getTextView(R.id.brand).setText(String.format("品牌：%s", bean.getBrand()));
        holder.getTextView(R.id.spesc).setText(String.format("规格：%s", bean.getSpecs()));
        holder.getTextView(R.id.itemunit).setText(String.format("单位：%s", bean.getUnit()));
        if (!"".equals(bean.getMessage())) {
            holder.getTextView(R.id.itemhintmsg).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.itemhintmsg).setText(String.format("提示:%s", bean.getMessage()));
        }
        Glide.with(context).load(bean.getImgurl())
                .apply(new RequestOptions().transform(new GlideCircleTransform(6)))
                .into(holder.getImageView(R.id.itemimag));
    }

}
