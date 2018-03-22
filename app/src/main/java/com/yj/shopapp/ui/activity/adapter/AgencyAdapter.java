package com.yj.shopapp.ui.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ui.activity.wholesale.WAgencyActivity;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;
import com.yj.shopapp.wbeen.Agency;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by huang on 2016/9/11.
 */
public class AgencyAdapter implements IRecyclerViewIntermediary {

    private WAgencyActivity mContext;
    String choosetype = "0";
    private List<Agency> notes;

    private BaseRecyclerView mListener;
    Unbinder unbinder;

    public AgencyAdapter(WAgencyActivity context, List<Agency> noteList, BaseRecyclerView myItemClickListener) {

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
        View v = View.inflate(viewGroup.getContext(), R.layout.wactivity_agency_item, null);
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
        Agency agents = notes.get(position);
        holder.agentNameTv.setText("名称："+agents.getName());
        holder.agentLinkmanTv.setText(agents.getTel());
        holder.agentNoTv .setText(agents.getMobile());
        holder.delect_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.delectUser(position);
            }
        });
        if (choosetype.equals("0"))
        {
           // 13500000000
           // 888888
            holder.delect_txt.setVisibility(View.GONE);
            if (position==0)
            {
              holder.wu.setVisibility(View.VISIBLE);
                holder. parent_lt.setVisibility(View.GONE);
            }
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
        @BindView(R.id.wu)
        TextView wu;
        @BindView(R.id.parent_lt)
        LinearLayout parent_lt;

        public HomeFragmentViewHolder(View itemView) {
            super(itemView);
            unbinder= ButterKnife.bind(this, itemView);

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
