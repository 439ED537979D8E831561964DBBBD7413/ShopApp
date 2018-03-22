package com.yj.shopapp.ui.activity.shopkeeper;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.loading.ILoadViewImpl;
import com.yj.shopapp.loading.LoadMoreClickListener;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ui.activity.adapter.WRefundOrderAdapter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.Order;
import com.yj.shopapp.wbeen.Worder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class SRefundorderViewActivity extends BaseFragment implements BaseRecyclerView
{


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.id_drawer_layout)
    LinearLayout idDrawerLayout;

    private ILoadView iLoadView = null;
    private View loadMoreView = null;



    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    private List<Worder> orderList = new ArrayList<>();

    String uid;
    String token;
    String ostatus = "0";

    public static SRefundorderViewActivity newInstance(String content) {
        SRefundorderViewActivity fragment = new SRefundorderViewActivity();
        fragment.ostatus = content;
        return fragment;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        uid = PreferenceUtils.getPrefString(mActivity, Contants.Preference.UID,"");
        token = PreferenceUtils.getPrefString(mActivity,Contants.Preference.TOKEN,"");

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        WRefundOrderAdapter oAdapter = new WRefundOrderAdapter(mActivity,orderList,this);

        layoutManager = new LinearLayoutManager(mActivity);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager,oAdapter);

        iLoadView = new ILoadViewImpl(mActivity, new SRefundorderViewActivity.mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new SRefundorderViewActivity.MyScrollListener());


        if(recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        if (NetUtils.isNetworkConnected(mActivity)) {
            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(true);

                        refreshRequest();
                    }
                }, 200);
            }
        }else{
            showToastShort("网络不给力");
        }
    }


    public void refreshRequest() {
        mCurrentPage = 1;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("ostatus",ostatus);
        adapter.removeFooter(loadMoreView);

        HttpHelper.getInstance().post(mActivity,Contants.PortU.Refundsorders,params,new OkHttpResponseHandler<String>(mActivity){
            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onAfter() {
                super.onAfter();
                swipeRefreshLayout.setRefreshing(false);
                isRequesting = false;
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                orderList.clear();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                System.out.println("response" + json);
                orderList.clear();
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            Worder worder = gson.fromJson(jsonArray.getString(i), Worder.class);
                            orderList.add(worder);
                        }
                    }
                    catch (JSONException e)
                    {

                    }
                    if (orderList.size() == 0) {

                    } else if (orderList.size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    }

                }

                adapter.notifyDataSetChanged();
            }
        });


    }

    public void loadMoreRequest(){

        if(isRequesting)
            return;
        if (orderList.size() < 20) {
            return;
        }

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("ostatus", ostatus);

        HttpHelper.getInstance().post(mActivity, Contants.PortU.Refundsorders, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json)  {

                super.onResponse(request, json);

                System.out.println("response"+json);
                if(JsonHelper.isRequstOK(json,mActivity)){
                    JsonHelper<Order> jsonHelper = new JsonHelper<Order>(Order.class);

                    if(jsonHelper.getDatas(json).size() == 0){
                        iLoadView.showFinishView(loadMoreView);
                    }else {
                        try {
                            JSONArray jsonArray = new JSONArray(json);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                Gson gson = new Gson();
                                Worder worder = gson.fromJson(jsonArray.getString(i), Worder.class);
                                orderList.add(worder);


                            }
                        }
                        catch (JSONException e)
                        {

                        }
                    }

                }else if(JsonHelper.getRequstOK(json)==6){
                    iLoadView.showFinishView(loadMoreView);
                }else{
                    mCurrentPage--;
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                mCurrentPage--;
                iLoadView.showErrorView(loadMoreView);
            }
        });

    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshRequest();
        }
    };

    @Override
    public int getLayoutID() {
        return R.layout.stab_orderview;
    }


    @Override
    public void onItemClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("oid",orderList.get(position).getOid());
        bundle.putInt("isType",1);
        CommonUtils.goActivity(mActivity,SRefundorOrderDetailActivity.class,bundle,false);
    }

    @Override
    public void onLongItemClick(int position) {

    }

    public class mLoadMoreClickListener implements LoadMoreClickListener {

        @Override
        public void clickLoadMoreData() {
            loadMoreRequest();
        }
    }



    public class MyScrollListener extends OnRecyclerViewScrollListener {

        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onBottom() {
            loadMoreRequest();
        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }
}
