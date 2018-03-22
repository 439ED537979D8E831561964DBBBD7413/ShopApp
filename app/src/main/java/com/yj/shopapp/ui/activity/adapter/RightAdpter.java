package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.GoodAddress;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by LK on 2017/10/29.
 * @author LK
 */

public class RightAdpter extends CommonBaseAdapter<GoodAddress.ChildrenBean> {
    public RightAdpter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = minflater.inflate(R.layout.test, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.commText.setText(list.get(i).getName());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.comm_text)
        TextView commText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
