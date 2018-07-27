package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.mining.app.zxing.MipcaActivityCapture;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.OrderRecord;
import com.yj.shopapp.ubeen.ReCode;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.GoodsRecordAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.view.ClearEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

public class GoodsRecord extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, TextWatcher {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.value_Et)
    ClearEditText valueEt;
    private List<OrderRecord> orderRecords = new ArrayList<>();
    private GoodsRecordAdpter adpter;
    private int CurrentPage = 1;
    private String kw = "";
    private static final int CAMERA_OK = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_record;
    }

    @Override
    protected void initData() {
        title.setText("历史商品记录");
        swipeRefreshLayout.setOnRefreshListener(this);
        adpter = new GoodsRecordAdpter(mContext);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        myRecyclerView.setAdapter(adpter);
        idRightBtu.setBackgroundResource(R.drawable.scan_code);
        Refresh();
        valueEt.addTextChangedListener(this);
        EventBus.getDefault().register(this);
        if (loading != null) {
            loading.showContent();
        }
        adpter.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("orderrecord", orderRecords.get(position));
            CommonUtils.goActivity(GoodsRecord.this, GoodsRecordDatails.class, bundle);
        });
    }

    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setEnableRefresh(false);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadMoreListener(this);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    private void requstHistoricalGoods() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("keyword", kw);
        params.put("p", CurrentPage + "");
        HttpHelper.getInstance().post(mContext, Contants.PortU.DETAILSHISTORY, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(false);
                    swipeRefreshLayout.finishLoadMore(false);
                }
                CurrentPage--;

            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(true);
                    swipeRefreshLayout.finishLoadMore(true);
                    swipeRefreshLayout.setEnableLoadMore(true);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (json.startsWith("\"")) {
                    if (orderRecords.size() > 0) {
                        CurrentPage--;
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    } else {
                        if (loading != null) {
                            loading.showEmpty();
                        }
                    }
                } else {
                    orderRecords.addAll(JSONArray.parseArray(json, OrderRecord.class));
                    adpter.setList(orderRecords);
                    if (loading != null) {
                        loading.showContent();
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReCode msg) {
        if (msg.getStatus() == 2) {
            valueEt.setText(msg.getCode());
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        CurrentPage++;
        requstHistoricalGoods();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        CurrentPage = 1;
        orderRecords.clear();
        kw = "";
        requstHistoricalGoods();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setNoMoreData(false);
        }
    }

//    @Override
//    public void onChildViewClickListener(View view, int position) {
//        switch (view.getId()) {
//            case R.id.itemview:
//                //跳转到详情
//                Bundle bundle = new Bundle();
//                bundle.putString("oid", orderRecords.get(position).getOid());
//                CommonUtils.goActivity(mContext, SOrderDatesActivity.class, bundle);
//                break;
//            case R.id.shopname:
//                //精确搜索
//                kw = orderRecords.get(position).getName();
//                CurrentPage = 1;
//                orderRecords.clear();
//                RequstData();
//                break;
//            case R.id.goto_orderDatails:
//                //跳转到详情
//                Bundle bundle2 = new Bundle();
//                bundle2.putString("oid", orderRecords.get(position).getOid());
//                CommonUtils.goActivity(mContext, SOrderDatesActivity.class, bundle2);
//                break;
//            default:
//                break;
//        }
//    }

    @OnClick(R.id.id_right_btu)
    public void onViewClicked() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(GoodsRecord.this,
                    android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(GoodsRecord.this,
                        new String[]{android.Manifest.permission.CAMERA}, CAMERA_OK);
            } else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
                showDialogCheckGoods();
            }
        } else {
//这个说明系统版本在6.0之下，不需要动态获取权限。
            showDialogCheckGoods();

        }
    }

    private void showDialogCheckGoods() {
        Bundle bundle = new Bundle();
        bundle.putInt(Contants.ScanValueType.KEY, Contants.ScanValueType.S_type);
        bundle.putString("type", "goodsRecord");
        CommonUtils.goActivity(mContext, MipcaActivityCapture.class, bundle);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!"".equals(s.toString())) {
            kw = s.toString().trim();
            orderRecords.clear();
            requstHistoricalGoods();
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    showDialogCheckGoods();
                } else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    showToastShort("请手动打开相机权限");
                }
                break;
        }
    }
}
