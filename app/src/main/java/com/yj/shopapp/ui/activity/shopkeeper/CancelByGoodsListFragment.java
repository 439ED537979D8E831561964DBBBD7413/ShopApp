package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.CancelByList;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.CancelListAdpter;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

public class CancelByGoodsListFragment extends NewBaseFragment {
    @BindView(R.id.cancelList_rv)
    RecyclerView cancelListRv;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.loading)
    LoadingLayout loading;
    private int CurrPage = 1;
    private CancelByList cancelByList;
    private List<CancelByList.ListsBean> listsBeans = new ArrayList<>();
    private CancelListAdpter adpter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cancel_by_goods_list;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        adpter = new CancelListAdpter(mActivity);
        cancelListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        cancelListRv.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        cancelListRv.setAdapter(adpter);
        Refresh();
    }

    @Override
    protected void initData() {
        if (isNetWork(mActivity)) {
            requestData();
        }

    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setOnRefreshListener(v -> {
            cancelByList = null;
            listsBeans.clear();
            smartRefreshLayout.setNoMoreData(false);
            CurrPage = 1;
            requestData();
        });
        smartRefreshLayout.setOnLoadMoreListener(v -> {
            CurrPage++;
            requestData();
        });
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        //smartRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
    }

    private void requestData() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(CurrPage));
        HttpHelper.getInstance().post(mActivity, Contants.PortS.CANCELLIST, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    JSONObject object = JSONObject.parseObject(response);
                    if (object.getInteger("status") == 1) {
                        listsBeans.clear();
                        cancelByList = object.toJavaObject(CancelByList.class);
                        if (cancelByList != null) {
                            listsBeans.addAll(cancelByList.getLists());
                        }
                        if (loading != null) {
                            loading.showContent();
                        }
                        adpter.setList(listsBeans);
                    } else {
                        if (listsBeans.size() > 0) {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            if (loading != null) {
                                loading.showEmpty();
                            }
                        }
                        CurrPage--;
                        adpter.notifyDataSetChanged();
                    }
                } else {
                    showToast(JSONObject.parseObject(response).getString("info"));
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
            }
        });
    }

}
