package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.view.CircleImageView;
import com.yj.shopapp.wbeen.WNewOrder;

import java.util.List;


/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class WorderItemAdapter extends Common2Adapter<WNewOrder.ClasslistBean> {
    public WorderItemAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.neworderitemview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RelativeLayout rl = (RelativeLayout) holder.getView(R.id.itemview);
        rl.setBackgroundColor(context.getResources().getColor(R.color.colorf5f5f5));
        rl.setLayoutParams(new RelativeLayout.LayoutParams(CommonUtils.screenWidth(context) / 4, ViewGroup.LayoutParams.WRAP_CONTENT));
        WNewOrder.ClasslistBean bean = list.get(position);
        holder.getTextView(R.id.name_tv).setText(String.format("%s件%s元", bean.getNum(), bean.getMoney()));
        holder.getTextView(R.id.edit_img).setText(bean.getName());
        CircleImageView circleImageView = (CircleImageView) holder.getView(R.id.simpleDraweeView);
        Glide.with(context).load(bean.getImgurl()).into(circleImageView);
    }
}
