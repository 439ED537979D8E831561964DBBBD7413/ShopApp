package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.LimitedSale;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.view.SaleProgressView;
import com.yj.shopapp.view.mTextView;

import java.util.List;

/**
 * Created by LK on 2018/4/3.
 *
 * @author LK
 */

public class BuGoodDetailsAdpter extends CommonAdapter<LimitedSale> implements View.OnClickListener {
    private SparseArray<CountDownTimer> countDownTimers = new SparseArray<>();
    private OnItemChildViewOnClickListener listener;

    public BuGoodDetailsAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.view_bugooditem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LimitedSale sale = list.get(position);
        holder.getTextView(R.id.shopname).setText(sale.getName());
        SaleProgressView saleProgressView = (SaleProgressView) holder.getView(R.id.spv);
        saleProgressView.setTotalAndCurrentCount(Integer.parseInt(sale.getNum()), Integer.parseInt(sale.getSalesnum()), sale.getUnit());
        holder.getTextView(R.id.Limit_shopping).setText(String.format("限购%1$s%2$s | 共%3$s%4$s", sale.getItemcount(), sale.getUnit(), sale.getNum(), sale.getUnit()));
        fontLarger("￥" + sale.getUnitprice(), holder.getTextView(R.id.Purchase_price));
        holder.getTextView(R.id.Original_price).setText(String.format("￥%s", sale.getPrice()));
        holder.getTextView(R.id.Original_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        RelativeLayout layout = (RelativeLayout) holder.getView(R.id.ceshi);
        layout.setOnClickListener(this);
        layout.setTag(position);
        TextView gorob = holder.getTextView(R.id.gorob);
        switch (sale.getStatus()) {
            case "1":
                layout.setVisibility(View.VISIBLE);
                layout.removeAllViews();
                if (sale.getIs_sale() == 1) {
                    gorob.setBackground(context.getResources().getDrawable(R.drawable.fillet_tv_gray));
                } else {
                    gorob.setBackground(context.getResources().getDrawable(R.drawable.fillet_tv_red));
                }

                holder.getView(R.id.content1).setVisibility(View.VISIBLE);
                layout.addView(getTextView(sale.getList()));
                Glide.with(context).load(sale.getImgurl()).apply(new RequestOptions().placeholder(R.drawable.load)).into(holder.getSimpleDraweeView(R.id.shopimag));
                break;
            case "3":
                holder.getTextView(R.id.limet_tips).setText(String.format("限购%1$s%2$s | 共%3$s%4$s", sale.getItemcount(), sale.getUnit(), sale.getNum(), sale.getUnit()));
                gorob.setVisibility(View.GONE);
                final TextView cdTv = holder.getTextView(R.id.CountdownTv);
                cdTv.setVisibility(View.VISIBLE);
                cdTv.setOnClickListener(this);
                cdTv.setTag(position);

                if (holder.getCountDown() != null) {
                    holder.getCountDown().cancel();
                }
                holder.countDownTimer = new CountDownTimer(DateUtils.ContrastTime(Long.parseLong(sale.getStart())), 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        cdTv.setText(DateUtils.timed(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        cdTv.setText("倒计时完成");
                    }
                }.start();
                countDownTimers.put(cdTv.hashCode(), holder.countDownTimer);
                holder.getView(R.id.content3).setVisibility(View.VISIBLE);
                Glide.with(context).load(sale.getImgurl()).apply(new RequestOptions().placeholder(R.drawable.load)).into(holder.getSimpleDraweeView(R.id.shopimag));
                break;
        }
        holder.getView(R.id.itemview).setOnClickListener(this);
        holder.getView(R.id.itemview).setTag(position);
        gorob.setOnClickListener(this);
        gorob.setTag(position);


    }

    private TextView getTextView(List<String> mList) {
        TextView mTextView = new mTextView(context);
        mTextView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTextView.setText(list2str(mList));
        mTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mTextView.setSingleLine(true);
        mTextView.setFocusable(true);
        mTextView.setTextSize(12f);
        mTextView.setGravity(Gravity.CENTER_VERTICAL);
        mTextView.setFocusableInTouchMode(true);
        mTextView.setSelected(true);
        return mTextView;

    }

    private String list2str(List<String> mList) {
        StringBuilder builder = new StringBuilder();
        for (String s : mList) {
            builder.append(s);
            builder.append("\t\t\t\t");
        }
        return builder.toString();
    }

    private void fontLarger(String test, TextView textView) {
        SpannableStringBuilder builder = new SpannableStringBuilder(test);
        builder.setSpan(new RelativeSizeSpan(1.6f), 1, test.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if (textView != null) {
            textView.setText(builder);
        }
    }

    public void setListener(OnItemChildViewOnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 清除倒计时
     */
    public void closeCountDownTimers() {
        if (countDownTimers == null) {
            return;
        }
        for (int i = 0, length = countDownTimers.size(); i < length; i++) {
            CountDownTimer cdt = countDownTimers.get(countDownTimers.keyAt(i));
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
