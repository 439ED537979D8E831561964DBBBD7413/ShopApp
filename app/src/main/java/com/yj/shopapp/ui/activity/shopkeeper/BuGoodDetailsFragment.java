package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.BuGoodDetailsAdpter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

public class BuGoodDetailsFragment extends NewBaseFragment implements OnRefreshListener, OnLoadMoreListener, OnItemChildViewOnClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.loading)
    LoadingLayout loading;
    private int type;
    private int p = 1;
    private List<LimitedSale> limitedSaleList = new ArrayList<>();
    private BuGoodDetailsAdpter adpter;
    private String addressId = "";
    private KProgressHUD kProgressHUD;

    public static BuGoodDetailsFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        BuGoodDetailsFragment fragment = new BuGoodDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bu_good_details;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        swipeRefreshLayout.setOnRefreshListener(this);
        adpter = new BuGoodDetailsAdpter(mActivity);
        adpter.setListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(adpter);
        Refresh();
    }

    @Override
    protected void initData() {
        type = getArguments().getInt("type", -1);
        addressId = PreferenceUtils.getPrefString(mActivity, "addressId", "");
        kProgressHUD = growProgress("正在抢购中");
        if (isNetWork(mActivity)) {
            limitedSaleList.clear();
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
    }

    private void requestDeta() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(p));
        params.put("status", String.valueOf(type));
        HttpHelper.getInstance().post(mActivity, Contants.PortS.LISTS, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(false);
                    swipeRefreshLayout.finishLoadMore(false);
                }
                p--;
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

                if (response.equals("[]")) {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                    p--;
                    if (limitedSaleList.size() > 0) {
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    } else {
                        if (loading != null) {
                            loading.showEmpty();
                        }

                    }
                } else {
                    limitedSaleList.addAll(JSONArray.parseArray(response, LimitedSale.class));
                    if (loading != null) {
                        loading.showContent();
                    }
                    adpter.setList(limitedSaleList);
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adpter.closeCountDownTimers();
    }

    private void submitData(final int position, String id) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", id);
        params.put("addressid", addressId != null ? addressId : "");

        HttpHelper.getInstance().post(mActivity, Contants.PortS.DO_SALES, params, new OkHttpResponseHandler<String>(mActivity) {
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
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    JSONObject object = JSONObject.parseObject(response);
                    if (object.getInteger("status") == 1) {
                        Bundle b = new Bundle();
                        b.putParcelable("LimitedSale", limitedSaleList.get(position));
                        CommonUtils.goActivity(mActivity, BuShopDetailsAcitivity.class, b);
                        showToast("恭喜你，抢购成功!");
                    } else {
                        showToast(object.getString("info"));
                    }
                }

            }

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
        });
    }

    @Override
    public void onChildViewClickListener(View view, int position) {
        switch (view.getId()) {
            case R.id.gorob:
                if (addressId.equals("")) {
                    showToast("暂无收货地址");
                } else {
                    if (limitedSaleList.get(position).getIs_sale() == 1) {
                        showToast("此商品您已经抢购过了!");
                    } else {
                        submitData(position, limitedSaleList.get(position).getId());
                    }

                }
                break;
            case R.id.ceshi:
                Bundle bundle = new Bundle();
                bundle.putString("sid", limitedSaleList.get(position).getId());
                CommonUtils.goActivity(mActivity, BuGoodInFoActivity.class, bundle);
                break;
            case R.id.CountdownTv:
                showToast("此商品暂未开抢");
                break;
            case R.id.itemview:
                Bundle bundle1 = new Bundle();
                bundle1.putString("sid", limitedSaleList.get(position).getId());
                bundle1.putInt("type", type);
                CommonUtils.goActivity(mActivity, BuGoodShopDatailsActivity.class, bundle1);
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        p++;
        requestDeta();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        p = 1;
        limitedSaleList.clear();
        requestDeta();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setNoMoreData(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loading = null;
    }
}
