package com.yj.shopapp.ui.activity.shopkeeper;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.OrderDatesBean;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.adapter.OrderFragmentAdapte;
import com.yj.shopapp.util.DDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDatesFragment extends NewBaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private OrderFragmentAdapte adapte;
    private List<OrderDatesBean.ItemlistBean> mDate = new ArrayList<>();

    public static OrderDatesFragment newInstance(String type) {
        Bundle args = new Bundle();
        OrderDatesFragment fragment = new OrderDatesFragment();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public static OrderDatesFragment newInstance(String type, List<OrderDatesBean.ItemlistBean> itemlist) {
        Bundle args = new Bundle();
        OrderDatesFragment fragment = new OrderDatesFragment();
        args.putString("type", type);
        args.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) itemlist);
        fragment.setArguments(args);
        return fragment;
    }

    private List<OrderDatesBean.ItemlistBean> getdata() {
        return getArguments().getParcelableArrayList("data");
    }

    private String getType() {
        return getArguments().getString("type");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_dates;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        adapte = new OrderFragmentAdapte(mActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        recyclerView.setAdapter(adapte);
    }

    @Override
    protected void initData() {
        mDate.clear();
        for (int i = 0; i < getdata().size(); i++) {
            if (getdata().get(i).getCid().equals(getType())) {
                mDate.add(getdata().get(i));
            }
        }
        adapte.setList(mDate);
    }


}
