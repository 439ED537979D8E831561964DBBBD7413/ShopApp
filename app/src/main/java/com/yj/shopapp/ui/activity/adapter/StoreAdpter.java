package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Store;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2018/1/8.
 *
 * @author LK
 */

public class StoreAdpter extends CommonAdapter<Store.DataBean> {
    public StoreAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.storeadpteractivity;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Store.DataBean bean = list.get(position);
        holder.getTextView(R.id.shopname).setText(bean.getShopname());
        Glide.with(context).load(bean.getImgurl()).into(holder.getImageView(R.id.shopimag));
    }
}
