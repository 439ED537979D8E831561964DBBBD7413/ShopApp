package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ShopCaseDeta;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.NetUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class Shop_Case_detailsActivity extends BaseActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.rootview)
    LinearLayout rootview;
    @BindView(R.id.title)
    TextView title;
    private WebView mWebView;
    private String caseId = "";
    private ShopCaseDeta shopCase;
    private String url;
    private String shopname;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop__case_details;
    }

    @Override
    protected void initData() {
        mWebView = new WebView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWebView.setLayoutParams(params);
        rootview.addView(mWebView);
        if (getIntent().hasExtra("caseId")) {
            caseId = getIntent().getStringExtra("caseId");
        }
        if (getIntent().hasExtra("titlename")) {
            title.setText(getIntent().getStringExtra("titlename"));
        }
        if (getIntent().hasExtra("shopname")) {
            shopname = getIntent().getStringExtra("shopname");
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                view.loadUrl(request);
                return true;
            }
        });
        if (NetUtils.isNetworkConnected(mContext)) {
            getShopCase();
        } else {
            showToastShort("无网络");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void getShopCase() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", caseId);
        HttpHelper.getInstance().post(mContext, Contants.PortU.SHOP_CASE_DETAILS, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                JSONObject object = JSONObject.parseObject(json);
                if (object.getInteger("status") == 1) {
                    shopCase = object.toJavaObject(ShopCaseDeta.class);
                    url = shopCase.getData().getImgurl();
                    setData();
                } else {
                    showToastShort("无数据");
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    private void setData() {
        if (shopCase != null) {
            mWebView.loadDataWithBaseURL(null, getHtmlData(shopCase.getData().getDetails()), "text/html", "utf-8", null);
            Glide.with(mContext).load(url).into(imageView);
        }
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }



    @Override
    public void onDestroy() {
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

    @OnClick({R.id.shopDetails, R.id.Contact_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shopDetails:
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                bundle.putString("Store_id", shopCase.getData().getShop_id());
                bundle.putString("shop_name", shopname);
                bundle.putBoolean("iscan", true);
                CommonUtils.goActivity(mContext, ClassifyListActivity.class, bundle);
                break;
            case R.id.Contact_us:

                break;
        }
    }
}
