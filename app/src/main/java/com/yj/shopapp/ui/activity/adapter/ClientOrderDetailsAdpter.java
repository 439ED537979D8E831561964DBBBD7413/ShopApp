package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.ClientOrderDetails;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2018/8/22.
 *
 * @author LK
 */
public class ClientOrderDetailsAdpter extends Common2Adapter<ClientOrderDetails.ContentsBean> {


    public ClientOrderDetailsAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.itemview_clentdatils;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClientOrderDetails.ContentsBean bean = list.get(position);
        holder.getTextView(R.id.industry_name).setText(String.format("%s:", bean.getName()));
        holder.getTextView(R.id.all_price_tv).setText(String.format("￥%s", bean.getMoney()));
        holder.getTextView(R.id.profit_tv).setText(String.format("￥%s", bean.getProfit()));
    }
}
