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
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;
import com.yj.shopapp.wbeen.Worder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huanghao on 2016/12/15.
 */

public class WRefundOrderAdapter implements IRecyclerViewIntermediary {

    private Context mContext;

    private List<Worder> notes;

    private BaseRecyclerView mListener;

    public WRefundOrderAdapter(Context context, List<Worder> noteList, BaseRecyclerView myItemClickListener) {

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
        View v = View.inflate(viewGroup.getContext(), R.layout.wtab_refund_recycleritem, null);
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
        holder.odate.setText(DateUtils.getDateToString4(order.getAddtime() + "000"));
        holder.omoney.setText(order.getMoney());
//        holder.oname.setText(order.getUsername());
        if (!StringHelper.isEmpty(order.getSumnum())){
            holder.goodCount.setText(order.getSumnum());
        }
        if (!StringHelper.isEmpty(order.getSumtype())){
            holder.goodType.setText(order.getSumtype());
        }
        holder.orderStatus.setText(Contants.OrderStatusString[Integer.parseInt(order.getStatus())]);
    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;

        @BindView(R.id.orderNo)
        TextView orderNo;
        @BindView(R.id.orderStatus)
        TextView orderStatus;
//        @BindView(R.id.oname)
//        TextView oname;
        @BindView(R.id.omoney)
        TextView omoney;
        @BindView(R.id.odate)
        TextView odate;
        @BindView(R.id.good_type)
        TextView goodType;
        @BindView(R.id.good_count)
        TextView goodCount;

//        @BindView(R.id.simpleDraweeView)
//        SimpleDraweeView img ;


        public HomeFragmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

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

