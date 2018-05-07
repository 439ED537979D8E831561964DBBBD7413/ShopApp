package com.yj.shopapp.ui.activity;

import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/24.
 */
public class MyWebView extends BaseActivity {
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.title)
    TextView titleView;
    private String url;
    private WebView mWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_mywebview;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
        }
        ShowLog.e(url);
        mWebView = new WebView(mContext);
        linearLayout.addView(mWebView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titleView.setText(title);
            }
        });
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.loadUrl(url);
        mWebView.getTitle();
    }


    @OnClick(R.id.forewadImg)
    public void onViewClicked() {
        finish();
    }


    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }

            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        super.onDestroy();
    }

}
