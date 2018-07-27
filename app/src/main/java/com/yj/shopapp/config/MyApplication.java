package com.yj.shopapp.config;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.util.NetStateReceiver;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.RudenessScreenHelper;

public class MyApplication extends Application {

    public String agentuid = null;
    public String agentuname = null;

    public String getAgentuid() {
        return agentuid;
    }

    public void setAgentuid(String agentuid) {
        this.agentuid = agentuid;
    }

    public String getAgentuname() {
        return agentuname;
    }

    public void setAgentuname(String agentuname) {
        this.agentuname = agentuname;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        ShowLog.syncIsDebug(getApplicationContext());
        new RudenessScreenHelper(this, 750).activate();
        setAgentuid("");
        setAgentuname("");
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
        initImageLoader();
        /*开启网络广播监听*/
        NetStateReceiver.registerNetworkStateReceiver(this);
        PushAgent mPushAgent = PushAgent.getInstance(this);
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("deviceToken", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        initX5WebView();
        CrashReport.initCrashReport(getApplicationContext(), "e276ff5502", false);
        CrashReport.setUserId(this, PreferenceUtils.getPrefString(this, Contants.Preference.UID, ""));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.color.color_c9c9c9)
                .showImageOnFail(R.color.color_c9c9c9)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initX5WebView() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        NetStateReceiver.unRegisterNetworkStateReceiver(this);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
