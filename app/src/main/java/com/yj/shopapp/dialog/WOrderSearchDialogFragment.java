package com.yj.shopapp.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.WOrderAdapter;
import com.yj.shopapp.ui.activity.shopkeeper.SOrderDetailActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.wbeen.WNewOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by LK on 2018/5/22.
 *
 * @author LK
 */
public class WOrderSearchDialogFragment extends BaseV4DialogFragment {
    @BindView(R.id.finish_tv)
    ImageView finishTv;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    private List<WNewOrder> newOrders = new ArrayList<>();
    private WOrderAdapter oAdapter;
    private int mCurrPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.wsearchpwd_view;
    }

    @Override
    protected void initData() {
        oAdapter = new WOrderAdapter(mActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        recyclerView.setAdapter(oAdapter);
        oAdapter.setOnItemClickListener((parent, view1, position, id) -> {
            //跳转到详情
            Bundle bundle = new Bundle();
            bundle.putString("oid", newOrders.get(position).getId());
            CommonUtils.goActivity(mActivity, SOrderDetailActivity.class, bundle);
        });
        loading.showContent();
        showKeyBoard(valueEt);
        Refresh();
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnLoadMoreListener((v) -> {
            mCurrPage++;
            refreshRequest(valueEt.getText().toString());
        });
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    public void refreshRequest(String keyword) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrPage));
        params.put("keyword", keyword);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.MYORDERS, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (loading != null) {
                    loading.showContent();
                }
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                }
                if (json.equals("[]")) {
                    mCurrPage--;
                }
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    newOrders.addAll(JSONArray.parseArray(json, WNewOrder.class));
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
            }
        });

    }

    @OnClick({R.id.finish_tv, R.id.search2Btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish_tv:
                dismiss();
                break;
            case R.id.search2Btn:
                hideImm(valueEt);
                newOrders.clear();
                refreshRequest(valueEt.getText().toString());
                if (loading != null) {
                    loading.showLoading();
                }
                break;
        }
    }

}
