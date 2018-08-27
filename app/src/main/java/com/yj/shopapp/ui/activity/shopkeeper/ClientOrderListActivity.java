package com.yj.shopapp.ui.activity.shopkeeper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ClientOrder;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ClientOrderListAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.view.YearPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

public class ClientOrderListActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.date_select)
    ImageView dateSelect;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.month_profit_tv)
    TextView monthProfitTv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    @BindView(R.id.loading)
    LoadingLayout loading;
    private String myUid;
    private int currPage = 1;
    private String yearAndMonth;
    private int mYear, mMonth, mDay;
    private int cYear, cMonth, cDay;
    private ClientOrderListAdpter listAdpter;
    private ClientOrder clientOrder;
    private List<ClientOrder.ListsBean> clientOrderList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_client_order_list;
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
            yearAndMonth = "";
            clientOrderList.clear();
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

    @Override
    protected void initData() {
        idRightBtu.setText("全部");
        timeTv.setText("全部");
        if (getIntent().hasExtra("myUid")) {
            myUid = getIntent().getStringExtra("myUid");
        }
        if (getIntent().hasExtra("userName")) {
            title.setText(getIntent().getStringExtra("userName"));
        }
        Refresh();
        requestData();
        Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
        mYear = dateAndTime.get(Calendar.YEAR);
        mMonth = dateAndTime.get(Calendar.MONTH);
        mDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
        cYear = dateAndTime.get(Calendar.YEAR);
        cMonth = dateAndTime.get(Calendar.MONTH);
        cDay = dateAndTime.get(Calendar.YEAR);
        listAdpter = new ClientOrderListAdpter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        recyclerView.setAdapter(listAdpter);
        listAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("id", clientOrderList.get(position).getId());
                bundle.putString("name", clientOrderList.get(position).getShopname());
                CommonUtils.goActivity(mContext, ClientOrderDetailsActivity.class, bundle);
            }
        });
    }

    private void requestData() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("myuid", myUid);
        params.put("p", String.valueOf(currPage));
        params.put("month", yearAndMonth);
        HttpHelper.getInstance().post(mContext, Contants.PortU.RECOMMEND_PROFITLIST, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (null != loading) {
                    loading.showContent();
                }
                if (JsonHelper.isRequstOK(response, mContext)) {
                    clientOrder = JSONObject.parseObject(response, ClientOrder.class);
                    clientOrderList.addAll(clientOrder.getLists());
                    listAdpter.setList(clientOrderList);
                    if (!clientOrder.getMoney().equals("")) {
                        monthProfitTv.setText(String.format("总收益￥%s", clientOrder.getMoney()));
                    }
                    if (clientOrderList.size() == 0) {
                        if (null != loading) {
                            loading.showEmpty();
                        }
                    } else if (null != smartRefreshLayout && clientOrder.getLists().size() == 0) {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    if (clientOrderList.size() > 0) {
                        if (null != smartRefreshLayout) {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }
                    currPage--;
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (null != smartRefreshLayout) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
            }
        });
    }

    @OnClick({R.id.date_select, R.id.id_right_btu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.date_select:
                //时间选择
                Context themed = new ContextThemeWrapper(mContext,
                        android.R.style.Theme_Holo_Light_Dialog);
                YearPickerDialog datePicker = new YearPickerDialog(themed, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        yearAndMonth = String.format("%d-%02d", year, month + 1);
                        timeTv.setText(String.format("%d-%02d", year, month + 1));
                        currPage = 1;
                        clientOrderList.clear();
                        requestData();
                        if (null != smartRefreshLayout) {
                            smartRefreshLayout.setNoMoreData(false);
                        }
                    }
                }, mYear, mMonth, mDay); //上下文，点击回调,Calendar年月日
                if (!datePicker.isHasNoDay()) {
                    datePicker.setHasNoDay(true);
                }
                datePicker.getDatePicker().init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        cYear = year;
                        cMonth = monthOfYear;
                        cDay = dayOfMonth;
                    }
                });
                datePicker.getDatePicker().setMaxDate((new Date()).getTime());
                datePicker.show();
                break;
            case R.id.id_right_btu:
                currPage = 1;
                timeTv.setText("全部");
                yearAndMonth = "";
                requestData();
                if (null != smartRefreshLayout) {
                    smartRefreshLayout.setNoMoreData(false);
                }
                break;
        }

    }
}

