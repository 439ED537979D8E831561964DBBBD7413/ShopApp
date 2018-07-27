package com.yj.shopapp.ui.activity.shopkeeper;


import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.MyBuGood;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.util.DateUtils;


public class MyItemRecyclerViewAdapter extends Common2Adapter<MyBuGood.ListsBean> implements View.OnClickListener {
    private SparseArray<CountDownTimer> count = new SparseArray<>();
    private OnItemChildViewOnClickListener listener;
    private String[] statuds = {"自动作废", "等待配送", "正在配送", "交易完成"};

    public MyItemRecyclerViewAdapter(Context context, OnItemChildViewOnClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.fragment_item;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MyBuGood.ListsBean bean = list.get(position);
        Glide.with(context).load(bean.getImgurl()).into(holder.getSimpleDraweeView(R.id.shopimag));
        holder.getTextView(R.id.shopname).setText(bean.getName());
        long time = DateUtils.ContrastTime(bean.getCanceltime());
        final TextView account_tv = holder.getTextView(R.id.account_tv);
        final TextView cancletv = holder.getTextView(R.id.cancelButton);
        if (time != -1) {
            cancletv.setVisibility(View.VISIBLE);
            holder.getTextView(R.id.status_tv).setVisibility(View.GONE);
            //holder.getTextView(R.id.account_tv).setText(String.format("可在%s前取消订单", DateUtils.timet(String.valueOf(bean.getCanceltime()), "HH:mm")));
            //holder.getTextView(R.id.account_tv).setText("daojishi");
            //holder.countDownTimer = count.get(count.keyAt(position));
            if (holder.countDownTimer != null) {
                holder.countDownTimer.cancel();
            }
            holder.countDownTimer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    account_tv.setText(Html.fromHtml("<font color=red>" + millisUntilFinished / 1000 + "s" + "</font>" + "   内可取消"));
                }

                @Override
                public void onFinish() {
                    account_tv.setText(DateUtils.timet(String.valueOf(bean.getAddtime())));
                    cancletv.setVisibility(View.GONE);
                    holder.getTextView(R.id.status_tv).setVisibility(View.VISIBLE);
                    holder.getTextView(R.id.status_tv).setText(statuds[bean.getStatus()]);
                }
            }.start();
            count.put(account_tv.hashCode(), holder.countDownTimer);
        } else {
            account_tv.setText(DateUtils.timet(bean.getAddtime()));
            cancletv.setVisibility(View.GONE);
            holder.getTextView(R.id.status_tv).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.status_tv).setText(statuds[bean.getStatus()]);
        }
        holder.getTextView(R.id.shopnum).setText(String.format("数量：%s", bean.getItemcount()));
        holder.getTextView(R.id.allprice).setText(Html.fromHtml("合计:" + "<font color=red>" + "￥"+bean.getMoneysum() + "</fon>"));
        cancletv.setOnClickListener(this);
        cancletv.setTag(position);
        holder.getView(R.id.itemview).setOnClickListener(this);
        holder.getView(R.id.itemview).setTag(position);
    }

    /**
     * 清除倒计时
     */
    public void closeCountDownTimers() {
        if (count == null) {
            return;
        }
        for (int i = 0, length = count.size(); i < length; i++) {
            CountDownTimer cdt = count.get(count.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

    @Override
    public void onClick(View v) {
        listener.onChildViewClickListener(v, (Integer) v.getTag());
    }
}
