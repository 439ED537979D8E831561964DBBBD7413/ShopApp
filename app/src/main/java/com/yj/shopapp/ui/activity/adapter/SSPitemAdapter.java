package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.Spitem;
import com.yj.shopapp.ui.activity.ImgUtil.CommonAdapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.util.DateUtils;

/**
 * Created by jm on 2016/4/25.
 */
public class SSPitemAdapter extends CommonAdapter<Spitem> implements View.OnClickListener {
    private String str = "";
    private OnViewClickListener listener;

    public SSPitemAdapter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.sactivity_spitemgoods_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Spitem spitem = list.get(position);
        holder.getTextView(R.id.orderName).setText(spitem.getItemname());
        holder.getTextView(R.id.startDate).setText(DateUtils.timed(spitem.getTime1()) + " - " + DateUtils.timed(spitem.getTime2()));
        if (spitem.getSales().equals("1")) {
            str = "满" + spitem.getDisstr() + "送" + spitem.getGift();
            holder.getTextView(R.id.orderContent).setText(str);
        } else if (spitem.getSales().equals("2")) {
            str = "打" + Double.parseDouble(spitem.getDisstr()) / 10 + "折";
            holder.getTextView(R.id.orderContent).setText(str);
        } else if (spitem.getSales().equals("3")) {
            String html = "促销价：" + "<font color='red'>" + "￥" + spitem.getDisstr() + "</font> ";
            CharSequence charSequence = Html.fromHtml(html);
            holder.getTextView(R.id.orderContent).setText(charSequence);
        }
        if (spitem.getPrice() != null) {
            holder.getTextView(R.id.front_price).setText("￥" + spitem.getPrice());
            holder.getTextView(R.id.front_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        TextView addcartTv = holder.getTextView(R.id.addcartTv);
        addcartTv.setOnClickListener(this);
        addcartTv.setTag(position);
        if (spitem.getSale_status() != null) {
            if (spitem.getSale_status().equals("0")) {
                addcartTv.setBackgroundResource(R.drawable.goodcar_bg_2);
                holder.getSimpleDraweeView(R.id.stop_simpleDraweeView).setImageResource(R.drawable.pause);
                addcartTv.setClickable(false);
            } else if (spitem.getSale_status().equals("1") || spitem.getSale_status().equals("null")) {
                addcartTv.setBackgroundResource(R.drawable.goodcar_bg);
                holder.getSimpleDraweeView(R.id.stop_simpleDraweeView).setImageResource(R.drawable.img_ic_promotion);
            }
        } else {
            addcartTv.setBackgroundResource(R.drawable.goodcar_bg);
            holder.getSimpleDraweeView(R.id.stop_simpleDraweeView).setImageResource(R.drawable.img_ic_promotion);
        }
        Glide.with(context).load(spitem.getImgurl()).into(holder.getSimpleDraweeView(R.id.simpleDraweeView));
        holder.getView(R.id.recy_item).setOnClickListener(this);
        holder.getView(R.id.recy_item).setTag(position);
    }


    @Override
    public void onClick(View v) {
        listener.onItemClick(v, (Integer) v.getTag());
    }

    public void setListener(OnViewClickListener listener) {
        this.listener = listener;
    }

    public interface OnViewClickListener {
        void onItemClick(View view, int position);
    }
}
