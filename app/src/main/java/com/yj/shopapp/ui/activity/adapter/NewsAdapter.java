package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.wbeen.Msgs;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by jm on 2016/4/28.
 */
public class NewsAdapter implements IRecyclerViewIntermediary {

    private Context mContext;

    private List<Msgs> notes;

    private BaseRecyclerView mListener;

    public NewsAdapter(Context context, List<Msgs> noteList, BaseRecyclerView myItemClickListener) {

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
        View v = View.inflate(viewGroup.getContext(), R.layout.activity_newslist_item, null);
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
        Msgs msgs= notes.get(position);
        if(msgs.getStatus().equals("0")){
            holder.statusTx.setVisibility(View.GONE);
        }else{
            holder.statusTx.setVisibility(View.VISIBLE);
        }
        holder.content.setText(msgs.getMesstitle());
        holder.dateTx.setText(DateUtils.getDateToString(msgs.getAddtime()+"000"));

    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
        @BindView(R.id.date_Tx)
        TextView dateTx;
        @BindView(R.id.status_Tx)
        TextView statusTx;
        @BindView(R.id.content)
        TextView content;

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
