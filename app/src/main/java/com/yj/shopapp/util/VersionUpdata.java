package com.yj.shopapp.util;

import android.app.FragmentManager;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public class VersionUpdata {
    private boolean isShow = false;
    private Context mContext;
    private FragmentManager manager;

    public VersionUpdata(boolean isShow, Context context, FragmentManager manager) {
        this.isShow = isShow;
        this.mContext = context;
        this.manager = manager;
    }

    public static VersionUpdata getInstance(boolean isShow, Context context, FragmentManager manager) {
        return new VersionUpdata(isShow, context, manager);
    }

    //检测版本
    public void updateVersion() {
        if (!NetUtils.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, Contants.NetStatus.NETDISABLE, Toast.LENGTH_SHORT).show();
            return;
        }
        String version = String.valueOf(CommonUtils.getVerCode(mContext));
        Map<String, String> params = new HashMap<>();
        params.put("version", version);
        params.put("type", "1");
        HttpHelper.getInstance().post(mContext, Contants.appd, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    int status = Integer.parseInt(jsonObject.getString("status"));
                    if (status == 0) {
                        String path = PreferenceUtils.getPrefString(mContext, "ApkPath", "");
                        if (path != null) {
                            CommonUtils.deleteFile(path);
                            PreferenceUtils.remove(mContext,"ApkPath");
                            PreferenceUtils.remove(mContext,"DownloadSuccess");
                        }
                    } else {
                        UpdataDialog.newInstance(PreferenceUtils.getPrefBoolean(mContext,"DownloadSuccess",false)).show(manager, "updata");
                    }
//                if (status == 0) {
//                    if (callback != null) {
//                        callback.LatestVersion();
//                    }
//                }
//                if (status == 1) {
//                    if (callback != null) {
//                        callback.NotTheLatestEdition();
//                    }
//                    if (isShow) {
//                        showDialog();
//                    } else {
//                        String yestday = PreferenceUtils.getPrefString(mContext, "svpday", "");
//                        String today = DateUtils.getNowDate();
//                        if (!today.equals(yestday)) {
//                            showDialog();
//                            PreferenceUtils.setPrefString(mContext, "svpday", today);
//                        }
//                    }
//
//                }
                }
            }
        });
    }

}
