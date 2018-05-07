package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.BrandGroup;
import com.yj.shopapp.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LK on 2017/10/29.
 *
 * @author LK
 */

public class Lift3Adpter extends CommonBaseAdapter<BrandGroup.ListBean> {
    int defItem = 0;

    public Lift3Adpter(Context context) {
        super(context);
    }

    public Lift3Adpter(Context context, List<BrandGroup.ListBean> list) {
        super(context, list);
    }

    /**
     * 适配器中添加这个方法
     */
    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    public int getDefItem() {
        return defItem;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.textviewandimg, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (defItem == i) {
            holder.verticalLine.setVisibility(View.VISIBLE);
            holder.commText.setTextColor(context.getResources().getColor(R.color.color_01ABFF));
        } else {
            holder.verticalLine.setVisibility(View.INVISIBLE);
            holder.commText.setTextColor(Color.parseColor("#333333"));
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
