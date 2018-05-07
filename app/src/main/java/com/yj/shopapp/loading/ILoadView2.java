package com.yj.shopapp.loading;

import android.view.View;

/**
 * Created by liucanwen on 15/12/8.
 */
public interface ILoadView2 {

    public abstract View inflate();

    public abstract void showLoadingView();

    public abstract void showErrorView();

    public abstract void showFinishView();

}
