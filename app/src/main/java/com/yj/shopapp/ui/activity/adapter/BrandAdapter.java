package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.TagGroup;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by Administrator on 2017/8/10 0010.
 */

public class BrandAdapter extends CommonAdapter<TagGroup> {

    public BrandAdapter(Context context) {
        super(context);
    }
    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.recycler_tv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // holder.getImageView(R.id.simpleDraweeView).setVisibility(View.GONE);
        TagGroup tg = list.get(position);
        holder.getTextView(R.id.recy_tv).setText(tg.getName());
    }


}
