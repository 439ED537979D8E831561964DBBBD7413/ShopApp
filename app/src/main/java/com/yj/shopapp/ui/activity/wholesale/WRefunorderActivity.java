package com.yj.shopapp.ui.activity.wholesale;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by huanghao on 2016/12/15.
 */

public class WRefunorderActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    WRefundorderViewActivity wOrderViewActivity0;
    WRefundorderViewActivity wOrderViewActivity1;
    WRefundorderViewActivity wOrderViewActivity2;
    WRefundorderViewActivity wOrderViewActivity3;
    WRefundorderViewActivity wOrderViewActivity4;
    @Override
    protected int getLayoutId() {
        return R.layout.wtab_refundorder;
    }

    @Override
    protected void initData() {
        title.setText("退货订单");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                WRefunorderActivity.TabAdapter tabAdapter = new WRefunorderActivity.TabAdapter(mContext, getSupportFragmentManager());

                viewpager.setAdapter(tabAdapter);
                viewpager.setOffscreenPageLimit(5);
                viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (position==0) {
                            if (wOrderViewActivity0 != null) {
                                wOrderViewActivity0.refreshRequest();
                            }
                        }
                        if (position==1) {
                            if (wOrderViewActivity1 != null) {
                                wOrderViewActivity1.refreshRequest();
                            }
                        }
                        if (position==2) {
                            if (wOrderViewActivity2 != null) {
                                wOrderViewActivity2.refreshRequest();
                            }
                        }
                        if (position==3) {
                            if (wOrderViewActivity3 != null) {
                                wOrderViewActivity3.refreshRequest();
                            }
                        }
                        if (position==4) {
                            if (wOrderViewActivity4 != null) {
                                wOrderViewActivity4.refreshRequest();
                            }
                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                tabsTl.setupWithViewPager(viewpager);
                tabsTl.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
        }, 100);
    }


    public class TabAdapter extends FragmentPagerAdapter {

        public String[] pageName = Contants.OrderDrawer;

        public TabAdapter(Context context, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                wOrderViewActivity0 = WRefundorderViewActivity.newInstance(position + "");
                return wOrderViewActivity0;
            }
            if (position == 1) {
                wOrderViewActivity1 = WRefundorderViewActivity.newInstance(position + "");
                return wOrderViewActivity1;
            }

            if (position == 2) {
                wOrderViewActivity2 = WRefundorderViewActivity.newInstance(position + "");
                return wOrderViewActivity2;
            }
            if (position == 3) {
                wOrderViewActivity3 = WRefundorderViewActivity.newInstance(position + "");
                return wOrderViewActivity3;
            }
            if (position == 4) {
                wOrderViewActivity4 = WRefundorderViewActivity.newInstance(position + "");
                return wOrderViewActivity4;
            }

            return WOrderViewActivity.newInstance(position + "");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageName[position];
        }

        @Override
        public int getCount() {
            return pageName.length;
        }
    }



}
