package com.yj.shopapp.ui.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ubeen.Address;
import com.yj.shopapp.ui.activity.shopkeeper.SAddressActivity;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by jm on 2016/5/14.
 */
public class AddressAdapter implements IRecyclerViewIntermediary {

    private SAddressActivity mContext;

    private List<Address> notes;

    private BaseRecyclerView mListener;

    private Choose mchoose;

    private boolean isEdit;
    Unbinder unbinder;

    public AddressAdapter(SAddressActivity context, List<Address> noteList, BaseRecyclerView myItemClickListener, Choose choose, boolean isEdit) {

        mContext = context;

        notes = noteList;

        mListener = myItemClickListener;

        mchoose = choose;
        this.isEdit = isEdit;
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
        View v = View.inflate(viewGroup.getContext(), R.layout.sactivity_address_item, null);
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
        Address address = notes.get(position);
        holder.managerName.setText(address.getShopname());
        holder.managerPhone.setText(address.getMobile());
        holder.managerAddress.setText(address.getAddress());
        if (StringHelper.isEmpty(address.getStatus()) || address.getStatus().equals("1")) {
            holder.managerCheckBox.setSelected(true);
            holder.managerCheckBox.setClickable(false);
        } else {
//            holder.managerCheckBox.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
            holder.moren.setVisibility(View.GONE);
        }
    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;

        @BindView(R.id.manager_name)
        TextView managerName;
        @BindView(R.id.manager_phone)
        TextView managerPhone;
        @BindView(R.id.manager_address)
        TextView managerAddress;
        @BindView(R.id.manager_line)
        View managerLine;
        @BindView(R.id.manager_checkBox)
        CheckBox managerCheckBox;
        @BindView(R.id.manager_edit)
        TextView managerEdit;
        @BindView(R.id.moren)
        TextView moren;
        @BindView(R.id.delect_lin)
        LinearLayout delect_lin;

        public HomeFragmentViewHolder(View itemView) {
            super(itemView);

            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            delect_lin.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            managerCheckBox.setOnClickListener(this);
            managerEdit.setOnClickListener(this);
            if (isEdit) {
                managerEdit.setVisibility(View.VISIBLE);
            } else {
                managerEdit.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.manager_checkBox) {
//                mchoose.choose(getPosition());
            } else if (v.getId() == R.id.manager_edit) {
                mchoose.edit(getPosition());

            } else if (v.getId() == R.id.delect_lin) {
                mContext.showDelectClint(getPosition());

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

    public interface Choose {
        void choose(int pos);

        void edit(int pos);
    }

}
