package com.yj.shopapp.util;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by LK on 2017/12/19.
 *
 * @author LK
 */

public class pagerAdpter<T> extends PagerAdapter {
    private List<Object> mData = new ArrayList();
    private Context mContext;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private OnItemClickListener listener;

    public pagerAdpter(Context context) {
        mContext = context;
    }

    public void addImgUrlList(List<T> imgUrlList) {
        this.mData.addAll(imgUrlList);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewpagerlayout, container, false);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.click(position);
            }
        });
        bind(mData.get(position), view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    private void bind(Object imgUrl, View view) {
        ImageView iv = (ImageView) view.findViewById(R.id.content_imag);
        Glide.with(this.mContext).load(imgUrl).into(iv);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public interface OnItemClickListener {
        void click(int position);
    }
}
