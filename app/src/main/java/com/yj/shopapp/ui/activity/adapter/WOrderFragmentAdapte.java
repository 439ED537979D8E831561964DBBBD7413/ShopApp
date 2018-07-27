package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.RVHolder;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.wbeen.WOrderNewDetails;

/**
 * Created by LK on 2017/12/20.
 *
 * @author LK
 */

public class WOrderFragmentAdapte extends Common2Adapter<WOrderNewDetails.ItemlistBean> {
    private final static int contentview = 0;
    private final static int foortView = 1;
    private View foootView;
    private int isFootview = 0;

    public WOrderFragmentAdapte(Context context) {
        super(context);
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case contentview:
                view = LayoutInflater.from(context).inflate(R.layout.worderfragmentlayout, parent, false);
                break;
            case foortView:
                view = foootView;
                break;
        }
        return new RVHolder(view);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        switch (viewType) {
            case contentview:
                return R.layout.orderfragmentlayout;
            case foortView:
                break;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isFooterViewPos(position)) {
            return;
        }
        WOrderNewDetails.ItemlistBean bean = list.get(position);
        holder.getTextView(R.id.itemname).setText(bean.getItemname());
        holder.getTextView(R.id.itemsum).setText(String.format("数量：%s%s", bean.getItemcount(),bean.getUnit()));
        holder.getTextView(R.id.itemmoery).setText(Html.fromHtml("金额：" + "<font color=#FE3000>" +"￥"+ bean.getMoneysum() + "</font>"));
        Glide.with(context).load(bean.getImageUrl()).apply(new RequestOptions().centerCrop()).into(holder.getImageView(R.id.itemimg));
        holder.getTextView(R.id.class_name).setText(bean.getName());

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size() + isFootview;
    }

    private boolean isFooterViewPos(int position) {
        return position == getItemCount() - isFootview;
    }

    public void setFoootView(View foootView) {
        this.foootView = foootView;
        isFootview = 1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterViewPos(position)) {
            return foortView;
        } else {
            return contentview;
        }
    }
}
