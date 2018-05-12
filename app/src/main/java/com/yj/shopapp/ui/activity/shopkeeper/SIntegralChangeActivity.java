package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.IntegralChange;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.IntegralChangeAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtil;
import com.yj.shopapp.view.headfootrecycleview.RecycleViewEmpty;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class SIntegralChangeActivity extends BaseActivity implements OnRefreshListener {
    @BindView(R.id.recycler_view)
    RecycleViewEmpty recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.Cempty_view)
    NestedScrollView CemptyView;
    @BindView(R.id.empty_tv)
    TextView emptyTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private IntegralChange integralChange;
    private IntegralChangeAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sintegral_detail;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Refresh();
        adapter = new IntegralChangeAdapter(mContext);
        emptyTv.setText("暂无兑换记录");
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 30);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNetWork(mContext)) {
            getIntegralChange();
        }
    }

    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        swipeRefreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        swipeRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
    }

    private void getIntegralChange() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortS.CHANGE_HISTORY, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(false);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    integralChange = JSONObject.parseObject(json, IntegralChange.class);
                    if (integralChange.getStatus() == 1) {
                        if (adapter != null) {
                            adapter.setList(integralChange.getData());
                        }
                    } else {
                        showToastShort(integralChange.getInfo());
                        if (recyclerView != null) {
                            recyclerView.setEmptyView(CemptyView);
                        }
                    }
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }
        });

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getIntegralChange();
    }
}
