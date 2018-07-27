package com.yj.shopapp.ui.activity.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ui.activity.wholesale.WNewGoodsFragment;
import com.yj.shopapp.wbeen.Classify;

import java.util.List;

/**
 * Created by LK on 2018/5/24.
 *
 * @author LK
 */
public class WNewGoodsPageAdpter extends FragmentPagerAdapter {
    private List<Classify> classLists;

    public WNewGoodsPageAdpter(FragmentManager fm, List<Classify> classifies) {
        super(fm);
        this.classLists = classifies;
    }

    @Override
    public Fragment getItem(int position) {
        return WNewGoodsFragment.newInstance(classLists.get(position).getCid());
    }

    @Override
    public int getCount() {
        return null != classLists ? classLists.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return classLists.get(position).getName();
    }
}
