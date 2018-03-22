package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.IntegralChange;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.IntegralChangeAdapter;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SIntegralChangeActivity extends NewBaseFragment implements ShowDataDialog.OnDataInterface, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.yearBt)
    TextView yearBt;
    @BindView(R.id.monthBt)
    TextView monthBt;
    private String year = "";
    private String month = "";
    private ShowDataDialog dataDialogs;
    private List<IntegralChange> integralChanges = new ArrayList<>();
    private IntegralChangeAdapter oAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sintegral_change;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        oAdapter = new IntegralChangeAdapter(mActivity);
        //设置adapter
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration)));
            recyclerView.setAdapter(oAdapter);
        }
        pullToRefresh.setOnRefreshListener(this);

        dataDialogs = new ShowDataDialog(mActivity, this);
    }

    @Override
    protected void initData() {
        if (NetUtils.isNetworkConnected(mActivity)) {
            if (null != pullToRefresh) {
                pullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefresh.setRefreshing(true);
                        getIntegralChange();
                    }
                }, 200);
            }
        } else {
            showToast("网络不给力");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (integralChanges != null) {
            oAdapter.setList(integralChanges);
        } else {
            if (NetUtils.isNetworkConnected(mActivity)) {
                if (null != pullToRefresh) {
                    pullToRefresh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pullToRefresh.setRefreshing(true);
                            getIntegralChange();
                        }
                    }, 200);
                }
            } else {
                showToast("网络不给力");
            }
        }
    }

    private void getIntegralChange() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        if (year != null) {
            params.put("year", year);
        }
        if (month != null) {
            params.put("month", month);
        }
        HttpHelper.getInstance().post(mActivity, Contants.PortU.UserIntegralChange, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (pullToRefresh != null) {
                    pullToRefresh.setRefreshing(false);
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (pullToRefresh != null) {
                    pullToRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    integralChanges = JSONArray.parseArray(json, IntegralChange.class);
                    oAdapter.setList(integralChanges);
                } else {
                    showToast(JSONObject.parseObject(json).getString("info"));
                }
            }
        });
    }


    @OnClick({R.id.yearBt, R.id.monthBt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.yearBt:
                dataDialogs.showDataDialog(0);
                break;
            case R.id.monthBt:
                dataDialogs.showDataDialog(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void setYead(String yead) {

        year = yead.substring(0, yead.length() - 1);
        yearBt.setText(yead);
        pullToRefresh.setRefreshing(true);
        getIntegralChange();

    }

    @Override
    public void setMonths(String yead, String months) {

        yearBt.setText(yead);
        monthBt.setText(months);
        year = yead.substring(0, yead.length() - 1);
        month = months.substring(0, months.length() - 1);
        pullToRefresh.setRefreshing(true);
        getIntegralChange();

    }

    @Override
    public void setQuarter(String yead, String quarter) {

    }
//    @Override
//    public void setDatas(int yead, int months, int datas) {
//
//    }

    @Override
    public void onRefresh() {
        getIntegralChange();
    }
}
