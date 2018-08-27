package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.ClientOrder;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

/**
 * Created by LK on 2018/8/9.
 *
 * @author LK
 */
public class ClientOrderListAdpter extends Common2Adapter<ClientOrder.ListsBean> {
    public ClientOrderListAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.itemview_clentlist;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClientOrder.ListsBean bean = list.get(position);
        holder.getTextView(R.id.client_name).setText(bean.getShopname());
        holder.getTextView(R.id.money_tv).setText(String.format("￥%S", bean.getProfit()));
    }

}
