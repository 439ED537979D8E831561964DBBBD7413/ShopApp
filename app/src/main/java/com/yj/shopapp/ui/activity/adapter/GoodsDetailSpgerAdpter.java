package com.yj.shopapp.ui.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ubeen.GoodData;
import com.yj.shopapp.ui.activity.shopkeeper.GoodsDetailFragment;
import com.yj.shopapp.ui.activity.shopkeeper.GoodsDetailtwoFragment;

/**
 * Created by LK on 2018/1/9.
 *
 * @author LK
 */

public class GoodsDetailSpgerAdpter extends FragmentPagerAdapter {
    private String[] titlename;
    private GoodData.DataBean bean;

    public GoodsDetailSpgerAdpter(FragmentManager fm, String[] titlename) {
        super(fm);
        this.titlename = titlename;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return GoodsDetailFragment.newInstance(bean);
            case 1:
                return GoodsDetailtwoFragment.newInstance(bean.getDetails());
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titlename.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlename[position];
    }

    public void setBean(GoodData.DataBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }
}
