package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.EventMassg;
import com.yj.shopapp.ubeen.NewOrder;
import com.yj.shopapp.ubeen.RefreshListCar;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.Interface.OnViewScrollListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SNewOrderAdpter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.view.headfootrecycleview.RecycleViewEmpty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by jm on 2016/4/25.
 */
public class SOrderViewActivity extends NewBaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.recycler_view)
    RecycleViewEmpty recyclerView;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.Cempty_view)
    NestedScrollView CemptyView;
    private SNewOrderAdpter adapter;
    //标记，是否正在刷新
    private boolean isRefresh = false;
    private int mCurrentPage = 1;
    private List<NewOrder> orderList = new ArrayList<>();
    private View FooterView;
    private String keyword = "";

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
            recyclerView.setAdapter(adapter);
            recyclerView.addOnScrollListener(new OnViewScrollListenter() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int topRowVerticalPosition =
                            (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                    pullToRefresh.setEnabled(topRowVerticalPosition >= 0);
                }

                @Override
                public void onBottom() {
                    mCurrentPage++;
                    refreshRequest();
                }
            });
        }
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("oid", orderList.get(position).getOid());
                CommonUtils.goActivity(mActivity, SOrderDatesActivity.class, bundle);
            }
        });
        pullToRefresh.setOnRefreshListener(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        FooterView = LayoutInflater.from(mActivity).inflate(R.layout.footerview, null);
        FooterView.setLayoutParams(lp);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        if (orderList.size() > 0) {
            adapter.setList(orderList);
        } else {
            if (NetUtils.isNetworkConnected(mActivity)) {
                pullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefresh.setRefreshing(true);
                        refreshRequest();
                    }
                }, 200);
            } else {
                showToast("无网络");
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshListCar car) {
        if (NetUtils.isNetworkConnected(mActivity)) {
            pullToRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullToRefresh.setRefreshing(true);
                    onRefresh();
                }
            }, 200);
        } else {
            showToast("无网络");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMesage(EventMassg msg) {
        keyword = msg.getMessage();
        if (NetUtils.isNetworkConnected(mActivity)) {
            pullToRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullToRefresh.setRefreshing(true);
                    onRefresh();
                }
            }, 200);
        } else {
            showToast("无网络");
        }
    }


    public void refreshRequest() {
        if (isRefresh) return;
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
                isRefresh = false;
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                if (pullToRefresh != null) {
                    pullToRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    orderList.addAll(JSONArray.parseArray(json, NewOrder.class));
                    adapter.setList(orderList);
                }
                if (JsonHelper.getRequstOK(json) == 6) {
                    if (orderList.size() == 0) {
                        recyclerView.setEmptyView(CemptyView);
                    } else {
                        if (orderList.size() > 6) {
                            adapter.setmFooterView(FooterView);
                        }
                    }
                    mCurrentPage--;
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRefresh = true;
            }

            @Override
            public void onAfter() {
                super.onAfter();
                isRefresh = false;
                if (pullToRefresh != null) {
                    pullToRefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        keyword = "";
        orderList.clear();
        refreshRequest();
    }

}
