package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.loading.ILoadViewImpl;
import com.yj.shopapp.loading.LoadMoreClickListener;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ui.activity.adapter.CustomerAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by huanghao on 2016/11/26.
 */

public class WCashCousponCustomerActivity extends BaseActivity implements BaseRecyclerView {


    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.id_drawer_layout)
    FrameLayout idDrawerLayout;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewHeaderFooterAdapter adapter;

    private boolean isRequesting = false;//标记，是否正在刷新

    List<Customer> agentsList = new ArrayList<>();

    @Override
    public void onItemClick(int position) {

        Bundle bundle=new Bundle();
        bundle.putString("customerId",agentsList.get(position).getUseruid());
        String shopname = agentsList.get(position).getShopname() ;
        String username = agentsList.get(position).getUsername() ;
        if (shopname==null||shopname.equals("")){
            bundle.putString("customerName",username);
        }else {
            bundle.putString("customerName",shopname);
        }
        CommonUtils.goResult(mContext, bundle, WAddCashCouponActivity.AddCASHREPUESTCODE);
    }

    @Override
    public void onLongItemClick(int position) {

    }



    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_cashcousponcutomer;
    }

    @Override
    protected void initData() {
        title.setText("我的零售商");


        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        CustomerAdapter sAdapter = new CustomerAdapter(mContext, agentsList, this);

        layoutManager = new LinearLayoutManager(mContext);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, sAdapter);

        iLoadView = new ILoadViewImpl(mContext, new mLoadMoreClickListener()

        );

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new MyScrollListener());

        if (recyclerView != null)

        {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        if (NetUtils.isNetworkConnected(mContext))

        {
            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(true);

                        refreshRequest();
                    }
                }, 50);
            }
        } else

        {
            showToastShort("网络不给力");
        }
    }


    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshRequest();
        }
    };


    public class mLoadMoreClickListener implements LoadMoreClickListener {

        @Override
        public void clickLoadMoreData() {

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

        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }

    /*****************
     * 网络请求
     *****************/

    public void refreshRequest() {


        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        adapter.removeFooter(loadMoreView);
        HttpHelper.getInstance().post(mContext, Contants.PortA.Cashcustomerlist, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
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

                System.out.println("response" + json);
                agentsList.clear();
                Customer customer=new Customer();
                customer.setShopname("全部零售商");
                customer.setUseruid("0");
                customer.setUsername("");
                agentsList.add(customer);


                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Customer> jsonHelper = new JsonHelper<Customer>(Customer.class);

                    agentsList.addAll(jsonHelper.getDatas(json));
                    if (agentsList.size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    }


                } else if (JsonHelper.getRequstOK(json) == 6) {
                    adapter.removeFooter(loadMoreView);
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                System.out.println("response" + e.toString());
                agentsList.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

}
