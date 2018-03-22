package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.ClassifyList;
import com.yj.shopapp.ubeen.ShopCase;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2018/1/8.
 *
 * @author LK
 */

public class ClassifyListAdpter extends CommonAdapter {
    private ClassifyList.DataBean cbean;
    private ShopCase.DataBean sbean;

    public ClassifyListAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.storeadpteractivity;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.get(position) instanceof ClassifyList.DataBean) {
            cbean = (ClassifyList.DataBean) list.get(position);
        } else {
            sbean = (ShopCase.DataBean) list.get(position);
        }
        if (cbean != null) {
            holder.getTextView(R.id.shopname).setText(cbean.getName());
            Glide.with(context).load(cbean.getImgurl()).into(holder.getImageView(R.id.shopimag));
        } else {
            holder.getTextView(R.id.shopname).setText(sbean.getTitle());
            Glide.with(context).load(sbean.getImgurl()).into(holder.getImageView(R.id.shopimag));
        }
    }
}
