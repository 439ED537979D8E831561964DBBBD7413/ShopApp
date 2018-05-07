package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

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
import com.yj.shopapp.ubeen.NewOrder;
import com.yj.shopapp.ubeen.RefreshListCar;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SNewOrderAdpter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by jm on 2016/4/25.
 */
public class SOrderViewActivity extends NewBaseFragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pullToRefresh)
    SmartRefreshLayout pullToRefresh;
    @BindView(R.id.loading)
    LoadingLayout loading;
    private SNewOrderAdpter adapter;
    private int mCurrentPage = 1;
    private List<NewOrder> orderList = new ArrayList<>();
    private String keyword = "";
    private boolean isFirst = true;

    public static SOrderViewActivity newInstance(int type) {
        Bundle args = new Bundle();
        SOrderViewActivity fragment = new SOrderViewActivity();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    private int gettype() {
        return getArguments().getInt("type");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.stab_orderview;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        adapter = new SNewOrderAdpter(mActivity, orderList);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration3)));
            recyclerView.setAdapter(adapter);
            //recyclerView.setEmptyView(CemptyView);
        }
        try {
            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (orderList.size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("oid", orderList.get(position).getOid());
                        CommonUtils.goActivity(mActivity, SOrderDatesActivity.class, bundle);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Refresh();
    }

    private void Refresh() {
        pullToRefresh.setHeaderHeight(50);
        pullToRefresh.setFooterHeight(50);
        pullToRefresh.setOnRefreshListener(this);
        pullToRefresh.setOnLoadMoreListener(this);
        pullToRefresh.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        pullToRefresh.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        if (isNetWork(mActivity)) {
            mCurrentPage = 1;
            orderList.clear();
            refreshRequest(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshListCar car) {
        switch (car.getStatus()) {
            case 1:
                if (isNetWork(mActivity)) {
                    orderList.clear();
                    keyword = car.getKw();
                    if (loading != null) {
                        loading.showLoading();
                    }
                    refreshRequest(0);
                }
                break;
            default:
                if (!isFirst && gettype() == 0) {
                    if (isNetWork(mActivity)) {
                        orderList.clear();
                        refreshRequest(0);
                    }
                }
                break;
        }

    }

    public void refreshRequest(final int status) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("keyword", keyword);
        params.put("ostatus", gettype() + "");
        HttpHelper.getInstance().post(mActivity, Contants.PortU.MYORDER, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                mCurrentPage--;
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                if (pullToRefresh != null) {
                    pullToRefresh.finishLoadMore(false);
                }
                if (pullToRefresh != null) {
                    pullToRefresh.finishRefresh(false);
                }

            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    if (status == 0) {
                        orderList.clear();
                        orderList.addAll(JSONArray.parseArray(json, NewOrder.class));
                        adapter.setList(orderList);
                    } else {
                        orderList.addAll(JSONArray.parseArray(json, NewOrder.class));
                        adapter.setList(orderList);
                    }
                    if (loading != null) {
                        loading.showContent();
                    }
                }
                if (JsonHelper.getRequstOK(json) == 6) {
                    if (orderList.size() == 0) {
                        if (pullToRefresh != null) {
                            pullToRefresh.finishRefresh();
                            pullToRefresh.finishLoadMoreWithNoMoreData();
                        }
                        if (loading != null) {
                            loading.showEmpty();
                        }
                    } else {
                        if (pullToRefresh != null) {
                            pullToRefresh.finishLoadMoreWithNoMoreData();
                        }
                    }
                    mCurrentPage--;
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (pullToRefresh != null) {
                    pullToRefresh.finishLoadMore();
                    pullToRefresh.finishRefresh();
                }
                isFirst = false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage++;
        refreshRequest(1);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage = 1;
        keyword = "";
        orderList.clear();
        refreshRequest(0);
        if (pullToRefresh != null) {
            pullToRefresh.setNoMoreData(false);
        }
    }
}
