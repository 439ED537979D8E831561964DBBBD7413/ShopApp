package com.yj.shopapp.http;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.Map;

/**
 * Http请求
 */
public class HttpHelper {

    private static volatile HttpHelper instance = null;

    private HttpHelper() {
    }

    public static HttpHelper getInstance() {
        if (null == instance) {
            synchronized (HttpHelper.class) {
                if (null == instance) {
                    instance = new HttpHelper();
                }
            }
        }
        return instance;
    }

    /**
     * post 请求
     *
     * @param context
     * @param url
     * @param params
     * @param responseHandler
     */
    public void post(Context context, String url, Map<String, String> params,
                     OkHttpResponseHandler responseHandler) {

        if (context == null)
            return;
        if (url != null) {
            Log.e("url", url);
        }

        if (params != null) {
            for (String key : params.keySet()) {
                System.out.println("key = " + key + " and value = " + params.get(key));
            }
        }
        System.out.println("url-------" + url);
        OkHttpClientManager.postAsyn(url, params, responseHandler);

    }

    public void get(String url,
                    OkHttpResponseHandler responseHandler) {


        Log.e("url", url);

        System.out.println("url-------" + url);
        OkHttpClientManager.getAsyn(url, responseHandler);

    }


    /**
     * 上传文件
     *
     * @param context
     * @param url
     * @param fileKeys
     * @param files
     * @param params
     * @param responseHandler
     */
    public void upload(Context context, String url, String[] fileKeys, File[] files, Map<String, String> params,
                       OkHttpResponseHandler responseHandler) {

        if (context == null)
            return;

        OkHttpClientManager.getUploadDelegate().postAsyn(url, fileKeys, files, OkHttpClientManager.getInstance().map2Params(params), responseHandler);
    }
}
