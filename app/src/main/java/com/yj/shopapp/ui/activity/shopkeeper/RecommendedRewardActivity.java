package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.RecommendIndex;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ClientListAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RecommendedRewardActivity extends BaseActivity {

    @BindView(R.id.client_num)
    TextView clientNum;
    @BindView(R.id.all_income)
    TextView allIncome;
    @BindView(R.id.month_income)
    TextView monthIncome;
    @BindView(R.id.client_rv)
    RecyclerView clientRv;
    @BindView(R.id.title_view)
    ConstraintLayout titleView;
    private ClientListAdpter listAdpter;
    private RecommendIndex recommendIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recommended_reward2;
    }

    @Override
    protected void initData() {
        if (Contants.isNotch) {
            StatusBarUtils.from(this)
                    .setActionbarView(titleView)
                    .setTransparentStatusbar(true)
                    .setLightStatusBar(false)
                    .process();
        }
        listAdpter = new ClientListAdpter(mContext);
        clientRv.setLayoutManager(new LinearLayoutManager(mContext));
        clientRv.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        clientRv.setAdapter(listAdpter);
        listAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到收益详情
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(recommendIndex.getList().get(position).getId()));
                bundle.putString("name", recommendIndex.getList().get(position).getShopname());
                CommonUtils.goActivity(mContext, ClientOrderDetailsActivity.class, bundle);
            }
        });
        refreshRequest();
    }

    private void refreshRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mContext, Contants.PortU.RECOMMEND_INDEX, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    recommendIndex = JSONObject.parseObject(response, RecommendIndex.class);
                    setData();
                }
            }
        });
    }

    private void setData() {
        if (recommendIndex == null) return;
        clientNum.setText(String.format("%s个", recommendIndex.getNum()));
        allIncome.setText(String.format("￥%s", recommendIndex.getAllprofit()));
        monthIncome.setText(String.format("￥%s", recommendIndex.getMonthprofit()));
        listAdpter.setList(recommendIndex.getList());
    }

    private void showRuleDialog() {
        new MaterialDialog.Builder(mContext)
                .title("推荐规则")
                .content(recommendIndex.getRules())
                .positiveText("我知道了")
                .build()
                .show();
    }

    @OnClick({R.id.Issued_imag, R.id.notissued_imag, R.id.rule_imag, R.id.finish_tv, R.id.more_tv, R.id.client_imag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Issued_imag:
                //已发放
                CommonUtils.goActivity(mContext, IssuedBonusActivity.class, null);
                break;
            case R.id.notissued_imag:
                //未发放
                CommonUtils.goActivity(mContext, NotIssuedBonusActivity.class, null);
                break;
            case R.id.rule_imag:
                //规则
                if (recommendIndex != null) {
                    showRuleDialog();
                }
                break;
            case R.id.finish_tv:
                finish();
                break;
            case R.id.more_tv:
                //更多
                CommonUtils.goActivity(mContext, NotIssuedBonusActivity.class, null);
                break;
            case R.id.client_imag:
                //客户
                CommonUtils.goActivity(mContext, MyReferralCustomerActivity.class, null);
                break;
        }
    }

}
