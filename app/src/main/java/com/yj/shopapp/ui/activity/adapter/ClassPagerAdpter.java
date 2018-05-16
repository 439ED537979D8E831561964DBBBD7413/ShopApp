package com.yj.shopapp.ui.activity.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ui.activity.wholesale.ClassPagerFragment;
import com.yj.shopapp.wbeen.ClassList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LK on 2018/5/14.
 *
 * @author LK
 */
public class ClassPagerAdpter extends FragmentPagerAdapter {
    private List<ClassList> classLists = new ArrayList<>();

    public ClassPagerAdpter(FragmentManager fm) {
        super(fm);
    }

    public void setClassLists(List<ClassList> classLists) {
        this.classLists = classLists;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return ClassPagerFragment.newInstance(classLists.get(position).getList());
    }

    @Override
    public int getCount() {
        return classLists.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return classLists.get(position).getName();
    }
}
