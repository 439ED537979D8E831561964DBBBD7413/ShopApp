package com.yj.shopapp.ui.activity.shopkeeper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.LookItem;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.BugGoodsDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DialogUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/13.
 */
public class SGoodsDetailActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.specialnote_Tv)
    TextView specialnote_Tv;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.maek)
    TextView maek;
    @BindView(R.id.add)
    TextView add;
    @BindView(R.id.good_back)
    TextView good_back;
    @BindView(R.id.bottom_layout)
    LinearLayout bottomLayout;
    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;

    @BindView(R.id.stop_simpleDraweeView)
    SimpleDraweeView stop_simpleDraweeView;
    @BindView(R.id.shopnameTv)
    TextView shopnameTv;
    @BindView(R.id.phoneTv)
    TextView phoneTv;
    @BindView(R.id.goodsnameTv)
    TextView goodsnameTv;
    @BindView(R.id.goodsstatusTv)
    TextView goodsstatusTv;
    @BindView(R.id.goodsunitTv)
    TextView goodsunitTv;
    @BindView(R.id.goodspriceTv)
    TextView goodspriceTv;
    @BindView(R.id.goodstypeTv)
    TextView goodstypeTv;
    @BindView(R.id.goodsspecsTv)
    TextView goodsspecsTv;
    @BindView(R.id.goodsnoidTv)
    TextView goodsnoidTv;
    @BindView(R.id.goodsnumberTv)
    TextView goodsnumberTv;
    @BindView(R.id.goodsbrochureTv)
    TextView goodsbrochureTv;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.cuxiao_lin)
    LinearLayout cuxiao_lin;
    @BindView(R.id.cuxiao_txt)
    TextView cuxiao_txt;
    String checkGoods = "";
    public InputMethodManager imm;
    @BindView(R.id.goodsprice_sign)
    TextView goodspriceSign;
    @BindView(R.id.maek_re)
    RelativeLayout maekRe;
    @BindView(R.id.cuxiaotxt)
    TextView cuxiaotxt;
    private boolean Collect = false;
    private boolean isRequesting = false;//标记，是否正在刷新

    String goodsId;
    LookItem lookItem;
    String unitTvStr;

    @OnClick(R.id.add)
    public void add() {

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
            BugGoodsDialog.newInstance(lookItem).show(getFragmentManager(), "123");
        }

    }

    @OnClick(R.id.good_back)
    public void goodBack() {
        checkGoods = "refund";
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
            //setDialogInput();
        }
    }

    @OnClick({R.id.maek_re})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.maek_re:
                if (!Collect) {
                    requestMark();
                } else {
                    showToastShort("已收藏");
                }
                break;
        }
    }

    @OnClick(R.id.phoneTv)
    public void tel() {
        if (!StringHelper.isEmpty(phoneTv.getText().toString())) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneTv.getText().toString()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }

    }
    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_goodsdetail;
    }

    @Override
    protected void initData() {
        shopnameTv.setVisibility(View.GONE);
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        title.setText("商品详情");
        goodsId = getIntent().getExtras().getString("goodsId");
        Collect = getIntent().getExtras().getBoolean("Collect");
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        phoneTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        phoneTv.getPaint().setAntiAlias(true);//抗锯齿

        if (NetUtils.isNetworkConnected(mContext)) {
            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(true);

                        refreshRequest();
                    }
                }, 200);
            }
        } else {
            showToastShort("网络不给力");
        }
        TextPaint tp = goodsnameTv.getPaint();
        tp.setFakeBoldText(true);
    }


    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshRequest();
        }
    };


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
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
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


                System.out.println("response" + json);
                if (JsonHelper.getRequstOK(json) == 0) {
                    JsonHelper<LookItem> jsonHelper = new JsonHelper<LookItem>(LookItem.class);
                    lookItem = jsonHelper.getData(json, null);
                    Glide.with(mContext).load(lookItem.getImgurl()).apply(new RequestOptions().centerCrop()).into(simpleDraweeView);

                    if (!StringHelper.isEmpty(lookItem.getAgentname())) {
                        shopnameTv.setText(lookItem.getAgentname() + "  ");
                    }
                    phoneTv.setText(lookItem.getAgenttel());

                    if (lookItem.getIs_show_price() != null && !lookItem.getIs_show_price().equals("")) {
                        if (Integer.parseInt(lookItem.getIs_show_price()) == 0) {
                            goodspriceTv.setText("");
                        } else {
                            goodspriceTv.setText(lookItem.getSprice());
                        }
                    } else {
                        goodspriceTv.setText(lookItem.getSprice());
                    }
                    if (lookItem.getSpecialnote() != null) {
                        if (!lookItem.getSpecialnote().equals("null") && !lookItem.getSpecialnote().equals("")) {
                            specialnote_Tv.setText("商品提示 " + lookItem.getSpecialnote());
                        }
                    }

                    if (lookItem.getSale_status().equals("0")) {
                        stop_simpleDraweeView.setVisibility(View.VISIBLE);
                        add.setClickable(false);

                        add.setBackgroundResource(R.color.gary_6);

                    } else {
                        stop_simpleDraweeView.setVisibility(View.GONE);
                    }

//                    string is_show_stock=PreferenceUtils.getPrefString(mContext, Contants.Preference.IS_SHOW_STOCK, "");
                    if (lookItem.getIs_show_stock() != null && !lookItem.getIs_show_stock().equals("")) {

                        if (Integer.parseInt(lookItem.getIs_show_stock()) == 0) {
                            goodsunitTv.setText("库存:");
                        } else {
                            goodsunitTv.setText("  (剩余：" + lookItem.getStock() + lookItem.getUnit() + ")");

                        }
                    } else {
                        goodsunitTv.setText("  (剩余：" + lookItem.getStock() + lookItem.getUnit() + ")");

                    }
                    if (lookItem.getIs_promotion().equals("1")) {
                        cuxiao_lin.setVisibility(View.VISIBLE);
                        if (lookItem.getSales().equals(1)) {
                            cuxiao_txt.setText("满" + lookItem.getDisstr() + "送" + lookItem.getGift());
                        } else if (lookItem.getSales().equals("2")) {
                            cuxiao_txt.setText(("打" + Double.parseDouble(lookItem.getDisstr()) / 10 + "折"));
                        } else if (lookItem.getSales().equals("3")) {
                            cuxiao_txt.setText(lookItem.getDisstr());
                        }
                        goodspriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        cuxiao_lin.setVisibility(View.GONE);
                        goodspriceTv.setTextColor(getResources().getColor(R.color.red));
                        goodspriceSign.setTextColor(getResources().getColor(R.color.red));
                    }
                    unitTvStr = lookItem.getUnit();
                    goodstypeTv.setText(lookItem.getTypestr());
                    goodsspecsTv.setText(lookItem.getSpecs());
                    goodsbrochureTv.setText(lookItem.getBrochure());
                    goodsnumberTv.setText(lookItem.getCnumber());
                    goodsnoidTv.setText(lookItem.getItemnoid());
                    goodsnameTv.setText(lookItem.getName());


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



}
