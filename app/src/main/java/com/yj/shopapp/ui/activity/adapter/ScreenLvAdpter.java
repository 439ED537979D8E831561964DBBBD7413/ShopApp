package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.shopapp.R;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by LK on 2018/3/1.
 *
 * @author LK
 */

public class ScreenLvAdpter extends CommonBaseAdapter<String> {
    int defItem = 0;

    public ScreenLvAdpter(Context context) {
        super(context);
    }

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = minflater.inflate(R.layout.listitem, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (defItem == i) {
            holder.commText.setTextColor(Color.parseColor("#48B4FD"));
        } else {
            holder.commText.setTextColor(Color.parseColor("#808080"));
        }
        holder.commText.setText(list.get(i));
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
