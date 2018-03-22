package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;
import com.yj.shopapp.wbeen.Customer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huanghao on 2016/11/26.
 */

public class CustomerAdapter implements IRecyclerViewIntermediary {
    private Context mContext;

    private List<Customer> notes;

    private BaseRecyclerView mListener;


    public CustomerAdapter(Context context, List<Customer> noteList, BaseRecyclerView myItemClickListener) {

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
        View v = View.inflate(viewGroup.getContext(), R.layout.witem_customer, null);
        //make sure it fills the space
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new CustomerAdapter.HomeFragmentViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        CustomerAdapter.HomeFragmentViewHolder holder = (CustomerAdapter.HomeFragmentViewHolder) viewHolder;
        Customer customer = notes.get(position);
        if (customer.getShopname()==null||customer.getShopname().equals("")){
            holder.shopnmae_tv.setText("店名没有设置");
        }else {
            holder.shopnmae_tv.setText(customer.getShopname());
        }
        if (customer.getUsername()==null||customer.getUsername().equals("")){
            holder.user_name_re.setVisibility(View.GONE);
        }else {
            holder.user_name_re.setVisibility(View.VISIBLE);
            holder.user_name_tv.setText(customer.getUsername());
        }
    }


    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
      @BindView(R.id.shopnmae_tv)
      TextView shopnmae_tv;
        @BindView(R.id.user_name_tv)
        TextView user_name_tv;
        @BindView(R.id.user_name_re)
        RelativeLayout user_name_re;

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
