package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.IntegralChange;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

/**
 * From ShopApp
 * Created by wxq on 17/4/5.
 * <p>
 * Beautiful Life ～
 */

public class IntegralChangeAdapter extends CommonAdapter<IntegralChange> {
    private String[] status = {"未审核", "提现中", "兑现成功", "兑现失败"};

    public IntegralChangeAdapter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_integral_change;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IntegralChange integral = list.get(position);
        holder.getTextView(R.id.account_tv).setText(integral.getAccountnumber());
        holder.getTextView(R.id.integral_tv).setText(integral.getIntegral());
        holder.getTextView(R.id.money_tv).setText(integral.getRmb());
        holder.getTextView(R.id.change_type_tv).setText(integral.getChangetype() == 1 ? "支付宝" : "微信");
        holder.getTextView(R.id.status_tv).setText(status[integral.getStatus() - 1]);
        holder.getTextView(R.id.time_tv).setText(DateUtils.timet(integral.getChangetime() + ""));
        holder.getTextView(R.id.content_tv).setText(integral.getRemark() == null ? "" : "(" + integral.getRemark() + ")");
    }
}
