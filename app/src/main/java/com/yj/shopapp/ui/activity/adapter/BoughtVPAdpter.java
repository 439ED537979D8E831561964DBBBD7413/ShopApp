package com.yj.shopapp.ui.activity.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ui.activity.shopkeeper.BoughtGoodsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LK on 2018/8/17.
 *
 * @author LK
 */
public class BoughtVPAdpter extends FragmentPagerAdapter {
    private List<Industry> mdatas;

    public BoughtVPAdpter(FragmentManager fm) {
        super(fm);
        mdatas = new ArrayList<Industry>();
    }

    @Override
    public Fragment getItem(int position) {
        return BoughtGoodsFragment.newInstance(mdatas.get(position).getId());
    }

    @Override
    public int getCount() {
        return mdatas == null ? 0 : mdatas.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mdatas.get(position).getName();
    }

    public void setMdatas(List<Industry> mdatas) {
        this.mdatas = mdatas;
        notifyDataSetChanged();
    }
}
