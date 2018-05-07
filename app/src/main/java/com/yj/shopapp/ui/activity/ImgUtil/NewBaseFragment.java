package com.yj.shopapp.ui.activity.ImgUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.util.NetUtils;
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
    private Bundle bundle;

    /**
     * 获得全局的，防止使用getActivity()为空 * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }

    public Bundle getBundle() {
        return bundle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(mActivity).inflate(getLayoutId(), container, false);
        bundle = savedInstanceState;
        unbinder = ButterKnife.bind(this, view);
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
        if (unbinder != null) {
            unbinder.unbind();
        }
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

    /**
     * 检测网络
     *
     * @param context
     * @return
     */
    protected boolean isNetWork(Context context) {
        if (NetUtils.isNetworkConnected(context)) {
            return true;
        } else {
            showToast("无网络");
            return false;
        }
    }

    @SuppressLint("ShowToast")
    protected void showToast(String msg) {
        TextView toastTv = null;
        if (mToast == null) {
            View toastRoot = LayoutInflater.from(mActivity).inflate(R.layout.view_toast, null);
            toastTv = toastRoot.findViewById(R.id.toast_tv);
            toastTv.setText(msg);
            mToast = new Toast(mActivity.getApplicationContext());
            //获取屏幕高度
            WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
            mToast.setGravity(Gravity.TOP, 0, height / 2);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setView(toastRoot);
            //mToast = Toast.makeText(mActivity.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toastTv = mToast.getView().findViewById(R.id.toast_tv);
            toastTv.setText(msg);
        }
        //mToast.setGravity(Gravity.CENTER, 0, 0);
        if (!msg.equals("")) {
            mToast.show();
        }
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
