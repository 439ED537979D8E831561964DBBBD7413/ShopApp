package com.yj.shopapp.util;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public class VersionUpdata {
    private Context mContext;
    private FragmentManager manager;
    @SuppressLint("StaticFieldLeak")
    private static VersionUpdata instance;

    private VersionUpdata() {
    }

    public static VersionUpdata getInstance() {
        if (instance == null) {
            instance = new VersionUpdata();
        }
        return instance;
    }

    public VersionUpdata init(Context context, FragmentManager manager) {
        this.mContext = context;
        this.manager = manager;
        return this;
    }

    //检测版本
    public void updateVersion() {
        int status = PreferenceUtils.getPrefInt(mContext, "appVersion", 1);
        if (status == 0) {
            //已是最新版
            String path = PreferenceUtils.getPrefString(mContext, "ApkPath", "");
            if (!"".equals(path)) {
                CommonUtils.deleteFile(path);
                PreferenceUtils.remove(mContext, "ApkPath");
                PreferenceUtils.remove(mContext, "DownloadSuccess");
                PreferenceUtils.remove(mContext, "downloadUpdateApkId");
            }
        } else {
            UpdataDialog.newInstance(PreferenceUtils.getPrefBoolean(mContext, "DownloadSuccess", false)).show(manager, "updata");
        }

    }

    public void onDestroy() {
        if (instance != null) {
            instance = null;
        }
    }
}
