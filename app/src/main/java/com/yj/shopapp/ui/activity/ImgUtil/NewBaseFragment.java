package com.yj.shopapp.ui.activity.ImgUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.util.PreferenceUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by LK on 2017/8/30.
 *
 * @author LK
 */

public abstract class NewBaseFragment extends Fragment {

    protected Activity mActivity;
    public static Toast mToast;
    public String uid;
    public String token;
    Unbinder unbinder;
    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;

    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;

    /**
     * 获得全局的，防止使用getActivity()为空 * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(mActivity).inflate(getLayoutId(), container, false);
        unbinder=ButterKnife.bind(this, view);
        uid = PreferenceUtils.getPrefString(mActivity, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mActivity, Contants.Preference.TOKEN, "");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
        mIsPrepare = true;
        onLazyLoad();

    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutID * @return
     */
    protected abstract int getLayoutId();

    /**
     * 该抽象方法就是 初始化view * @param view * @param savedInstanceState
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 执行数据的加载
     */
    protected abstract void initData();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsVisible = false;
        mIsPrepare = false;
        mToast = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            mIsVisible = true;
            onLazyLoad();
        } else {
            mIsVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);

    }


    /**
     * 懒加载，仅当用户可见切view初始化结束后才会执行
     *
     * @author lk
     * @date 2016-5-26 下午4:10:20
     */
    protected void onLazyLoad() {
        if (mIsPrepare && mIsVisible) {
            initData();
            mIsVisible = false;
            mIsPrepare = false;

        }

    }

    @SuppressLint("ShowToast")
    protected void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mActivity.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    protected void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && mActivity.getCurrentFocus() != null) {
            if (mActivity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public KProgressHUD growProgress(String label) {
        KProgressHUD builder = KProgressHUD.create(mActivity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(label)
                .setCancellable(false);
        return builder;
    }
}
