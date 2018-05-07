package com.yj.shopapp.ui.activity.base;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.yj.shopapp.config.AppManager;
import com.yj.shopapp.config.MyApplication;
import com.yj.shopapp.ui.activity.upversion.Callback;
import com.yj.shopapp.ui.activity.upversion.ConfirmDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.VersionUpdata;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseTabActivity extends FinalFragmentActivity {

    protected Context mContext = this;

    protected MyApplication myApplication;

    protected List<Fragment> mFragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        // 去除头部
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化
        init(savedInstanceState);

        myApplication = (MyApplication) getApplication();
        checkAndUpdate(mContext);
    }

    /**
     * @param savedInstanceState
     * @return void
     * @description: 主要用于控件等初始化工作
     */
    protected abstract void init(Bundle savedInstanceState);


    /**
     * 重写setContentView，让子类传入的View上方再覆盖一层LoadingView
     */
    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(mContext).inflate(layoutResID, null);
        super.setContentView(view);
    }

    /**
     * @Description:finish Activity 提供给xml中"返回"Button的android:Onclick属性调用
     */
    public void finish(View view) {
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().removeActivityFromStack(this);

    }


    private Toast mToast = null;

    private void toast(String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, msg, duration);
        } else {
            mToast.setText(msg);
            //mToast.setDuration(duration);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        checkAndUpdate(mContext);
    }

    public KProgressHUD growProgress(String label) {
        final KProgressHUD builder = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(label)
                .setCancellable(true);
        return builder;
    }

    private void checkAndUpdate(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            VersionUpdata.getInstance(false, mContext, getFragmentManager()).updateVersion();
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                VersionUpdata.getInstance(false, mContext, getFragmentManager()).updateVersion();
            } else {//申请权限
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                try {
                    if (grantResults != null) {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            VersionUpdata.getInstance(false, mContext, getFragmentManager()).updateVersion();
                        } else {
                            new ConfirmDialog(this, new Callback() {
                                @Override
                                public void callback(int position) {
                                    if (position == 1) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                                        startActivity(intent);
                                    }
                                }
                            }).setContent("暂无读写SD卡权限\n是否前往设置？").show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}

