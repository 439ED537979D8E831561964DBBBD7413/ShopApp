package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.BrandGroup;
import com.yj.shopapp.ubeen.IndustryCatelist;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class SBrandAdapter extends Common2Adapter {
    private int mGid = -1;

    public SBrandAdapter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        switch (viewType) {
            case 1:
                return R.layout.brandrecyview;
            case 2:
                return R.layout.category_item;
            case 3:
                return R.layout.viewclickload;
            default:
                break;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list.get(position) instanceof BrandGroup.ListBean) {
            BrandGroup.ListBean bean = (BrandGroup.ListBean) list.get(position);
            if (bean.isSort()) {
                holder.getTextView(R.id.itemname).setText(bean.getName());
            } else if (bean.isFoot()) {
                if (bean.getGid().equals(mGid + "")) {
                    Glide.with(context).load(R.drawable.ic_close).into(holder.getImageView(R.id.centerImag));
                } else {
                    Glide.with(context).load(R.drawable.ic_open).into(holder.getImageView(R.id.centerImag));
                }
                holder.getTextView(R.id.itemname).setText(bean.getName().substring(0, 2));
                holder.getTextView(R.id.itemnameRight).setText(bean.getName().substring(2));
            } else {
                Glide.with(context).load(bean.getImgurl()).into(holder.getImageView(R.id.simpleDraweeView));
                holder.getTextView(R.id.name_tv).setText(bean.getName());
            }
        } else {
            IndustryCatelist.DataBean.TagGroup group = (IndustryCatelist.DataBean.TagGroup) list.get(position);
            if (group.isSort()) {
                holder.getTextView(R.id.itemname).setText(group.getName());
            } else {
                Glide.with(context).load(group.getImgurl()).into(holder.getImageView(R.id.simpleDraweeView));
                holder.getTextView(R.id.name_tv).setText(group.getName());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (isTitleView(position)) {
            return 1;
        } else if (isFootView(position)) {
            return 3;
        } else {
            return 2;
        }
    }

    /**
     * 添加支持GridLayoutManager * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isTitleView(position) || isFootView(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    private boolean isTitleView(int position) {
        if (list.size() > 0) {
            if (list.get(position) instanceof BrandGroup.ListBean) {
                return ((BrandGroup.ListBean) list.get(position)).isSort();
            } else {
                return ((IndustryCatelist.DataBean.TagGroup) list.get(position)).isSort();
            }
        }
        return false;
    }

    private boolean isFootView(int position) {
        if (list.size() > 0) {
            if (list.get(position) instanceof BrandGroup.ListBean) {
                return ((BrandGroup.ListBean) list.get(position)).isFoot();
            }
            return false;
        }
        return false;
    }

    public void setImagRotate(int gid) {
        this.mGid = gid;
    }
}
