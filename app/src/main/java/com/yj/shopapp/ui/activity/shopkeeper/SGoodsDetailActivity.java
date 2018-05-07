package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.LookItem;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.BugGoodsDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DialogUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/13.
 */
public class SGoodsDetailActivity extends BaseActivity {


    @BindView(R.id.shopimag)
    ImageView shopimag;
    @BindView(R.id.bgView)
    RelativeLayout bgView;
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.shopname)
    TextView shopname;
    @BindView(R.id.goodspec)
    TextView goodspec;
    @BindView(R.id.industry_name)
    TextView industryName;
    @BindView(R.id.class_name)
    TextView className;
    @BindView(R.id.goods_num)
    TextView goodsNum;
    @BindView(R.id.shopprice)
    TextView shopprice;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.maek)
    TextView maek;
    @BindView(R.id.maek_re)
    LinearLayout maekRe;
    @BindView(R.id.add)
    TextView add;
    @BindView(R.id.bottom_layout)
    LinearLayout bottomLayout;
    @BindView(R.id.promotion_price)
    TextView promotionPrice;
    @BindView(R.id.shop_stock)
    TextView shopStock;
    @BindView(R.id.shopDetails)
    TextView shopDetails;
    @BindView(R.id.maek_imag)
    ImageView maekImag;
    @BindView(R.id.warning_tv)
    TextView warningTv;
    @BindView(R.id.warning_tv_super)
    LinearLayout warningTvSuper;
    private boolean Collect = false;
    private boolean isRequesting = false;//标记，是否正在刷新
    String checkGoods = "";
    String goodsId;
    LookItem lookItem;
    @BindView(R.id.htmlView)
    FrameLayout htmlView;
    String unitTvStr;
    private WebView mWebView;
    public InputMethodManager imm;

    //    @OnClick(R.id.good_back)
//    public void goodBack() {
//        checkGoods = "refund";
//        if (lookItem.getStock().equals("0")) {
//            DialogUtils dialog = new DialogUtils();
//            dialog.getMaterialDialog(mContext, "温馨提示", "库存不足，是否需要为您匹配类似商品？", new MaterialDialog.SingleButtonCallback() {
//                @Override
//                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("bigtypeid", lookItem.getBigtypeid());
//                    CommonUtils.goActivity(mContext, SGoodsActivity.class, bundle);
//                }
//            }, null);
//            dialog.show();
//            return;
//        } else {
//            //setDialogInput();
//        }
//    }
    @OnClick({R.id.backTv, R.id.maek_re, R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backTv:
                finish();
                break;
            case R.id.maek_re:
                if (lookItem.getBookmark().equals("0")) {
                    requestMark();
                } else {
                    showToastShort("已收藏");
                }
                break;
            case R.id.add:
                checkGoods = "order";
                if (lookItem.getStock().equals("0")) {
                    DialogUtils dialog = new DialogUtils();
                    dialog.getMaterialDialog(mContext, "温馨提示", "库存不足，是否需要为您匹配类似商品？", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Bundle bundle = new Bundle();
                            bundle.putString("bigtypeid", lookItem.getBigtypeid());
                            CommonUtils.goActivity(mContext, SGoodsActivity.class, bundle);
                        }
                    }, null);
                    dialog.show();
                    return;
                } else {
                    if (getAddressId().equals("")) {
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
                    } else {
                        BugGoodsDialog.newInstance(lookItem).show(getFragmentManager(), "123");
                    }
                }
                break;
            default:
                break;
        }
    }

