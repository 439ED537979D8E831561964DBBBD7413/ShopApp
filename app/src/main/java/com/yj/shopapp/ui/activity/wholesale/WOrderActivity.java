package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.adapter.WorderPagerAdpter;
import com.yj.shopapp.ui.activity.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by jm on 2016/4/25.
 */
public class WOrderActivity extends BaseFragment {
    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    WorderPagerAdpter pagerAdpter;
    private int [] status={0,1,4,3};
    @Override
    public void init(Bundle savedInstanceState) {
        pagerAdpter = new WorderPagerAdpter(getChildFragmentManager(),status);
        viewpager.setAdapter(pagerAdpter);
        tabsTl.setupWithViewPager(viewpager);
    }

    @Override
    public int getLayoutID() {
        return R.layout.wtab_order;
    }

}
