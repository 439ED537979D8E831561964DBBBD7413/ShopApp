package com.yj.shopapp.ui.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ui.activity.shopkeeper.IntegralBuGoodsFragment;
import com.yj.shopapp.ui.activity.shopkeeper.SExchangeActivity;
import com.yj.shopapp.ui.activity.shopkeeper.SIntegralRecordF;

/**
 * Created by LK on 2018/1/29.
 *
 * @author LK
 */

public class IntegralViewPagerAdpter extends FragmentPagerAdapter {
    private int size = 0;

    public IntegralViewPagerAdpter(FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IntegralBuGoodsFragment();
            case 1:
                return new SExchangeActivity();
            case 2:
                return new SIntegralRecordF();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return size;
    }
}
