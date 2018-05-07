package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.IntegralDetail;
import com.yj.shopapp.ui.activity.Interface.OnDateListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.IntegralAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SIntegralDetailActivity extends BaseActivity implements OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pullToRefresh)
    SmartRefreshLayout pullToRefresh;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String year = "";
    private String month = "";
    private List<IntegralDetail> integralDetails = new ArrayList<>();
    private IntegralAdapter adapter;
    private String startTime = "";
    private String endTime = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sintegral_change;
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
        //设置adapter
        adapter = new IntegralAdapter(mContext);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
            recyclerView.setAdapter(adapter);
        }
        Refresh();
    }

    private void Refresh() {
        pullToRefresh.setHeaderHeight(50);
        pullToRefresh.setFooterHeight(50);
        pullToRefresh.setOnRefreshListener(this);
        pullToRefresh.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        pullToRefresh.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 30);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNetWork(mContext)) {
            getIntegralDetail();
        }
    }

    private void getIntegralDetail() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("start", startTime);
        params.put("end", endTime);
        HttpHelper.getInstance().post(mContext, Contants.PortS.DETAILS, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (pullToRefresh != null) {
                    pullToRefresh.finishRefresh();
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (pullToRefresh != null) {
                    pullToRefresh.finishRefresh(false);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    if (json.charAt(0) == '{') {
                        if (integralDetails.size() == 0) {
                            showToastShort(JSONObject.parseObject(json).getString("info"));
                        }
                    } else {
                        integralDetails = JSONArray.parseArray(json, IntegralDetail.class);
                        adapter.setList(integralDetails);
                    }
                } else {
                    showToastShort(JSONObject.parseObject(json).getString("info"));
                }
            }
        });
    }

    @OnClick(R.id.screenTime)
    public void onViewClicked() {
        new SelectTimeFragmnet().setListenter(new OnDateListenter() {
            @Override
            public void getDate(String starttime, String endtime) {
                startTime = starttime;
                endTime = endtime;
                onResume();
            }
        }).show(getFragmentManager(), "select");
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getIntegralDetail();
    }
}
