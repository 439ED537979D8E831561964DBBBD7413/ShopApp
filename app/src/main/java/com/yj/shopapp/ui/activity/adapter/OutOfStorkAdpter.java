package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.OrderPreview;
import com.yj.shopapp.ubeen.OutOfStork;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

import java.util.List;

/**
 * Created by LK on 2018/4/29.
 *
 * @author LK
 */
public class OutOfStorkAdpter extends Common2Adapter {
    public OutOfStorkAdpter(Context context) {
        super(context);
    }

    public OutOfStorkAdpter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.outofstorkitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.get(position) instanceof OutOfStork) {
            OutOfStork stork = (OutOfStork) list.get(position);
            holder.getTextView(R.id.shopname).setText(stork.getName());
            holder.getTextView(R.id.shopnum).setText(String.format("缺货:%s", stork.getNum()));
        } else {
            OrderPreview.CancelBean bean = (OrderPreview.CancelBean) list.get(position);
            holder.getTextView(R.id.shopname).setText(bean.getName());
            holder.getTextView(R.id.shopnum).setText(String.format("缺%1$s%2$s", bean.getNum(), bean.getUnit()));
        }

    }
}
