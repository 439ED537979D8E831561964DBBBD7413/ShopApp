package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.shopkeeper.BuGoodDetailsFragment;
import com.yj.shopapp.ui.activity.shopkeeper.CancelByGoodsListFragment;
import com.yj.shopapp.ui.activity.shopkeeper.MyBuGoodFragment;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by LK on 2018/4/3.
 *
 * @author LK
 */

public class BuGoodDetailsViewPager extends FragmentPagerAdapter {
    private String[] titles;
    private Context mContext;

    public BuGoodDetailsViewPager(Context context, FragmentManager fm, String[] titles) {
        super(fm);
        this.mContext = context;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BuGoodDetailsFragment.newInstance(1);
            case 1:
                return BuGoodDetailsFragment.newInstance(3);
            case 2:
                return new MyBuGoodFragment();
            case 3:
                return new CancelByGoodsListFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public View getTabItemView(int position, int size) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_layout_item, null);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(titles[position]);
       // ((TextView) view.findViewById(R.id.brand_view)).setText(String.valueOf(size));
        new QBadgeView(mContext).bindTarget(textView).setBadgeGravity(Gravity.CENTER | Gravity.END).setBadgeTextSize(8, true).setBadgeNumber(size);
        return view;
    }

}
