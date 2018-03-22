package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.OrderChart;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.view.RoundView;

/**
 * Created by LK on 2018/3/16.
 *
 * @author LK
 */

public class PieChartAdpter extends CommonAdapter<OrderChart.ListBean> {
    public PieChartAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.piechartitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderChart.ListBean bean=list.get(position);
        holder.getTextView(R.id.name_tv).setText(bean.getClassX());
        holder.getTextView(R.id.money_tv).setText(bean.getMoney());
        holder.getTextView(R.id.pressent).setText(bean.getPercentage());
        ((RoundView)holder.getView(R.id.roundView)).setBgColor(Contants.PieColor.COLORS[position]);
    }
}
