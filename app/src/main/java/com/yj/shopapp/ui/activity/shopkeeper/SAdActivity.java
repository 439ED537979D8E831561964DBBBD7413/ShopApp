package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.view.X5WebView;

import butterknife.BindView;

public class SAdActivity extends BaseActivity {

    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.webView_container)
    RelativeLayout webViewContainer;
    private X5WebView webView;
    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sad;
    }

    @Override
    protected void initData() {
        title.setText("广告详情");
        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
            ShowLog.e(url + "");
        }
        webView = new X5WebView(mContext);
        webViewContainer.addView(webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                addImageClickListner();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
        webView.addJavascriptInterface(new JavascriptInterface(mContext), "imagelistner");
    }


    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  "
                + "{"
                + "    objs[i].onclick=function()  "
                + "    {  "
                + "window.imagelistner.openImage(this.src);"
                + "}"
                + "}"
                + "})()");
    }

    // js通信接口
    public static class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            System.out.println(img);
            Intent intent = new Intent();
            intent.putExtra("image", img);
            intent.setClass(context, ShowWebImageActivity.class);
            context.startActivity(intent);
            System.out.println(img);
        }
    }


    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();

    }
}
