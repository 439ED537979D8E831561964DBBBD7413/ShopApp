package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.CancelByList;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

/**
 * Created by LK on 2018/8/16.
 *
 * @author LK
 */
public class CancelListAdpter extends Common2Adapter<CancelByList.ListsBean> {
    public CancelListAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.cancellist_itemview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CancelByList.ListsBean bean = list.get(position);
        holder.getTextView(R.id.shopName).setText(bean.getName());
        holder.getTextView(R.id.cread_time).setText(DateUtils.timet(bean.getAddtime(), "yyyy年MM月dd日  HH:mm"));
        holder.getTextView(R.id.shop_num).setText(String.format("数量：%s", bean.getItemcount()));
        holder.getTextView(R.id.shop_price).setText(String.format("合计：￥%s", bean.getMoney()));
        Glide.with(context).load(bean.getImgurl()).into(holder.getImageView(R.id.shop_img));

    }
}
