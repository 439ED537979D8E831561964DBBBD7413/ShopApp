package com.yj.shopapp.ui.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ubeen.OrderDatesBean;
import com.yj.shopapp.ui.activity.shopkeeper.OrderDatesFragment;

import java.util.List;

/**
 * Created by LK on 2017/12/20.
 *
 * @author LK
 */

public class ViewPageAdpter extends FragmentPagerAdapter {
    private List<String> types;
    private List<String> names;

    public void setItemlist(List<OrderDatesBean.ItemlistBean> itemlist) {
        this.itemlist = itemlist;
        notifyDataSetChanged();
    }

    private List<OrderDatesBean.ItemlistBean> itemlist;

    public ViewPageAdpter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return OrderDatesFragment.newInstance(types.get(position), itemlist);
    }

    @Override
    public int getCount() {
        return null==names?0:names.size();
    }

    public void setNames(List<String> names, List<String> types) {
        this.names = names;
        this.types = types;
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return names.get(position);
    }
}
