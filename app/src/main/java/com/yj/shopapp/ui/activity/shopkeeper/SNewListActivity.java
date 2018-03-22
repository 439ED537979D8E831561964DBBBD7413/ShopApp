package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.base.BaseActivity;

import butterknife.BindView;

/**
 */
public class SNewListActivity extends BaseActivity {

    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_news;
    }

    @Override
    protected void initData() {
        title.setText("我的消息");
        TabAdapter tabAdapter = new TabAdapter(mContext, getSupportFragmentManager());

        viewpager.setAdapter(tabAdapter);
        viewpager.setOffscreenPageLimit(4);
        tabsTl.setupWithViewPager(viewpager);
        tabsTl.setTabMode(TabLayout.MODE_FIXED);
    }



    public class TabAdapter extends FragmentPagerAdapter {

        public String[] pageName = {"全部", "用户信息", "系统信息"};

        public TabAdapter(Context context, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return SNewListViewActivity.newInstance(position + "");
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
