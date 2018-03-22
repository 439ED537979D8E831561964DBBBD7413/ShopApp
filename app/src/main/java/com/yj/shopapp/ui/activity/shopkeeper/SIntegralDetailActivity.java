package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.IntegralDetail;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.IntegralAdapter;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.view.headfootrecycleview.RecycleViewEmpty;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class SIntegralDetailActivity extends NewBaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecycleViewEmpty recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.Cempty_view)
    NestedScrollView CemptyView;
    private IntegralDetail integralDetails;
    private IntegralAdapter adapter;
    private View FooterView;
    private boolean isLoad = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sintegral_detail;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new IntegralAdapter(mActivity);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerView.setAdapter(adapter);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        FooterView = LayoutInflater.from(mActivity).inflate(R.layout.footerview, null);
        FooterView.setLayoutParams(lp);
    }

    @Override
    protected void initData() {
        if (NetUtils.isNetworkConnected(mActivity)) {
            if (null != swipeRefreshLayout) {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        getIntegralDetail();
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
        if (isLoad) {
            if (integralDetails != null) {
                adapter.setList(integralDetails.getData());
            } else {
                if (NetUtils.isNetworkConnected(mActivity)) {
                    if (null != swipeRefreshLayout) {
                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(true);
                                getIntegralDetail();
                            }
                        }, 200);
                    }
                } else {
                    showToast("网络不给力");
                }
            }
        }
    }

    private void getIntegralDetail() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.CHANGE_HISTORY, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                isLoad = true;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    integralDetails = JSONObject.parseObject(json, IntegralDetail.class);
                    if (integralDetails.getStatus() == 1) {
                        adapter.setList(integralDetails.getData());
                    } else {
                        showToast(integralDetails.getInfo());
                        adapter.setmFooterView(FooterView);
                    }
                    if (integralDetails.getData().size() == 0) {
                        recyclerView.setEmptyView(CemptyView);
                    }
                } else {
                    showToast(JsonHelper.errorMsg(json));
                }
            }
        });

    }


    @Override
    public void onRefresh() {
        getIntegralDetail();
    }

}
