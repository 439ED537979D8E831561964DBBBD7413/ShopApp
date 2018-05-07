package com.yj.shopapp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yj.shopapp.R;
import com.yj.shopapp.loading.ILoadView2;

/**
 * Created by LK on 2018/4/1.
 *
 * @author LK
 */

public class ReturnView implements ILoadView2 {
    private View rootView;
    private LinearLayout loadView, emptyView;
    private RelativeLayout doneView;
    private Context mContext;

    public ReturnView(Context mContext) {
        this.mContext = mContext;
        rootView = inflate();
    }

    public View init() {
        loadView = rootView.findViewById(R.id.load);
        emptyView = rootView.findViewById(R.id.empty);
        doneView = rootView.findViewById(R.id.end);
        return rootView;
    }

    @Override
    public View inflate() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View footerView = LayoutInflater.from(mContext).inflate(R.layout.footerview, null);
        footerView.setLayoutParams(lp);
        return footerView;
    }

    @Override
    public void showLoadingView() {
        emptyView.setVisibility(View.GONE);
        doneView.setVisibility(View.GONE);
        loadView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorView() {
        emptyView.setVisibility(View.VISIBLE);
        doneView.setVisibility(View.GONE);
        loadView.setVisibility(View.GONE);
    }

    @Override
    public void showFinishView() {
        emptyView.setVisibility(View.GONE);
        doneView.setVisibility(View.VISIBLE);
        loadView.setVisibility(View.GONE);
    }
}
