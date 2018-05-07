package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/27 0027.
 */

public abstract class CommonBaseAdapter<T> extends BaseAdapter {
    protected Context context;
    protected List<T> list;
    protected LayoutInflater minflater;

    public CommonBaseAdapter(Context context) {
        this.context = context;
        minflater = LayoutInflater.from(context);
    }

    public CommonBaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addItem(T t) {
        if (null == list) {
            list = new ArrayList<T>();
        }
        list.add(t);
        notifyDataSetChanged();
    }
}
