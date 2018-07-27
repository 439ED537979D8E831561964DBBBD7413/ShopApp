package com.yj.shopapp.ui.activity.wholesale;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONArray;
import com.gavin.com.library.StickyDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SalesAdapter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.DisplayUtil;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.wbeen.MessgEvt;
import com.yj.shopapp.wbeen.SPlist;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by jm on 2016/4/25.
 * 促销
 */
public class WSalesViewActivity extends NewBaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    private int mCurrentPage = 0;
    private List<SPlist> sPlistList = new ArrayList<>();
    private String cid;
    private String statusid = "0";
    private String keyword = "";
    private SalesAdapter sAdapter;
    private int status_sales;
    private String saleStatus;

    public static WSalesViewActivity newInstance(String cid, int status) {

        Bundle args = new Bundle();
        args.putString("cid", cid);
        args.putInt("status", status);
        WSalesViewActivity fragment = new WSalesViewActivity();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_salesview;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        cid = Objects.requireNonNull(getArguments()).getString("cid");
        status_sales = getArguments().getInt("status");
        sAdapter = new SalesAdapter(mActivity);
        StickyDecoration decoration = StickyDecoration.Builder
                .init(position -> {
                    //组名回调
                    //获取组名，用于判断是否是同一组
                    if (sPlistList.size() > 0) {
                        return DateUtils.timet(sPlistList.get(position).getAddtime(), "yyyy-MM-dd");
                    }
                    return "";
                })
                .setGroupBackground(Color.parseColor("#f4f5f9"))
                .setGroupHeight(CommonUtils.dip2px(mActivity, 34))
                .setDivideColor(Color.parseColor("#E3E3E3"))
                .setGroupTextColor(Color.parseColor("#000000"))
                .setDivideHeight(1)
                .setGroupTextSize(DisplayUtil.sp2px(mActivity, 14))
                .setTextSideMargin(CommonUtils.dip2px(mActivity, 14))
                .build();
        recyclerView.addItemDecoration(decoration);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerView.setAdapter(sAdapter);
        }
        sAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("itemid", "");
                bundle.putParcelable("been", sPlistList.get(position));
                bundle.putString("goodsname", sPlistList.get(position).getName());
                bundle.putString("isModify", String.valueOf(status_sales));
                CommonUtils.goActivity(mActivity, WSalesDetailActivity.class, bundle);
            }
        });
        Refresh();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        if (NetUtils.isNetworkConnected(mActivity)) {
            refreshRequest();
        } else {
            showToast("网络不给力");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessgEvt messgEvt) {
        switch (messgEvt.getStatus()) {
            case 1:
                statusid = messgEvt.getValue();
                refreshRequest();
                break;
            case 2:
                saleStatus = messgEvt.getValue();
                refreshRequest();
                break;
        }
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setOnRefreshListener((v) -> {
            refreshRequest();
            if (null != smartRefreshLayout) {
                smartRefreshLayout.setNoMoreData(false);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener((v) -> loadMoreRequest());
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }


    public void refreshRequest() {
        mCurrentPage = 1;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("cid", cid);
        params.put("keyword", keyword);
        params.put("status", statusid);
        params.put("sale_status", saleStatus);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.SALELIST, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
                sPlistList.clear();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.setNoMoreData(false);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (loading != null) {
                    loading.showContent();
                }
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    sPlistList.addAll(JSONArray.parseArray(json, SPlist.class));
                    sAdapter.setList(sPlistList);
                    if (sPlistList.size() == 0) {
                        if (loading != null) {
                            loading.showEmpty();
                        }
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                sPlistList.clear();
            }
        });

    }

    public void loadMoreRequest() {
        mCurrentPage++;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("status", statusid);
        params.put("cid", cid);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.SALELIST, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();

            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    sPlistList.addAll(JSONArray.parseArray(json, SPlist.class));
                    sAdapter.setList(sPlistList);
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    mCurrentPage--;
                    if (smartRefreshLayout != null) {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
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

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
