package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.text.Html;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.ShopHistory;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

/**
 * Created by LK on 2018/4/28.
 *
 * @author LK
 */
public class ShopHistoryAdpter extends Common2Adapter<ShopHistory> {

    public ShopHistoryAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.shophistoryitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopHistory history = list.get(position);
        holder.getTextView(R.id.shopname).setText(DateUtils.timet(history.getAddtime(), "yyyy.MM.dd HH:ss"));
        holder.getTextView(R.id.orderNumber).setText(String.format("订单号：%s", history.getOid()));
        holder.getTextView(R.id.shopnum).setText(String.format("数量：%1$s%2$s", history.getItemcount(), history.getUnit()));
        holder.getTextView(R.id.shopprice).setText(String.format("单价：￥%s", history.getUnitprice()));
        holder.getTextView(R.id.allprice).setText(Html.fromHtml("总计：" + "<font color=red>" + "￥" + history.getMoneysum() + "</font>"));
    }
}
