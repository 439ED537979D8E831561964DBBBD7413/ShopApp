package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.BrandGroup;
import com.yj.shopapp.ubeen.IndustryCatelist;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2018/3/1.
 *
 * @author LK
 */

public class ScreenLvAdpter extends Common2Adapter {
    private int currposition = 0;

    public ScreenLvAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.listitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.get(position) instanceof CharSequence) {
            if (position == currposition) {
                holder.getTextView(R.id.comm_text).setSelected(true);
            } else {
                holder.getTextView(R.id.comm_text).setSelected(false);
            }
            holder.getTextView(R.id.comm_text).setText((CharSequence) list.get(position));
        } else if (list.get(position) instanceof BrandGroup.ListBean) {
            BrandGroup.ListBean bean = (BrandGroup.ListBean) list.get(position);
            if (position == currposition) {
                holder.getTextView(R.id.comm_text).setSelected(true);
            } else {
                holder.getTextView(R.id.comm_text).setSelected(false);
            }
            holder.getTextView(R.id.comm_text).setText(bean.getName());
        } else {
            IndustryCatelist.DataBean.TagGroup tagGroup = (IndustryCatelist.DataBean.TagGroup) list.get(position);
            if (position == currposition) {
                holder.getTextView(R.id.comm_text).setSelected(true);
            } else {
                holder.getTextView(R.id.comm_text).setSelected(false);
            }
            holder.getTextView(R.id.comm_text).setText(tagGroup.getName());
        }

    }

    public void setDef(int position) {
        currposition = position;
        notifyDataSetChanged();
    }

}
