package com.yj.shopapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.view.X5WebView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/24.
 */
public class MyWebView2 extends BaseActivity {

    @BindView(R.id.linearLayout)
    RelativeLayout linearLayout;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.Consultation)
    TextView Consultation;
    private String url;
    private X5WebView mWebView;
    private String tel = "";
    private static final int REQUEST_CODE = 1;
    private final int REQUEST_CODEC = 0x1001;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mywebview2;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
        }
        if (getIntent().hasExtra("phone")) {
            tel = getIntent().getStringExtra("phone");
        }
        ShowLog.e(url);
        mWebView = new X5WebView(mContext);
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
        if (tel.equals("")) {
            Consultation.setVisibility(View.GONE);
        } else {
            Consultation.setVisibility(View.VISIBLE);
        }

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

    @OnClick({R.id.forewadImg, R.id.Consultation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forewadImg:
                finish();
                break;
            case R.id.Consultation:
                if (Build.VERSION.SDK_INT >= 23) {
                    //判断有没有拨打电话权限
                    if (PermissionChecker.checkSelfPermission(MyWebView2.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        //请求拨打电话权限
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODEC);
                    } else {
                        callPhone();
                    }
                } else {
                    callPhone();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODEC && PermissionChecker.checkSelfPermission(MyWebView2.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            callPhone();
        } else {
            Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void callPhone() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
            startActivity(intent);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MyWebView2.this, Manifest.permission.CALL_PHONE)) {
                //已经禁止提示了("您已禁止该权限，需要重新开启");
                Toast.makeText(mContext, "您已禁止该权限，需要重新开启", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(MyWebView2.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

            }

        }

    }

}
