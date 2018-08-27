package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.MonthEarning;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.MonthEarningAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

public class IssuedBonusActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bonus_rv)
    RecyclerView bonusRv;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    private List<MonthEarning> monthEarnings = new ArrayList<>();
    private MonthEarningAdpter earningAdpter;
    private int currPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_issued_bonus;
    }

    @Override
    protected void initData() {
        title.setText("每月收益");
        earningAdpter = new MonthEarningAdpter(mContext);
        bonusRv.setLayoutManager(new LinearLayoutManager(mContext));
        bonusRv.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        bonusRv.setAdapter(earningAdpter);
        if (isNetWork(mContext)) {
            requestData();
        }
        Refresh();
        earningAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("month", monthEarnings.get(position).getMonth());
                CommonUtils.goActivity(mContext, NotIssuedBonusActivity.class, bundle);
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setOnRefreshListener(v -> {
            currPage = 1;
            monthEarnings.clear();
            requestData();
            if (null != smartRefreshLayout) {
                smartRefreshLayout.setNoMoreData(false);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(v -> {
            currPage++;
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
        params.put("p", String.valueOf(currPage));
        HttpHelper.getInstance().post(mContext, Contants.PortU.RECOMMEND_MONTHLIST, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (null != loading) {
                    loading.showContent();
                }
                if (JsonHelper.isRequstOK(response, mContext)) {
                    monthEarnings.addAll(JSONObject.parseArray(response, MonthEarning.class));
                    earningAdpter.setList(monthEarnings);
                    if (monthEarnings.size() == 0) {
                        if (null != loading) {
                            loading.showEmpty();
                        }
                    }
                } else {
                    if (monthEarnings.size() > 0) {
                        if (null != smartRefreshLayout) {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    } else {
                        if (null != loading) {
                            loading.showEmpty();
                        }
                    }
                    currPage--;
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (null != smartRefreshLayout) {
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (null != loading) {
                    loading.showError();
                }
            }
        });
    }

}
