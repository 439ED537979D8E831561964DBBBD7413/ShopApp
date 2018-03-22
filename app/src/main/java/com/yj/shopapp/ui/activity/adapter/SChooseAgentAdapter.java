package com.yj.shopapp.ui.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ubeen.Agents;
import com.yj.shopapp.ui.activity.shopkeeper.SChooseAgentActivity;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jm on 2016/4/25.
 */
public class SChooseAgentAdapter implements IRecyclerViewIntermediary {

    private SChooseAgentActivity mContext;
    String choosetype = "0";
    private List<Agents> notes;

    private BaseRecyclerView mListener;


    public SChooseAgentAdapter(SChooseAgentActivity context, List<Agents> noteList, BaseRecyclerView myItemClickListener) {

        mContext = context;
        choosetype=mContext.choosetype;
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
        View v = View.inflate(viewGroup.getContext(), R.layout.sactivity_chooseagent_item, null);
        //make sure it fills the space
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new HomeFragmentViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        HomeFragmentViewHolder holder = (HomeFragmentViewHolder) viewHolder;
        Agents agents = notes.get(position);
        holder.agentNameTv.setText(agents.getShopname());
        holder.agentLinkmanTv.setText(agents.getContacts());
        holder.agentNoTv .setText(agents.getUsername());
        holder.delect_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.delectUser(position);
            }
        });
        if (choosetype.equals("0"))
        {
            holder.delect_txt.setVisibility(View.GONE);
        }
        else {
            holder.delect_txt.setVisibility(View.VISIBLE);
        }

    }


    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
        @BindView(R.id.agentNameTv)
        TextView agentNameTv;
        @BindView(R.id.agentStatus)
        TextView agentStatus;
        @BindView(R.id.agentNoTv)
        TextView agentNoTv;
        @BindView(R.id.agentLinkmanTv)
        TextView agentLinkmanTv;
        @BindView(R.id.delect_txt)
        TextView delect_txt;

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
