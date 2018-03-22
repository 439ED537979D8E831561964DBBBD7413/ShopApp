package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.RecordRedPack;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.DeatilsRecyAdapter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.view.headfootrecycleview.RecycleViewEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class myRedPack extends BaseFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    List<RecordRedPack> list = new ArrayList<>();
    @BindView(R.id.details_recycler)
    RecycleViewEmpty detailsRecycler;
    @BindView(R.id.empty_view)
    TextView emptyView;

    private DeatilsRecyAdapter adapter;
    private LinearLayoutManager manager;

    @Override
    public void init(Bundle savedInstanceState) {
        emptyView.setText("暂无领取记录\r\n\r\n前往我的推荐查看有没有未领取的红包");
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);
        detailsRecycler.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));
        manager = new LinearLayoutManager(mActivity);
        adapter = new DeatilsRecyAdapter(mActivity, list);
        detailsRecycler.setEmptyView(emptyView);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转进入红包详情
                Bundle bundle = new Bundle();
                bundle.putParcelable("RecordRedPack", list.get(0));
                CommonUtils.goActivity(mActivity, BillDetails.class, bundle, false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetUtils.isNetworkConnected(mActivity)) {
            if (null != swipeRefreshLayout) {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fresh();
                    }
                }, 200);
            }
        } else {
            showToastShort("网络不给力");
        }
    }

    public void fresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshRequest();
    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshRequest();
        }
    };

    @Override
    public int getLayoutID() {
        return R.layout.fragment_my_redpack;
    }

    /**
     * 网络请求
     */
    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.MYREWARDLIST, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onAfter() {
                super.onAfter();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    list = getRedList(json);
                }
                if (detailsRecycler != null) {
                    detailsRecycler.setLayoutManager(manager);
                    detailsRecycler.setAdapter(adapter);
                    adapter.setList(list);
                }
            }
        });
    }

    private List<RecordRedPack> getRedList(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<RecordRedPack>>() {
        }.getType());
    }

}
