package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoaderInterface;

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
        ImageView imageView = (ImageView) imageLoader.createImageView(mContext);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickView(position);
            }
        });
        imageLoader.displayImage(mContext, imags.get(position), imageView);
        container.addView(imageView);
        return imageView;
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
