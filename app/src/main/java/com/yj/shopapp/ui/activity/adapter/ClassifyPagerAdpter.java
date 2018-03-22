package com.yj.shopapp.ui.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yj.shopapp.ubeen.ShopDetails;
import com.yj.shopapp.ui.activity.shopkeeper.ClassifyDetaFragment;
import com.yj.shopapp.ui.activity.shopkeeper.ContactUsFragment;
import com.yj.shopapp.ui.activity.shopkeeper.GoodsDetailtwoFragment;

/**
 * Created by LK on 2018/1/23.
 *
 * @author LK
 */

public class ClassifyPagerAdpter extends FragmentPagerAdapter {
    private String[] titles;
    private String strodid;
    private int type;
    private String html;
    private ShopDetails.DataBean bean;
    private String titleName;

    public ClassifyPagerAdpter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ClassifyDetaFragment.newInstance(strodid, type,titleName);
            case 1:
                return GoodsDetailtwoFragment.newInstance(html);
            default:
                return ContactUsFragment.newInstance(bean);
        }
    }

    @Override
    public int getCount() {
        return titles == null ? 0 : titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public void setBean(ShopDetails.DataBean bean) {
        this.bean = bean;
    }

    public void setDeta(String strodid, int type, String html, String[] titles,String titleName) {
        this.strodid = strodid;
        this.type = type;
        this.html = html;
        this.titles = titles;
        this.titleName=titleName;
        notifyDataSetChanged();
    }
}
