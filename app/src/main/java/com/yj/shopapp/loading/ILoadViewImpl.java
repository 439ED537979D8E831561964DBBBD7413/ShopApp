package com.yj.shopapp.loading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;


/**
 * Created by liucanwen on 15/12/8.
 */
public class ILoadViewImpl implements ILoadView, View.OnClickListener{

    public Context mContext;

    public LoadMoreClickListener mClickListener;

    public View rootView;

    public ILoadViewImpl(Context context, LoadMoreClickListener clickListener){
        mContext = context;
        mClickListener = clickListener;

        rootView = inflate();
    }

    @Override
    public View inflate() {
        return LayoutInflater.from(mContext).inflate(R.layout.load_more_item, null);
    }

    @Override
    public void showLoadingView(View parentView) {
        ProgressBar progressBar = (ProgressBar) parentView.findViewById(R.id.progressbar_moredata);
        TextView loadingTv = (TextView) parentView.findViewById(R.id.tip_text_layout);

        progressBar.setVisibility(View.VISIBLE);
        loadingTv.setText(Contants.LoadView.LOADING);

        parentView.setClickable(false);
    }

    @Override
    public void showErrorView(View parentView) {
        ProgressBar progressBar = (ProgressBar) parentView.findViewById(R.id.progressbar_moredata);
        TextView loadingTv = (TextView) parentView.findViewById(R.id.tip_text_layout);

        progressBar.setVisibility(View.GONE);
        loadingTv.setText(Contants.LoadView.CLICKLOAD);

        parentView.setOnClickListener(this);
    }

    @Override
    public void showFinishView(View parentView) {
        ProgressBar progressBar = (ProgressBar) parentView.findViewById(R.id.progressbar_moredata);
        TextView loadingTv = (TextView) parentView.findViewById(R.id.tip_text_layout);

        progressBar.setVisibility(View.GONE);
        loadingTv.setText("数据已全部加载！");
        parentView.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        mClickListener.clickLoadMoreData();
    }
}
