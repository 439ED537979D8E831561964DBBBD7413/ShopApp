package com.yj.shopapp.ui.activity.base;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.umeng.message.PushAgent;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.config.MyApplication;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.PreferenceUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Activity基类，所有的Activity均继承它
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected MyApplication myApplication;
    public String WId;
    public String uid;
    public String token;
    public String agentuid;
    public String userPhone;
    public int verCode;
    protected Context mContext = this;
    //private string mPageName;
    //是否ActivityGroup父视图
    protected boolean mIsActivityGroupBase = false;
    Unbinder unbinder;
    private Bundle bundle;

    public Bundle getBundle() {
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        PushAgent.getInstance(mContext).onAppStart();
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        mContext = this;
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");
        WId = PreferenceUtils.getPrefString(mContext, Contants.Preference.AGENTUID, "");
        agentuid = PreferenceUtils.getPrefString(mContext, Contants.Preference.AGENTUID, "");
        userPhone = PreferenceUtils.getPrefString(mContext, Contants.Preference.USER_NAME, "");
        verCode = CommonUtils.getVerCode(mContext);
        myApplication = (MyApplication) getApplication();
        // 添加Activity到堆栈
        //屏蔽登录页面
        if (this.getClass().getCanonicalName().indexOf("Login") == -1) {
            AppManager.getAppManager().addActivity(this);
        }
        initData();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 获取布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    public void Forewad(View v) {
        this.finish();
    }


    /**
     * 重写setContentView，让子类传入的View上方再覆盖一层LoadingView
     */
    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(mContext).inflate(layoutResID, null);
        super.setContentView(view);
    }

    /**
     * 隐藏键盘
     */
    public void unkeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private Toast mToast = null;

    public void toast(String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext.getApplicationContext(), msg, duration);
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
        KProgressHUD builder = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(label)
                .setCancellable(false);
        return builder;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mToast != null) {
            mToast.cancel();
        }
        unbinder.unbind();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().removeActivityFromStack(this);
        PreferenceUtils.setPrefInt(mContext, Contants.Preference.ISLOGGIN, 0);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return super.getResources();
    }

    public void showbg() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public void hidebg() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
