package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.GoodAddress;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LK on 2017/10/29.
 *
 * @author LK
 */

public class LiftAdpter extends CommonBaseAdapter<GoodAddress> {
    int defItem = -1;

    public LiftAdpter(Context context) {
        super(context);
    }

    /**
     * 适配器中添加这个方法
     */
    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
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
        if (defItem == i) {
            view.setBackgroundColor(context.getResources().getColor(R.color.skyblue));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.all_bg));
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
