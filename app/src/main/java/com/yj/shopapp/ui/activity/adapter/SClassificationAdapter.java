package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yj.shopapp.R;
import com.yj.shopapp.presenter.GoodsRecyclerView;
import com.yj.shopapp.ubeen.Classise;
import com.yj.shopapp.view.CircleImageView;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huanghao on 2016/10/20.
 */

public class SClassificationAdapter implements IRecyclerViewIntermediary {


    private Context mContext;

    private List<Classise> notes;

    private GoodsRecyclerView mListener;

    public SClassificationAdapter(Context context, List<Classise> noteList, GoodsRecyclerView myItemClickListener) {

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
        View v = View.inflate(viewGroup.getContext(), R.layout.category_item, null);
        //make sure it fills the space
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new SClassificationAdapter.HomeFragmentViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        SClassificationAdapter.HomeFragmentViewHolder holder = (SClassificationAdapter.HomeFragmentViewHolder) viewHolder;
        Classise mClassise = notes.get(position);
        holder.edit_img.setVisibility(View.GONE);
        holder.name_tv.setText(mClassise.getName());
        Glide.with(mContext).load(mClassise.getImgurl()).apply(new RequestOptions().centerCrop()).into(holder.simpleDraweeView);
    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.simpleDraweeView)
        CircleImageView simpleDraweeView;
        @BindView(R.id.name_tv)
        TextView name_tv;
        @BindView(R.id.edit_img)
        ImageView edit_img;


        public HomeFragmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


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
