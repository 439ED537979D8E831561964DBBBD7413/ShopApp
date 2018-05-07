package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.IndClass;
import com.yj.shopapp.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LK on 2017/10/29.
 *
 * @author LK
 */

public class Lift2Adpter extends CommonBaseAdapter<IndClass> {
    int defItem = -1;

    public Lift2Adpter(Context context) {
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
            view.setBackgroundColor(context.getResources().getColor(R.color.skyblue));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.all_bg));
        }
        holder.commText.setText(list.get(i).getName());
        Glide.with(context).load(list.get(i).getImgurl()).into(holder.imageView);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.comm_text)
        TextView commText;
        @BindView(R.id.imageView)
        CircleImageView imageView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
