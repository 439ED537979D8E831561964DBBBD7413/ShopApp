package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Extend;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LK on 2017/10/11.
 */

public class RecommendAdpter extends CommonBaseAdapter<Extend> implements View.OnClickListener {
    private Callback callback;
    int num, finishnum;
    boolean isshow = false;
    private List<Integer> mshowControl;

    public RecommendAdpter(Context context, Callback callback) {
        super(context);
        this.callback = callback;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = minflater.inflate(R.layout.activity_recommend, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (mshowControl.get(i) == 0) {
            holder.imag2.setRotation(0);
            holder.recRl1.setVisibility(View.GONE);
        } else {
            holder.imag2.setRotation(90);
            holder.recRl1.setVisibility(View.VISIBLE);
        }

        Extend extend = list.get(i);
        holder.recRl.setTag(i);
        if (extend.getShopname() == null) {
            holder.tv1.setText("暂无店铺信息");
        } else {
            holder.tv1.setText(extend.getShopname());
        }
        holder.tv2.setText(extend.getUsername());

        if (0 == extend.getRid()) {
            isshow = false;
            holder.tv3.setText("选择红包\u0020");
            holder.tv3.setVisibility(View.VISIBLE);
            holder.recRl1.setVisibility(View.GONE);
            holder.tv4.setVisibility(View.GONE);

        } else {
            holder.tv3.setVisibility(View.GONE);
            isshow = true;
            float money = Float.parseFloat(extend.getReward());
            holder.tv4.setVisibility(View.VISIBLE);
            holder.tv4.setText(((int) money) + "元红包");
        }

        int status = extend.getStatus();
        switch (status) {
            case 0:
                holder.tv4.setBackground(new ColorDrawable());
                break;
            case 1:
                holder.tv4.setText("领取红包");
                holder.tv4.setBackgroundResource(R.drawable.tv_bg_shape);
                isshow = false;
                holder.recRl1.setVisibility(View.GONE);
                break;
            case 2:
                holder.tv4.setText("已领取\u0020\u0020");
                isshow = false;
                holder.tv4.setBackground(new ColorDrawable());
                holder.recRl1.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        if (isshow) {
            holder.tv5.setText("完成额度:" + extend.getFinish());
            if (extend.getNum() >= 0) {
                num = extend.getNum();
            }
            if (extend.getFinish_num() >= 0) {
                finishnum = extend.getFinish_num();
            }
            holder.tv6.setText("未达标订单：" + (num - finishnum));
            holder.tv7.setText("订单：" + finishnum);

        }
        if (isshow) {
            holder.recRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (int) v.getTag();
                    if (mshowControl.get(i) == 0) {
                        mshowControl.set(i, 1);
                    } else {
                        mshowControl.set(i, 0);
                    }
                    notifyDataSetChanged();
                }
            });
        }
        holder.tv3.setOnClickListener(this);
        holder.tv3.setTag(i);
        holder.tv4.setOnClickListener(this);
        holder.tv4.setTag(i);
        return view;
    }

    public interface Callback {
        void click(View v);
    }

    public void setmshowControl(List<Integer> mshowControl) {
        this.mshowControl = mshowControl;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        callback.click(v);
    }

    static class ViewHolder {
        @BindView(R.id.tv_1)
        TextView tv1;
        @BindView(R.id.tv_2)
        TextView tv2;
        @BindView(R.id.tv_3)
        TextView tv3;
        @BindView(R.id.retv_4)
        TextView tv4;
        @BindView(R.id.rec_rl)
        RelativeLayout recRl;
        @BindView(R.id.tv_5)
        TextView tv5;
        @BindView(R.id.tv_6)
        TextView tv6;
        @BindView(R.id.tv_7)
        TextView tv7;
        @BindView(R.id.rec_rl_1)
        RelativeLayout recRl1;
        @BindView(R.id.imag2)
        ImageView imag2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
