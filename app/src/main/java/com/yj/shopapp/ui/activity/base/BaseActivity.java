package com.yj.shopapp.ui.activity.base;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.umeng.message.PushAgent;
import com.yj.shopapp.R;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.config.MyApplication;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.NotchUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarUtil;

import java.lang.reflect.Field;

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

    //private ImmersionBar mImmersionBar;
    public Bundle getBundle() {
        return bundle;
    }

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        PushAgent.getInstance(mContext).onAppStart();

        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBar();
        }
        mContext = this;
//        mImmersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary);
//        mImmersionBar.init();   //所有子类都将继承这些相同的属性
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

    protected boolean isNotch() {
        if (NotchUtils.hasNotchInScreen(mContext)) {
            NotchUtils.addStatusViewWithColor(this, Color.parseColor("#474747"), NotchUtils.getNotchSize(mContext)[1]);
            return true;
        }
        if (NotchUtils.hasNotchInScreenAtOppo(mContext)) {
            NotchUtils.addStatusViewWithColor(this, Color.parseColor("#474747"), 80);
            return true;
        }
        if (NotchUtils.hasNotchInScreenAtVoio(mContext)) {
            NotchUtils.addStatusViewWithColor(this, Color.parseColor("#474747"), CommonUtils.dip2px(mContext, 32));
            return true;
        }
        return false;
    }

    protected String getAddressId() {
        return PreferenceUtils.getPrefString(mContext, "addressId", "");
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_01ABFF), 30);
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

    protected void showKeyBoard(EditText valuet) {
        new Handler().postDelayed(() -> {
            InputMethodManager inManager = (InputMethodManager) valuet.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inManager != null;
            inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }, 300);
    }

    protected void hideImm(EditText valuet) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(valuet.getWindowToken(), 0);
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
            showToastShort("无网络");
            return false;
        }
    }


    public void toast(String msg, int duration) {
        TextView toastTv = null;
        if (mToast == null) {
            //mToast = Toast.makeText(mContext.getApplicationContext(), msg, duration);
            View toastRoot = LayoutInflater.from(mContext).inflate(R.layout.view_toast, null);
            toastTv = toastRoot.findViewById(R.id.toast_tv);
            toastTv.setText(msg);
            mToast = new Toast(mContext.getApplicationContext());
            //获取屏幕高度
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
            mToast.setGravity(Gravity.TOP, 0, height / 2);
            mToast.setDuration(duration);
            mToast.setView(toastRoot);
        } else {
            toastTv = mToast.getView().findViewById(R.id.toast_tv);
            toastTv.setText(msg);
        }
        //mToast.setGravity(Gravity.CENTER, 0, 0);
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
//        if (mImmersionBar != null) {
//            mImmersionBar.destroy();
//        }
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

    public void reflex(final TabLayout tabLayout, final int margin) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = CommonUtils.dip2px(tabLayout.getContext(), margin == 0 ? 10 : margin);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
