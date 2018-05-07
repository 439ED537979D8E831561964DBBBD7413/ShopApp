package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.adapter.NewsAdapter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.wbeen.Msgs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by jm on 2016/5/16.
 */
public class WNewListViewActivity extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private boolean isRequesting = false;//标记，是否正在刷新或者加载
    private int mCurrentPage = 0;
    private List<Msgs> megsList = new ArrayList<>();
    String mtype = "0";
    private NewsAdapter nAdapter;

    public static WNewListViewActivity newInstance(String content) {
        WNewListViewActivity fragment = new WNewListViewActivity();
        fragment.mtype = content;
        return fragment;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        Refresh();
        nAdapter = new NewsAdapter(mActivity, megsList);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration4)));
            recyclerView.setAdapter(nAdapter);
        }

        if (NetUtils.isNetworkConnected(mActivity)) {
            refreshRequest();
        } else {
            showToastShort("网络不给力");
        }
        nAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("id", megsList.get(position).getId());
                CommonUtils.goActivity(mActivity, WMyNewsDetailActivity.class, bundle, false);
                megsList.get(position).setStatus("0");
            }
        });
    }

    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadMoreListener(this);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    public void refreshRequest() {

        mCurrentPage = 1;

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("mtype", mtype);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.Msgs, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh();
                }
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                megsList.clear();
                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JsonHelper<Msgs> jsonHelper = new JsonHelper<Msgs>(Msgs.class);
                    megsList.addAll(jsonHelper.getDatas(json));
                }
                nAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                megsList.clear();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(false);
                }
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
        params.put("mtype", mtype);

        HttpHelper.getInstance().post(mActivity, Contants.PortA.Msgs, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequesting = false;
                if (swipeRefreshLayout!=null){
                    swipeRefreshLayout.finishLoadMore();
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

                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JsonHelper<Msgs> jsonHelper = new JsonHelper<Msgs>(Msgs.class);
                    if (jsonHelper.getDatas(json).size() == 0) {

                    } else {
                        megsList.addAll(jsonHelper.getDatas(json));
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }
                nAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                mCurrentPage--;
                if (swipeRefreshLayout!=null){
                    swipeRefreshLayout.finishLoadMore(false);
                }
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadMoreRequest();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshRequest();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_newslist;
    }
}
