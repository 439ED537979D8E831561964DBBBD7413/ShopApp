package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.IndustryCatelist;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class SIndustryAdapter extends CommonAdapter<IndustryCatelist.DataBean.TagGroup> {

    public SIndustryAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.category_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IndustryCatelist.DataBean.TagGroup mdatas = list.get(position);
        holder.getTextView(R.id.name_tv).setText(mdatas.getName());
        Glide.with(context).load(mdatas.getImgurl()).into(holder.getImageView(R.id.simpleDraweeView));

    }

}
