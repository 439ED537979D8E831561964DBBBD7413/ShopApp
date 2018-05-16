package com.yj.shopapp.ui.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ui.activity.wholesale.WHomeActivity;
import com.yj.shopapp.ui.activity.wholesale.WMyInfoActivity;
import com.yj.shopapp.ui.activity.wholesale.WOrderActivity;

/**
 * Created by LK on 2018/5/14.
 *
 * @author LK
 */
public class WhomePagerAdpter extends FragmentPagerAdapter {
    public WhomePagerAdpter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return WHomeActivity.newInstance();
            case 1:
                return new WOrderActivity();
            case 2:
                return new WMyInfoActivity();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
