package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.View;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.wbeen.Msgs;

import java.util.List;

/**
 * Created by jm on 2016/4/28.
 */
public class NewsAdapter extends Common2Adapter<Msgs> {

    public NewsAdapter(Context context, List<Msgs> noteList) {
        super(context, noteList);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.activity_newslist_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msgs msgs = list.get(position);
        if (msgs.getStatus().equals("0")) {
            holder.getTextView(R.id.status_Tx).setVisibility(View.GONE);
        } else {
            holder.getTextView(R.id.status_Tx).setVisibility(View.VISIBLE);
        }
        holder.getTextView(R.id.content).setText(msgs.getMesstitle());
        holder.getTextView(R.id.date_Tx).setText(DateUtils.getDateToString(msgs.getAddtime() + "000"));
    }

}
