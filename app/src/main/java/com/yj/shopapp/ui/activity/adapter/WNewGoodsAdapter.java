package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yj.shopapp.R;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;
import com.yj.shopapp.wbeen.Goods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/30.
 */
public class WNewGoodsAdapter implements IRecyclerViewIntermediary {

    private Context mContext;

    private List<Goods> notes;

    private BaseRecyclerView mListener;

    public WNewGoodsAdapter(Context context, List<Goods> noteList, BaseRecyclerView myItemClickListener) {

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
        View v = View.inflate(viewGroup.getContext(), R.layout.wtab_goods_item, null);
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
        Goods goods = notes.get(position);
        holder.goodsnameTv.setText(goods.getName());
        holder.priceTv.setText(goods.getPrice());
        holder.specsTv.setText(goods.getItemsum());
        Uri imageUri = Uri.parse(goods.getImgurl());
        if (goods.getSale_status() != null) {
            if (goods.getSale_status().equals("0")) {


                holder.top_simpleDraweeView.setVisibility(View.VISIBLE);
            } else if (goods.getSale_status().equals("1")) {
                // holder.addCardView.setBackgroundResource(R.color.colorPrimary);

                holder.top_simpleDraweeView.setVisibility(View.GONE);
            }
        }
        //开始下载
        Glide.with(mContext).load(imageUri).apply(new RequestOptions().placeholder(R.drawable.load).override(180,180).centerCrop())
                .into(holder.simpleDraweeView);
        //holder.simpleDraweeView.setImageURI(imageUri);

    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;

        @BindView(R.id.addCardView)
        CardView addCardView;
        @BindView(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @BindView(R.id.top_simpleDraweeView)
        SimpleDraweeView top_simpleDraweeView;
        @BindView(R.id.goodsnameTv)
        TextView goodsnameTv;
        @BindView(R.id.specsTv)
        TextView specsTv;
        @BindView(R.id.priceTv)
        TextView priceTv;

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

//    @Override
//    public int getItemCount() {
//        return notes.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return notes.get(position);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, int type) {
//        View v = View.inflate(viewGroup.getContext(), R.layout.wtab_goods_item, null);
//        //make sure it fills the space
//        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        return new HomeFragmentViewHolder(v);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;  //any logic can go here
//    }
//
//    @Override
//    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//
//        HomeFragmentViewHolder holder = (HomeFragmentViewHolder) viewHolder;
//        Goods goods = notes.get(position);
//        holder.goodsNmaeTx.setText(goods.getName());
//        holder.goodsMoneyTx.setText(goods.getPrice());
//        holder.goodsInventoryTx.setText(mContext.getString(R.string.inventory)+goods.getItemsum());
//        Uri imageUri = Uri.parse(goods.getImgurl());
//        //开始下载
//        holder.simpleDraweeView.setImageURI(imageUri);
//
//    }
//
//    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
//
//        View view;
//        @BindView(R.id.card_view_layout)
//        CardView cardviewlayout;
//        @BindView(R.id.simpleDraweeView)
//        SimpleDraweeView simpleDraweeView;
//        @BindView(R.id.goodsNmaeTx)
//        TextView goodsNmaeTx;
//        @BindView(R.id.goodsDiscountTx)
//        TextView goodsDiscountTx;
//        @BindView(R.id.goodsInventoryTx)
//        TextView goodsInventoryTx;
//        @BindView(R.id.goodsMoneyTx)
//        TextView goodsMoneyTx;
//
//        public HomeFragmentViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.inject(this, itemView);
//            cardviewlayout.setOnClickListener(this);
//            cardviewlayout.setOnLongClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (mListener != null) {
//                mListener.onItemClick(getPosition());
//            }
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            if (mListener != null) {
//                mListener.onLongItemClick(getPosition());
//            }
//            return true;
//        }
//    }
}
