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
import com.yj.shopapp.presenter.GoodsRecyclerView;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;
import com.yj.shopapp.wbeen.Goods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huang on 2016/9/19.
 */
public class WStopGoodsAdapter  implements IRecyclerViewIntermediary {

    private Context mContext;

    private List<Goods> notes;

    private GoodsRecyclerView mListener;

    public WStopGoodsAdapter(Context context, List<Goods> noteList, GoodsRecyclerView myItemClickListener) {

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
        holder.addcartTv.setPadding(60,12,60,12);
        holder.addcartTv.setText("删除");
//        string is_show_price= PreferenceUtils.getPrefString(mContext, Contants.Preference.IS_SHOW_PRICE, "");
        holder.priceTv.setText(goods.getPrice());
        holder.top_simpleDraweeView.setVisibility(View.VISIBLE);
        holder.specsTv.setText(goods.getPrice());
        Glide.with(mContext).load(goods.getImgurl()).apply(new RequestOptions().centerCrop().override(180,180)).into(holder.simpleDraweeView);
    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

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

        public HomeFragmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            addcartTv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.addcartTv){
                mListener.CardClick(getPosition());
            }else{
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

