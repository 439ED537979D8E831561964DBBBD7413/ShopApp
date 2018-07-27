package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.WOrderAdapter;
import com.yj.shopapp.ui.activity.shopkeeper.SOrderDetailActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.wbeen.WNewOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by jm on 2016/4/25.
 */
public class WOrderViewActivity extends NewBaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isRequesting = false;//标记，是否正在刷新
    private int mCurrentPage = 0;
    //private List<Worder> orderList = new ArrayList<>();
    private List<WNewOrder> newOrders = new ArrayList<>();
    private String ostatus = "0";
    private WOrderAdapter oAdapter;

    public static WOrderViewActivity newInstance(String content) {
        Bundle args = new Bundle();
        WOrderViewActivity fragment = new WOrderViewActivity();
        args.putString("content", content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wtab_orderview;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ostatus = Objects.requireNonNull(getArguments()).getString("content");
        oAdapter = new WOrderAdapter(mActivity);
        layoutManager = new LinearLayoutManager(mActivity);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration3)));
            recyclerView.setAdapter(oAdapter);
            oAdapter.setOnItemClickListener((parent, view1, position, id) -> {
                //跳转到详情
                Bundle bundle = new Bundle();
                bundle.putString("oid", newOrders.get(position).getId());
                CommonUtils.goActivity(mActivity, SOrderDetailActivity.class, bundle);
            });
        }
        Refresh();
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setOnRefreshListener((v) -> {
            if (smartRefreshLayout != null) {
                smartRefreshLayout.setNoMoreData(false);
            }
            refreshRequest();
        });
        smartRefreshLayout.setOnLoadMoreListener((v) -> loadMoreRequest());
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @Override
    protected void initData() {
        if (NetUtils.isNetworkConnected(mActivity)) {
            refreshRequest();
        }
    }


    public void refreshRequest() {
        mCurrentPage = 1;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("ostatus", ostatus);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.MYORDERS, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequesting = false;
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (loading != null) {
                    loading.showContent();
                }
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    newOrders = JSONArray.parseArray(json, WNewOrder.class);
                    oAdapter.setList(newOrders);
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    if (loading != null) {
                        loading.showEmpty();
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });

    }

    public void loadMoreRequest() {
        if (isRequesting)
            return;
        mCurrentPage++;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("ostatus", ostatus);
        //ShowLog.e("token" + token + "uid" + uid + "mCurrentPage" + mCurrentPage + "ostatus" + ostatus);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.MYORDERS, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequesting = false;
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    newOrders.addAll(JSONArray.parseArray(json, WNewOrder.class));
                    oAdapter.setList(newOrders);
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    if (null != smartRefreshLayout) {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    mCurrentPage--;
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                mCurrentPage--;
            }
        });
    }

}
