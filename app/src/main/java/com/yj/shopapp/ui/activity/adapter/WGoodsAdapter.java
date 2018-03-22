package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
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

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by jm on 2016/4/25.
 */
public class WGoodsAdapter implements IRecyclerViewIntermediary {

    private Context mContext;

    private List<Goods> notes;

    private BaseRecyclerView mListener;

    public WGoodsAdapter(Context context, List<Goods> noteList, BaseRecyclerView myItemClickListener) {

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
        if (goods.getSale_status()!=null) {
            if (goods.getSale_status().equals("0")) {


                holder.top_simpleDraweeView.setVisibility(View.VISIBLE);
            } else {
                // holder.addCardView.setBackgroundResource(R.color.colorPrimary);

                holder.top_simpleDraweeView.setVisibility(View.GONE);
            }
        }
        else {
            holder.top_simpleDraweeView.setVisibility(View.GONE);
        }

        Glide.with(mContext).load(goods.getImgurl()).apply(new RequestOptions().centerCrop()).into(holder.simpleDraweeView);

    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;

//        @BindView(R.id.addCardView)
//        CardView addCardView;
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

}
