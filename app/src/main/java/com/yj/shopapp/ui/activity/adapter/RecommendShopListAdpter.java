package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.ShopList;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2018/8/10.
 *
 * @author LK
 */
public class RecommendShopListAdpter extends Common2Adapter<ShopList.ListsBean> {
    public RecommendShopListAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.itemview_recommendlist;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopList.ListsBean shop = list.get(position);
        holder.getTextView(R.id.shopname).setText(shop.getShopname());
        holder.getTextView(R.id.username).setText(shop.getUsername());
        holder.getTextView(R.id.profit_tv).setText(String.format("￥%s", shop.getProfit()));
        holder.getTextView(R.id.satisfy_num).setText(shop.getNum());
    }
}
