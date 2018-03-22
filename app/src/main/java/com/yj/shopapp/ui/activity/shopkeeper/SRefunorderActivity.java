package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.ui.activity.wholesale.WOrderViewActivity;

import butterknife.BindView;

public class SRefunorderActivity extends BaseActivity {


    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.activity_srefunorder)
    RelativeLayout activitySrefunorder;

    SRefundorderViewActivity sOrderViewActivity0 ;
    SRefundorderViewActivity sOrderViewActivity1 ;
    SRefundorderViewActivity sOrderViewActivity2 ;
    SRefundorderViewActivity sOrderViewActivity3 ;
    SRefundorderViewActivity sOrderViewActivity4 ;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_srefunorder;
    }

    @Override
    protected void initData() {
        title.setText("退货订单");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                SRefunorderActivity.TabAdapter tabAdapter = new SRefunorderActivity.TabAdapter(mContext, getSupportFragmentManager());

                viewpager.setAdapter(tabAdapter);
                viewpager.setOffscreenPageLimit(5);
                viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (position==0) {
                            if (sOrderViewActivity0 != null) {
                                sOrderViewActivity0.refreshRequest();
                            }
                        }
                        if (position==1) {
                            if (sOrderViewActivity1 != null) {
                                sOrderViewActivity1.refreshRequest();
                            }
                        }
                        if (position==2) {
                            if (sOrderViewActivity2 != null) {
                                sOrderViewActivity2.refreshRequest();
                            }
                        }
                        if (position==3) {
                            if (sOrderViewActivity3 != null) {
                                sOrderViewActivity3.refreshRequest();
                            }
                        }
                        if (position==4) {
                            if (sOrderViewActivity4 != null) {
                                sOrderViewActivity4.refreshRequest();
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
                sOrderViewActivity0 = SRefundorderViewActivity.newInstance(position + "");
                return sOrderViewActivity0;
            }
            if (position == 1) {
                sOrderViewActivity1 = SRefundorderViewActivity.newInstance(position + "");
                return sOrderViewActivity1;
            }

            if (position == 2) {
                sOrderViewActivity2 = SRefundorderViewActivity.newInstance(position + "");
                return sOrderViewActivity2;
            }
            if (position == 3) {
                sOrderViewActivity3 = SRefundorderViewActivity.newInstance(position + "");
                return sOrderViewActivity3;
            }
            if (position == 4) {
                sOrderViewActivity4 = SRefundorderViewActivity.newInstance(position + "");
                return sOrderViewActivity4;
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
