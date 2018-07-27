package com.yj.shopapp.ui.activity.shopkeeper;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.squareup.okhttp.Request;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.BuGoodShopDatail;
import com.yj.shopapp.ubeen.LimitedSale;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarUtil;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.view.SaleProgressView;
import com.yj.shopapp.view.X5WebView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class BuGoodShopDatailsActivity extends BaseActivity {

    @BindView(R.id.shopimag)
    ImageView shopimag;
    @BindView(R.id.bgView)
    RelativeLayout bgView;
    @BindView(R.id.sales_price)
    TextView salesPrice;
    @BindView(R.id.shopprice)
    TextView shopprice;
    @BindView(R.id.spv)
    SaleProgressView spv;
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.shopname)
    TextView shopname;
    @BindView(R.id.goodspec)
    TextView goodspec;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.goods_num)
    TextView goodsNum;
    @BindView(R.id.htmlView)
    FrameLayout htmlView;
    @BindView(R.id.shopDetails)
    TextView shopDetails;
    @BindView(R.id.alarmclock)
    ImageView alarmclock;
    @BindView(R.id.Flash_saleText)
    TextView FlashSaleText;
    @BindView(R.id.sales_time)
    TextView salesTime;
    @BindView(R.id.ImmediateChange)
    TextView ImmediateChange;
    @BindView(R.id.warning_tv)
    TextView warningTv;
    @BindView(R.id.warning_tv_super)
    LinearLayout warningTvSuper;
    private String shopId;
    private X5WebView mWebView;
    private String addressId = "";
    private BuGoodShopDatail shopDatail;
    private int type = 0;
    private CountDownTimer countDownTimer;
    private boolean isStartsales = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bu_good_shop_datails;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("sid")) {
            shopId = getIntent().getStringExtra("sid");
        }
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", 0);
        }
        mWebView = new X5WebView(mContext);
        htmlView.addView(mWebView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        if (type == 3) {
            alarmclock.setImageDrawable(getResources().getDrawable(R.drawable.ic_alarmclock_yellow));
            FlashSaleText.setTextColor(getResources().getColor(R.color.color_fec329));
            isStartsales = false;
        }
        addressId = PreferenceUtils.getPrefString(mContext, "addressId", "");
    }

    private void fontLarger(String test, TextView textView) {
        SpannableStringBuilder builder = new SpannableStringBuilder(test);
        builder.setSpan(new RelativeSizeSpan(1.8f), 1, test.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if (textView != null) {
            textView.setText(builder);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetWork(mContext)) {
            getShopDatails();
        }
    }

    private void getShopDatails() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", shopId);
        HttpHelper.getInstance().post(mContext, Contants.PortS.SALES_DETAILS, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    shopDatail = JSONObject.parseObject(response, BuGoodShopDatail.class);
                    if (shopDatail.getStatus() == 1) {
                        BuGoodShopDatail.DataBean bean = shopDatail.getData();
                        Glide.with(mContext).load(bean.getImgurl()).into(shopimag);
                        fontLarger(String.format("￥%s", bean.getUnitprice()), salesPrice);
                        shopprice.setText(String.format("￥%s", bean.getPrice()));
                        shopprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                        tips.setText(String.format("限购%1$s%2$s", bean.getItemcount(), bean.getUnit()));
                        if (type == 3) {
                            salesTime.setVisibility(View.VISIBLE);
                            countDownTimer = new CountDownTimer(DateUtils.ContrastTime(Long.parseLong(bean.getStart())), 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    if (salesTime != null) {
                                        salesTime.setText(String.format("%s 后开抢", DateUtils.timed(millisUntilFinished)));
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    if (salesTime != null) {
                                        salesTime.setText("正在疯抢");
                                    }
                                    ImmediateChange.setBackgroundColor(getResources().getColor(R.color.color_fc2b32));
                                    isStartsales = true;
                                }
                            }.start();
                            tips.setTextColor(getResources().getColor(R.color.color_fec329));
                            ImmediateChange.setBackgroundColor(getResources().getColor(R.color.color_aaaaaa));
                            ImmediateChange.setText("即将开抢");
                            spv.setVisibility(View.GONE);
                        } else {
                            spv.setTotalAndCurrentCount(Integer.parseInt(bean.getNum()), Integer.parseInt(bean.getSalesnum()), bean.getUnit());
                        }
                        if (bean.getIs_sale() == 1) {
                            ImmediateChange.setBackgroundColor(getResources().getColor(R.color.color_aaaaaa));
                        }
                        if (bean.getMsg().equals("")) {
                            warningTvSuper.setVisibility(View.GONE);
                        } else {
                            warningTv.setText(bean.getMsg());
                        }
                        shopname.setText(bean.getName());
                        goodspec.setText(String.format("%1$s/%2$s", bean.getSpecs(), bean.getUnit()));
                        num.setText(String.format("%1$s%2$s", bean.getNum(), bean.getUnit()));
                        goodsNum.setText(bean.getItemnumber());
                        shopDetails.setText("此商品暂无详情");
                        shopDetails.setVisibility(bean.getBrochure().equals("") ? View.VISIBLE : View.GONE);
                        // ShowLog.e(getHtmlData(bean.getBrochure()));
                        mWebView.loadDataWithBaseURL(null, getHtmlData(bean.getBrochure()), "text/html", "utf-8", null);
                    } else {
                        showToastShort(shopDatail.getInfo());
                    }
                }
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this).setActionbarView(bgView).setTransparentStatusbar(true)
                .setLightStatusBar(false).process();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 50);
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
        if (countDownTimer != null) {
            countDownTimer.cancel();
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

    private void submitData() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", shopDatail.getData().getId());
        params.put("addressid", addressId != null ? addressId : "");

        HttpHelper.getInstance().post(mContext, Contants.PortS.DO_SALES, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    JSONObject object = JSONObject.parseObject(response);
                    if (object.getInteger("status") == 1) {
                        Bundle b = new Bundle();
                        b.putParcelable("shopDatail", shopDatail.getData());
                        EventBus.getDefault().post(new LimitedSale("3"));
                        showToastShort("恭喜你，抢购成功!");
                        CommonUtils.goActivity(mContext, BuShopDetailsAcitivity.class, b, true);
                    } else {
                        showToastShort(object.getString("info"));
                    }
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }
        });
    }

    @OnClick({R.id.backTv, R.id.ImmediateChange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backTv:
                finish();
                break;
            case R.id.ImmediateChange:
                if (!isStartsales) {
                    showToastShort("此商品暂未开枪");
                } else {
                    //判断是否有地址
                    ShowLog.e(getAddressId() + "地址id");
                    if (!getAddressId().equals("")) {
                        addressId = getAddressId();
                        submitData();
                    } else {
                        new MaterialDialog.Builder(mContext).title("温馨提示!")
                                .content("您暂未添加收货地址!")
                                .positiveText("去完善信息")
                                .negativeText("下次吧")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        CommonUtils.goActivity(mContext, SAddressRefreshActivity.class, null);
                                        dialog.dismiss();
                                    }
                                }).show();
                    }

                }
                break;
        }
    }

}
