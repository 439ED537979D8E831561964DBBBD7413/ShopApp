package com.yj.shopapp.ui.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.shopkeeper.SOrderViewActivity;

/**
 * Created by LK on 2017/12/20.
 *
 * @author LK
 */

public class OrderViewPageAdpter extends FragmentPagerAdapter {
    private int[] names;

    public OrderViewPageAdpter(FragmentManager fm, int[] names) {
        super(fm);
        this.names = names;
    }

    @Override
    public Fragment getItem(int position) {
        return SOrderViewActivity.newInstance(names[position]);
    }

    @Override
    public int getCount() {
        return Contants.OrderDrawer.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Contants.OrderDrawer[position];
    }
}
