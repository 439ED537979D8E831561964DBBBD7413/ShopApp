package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ServiceOrder;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ValueAddedServiceAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.view.headfootrecycleview.RecycleViewEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by LK on 2018/1/15.
 *
 * @author LK
 */

public class ValueAddedService extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycler_view)
    RecycleViewEmpty recyclerView;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.empty_tv)
    TextView emptyTv;
    @BindView(R.id.Cempty_view)
    NestedScrollView CemptyView;
    private ValueAddedServiceAdpter addedServiceAdpter;
    private List<ServiceOrder> mdata = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_valueaddedservice;
    }

    @Override
    protected void initData() {
        title.setText("增值服务订单");
        addedServiceAdpter = new ValueAddedServiceAdpter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(addedServiceAdpter);
        addedServiceAdpter.setOnItemClickListener(this);
        pullToRefresh.setOnRefreshListener(this);
        if (NetUtils.isNetworkConnected(mContext)) {
            pullToRefresh.setRefreshing(true);
            onRefresh();
        } else {
            showToastShort("无网络");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mdata.size() > 0) {
            addedServiceAdpter.setList(mdata);
        } else {
            if (NetUtils.isNetworkConnected(mContext)) {
                pullToRefresh.setRefreshing(true);
                onRefresh();
            } else {
                showToastShort("无网络");
            }
        }
    }

    private void RequstData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.SHOP_ORDER_LIST, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onAfter() {
                super.onAfter();
                pullToRefresh.setRefreshing(false);
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (!"[]".equals(json)) {
                    mdata = JSONArray.parseArray(json, ServiceOrder.class);
                    addedServiceAdpter.setList(mdata);
                }else {
                    recyclerView.setEmptyView(CemptyView);
                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", mdata.get(position));
        CommonUtils.goActivity(mContext, AddedServiceData.class, bundle);
    }

    @Override
    public void onRefresh() {
        RequstData();
    }


}
