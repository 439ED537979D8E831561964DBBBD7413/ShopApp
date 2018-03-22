package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.OrderRecord;
import com.yj.shopapp.ui.activity.Interface.OnViewScrollListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.GoodsRecordAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class GoodsRecord extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private List<OrderRecord> orderRecords = new ArrayList<>();
    private GoodsRecordAdpter adpter;
    private int CurrentPage = 1;
    private View FooterView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_record;
    }

    @Override
    protected void initData() {
        title.setText("商品记录");
        swipeRefreshLayout.setOnRefreshListener(this);
        adpter = new GoodsRecordAdpter(mContext);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.addItemDecoration(new DDecoration(mContext));
        myRecyclerView.setAdapter(adpter);
        myRecyclerView.addOnScrollListener(new OnViewScrollListenter() {
            @Override
            public void onBottom() {
                CurrentPage++;
                RequstData();
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        FooterView = LayoutInflater.from(mContext).inflate(R.layout.footerview, null);
        FooterView.setLayoutParams(lp);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtils.isNetworkConnected(mContext)) {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    RequstData();
                }
            }, 200);
        } else {
            showToastShort("网络异常");
        }
    }

    private void RequstData() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("keyword", CurrentPage + "");
        params.put("p", "");
        HttpHelper.getInstance().post(mContext, Contants.PortU.DETAILSHISTORY, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                CurrentPage--;
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                adpter.setList(orderRecords);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if ("".equals(json)) {
                    showToastShort("无数据");
                    if (orderRecords.size() > 0) {
                        CurrentPage--;
                        if (orderRecords.size() > 6) {
                            adpter.setmFooterView(FooterView);
                        }else {
                            adpter.removeFooterView();
                        }
                    }
                    return;
                }
                if (JsonHelper.isRequstOK(json, mContext)) {
                    orderRecords.addAll(JSONArray.parseArray(json, OrderRecord.class));
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        CurrentPage = 1;
        orderRecords.clear();
        RequstData();
    }


}
