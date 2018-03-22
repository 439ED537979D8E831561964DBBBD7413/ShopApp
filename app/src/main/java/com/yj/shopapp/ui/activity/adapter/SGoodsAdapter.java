package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yj.shopapp.R;
import com.yj.shopapp.presenter.GoodsRecyclerView;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by jm on 2016/4/25.
 */
public class SGoodsAdapter implements IRecyclerViewIntermediary {


    private Context mContext;
    private List<Goods> notes;
    private GoodsRecyclerView mListener;
    private boolean isshow = false;
    private Delte_Item delte_item;
    private boolean del_btn = false;
    private List<Integer> chooseArray;
    private int num = 0;

    public SGoodsAdapter(Context context, List<Goods> noteList, GoodsRecyclerView myItemClickListener) {

        mContext = context;

        notes = noteList;

        mListener = myItemClickListener;

    }

    public SGoodsAdapter(Context context, List<Goods> noteList, GoodsRecyclerView myItemClickListener, int num) {

        mContext = context;

        notes = noteList;

        mListener = myItemClickListener;
        this.num = num;
    }

    public SGoodsAdapter(Context context, List<Goods> noteList, GoodsRecyclerView myItemClickListener, boolean isshow) {

        mContext = context;

        notes = noteList;

        mListener = myItemClickListener;
        this.isshow = isshow;
    }

    public SGoodsAdapter(Context context, List<Goods> noteList, GoodsRecyclerView myItemClickListener
            , boolean del_btn, Delte_Item delte_item, List<Integer> chooseArray) {
        mContext = context;
        notes = noteList;
        mListener = myItemClickListener;
        this.del_btn = del_btn;
        this.delte_item = delte_item;
        this.chooseArray = chooseArray;

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
        View v = View.inflate(viewGroup.getContext(), R.layout.stab_goods_item, null);
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

        Glide.with(mContext).load(goods.getImgurl())
                .apply(new RequestOptions().placeholder(R.drawable.load).override(180,180).centerCrop()).into(holder.simpleDraweeView);
//        string is_show_price= PreferenceUtils.getPrefString(mContext, Contants.Preference.IS_SHOW_PRICE, "");
        if (!(goods.getIs_show_price() == null)) {
            if (Integer.parseInt(goods.getIs_show_price()) == 0) {
                holder.priceTv.setText("");
            } else {
                holder.priceTv.setText(goods.getPrice());
            }
        } else {
            holder.priceTv.setText(goods.getPrice());
        }

        if (num != 0) {
            holder.moneyLowgood.setText("特价:");
        }
        if (goods.getSale_status() != null) {
            if (goods.getSale_status().equals("0")) {
                holder.addcartTv.setBackgroundResource(R.drawable.goodcar_bg_2);
                // holder.addCardView.setBackgroundResource(R.color.huise1);
                holder.addcartTv.setClickable(false);
                holder.top_simpleDraweeView.setImageResource(R.drawable.pause);
            } else {
                holder.addcartTv.setBackgroundResource(R.drawable.goodcar_bg);
                if (isshow) {
                    holder.top_simpleDraweeView.setImageResource(R.drawable.img_ic_new);
                } else {
                    holder.top_simpleDraweeView.setVisibility(View.GONE);
                }

            }

        }
        if (goods.getSales_price() != null && goods.getSale_status() != null) {
            if (!"0".equals(goods.getSales_price()) && goods.getSale_status().equals("1")) {
                holder.top_simpleDraweeView.setVisibility(View.VISIBLE);
                holder.top_simpleDraweeView.setImageResource(R.drawable.img_ic_promotion);
                holder.priceTv.setText(goods.getSales_price());
                holder.salesPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.salesPrice.setText(goods.getPrice());
            }
        }

        if (del_btn) {
            holder.chooseRe.setVisibility(View.VISIBLE);
            if (chooseArray.get(position) == 0) {
                holder.choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_unhook));
            } else {
                holder.choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_hook));
            }
        }
        holder.specsTv.setText(goods.getSpecs());


    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        @BindView(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @BindView((R.id.top_simpleDraweeView))
        SimpleDraweeView top_simpleDraweeView;
        @BindView(R.id.goodsnameTv)
        TextView goodsnameTv;
        @BindView(R.id.specsTv)
        TextView specsTv;
        @BindView(R.id.priceTv)
        TextView priceTv;
        @BindView(R.id.addcartTv)
        TextView addcartTv;
        @BindView(R.id.choose)
        ImageView choose;
        @BindView(R.id.choose_re)
        RelativeLayout chooseRe;
        @BindView(R.id.money_lowgood)
        TextView moneyLowgood;
        @BindView(R.id.sales_price)
        TextView salesPrice;

        public HomeFragmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            addcartTv.setOnClickListener(this);
            choose.setOnClickListener(this);
            chooseRe.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.choose || v.getId() == R.id.choose_re) {

                if (chooseArray.get(getPosition()) == 0) {
                    choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_hook));
                    chooseArray.set(getPosition(), 1);
                    delte_item.del_item(getPosition(), 1);
                    return;
                } else {
                    choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_unhook));
                    chooseArray.set(getPosition(), 0);
                    delte_item.del_item(getPosition(), 0);
                    return;
                }

            }
            switch (v.getId()) {
                case R.id.addcartTv:
                    mListener.CardClick(getPosition());
                    break;
                default:
                    mListener.onItemClick(getPosition());
                    break;
            }

        }


    }

    public interface Delte_Item {
        void del_item(int position, int value);
    }


}
