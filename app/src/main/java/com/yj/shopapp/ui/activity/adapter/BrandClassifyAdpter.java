package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.BrandGroup;
import com.yj.shopapp.ubeen.IndustryCatelist;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2018/3/28.
 *
 * @author LK
 */

public class BrandClassifyAdpter extends Common2Adapter {
    public BrandClassifyAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.view_shopsumitem2;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.get(position) instanceof BrandGroup.ListBean) {
            BrandGroup.ListBean bean = (BrandGroup.ListBean) list.get(position);
            holder.getTextView(R.id.itemsum).setText(bean.getName());
        } else {
            IndustryCatelist.DataBean.TagGroup group= (IndustryCatelist.DataBean.TagGroup) list.get(position);
            holder.getTextView(R.id.itemsum).setText(group.getName());
        }
    }
}
