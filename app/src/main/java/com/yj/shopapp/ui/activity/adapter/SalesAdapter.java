package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;
import com.yj.shopapp.wbeen.SPlist;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by jm on 2016/4/25.
 */
public class SalesAdapter implements IRecyclerViewIntermediary {


    private Context mContext;

    private List<SPlist> notes;

    private BaseRecyclerView mListener;

    public SalesAdapter(Context context, List<SPlist> noteList, BaseRecyclerView myItemClickListener) {

        mContext = context;

        notes = noteList;

        mListener = myItemClickListener;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, int type) {
        View v = View.inflate(viewGroup.getContext(), R.layout.wactivity_sales_item, null);
        //make sure it fills the space
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new HomeFragmentViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        HomeFragmentViewHolder holder = (HomeFragmentViewHolder) viewHolder;
        SPlist sPlist = notes.get(position);
        System.out.println(sPlist.getTime1() + "_____position______-" + sPlist.getTime2());
        holder.orderName.setText(sPlist.getItemname());
        String str = "";
        if (sPlist.getSales().equals("1")) {
            str = "满" + sPlist.getDisstr() + "送" + sPlist.getGift();
            holder.orderContent.setText(str);
        } else if (sPlist.getSales().equals("2")) {
            str = "打" + Float.parseFloat(sPlist.getDisstr()) / 10 + "折";
            holder.orderContent.setText(str);
        } else if (sPlist.getSales().equals("3")) {
            String html = "促销价：" + "<font color='red'>" + sPlist.getDisstr() + "</font> " + "元";
            CharSequence charSequence = Html.fromHtml(html);
            holder.orderContent.setText(charSequence);
            //str="促销价："+sPlist.getDisstr();
        }
        //ShowLog.e(sPlist.getSale_status());
        if (!sPlist.getStatus().equals("0")) {
            holder.RightSimpleDraweeView.setImageResource(Contants.StateImg[Integer.parseInt(sPlist.getStatus())]);
        }

        if (sPlist.getSale_status() != null) {
            if (sPlist.getSale_status().equals("0")) {
                holder.stop_simpleDraweeView.setVisibility(View.VISIBLE);
            } else if (sPlist.getSale_status().equals("1") || sPlist.getSale_status().equals("null")) {
                // holder.addCardView.setBackgroundResource(R.color.colorPrimary);
                holder.stop_simpleDraweeView.setVisibility(View.GONE);
            }
        } else {
            holder.stop_simpleDraweeView.setVisibility(View.GONE);
        }
        if (sPlist.getImgurl() != null) {
            Glide.with(mContext).load(sPlist.getImgurl()).apply(new RequestOptions().override(180, 180).centerCrop()).into(holder.simpleDraweeView);
        }


        System.out.println("========" + sPlist.getStatus());
        System.out.println("========" + Contants.SalesStatusString[Integer.parseInt(sPlist.getStatus())]);
        holder.orderStatus.setText(Contants.SalesStatusString[Integer.parseInt(sPlist.getStatus())]);
        holder.orderStatus.setBackgroundColor(mContext.getResources().getColor(Contants.OrderStatusColor[Integer.parseInt(sPlist.getStatus())]));
        holder.startDate.setText(DateUtils.timed(sPlist.getTime1()) + " - " + DateUtils.timed(sPlist.getTime2()));
        //  holder.overDate.setText(DateUtils.getDateToString(sPlist.getTime2()+"000"));
    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;

        @BindView(R.id.orderName)
        TextView orderName;
        @BindView(R.id.orderStatus)
        TextView orderStatus;
        @BindView(R.id.orderContent)
        TextView orderContent;
        @BindView(R.id.startDate)
        TextView startDate;
        @BindView(R.id.Right_simpleDraweeView)
        SimpleDraweeView RightSimpleDraweeView;
        @BindView(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @BindView(R.id.stop_simpleDraweeView)
        SimpleDraweeView stop_simpleDraweeView;


        public HomeFragmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mListener != null) {
                mListener.onLongItemClick(getPosition());
            }
            return true;
        }
    }


}
