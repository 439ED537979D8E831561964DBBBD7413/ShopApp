package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yj.shopapp.util.load.ImageLoaderInterface;

import java.util.List;

/**
 * Created by LK on 2018/1/10.
 *
 * @author LK
 */

public class WelcomePagerAdpter extends PagerAdapter {
    private Context mContext;


    private OnPagerClickListener listener;

    public void setImags(List<String> imags) {
        this.imags = imags;
        notifyDataSetChanged();
    }

    private List<String> imags;

    private ImageLoaderInterface imageLoader;

    public WelcomePagerAdpter(Context context, ImageLoaderInterface imageLoader) {
        this.mContext = context;
        this.imageLoader = imageLoader;
    }

    @Override
    public int getCount() {
        return imags == null ? 0 : imags.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        return "";
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setListener(OnPagerClickListener listener) {
        this.listener = listener;
    }

    public interface OnPagerClickListener {
        void onClickView(int position);
    }
}
