package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.presenter.GoodsRecyclerView;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huanghao on 2016/11/11.
 */

public class WLikeCheckAdapter implements IRecyclerViewIntermediary {

    private Context mContext;

    private List<Goods> notes;

    private GoodsRecyclerView mListener;

    public WLikeCheckAdapter(Context context, List<Goods> noteList, GoodsRecyclerView myItemClickListener) {

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
        View v = View.inflate(viewGroup.getContext(), R.layout.sitem_likecheck, null);
        //make sure it fills the space
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new WLikeCheckAdapter.HomeFragmentViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        WLikeCheckAdapter.HomeFragmentViewHolder holder = (WLikeCheckAdapter.HomeFragmentViewHolder) viewHolder;
        Goods goods = notes.get(position);
        holder.goodsnameTv.setText(goods.getName());

//        string is_show_price= PreferenceUtils.getPrefString(mContext, Contants.Preference.IS_SHOW_PRICE, "");
//        if (!(goods.getIs_show_price()==null)) {
//            if (Integer.parseInt(goods.getIs_show_price()) == 0) {
//                holder.priceTv.setText("");
//            } else {
//                holder.priceTv.setText(goods.getPrice());
//            }
//        }
//        else {
//            holder.priceTv.setText(goods.getPrice());
//        }
        //holder.priceTv.setText(goods.getCostprice());
        holder.codeTv.setText(goods.getItemnumber());
//        if (goods.getSale_status()!=null) {
//            if (goods.getSale_status().equals("0")) {
//
//                holder.addcartTv.setBackgroundResource(R.color.qianhui);
//                // holder.addCardView.setBackgroundResource(R.color.huise1);
//                holder.addcartTv.setClickable(false);
//                holder.top_simpleDraweeView.setVisibility(View.VISIBLE);
//            } else {
//                // holder.addCardView.setBackgroundResource(R.color.colorPrimary);
//                holder.addcartTv.setBackgroundResource(R.color.color_ff8000);
//                // holder.addCardView.setCardBackgroundColor(R.color.color_FFA500);
//
//                holder.addcartTv.setClickable(true);
//                holder.top_simpleDraweeView.setVisibility(View.GONE);
//            }
//        }
//        holder.specsTv.setText(goods.getCostprice());
//        Uri imageUri = Uri.parse(goods.getImgurl());
        //开始下载
//        holder.goodImg.setImageURI(imageUri);
        holder.goodImg.setVisibility(View.GONE);
    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;


//        @BindView(R.id.simpleDraweeView)
//        SimpleDraweeView simpleDraweeView;
//
//        @BindView((R.id.top_simpleDraweeView))
//        SimpleDraweeView top_simpleDraweeView;

        @BindView(R.id.goodsnameTv)
        TextView goodsnameTv;
        @BindView(R.id.codeTv)
        TextView codeTv;
        @BindView(R.id.top_simpleDraweeView)
        ImageView goodImg;
        // @BindView(R.id.priceTv)
//        TextView priceTv;
//        @BindView(R.id.addcartTv)
//        TextView addcartTv;


        public HomeFragmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            //  addcartTv.setVisibility(View.GONE);
            // addcartTv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.addcartTv) {
                mListener.CardClick(getPosition());
            } else {
                if (mListener != null) {
                    mListener.onItemClick(getPosition());
                }
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

