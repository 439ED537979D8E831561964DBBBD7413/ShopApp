package com.yj.shopapp.ui.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yj.shopapp.ubeen.Classify;
import com.yj.shopapp.ui.activity.shopkeeper.CarListPagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LK on 2017/12/27.
 *
 * @author LK
 */

public class CarListViewPagerAdpter extends FragmentStatePagerAdapter {
    private List<Classify> classname = new ArrayList<>();

    public CarListViewPagerAdpter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CarListPagerFragment.newInstance(Integer.parseInt(classname.get(position).getId()));
    }

    @Override
    public int getCount() {
        return classname == null ? 0 : classname.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return classname.get(position).getName() + "\n" + "￥" + classname.get(position).getMoney();
    }

    public void setClassname(List<Classify> classname) {
        this.classname = classname;
        notifyDataSetChanged();
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
