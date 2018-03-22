package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.shopapp.R;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by LK on 2017/12/26.
 *
 * @author LK
 */

public class OrderListAdapter extends CommonBaseAdapter<String> {
    public OrderListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = minflater.inflate(R.layout.listviewitem, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.contentTv.setText(list.get(i));
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.Serial_number)
        TextView SerialNumber;
        @BindView(R.id.content_tv)
        TextView contentTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
