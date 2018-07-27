package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.NewOrder;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.view.CircleImageView;

import java.util.List;


/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class SorderItemAdapter extends Common2Adapter<NewOrder.DataBean> {
    public SorderItemAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.neworderitemview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RelativeLayout rl = (RelativeLayout) holder.getView(R.id.itemview);
        rl.setLayoutParams(new RelativeLayout.LayoutParams(CommonUtils.screenWidth(context) / 4, ViewGroup.LayoutParams.WRAP_CONTENT));
        NewOrder.DataBean bean = list.get(position);
        holder.getTextView(R.id.name_tv).setText(String.format("%s件%s元", bean.getItemnum(), bean.getMoney()));
        holder.getTextView(R.id.edit_img).setText(bean.getName());
        CircleImageView circleImageView = (CircleImageView) holder.getView(R.id.simpleDraweeView);
        Glide.with(context).load(bean.getImgurl()).into(circleImageView);
    }
}
