package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.TagGroup;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class SBrand2Adapter extends CommonAdapter<TagGroup> {
    public SBrand2Adapter(Context context) {
        super(context);
    }

    public SBrand2Adapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.category_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TagGroup mdatas = list.get(position);
        holder.getTextView(R.id.name_tv).setText(mdatas.getName());
        Glide.with(context).load(mdatas.getImgurl()).into(holder.getImageView(R.id.simpleDraweeView));

    }

}
