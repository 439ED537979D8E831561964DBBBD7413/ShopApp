package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yj.shopapp.R;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ubeen.OrderDetails;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jm on 2016/4/25.
 */
public class SOrderDetailAdapter implements IRecyclerViewIntermediary {


    private Context mContext;

    private List<OrderDetails> notes;

    private BaseRecyclerView mListener;

    private int isType ;

    public SOrderDetailAdapter(Context context, List<OrderDetails> noteList , int type , BaseRecyclerView myItemClickListener) {

        mContext = context;

        notes = noteList;

        isType = type ;

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
        View v = View.inflate(viewGroup.getContext(), R.layout.sactivity_orderdetail_item, null);
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
        OrderDetails orderDetails = notes.get(position);

        holder.goodsnameTv.setText(orderDetails.getItemname());
        holder.goodsnumTv.setText("数量: "+orderDetails.getItemcount()+"   ");
        holder.goodsmoneyTv.setText(orderDetails.getMoneysum());

        //图片的加载
        if (orderDetails.getImageUrl()==null){
            holder.img.setImageURI("");
        }else {
            Uri imgurl = Uri.parse(orderDetails.getImageUrl());
            holder.img.setImageURI(imgurl);
        }
    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
        @BindView(R.id.goodsnameTv)
        TextView goodsnameTv;
        @BindView(R.id.goodsnumTv)
        TextView goodsnumTv;
        @BindView(R.id.goodsmoneyTv)
        TextView goodsmoneyTv;
        @BindView(R.id.good_img)
        SimpleDraweeView img ;

        public HomeFragmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.onItemClick(getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {

            return true;
        }
    }
}
