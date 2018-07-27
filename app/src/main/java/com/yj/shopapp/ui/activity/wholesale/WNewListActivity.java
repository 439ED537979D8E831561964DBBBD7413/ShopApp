package com.yj.shopapp.ui.activity.wholesale;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.StatusBarUtils;

import butterknife.BindView;

/**
 */
public class WNewListActivity extends BaseActivity {

    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.title_view)
    RelativeLayout titleView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
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

            return WNewListViewActivity.newInstance(position + "");
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
