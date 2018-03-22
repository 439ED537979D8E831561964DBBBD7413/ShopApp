package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ShopListData;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ShopListAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class ShopListActivity extends BaseActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.xRecyclerView)
    RecyclerView xRecyclerView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;
    private String StordId;
    private String shopId;
    private ShopListData listData;
    private ShopListAdpter adpter;
    private String shopname;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_list;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("Store_id")) {
            StordId = getIntent().getStringExtra("Store_id");
        }
        if (getIntent().hasExtra("shop_id")) {
            shopId = getIntent().getStringExtra("shop_id");
        }
        if (getIntent().hasExtra("titlename")) {
            title.setText(getIntent().getStringExtra("titlename"));
        }
        if (getIntent().hasExtra("shopname")) {
            shopname = getIntent().getStringExtra("shopname");
        }
        adpter = new ShopListAdpter(mContext);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        xRecyclerView.setAdapter(adpter);
        adpter.setOnItemClickListener(this);
        pullToRefresh.setOnRefreshListener(this);
        if (NetUtils.isNetworkConnected(mContext)) {
            pullToRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullToRefresh.setRefreshing(true);
                    onRefresh();
                }
            }, 200);

        } else {
            showToastShort("无网络");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (listData != null) {
            adpter.setList(listData.getData());
        } else {
            if (NetUtils.isNetworkConnected(mContext)) {
                pullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefresh.setRefreshing(true);
                        onRefresh();
                    }
                }, 200);

            } else {
                showToastShort("无网络");
            }
        }
    }

    private void getShopList() {
        final Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        params.put("shop_id", StordId);
        params.put("class_id", shopId);
        HttpHelper.getInstance().post(mContext, Contants.PortU.SHOP_GOODS, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (pullToRefresh != null) {
                    pullToRefresh.setRefreshing(false);
                }

            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (pullToRefresh != null) {
                    pullToRefresh.setRefreshing(false);
                }

            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JSONObject object = JSONObject.parseObject(json);
                    if (object.getInteger("status") == 0) {
                        showToastShort(object.getString("info"));
                    } else {
                        listData = object.toJavaObject(ShopListData.class);
                        adpter.setList(listData.getData());
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("shop_id", listData.getData().get(position).getId());
        bundle.putString("Store_id", StordId);
        bundle.putString("shopname", shopname);
        CommonUtils.goActivity(mContext, CommodityDetails.class, bundle);
    }

    @Override
    public void onRefresh() {
        pullToRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                getShopList();
            }
        }, 300);
    }


}
