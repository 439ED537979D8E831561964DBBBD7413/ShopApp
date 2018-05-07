package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.BrandGroup;
import com.yj.shopapp.ubeen.IndustryCatelist;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.RVHolder;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.CommonUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class SBrandAdapter extends Common2Adapter {
    private View foootView;
    private int isFootview = 0;

    public SBrandAdapter(Context context) {
        super(context);
    }

    public SBrandAdapter(Context context, List list) {
        super(context, list);
    }

    /**
     * @param parent
     * @param viewType 0 content
     *                 1 头部
     *                 2 底部
     * @return
     */
    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.brandrecyview, parent, false);
                break;
            case 2:
                view = foootView;
                break;
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.brand_recy_itemview, parent, false);
                break;
        }
        return new RVHolder(view);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
//        switch (viewType) {
//            case 1:
//                return R.layout.brandrecyview;
//            case 2:
//                return R.layout.brand_recy_itemview;
//            default:
//                break;
//        }
        return 0;
    }

    @Override
    public void onBindViewHolder(final RVHolder holder, int position) {
        if (isFootview == 0) {
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
            if (isFooterViewPos(position)) return;
            onBindViewHolder(holder.getViewHolder(), position);
            if (onItemClickListener != null) {
                if (!isFooterViewPos(position)) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                        }
                    });
                }
            }
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isFooterViewPos(position)) return;
        if (list.get(position) instanceof BrandGroup.ListBean) {
            BrandGroup.ListBean bean = (BrandGroup.ListBean) list.get(position);
            if (bean.isSort()) {
                holder.getTextView(R.id.itemname).setText(bean.getName());
            } else {
                //setImg(holder.getSimpleDraweeView(R.id.simpleDraweeView), bean.getImgurl());
                Glide.with(context).load(bean.getImgurl()).apply(new RequestOptions().circleCrop()).into(holder.getSimpleDraweeView(R.id.simpleDraweeView));
                holder.getTextView(R.id.name_tv).setText(bean.getName());
            }
        } else {
            IndustryCatelist.DataBean.TagGroup group = (IndustryCatelist.DataBean.TagGroup) list.get(position);
            if (group.isSort()) {
                holder.getTextView(R.id.itemname).setText(group.getName());
            } else {
                Glide.with(context).load(group.getImgurl()).apply(new RequestOptions().circleCrop()).into(holder.getSimpleDraweeView(R.id.simpleDraweeView));
                //setImg(holder.getSimpleDraweeView(R.id.simpleDraweeView), group.getImgurl());
                holder.getTextView(R.id.name_tv).setText(group.getName());
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (isFooterViewPos(position)) {
            return 2;
        }
        if (isTitleView(position)) {
            return 1;
        }
        return 0;

    }

    private boolean isFooterViewPos(int position) {
        return position == getItemCount() - isFootview;
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size() + isFootview;
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
                    return isFooterViewPos(position) || isTitleView(position) ? gridLayoutManager.getSpanCount() : 1;
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

    public void setFoootView(View foootView) {
        this.foootView = foootView;
        isFootview = 1;
        notifyDataSetChanged();
    }

    private void setImg(SimpleDraweeView mImg, String url) {
        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(CommonUtils.dip2px(context, 45), CommonUtils.dip2px(context, 45)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(mImg.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .setImageRequest(request).build();
        mImg.setController(controller);
    }

    private AdapterView.OnItemClickListener onItemClickListener;

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
