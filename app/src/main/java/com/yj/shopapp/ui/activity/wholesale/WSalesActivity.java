package com.yj.shopapp.ui.activity.wholesale;

import android.content.Context;
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
 * Created by jm on 2016/4/25.
 * 促销
 */
public class WSalesActivity extends BaseActivity {

    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_sales;
    }

    @Override
    protected void initData() {
        title.setText("我的促销");

        TabAdapter tabAdapter = new TabAdapter(mContext, getSupportFragmentManager());

        viewpager.setAdapter(tabAdapter);
        viewpager.setOffscreenPageLimit(2);
        tabsTl.setupWithViewPager(viewpager);
        tabsTl.setTabMode(TabLayout.MODE_FIXED);
    }


    public class TabAdapter extends FragmentPagerAdapter {

        public String[] pageName = Contants.SalesDrawer;

        public TabAdapter(Context context, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return WSalesViewActivity.newInstance(position + "");
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
