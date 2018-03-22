package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yj.shopapp.R;
import com.yj.shopapp.presenter.GoodsRecyclerView;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by huanghao on 2016/11/21.
 */

public class WSetupPathAdapter implements IRecyclerViewIntermediary {

    List<Integer> isedit;
    private Context mContext;


    private List<String> notes;

    private GoodsRecyclerView mListener;

    public WSetupPathAdapter(Context context, List<String> noteList, GoodsRecyclerView myItemClickListener) {



       this.mContext=context;
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
        return new WSetupPathAdapter.HomeFragmentViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        WSetupPathAdapter.HomeFragmentViewHolder holder = (WSetupPathAdapter.HomeFragmentViewHolder) viewHolder;
        final String path = notes.get(position);


//        string is_show_price= PreferenceUtils.getPrefString(mContext, Contants.Preference.IS_SHOW_PRICE, "");
        holder.name_tv.setVisibility(View.GONE);
            holder.edit_img.setVisibility(View.VISIBLE);
            holder.edit_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.CardClick(position);
                }
            });




        Uri imageUri = Uri.parse(path);
        //开始下载
        holder.simpleDraweeView.setImageURI(imageUri);
    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
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


