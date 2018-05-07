package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yj.shopapp.R;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;
import com.yj.shopapp.wbeen.Imglist;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jm on 2016/4/27.
 */
public class ChooseAdapter implements IRecyclerViewIntermediary {

    private Context mContext;

    private List<Imglist> notes;

    private BaseRecyclerView mListener;

    public ChooseAdapter(Context context, List<Imglist> noteList, BaseRecyclerView myItemClickListener) {

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
        View v = View.inflate(viewGroup.getContext(), R.layout.selectimage_gridview_item, null);
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
        Uri imageUri = Uri.parse(notes.get(position).getImgurl());
        //开始下载
        holder.simpleDraweeView.setImageURI(imageUri);


    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
        @BindView(R.id.camera_iv)
        SimpleDraweeView simpleDraweeView;
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