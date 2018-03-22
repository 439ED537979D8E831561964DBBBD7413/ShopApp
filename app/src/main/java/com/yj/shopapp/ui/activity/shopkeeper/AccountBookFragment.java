package com.yj.shopapp.ui.activity.shopkeeper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.AccountBook;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.AccountBookAdpter;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.view.headfootrecycleview.RecycleViewEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountBookFragment extends NewBaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecycleViewEmpty recyclerView;
    @BindView(R.id.empty_tv)
    TextView emptyTv;
    @BindView(R.id.Cempty_view)
    NestedScrollView CemptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private AccountBookAdpter accountBookAdpter;
    private List<AccountBook> accountBooks = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account_book;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        accountBookAdpter = new AccountBookAdpter(mActivity);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DDecoration(mActivity));
        recyclerView.setAdapter(accountBookAdpter);

    }

    @Override
    protected void initData() {
        if (NetUtils.isNetworkConnected(mActivity)) {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    requestData();
                }
            }, 300);
        } else {
            showToast("无网络");
        }
    }

    private void requestData() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.UserIntegralDetails, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    accountBooks = JSONArray.parseArray(json, AccountBook.class);
                    accountBookAdpter.setList(accountBooks);
                    ShowLog.e(accountBooks.size() + "");
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    showToast("无数据");
                    recyclerView.setEmptyView(emptyTv);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
            //            @Override
//            public void onAfter() {
//                super.onAfter();
//                isLoad = true;
//                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onResponse(Request request, String json) {
//                super.onResponse(request, json);
//                ShowLog.e(json);
//                if (JsonHelper.isRequstOK(json, mActivity)) {
//                    integralDetails = JSONObject.parseObject(json, IntegralDetail.class);
//                    if (integralDetails.getStatus() == 1) {
//                        adapter.setList(integralDetails.getData());
//                    } else {
//                        showToast(integralDetails.getInfo());
//                        adapter.setmFooterView(FooterView);
//                    }
//                    if (integralDetails.getData().size() == 0) {
//                        recyclerView.setEmptyView(CemptyView);
//                    }
//                } else {
//                    showToast(JsonHelper.errorMsg(json));
//                }
//            }
        });
    }

    @Override
    public void onRefresh() {
        if (NetUtils.isNetworkConnected(mActivity)) {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    requestData();
                }
            }, 300);
        } else {
            showToast("无网络");
        }
    }
}
