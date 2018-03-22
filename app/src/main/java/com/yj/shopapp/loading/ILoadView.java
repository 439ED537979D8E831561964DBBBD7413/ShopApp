package com.yj.shopapp.loading;

import android.view.View;

/**
 * Created by liucanwen on 15/12/8.
 */
public interface ILoadView {

    public abstract View inflate();

    public abstract void showLoadingView(View parentView);

    public abstract void showErrorView(View parentView);

    public abstract void showFinishView(View parentView);

}
