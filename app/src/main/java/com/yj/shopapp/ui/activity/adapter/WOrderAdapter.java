package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ui.activity.wholesale.WChilOrderActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;
import com.yj.shopapp.wbeen.Worder;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by jm on 2016/4/25.
 */
public class WOrderAdapter implements IRecyclerViewIntermediary {


    private Context mContext;

    private List<Worder> notes;

    private BaseRecyclerView mListener;

    public WOrderAdapter(Context context, List<Worder> noteList, BaseRecyclerView myItemClickListener) {

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
        View v = View.inflate(viewGroup.getContext(), R.layout.wtab_oeder_recycleritem, null);
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
        Worder order = notes.get(position);
        holder.orderNo.setText(order.getOid());
        holder.odate.setText(DateUtils.timet(order.getAddtime()));
        holder.omoney.setText(order.getMoney());
        holder.oname.setText(order.getUsername());
        holder.total.setText("总件数：" + order.getSumnum() + "件");
        holder.orderStatus.setText(Contants.OrderStatusString[Integer.parseInt(order.getStatus())]);
        holder.orderStatus.setBackgroundColor(mContext.getResources().getColor(Contants.OrderStatusColor[Integer.parseInt(order.getStatus())]));
    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;

        @BindView(R.id.orderNo)
        TextView orderNo;
        @BindView(R.id.orderStatus)
        TextView orderStatus;
        @BindView(R.id.oname)
        TextView oname;
        @BindView(R.id.omoney)
        TextView omoney;
        @BindView(R.id.odate)
        TextView odate;
        @BindView(R.id.lookchillorder_tv)
        TextView lookchillorder_tv;
        @BindView(R.id.total)
        TextView total;

        public HomeFragmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            lookchillorder_tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.lookchillorder_tv) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("childOrder", notes.get(getPosition()));
                CommonUtils.goActivity(mContext, WChilOrderActivity.class, bundle);
                return;

            }
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
