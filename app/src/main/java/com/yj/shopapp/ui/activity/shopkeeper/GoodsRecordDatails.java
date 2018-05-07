package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.OrderRecord;
import com.yj.shopapp.ubeen.ShopHistory;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ShopHistoryAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class GoodsRecordDatails extends BaseActivity {

    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.right_tv)
    ImageView rightTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.shopimag)
    ImageView shopimag;
    @BindView(R.id.shopname)
    TextView shopname;
    @BindView(R.id.shopnum)
    TextView shopnum;
    @BindView(R.id.shopspec)
    TextView shopspec;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    private int CurrentPage = 1;
    private String itemid;
    private OrderRecord orderRecord;
    private List<ShopHistory> shopHistories = new ArrayList<>();
    private ShopHistoryAdpter adpter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_record_datails;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rightTv.setVisibility(View.GONE);
        contentTv.setText("商品记录详情");
        if (getIntent().hasExtra("orderrecord")) {
            orderRecord = getIntent().getParcelableExtra("orderrecord");
        }
        adpter = new ShopHistoryAdpter(mContext);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        myRecyclerView.setAdapter(adpter);
        setData();
        adpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("oid", shopHistories.get(position).getOid());
                CommonUtils.goActivity(GoodsRecordDatails.this, SOrderDatesActivity.class, bundle);
            }
        });
    }

    private void setData() {
        itemid = orderRecord.getItemid();
        Glide.with(mContext).load(orderRecord.getImgurl()).into(shopimag);
        shopname.setText(orderRecord.getName());
        shopnum.setText(String.format("条码：%s", orderRecord.getItemnumber()));
        shopspec.setText(String.format("规格：%1$s/%2$s", orderRecord.getSpecs(), orderRecord.getUnit()));
        if (isNetWork(mContext)) {
            RequstData();
        }
    }

    private void RequstData() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", itemid);
        params.put("p", CurrentPage + "");
        HttpHelper.getInstance().post(mContext, Contants.PortU.SHOWHISTORY, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    shopHistories = JSONArray.parseArray(json, ShopHistory.class);
                    adpter.setList(shopHistories);
                }
            }
        });
    }
}
