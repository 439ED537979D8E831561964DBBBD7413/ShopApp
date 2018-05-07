package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;
import com.yj.shopapp.wbeen.Worder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huanghao on 2016/11/30.
 */

public class WChildOrderAdapter implements IRecyclerViewIntermediary {

    private Context mContext;

    private List<Worder.ChildorderBean> notes;

    private BaseRecyclerView mListener;

    public WChildOrderAdapter(Context context, List<Worder.ChildorderBean> noteList, BaseRecyclerView myItemClickListener) {

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
        View v = View.inflate(viewGroup.getContext(), R.layout.witem_childorder, null);
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
        Worder.ChildorderBean order = notes.get(position);
        holder.orderNo.setText(order.getOid());
        holder.odate.setText(DateUtils.getDateToString4(order.getAddtime() + "000"));
        holder.omoney.setText(order.getMoney());
        holder.type_tv.setText(order.getBigtypename()+"");


    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;

        @BindView(R.id.orderNo)
        TextView orderNo;

        @BindView(R.id.type_tv)
        TextView type_tv;
        @BindView(R.id.omoney)
        TextView omoney;
        @BindView(R.id.odate)
        TextView odate;



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
