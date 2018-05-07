package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.BuGood;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.NoticeAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class EverybodyChangeActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int p = 1;
    private List<BuGood> bugoodList = new ArrayList<>();
    private NoticeAdpter adpter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_everybody_change;
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
        adpter = new NoticeAdpter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        recyclerView.setAdapter(adpter);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 50);
    }

    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadMoreListener(this);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        swipeRefreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        swipeRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetWork(mContext)) {
            requestData();
        }
    }

    private void requestData() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(p));
        HttpHelper.getInstance().post(this, Contants.PortS.EVERYBODY_CHANGING, params, new OkHttpResponseHandler<String>(this) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    if ('{' == response.charAt(0)) {
                        p--;
                        if (bugoodList.size() > 0) {
                            if (bugoodList.size() > 8) {
                                if (swipeRefreshLayout != null) {
                                    swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            }
                        } else {
                            showToastShort(JSONObject.parseObject(response).getString("info"));
                        }
                    } else {
                        bugoodList.addAll(JSONArray.parseArray(response, BuGood.class));
                        adpter.setList(bugoodList);
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                p--;
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(false);
                }
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore(false);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh();
                }
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore();
                }
            }

        });
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        p++;
        requestData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        p = 1;
        bugoodList.clear();
        requestData();
        swipeRefreshLayout.setNoMoreData(false);
    }
}
