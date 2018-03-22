package com.yj.shopapp.ui.activity.ImgUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;


/**
 * Created by lk on 2017/8/7.
 */

public abstract class Common2Adapter<T> extends RecyclerView.Adapter<RVHolder> {
    protected List<T> list;
    protected Context context;

    public Common2Adapter(Context context, List list) {
        this.context = context;
        this.list = list;

    }

    public Common2Adapter(Context context) {
        this.context = context;
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(onCreateViewLayoutID(viewType), parent, false);
        return new RVHolder(view);
    }

    public abstract int onCreateViewLayoutID(int viewType);

    @Override
    public void onBindViewHolder(final RVHolder holder, int position) {
            onBindViewHolder(holder.getViewHolder(), position);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                    }
                });
            }
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return list;
    }

    public abstract void onBindViewHolder(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();

    }

    private AdapterView.OnItemClickListener onItemClickListener;

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