//    @OnClick(R.id.phoneTv)
//    public void tel() {
//        if (!StringHelper.isEmpty(phoneTv.getText().toString())) {
//            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneTv.getText().toString()));
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            startActivity(intent);
//        }
//
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_goodsdetail;
    }

    @Override
    protected void initData() {
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        goodsId = getIntent().getExtras().getString("goodsId");
        Collect = getIntent().getExtras().getBoolean("Collect");
        mWebView = new WebView(mContext);
        htmlView.addView(mWebView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
//        phoneTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//        phoneTv.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this).setLightStatusBar(true).setTransparentStatusbar(true).setActionbarView(bgView).process();
//        StatusBarUtil.setTransparentForImageView(this, bgView);
//        StatusBarUtil.setDarkMode(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetWork(mContext)) {
            refreshRequest();
        }
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    /***
     * 网络数据
     ***********************************************************/

    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", goodsId);
        params.put("itemnoid", "");
        params.put("agentuid", "");

        HttpHelper.getInstance().post(mContext, Contants.PortU.LookItem, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);


                ShowLog.e(json);
                if (JsonHelper.getRequstOK(json) == 0) {
                    JsonHelper<LookItem> jsonHelper = new JsonHelper<LookItem>(LookItem.class);
                    lookItem = jsonHelper.getData(json, null);
                    setData();
//                    Glide.with(mContext).load(lookItem.getImgurl()).into(simpleDraweeView);
//
//                    if (!StringHelper.isEmpty(lookItem.getAgentname())) {
//                        shopnameTv.setText(lookItem.getAgentname() + "  ");
//                    }
//                    phoneTv.setText(lookItem.getAgenttel());
//
//                    if (lookItem.getIs_show_price() != null && !lookItem.getIs_show_price().equals("")) {
//                        if (Integer.parseInt(lookItem.getIs_show_price()) == 0) {
//                            goodspriceTv.setText("");
//                        } else {
//                            goodspriceTv.setText(lookItem.getSprice());
//                        }
//                    } else {
//                        goodspriceTv.setText(lookItem.getSprice());
//                    }
//                    if (lookItem.getSpecialnote() != null) {
//                        if (!lookItem.getSpecialnote().equals("null") && !lookItem.getSpecialnote().equals("")) {
//                            specialnote_Tv.setText("商品提示 " + lookItem.getSpecialnote());
//                        }
//                    }
//
//                    if (lookItem.getSale_status().equals("0")) {
//                        stop_simpleDraweeView.setVisibility(View.VISIBLE);
//                        add.setClickable(false);
//
//                        add.setBackgroundResource(R.color.gary_6);
//
//                    } else {
//                        stop_simpleDraweeView.setVisibility(View.GONE);
//                    }
//
////                    string is_show_stock=PreferenceUtils.getPrefString(mContext, Contants.Preference.IS_SHOW_STOCK, "");
//                    if (lookItem.getIs_show_stock() != null && !lookItem.getIs_show_stock().equals("")) {
//
//                        if (Integer.parseInt(lookItem.getIs_show_stock()) == 0) {
//                            goodsunitTv.setText("库存:");
//                        } else {
//                            goodsunitTv.setText("  (剩余：" + lookItem.getStock() + lookItem.getUnit() + ")");
//
//                        }
//                    } else {
//                        goodsunitTv.setText("  (剩余：" + lookItem.getStock() + lookItem.getUnit() + ")");
//
//                    }
//                    if (lookItem.getIs_promotion().equals("1")) {
//                        cuxiao_lin.setVisibility(View.VISIBLE);
//                        if (lookItem.getSales().equals(1)) {
//                            cuxiao_txt.setText("满" + lookItem.getDisstr() + "送" + lookItem.getGift());
//                        } else if (lookItem.getSales().equals("2")) {
//                            cuxiao_txt.setText(("打" + Double.parseDouble(lookItem.getDisstr()) / 10 + "折"));
//                        } else if (lookItem.getSales().equals("3")) {
//                            cuxiao_txt.setText(lookItem.getDisstr());
//                        }
//                        goodspriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                    } else {
//                        cuxiao_lin.setVisibility(View.GONE);
//                        goodspriceTv.setTextColor(getResources().getColor(R.color.red));
//                        goodspriceSign.setTextColor(getResources().getColor(R.color.red));
//                    }
//                    unitTvStr = lookItem.getUnit();
//                    goodstypeTv.setText(lookItem.getTypestr());
//                    goodsspecsTv.setText(lookItem.getSpecs());
//                    goodsbrochureTv.setText(lookItem.getBrochure());
//                    goodsnumberTv.setText(lookItem.getCnumber());
//                    goodsnoidTv.setText(lookItem.getItemnoid());
//                    goodsnameTv.setText(lookItem.getName());


                } else if (JsonHelper.getRequstOK(json) == 0) {
                    showToastShort(Contants.NetStatus.NETNOFOUND);
                } else {
                    showToastShort(Contants.NetStatus.NETERROR);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });

    }

    private void setData() {
        if (!SGoodsDetailActivity.this.isFinishing()) {
            Glide.with(this).load(lookItem.getImgurl()).into(shopimag);
            shopname.setText(lookItem.getName());
            promotionPrice.setText(lookItem.getIs_promotion() == 1 ? String.format("￥%s", lookItem.getSprice()) : "".equals(lookItem.getDisstr()) ? "" : String.format("￥%s", lookItem.getDisstr()));
            promotionPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            fontLarger(String.format("￥%s", lookItem.getIs_promotion() == 1 ? lookItem.getDisstr() : lookItem.getSprice()), shopprice);
            tips.setText("".equals(lookItem.getSpecialnote()) ? "" : String.format("商品提示  %1$s", lookItem.getSpecialnote()));
            goodspec.setText(String.format("%1$s/%2$s", lookItem.getSpecs(), lookItem.getUnit()));
            industryName.setText(lookItem.getIndustry_name());
            className.setText(lookItem.getClass_name());
            goodsNum.setText(lookItem.getItemnoid());
            shopStock.setText(String.format("库存：%1$s%2$s", lookItem.getStock(), lookItem.getUnit()));
            shopDetails.setText("该商品暂无详情");
            shopDetails.setVisibility(lookItem.getBrochure().equals("") ? View.VISIBLE : View.GONE);
            if (lookItem.getBookmark().equals("1")) {
                maekImag.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_select));
            }
            if (lookItem.getMsg().equals("")) {
                warningTvSuper.setVisibility(View.GONE);
            } else {
                warningTv.setText(lookItem.getMsg());
            }
            add.setBackgroundResource(lookItem.getSale_status().equals("0") ? R.color.qianhui : R.color.red);
            add.setClickable(!lookItem.getSale_status().equals("0"));
            mWebView.loadDataWithBaseURL(null, getHtmlData(lookItem.getBrochure()), "text/html", "utf-8", null);
        }
    }


    private void fontLarger(String test, TextView textView) {
        SpannableStringBuilder builder = new SpannableStringBuilder(test);
        builder.setSpan(new RelativeSizeSpan(1.4f), 1, test.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if (textView != null) {
            textView.setText(builder);
        }
    }

    /**
     * 收藏
     */
    public void requestMark() {
        if (lookItem == null || StringHelper.isEmpty(lookItem.getId())) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", goodsId);
        final KProgressHUD kProgressHUD = growProgress(Contants.Progress.SUMBIT_ING);
        HttpHelper.getInstance().post(mContext, Contants.PortU.SetBookMark, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                kProgressHUD.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                kProgressHUD.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("收藏成功");
                    maekImag.setImageDrawable(getResources().getDrawable(R.drawable.ic_rating_select));
                } else {
                    showToastShort("已收藏");
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
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

}
