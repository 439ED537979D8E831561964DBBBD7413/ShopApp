package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ExcGoods;
import com.yj.shopapp.ui.activity.Interface.OnViewScrollListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.IntegraAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class VipActivity extends BaseActivity implements IntegraAdapter.OnViewClickListener {

    @BindView(R.id.money_tv)
    TextView moneyTv;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.ranking)
    TextView ranking;
    @BindView(R.id.Presentintegral)
    TextView Presentintegral;
    @BindView(R.id.condition)
    TextView condition;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bgView)
    LinearLayout bgView;
    private ExcGoods goods;
    private IntegraAdapter adapter;
    private String goodnumber = "";
    private String IntegralRule = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_adpter;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        StatusBarUtils.from(this)
                .setActionbarView(bgView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new IntegraAdapter(mContext, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnViewScrollListenter() {
            @Override
            public void onBottom() {
                CommonUtils.goActivity(mContext, IntegralBuGoodsFragment.class, null);
            }
        });
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
        HttpHelper.getInstance().post(this, Contants.PortS.VIP_INDEX, params, new OkHttpResponseHandler<String>(this) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    JSONObject object = JSONObject.parseObject(response);
                    moneyTv.setText(String.valueOf((int) (Double.parseDouble(object.getString("summoney")))));
                    fontLarger(String.format("兑换积分 %s 分", object.getString("integral")), textView4);
                    fontLarger(String.format("提现积分 %s 分", object.getString("return_integral")), Presentintegral);
                    condition.setText(String.format("(%s)", object.getString("vip_content")));
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
                    JSONObject object = JSONObject.parseObject(response);
                    IntegralRule = object.getString("rule");
                }
            }
        });
    }

    private void showRuleDialog() {
        new MaterialDialog.Builder(mContext)
                .title("积分规则")
                .content(IntegralRule)
                .positiveText("我知道了")
                .show();
    }

    private void fontLarger(String test, TextView textView) {
        SpannableStringBuilder builder = new SpannableStringBuilder(test);
        builder.setSpan(new RelativeSizeSpan(1.4f), 4, test.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    private void getShopList() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(this, Contants.PortS.GOODS, params, new OkHttpResponseHandler<String>(this) {
            @Override
            public void onAfter() {
                super.onAfter();
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
            }
        });
    }

    @OnClick({R.id.bill, R.id.date_Tx, R.id.shopmore, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bill:
                CommonUtils.goActivity(mContext, AccountBookActivity.class, null);
                break;
            case R.id.date_Tx:
                CommonUtils.goActivity(mContext, PresentInformationActivity.class, null);
                break;
            case R.id.shopmore:
                CommonUtils.goActivity(mContext, IntegralBuGoodsFragment.class, null);
                break;
            case R.id.tv_right:
                showRuleDialog();
                break;

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.Redeem_now:
                showInputDialog(position);
                break;
            case R.id.onItemclick:
                Bundle bundle = new Bundle();
                bundle.putParcelable("exgood", goods.getData().get(position));
                CommonUtils.goActivity(mContext, ExchangeOfGoodsDetails.class, bundle);
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
                            changeGoods(goods.getData().get(position).getId());
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
                getShopList();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

}
