package com.yj.shopapp.ui.activity.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ui.activity.wholesale.WStopGoodsFragment;
import com.yj.shopapp.wbeen.Classify;

import java.util.List;

/**
 * Created by LK on 2018/5/24.
 *
 * @author LK
 */
public class WStopGoodsPageAdpter extends FragmentPagerAdapter {
    private List<Classify> classLists;
    private int stop_status;
    public WStopGoodsPageAdpter(FragmentManager fm, List<Classify> classifies,int s) {
        super(fm);
        this.classLists = classifies;
        this.stop_status=s;
    }

    @Override
    public Fragment getItem(int position) {
        return WStopGoodsFragment.newInstance(classLists.get(position).getCid(),stop_status);
    }

    @Override
    public int getCount() {
        return null == classLists ? 0 : classLists.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return classLists.get(position).getName();
    }
}
