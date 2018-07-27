package com.yj.shopapp.ui.activity.shopkeeper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.gavin.com.library.StickyDecoration;
import com.gavin.com.library.listener.GroupListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.LimitedSale;
import com.yj.shopapp.ubeen.MyBuGood;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.DisplayUtil;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

public class MyBuGoodFragment extends NewBaseFragment implements OnRefreshListener, OnLoadMoreListener, OnItemChildViewOnClickListener {
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.loading)
    LoadingLayout loading;
    private MyItemRecyclerViewAdapter adapter;
    private MyBuGood myBuGood;
    private String addressId = "";
    private int p = 1;
    private KProgressHUD kProgressHUD;
    private List<MyBuGood.ListsBean> listsBeans = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_item_list;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new MyItemRecyclerViewAdapter(mActivity, this);
        Refresh();
        StickyDecoration decoration = StickyDecoration.Builder
                .init(new GroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        //组名回调
                        //获取组名，用于判断是否是同一组
                        if (position < listsBeans.size()) {
                            return DateUtils.timet(listsBeans.get(position).getAddtime(), "yyyy年MM月dd日");
                        }
                        return "";
                    }
                })
                .setGroupBackground(Color.parseColor("#f1f1f1"))
                .setGroupHeight(CommonUtils.dip2px(mActivity, 36))
                .setDivideColor(Color.parseColor("#f5f5f5"))
                .setDivideHeight(CommonUtils.dip2px(mActivity, 8))
                .setGroupTextColor(Color.parseColor("#000000"))
                .setGroupTextSize(DisplayUtil.sp2px(mActivity, 13))
                .setTextSideMargin(CommonUtils.dip2px(mActivity, 10))
                .build();
        myRecyclerView.addItemDecoration(decoration);
        myRecyclerView.setAdapter(adapter);
        addressId = PreferenceUtils.getPrefString(mActivity, "addressId", "");
        kProgressHUD = growProgress("正在取消");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        if (isNetWork(mActivity)) {
            myBuGood = null;
            listsBeans.clear();
            p = 1;
            requestData();
        }
    }

    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadMoreListener(this);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    private void requestData() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(p));
        HttpHelper.getInstance().post(mActivity, Contants.PortS.MYSALES, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                p--;
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(false);
                    swipeRefreshLayout.finishLoadMore(false);
                }

            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(true);
                    swipeRefreshLayout.finishLoadMore(true);
                }
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                adapter.closeCountDownTimers();
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    JSONObject object = JSONObject.parseObject(response);
                    if (object.getInteger("status") == 1) {
                        listsBeans.clear();
                        myBuGood = object.toJavaObject(MyBuGood.class);
                        if (myBuGood != null) {
                            listsBeans.addAll(myBuGood.getLists());
                        }
                        if (loading != null) {
                            loading.showContent();
                        }
                        adapter.setList(listsBeans);
                    } else {
                        if (listsBeans.size() > 0) {
                            swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            if (loading != null) {
                                loading.showEmpty();
                            }
                        }
                        p--;
                        //showToast(object.getString("info"));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LimitedSale sale) {
        if (sale.getId().equals("3")) {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.autoRefresh();
            }
        }
    }

    @Override
    public void onChildViewClickListener(View view, int position) {
        switch (view.getId()) {
            case R.id.itemview:
                Bundle bundle = new Bundle();
                bundle.putParcelable("mybugood", listsBeans.get(position));
                bundle.putString("isRequest", "false");
                CommonUtils.goActivity(mActivity, BuShopDetailsAcitivity.class, bundle);
                break;
            case R.id.cancelButton:
                cancelOrder(position);
                break;
        }
    }

    private void cancelOrder(final int position) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", listsBeans.get(position).getId());
        params.put("addressid", addressId);
        HttpHelper.getInstance().post(mActivity, Contants.PortS.CANCEL_SALES, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (kProgressHUD != null) {
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
                if (kProgressHUD != null) {
                    kProgressHUD.show();
                }
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                JSONObject object = JSONObject.parseObject(response);
                if (object.getInteger("status") == 1) {
                    adapter.closeCountDownTimers();
                    //刷新
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.autoRefresh();
                    }
                    EventBus.getDefault().post(new LimitedSale("2"));
                } else {
                    showToast(JSONObject.parseObject(response).getString("info"));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter.closeCountDownTimers();
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        p++;
        requestData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        p = 1;
        myBuGood = null;
        listsBeans.clear();
        requestData();
        swipeRefreshLayout.setNoMoreData(false);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        loading = null;
    }
}
