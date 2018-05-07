package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.adapter.OrderViewPageAdpter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.view.CustomViewPager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/25.
 */
public class SOrderActivity extends NewBaseFragment {
    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    @BindView(R.id.viewpager)
    CustomViewPager viewpager;
    @BindView(R.id.title_view)
    LinearLayout titleView;
    @BindView(R.id.search_tv)
    TextView searchTv;
    private OrderViewPageAdpter adpter;
    private int[] status = {0, 1, 4, 3};

    @Override
    protected int getLayoutId() {
        return R.layout.stab_order;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.from(getActivity())
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
        adpter = new OrderViewPageAdpter(getFragmentManager(), status);
        viewpager.setOpenAnimation(false);
        viewpager.setScanScroll(false);
    }

    @Override
    protected void initData() {
        viewpager.setAdapter(adpter);
        tabsTl.setupWithViewPager(viewpager);
    }

    /**
     * 1.订单统计
     * 2.搜索
     * 3.订单记录
     *
     * @param view
     */
    @OnClick({R.id.order_count, R.id.search_rl, R.id.orderRecord})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_count:
                CommonUtils.goActivity(mActivity, PieChartActivity.class, null);
                break;
            case R.id.search_rl:
                FragmentSearchBoxSelect.newInstance(1).show(mActivity.getFragmentManager(), "searchBox");
                break;
            case R.id.orderRecord:
                CommonUtils.goActivity(mActivity, GoodsRecord.class, null);
                break;
        }
    }

}