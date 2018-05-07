package com.yj.shopapp.ui.activity.shopkeeper;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.gavin.com.library.StickyDecoration;
import com.gavin.com.library.listener.GroupListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.AccountBook;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.AccountBookAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DisplayUtil;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtil;
import com.yj.shopapp.view.headfootrecycleview.RecycleViewEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountBookActivity extends BaseActivity implements OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecycleViewEmpty recyclerView;
    @BindView(R.id.empty_tv)
    TextView emptyTv;
    @BindView(R.id.Cempty_view)
    NestedScrollView CemptyView;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private AccountBookAdpter accountBookAdpter;
    private List<AccountBook> accountBooks = new ArrayList<>();
    private String Month = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account_book;
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
        StickyDecoration decoration = StickyDecoration.Builder
                .init(new GroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        //组名回调
                        if (accountBooks.size() > position) {
                            //获取组名，用于判断是否是同一组
                            return accountBooks.get(position).getMonth();
                        }
                        return "";
                    }
                })
                .setGroupBackground(Color.parseColor("#F4F5F9"))
                .setGroupHeight(CommonUtils.dip2px(this, 36))
                .setDivideColor(Color.parseColor("#e3e3e3"))
                .setGroupTextColor(Color.parseColor("#000000"))
                .setDivideHeight(CommonUtils.dip2px(this, 1))
                .setGroupTextSize(DisplayUtil.sp2px(this, 13))
                .setTextSideMargin(CommonUtils.dip2px(this, 10))
                .build();
        accountBookAdpter = new AccountBookAdpter(mContext);
        Refresh();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(accountBookAdpter);
    }

    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        swipeRefreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        swipeRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 30);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetWork(mContext)) {
            requestData();
        }
    }

    private void requestData() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        params.put("month", Month);
        HttpHelper.getInstance().post(mContext, Contants.PortS.MONTH, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    accountBooks = JSONArray.parseArray(json, AccountBook.class);
                    accountBookAdpter.setList(accountBooks);
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    recyclerView.setEmptyView(emptyTv);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(false);
                }
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (isNetWork(mContext)) {
            requestData();
        }
    }
}
