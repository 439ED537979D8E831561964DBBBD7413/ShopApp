package com.yj.shopapp.ui.activity.ImgUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.yj.shopapp.util.ReturnView;

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
    public static final int TYPE_NORMAL = 0;  //默认的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_HEAD = 2;//带有头部
    //上拉加载的状态
    private int loadstate = 1;//默认加载完成
    public final int LOADING = 1;//正在加载
    public final int FINISH = 2;// 加载完成
    public final int END = 3;  //没有更多数据了(显示另一footer)
    private ReturnView returnView;

    public CommonAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
        returnView = new ReturnView(context.getApplicationContext());
    }

    public CommonAdapter(Context context) {
        this.context = context;
        returnView = new ReturnView(context.getApplicationContext());
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_NORMAL:
                view = LayoutInflater.from(context).inflate(onCreateViewLayoutID(viewType), null, false);
                break;
            case TYPE_FOOTER:
                view = returnView.init();
                break;
            case TYPE_HEAD:
                view = mHeadView;
                break;
            default:
                break;
        }
        return new RVHolder(view);
    }

    public abstract int onCreateViewLayoutID(int viewType);

    public void setLoadstate(int loadstate) {//用于动态设置加载状态
        this.loadstate = loadstate;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final RVHolder holder, int position) {
        if (isFootview == 0 && isHeadView == 0) {
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
                switch (loadstate) {
                    case LOADING:
                        returnView.showLoadingView();
                        break;
                    case FINISH:
                        returnView.showFinishView();
                        break;
                    case END:
                        returnView.showErrorView();
                        break;
                    default:
                        break;
                }
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

    private boolean isHeaderViewPos(int position) {
        return position < isHeadView;
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


    public void setmHeadView(View mHeadView) {
        this.mHeadView = mHeadView;
        isHeadView = 1;
        notifyDataSetChanged();
    }

    public void setmFooterView() {
        isFootview = 1;
        notifyDataSetChanged();
    }

    public void removeFooterView() {
        isFootview = 0;
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
