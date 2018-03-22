package com.yj.shopapp.ui.activity;

import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by jm on 2016/5/24.
 */
public class MyWebView2 extends BaseActivity {

    @BindView(R.id.forewadImg)
    LinearLayout forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.webView)
    WebView webView;

    String wUrl = "";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_mywebview2;
    }

    @Override
    protected void initData() {
        forewadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wUrl = getIntent().getExtras().getString("wUrl");
        wUrl = "http://u.19diandian.com/Public/uploads/classify/59a00e2169a0b.jpg";
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//		webSettings.setUseWideViewPort(true);//关键点
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }

            //重写此方法可以让webview处理https请求。
            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //设置图片加载缓存
//				super.onPageFinished(view, url);
                view.getSettings().setLoadsImagesAutomatically(true);
//                title.setText(view.getTitle());
            }
        });
        //自适应屏幕
//		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
//		webSettings.setLoadWithOverviewMode(true);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //设置页面finish后再发起图片加载
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
        webView.loadUrl(wUrl);
    }



}
