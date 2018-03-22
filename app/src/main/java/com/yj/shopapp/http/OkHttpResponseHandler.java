package com.yj.shopapp.http;

import android.content.Context;


/**
 * 公共请求方法监听
 */
public class OkHttpResponseHandler<T> extends OkHttpClientManager.ResultCallback<String> {
    private static final String TAG = "MyAsyncHttpResponseHandler";

    public static final String ERROR_TIP = "网络不给力或者服务端异常！";

    private Context mContext;

    public OkHttpResponseHandler(Context context) {
        mContext = context;
    }

    @Override
    public void onBefore() {

        super.onBefore();
    }

    @Override
    public void onAfter() {

        super.onAfter();
    }

    @Override
    public void onError(com.squareup.okhttp.Request request, Exception e) {

    }

    @Override
    public void onResponse(com.squareup.okhttp.Request request, String response) {

    }


}
