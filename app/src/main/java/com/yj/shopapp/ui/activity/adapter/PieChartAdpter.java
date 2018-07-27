package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Color;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.OrderChart;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.view.RoundView;

import java.util.Random;

/**
 * Created by LK on 2018/3/16.
 *
 * @author LK
 */

public class PieChartAdpter extends CommonAdapter<OrderChart.ListBean> {
    public PieChartAdpter(Context context) {
        super(context);
    }

    private Random random = new Random();

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.piechartitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderChart.ListBean bean = list.get(position);
        holder.getTextView(R.id.name_tv).setText(bean.getClassX());
        holder.getTextView(R.id.money_tv).setText(String.format("￥%s", bean.getMoney()));
        holder.getTextView(R.id.pressent).setText(bean.getPercentage());
        if (position < 10) {
            ((RoundView) holder.getView(R.id.roundView)).setBgColor(Contants.PieColor.COLORS[position]);
        } else {
            ((RoundView) holder.getView(R.id.roundView)).setBgColor(randomColor());
        }
        if (position < 3) {
            holder.getTextView(R.id.money_tv).setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.getTextView(R.id.money_tv).setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    private int randomColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }
}
