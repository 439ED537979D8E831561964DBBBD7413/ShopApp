package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.SalesInfo;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SalesInfoAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class BuGoodInFoActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    private String goodId = "";
    private int p = 1;
    private List<SalesInfo> salesInfos = new ArrayList<>();
    private SalesInfoAdpter adpter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bu_good_in_fo;
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
        if (getIntent().hasExtra("sid")) {
            goodId = getIntent().getStringExtra("sid");
        }
        rightTv.setVisibility(View.GONE);
        contentTv.setText("抢购信息");
        Refresh();
        adpter = new SalesInfoAdpter(mContext);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        myRecyclerView.setAdapter(adpter);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 50);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetWork(mContext)) {
            requestDeta();
        }
    }

    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadMoreListener(this);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        swipeRefreshLayout.setEnableFooterFollowWhenLoadFinished(true);
    }

    private void requestDeta() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(p));
        params.put("id", goodId);
        HttpHelper.getInstance().post(mContext, Contants.PortS.SALES_INFO, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(false);
                }
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore(false);
                }
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    if (!"[]".equals(response)) {
                        salesInfos.addAll(JSONArray.parseArray(response, SalesInfo.class));
                        adpter.setList(salesInfos);
                    } else {
                        if (salesInfos.size() > 0) {
                            if (salesInfos.size() > 5) {
                                if (swipeRefreshLayout != null) {
                                    swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            }
                        } else {
                            if (swipeRefreshLayout != null) {
                                swipeRefreshLayout.finishRefresh(false);
                            }
                            showToastShort("还未有抢购信息");
                        }

                    }
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(true);
                }
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore(true);
                }
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        p++;
        requestDeta();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        p = 1;
        salesInfos.clear();
        requestDeta();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setNoMoreData(false);
        }
    }
}
