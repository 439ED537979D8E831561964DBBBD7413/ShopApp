package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ExcGoods;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ExchangeOfGoodsDetails extends BaseActivity {

    @BindView(R.id.shopname)
    TextView shopname;
    @BindView(R.id.goodspec)
    TextView goodspec;
    @BindView(R.id.goods_num)
    TextView goodsNum;
    @BindView(R.id.htmlView)
    FrameLayout htmlView;
    @BindView(R.id.bugoodIntergal)
    TextView bugoodIntergal;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.shopimag)
    ImageView shopimag;
    @BindView(R.id.backTv)
    ImageView backTv;
    @BindView(R.id.bgView)
    RelativeLayout bgView;

    private WebView mWebView;
    private String goodnumber = "";
    private ExcGoods.DataBean bean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_of_goods_details;
    }

    @Override
    protected void initData() {

        if (getIntent().hasExtra("exgood")) {
            bean = getIntent().getParcelableExtra("exgood");
        }
        mWebView = new WebView(mContext);
        htmlView.addView(mWebView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        if (bean != null) {
            mWebView.loadDataWithBaseURL(null, getHtmlData(bean.getDetails()), "text/html", "utf-8", null);
            Glide.with(mContext).load(bean.getImgurl()).apply(new RequestOptions().placeholder(R.drawable.load)).into(shopimag);
            goodspec.setText(String.format("%1$s/%2$s", bean.getSpecs(), bean.getUnit()));
            num.setText(String.format("%1$s %2$s", bean.getNum(), bean.getUnit()));
            shopname.setText(bean.getName());
            goodsNum.setText(bean.getNum());
            bugoodIntergal.setText(String.format("积分：%s", bean.getIntegral()));
        }else {
            showToastShort("");
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this).setLightStatusBar(true).setTransparentStatusbar(true).setActionbarView(bgView).process();
//        StatusBarUtils.from(this).setActionbarView(bgView).setTransparentStatusbar(true)
//                .setLightStatusBar(false).process();
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 50);
    }

    private void showInputDialog() {
        goodnumber = "";
        new MaterialDialog.Builder(mContext)
                .title("请输入换购数量")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("请输入数量", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        goodnumber = dialog.getInputEditText().getText().toString();
                        if (!"".equals(goodnumber)) {
                            changeGoods();
                        }
                    }
                })
                .canceledOnTouchOutside(false)
                .show();
    }

    private void changeGoods() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("goods_id", bean.getId());
        params.put("num", goodnumber);
        HttpHelper.getInstance().post(mContext, Contants.PortS.CHANGE_GOODS, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                showToastShort(JSONObject.parseObject(json).getString("info"));
            }

            @Override
            public void onAfter() {
                super.onAfter();
                finish();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
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
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @OnClick({R.id.backTv, R.id.ImmediateChange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backTv:
                finish();
                break;
            case R.id.ImmediateChange:
                showInputDialog();
                break;
        }
    }

}
