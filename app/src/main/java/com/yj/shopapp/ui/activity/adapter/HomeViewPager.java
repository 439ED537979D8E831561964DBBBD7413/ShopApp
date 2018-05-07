package com.yj.shopapp.ui.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ui.activity.shopkeeper.BrandFragment;
import com.yj.shopapp.ui.activity.shopkeeper.SHomeActivity;
import com.yj.shopapp.ui.activity.shopkeeper.SMyInfoActivity;
import com.yj.shopapp.ui.activity.shopkeeper.SNewCartListActivity;
import com.yj.shopapp.ui.activity.shopkeeper.SOrderActivity;

/**
 * Created by LK on 2018/3/17.
 *
 * @author LK
 */

public class HomeViewPager extends FragmentPagerAdapter {
    public HomeViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SHomeActivity();
            case 1:
                return new SOrderActivity();
            case 2:
                return new BrandFragment();
            case 3:
                return new SNewCartListActivity();
            case 4:
                return new SMyInfoActivity();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

}
