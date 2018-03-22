package com.yj.shopapp.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 网络请求方法
 */
public class HttpClientUtil {
	
	private static final String TAG = "HttpClient";
	
	private static final int TIME_OUT_MILLIS = 30 * 1000;

	private static AsyncHttpClient client = null;

	public static void get(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		
		getInstance().setTimeout(TIME_OUT_MILLIS);
        getInstance().setConnectTimeout(TIME_OUT_MILLIS);
        getInstance().setResponseTimeout(TIME_OUT_MILLIS * 3);
		getInstance().get(url, params, responseHandler);

	}

	public static void post(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		getInstance().setTimeout(TIME_OUT_MILLIS);
		getInstance().setConnectTimeout(TIME_OUT_MILLIS);
		getInstance().setResponseTimeout(TIME_OUT_MILLIS * 3);
		getInstance().post(url, params, responseHandler);

	}

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	private synchronized static AsyncHttpClient getInstance() {
		if (null == client) {
			client = new AsyncHttpClient();
		}

		return client;
	}
}
