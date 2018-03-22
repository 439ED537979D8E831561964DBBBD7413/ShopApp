package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.presenter.CardListRecyclerView;
import com.yj.shopapp.ubeen.ScashCoupon;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huanghao on 2016/12/5.
 */

public class ScashcouponUnAdapter implements IRecyclerViewIntermediary {
    private Context mContext;

    private List<ScashCoupon.CanuseBean> notes;

    private CardListRecyclerView mListener;

    private List<Integer> chooseArray;
    public int ostatus = 3;
    public int isstop=0;


    public ScashcouponUnAdapter(Context context, List<ScashCoupon.CanuseBean> noteList, CardListRecyclerView myItemClickListener, List<Integer> chooseArray) {

        mContext = context;

        notes = noteList;

        mListener = myItemClickListener;

        this.chooseArray = chooseArray;
    }

    /**
     * 当前点击操作的加减数量数否已经选中
     *
     * @return
     */
    private boolean isselect(int position) {


        return chooseArray.get(position) == 1 ? true : false;

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
        View v = View.inflate(viewGroup.getContext(), R.layout.witem_cashcoupon, null);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ScashcouponUnAdapter.HomeFragmentViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final ScashcouponUnAdapter.HomeFragmentViewHolder holder = (ScashcouponUnAdapter.HomeFragmentViewHolder) viewHolder;
        final ScashCoupon.CanuseBean cashCoupon = notes.get(position);
        // final double prise=Double.parseDouble(cartList.getPrice());
        holder.clent_tv.setText(cashCoupon.getShopname());
        holder.classi_tv.setText(cashCoupon.getBigtypename());
        holder.moneyTv.setText("￥" + cashCoupon.getMoney());
        holder.conditionTv.setText("满" + cashCoupon.getAvailable_money() + "使用");
        holder.termTv.setText(cashCoupon.getStarttime() + "至" + cashCoupon.getEndtime());
        if (ostatus == 0) {
            holder.choose_re.setVisibility(View.VISIBLE);
        }
        if (ostatus == 1) {
            holder.choose_re.setVisibility(View.GONE);
        }


        if (chooseArray.get(position) == 0) {
            holder.choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
        } else {
            holder.choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
        }

    }


    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
        @BindView(R.id.choose_re)
        LinearLayout choose_re;
        @BindView(R.id.choose)
        ImageView choose;
        @BindView(R.id.money_tv)
        TextView moneyTv;
        @BindView(R.id.condition_tv)
        TextView conditionTv;
        @BindView(R.id.term_tv)
        TextView termTv;
        @BindView(R.id.clent_tv)
        TextView clent_tv;
        @BindView(R.id.classi_tv)
        TextView classi_tv;


        public HomeFragmentViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
            //choose.setOnClickListener(this);
            choose_re.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.choose_re) {

                if (chooseArray.get(getPosition()) == 0) {

                    //chooseArray.set(getPosition(), 1);
                    mListener.chooseItem(getPosition(), 1);
                    if (isstop==0) {
                        choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
                    }
                    isstop=0;
                } else {
                    choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
                    //chooseArray.set(getPosition(), 0);
                    mListener.chooseItem(getPosition(), 0);

                    choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
                }


//            }else{
//                if (mListener != null) {
////                    mListener.onItemClick(getPosition());
//                    if(chooseArray.get(getPosition())==0){
//                        choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
//                        chooseArray.set(getPosition(),1);
//                        mListener.chooseItem(getPosition(),1);
//                    }else{
//                        choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
//                        chooseArray.set(getPosition(),0);
//                        mListener.chooseItem(getPosition(),0);
//                    }
//
//                }
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

