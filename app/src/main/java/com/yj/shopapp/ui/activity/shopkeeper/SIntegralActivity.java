package com.yj.shopapp.ui.activity.shopkeeper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.CenterDialog;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ExcGoods;
import com.yj.shopapp.ubeen.IntegralRule;
import com.yj.shopapp.ubeen.UserAccount;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.IntegraAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * 积分管理界面
 */
public class SIntegralActivity extends BaseActivity implements IntegraAdapter.OnViewClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.ranking)
    TextView ranking;
    @BindView(R.id.Presentintegral)
    TextView Presentintegral;
    @BindView(R.id.condition)
    TextView condition;
    @BindView(R.id.hot_mv)
    SimpleMarqueeView hotMv;
    @BindView(R.id.returnIntegral)
    LinearLayout returnIntegral;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bgView)
    RelativeLayout bgView;
    @BindView(R.id.vipImag)
    ImageView vipImag;
    @BindView(R.id.super_swipe_refresh_layout)
    SwipeRefreshLayout superSwipeRefreshLayout;
    @BindView(R.id.current_integral)
    TextView currentIntegral;
    @BindView(R.id.redeemed_integral)
    TextView redeemedIntegral;
    private Activity mContext = this;
    private KProgressHUD kProgressHUD;
    //private String IntegralRule;
    private ExcGoods goods;
    private IntegraAdapter adapter;
    private String goodnumber = "";
    private boolean NOVip = false;
    private boolean isRequst = false;
    private MarqueeFactory<TextView, String> marqueeFactory2;
    private IntegralRule integralRule;
    private CenterDialog dialog;
    private String withdrawalsType;
    private UserAccount account;
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == resultCode) {
//            requestData();
//        }
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sintegral;
    }

    @Override
    protected void initData() {
        StatusBarUtils.from(this)
                .setActionbarView(bgView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());
        superSwipeRefreshLayout.setOnRefreshListener(this);
        kProgressHUD = growProgress(Contants.Progress.LOAD_ING);
//        if (PreferenceUtils.getPrefInt(mContext, "isVip", 0) == 0) {
//            returnIntegral.setVisibility(View.GONE);
            Glide.with(mContext).load(R.drawable.ic_vip).apply(new RequestOptions().optionalTransform(new GrayscaleTransformation())).into(vipImag);
        //}
        adapter = new IntegraAdapter(mContext, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        recyclerView.setAdapter(adapter);
        marqueeFactory2 = new SimpleMF<>(mContext);
        hotMv.setMarqueeFactory(marqueeFactory2);
        dialog = new CenterDialog(mContext, R.layout.dialaog_buyment_view, new int[]{R.id.dialog_sure, R.id.dialog_cancel, R.id.zhifubao_tv, R.id.weixin_tv, R.id.dialog_close});
    }

    @Override
    protected void onStart() {
        super.onStart();
        hotMv.startFlipping();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtils.isNetworkConnected(mContext)) {
            requestData();
            getShopList();
            getRule();
        } else {
            showToastShort("无网络");
        }
    }

    private void requestData() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(this, Contants.PortS.INDEX, params, new OkHttpResponseHandler<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onBefore() {
                super.onBefore();
                kProgressHUD.show();
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    JSONObject object = JSONObject.parseObject(response);
                    fontLarger(String.format("兑换积分 %s 分", object.getString("integral")), textView4);
                    if (object.getInteger("vip") == 1) {
                        NOVip = true;
                        condition.setText(String.format("(%s)", object.getString("vip_content")));
                        fontLarger(String.format("提现积分 %s 分", object.getString("return_integral")), Presentintegral);
                    }
                    if (object.get("everybody_changing") != null && !object.get("everybody_changing").equals("") && !object.get("everybody_changing").equals("[]")) {
                        JSONArray array = object.getJSONArray("everybody_changing");
                        List<String> mdeta = array.toJavaList(String.class);
                        if (marqueeFactory2.getData() == null) {
                            marqueeFactory2.setData(mdeta);
                        }
                        //MarqueeView设置Factory
                        hotMv.startFlipping();
                    }
                    currentIntegral.setText(object.getString("integral"));
                    redeemedIntegral.setText(String.format("已兑换%s积分", object.getString("change_integral")));

                }
            }

            @Override
            public void onAfter() {
                super.onAfter();

            }
        });
    }


    private void fontLarger(String test, TextView textView) {
        SpannableStringBuilder builder = new SpannableStringBuilder(test);
        builder.setSpan(new RelativeSizeSpan(1.4f), 4, test.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if (textView != null) {
            textView.setText(builder);
        }
    }

    private void getShopList() {
        if (isRequst) return;
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(this, Contants.PortS.GOODS, params, new OkHttpResponseHandler<String>(this) {
            @Override
            public void onAfter() {
                super.onAfter();
                if (superSwipeRefreshLayout != null) {
                    superSwipeRefreshLayout.setRefreshing(false);
                }
                isRequst = false;
                kProgressHUD.dismiss();
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    JSONObject object = JSONObject.parseObject(response);
                    if (object.getInteger("status") == 0) {
                        showToastShort(object.getString("info"));
                    } else {
                        goods = object.toJavaObject(ExcGoods.class);
                        adapter.setList(goods.getData());
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (superSwipeRefreshLayout != null) {
                    superSwipeRefreshLayout.setRefreshing(false);
                }
                isRequst = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequst = true;
            }
        });
    }

    private void getChangeInfo(int position) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(this, Contants.PortS.MYCHANNGE_INFO, params, new OkHttpResponseHandler<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                //status 为0 未填写资料 为1 为填写资料
                if (JsonHelper.isRequstOK(response, mContext)) {
                    JSONObject object = JSONObject.parseObject(response);
                    if (object.getInteger("status") == 1) {
                        account = object.toJavaObject(UserAccount.class);
                        withdrawalsType = account.getData().getType();
                        //显示弹窗R
                        showModifyNumberDialog(position);
                    } else {
                        //跳转页面
                        new MaterialDialog.Builder(mContext).title("温馨提示").content("您还未填写提现资料，现在就去添加").positiveText("确定").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                CommonUtils.goActivity(SIntegralActivity.this, PresentInformationActivity.class, null);
                            }
                        }).show();
                    }
                }
            }
        });
    }

    private void showModifyNumberDialog(int position) {
        dialog.show();

        ((TextView) dialog.findViewById(R.id.weixin_tv)).setText(account.getData().getWx_name());
        ((TextView) dialog.findViewById(R.id.zhifubao_tv)).setText(account.getData().getZfb_name());
        ((TextView) dialog.findViewById(R.id.zhifubao_account)).setText(account.getData().getZfb());
        ((TextView) dialog.findViewById(R.id.weixin_account)).setText(account.getData().getWx());
        if (account.getData().getZfb().equals("")) {
            dialog.findViewById(R.id.zhifubao_rl).setVisibility(View.GONE);
        }
        if (account.getData().getWx().equals("")) {
            dialog.findViewById(R.id.weixin_rl).setVisibility(View.GONE);
        }
        if (account.getData().getType().equals("1")) {
            dialog.findViewById(R.id.weixin_select).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.zhifubao_select).setVisibility(View.GONE);
            withdrawalsType = "1";
        }
        if (account.getData().getType().equals("2")) {
            dialog.findViewById(R.id.zhifubao_select).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.weixin_select).setVisibility(View.GONE);
            withdrawalsType = "2";
        }
        EditText valueTv = dialog.findViewById(R.id.ecit_phone);
        showKeyBoard(valueTv);
        dialog.setOnCenterItemClickListener(new CenterDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(CenterDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.dialog_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.dialog_sure:
                        goodnumber = valueTv.getText().toString();
                        changeGoods(goods.getData().get(position).getId());
                        dialog.dismiss();
                        break;
                    case R.id.weixin_tv:
                        withdrawalsType = "1";
                        dialog.findViewById(R.id.weixin_select).setVisibility(View.VISIBLE);
                        dialog.findViewById(R.id.zhifubao_select).setVisibility(View.GONE);
                        break;
                    case R.id.zhifubao_tv:
                        withdrawalsType = "2";
                        dialog.findViewById(R.id.zhifubao_select).setVisibility(View.VISIBLE);
                        dialog.findViewById(R.id.weixin_select).setVisibility(View.GONE);
                        break;
                    case R.id.dialog_close:
                        dialog.dismiss();
                        break;
                }
            }
        });
    }

    private void getRule() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(this, Contants.PortS.RULE, params, new OkHttpResponseHandler<String>(this) {
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
                if (!response.isEmpty()) {
                    integralRule = JSONObject.parseObject(response, IntegralRule.class);
                }
            }
        });
    }

    private void showRuleDialog() {
        new MaterialDialog.Builder(mContext)
                .title(integralRule.getTitle())
                .content(integralRule.getContents())
                .positiveText("我知道了")
                .show();
    }


    @OnClick({R.id.more, R.id.exchangeshop, R.id.exchangeRecord, R.id.integraldatails, R.id.my_Vip, R.id.shopmore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more:
                CommonUtils.goActivity(mContext, EverybodyChangeActivity.class, null);
                break;
            case R.id.exchangeshop:
                CommonUtils.goActivity(mContext, PresentInformationActivity.class, null);
                //CommonUtils.goActivity(mContext, IntegralBuGoodsFragment.class, null);
                break;
            case R.id.exchangeRecord:
                CommonUtils.goActivity(mContext, SIntegralChangeActivity.class, null);
                break;
            case R.id.integraldatails:
                CommonUtils.goActivity(mContext, SIntegralDetailActivity.class, null);
                break;
            case R.id.my_Vip:
//                if (NOVip) {
//                    CommonUtils.goActivity(mContext, VipActivity.class, null);
//                } else {
                showRuleDialog();
                // }

                break;
            case R.id.shopmore:
                CommonUtils.goActivity(mContext, IntegralBuGoodsFragment.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.Redeem_now:
                //showInputDialog(position);
                getChangeInfo(position);
                break;
            case R.id.onItemclick:
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("exgood", goods.getData().get(position));
//                CommonUtils.goActivity(mContext, ExchangeOfGoodsDetails.class, bundle);
                break;
        }
    }

    private void showInputDialog(final int position) {
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

                        }
                    }
                })
                .canceledOnTouchOutside(false)
                .show();
    }

    private void changeGoods(String gid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("goods_id", gid);
        params.put("num", goodnumber);
        params.put("type", withdrawalsType); //类型
        params.put("username", withdrawalsType.equals("2") ? account.getData().getZfb_name() : account.getData().getWx_name()); //支付宝账号
        params.put("nickname", withdrawalsType.equals("2") ? account.getData().getZfb() : account.getData().getWx()); //支付宝昵称
        //ShowLog.e(String.format("%1$s|%2$s|%3$s|%4$s|%5$s",uid,token,gid,goodnumber,site));
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
                if (superSwipeRefreshLayout != null) {
                    superSwipeRefreshLayout.setRefreshing(true);
                }
                onRefresh();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        hotMv.stopFlipping();
    }


    @Override
    public void onRefresh() {
        requestData();
        getShopList();
    }
}
