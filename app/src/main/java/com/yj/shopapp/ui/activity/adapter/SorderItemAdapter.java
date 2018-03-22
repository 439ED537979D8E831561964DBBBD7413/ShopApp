package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.NewOrder;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.CircleImageView;

import java.util.List;


/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class SorderItemAdapter extends CommonAdapter<NewOrder.DataBean> {
    public SorderItemAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.neworderitemview;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewOrder.DataBean bean = list.get(position);
        holder.getTextView(R.id.name_tv).setText(String.format("共%1$s件 ￥%2$s", bean.getItemnum(), bean.getMoney()));
        holder.getTextView(R.id.edit_img).setText(bean.getName());
        CircleImageView circleImageView = (CircleImageView) holder.getView(R.id.simpleDraweeView);
        Glide.with(context).load(PreferenceUtils.Json2map(context, "imagurl").get(bean.getId())).into(circleImageView);
    }
}
