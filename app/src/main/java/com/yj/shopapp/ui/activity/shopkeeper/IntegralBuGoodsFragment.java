package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.yj.shopapp.ubeen.ExcGoods;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.IntegraAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.view.ClearEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;


public class IntegralBuGoodsFragment extends BaseActivity implements IntegraAdapter.OnViewClickListener, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.value_Et)
    ClearEditText valueEt;
    private IntegraAdapter adapter;
    private ExcGoods mData;
    private String goodnumber = "";
    private int p = 1;
    private boolean isRequst = false;
    private KProgressHUD kProgressHUD;
    private boolean isSereen;
    private List<ExcGoods.DataBean> dataBeanList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_integral_bu_goods;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());
        kProgressHUD = growProgress(Contants.Progress.LOAD_ING);
        adapter = new IntegraAdapter(mContext, this);
        if (myRecyclerView != null) {
            myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            myRecyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
            myRecyclerView.setAdapter(adapter);
        }
        Refresh();
        valueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataBeanList.clear();
                if (!"".equals(s.toString())) {
                    for (ExcGoods.DataBean bean : mData.getData()) {
                        if (bean.getName().contains(s.toString())) {
                            dataBeanList.add(bean);
                        }
                    }
                    adapter.setList(dataBeanList);
                    isSereen = true;
                } else {
                    isSereen = false;
                    adapter.setList(mData.getData());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (isNetWork(mContext)) {
            getShopList();
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

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.Redeem_now:
                showInputDialog(position);
                break;
            case R.id.onItemclick:
                if (isSereen) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("exgood", dataBeanList.get(position));
                    CommonUtils.goActivity(mContext, ExchangeOfGoodsDetails.class, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("exgood", mData.getData().get(position));
                    CommonUtils.goActivity(mContext, ExchangeOfGoodsDetails.class, bundle);
                }
                break;
        }

    }

    private void showInputDialog(final int position) {
        goodnumber = "";
        new MaterialDialog.Builder(mContext)
                .title("请输入换购数量")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("请输入数量", "", (dialog, input) -> {

                })
                .positiveText("确定")
                .negativeText("取消")
                .onPositive((dialog, which) -> {
                    goodnumber = dialog.getInputEditText().getText().toString();
                    if (!"".equals(goodnumber)) {
                        changeGoods(mData.getData().get(position).getId());
                    }
                })
                .canceledOnTouchOutside(false)
                .show();
    }

    private void changeGoods(String gid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("goods_id", gid);
        params.put("num", goodnumber);
        //ShowLog.e(String.format("%1$s|%2$s|%3$s|%4$s|%5$s",uid,token,gid,goodnumber,site));
        HttpHelper.getInstance().post(mContext, Contants.PortS.CHANGE_GOODS, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                showToastShort(JSONObject.parseObject(json).getString("info"));
            }

            @Override
            public void onAfter() {
                super.onAfter();
                swipeRefreshLayout.autoRefresh();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    private void getShopList() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(p));
        HttpHelper.getInstance().post(this, Contants.PortS.GOODS, params, new OkHttpResponseHandler<String>(this) {
            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh();
                }
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore();
                }
                kProgressHUD.dismiss();
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    JSONObject object = JSONObject.parseObject(response);
                    if (object.getInteger("status") == 0) {
                        if (mData != null) {
                            if (!isSereen) {
                                if (swipeRefreshLayout != null) {
                                    swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            }
                            p--;
                        } else {
                            showToastShort(object.getString("info"));
                        }
                    } else {
                        if (mData == null) {
                            mData = object.toJavaObject(ExcGoods.class);
                        } else {
                            JSONArray jsonArray = object.getJSONArray("data");
                            mData.getData().addAll(jsonArray.toJavaList(ExcGoods.DataBean.class));
                        }
                        if (!isSereen) {
                            adapter.setList(mData.getData());
                        }
                    }

                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh();
                }
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore();
                }
                p--;
            }

        });
    }


    @OnClick({R.id.regit_serach})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.regit_serach:
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        p++;
        getShopList();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (isNetWork(mContext)) {
            p = 1;
            mData = null;
            getShopList();
            swipeRefreshLayout.setNoMoreData(false);
        }
    }
}
