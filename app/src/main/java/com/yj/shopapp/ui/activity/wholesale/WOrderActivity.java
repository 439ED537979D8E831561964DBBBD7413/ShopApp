package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.yj.shopapp.R;
import com.yj.shopapp.dialog.WOrderSearchDialogFragment;
import com.yj.shopapp.ui.activity.adapter.WorderPagerAdpter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.util.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/25.
 */
public class WOrderActivity extends BaseFragment {
    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    WorderPagerAdpter pagerAdpter;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    private int[] status = {0, 1, 4, 3};

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtils.from(getActivity())
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
        pagerAdpter = new WorderPagerAdpter(getChildFragmentManager(), status);
        viewpager.setAdapter(pagerAdpter);
        tabsTl.setupWithViewPager(viewpager);
    }

    @Override
    public int getLayoutID() {
        return R.layout.wtab_order;
    }

    @OnClick(R.id.id_right_btu)
    public void onViewClicked() {
        //搜索
        new WOrderSearchDialogFragment().show(getFragmentManager(), "WOrderSearchDialogFragment");
    }
}
