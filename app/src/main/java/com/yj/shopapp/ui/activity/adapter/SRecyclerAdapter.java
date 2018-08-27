package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.util.GlideCircleTransform;
import com.yj.shopapp.view.CircleImageView;

import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;


/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class SRecyclerAdapter extends RecyclerView.Adapter<SRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<Industry> industries;
    private OnItemChildViewOnClickListener listener;

    public SRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public SRecyclerAdapter(Context mContext, List<Industry> industries) {
        this.mContext = mContext;
        this.industries = industries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Industry mdatas = industries.get(position);
        holder.nameTv.setText(mdatas.getName());
        if (0 == mdatas.getResult()) {
            Glide.with(mContext).load(mdatas.getUrl()).apply(new RequestOptions().transform(new GlideCircleTransform())).into(holder.simpleDraweeView);
        } else {
            Glide.with(mContext).load(mdatas.getUrl()).into(holder.simpleDraweeView);
        }
        if (mdatas.getRebate() == 1) {
            holder.badge.setBadgeText("返");
        }else {
            holder.badge.hide(true);
        }
        if (listener != null) {
            holder.itemview.setOnClickListener(v -> listener.onChildViewClickListener(v, holder.getPosition()));
        }
    }

    @Override
    public int getItemCount() {
        return industries == null ? 0 : industries.size();
    }

    public void setListener(OnItemChildViewOnClickListener listener) {
        this.listener = listener;
    }

    public void setIndustries(List<Industry> industries) {
        this.industries = industries;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView simpleDraweeView;
        TextView nameTv;
        RelativeLayout itemview;
        Badge badge;

        public ViewHolder(View view) {
            super(view);
            simpleDraweeView = view.findViewById(R.id.simpleDraweeView);
            nameTv = view.findViewById(R.id.name_tv);
            itemview = view.findViewById(R.id.itemview);
            badge = new QBadgeView(mContext).bindTarget(itemview);
            badge.setBadgeGravity(Gravity.END | Gravity.TOP);
        }
    }
}
