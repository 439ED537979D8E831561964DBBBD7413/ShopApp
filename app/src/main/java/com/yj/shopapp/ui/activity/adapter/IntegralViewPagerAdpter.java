package com.yj.shopapp.ui.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ui.activity.shopkeeper.SExchangeActivity;

/**
 * Created by LK on 2018/1/29.
 *
 * @author LK
 */

public class IntegralViewPagerAdpter extends FragmentPagerAdapter {

    public IntegralViewPagerAdpter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

            case 1:
                return new SExchangeActivity();
            case 2:
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
