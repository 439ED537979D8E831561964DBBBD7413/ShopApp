package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.GlideCircleTransform;

import java.util.List;


/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class SRecyclerAdapter extends CommonAdapter {
    public SRecyclerAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.category_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Industry mdatas = (Industry) list.get(position);
        holder.getTextView(R.id.name_tv).setText(mdatas.getName());
        if (0==mdatas.getResult()) {
            Glide.with(context).load(mdatas.getUrl()).apply(new RequestOptions().transform(new GlideCircleTransform())).into(holder.getImageView(R.id.simpleDraweeView));
        }else {
            Glide.with(context).load(mdatas.getUrl()).into(holder.getImageView(R.id.simpleDraweeView));
        }

    }
}
