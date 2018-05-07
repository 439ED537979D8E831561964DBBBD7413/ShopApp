package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.AccountBook;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;

/**
 * Created by LK on 2018/3/10.
 *
 * @author LK
 */

public class AccountBookAdpter extends Common2Adapter<AccountBook> {
    public AccountBookAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.accountbookitem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AccountBook book = list.get(position);
        if (book.getChangetype().equals("1")) {
            //微信
            Glide.with(context).load(R.drawable.weixin).into(holder.getImageView(R.id.itemimag));
            holder.getTextView(R.id.account_tv).setText(String.format("微信：%s", book.getAccount()));
        } else {
            Glide.with(context).load(R.drawable.zhifubao).into(holder.getImageView(R.id.itemimag));
            holder.getTextView(R.id.account_tv).setText(String.format("支付宝：%s", book.getAccount()));
        }
        holder.getTextView(R.id.integral_tv).setText(String.format("消耗积分: %1$s    返点: %2$s", book.getIntegral(), CommonUtils.dec2perc(Double.valueOf(book.getRule()))));
        holder.getTextView(R.id.time_tv).setText(DateUtils.timet(book.getDotime(), "yyyy年MM月dd日  HH:mm:ss"));
        TextView tv = holder.getTextView(R.id.status_tv);
        TextView moneytv = holder.getTextView(R.id.money_tv);
        if (book.getStatus().equals("1")) {
            tv.setTextColor(context.getResources().getColor(R.color.colorcfc6f74));
            tv.setText("处理中");
            moneytv.setTextColor(context.getResources().getColor(R.color.color_999999));
        } else {
            tv.setTextColor(context.getResources().getColor(R.color.colorc666666));
            tv.setText("已到账");
            moneytv.setTextColor(context.getResources().getColor(R.color.colorcfc6f74));
        }
        moneytv.setText(String.format("%s元", String.valueOf((int) Double.parseDouble(book.getMoney()))));
    }
}
