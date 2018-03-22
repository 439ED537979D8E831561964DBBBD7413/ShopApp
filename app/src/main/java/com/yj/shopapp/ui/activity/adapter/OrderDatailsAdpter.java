package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.OrderPreview;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.PreferenceUtils;

import java.util.List;

/**
 * Created by LK on 2017/12/26.
 *
 * @author LK
 */

public class OrderDatailsAdpter extends CommonAdapter<OrderPreview.ClassBean> {
    public OrderDatailsAdpter(Context context, List list) {
        super(context, list);
    }

    public OrderDatailsAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.baserecyitemview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderPreview.ClassBean bean = list.get(position);
        Glide.with(context).load(PreferenceUtils.Json2map(context, "imagurl").get(bean.getCid())).into(holder.getImageView(R.id.simpleDraweeView));
        holder.getTextView(R.id.num2money).setText(String.format("共%1$s件：%2$s元", bean.getNum(), bean.getMoney()));
        holder.getTextView(R.id.name_tv).setText(bean.getClassX());
    }
}
