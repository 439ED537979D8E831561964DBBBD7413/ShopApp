package com.yj.shopapp.ui.activity.base;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.config.MyApplication;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.PreferenceUtils;

import net.tsz.afinal.FinalActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ClassName: NewBaseFragment
 * @Description: Fragment基类
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;
    protected View mFragmentView;
    protected MyApplication myApplication;
    public String WId;
    public String uid;
    public String token;
    public String userPhone;
    Unbinder unbinder;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(getLayoutID(), container, false);
        FinalActivity.initInjectedView(this, viewRoot);
        unbinder=ButterKnife.bind(this, viewRoot);
        return viewRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFragmentView = this.getView();
        myApplication = (MyApplication) mActivity.getApplication();
        uid = PreferenceUtils.getPrefString(mActivity, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mActivity, Contants.Preference.TOKEN, "");
        WId = PreferenceUtils.getPrefString(mActivity, Contants.Preference.AGENTUID, "");
        userPhone = PreferenceUtils.getPrefString(mActivity, Contants.Preference.USER_NAME, "");
        init(savedInstanceState);


    }


    public abstract void init(Bundle savedInstanceState);
    /**
     * @description: 设置PageName，用于友盟页面统计
     * @return void
     */
//	protected abstract string getPageName();

    /**
     * 获取页面布局ID
     *
     * @return 返回整型的ID 值
     */
    public abstract int getLayoutID();


    private Toast mToast = null;

    private void toast(String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(mActivity, msg, duration);
        } else {
            mToast.setText(msg);
            mToast.setDuration(duration);
        }
        mToast.show();
    }


    public void showToastLong(String msg) {
        if (null != msg && !CommonUtils.isEmpty(msg)) {
            toast(msg, Toast.LENGTH_LONG);
        }
    }


    public void showToastShort(String msg) {
        if (null != msg && !CommonUtils.isEmpty(msg)) {
            toast(msg, Toast.LENGTH_SHORT);
        }
    }

    public KProgressHUD growProgress(String label) {
        final KProgressHUD builder = KProgressHUD.create(mActivity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(label)
                .setCancellable(true);
        return builder;
    }

    public void showbg() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.6f;
        mActivity.getWindow().setAttributes(lp);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public void hidebg() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mActivity.getWindow().setAttributes(lp);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
