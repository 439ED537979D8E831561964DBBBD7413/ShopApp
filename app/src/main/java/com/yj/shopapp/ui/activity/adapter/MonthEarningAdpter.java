package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.View;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.MonthEarning;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

/**
 * Created by LK on 2018/8/23.
 *
 * @author LK
 */
public class MonthEarningAdpter extends Common2Adapter<MonthEarning> {
    public MonthEarningAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.itemview_monthearning;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MonthEarning earning = list.get(position);
        holder.getTextView(R.id.shopname_tv).setText(String.format("%s收益", earning.getMonth()));
        if (earning.getStatus().equals("0")) {
            holder.getTextView(R.id.status_tv).setText("未发放");
            holder.getTextView(R.id.profit_tv).setText(String.format("￥%s", earning.getMoney()));
            //holder.getTextView(R.id.time_tv).setText(DateUtils.timet(earning.getUpdatetime()));
            holder.getTextView(R.id.time_tv).setVisibility(View.GONE);
        } else {
            holder.getTextView(R.id.status_tv).setText("已发放");
            holder.getTextView(R.id.profit_tv).setText(String.format("+%s", earning.getMoney()));
            holder.getTextView(R.id.time_tv).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.time_tv).setText(DateUtils.timet(earning.getUpdatetime()));
        }
    }
}
