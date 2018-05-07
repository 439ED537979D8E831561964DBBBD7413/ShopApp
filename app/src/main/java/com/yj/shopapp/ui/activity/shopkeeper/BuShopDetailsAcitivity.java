package com.yj.shopapp.ui.activity.shopkeeper;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.BuGoodShopDatail;
import com.yj.shopapp.ubeen.LimitedSale;
import com.yj.shopapp.ubeen.MyBuGood;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class BuShopDetailsAcitivity extends BaseActivity {

    @BindView(R.id.shopimag)
    ImageView shopimag;
    @BindView(R.id.shopname)
    TextView shopname;
    @BindView(R.id.shopspec)
    TextView shopspec;
    @BindView(R.id.shopprice)
    TextView shopprice;
    @BindView(R.id.shopnumber)
    TextView shopnumber;
    @BindView(R.id.Preferential)
    TextView Preferential;
    @BindView(R.id.shopnum)
    TextView shopnum;
    @BindView(R.id.Amount_payable)
    TextView AmountPayable;
    @BindView(R.id.addwebView)
    FrameLayout addwebView;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    private LimitedSale limitedSale;
    private WebView mWebView;
    private BuGoodShopDatail.DataBean shopDatailben;
    private MyBuGood.ListsBean listsBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bu_shop_details;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("LimitedSale")) {
            limitedSale = getIntent().getParcelableExtra("LimitedSale");
        }
        if (getIntent().hasExtra("shopDatail")) {
            shopDatailben = getIntent().getParcelableExtra("shopDatail");
        }
        if (getIntent().hasExtra("mybugood")) {
            listsBean = getIntent().getParcelableExtra("mybugood");
        }
        mWebView = new WebView(mContext);
        addwebView.addView(mWebView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetWork(mContext)) {
            setData();
            getRule();
        }
    }

    private void setData() {
        if (limitedSale != null) {
            Glide.with(mContext).load(limitedSale.getImgurl()).into(shopimag);
            shopname.setText(limitedSale.getName());
            shopspec.setText(String.format("商品规格：%s", limitedSale.getSpecs() == null ? "暂无" : limitedSale.getSpecs()));
            shopprice.setText(String.format("￥%s", limitedSale.getUnitprice()));
            shopnumber.setText(String.format("x%s", limitedSale.getItemcount()));
            shopnum.setText(String.format("%1$s%2$s", limitedSale.getItemcount(), limitedSale.getUnit()));
            AmountPayable.setText(Html.fromHtml("￥" + calculatingPrice(limitedSale.getUnitprice(), limitedSale.getItemcount())));
        } else if (shopDatailben != null) {
            Glide.with(mContext).load(shopDatailben.getImgurl()).into(shopimag);
            shopname.setText(shopDatailben.getName());
            shopspec.setText(String.format("商品规格：%s", shopDatailben.getSpecs() == null ? "暂无" : shopDatailben.getSpecs()));
            shopprice.setText(String.format("￥%s", shopDatailben.getUnitprice()));
            shopnumber.setText(String.format("x%s", shopDatailben.getItemcount()));
            shopnum.setText(String.format("%1$s%2$s", shopDatailben.getItemcount(), shopDatailben.getUnit()));
        } else {
            Glide.with(mContext).load(listsBean.getImgurl()).into(shopimag);
            shopname.setText(listsBean.getName());
            shopspec.setText(String.format("商品规格：%s", listsBean.getSpecs() == null ? "暂无" : listsBean.getSpecs()));
            shopprice.setText(String.format("￥%s", listsBean.getUnitprice()));
            shopnumber.setText(String.format("x%s", listsBean.getItemcount()));
            shopnum.setText(String.format("%1$s%2$s", listsBean.getItemcount(), listsBean.getUnit()));
        }

    }

    private Double calculatingPrice(String price, String count) {
        Double mPrice = Double.valueOf(price);
        int num = Integer.parseInt(count);
        return mPrice * num;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 50);
    }


    private void getRule() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortS.MSG, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    mWebView.loadDataWithBaseURL(null, getHtmlData(JSONObject.parseObject(response).getString("html")), "text/html", "utf-8", null);
                }

            }

            @Override
            public void onBefore() {
                super.onBefore();
            }
        });

    }

    @OnClick({R.id.submit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit_tv:
                EventBus.getDefault().post(new LimitedSale());
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new LimitedSale());
        super.onBackPressed();
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

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body style=margin:0;padding:0>" + bodyHTML + "</body></html>";
    }

}
