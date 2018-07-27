package com.yj.shopapp.ui.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.wholesale.WOrderViewActivity;

/**
 * Created by LK on 2017/12/29.
 *
 * @author LK
 */

public class WorderPagerAdpter extends FragmentPagerAdapter {
    private int[] names;

    public WorderPagerAdpter(FragmentManager fm, int[] names) {
        super(fm);
        this.names = names;
    }

    @Override
    public Fragment getItem(int position) {
        return WOrderViewActivity.newInstance(names[position] + "");
    }

    @Override
    public int getCount() {
        return Contants.OrderNewDrawerw.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Contants.OrderNewDrawerw[position];
    }
}
