package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.View;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.wbeen.Agency;

/**
 * Created by huang on 2016/9/11.
 */
public class AgencyAdapter extends Common2Adapter<Agency> {
    private boolean isFiter;

    public AgencyAdapter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.wactivity_agency_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Agency agents = list.get(position);
        if (position == 0 && isFiter) {
            holder.getTextView(R.id.wu).setVisibility(View.VISIBLE);
            holder.getView(R.id.parent_lt).setVisibility(View.GONE);
        } else {
            holder.getTextView(R.id.wu).setVisibility(View.GONE);
            holder.getView(R.id.parent_lt).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.agentNameTv).setText(String.format("名称：%s", agents.getName()));
            holder.getTextView(R.id.agentNoTv).setText(String.format("手机：%s", agents.getMobile()));
            holder.getTextView(R.id.agentLinkmanTv).setText(String.format("固话：%s", agents.getTel()));
        }
    }

    public void setFiter(boolean fiter) {
        isFiter = fiter;
        notifyDataSetChanged();
    }
}
