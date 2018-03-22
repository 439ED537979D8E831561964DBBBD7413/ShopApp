package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.view.CircleImageView;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by LK on 2017/10/29.
 *
 * @author LK
 */

public class Lift3Adpter extends CommonBaseAdapter<Industry> {
    int defItem = -1;

    public Lift3Adpter(Context context) {
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
            view = minflater.inflate(R.layout.textviewandimg, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (defItem == i) {
            view.setBackgroundColor(context.getResources().getColor(R.color.all_bg));
            holder.verticalLine.setVisibility(View.VISIBLE);
            holder.commText.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.verticalLine.setVisibility(View.INVISIBLE);
            holder.commText.setTextColor(Color.parseColor("#757575"));
        }
        holder.commText.setText(list.get(i).getName());
        holder.imageView.setVisibility(View.GONE);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.comm_text)
        TextView commText;
        @BindView(R.id.imageView)
        CircleImageView imageView;
        @BindView(R.id.vertical_line)
        View verticalLine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
