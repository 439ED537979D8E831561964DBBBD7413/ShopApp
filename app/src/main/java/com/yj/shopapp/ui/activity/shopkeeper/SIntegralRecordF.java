package com.yj.shopapp.ui.activity.shopkeeper;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.adapter.SIntegralRecordAdpter;
import com.yj.shopapp.view.ParentViewPager;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SIntegralRecordF extends NewBaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.my_viewpager)
    ParentViewPager myViewpager;
    private SIntegralRecordAdpter sIntegralRecordAdpter;
    private String[] titleName = {"换购记录", "兑现记录", "积分明细"};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sintegral_record;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        sIntegralRecordAdpter = new SIntegralRecordAdpter(getChildFragmentManager(), titleName);
        myViewpager.setAdapter(sIntegralRecordAdpter);
        tabLayout.setupWithViewPager(myViewpager);
    }

    @Override
    protected void initData() {

    }

}
