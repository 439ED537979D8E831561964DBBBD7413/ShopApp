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

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<RVHolder> {
    protected List<T> list;
    protected Context context;
    protected View mFooterView;
    protected View mHeadView;
    private int isFootview = 0;
    private int isHeadView = 0;
    public static final int TYPE_NORMAL = 1;  //默认的
    public static final int TYPE_FOOTER = 2;  //说明是带有Footer的
    public static final int TYPE_HEAD = 3;//带有头部

    public CommonAdapter(Context context, List list) {
        this.context = context;
        this.list = list;

    }

    public CommonAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_NORMAL:
                view = LayoutInflater.from(context).inflate(onCreateViewLayoutID(viewType), null, false);
                break;
            case TYPE_FOOTER:
                view = mFooterView;
                break;
            case TYPE_HEAD:
                view = mHeadView;
                break;
        }
        return new RVHolder(view);
    }

    public abstract int onCreateViewLayoutID(int viewType);

    @Override
    public void onBindViewHolder(final RVHolder holder, int position) {
        if (mFooterView == null && mHeadView == null) {
            onBindViewHolder(holder.getViewHolder(), position);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                    }
                });
            }
        } else {
            if (isFooterViewPos(position)) {
                return;
            }
            if (isHeaderViewPos(position)) {
                return;
            }
            onBindViewHolder(holder.getViewHolder(), position - isHeadView);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                    }
                });
            }
        }

    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return list;
    }

    private boolean isFooterViewPos(int position) {
        return position == getItemCount() - isFootview;
    }


    public abstract void onBindViewHolder(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size() + isFootview + isHeadView;

    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return TYPE_HEAD;
        }
        if (isFooterViewPos(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }


    private boolean isHeaderViewPos(int position) {
        return position < isHeadView;
    }

    public void setmHeadView(View mHeadView) {
        this.mHeadView = mHeadView;
        isHeadView = 1;
        notifyDataSetChanged();
    }

    public void setmFooterView(View mFooterView) {
        this.mFooterView = mFooterView;
        isFootview = 1;
        notifyDataSetChanged();
    }

    public void removeFooterView() {
        isFootview=0;
        notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener onItemClickListener;

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
