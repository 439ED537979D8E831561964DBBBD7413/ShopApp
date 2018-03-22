package com.yj.shopapp.ui.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ui.activity.shopkeeper.AccountBookFragment;
import com.yj.shopapp.ui.activity.shopkeeper.SIntegralChangeActivity;
import com.yj.shopapp.ui.activity.shopkeeper.SIntegralDetailActivity;

/**
 * Created by LK on 2018/3/10.
 *
 * @author LK
 */

public class SIntegralRecordAdpter extends FragmentPagerAdapter {
    private String[] titleName;

    public SIntegralRecordAdpter(FragmentManager fm, String[] titleName) {
        super(fm);
        this.titleName = titleName;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SIntegralDetailActivity();
            case 1:
                return new SIntegralChangeActivity();
            case 2:
                return new AccountBookFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return titleName.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleName[position];
    }
}
