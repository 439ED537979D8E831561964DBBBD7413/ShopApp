package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ubeen.Province;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huanghao on 2016/12/9.
 */

public class AreaAdapter implements IRecyclerViewIntermediary {
    private Context mContext;
    String choosetype = "0";
    private List<Province> notes;
   public int isselect=0;
    List<Integer>integers;

    private BaseRecyclerView mListener;


    public AreaAdapter(Context context, List<Province> noteList, BaseRecyclerView myItemClickListener,List<Integer>integers) {

        mContext = context;
this.integers=integers;
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
        View v = View.inflate(viewGroup.getContext(), R.layout.my_simple_spinner_item, null);
        //make sure it fills the space
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new AreaAdapter.HomeFragmentViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        AreaAdapter.HomeFragmentViewHolder holder = (AreaAdapter.HomeFragmentViewHolder) viewHolder;
        Province province = notes.get(position);
        if (integers!=null)
        {
            if (integers.get(position)==1)
            {
                holder.name_tv.setBackgroundColor(mContext.getResources().getColor(R.color.color_8e8e8e));
            }
            else
            {
                holder.name_tv.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }

        holder.name_tv.setText(province.getArea_name());


    }


    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
        @BindView(R.id.name_tv)
        TextView name_tv;


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
